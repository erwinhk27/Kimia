package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.chemistry.TkaQuestionBank
import com.example.data.models.TkaExamResult
import com.example.ui.theme.ChemAmber
import com.example.ui.theme.ChemAmberBg
import com.example.ui.theme.ChemBgLight
import com.example.ui.theme.ChemBorder
import com.example.ui.theme.ChemEmerald
import com.example.ui.theme.ChemEmeraldBg
import com.example.ui.theme.ChemIndigo
import com.example.ui.theme.ChemIndigoBorder
import com.example.ui.theme.ChemIndigoSubtle
import com.example.ui.theme.ChemMidnight
import com.example.ui.theme.ChemRose
import com.example.ui.theme.ChemRoseBg
import com.example.ui.theme.ChemSurfaceSubtle
import com.example.ui.theme.ChemTextDark
import com.example.ui.theme.ChemTextMuted
import com.example.ui.theme.ChemTextPrimary
import com.example.ui.theme.ChemTextSecondary
import com.example.ui.theme.ChemWhite
import com.example.viewmodel.MainUiState

@Composable
fun TkaSimulationScreen(
    uiState: MainUiState,
    onStartExam: () -> Unit,
    onAnswerQuestion: (String, Int) -> Unit,
    onToggleFlag: (String) -> Unit,
    onJumpToQuestion: (Int) -> Unit,
    onFinishExam: () -> Unit
) {
    val questions = TkaQuestionBank.tkaQuestions
    val result = uiState.tkaResult

    // If Result Available and Exam Finished, show Deep Analytics
    if (result != null && !uiState.isTkaExamRunning) {
        TkaExamResultView(
            result = result,
            questions = questions,
            onRetake = onStartExam
        )
        return
    }

    // Landing / Instructions Screen
    if (!uiState.isTkaExamRunning) {
        TkaLandingView(onStart = onStartExam)
        return
    }

    // Active Exam Mode
    val currentIndex = uiState.tkaCurrentIndex
    val currentQ = questions[currentIndex]
    val selectedAnswer = uiState.tkaUserAnswers[currentQ.id]
    val isFlagged = uiState.tkaFlaggedQuestions.contains(currentQ.id)

    val minutes = uiState.tkaTimeRemainingSeconds / 60
    val seconds = uiState.tkaTimeRemainingSeconds % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ChemBgLight)
            .padding(16.dp)
            .testTag("tka_active_exam_screen")
    ) {
        // Exam Top Bar (Timer + Flag + Submit)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (minutes < 5) ChemRoseBg else ChemIndigoSubtle,
                border = BorderStroke(
                    1.dp,
                    if (minutes < 5) ChemRose.copy(alpha = 0.4f) else ChemIndigoBorder
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = if (minutes < 5) ChemRose else ChemIndigo,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = formattedTime,
                        color = if (minutes < 5) ChemRose else ChemIndigo,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onToggleFlag(currentQ.id) },
                    color = if (isFlagged) ChemAmberBg else ChemWhite,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(
                        1.dp,
                        if (isFlagged) ChemAmber else ChemBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Flag,
                            contentDescription = null,
                            tint = if (isFlagged) ChemAmber else ChemTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isFlagged) "Ragu-ragu" else "Tandai",
                            color = if (isFlagged) ChemAmber else ChemTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Button(
                    onClick = onFinishExam,
                    colors = ButtonDefaults.buttonColors(containerColor = ChemRose),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Kumpulkan", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ChemWhite)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Question Navigator Grid
        QuestionNavigatorGrid(
            totalQuestions = questions.size,
            currentIndex = currentIndex,
            userAnswers = uiState.tkaUserAnswers,
            flagged = uiState.tkaFlaggedQuestions,
            questions = questions,
            onSelect = onJumpToQuestion
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Active Question Card
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
                item {
                    Text(
                        text = "Soal ${currentIndex + 1} • ${currentQ.questionType}",
                        color = ChemIndigo,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (!currentQ.contextText.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ChemSurfaceSubtle)
                                .border(1.dp, ChemBorder, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = currentQ.contextText,
                                color = ChemTextPrimary,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Text(
                        text = currentQ.question,
                        color = ChemTextDark,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                }

                items(currentQ.options.size) { optIndex ->
                    val optText = currentQ.options[optIndex]
                    val isSelected = selectedAnswer == optIndex

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onAnswerQuestion(currentQ.id, optIndex) }
                            .testTag("tka_opt_${optIndex}"),
                        color = if (isSelected) ChemIndigoSubtle else ChemSurfaceSubtle,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) ChemIndigo else ChemBorder
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) ChemIndigo else ChemWhite)
                                    .border(1.dp, if (isSelected) ChemIndigo else ChemBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${('A' + optIndex)}",
                                    color = if (isSelected) ChemWhite else ChemTextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = optText,
                                color = ChemTextDark,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Previous & Next Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (currentIndex > 0) {
                OutlinedButton(
                    onClick = { onJumpToQuestion(currentIndex - 1) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, ChemBorder),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ChemTextDark)
                ) {
                    Text(text = "Sebelumnya", color = ChemTextDark)
                }
            }

            if (currentIndex < questions.size - 1) {
                Button(
                    onClick = { onJumpToQuestion(currentIndex + 1) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = ChemIndigo),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Selanjutnya ➔", color = ChemWhite, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuestionNavigatorGrid(
    totalQuestions: Int,
    currentIndex: Int,
    userAnswers: Map<String, Int>,
    flagged: Set<String>,
    questions: List<com.example.data.models.Question>,
    onSelect: (Int) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        for (i in 0 until totalQuestions) {
            val q = questions[i]
            val isCurrent = i == currentIndex
            val isAnswered = userAnswers.containsKey(q.id)
            val isFlag = flagged.contains(q.id)

            val bgColor = when {
                isCurrent -> ChemIndigo
                isFlag -> ChemAmber
                isAnswered -> ChemEmerald
                else -> ChemWhite
            }

            val textColor = when {
                isCurrent || isFlag || isAnswered -> ChemWhite
                else -> ChemTextSecondary
            }

            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(bgColor)
                    .border(
                        1.dp,
                        if (isCurrent || isFlag || isAnswered) Color.Transparent else ChemBorder,
                        RoundedCornerShape(8.dp)
                    )
                    .clickable { onSelect(i) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${i + 1}",
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun TkaLandingView(onStart: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ChemBgLight)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ChemWhite),
            border = BorderStroke(1.dp, ChemBorder)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(ChemRoseBg)
                        .border(1.dp, ChemRose.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Assessment,
                        contentDescription = null,
                        tint = ChemRose,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Simulasi Ujian TKA Kimia SMA",
                    color = ChemTextDark,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Uji kemampuan mengerjakan soal TKA standar UTBK & Ujian Mandiri dengan timer 30 menit. Dilengkapi analisis kelemahan topik dan bedah kesalahan berpikir.",
                    color = ChemTextSecondary,
                    fontSize = 13.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_start_tka_exam"),
                    colors = ButtonDefaults.buttonColors(containerColor = ChemRose),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Mulai Ujian Simulasi TKA",
                        color = ChemWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TkaExamResultView(
    result: TkaExamResult,
    questions: List<com.example.data.models.Question>,
    onRetake: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ChemBgLight)
            .padding(16.dp)
            .testTag("tka_result_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Score Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = ChemWhite),
                border = BorderStroke(1.dp, ChemBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SKOR SIMULASI TKA KIMIA",
                        color = ChemIndigo,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${result.score}",
                        color = if (result.score >= 700) ChemEmerald else ChemAmber,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Black
                    )

                    Text(
                        text = "Skala UTBK 0 - 1000 • Benar: ${result.correctCount} / ${result.totalQuestions} Soal",
                        color = ChemTextSecondary,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ChemSurfaceSubtle)
                                .border(1.dp, ChemBorder, RoundedCornerShape(10.dp))
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Akurasi", color = ChemIndigo, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "${(result.correctCount * 100) / result.totalQuestions}%",
                                color = ChemTextDark,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ChemSurfaceSubtle)
                                .border(1.dp, ChemBorder, RoundedCornerShape(10.dp))
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Waktu Pengerjaan", color = ChemIndigo, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "${result.timeSpentSeconds / 60}m ${result.timeSpentSeconds % 60}s",
                                color = ChemTextDark,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Section Title: Bedah Soal & Cara Berpikir yang Benar
        item {
            Text(
                text = "Bedah Pembahasan & Kesalahan Berpikir",
                color = ChemTextDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(questions.size) { qIndex ->
            val q = questions[qIndex]
            val userAns = result.userAnswers[q.id]
            val isCorrect = userAns == q.correctAnswerIndex

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ChemWhite),
                border = BorderStroke(
                    1.dp,
                    if (isCorrect) ChemBorder else ChemRose.copy(alpha = 0.3f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Soal ${qIndex + 1}: ${q.conceptTested}",
                            color = ChemIndigo,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isCorrect) ChemEmeraldBg else ChemRoseBg,
                            border = BorderStroke(
                                1.dp,
                                if (isCorrect) ChemEmerald.copy(alpha = 0.3f) else ChemRose.copy(alpha = 0.3f)
                            )
                        ) {
                            Text(
                                text = if (isCorrect) "BENAR" else "SALAH",
                                color = if (isCorrect) ChemEmerald else ChemRose,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = q.question,
                        color = ChemTextDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Jawaban Anda vs Jawaban Benar
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(ChemSurfaceSubtle)
                            .border(1.dp, ChemBorder, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "• Jawaban Anda: ${if (userAns != null) "${('A' + userAns)}. ${q.options[userAns]}" else "Tidak Dijawab"}",
                            color = if (isCorrect) ChemEmerald else ChemRose,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "• Kunci Jawaban: ${('A' + q.correctAnswerIndex)}. ${q.options[q.correctAnswerIndex]}",
                            color = ChemEmerald,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Deep Explanation & Thinking Method
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(ChemIndigoSubtle)
                            .border(1.dp, ChemIndigoBorder, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "📝 Pembahasan Lengkap:",
                            color = ChemIndigo,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = q.explanation,
                            color = ChemTextDark,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )

                        if (q.rightThinkingMethod.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "💡 Cara Berpikir yang Benar:",
                                color = ChemAmber,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = q.rightThinkingMethod,
                                color = ChemTextDark,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = onRetake,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = ChemIndigo),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Ulangi Simulasi Ujian TKA", color = ChemWhite, fontWeight = FontWeight.Bold)
            }
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}
