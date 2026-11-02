package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.chemistry.DiagnosticData
import com.example.data.models.DiagnosticResult
import com.example.data.models.Question
import com.example.data.models.QuestionDifficulty
import com.example.ui.theme.ChemAmber
import com.example.ui.theme.ChemAmberBg
import com.example.ui.theme.ChemBgLight
import com.example.ui.theme.ChemBorder
import com.example.ui.theme.ChemCyan
import com.example.ui.theme.ChemCyanBg
import com.example.ui.theme.ChemEmerald
import com.example.ui.theme.ChemEmeraldBg
import com.example.ui.theme.ChemIndigo
import com.example.ui.theme.ChemIndigoBorder
import com.example.ui.theme.ChemIndigoSubtle
import com.example.ui.theme.ChemMidnight
import com.example.ui.theme.ChemPurple
import com.example.ui.theme.ChemPurpleBg
import com.example.ui.theme.ChemRose
import com.example.ui.theme.ChemRoseBg
import com.example.ui.theme.ChemSky
import com.example.ui.theme.ChemSkyBg
import com.example.ui.theme.ChemSurfaceSubtle
import com.example.ui.theme.ChemTextDark
import com.example.ui.theme.ChemTextMuted
import com.example.ui.theme.ChemTextPrimary
import com.example.ui.theme.ChemTextSecondary
import com.example.ui.theme.ChemWhite
import com.example.viewmodel.MainUiState

/**
 * DiagnosticScreen provides an interactive, comprehensive diagnostic evaluation
 * for high school chemistry students preparing for TKA Saintek / UTBK.
 * It features:
 * - Diagnostic Overview & Introduction
 * - Interactive Question Navigator & Multiple-Choice answering
 * - Multi-topic proficiency tracking matrix (Mastered, Intermediate, Needs Review)
 * - Deep misconception detection & personalized study recommendations
 */
@Composable
fun DiagnosticScreen(
    uiState: MainUiState,
    onAnswerQuestion: (String, Int) -> Unit,
    onNextQuestion: () -> Unit,
    onPrevQuestion: () -> Unit,
    onFinishDiagnostic: () -> Unit,
    onStartDiagnostic: () -> Unit,
    onNavigateToRoadmap: () -> Unit,
    onJumpToQuestion: (Int) -> Unit = {},
    onNavigateToAiTutor: () -> Unit = {}
) {
    val questions = DiagnosticData.diagnosticQuestions
    val currentIndex = uiState.diagnosticCurrentIndex
    val result = uiState.diagnosticResult

    // If result exists and not running, show Comprehensive Diagnostic Report
    if (result != null && !uiState.isDiagnosticRunning) {
        DiagnosticReportView(
            result = result,
            onRetake = onStartDiagnostic,
            onGoToRoadmap = onNavigateToRoadmap,
            onAskAiTutor = onNavigateToAiTutor
        )
        return
    }

    // If not running and no result, show Landing/Start Screen
    if (!uiState.isDiagnosticRunning) {
        DiagnosticLandingView(
            onStart = onStartDiagnostic
        )
        return
    }

    // Diagnostic Questions in Progress
    val safeIndex = currentIndex.coerceIn(0, (questions.size - 1).coerceAtLeast(0))
    val currentQuestion = questions.getOrElse(safeIndex) { questions.first() }
    val selectedOption = uiState.diagnosticAnswers[currentQuestion.id]
    val answeredCount = uiState.diagnosticAnswers.size
    val isLast = safeIndex == questions.size - 1
    var showConfirmFinishDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ChemBgLight)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("diagnostic_running_screen")
    ) {
        // TOP HEADER BAR: Live progress & badges
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Tes Diagnostik Mandiri",
                    color = ChemTextDark,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$answeredCount dari ${questions.size} soal terjawab",
                    color = ChemTextSecondary,
                    fontSize = 12.sp
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = ChemIndigoSubtle,
                border = BorderStroke(1.dp, ChemIndigoBorder)
            ) {
                Text(
                    text = "Soal ${safeIndex + 1} / ${questions.size}",
                    color = ChemIndigo,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Progress Bar
        val progressVal = (answeredCount.toFloat() / questions.size.toFloat()).coerceIn(0f, 1f)
        LinearProgressIndicator(
            progress = { progressVal },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = ChemIndigo,
            trackColor = ChemIndigoBorder
        )

        Spacer(modifier = Modifier.height(12.dp))

        // QUESTION NAVIGATOR PILL STRIP (1..N)
        QuestionNavigatorStrip(
            totalQuestions = questions.size,
            currentIndex = safeIndex,
            answers = uiState.diagnosticAnswers,
            questions = questions,
            onSelectQuestion = onJumpToQuestion
        )

        Spacer(modifier = Modifier.height(12.dp))

        // MAIN QUESTION CARD (with Topic & Difficulty Header)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = ChemWhite),
            border = BorderStroke(1.dp, ChemBorder)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Topic & Difficulty metadata tags
                item {
                    val topicInfo = getTopicMetadata(currentQuestion.topicId)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = topicInfo.bgColor,
                            border = BorderStroke(1.dp, topicInfo.tintColor.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = topicInfo.icon,
                                    contentDescription = null,
                                    tint = topicInfo.tintColor,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = topicInfo.name,
                                    color = topicInfo.tintColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        val diffLabel = when (currentQuestion.difficulty) {
                            QuestionDifficulty.LEVEL_1_RECALL -> "Level 1: Ingatan"
                            QuestionDifficulty.LEVEL_2_UNDERSTANDING -> "Level 2: Pemahaman"
                            QuestionDifficulty.LEVEL_3_APPLICATION -> "Level 3: Aplikasi"
                            QuestionDifficulty.LEVEL_4_ANALYSIS -> "Level 4: Penalaran HOTS"
                            QuestionDifficulty.LEVEL_5_EVALUATION -> "Level 5: Evaluasi"
                            QuestionDifficulty.LEVEL_6_TKA_CHALLENGE -> "Level 6: UTBK TKA"
                        }

                        Text(
                            text = diffLabel,
                            color = ChemTextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (currentQuestion.conceptTested.isNotEmpty()) {
                        Text(
                            text = "Konsep Diuji: ${currentQuestion.conceptTested}",
                            color = ChemIndigo,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    // Question prompt
                    Text(
                        text = currentQuestion.question,
                        color = ChemTextDark,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 23.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))
                }

                // Multiple-Choice Options
                itemsIndexed(currentQuestion.options) { optIndex, optText ->
                    val isSelected = selectedOption == optIndex
                    val optionBgColor by animateColorAsState(
                        targetValue = if (isSelected) ChemIndigoSubtle else ChemSurfaceSubtle,
                        animationSpec = tween(durationMillis = 200),
                        label = "opt_bg"
                    )
                    val optionBorderColor by animateColorAsState(
                        targetValue = if (isSelected) ChemIndigo else ChemBorder,
                        animationSpec = tween(durationMillis = 200),
                        label = "opt_border"
                    )

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .heightIn(min = 52.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                onAnswerQuestion(currentQuestion.id, optIndex)
                            }
                            .testTag("diag_opt_${optIndex}"),
                        color = optionBgColor,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, optionBorderColor)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) ChemIndigo else ChemWhite)
                                    .border(
                                        1.dp,
                                        if (isSelected) ChemIndigo else ChemBorder,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = ChemWhite,
                                        modifier = Modifier.size(16.dp)
                                    )
                                } else {
                                    Text(
                                        text = "${('A' + optIndex)}",
                                        color = ChemTextSecondary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = optText,
                                color = if (isSelected) ChemIndigo else ChemTextDark,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                modifier = Modifier.weight(1f),
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // NAVIGATION BOTTOM FOOTER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onPrevQuestion,
                enabled = safeIndex > 0,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, if (safeIndex > 0) ChemBorder else ChemBorder.copy(alpha = 0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = ChemTextDark,
                    disabledContentColor = ChemTextMuted
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Sebelumnya", fontSize = 13.sp)
            }

            if (isLast || answeredCount == questions.size) {
                Button(
                    onClick = {
                        if (answeredCount < questions.size) {
                            showConfirmFinishDialog = true
                        } else {
                            onFinishDiagnostic()
                        }
                    },
                    modifier = Modifier
                        .weight(1.3f)
                        .height(48.dp)
                        .testTag("btn_finish_diag"),
                    colors = ButtonDefaults.buttonColors(containerColor = ChemEmerald),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = ChemWhite,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Selesai & Analisis",
                        color = ChemWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            } else {
                Button(
                    onClick = onNextQuestion,
                    modifier = Modifier
                        .weight(1.3f)
                        .height(48.dp)
                        .testTag("btn_next_diag"),
                    colors = ButtonDefaults.buttonColors(containerColor = ChemIndigo),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Selanjutnya",
                        color = ChemWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = ChemWhite,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }

    // Confirmation dialog if submitting before all questions answered
    if (showConfirmFinishDialog) {
        val unansweredCount = questions.size - answeredCount
        AlertDialog(
            onDismissRequest = { showConfirmFinishDialog = false },
            title = {
                Text(text = "Selesaikan Tes Diagnostik?", fontWeight = FontWeight.Bold)
            },
            text = {
                Text("Masih terdapat $unansweredCount soal yang belum Anda jawab. Soal yang belum terjawab akan dihitung sebagai 0. Apakah Anda yakin ingin menyelesaikan sekarang?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmFinishDialog = false
                        onFinishDiagnostic()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ChemEmerald)
                ) {
                    Text("Ya, Selesaikan", color = ChemWhite)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmFinishDialog = false }) {
                    Text("Periksa Lagi", color = ChemIndigo)
                }
            }
        )
    }
}

/**
 * Question Navigator Strip: horizontal scrollable pills showing question status
 */
@Composable
fun QuestionNavigatorStrip(
    totalQuestions: Int,
    currentIndex: Int,
    answers: Map<String, Int>,
    questions: List<Question>,
    onSelectQuestion: (Int) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ChemWhite)
            .border(1.dp, ChemBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until totalQuestions) {
            val q = questions.getOrNull(i)
            val isAnswered = q != null && answers.containsKey(q.id)
            val isCurrent = i == currentIndex

            val bgColor = when {
                isCurrent -> ChemIndigo
                isAnswered -> ChemEmeraldBg
                else -> ChemSurfaceSubtle
            }

            val textColor = when {
                isCurrent -> ChemWhite
                isAnswered -> ChemEmerald
                else -> ChemTextSecondary
            }

            val borderColor = when {
                isCurrent -> ChemIndigo
                isAnswered -> ChemEmerald
                else -> ChemBorder
            }

            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(bgColor)
                    .border(1.dp, borderColor, CircleShape)
                    .clickable { onSelectQuestion(i) }
                    .testTag("nav_question_pill_$i"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${i + 1}",
                    color = textColor,
                    fontSize = 12.sp,
                    fontWeight = if (isCurrent || isAnswered) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

/**
 * Landing View: Information, instructions, and target topics preview
 */
@Composable
fun DiagnosticLandingView(onStart: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ChemBgLight)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ChemWhite),
                border = BorderStroke(1.dp, ChemBorder)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(ChemIndigoSubtle)
                            .border(1.dp, ChemIndigoBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = ChemIndigo,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Tes Diagnostik Mandiri Kimia",
                        color = ChemTextDark,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Petakan tingkat kemahiran Anda pada 10 topik esensial Kimia SMA & TKA Saintek. Sistem secara presisi mengidentifikasi miskonsepsi dan menyusun jalur belajar personal.",
                        color = ChemTextSecondary,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Feature highlights row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DiagnosticFeatureTag(
                            icon = Icons.Default.Assessment,
                            label = "12 Soal Terarah",
                            modifier = Modifier.weight(1f)
                        )
                        DiagnosticFeatureTag(
                            icon = Icons.Default.Science,
                            label = "10 Topik Diuji",
                            modifier = Modifier.weight(1f)
                        )
                        DiagnosticFeatureTag(
                            icon = Icons.Default.AutoAwesome,
                            label = "Rekomendasi Pintar",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Topics Tested Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ChemWhite),
                border = BorderStroke(1.dp, ChemBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Topik Kimia yang Dievaluasi:",
                        color = ChemTextDark,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val topicsList = listOf(
                        "Fondasi Matematika & Notasi Ilmiah Kimia",
                        "Struktur Atom, Isotop & Notasi Nuklida",
                        "Tabel Periodik & Sifat Keperiodikan",
                        "Ikatan Kimia (Ionik & Kovalen)",
                        "Konsep Mol, Massa Molar & Perhitungan Zat",
                        "Stoikiometri Reaksi & Pereaksi Pembatas",
                        "Kesetimbangan Kimia & Asas Le Chatelier",
                        "Larutan Asam Basa & Perhitungan pH",
                        "Reaksi Redoks & Bilangan Oksidasi",
                        "Kimia Organik & Isomer Gugus Fungsi"
                    )

                    topicsList.forEachIndexed { index, topicName ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(ChemIndigoSubtle),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    color = ChemIndigo,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = topicName,
                                color = ChemTextPrimary,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // CTA Start Button
        item {
            Button(
                onClick = onStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_start_diagnostic_landing"),
                colors = ButtonDefaults.buttonColors(containerColor = ChemIndigo),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = ChemWhite,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Mulai Tes Diagnostik Sekarang (12 Soal)",
                    color = ChemWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun DiagnosticFeatureTag(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = ChemSurfaceSubtle,
        border = BorderStroke(1.dp, ChemBorder)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ChemIndigo,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = ChemTextDark,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Diagnostic Report View: Deep analytics tracking proficiency levels across topics
 */
@Composable
fun DiagnosticReportView(
    result: DiagnosticResult,
    onRetake: () -> Unit,
    onGoToRoadmap: () -> Unit,
    onAskAiTutor: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ChemBgLight)
            .padding(16.dp)
            .testTag("diagnostic_report_view"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // OVERALL SCORE HERO CARD
        item {
            val (statusText, statusBg, statusColor) = when {
                result.overallScore >= 80 -> Triple("MAHIR (TKA READY)", ChemEmeraldBg, ChemEmerald)
                result.overallScore >= 60 -> Triple("KOMPETEN (INTERMEDIATE)", ChemSkyBg, ChemSky)
                else -> Triple("BUTUH PENGUATAN FONDASI", ChemAmberBg, ChemAmber)
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ChemWhite),
                border = BorderStroke(1.dp, ChemBorder)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "HASIL DIAGNOSTIK KEMAMPUAN KIMIA",
                        color = ChemIndigo,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Score Badge
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(statusBg)
                            .border(2.dp, statusColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${result.overallScore}%",
                                color = statusColor,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "Skor Total",
                                color = ChemTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = statusBg,
                        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = statusText,
                            color = statusColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = result.recommendationSummary,
                        color = ChemTextPrimary,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Breakdown Sub-Scores
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SubScoreCard(
                            title = "Fondasi Matematika",
                            score = result.foundationScore,
                            modifier = Modifier.weight(1f)
                        )
                        SubScoreCard(
                            title = "Konsep Inti Kimia",
                            score = result.chemistryScore,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // TOPIC PROFICIENCY MATRIX SECTION (Tracks proficiency per topic)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = ChemWhite),
                border = BorderStroke(1.dp, ChemBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Matriks Kemahiran per Topik",
                            color = ChemTextDark,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ChemIndigoSubtle
                        ) {
                            Text(
                                text = "${result.topicScores.size} Topik",
                                color = ChemIndigo,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Tingkat penguasaan dikelompokkan menjadi Mahir (≥75%), Kompeten (50-74%), dan Perlu Penguatan (<50%).",
                        color = ChemTextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val allTopics = listOf(
                        TopicProficiencyItem("topic_0_foundation", "Fondasi Matematika & Satuan", "Dasar"),
                        TopicProficiencyItem("topic_1_atomic", "Struktur Atom & Notasi Nuklida", "Teori"),
                        TopicProficiencyItem("topic_2_periodic", "Tabel Periodik & Sifat Keperiodikan", "Teori"),
                        TopicProficiencyItem("topic_3_bonding", "Ikatan Kimia & Geometri Molekul", "Struktur"),
                        TopicProficiencyItem("topic_4_mole", "Konsep Mol & Konversi Zat", "Hitungan"),
                        TopicProficiencyItem("topic_5_stoichiometry", "Stoikiometri & Pereaksi Pembatas", "Hitungan"),
                        TopicProficiencyItem("topic_10_equilibrium", "Kesetimbangan & Le Chatelier", "Analisis"),
                        TopicProficiencyItem("topic_11_acidbase", "Larutan Asam Basa & Derajat pH", "Analisis"),
                        TopicProficiencyItem("topic_12_redox", "Redoks & Bilangan Oksidasi", "Analisis"),
                        TopicProficiencyItem("topic_13_organic", "Kimia Organik & Isomer Gugus", "Struktur")
                    )

                    allTopics.forEach { item ->
                        val score = result.topicScores[item.id] ?: 0
                        TopicProficiencyRow(
                            name = item.title,
                            category = item.category,
                            score = score
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

        // IDENTIFIED MISCONCEPTIONS CARD
        if (result.misconceptions.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ChemWhite),
                    border = BorderStroke(1.dp, ChemRose.copy(alpha = 0.35f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(ChemRoseBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = ChemRose,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Miskonsepsi & Celah Konsep Terdeteksi:",
                                color = ChemRose,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        result.misconceptions.forEach { itemText ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "•",
                                    color = ChemRose,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = itemText,
                                    color = ChemTextDark,
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // ACTION BUTTONS
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Primary Action: Go to Recommended Learning Roadmap
                Button(
                    onClick = onGoToRoadmap,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_recommended_roadmap"),
                    colors = ButtonDefaults.buttonColors(containerColor = ChemIndigo),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = ChemWhite,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Buka Jalur Belajar yang Direkomendasikan",
                        color = ChemWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                // AI Tutor Action: Ask Chemia AI for guidance on misconceptions
                Button(
                    onClick = onAskAiTutor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_ask_ai_diagnostic"),
                    colors = ButtonDefaults.buttonColors(containerColor = ChemMidnight),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = ChemEmerald,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Bahas Materi Bersama CHEMIA AI",
                        color = ChemWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                // Retake Diagnostic
                OutlinedButton(
                    onClick = onRetake,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_retake_diagnostic"),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, ChemBorder),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ChemTextDark)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = ChemTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Ulangi Tes Diagnostik", color = ChemTextDark, fontSize = 13.sp)
                }
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
fun SubScoreCard(
    title: String,
    score: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = ChemSurfaceSubtle,
        border = BorderStroke(1.dp, ChemBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = ChemTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$score%",
                color = if (score >= 70) ChemEmerald else ChemIndigo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Individual Topic Row in the Proficiency Matrix
 */
@Composable
fun TopicProficiencyRow(
    name: String,
    category: String,
    score: Int
) {
    val (levelLabel, levelColor, levelBg) = when {
        score >= 75 -> Triple("Mahir", ChemEmerald, ChemEmeraldBg)
        score >= 50 -> Triple("Kompeten", ChemSky, ChemSkyBg)
        else -> Triple("Perlu Belajar", ChemRose, ChemRoseBg)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = ChemSurfaceSubtle,
        border = BorderStroke(1.dp, ChemBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        color = ChemTextDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Kategori: $category",
                        color = ChemTextMuted,
                        fontSize = 10.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = levelBg,
                    border = BorderStroke(1.dp, levelColor.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "$levelLabel ($score%)",
                        color = levelColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { (score.toFloat() / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = levelColor,
                trackColor = ChemWhite
            )
        }
    }
}

data class TopicProficiencyItem(
    val id: String,
    val title: String,
    val category: String
)

data class TopicMetadata(
    val name: String,
    val icon: ImageVector,
    val tintColor: Color,
    val bgColor: Color
)

fun getTopicMetadata(topicId: String): TopicMetadata {
    return when (topicId) {
        "topic_0_foundation" -> TopicMetadata("Fondasi Matematika & Satuan", Icons.Default.Assessment, ChemIndigo, ChemIndigoSubtle)
        "topic_1_atomic" -> TopicMetadata("Struktur Atom & Notasi Nuklida", Icons.Default.Science, ChemPurple, ChemPurpleBg)
        "topic_2_periodic" -> TopicMetadata("Tabel Periodik & Sifat Atom", Icons.Default.Science, ChemCyan, ChemCyanBg)
        "topic_3_bonding" -> TopicMetadata("Ikatan Kimia & Geometri", Icons.Default.AutoAwesome, ChemEmerald, ChemEmeraldBg)
        "topic_4_mole" -> TopicMetadata("Konsep Mol & Konversi Zat", Icons.Default.Psychology, ChemSky, ChemSkyBg)
        "topic_5_stoichiometry" -> TopicMetadata("Stoikiometri & Pereaksi", Icons.Default.Psychology, ChemAmber, ChemAmberBg)
        "topic_10_equilibrium" -> TopicMetadata("Kesetimbangan Kimia", Icons.Default.Lightbulb, ChemIndigo, ChemIndigoSubtle)
        "topic_11_acidbase" -> TopicMetadata("Larutan Asam Basa", Icons.Default.Science, ChemRose, ChemRoseBg)
        "topic_12_redox" -> TopicMetadata("Redoks & Bilangan Oksidasi", Icons.Default.Psychology, ChemPurple, ChemPurpleBg)
        "topic_13_organic" -> TopicMetadata("Kimia Organik & Isomer", Icons.Default.Science, ChemCyan, ChemCyanBg)
        else -> TopicMetadata("Kimia SMA", Icons.Default.Science, ChemIndigo, ChemIndigoSubtle)
    }
}
