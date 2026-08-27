package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.chemistry.DiagnosticData
import com.example.ui.theme.ChemAmber
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
fun DiagnosticScreen(
    uiState: MainUiState,
    onAnswerQuestion: (String, Int) -> Unit,
    onNextQuestion: () -> Unit,
    onPrevQuestion: () -> Unit,
    onFinishDiagnostic: () -> Unit,
    onStartDiagnostic: () -> Unit,
    onNavigateToRoadmap: () -> Unit
) {
    val questions = DiagnosticData.diagnosticQuestions
    val currentIndex = uiState.diagnosticCurrentIndex
    val result = uiState.diagnosticResult

    // If result exists and not running, show Comprehensive Diagnostic Report
    if (result != null && !uiState.isDiagnosticRunning) {
        DiagnosticReportView(
            result = result,
            onRetake = onStartDiagnostic,
            onGoToRoadmap = onNavigateToRoadmap
        )
        return
    }

    // If not running and no result, show Landing/Start Screen
    if (!uiState.isDiagnosticRunning) {
        DiagnosticLandingView(onStart = onStartDiagnostic)
        return
    }

    // Diagnostic Questions in Progress
    val currentQuestion = questions[currentIndex]
    val selectedOption = uiState.diagnosticAnswers[currentQuestion.id]
    val isLast = currentIndex == questions.size - 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ChemBgLight)
            .padding(16.dp)
            .testTag("diagnostic_running_screen")
    ) {
        // Progress Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tes Diagnostik Kimia & Fondasi",
                color = ChemTextDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = ChemIndigoSubtle,
                border = BorderStroke(1.dp, ChemIndigoBorder)
            ) {
                Text(
                    text = "Soal ${currentIndex + 1} / ${questions.size}",
                    color = ChemIndigo,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LinearProgressIndicator(
            progress = { (currentIndex + 1).toFloat() / questions.size.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = ChemIndigo,
            trackColor = ChemWhite
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Question Card
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
                        text = currentQuestion.question,
                        color = ChemTextDark,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                items(currentQuestion.options.size) { optIndex ->
                    val optText = currentQuestion.options[optIndex]
                    val isSelected = selectedOption == optIndex

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                onAnswerQuestion(currentQuestion.id, optIndex)
                            }
                            .testTag("diag_opt_${optIndex}"),
                        color = if (isSelected) ChemIndigoSubtle else ChemSurfaceSubtle,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) ChemIndigo else ChemBorder
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) ChemIndigo else ChemWhite)
                                    .border(1.dp, if (isSelected) ChemIndigo else ChemBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${('A' + optIndex)}",
                                    color = if (isSelected) ChemWhite else ChemTextSecondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = optText,
                                color = ChemTextDark,
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Footer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (currentIndex > 0) {
                OutlinedButton(
                    onClick = onPrevQuestion,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, ChemBorder),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ChemTextDark)
                ) {
                    Text(text = "Sebelumnya", color = ChemTextDark)
                }
            }

            Button(
                onClick = {
                    if (isLast) {
                        onFinishDiagnostic()
                    } else {
                        onNextQuestion()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag(if (isLast) "btn_finish_diag" else "btn_next_diag"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isLast) ChemEmerald else ChemIndigo
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (isLast) "Selesaikan & Analisis" else "Selanjutnya ➔",
                    color = ChemWhite,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun DiagnosticLandingView(onStart: () -> Unit) {
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
                        .background(ChemIndigoSubtle)
                        .border(1.dp, ChemIndigoBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = ChemIndigo,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Tes Diagnostik Cerdas",
                    color = ChemTextDark,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Petakan kemampuan fondasi matematika sains, pemahaman konsep atom, stoikiometri, asam-basa, hingga redoks. Sistem akan merekomendasikan jalur belajar yang dipersonalisasi untuk Anda.",
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
                        .testTag("btn_start_diagnostic_landing"),
                    colors = ButtonDefaults.buttonColors(containerColor = ChemIndigo),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Mulai Tes Diagnostik (12 Soal)",
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
fun DiagnosticReportView(
    result: com.example.data.models.DiagnosticResult,
    onRetake: () -> Unit,
    onGoToRoadmap: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ChemBgLight)
            .padding(16.dp)
            .testTag("diagnostic_report_view"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
                        text = "HASIL DIAGNOSTIK BELAJAR",
                        color = ChemIndigo,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${result.overallScore}%",
                        color = if (result.overallScore >= 70) ChemEmerald else ChemAmber,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black
                    )

                    Text(
                        text = result.recommendationSummary,
                        color = ChemTextPrimary,
                        fontSize = 12.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Breakdown bars
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ChemSurfaceSubtle)
                                .border(1.dp, ChemBorder, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Text(text = "Fondasi Matematika", color = ChemIndigo, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(text = "${result.foundationScore}%", color = ChemTextDark, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(ChemSurfaceSubtle)
                                .border(1.dp, ChemBorder, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Text(text = "Konsep Kimia", color = ChemIndigo, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(text = "${result.chemistryScore}%", color = ChemTextDark, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Identified Misconceptions
        if (result.misconceptions.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ChemWhite),
                    border = BorderStroke(1.dp, ChemRose.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = ChemRose,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Miskonsepsi & Topik yang Perlu Diperkuat:",
                                color = ChemRose,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        result.misconceptions.forEach { item ->
                            Text(
                                text = "• $item",
                                color = ChemTextDark,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                modifier = Modifier.padding(vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }

        // Recommended Learning Path
        item {
            Button(
                onClick = onGoToRoadmap,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_recommended_roadmap"),
                colors = ButtonDefaults.buttonColors(containerColor = ChemIndigo),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Buka Jalur Belajar yang Direkomendasikan ➔",
                    color = ChemWhite,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            OutlinedButton(
                onClick = onRetake,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ChemBorder),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ChemTextDark)
            ) {
                Text(text = "Ulangi Tes Diagnostik", color = ChemTextDark)
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}
