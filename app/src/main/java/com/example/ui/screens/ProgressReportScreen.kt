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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.chemistry.CurriculumData
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
fun ProgressReportScreen(
    uiState: MainUiState,
    onNavigateToRoadmap: () -> Unit
) {
    var isTeacherMode by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ChemBgLight)
            .padding(16.dp)
            .testTag("progress_report_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Mode Switcher (Siswa vs Guru)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ChemWhite),
                border = BorderStroke(1.dp, ChemBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isTeacherMode) ChemAmberBg else ChemIndigoSubtle)
                                .border(1.dp, if (isTeacherMode) ChemAmber.copy(alpha = 0.3f) else ChemIndigoBorder, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isTeacherMode) Icons.Default.School else Icons.Default.Person,
                                contentDescription = null,
                                tint = if (isTeacherMode) ChemAmber else ChemIndigo,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isTeacherMode) "Mode Guru & Analitik Kelas" else "Laporan Kemajuan Belajar Siswa",
                                color = ChemTextDark,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isTeacherMode) "Pantau penguasaan & miskonsepsi siswa" else "Lihat skor TKA, XP, dan rekomendasi topik",
                                color = ChemTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Switch(
                        checked = isTeacherMode,
                        onCheckedChange = { isTeacherMode = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = ChemWhite,
                            checkedTrackColor = ChemIndigo,
                            uncheckedThumbColor = ChemWhite,
                            uncheckedTrackColor = ChemBorder
                        )
                    )
                }
            }
        }

        if (!isTeacherMode) {
            // STUDENT PROGRESS DASHBOARD
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = ChemWhite),
                    border = BorderStroke(1.dp, ChemBorder)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Kesiapan TKA Kimia",
                                color = ChemIndigo,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.3.sp
                            )
                            Text(
                                text = "${uiState.userProfile.tkaReadinessScore}% Siap",
                                color = ChemEmerald,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { uiState.userProfile.tkaReadinessScore / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(CircleShape),
                            color = ChemEmerald,
                            trackColor = ChemBgLight
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Stats Trio
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StatBox(label = "Total XP", value = "${uiState.totalXp}", icon = "⚡", modifier = Modifier.weight(1f))
                            StatBox(label = "Streak", value = "${uiState.currentStreak} Hari", icon = "🔥", modifier = Modifier.weight(1f))
                            StatBox(label = "Materi Selesai", value = "${uiState.completedLessonIds.size}/14", icon = "📚", modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            // Topic Mastery Breakdown
            item {
                Text(
                    text = "Penguasaan Per Topik Kurikulum",
                    color = ChemTextDark,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(CurriculumData.topics.size) { index ->
                val topic = CurriculumData.topics[index]
                val isCompleted = uiState.completedLessonIds.contains("lesson_${topic.levelNumber}_1")
                val masteryPct = if (isCompleted) 100 else (30 + index * 5).coerceIn(20, 85)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ChemWhite),
                    border = BorderStroke(1.dp, ChemBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${topic.levelName}: ${topic.title}",
                                color = ChemTextDark,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "$masteryPct%",
                                color = if (masteryPct >= 80) ChemEmerald else ChemIndigo,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        LinearProgressIndicator(
                            progress = { masteryPct / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CircleShape),
                            color = if (masteryPct >= 80) ChemEmerald else ChemIndigo,
                            trackColor = ChemBgLight
                        )
                    }
                }
            }
        } else {
            // TEACHER / GURU ANALYTICS VIEW
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ChemWhite),
                    border = BorderStroke(1.dp, ChemBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Ringkasan Kohort Kelas XII MIA",
                            color = ChemAmber,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StatBox(label = "Rata-Rata TKA", value = "742 / 1000", icon = "📊", modifier = Modifier.weight(1f))
                            StatBox(label = "Siswa Tuntas", value = "85%", icon = "🎓", modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ChemWhite),
                    border = BorderStroke(1.dp, ChemRose.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = ChemRose, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Topik Rentan Miskonsepsi Siswa:",
                                color = ChemRose,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = "1. Pereaksi Pembatas (68% siswa lupa membagi mol dengan koefisien)", color = ChemTextDark, fontSize = 12.sp, lineHeight = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "2. Pergeseran Kesetimbangan Le Chatelier Suhu & Tekanan (54% siswa keliru arah endoterm)", color = ChemTextDark, fontSize = 12.sp, lineHeight = 16.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "3. Penyetaraan Redoks Suasana Asam vs Basa (47% siswa lupa ion OH⁻)", color = ChemTextDark, fontSize = 12.sp, lineHeight = 16.sp)
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}

@Composable
fun StatBox(
    label: String,
    value: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(ChemSurfaceSubtle)
            .border(1.dp, ChemBorder, RoundedCornerShape(10.dp))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = icon, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, color = ChemTextDark, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = ChemTextSecondary, fontSize = 10.sp)
    }
}
