package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.chemistry.CurriculumData
import com.example.ui.components.ChemistryNavBar
import com.example.ui.components.ChemistryTopBar
import com.example.ui.screens.AiTutorScreen
import com.example.ui.screens.CalculatorsScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DiagnosticScreen
import com.example.ui.screens.LessonScreen
import com.example.ui.screens.ProgressReportScreen
import com.example.ui.screens.RoadmapScreen
import com.example.ui.screens.SimulationsScreen
import com.example.ui.screens.TkaSimulationScreen
import com.example.ui.theme.ChemNavyDark
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ChemistryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                KimiaMasterApp()
            }
        }
    }
}

@Composable
fun KimiaMasterApp(viewModel: ChemistryViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(ChemNavyDark),
        topBar = {
            ChemistryTopBar(
                xp = uiState.totalXp,
                streak = uiState.currentStreak,
                readinessScore = uiState.userProfile.tkaReadinessScore,
                onOpenAiTutor = { viewModel.navigateToTab("ai_tutor") }
            )
        },
        bottomBar = {
            // Hide bottom bar only during full TKA live exam to avoid distraction
            if (!uiState.isTkaExamRunning) {
                ChemistryNavBar(
                    currentTab = uiState.currentTab,
                    onTabSelected = { tabId -> viewModel.navigateToTab(tabId) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(ChemNavyDark)
        ) {
            when (uiState.currentTab) {
                "dashboard" -> DashboardScreen(
                    uiState = uiState,
                    onNavigateToTab = { viewModel.navigateToTab(it) },
                    onStartDiagnostic = { viewModel.startDiagnostic() },
                    onStartTkaExam = { viewModel.startTkaExam() },
                    onOpenTopic = { topicId ->
                        val topic = CurriculumData.topics.find { it.id == topicId }
                        if (topic != null) {
                            val lesson = topic.lessons.firstOrNull()
                            if (lesson != null) {
                                viewModel.openLesson(topic, lesson)
                            } else {
                                viewModel.selectTopic(topic)
                            }
                        }
                    }
                )

                "roadmap" -> RoadmapScreen(
                    uiState = uiState,
                    onSelectTopicLesson = { topic, lesson ->
                        viewModel.openLesson(topic, lesson)
                    }
                )

                "lesson" -> LessonScreen(
                    topic = uiState.activeTopic,
                    lesson = uiState.activeLesson,
                    onBackToRoadmap = { viewModel.navigateToTab("roadmap") },
                    onCompleteLesson = { lessonId ->
                        viewModel.markLessonCompleted(lessonId)
                    }
                )

                "diagnostic" -> DiagnosticScreen(
                    uiState = uiState,
                    onAnswerQuestion = { qId, ans -> viewModel.answerDiagnosticQuestion(qId, ans) },
                    onNextQuestion = { viewModel.nextDiagnosticQuestion() },
                    onPrevQuestion = { viewModel.prevDiagnosticQuestion() },
                    onFinishDiagnostic = { viewModel.finishDiagnostic() },
                    onStartDiagnostic = { viewModel.startDiagnostic() },
                    onNavigateToRoadmap = { viewModel.navigateToTab("roadmap") }
                )

                "tka" -> TkaSimulationScreen(
                    uiState = uiState,
                    onStartExam = { viewModel.startTkaExam() },
                    onAnswerQuestion = { qId, ans -> viewModel.answerTkaQuestion(qId, ans) },
                    onToggleFlag = { qId -> viewModel.toggleFlagTkaQuestion(qId) },
                    onJumpToQuestion = { idx -> viewModel.jumpToTkaQuestion(idx) },
                    onFinishExam = { viewModel.finishTkaExam() }
                )

                "simulations" -> SimulationsScreen(
                    uiState = uiState,
                    onSelectElement = { viewModel.selectElement(it) },
                    onSearchElement = { viewModel.setElementSearch(it) },
                    onFilterElementCategory = { viewModel.setElementFilter(it) },
                    onUpdateAtomSubatomic = { p, n, e -> viewModel.updateAtomSubatomic(p, n, e) },
                    onUpdateGas = { p, v, t, n -> viewModel.updateGasVariables(p, v, t, n) },
                    onUpdateTitration = { v -> viewModel.updateTitrationVolume(v) }
                )

                "calculators" -> CalculatorsScreen()

                "ai_tutor" -> AiTutorScreen(
                    uiState = uiState,
                    onSendMessage = { viewModel.sendAiPrompt(it) }
                )

                "progress" -> ProgressReportScreen(
                    uiState = uiState,
                    onNavigateToRoadmap = { viewModel.navigateToTab("roadmap") }
                )

                else -> DashboardScreen(
                    uiState = uiState,
                    onNavigateToTab = { viewModel.navigateToTab(it) },
                    onStartDiagnostic = { viewModel.startDiagnostic() },
                    onStartTkaExam = { viewModel.startTkaExam() },
                    onOpenTopic = { viewModel.navigateToTab("roadmap") }
                )
            }
        }
    }
}
