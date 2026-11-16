package com.Rirsefu3.Degmed.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Rirsefu3.Degmed.core.config.GameConfig
import com.Rirsefu3.Degmed.domain.model.BoardState
import com.Rirsefu3.Degmed.domain.model.LevelDefinition
import com.Rirsefu3.Degmed.domain.model.RoundOutcome
import com.Rirsefu3.Degmed.domain.model.RoundResult
import com.Rirsefu3.Degmed.domain.repository.LevelRepository
import com.Rirsefu3.Degmed.domain.repository.ProgressRepository
import com.Rirsefu3.Degmed.domain.repository.SettingsRepository
import com.Rirsefu3.Degmed.domain.usecase.EvaluateBoardUseCase
import com.Rirsefu3.Degmed.domain.usecase.ScoreRoundUseCase
import com.Rirsefu3.Degmed.domain.usecase.SpinColumnUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class GameViewModel(
    private val levelId: Int,
    private val attempt: Int,
    private val levels: LevelRepository,
    private val progress: ProgressRepository,
    private val settings: SettingsRepository,
    private val spinColumn: SpinColumnUseCase,
    private val evaluateBoard: EvaluateBoardUseCase,
    private val scoreRound: ScoreRoundUseCase
) : ViewModel() {

    private data class Snapshot(
        val board: BoardState,
        val movesLeft: Int,
        val drift: Int,
        val proximity: Int,
        val frozenRow: Int,
        val freezePlaced: Boolean
    )

    private val level: LevelDefinition = levels.level(levelId, attempt)
    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<RoundResult>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var completed: Set<Int> = emptySet()
    private var proximity: Int = 0
    private var chainStep: Int = -1
    private var bestChain: Float = 1.0f
    private var lastCompletionMove: Int = -99
    private var freezePlaced: Boolean = false
    private var snapshot: Snapshot? = null
    private var pendingJob: Job? = null

    init {
        val evaluation = evaluateBoard(level.board, level.objectives, emptySet())
        completed = evaluation.completedIndices
        proximity = evaluation.proximity
        _state.value = GameUiState(
            levelTitleRes = level.titleRes,
            expert = level.expert,
            seed = levelId * 7919 xor (attempt * 104729),
            board = level.board,
            objectives = evaluation.progress,
            movesLeft = level.moveLimit,
            moveLimit = level.moveLimit,
            driftLimit = level.driftLimit,
            rewindAvailable = level.allowRewind,
            selectedColumn = 0,
            settings = settings.settings.value
        )
        viewModelScope.launch {
            settings.settings.collect { value ->
                _state.value = _state.value.copy(settings = value)
            }
        }
    }

    fun onCellTap(column: Int, row: Int) {
        val current = _state.value
        if (current.phase != GamePhase.IDLE || current.paused) return
        if (current.freezePickMode) {
            applyFreeze(row)
        } else {
            _state.value = current.copy(selectedColumn = column)
        }
    }

    fun onFreezeArmed() {
        val current = _state.value
        if (current.phase != GamePhase.IDLE || current.paused) return
        _state.value = current.copy(freezePickMode = !current.freezePickMode)
    }

    private fun applyFreeze(row: Int) {
        val current = _state.value
        val unfreezing = current.frozenRow == row
        val cost = if (freezePlaced) 1 else 0
        if (cost > current.movesLeft) {
            _state.value = current.copy(freezePickMode = false)
            return
        }
        freezePlaced = true
        val nextRow = if (unfreezing) BoardState.NO_FROZEN_ROW else row
        _state.value = current.copy(
            frozenRow = nextRow,
            freezePickMode = false,
            movesLeft = current.movesLeft - cost
        )
        if (current.movesLeft - cost <= 0) finishRound(RoundOutcome.OUT_OF_MOVES)
    }

    fun onRewind() {
        val current = _state.value
        val saved = snapshot ?: return
        if (current.phase != GamePhase.IDLE || current.paused || !current.rewindAvailable) return
        snapshot = null
        proximity = saved.proximity
        freezePlaced = saved.freezePlaced
        val evaluation = evaluateBoard(saved.board, level.objectives, completed)
        _state.value = current.copy(
            board = saved.board,
            movesLeft = saved.movesLeft,
            drift = saved.drift,
            frozenRow = saved.frozenRow,
            objectives = evaluation.progress,
            rewindAvailable = false,
            selectedColumn = GameUiState.NO_COLUMN,
            freezePickMode = false
        )
    }

    fun onSpin() {
        val current = _state.value
        if (!current.canSpin) {
            _state.value = current.copy(shakeTick = current.shakeTick + 1)
            return
        }
        snapshot = Snapshot(
            board = current.board,
            movesLeft = current.movesLeft,
            drift = current.drift,
            proximity = proximity,
            frozenRow = current.frozenRow,
            freezePlaced = freezePlaced
        )

        val column = current.selectedColumn
        val nextBoard = spinColumn(current.board, column, current.frozenRow)
        val movesLeft = current.movesLeft - 1
        val movesUsed = current.moveLimit - movesLeft
        val evaluation = evaluateBoard(nextBoard, level.objectives, completed)
        val newlyCompleted = evaluation.completedIndices - completed
        completed = evaluation.completedIndices

        var spark = current.spark
        newlyCompleted.sorted().forEach { index ->
            chainStep = if (movesUsed - lastCompletionMove <= GameConfig.CHAIN_WINDOW_MOVES) {
                chainStep + 1
            } else {
                0
            }
            spark += scoreRound.objectiveSpark(level.objectives[index], chainStep)
            val multiplier = scoreRound.chainMultiplier(chainStep)
            if (multiplier > bestChain) bestChain = multiplier
            lastCompletionMove = movesUsed
        }

        val progressed = newlyCompleted.isNotEmpty() || evaluation.proximity > proximity
        proximity = evaluation.proximity
        val drift = if (progressed) 0 else current.drift + 1

        _state.value = current.copy(
            board = nextBoard,
            objectives = evaluation.progress,
            movesLeft = movesLeft,
            spark = spark,
            drift = drift,
            phase = GamePhase.SPINNING,
            spinTick = current.spinTick + 1,
            spinningColumn = column,
            shakeTick = if (progressed) current.shakeTick else current.shakeTick + 1
        )

        pendingJob?.cancel()
        pendingJob = viewModelScope.launch {
            delay(GameConfig.SPIN_ANIM_MS)
            _state.value = _state.value.copy(phase = GamePhase.EVALUATING)
            delay(GameConfig.EVALUATE_MS)
            val outcome = resolveOutcome()
            if (outcome == null) {
                _state.value = _state.value.copy(phase = GamePhase.IDLE)
            } else {
                finishRound(outcome)
            }
        }
    }

    private fun resolveOutcome(): RoundOutcome? {
        val current = _state.value
        return when {
            completed.size >= level.objectives.size -> RoundOutcome.ALIGNED
            current.drift >= current.driftLimit -> RoundOutcome.SIGNAL_LOST
            current.movesLeft <= 0 -> RoundOutcome.OUT_OF_MOVES
            else -> null
        }
    }

    private fun finishRound(outcome: RoundOutcome) {
        val current = _state.value
        if (current.phase == GamePhase.FINISHED) return
        val won = outcome == RoundOutcome.ALIGNED
        val bonus = if (won) scoreRound.movesBonus(current.movesLeft) else 0
        val totalSpark = current.spark + bonus
        val stars = scoreRound.stars(current.movesLeft, current.moveLimit, won)
        val newBest = progress.recordRound(level.id, stars, totalSpark)
        _state.value = current.copy(phase = GamePhase.FINISHED, spark = totalSpark)
        viewModelScope.launch {
            _events.send(
                RoundResult(
                    levelId = level.id,
                    titleRes = level.titleRes,
                    attempt = attempt,
                    outcome = outcome,
                    spark = totalSpark,
                    stars = stars,
                    movesLeft = current.movesLeft,
                    goalsDone = current.goalsDone,
                    goalsTotal = current.goalsTotal,
                    bestChain = bestChain,
                    expert = level.expert,
                    nextLevelId = levels.nextLevelId(level.id),
                    newBest = newBest
                )
            )
        }
    }

    fun onPauseRequested() {
        if (_state.value.phase == GamePhase.FINISHED) return
        pendingJob?.cancel()
        _state.value = _state.value.copy(paused = true, phase = GamePhase.IDLE)
    }

    fun onResume() {
        _state.value = _state.value.copy(paused = false)
        val outcome = resolveOutcome()
        if (outcome != null) finishRound(outcome)
    }

    fun pause() {
        pendingJob?.cancel()
        pendingJob = null
    }

    override fun onCleared() {
        pendingJob?.cancel()
        super.onCleared()
    }
}
