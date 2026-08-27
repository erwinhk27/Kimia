package com.example.data.models

enum class QuestionDifficulty {
    LEVEL_1_RECALL,
    LEVEL_2_UNDERSTANDING,
    LEVEL_3_APPLICATION,
    LEVEL_4_ANALYSIS,
    LEVEL_5_EVALUATION,
    LEVEL_6_TKA_CHALLENGE
}

enum class ErrorType(val label: String) {
    CONCEPTUAL("Kesalahan Konseptual (Conceptual Error)"),
    CALCULATION("Kesalahan Hitung (Calculation Error)"),
    UNIT("Kesalahan Satuan (Unit Error)"),
    READING("Kurang Teliti Membaca Soal (Reading Error)"),
    CARELESS("Kecerobohan Notasi/Tanda (Careless Mistake)"),
    REASONING("Kesalahan Penalaran Logis (Reasoning Error)")
}

data class Question(
    val id: String,
    val topicId: String,
    val difficulty: QuestionDifficulty,
    val questionType: String = "Pilihan Ganda",
    val contextText: String? = null,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String,
    val whyExplanation: String = "",
    val conceptTested: String = "",
    val errorType: ErrorType = ErrorType.CONCEPTUAL,
    val rightThinkingMethod: String = "",
    val similarPracticeHint: String = ""
)

data class WorkedExample(
    val title: String,
    val problem: String,
    val strategy: String,
    val calculation: String,
    val answer: String,
    val explanation: String
)

data class CommonMistake(
    val mistake: String,
    val correctConcept: String,
    val explanation: String
)

data class GuidedStep(
    val stepNumber: Int,
    val instruction: String,
    val hint: String,
    val solution: String
)

data class GuidedPractice(
    val problem: String,
    val steps: List<GuidedStep>,
    val finalAnswer: String
)

data class VisualCard(
    val title: String,
    val formulaOrSymbol: String,
    val explanation: String,
    val category: String = "Konsep Visual"
)

data class Lesson(
    val id: String,
    val topicId: String,
    val title: String,
    val subtitle: String,
    val durationMinutes: Int = 15,
    val learningObjectives: List<String>,
    val preTest: List<Question> = emptyList(),
    val conceptExplanation: String,
    val visualCards: List<VisualCard> = emptyList(),
    val workedExamples: List<WorkedExample> = emptyList(),
    val commonMistakes: List<CommonMistake> = emptyList(),
    val guidedPractices: List<GuidedPractice> = emptyList(),
    val independentPractices: List<Question> = emptyList(),
    val hotsChallenge: Question? = null,
    val exitTest: List<Question> = emptyList()
)

data class Topic(
    val id: String,
    val levelNumber: Int,
    val levelName: String,
    val title: String,
    val description: String,
    val prerequisiteTopicIds: List<String> = emptyList(),
    val learningObjectives: List<String>,
    val lessons: List<Lesson> = emptyList(),
    val iconName: String = "science"
)

data class DiagnosticResult(
    val timestamp: Long = System.currentTimeMillis(),
    val overallScore: Int,
    val foundationScore: Int,
    val chemistryScore: Int,
    val topicScores: Map<String, Int>,
    val strengths: List<String>,
    val weaknesses: List<String>,
    val misconceptions: List<String>,
    val recommendedTopicIds: List<String>,
    val recommendationSummary: String
)

data class TkaExamResult(
    val id: String,
    val timestamp: Long = System.currentTimeMillis(),
    val totalQuestions: Int,
    val correctCount: Int,
    val wrongCount: Int,
    val score: Int,
    val timeSpentSeconds: Int,
    val topicScores: Map<String, Int>,
    val strongestTopics: List<String>,
    val weakestTopics: List<String>,
    val recommendedTopics: List<String>,
    val userAnswers: Map<String, Int> // questionId -> selectedIndex
)

data class PeriodicElement(
    val atomicNumber: Int,
    val symbol: String,
    val name: String,
    val indonesianName: String,
    val atomicMass: Double,
    val group: Int,
    val period: Int,
    val category: String, // Alkali, Alkali Tanah, Transisi, Nonlogam, Halogen, Gas Mulia, dll.
    val electronConfig: String,
    val oxidationStates: String,
    val electronegativity: Double,
    val ionizationEnergyKj: Double,
    val atomicRadiusPm: Int,
    val summary: String,
    val reactivityTrend: String,
    val funFact: String,
    val colorHex: Long
)

data class DailyChallenge(
    val id: String,
    val date: String,
    val conceptTitle: String,
    val conceptSummary: String,
    val question: Question,
    val reasoningChallenge: String,
    val realWorldContext: String
)

data class AchievementBadge(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null
)

data class UserProfile(
    val name: String = "Siswa Kimia SMA",
    val grade: String = "Kelas XII",
    val targetExam: String = "TKA Saintek Kimia (UTBK / PTN Mandiri)",
    val xp: Int = 1250,
    val currentStreak: Int = 5,
    val tkaReadinessScore: Int = 76,
    val hasCompletedDiagnostic: Boolean = false
)
