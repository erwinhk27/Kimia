package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.chemistry.CurriculumData
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
import com.example.ui.theme.ChemOrange
import com.example.ui.theme.ChemOrangeBg
import com.example.ui.theme.ChemRose
import com.example.ui.theme.ChemRoseBg
import com.example.ui.theme.ChemSurfaceSubtle
import com.example.ui.theme.ChemTextDark
import com.example.ui.theme.ChemTextMuted
import com.example.ui.theme.ChemTextPrimary
import com.example.ui.theme.ChemTextSecondary
import com.example.ui.theme.ChemWhite
import com.example.viewmodel.MainUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(
    uiState: MainUiState,
    onNavigateToTab: (String) -> Unit,
    onStartDiagnostic: () -> Unit,
    onStartTkaExam: () -> Unit,
    onOpenTopic: (String) -> Unit
) {
    var showChallengeReasoning by remember { mutableStateOf(false) }
    var selectedChallengeAnswer by remember { mutableStateOf<Int?>(null) }

    val activeTopic = uiState.activeTopic ?: CurriculumData.topics.find { it.levelNumber == 4 } ?: CurriculumData.topics.first()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ChemBgLight)
            .padding(horizontal = 16.dp)
            .testTag("dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { Spacer(modifier = Modifier.height(2.dp)) }

        // 1. HERO SECTION: ESTIMASI KESIAPAN TKA (Professional Polish Midnight Indigo Card)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dashboard_hero_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ChemMidnight),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "ESTIMASI KESIAPAN TKA",
                            color = Color(0xFFBFDBFE),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.2.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "${uiState.userProfile.tkaReadinessScore}%",
                                color = ChemWhite,
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Black,
                                lineHeight = 40.sp
                            )
                            Text(
                                text = "TKA Ready",
                                color = Color(0xFF93C5FD),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Progress Bar with #6366F1 fill
                        LinearProgressIndicator(
                            progress = { uiState.userProfile.tkaReadinessScore / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape),
                            color = ChemIndigo,
                            trackColor = Color.White.copy(alpha = 0.15f)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(ChemEmerald)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${uiState.completedLessonIds.size.coerceAtLeast(8)} Topik Dikuasai",
                                    color = ChemWhite,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Text(
                                text = "Streak ${uiState.currentStreak} Hari",
                                color = Color(0xFFBFDBFE),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // 2. LANJUTKAN BELAJAR SECTION
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "LANJUTKAN BELAJAR",
                    color = ChemTextDark,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenTopic(activeTopic.id) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ChemWhite),
                    border = BorderStroke(1.dp, ChemBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Level Box Badge (Level 04)
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(ChemIndigoSubtle)
                                .border(1.dp, ChemIndigoBorder, RoundedCornerShape(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "LEVEL",
                                    color = ChemIndigo,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = String.format("%02d", activeTopic.levelNumber),
                                    color = ChemIndigo,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        // Info & Progress
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = activeTopic.title,
                                color = ChemTextDark,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Sub: ${activeTopic.lessons.firstOrNull()?.title ?: "Massa Molar & Stoikiometri"}",
                                color = ChemTextSecondary,
                                fontSize = 11.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                LinearProgressIndicator(
                                    progress = { 0.65f },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(6.dp)
                                        .clip(CircleShape),
                                    color = ChemOrange,
                                    trackColor = ChemBgLight
                                )
                                Text(
                                    text = "65%",
                                    color = ChemTextMuted,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Arrow Action Button
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(ChemMidnight)
                                .testTag("btn_continue_learning"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Lanjutkan",
                                tint = ChemWhite,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // 3. 2-COLUMN ACTION GRID (Tes Diagnostik, Simulasi TKA, Tabel Periodik, 10 Kalkulator)
        item {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                maxItemsInEachRow = 2
            ) {
                PolishActionCard(
                    title = "Tes Diagnostik",
                    subtitle = "Cek kelemahan dasar",
                    icon = Icons.Default.Psychology,
                    iconBg = ChemEmeraldBg,
                    iconColor = ChemEmerald,
                    modifier = Modifier.weight(1f),
                    onClick = onStartDiagnostic
                )

                PolishActionCard(
                    title = "Simulasi TKA",
                    subtitle = "15 Soal • 20 Menit",
                    icon = Icons.Default.Assessment,
                    iconBg = ChemAmberBg,
                    iconColor = ChemAmber,
                    modifier = Modifier.weight(1f),
                    onClick = onStartTkaExam
                )

                PolishActionCard(
                    title = "Tabel Periodik",
                    subtitle = "Sifat & orbital unsur",
                    icon = Icons.Default.Science,
                    iconBg = ChemCyanBg,
                    iconColor = ChemCyan,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToTab("simulations") }
                )

                PolishActionCard(
                    title = "10 Kalkulator",
                    subtitle = "Mr, Mol, pH, Gas",
                    icon = Icons.Default.Calculate,
                    iconBg = ChemIndigoSubtle,
                    iconColor = ChemIndigo,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToTab("calculators") }
                )
            }
        }

        // 4. CHEMIA AI TUTOR BANNER (Indigo #6366F1 Callout)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ai_tutor_banner"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = ChemIndigo),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.22f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🤖", fontSize = 22.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "TANYA AI TUTOR",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Halo, saya Chemia!",
                                color = ChemWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = { onNavigateToTab("ai_tutor") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ChemWhite,
                            contentColor = ChemIndigo
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "CHAT",
                            color = ChemIndigo,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 5. TODAY'S REASONING CHALLENGE (Clean White Card Style)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("daily_challenge_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ChemWhite),
                border = BorderStroke(1.dp, ChemBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ChemAmberBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = ChemAmber,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Tantangan Penalaran Hari Ini",
                                color = ChemTextDark,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ChemAmberBg,
                            border = BorderStroke(1.dp, ChemAmber.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "+50 XP",
                                color = ChemAmber,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Topik: Kesetimbangan Le Chatelier pada Pembentukan Amonia",
                        color = ChemIndigo,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Reaksi sintesis amonia: N₂(g) + 3H₂(g) ⇌ 2NH₃(g) ΔH = -92 kJ. Mengapa pada industri Proses Haber-Bosch digunakan suhu tinggi (~450°C) padahal reaksi ini bersifat eksoterm?",
                        color = ChemTextPrimary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val challengeOptions = listOf(
                        "Untuk menggeser kesetimbangan ke arah pembentukan NH₃",
                        "Untuk mempercepat laju reaksi mencapai kesetimbangan (kompromi kinetika vs termodinamika)",
                        "Agar katalis besi (Fe) dapat terurai sempurna",
                        "Untuk memperbesar volume wadah reaksi"
                    )

                    challengeOptions.forEachIndexed { index, optionText ->
                        val isSelected = selectedChallengeAnswer == index
                        val isCorrect = index == 1

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    selectedChallengeAnswer = index
                                    showChallengeReasoning = true
                                },
                            color = when {
                                isSelected && isCorrect -> ChemEmeraldBg
                                isSelected && !isCorrect -> ChemRoseBg
                                else -> ChemSurfaceSubtle
                            },
                            border = BorderStroke(
                                1.dp,
                                when {
                                    isSelected && isCorrect -> ChemEmerald
                                    isSelected && !isCorrect -> ChemRose
                                    else -> ChemBorder
                                }
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${('A' + index)}. ",
                                    color = if (isSelected && isCorrect) ChemEmerald else ChemIndigo,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = optionText,
                                    color = ChemTextDark,
                                    fontSize = 12.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    AnimatedVisibility(visible = showChallengeReasoning) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .background(
                                    ChemEmeraldBg,
                                    RoundedCornerShape(10.dp)
                                )
                                .border(1.dp, ChemEmerald.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = ChemEmerald,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Analisis Penalaran TKA:",
                                    color = ChemEmerald,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Secara termodinamika (Le Chatelier), suhu rendah menguntungkan pembentukan NH₃. Namun pada suhu rendah energi kinetik partikel sangat rendah sehingga laju reaksi amat lambat. Suhu 450°C adalah SUHU OPTIMUM (kompromi kinetika vs termodinamika).",
                                color = ChemTextDark,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }

        // 6. JALUR BELAJAR ROADMAP PREVIEW
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Jalur Belajar Terstruktur",
                    color = ChemTextDark,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Lihat Semua ➔",
                    color = ChemIndigo,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onNavigateToTab("roadmap") }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            CurriculumData.topics.take(4).forEach { topic ->
                val isCompleted = uiState.completedLessonIds.contains("lesson_${topic.levelNumber}_1")
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clickable { onOpenTopic(topic.id) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ChemWhite),
                    border = BorderStroke(1.dp, ChemBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isCompleted) ChemEmeraldBg else ChemIndigoSubtle
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${topic.levelNumber}",
                                color = if (isCompleted) ChemEmerald else ChemIndigo,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = topic.levelName,
                                color = ChemIndigo,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = topic.title,
                                color = ChemTextDark,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selesai",
                                tint = ChemEmerald,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Buka",
                                tint = ChemTextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

@Composable
fun PolishActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconBg: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ChemWhite),
        border = BorderStroke(1.dp, ChemBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                color = ChemTextDark,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = ChemTextSecondary,
                fontSize = 11.sp
            )
        }
    }
}
