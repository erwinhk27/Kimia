package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import com.example.ui.theme.ChemAmber
import com.example.ui.theme.ChemAmberBg
import com.example.ui.theme.ChemBorder
import com.example.ui.theme.ChemEmerald
import com.example.ui.theme.ChemEmeraldBg
import com.example.ui.theme.ChemIndigo
import com.example.ui.theme.ChemIndigoSubtle
import com.example.ui.theme.ChemMidnight
import com.example.ui.theme.ChemTextDark
import com.example.ui.theme.ChemTextMuted
import com.example.ui.theme.ChemTextPrimary
import com.example.ui.theme.ChemTextSecondary
import com.example.ui.theme.ChemWhite

@Composable
fun ChemistryTopBar(
    xp: Int,
    streak: Int,
    readinessScore: Int,
    onOpenAiTutor: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("chemistry_top_bar"),
        color = ChemWhite,
        shadowElevation = 1.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // App Logo & Title (Professional Polish style)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ChemIndigo),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "K",
                            color = ChemWhite,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "KIMIA MASTER",
                            color = ChemTextDark,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.3.sp
                        )
                        Text(
                            text = "SIAP TKA • LEVEL 4",
                            color = ChemTextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Stats Badges
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Streak Badge
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = ChemAmberBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, ChemAmber.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🔥", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${streak}d",
                                color = ChemAmber,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // XP Badge
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = ChemIndigoSubtle,
                        border = androidx.compose.foundation.BorderStroke(1.dp, ChemIndigo.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "⚡", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "$xp",
                                color = ChemIndigo,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // TKA Readiness Pill
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = ChemEmeraldBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, ChemEmerald.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🎯", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "$readinessScore%",
                                color = ChemEmerald,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                color = ChemBorder,
                thickness = 1.dp
            )
        }
    }
}

@Composable
fun ChemistryNavBar(
    currentTab: String,
    onTabSelected: (String) -> Unit
) {
    Column {
        HorizontalDivider(
            color = ChemBorder,
            thickness = 1.dp
        )
        NavigationBar(
            modifier = Modifier.testTag("chemistry_nav_bar"),
            containerColor = ChemWhite,
            contentColor = ChemTextPrimary,
            tonalElevation = 0.dp
        ) {
            val items = listOf(
                Triple("dashboard", "Beranda", Icons.Default.Home),
                Triple("roadmap", "Materi", Icons.Default.MenuBook),
                Triple("tka", "Ujian TKA", Icons.Default.Assessment),
                Triple("simulations", "Simulasi", Icons.Default.Science),
                Triple("calculators", "Kalkulator", Icons.Default.Calculate),
                Triple("ai_tutor", "Chemia AI", Icons.Default.AutoAwesome),
                Triple("progress", "Laporan", Icons.Default.Psychology)
            )

            items.forEach { (tabId, title, icon) ->
                val selected = currentTab == tabId
                NavigationBarItem(
                    selected = selected,
                    onClick = { onTabSelected(tabId) },
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    label = {
                        Text(
                            text = title,
                            fontSize = 10.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ChemIndigo,
                        selectedTextColor = ChemIndigo,
                        indicatorColor = ChemIndigoSubtle,
                        unselectedIconColor = ChemTextMuted,
                        unselectedTextColor = ChemTextMuted
                    )
                )
            }
        }
    }
}
