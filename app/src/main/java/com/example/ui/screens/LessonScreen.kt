package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
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
import com.example.ui.theme.ChemRose
import com.example.ui.theme.ChemRoseBg
import com.example.ui.theme.ChemSurfaceSubtle
import com.example.ui.theme.ChemTextDark
import com.example.ui.theme.ChemTextMuted
import com.example.ui.theme.ChemTextPrimary
import com.example.ui.theme.ChemTextSecondary
import com.example.ui.theme.ChemWhite

@Composable
fun LessonScreen(
    topic: Topic?,
    lesson: Lesson?,
    onBackToRoadmap: () -> Unit,
    onCompleteLesson: (String) -> Unit
) {
    if (topic == null || lesson == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ChemBgLight),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Silakan pilih pelajaran dari peta kurikulum.", color = ChemTextPrimary)
        }
        return
    }

    var activeStepTab by remember { mutableIntStateOf(0) }
    val stepTitles = listOf(
        "1. Konsep",
        "2. Visual",
        "3. Contoh",
        "4. Miskonsepsi",
        "5. Latihan",
        "6. Kuis"
    )

    // State for Guided Practice Hint revealing
    var revealedStepHints by remember { mutableStateOf(setOf<Int>()) }

    // State for Exit Quiz Answers
    val exitQuizAnswers = remember { mutableStateMapOf<String, Int>() }
    var isExitQuizSubmitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ChemBgLight)
            .testTag("lesson_screen")
    ) {
        // Top Action Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = ChemWhite
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackToRoadmap,
                        modifier = Modifier.testTag("btn_back_lesson")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali ke Roadmap",
                            tint = ChemTextDark
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = topic.levelName,
                            color = ChemIndigo,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = lesson.title,
                            color = ChemTextDark,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ChemIndigoSubtle,
                        border = BorderStroke(1.dp, ChemIndigoBorder)
                    ) {
                        Text(
                            text = "${lesson.durationMinutes} Menit",
                            color = ChemIndigo,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                HorizontalDivider(color = ChemBorder, thickness = 1.dp)
            }
        }

        // Horizontal Step Tabs
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = ChemWhite
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    stepTitles.forEachIndexed { index, title ->
                        val isSelected = activeStepTab == index
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { activeStepTab = index },
                            color = if (isSelected) ChemIndigo else ChemSurfaceSubtle,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) ChemIndigo else ChemBorder
                            )
                        ) {
                            Text(
                                text = "${index + 1}",
                                color = if (isSelected) ChemWhite else ChemTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(vertical = 6.dp)
                                    .align(Alignment.CenterVertically),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
                HorizontalDivider(color = ChemBorder, thickness = 1.dp)
            }
        }

        // Main Lesson Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // STEP 0: KONSEP & TEORI
            if (activeStepTab == 0) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = ChemWhite),
                        border = BorderStroke(1.dp, ChemBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(ChemIndigoSubtle),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = null,
                                        tint = ChemIndigo,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Konsep Inti & Pembahasan",
                                    color = ChemTextDark,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = lesson.conceptExplanation,
                                color = ChemTextPrimary,
                                fontSize = 13.sp,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                item {
                    Button(
                        onClick = { activeStepTab = 1 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = ChemIndigo),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Lanjut: Representasi Visual ➔",
                            color = ChemWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // STEP 1: REPRESENTASI VISUAL
            if (activeStepTab == 1) {
                item {
                    Text(
                        text = "Representasi Visual & Molekuler",
                        color = ChemTextDark,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                itemsIndexed(lesson.visualCards) { _, card ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = ChemWhite),
                        border = BorderStroke(1.dp, ChemBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = card.title,
                                color = ChemIndigo,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ChemSurfaceSubtle)
                                    .border(1.dp, ChemBorder, RoundedCornerShape(10.dp))
                                    .padding(14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = card.formulaOrSymbol,
                                    color = ChemMidnight,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = card.explanation,
                                color = ChemTextPrimary,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }

                item {
                    Button(
                        onClick = { activeStepTab = 2 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = ChemIndigo),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Lanjut: Contoh Soal & Solusi ➔",
                            color = ChemWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // STEP 2: CONTOH SOAL BERTAHAP
            if (activeStepTab == 2) {
                item {
                    Text(
                        text = "Contoh Soal Model TKA & Pembahasan Step-by-Step",
                        color = ChemTextDark,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                itemsIndexed(lesson.workedExamples) { _, example ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = ChemWhite),
                        border = BorderStroke(1.dp, ChemBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = ChemAmberBg,
                                border = BorderStroke(1.dp, ChemAmber.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = example.title.ifBlank { "SOAL CONTOH" },
                                    color = ChemAmber,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = example.problem,
                                color = ChemTextDark,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 19.sp
                            )

                            if (example.strategy.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Strategi Analisis: ${example.strategy}",
                                    color = ChemIndigo,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Langkah Perhitungan / Solusi:",
                                color = ChemIndigo,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = example.calculation,
                                color = ChemTextPrimary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = ChemEmeraldBg,
                                border = BorderStroke(1.dp, ChemEmerald.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Jawaban Akhir: ${example.answer}",
                                    color = ChemEmerald,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }

                            if (example.explanation.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "💡 Penjelasan: ${example.explanation}",
                                    color = ChemTextSecondary,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }

                item {
                    Button(
                        onClick = { activeStepTab = 3 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = ChemIndigo),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Lanjut: Jebakan Miskonsepsi ➔",
                            color = ChemWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // STEP 3: MISKONSEPSI & JEBAKAN UMUM
            if (activeStepTab == 3) {
                item {
                    Text(
                        text = "Jebakan Miskonsepsi Siswa SMA",
                        color = ChemTextDark,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                itemsIndexed(lesson.commonMistakes) { _, misc ->
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
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Miskonsepsi Umum:",
                                    color = ChemRose,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = misc.mistake,
                                color = ChemTextDark,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ChemEmeraldBg)
                                    .border(1.dp, ChemEmerald.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = ChemEmerald,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Konsep yang Benar:",
                                        color = ChemEmerald,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = misc.correctConcept,
                                    color = ChemTextDark,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Mengapa siswa sering salah: ${misc.explanation}",
                                color = ChemTextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }

                item {
                    Button(
                        onClick = { activeStepTab = 4 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = ChemIndigo),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Lanjut: Latihan Terbimbing ➔",
                            color = ChemWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // STEP 4: LATIHAN TERBIMBING
            if (activeStepTab == 4) {
                item {
                    Text(
                        text = "Latihan Mandiri Terbimbing (Dengan Petunjuk Bertingkat)",
                        color = ChemTextDark,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                itemsIndexed(lesson.guidedPractices) { pIdx, practice ->
                    val isHintRevealed = revealedStepHints.contains(pIdx)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = ChemWhite),
                        border = BorderStroke(1.dp, ChemBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Latihan ${pIdx + 1}",
                                color = ChemIndigo,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = practice.problem,
                                color = ChemTextDark,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedButton(
                                onClick = {
                                    revealedStepHints = if (isHintRevealed) {
                                        revealedStepHints - pIdx
                                    } else {
                                        revealedStepHints + pIdx
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, ChemIndigo)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = ChemIndigo,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isHintRevealed) "Sembunyikan Petunjuk & Jawaban" else "Lihat Petunjuk / Jawaban",
                                    color = ChemIndigo,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            AnimatedVisibility(visible = isHintRevealed) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(ChemSurfaceSubtle)
                                        .border(1.dp, ChemBorder, RoundedCornerShape(8.dp))
                                        .padding(10.dp)
                                ) {
                                    practice.steps.forEach { step ->
                                        Text(
                                            text = "Langkah ${step.stepNumber}: ${step.instruction}",
                                            color = ChemIndigo,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "💡 Petunjuk: ${step.hint}",
                                            color = ChemAmber,
                                            fontSize = 11.sp,
                                            lineHeight = 15.sp
                                        )
                                        Text(
                                            text = "Solusi: ${step.solution}",
                                            color = ChemTextDark,
                                            fontSize = 11.sp,
                                            lineHeight = 15.sp
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                    }
                                    Text(
                                        text = "✅ Jawaban Akhir: ${practice.finalAnswer}",
                                        color = ChemEmerald,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Button(
                        onClick = { activeStepTab = 5 },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = ChemIndigo),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Lanjut: Uji Pemahaman Akhir (Kuis) ➔",
                            color = ChemWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // STEP 5: UJI PEMAHAMAN (EXIT QUIZ)
            if (activeStepTab == 5) {
                item {
                    Text(
                        text = "Uji Pemahaman & Verifikasi Konsep",
                        color = ChemTextDark,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                itemsIndexed(lesson.exitTest) { qIdx, question ->
                    val selectedAns = exitQuizAnswers[question.id]

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = ChemWhite),
                        border = BorderStroke(1.dp, ChemBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Soal ${qIdx + 1}. ${question.question}",
                                color = ChemTextDark,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            question.options.forEachIndexed { optIdx, optionText ->
                                val isSelected = selectedAns == optIdx
                                val isCorrect = optIdx == question.correctAnswerIndex

                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            if (!isExitQuizSubmitted) {
                                                exitQuizAnswers[question.id] = optIdx
                                            }
                                        },
                                    color = when {
                                        isExitQuizSubmitted && isCorrect -> ChemEmeraldBg
                                        isExitQuizSubmitted && isSelected && !isCorrect -> ChemRoseBg
                                        isSelected -> ChemIndigoSubtle
                                        else -> ChemSurfaceSubtle
                                    },
                                    border = BorderStroke(
                                        1.dp,
                                        when {
                                            isExitQuizSubmitted && isCorrect -> ChemEmerald
                                            isExitQuizSubmitted && isSelected && !isCorrect -> ChemRose
                                            isSelected -> ChemIndigo
                                            else -> ChemBorder
                                        }
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${('A' + optIdx)}. ",
                                            color = if (isSelected) ChemIndigo else ChemTextSecondary,
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

                            if (isExitQuizSubmitted) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "💡 Pembahasan: ${question.explanation}",
                                    color = ChemEmerald,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }

                item {
                    if (!isExitQuizSubmitted) {
                        Button(
                            onClick = {
                                isExitQuizSubmitted = true
                                onCompleteLesson(lesson.id)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = ChemEmerald),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Kirim Jawaban & Selesaikan (+100 XP)",
                                color = ChemWhite,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Button(
                            onClick = onBackToRoadmap,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = ChemIndigo),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Kembali ke Peta Kurikulum ➔",
                                color = ChemWhite,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
    }
}
