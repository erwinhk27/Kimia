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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.chemistry.CurriculumData
import com.example.data.models.Lesson
import com.example.data.models.Topic
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
import com.example.ui.theme.ChemSurfaceSubtle
import com.example.ui.theme.ChemTextDark
import com.example.ui.theme.ChemTextMuted
import com.example.ui.theme.ChemTextPrimary
import com.example.ui.theme.ChemTextSecondary
import com.example.ui.theme.ChemWhite
import com.example.viewmodel.MainUiState

@Composable
fun RoadmapScreen(
    uiState: MainUiState,
    onSelectTopicLesson: (Topic, Lesson) -> Unit
) {
    var selectedTierIndex by remember { mutableIntStateOf(0) }
    val tiers = listOf(
        "Semua Level",
        "Fondasi & Atom (0-2)",
        "Ikatan & Stoikiometri (3-5)",
        "Fisika & Kesetimbangan (6-10)",
        "Asam Basa & Redoks (11-12)",
        "Organik & TKA (13+)"
    )

    val filteredTopics = when (selectedTierIndex) {
        1 -> CurriculumData.topics.filter { it.levelNumber in 0..2 }
        2 -> CurriculumData.topics.filter { it.levelNumber in 3..5 }
        3 -> CurriculumData.topics.filter { it.levelNumber in 6..10 }
        4 -> CurriculumData.topics.filter { it.levelNumber in 11..12 }
        5 -> CurriculumData.topics.filter { it.levelNumber >= 13 }
        else -> CurriculumData.topics
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ChemBgLight)
            .testTag("roadmap_screen")
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = ChemWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = "Kurikulum & Peta Jalur Belajar",
                    color = ChemTextDark,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tersusun bertahap dari konsep paling dasar hingga penalaran TKA",
                    color = ChemTextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Tier Filter Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTierIndex,
            containerColor = ChemWhite,
            contentColor = ChemIndigo,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTierIndex]),
                    color = ChemIndigo
                )
            }
        ) {
            tiers.forEachIndexed { index, tierName ->
                Tab(
                    selected = selectedTierIndex == index,
                    onClick = { selectedTierIndex = index },
                    text = {
                        Text(
                            text = tierName,
                            fontSize = 12.sp,
                            fontWeight = if (selectedTierIndex == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTierIndex == index) ChemIndigo else ChemTextSecondary
                        )
                    }
                )
            }
        }

        HorizontalDivider(color = ChemBorder, thickness = 1.dp)

        Spacer(modifier = Modifier.height(10.dp))

        // Topic List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filteredTopics) { topic ->
                val firstLesson = topic.lessons.firstOrNull()
                val isCompleted = firstLesson?.let { uiState.completedLessonIds.contains(it.id) } ?: false

                TopicCardItem(
                    topic = topic,
                    isCompleted = isCompleted,
                    onStartLesson = {
                        if (firstLesson != null) {
                            onSelectTopicLesson(topic, firstLesson)
                        }
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
    }
}

@Composable
fun TopicCardItem(
    topic: Topic,
    isCompleted: Boolean,
    onStartLesson: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("topic_card_${topic.id}"),
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
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isCompleted) ChemEmeraldBg else ChemIndigoSubtle,
                    border = BorderStroke(
                        1.dp,
                        if (isCompleted) ChemEmerald.copy(alpha = 0.3f) else ChemIndigoBorder
                    )
                ) {
                    Text(
                        text = topic.levelName,
                        color = if (isCompleted) ChemEmerald else ChemIndigo,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = ChemAmberBg,
                        border = BorderStroke(1.dp, ChemAmber.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = "+100 XP",
                            color = ChemAmber,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    if (isCompleted) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selesai",
                            tint = ChemEmerald,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = topic.title,
                color = ChemTextDark,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = topic.description,
                color = ChemTextSecondary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Learning Objectives preview
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(ChemSurfaceSubtle)
                    .border(1.dp, ChemBorder, RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Text(
                    text = "🎯 Tujuan Pembelajaran:",
                    color = ChemIndigo,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                topic.learningObjectives.forEach { obj ->
                    Text(
                        text = "• $obj",
                        color = ChemTextPrimary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onStartLesson,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_open_lesson_${topic.id}"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCompleted) ChemMidnight else ChemIndigo
                )
            ) {
                Icon(
                    imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = if (isCompleted) ChemEmerald else ChemWhite,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isCompleted) "Pelajari Ulang Materi" else "Mulai Pelajaran (8 Tahap)",
                    color = ChemWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}
