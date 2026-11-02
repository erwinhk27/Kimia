package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.chemistry.AiTutorEngine
import com.example.data.chemistry.ChatMessage
import com.example.data.chemistry.CurriculumData
import com.example.data.chemistry.DiagnosticData
import com.example.data.chemistry.PeriodicTableData
import com.example.data.chemistry.TkaQuestionBank
import com.example.data.models.DiagnosticResult
import com.example.data.models.Lesson
import com.example.data.models.PeriodicElement
import com.example.data.models.Question
import com.example.data.models.TkaExamResult
import com.example.data.models.Topic
import com.example.data.models.UserProfile
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUiState(
    val userProfile: UserProfile = UserProfile(),
    val currentTab: String = "dashboard", // "dashboard", "roadmap", "lesson", "diagnostic", "tka", "simulations", "calculators", "ai_tutor", "progress", "teacher"
    val activeTopic: Topic? = null,
    val activeLesson: Lesson? = null,
    val completedLessonIds: Set<String> = emptySet(),
    val diagnosticAnswers: Map<String, Int> = emptyMap(),
    val diagnosticCurrentIndex: Int = 0,
    val diagnosticResult: DiagnosticResult? = null,
    val isDiagnosticRunning: Boolean = false,
    
    // TKA Simulation Exam State
    val isTkaExamRunning: Boolean = false,
    val tkaCurrentIndex: Int = 0,
    val tkaUserAnswers: Map<String, Int> = emptyMap(),
    val tkaFlaggedQuestions: Set<String> = emptySet(),
    val tkaTimeRemainingSeconds: Int = 1800, // 30 minutes for exam
    val tkaResult: TkaExamResult? = null,
    
    // AI Tutor State
    val chatMessages: List<ChatMessage> = listOf(
        ChatMessage(
            sender = "ai",
            text = "Halo! Saya **Chemia AI**, tutor cerdas Kimia SMA & spesialis persiapan TKA Anda. Ada konsep atau soal kimia yang ingin kita bahas bersama hari ini?"
        )
    ),
    val isAiThinking: Boolean = false,
    
    // Periodic Table Explorer
    val selectedElement: PeriodicElement = PeriodicTableData.elements[0],
    val elementSearchQuery: String = "",
    val elementCategoryFilter: String = "Semua",
    
    // Interactive Simulations State
    val simAtomProtons: Int = 6, // Carbon by default
    val simAtomNeutrons: Int = 6,
    val simAtomElectrons: Int = 6,
    
    val simGasPressure: Float = 1.0f, // atm
    val simGasVolume: Float = 22.4f, // L
    val simGasTemp: Float = 273.15f, // K
    val simGasMoles: Float = 1.0f, // mol
    
    val simLeChatelierReactantA: Float = 1.0f,
    val simLeChatelierReactantB: Float = 3.0f,
    val simLeChatelierProductC: Float = 2.0f,
    val simLeChatelierTemp: Float = 300f,
    val simLeChatelierPressure: Float = 1.0f,
    
    val simTitrationVolBasaAdded: Float = 0.0f, // mL 0.1M NaOH added to 25mL 0.1M HCl
    
    // Student Report / Teacher
    val bookmarkedQuestions: Set<String> = emptySet(),
    val currentStreak: Int = 5,
    val totalXp: Int = 1250
)

class ChemistryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private var tkaTimerJob: Job? = null

    init {
        // Set default active topic to first topic
        if (CurriculumData.topics.isNotEmpty()) {
            _uiState.update { it.copy(activeTopic = CurriculumData.topics[0]) }
        }
    }

    fun navigateToTab(tab: String) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun selectTopic(topic: Topic) {
        _uiState.update {
            it.copy(
                activeTopic = topic,
                activeLesson = topic.lessons.firstOrNull(),
                currentTab = "roadmap"
            )
        }
    }

    fun openLesson(topic: Topic, lesson: Lesson) {
        _uiState.update {
            it.copy(
                activeTopic = topic,
                activeLesson = lesson,
                currentTab = "lesson"
            )
        }
    }

    fun markLessonCompleted(lessonId: String) {
        _uiState.update { state ->
            val updated = state.completedLessonIds + lessonId
            val newXp = state.totalXp + 100
            val readiness = (updated.size * 100 / 14).coerceIn(40, 98)
            state.copy(
                completedLessonIds = updated,
                totalXp = newXp,
                userProfile = state.userProfile.copy(
                    xp = newXp,
                    tkaReadinessScore = readiness
                )
            )
        }
    }

    // Diagnostic Test Actions
    fun startDiagnostic() {
        _uiState.update {
            it.copy(
                currentTab = "diagnostic",
                isDiagnosticRunning = true,
                diagnosticCurrentIndex = 0,
                diagnosticAnswers = emptyMap(),
                diagnosticResult = null
            )
        }
    }

    fun answerDiagnosticQuestion(questionId: String, selectedOptionIndex: Int) {
        _uiState.update {
            val updated = it.diagnosticAnswers + (questionId to selectedOptionIndex)
            it.copy(diagnosticAnswers = updated)
        }
    }

    fun nextDiagnosticQuestion() {
        val total = DiagnosticData.diagnosticQuestions.size
        _uiState.update {
            if (it.diagnosticCurrentIndex < total - 1) {
                it.copy(diagnosticCurrentIndex = it.diagnosticCurrentIndex + 1)
            } else {
                it
            }
        }
    }

    fun prevDiagnosticQuestion() {
        _uiState.update {
            if (it.diagnosticCurrentIndex > 0) {
                it.copy(diagnosticCurrentIndex = it.diagnosticCurrentIndex - 1)
            } else {
                it
            }
        }
    }

    fun jumpToDiagnosticQuestion(index: Int) {
        val total = DiagnosticData.diagnosticQuestions.size
        if (index in 0 until total) {
            _uiState.update { it.copy(diagnosticCurrentIndex = index) }
        }
    }

    fun finishDiagnostic() {
        val answers = _uiState.value.diagnosticAnswers
        val result = DiagnosticData.evaluateDiagnostic(answers)
        _uiState.update {
            it.copy(
                isDiagnosticRunning = false,
                diagnosticResult = result,
                userProfile = it.userProfile.copy(
                    hasCompletedDiagnostic = true,
                    tkaReadinessScore = result.overallScore
                ),
                totalXp = it.totalXp + 150
            )
        }
    }

    // TKA Simulation Exam Actions
    fun startTkaExam() {
        tkaTimerJob?.cancel()
        _uiState.update {
            it.copy(
                currentTab = "tka",
                isTkaExamRunning = true,
                tkaCurrentIndex = 0,
                tkaUserAnswers = emptyMap(),
                tkaFlaggedQuestions = emptySet(),
                tkaTimeRemainingSeconds = 1800,
                tkaResult = null
            )
        }
        startTkaTimer()
    }

    private fun startTkaTimer() {
        tkaTimerJob = viewModelScope.launch {
            while (_uiState.value.isTkaExamRunning && _uiState.value.tkaTimeRemainingSeconds > 0) {
                delay(1000)
                _uiState.update {
                    val remaining = it.tkaTimeRemainingSeconds - 1
                    if (remaining <= 0) {
                        it.copy(tkaTimeRemainingSeconds = 0)
                    } else {
                        it.copy(tkaTimeRemainingSeconds = remaining)
                    }
                }
                if (_uiState.value.tkaTimeRemainingSeconds <= 0) {
                    finishTkaExam()
                    break
                }
            }
        }
    }

    fun answerTkaQuestion(questionId: String, optionIndex: Int) {
        _uiState.update {
            it.copy(tkaUserAnswers = it.tkaUserAnswers + (questionId to optionIndex))
        }
    }

    fun toggleFlagTkaQuestion(questionId: String) {
        _uiState.update {
            val flags = it.tkaFlaggedQuestions.toMutableSet()
            if (flags.contains(questionId)) {
                flags.remove(questionId)
            } else {
                flags.add(questionId)
            }
            it.copy(tkaFlaggedQuestions = flags)
        }
    }

    fun jumpToTkaQuestion(index: Int) {
        _uiState.update { it.copy(tkaCurrentIndex = index.coerceIn(0, TkaQuestionBank.tkaQuestions.size - 1)) }
    }

    fun finishTkaExam() {
        tkaTimerJob?.cancel()
        val questions = TkaQuestionBank.tkaQuestions
        val answers = _uiState.value.tkaUserAnswers
        val timeSpent = 1800 - _uiState.value.tkaTimeRemainingSeconds

        var correct = 0
        var wrong = 0
        val topicScores = mutableMapOf<String, Int>()
        val topicTotals = mutableMapOf<String, Int>()

        questions.forEach { q ->
            val userAns = answers[q.id]
            val isCorr = userAns == q.correctAnswerIndex
            if (isCorr) correct++ else wrong++

            topicTotals[q.topicId] = (topicTotals[q.topicId] ?: 0) + 1
            if (isCorr) {
                topicScores[q.topicId] = (topicScores[q.topicId] ?: 0) + 1
            }
        }

        val total = questions.size
        val score = if (total > 0) (correct * 1000) / total else 0 // scale 0-1000 standard TKA UTBK

        val strongest = mutableListOf<String>()
        val weakest = mutableListOf<String>()
        val recommendations = mutableListOf<String>()

        topicTotals.forEach { (topId, tot) ->
            val corr = topicScores[topId] ?: 0
            val pct = (corr * 100) / tot
            if (pct >= 80) {
                strongest.add("$topId ($pct%)")
            } else {
                weakest.add("$topId ($pct%)")
                recommendations.add(topId)
            }
        }

        val examResult = TkaExamResult(
            id = java.util.UUID.randomUUID().toString(),
            totalQuestions = total,
            correctCount = correct,
            wrongCount = wrong,
            score = score,
            timeSpentSeconds = timeSpent,
            topicScores = topicScores,
            strongestTopics = strongest,
            weakestTopics = weakest,
            recommendedTopics = recommendations,
            userAnswers = answers
        )

        _uiState.update {
            it.copy(
                isTkaExamRunning = false,
                tkaResult = examResult,
                totalXp = it.totalXp + 250,
                userProfile = it.userProfile.copy(
                    tkaReadinessScore = (score / 10).coerceIn(30, 99)
                )
            )
        }
    }

    // AI Tutor Actions
    fun sendAiPrompt(prompt: String, mode: String = "socratic") {
        if (prompt.isBlank()) return

        val userMessage = ChatMessage(sender = "user", text = prompt, socraticMode = mode)
        _uiState.update {
            it.copy(
                chatMessages = it.chatMessages + userMessage,
                isAiThinking = true
            )
        }

        viewModelScope.launch {
            val (responseText, followUps) = AiTutorEngine.getTutorResponse(_uiState.value.chatMessages, prompt, mode)
            val aiMessage = ChatMessage(
                sender = "ai",
                text = responseText,
                socraticMode = mode,
                suggestedFollowUps = followUps
            )
            _uiState.update {
                it.copy(
                    chatMessages = it.chatMessages + aiMessage,
                    isAiThinking = false
                )
            }
        }
    }

    fun clearAiChat() {
        _uiState.update {
            it.copy(
                chatMessages = listOf(
                    ChatMessage(
                        sender = "ai",
                        text = "Halo! Saya **CHEMIA AI**, tutor cerdas Kimia SMA & spesialis persiapan TKA Anda. Silakan pilih mode bimbingan (Socratic, Clue, Analogi, Perhitungan, atau Diagram) dan tanyakan konsep/soal apa pun!"
                    )
                )
            )
        }
    }

    // Periodic Table
    fun selectElement(element: PeriodicElement) {
        _uiState.update { it.copy(selectedElement = element) }
    }

    fun setElementSearch(query: String) {
        _uiState.update { it.copy(elementSearchQuery = query) }
    }

    fun setElementFilter(category: String) {
        _uiState.update { it.copy(elementCategoryFilter = category) }
    }

    // Simulations
    fun updateAtomSubatomic(protons: Int, neutrons: Int, electrons: Int) {
        _uiState.update {
            it.copy(
                simAtomProtons = protons.coerceIn(1, 20),
                simAtomNeutrons = neutrons.coerceIn(0, 24),
                simAtomElectrons = electrons.coerceIn(1, 20)
            )
        }
    }

    fun updateGasVariables(pressure: Float? = null, volume: Float? = null, temp: Float? = null, moles: Float? = null) {
        _uiState.update { state ->
            val p = pressure ?: state.simGasPressure
            val v = volume ?: state.simGasVolume
            val t = temp ?: state.simGasTemp
            val n = moles ?: state.simGasMoles
            state.copy(
                simGasPressure = p,
                simGasVolume = v,
                simGasTemp = t,
                simGasMoles = n
            )
        }
    }

    fun updateTitrationVolume(volumeBasaMl: Float) {
        _uiState.update { it.copy(simTitrationVolBasaAdded = volumeBasaMl.coerceIn(0f, 50f)) }
    }

    fun toggleBookmark(questionId: String) {
        _uiState.update {
            val bookmarks = it.bookmarkedQuestions.toMutableSet()
            if (bookmarks.contains(questionId)) bookmarks.remove(questionId) else bookmarks.add(questionId)
            it.copy(bookmarkedQuestions = bookmarks)
        }
    }
}
