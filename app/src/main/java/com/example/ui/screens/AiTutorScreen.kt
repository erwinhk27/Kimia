package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.chemistry.ChatMessage
import com.example.ui.theme.ChemAmber
import com.example.ui.theme.ChemAmberBg
import com.example.ui.theme.ChemBgLight
import com.example.ui.theme.ChemBorder
import com.example.ui.theme.ChemEmerald
import com.example.ui.theme.ChemEmeraldBg
import com.example.ui.theme.ChemIndigo
import com.example.ui.theme.ChemIndigoBorder
import com.example.ui.theme.ChemIndigoSubtle
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

data class SocraticMode(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiTutorScreen(
    uiState: MainUiState,
    onSendMessage: (String, String) -> Unit,
    onClearChat: () -> Unit = {}
) {
    var textInput by remember { mutableStateOf("") }
    var selectedModeIndex by remember { mutableIntStateOf(0) }
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    val listState = rememberLazyListState()

    val socraticModes = listOf(
        SocraticMode("socratic", "Socratic (Tanya Jawab)", "Bimbingan langkah pemandu", Icons.Default.Psychology, ChemIndigo),
        SocraticMode("clue", "Petunjuk / Clue", "Clue bertahap tanpa spoiler", Icons.Default.Lightbulb, ChemAmber),
        SocraticMode("analogy", "Analogi Riil", "Perumpamaan konsep sehari-hari", Icons.Default.Science, ChemEmerald),
        SocraticMode("calculation", "Bedah Hitungan", "Langkah matematis & rumus praktis", Icons.Default.Calculate, ChemSky),
        SocraticMode("diagram", "Diagram Visual", "Skema terstruktur & grafik reaksi", Icons.Default.GraphicEq, ChemIndigo),
        SocraticMode("misconception", "Miskonsepsi TKA", "Analisis jebakan soal ujian", Icons.Default.WarningAmber, ChemRose)
    )

    val topicCategories = listOf(
        "Semua Topik",
        "Stoikiometri",
        "Struktur Atom",
        "Ikatan Kimia",
        "Termokimia",
        "Laju Reaksi",
        "Kesetimbangan",
        "Asam Basa & Buffer",
        "Redoks & Sel Volta",
        "Kimia Organik",
        "Koloid"
    )

    val categorizedPrompts = mapOf(
        0 to listOf(
            "Bimbing saya menentukan pereaksi pembatas pada reaksi kimia",
            "Jelaskan prinsip pergeseran kesetimbangan Le Chatelier dengan analogi",
            "Bagaimana cara membedakan larutan buffer dan hidrolisis garam?",
            "Gambarkan diagram energi reaksi eksoterm vs endoterm",
            "Beri clue untuk menyetarakan reaksi redoks metode ion elektron",
            "Mengapa HF asam lemah padahal elektronegativitas F paling tinggi?"
        ),
        1 to listOf(
            "Bagaimana cara cepat menentukan massa zat reaktan yang tersisa?",
            "Jelaskan konsep mol dengan analogi belanja di pasar",
            "Bimbing saya menentukan rumus empiris dan molekul senyawa CxHyOz",
            "Beri clue untuk menghitung volume gas STP dari massa zat"
        ),
        2 to listOf(
            "Bagaimana cara menentukan 4 bilangan kuantum (n, l, m, s) elektron terakhir?",
            "Jelaskan aturan Hund dan larangan Pauli dengan analogi kursi bus",
            "Mengapa konfigurasi Cr adalah 4s1 3d5 bukan 4s2 3d4?",
            "Gambarkan kotak pengisian orbital untuk ion Fe2+"
        ),
        3 to listOf(
            "Apa perbedaan ikatan kovalen polar, nonpolar, dan kovalen koordinasi?",
            "Bagaimana cara meramalkan bentuk molekul dengan teori VSEPR?",
            "Mengapa titik didih H2O jauh lebih tinggi dibanding H2S?",
            "Jelaskan hibridisasi sp3 pada molekul metana CH4"
        ),
        4 to listOf(
            "Bimbing saya menghitung entalpi reaksi menggunakan hukum Hess",
            "Bagaimana cara menghitung Delta H reaksi dari data energi ikatan rata-rata?",
            "Gambarkan diagram siklus energi pembentukan SO3",
            "Apa perbedaan sistem terbuka, tertutup, dan terisolasi?"
        ),
        5 to listOf(
            "Bagaimana cara menentukan orde reaksi dari tabel data eksperimen?",
            "Mengapa kenaikan suhu 10 derajat C melipatgandakan laju reaksi?",
            "Jelaskan peran katalis terhadap energi aktivasi reaksi",
            "Apa perbedaan reaksi orde nol, orde satu, dan orde dua?"
        ),
        6 to listOf(
            "Bimbing saya menghitung derajat disosiasi alfa pada reaksi kesetimbangan",
            "Bagaimana pengaruh penambahan volume terhadap reaksi fase gas?",
            "Jelaskan proses Haber-Bosch untuk pembuatan amonia industri",
            "Hubungan Kc dan Kp: Kp = Kc (RT)^Delta_n"
        ),
        7 to listOf(
            "Bagaimana cara menghitung pH campuran asam lemah dan basa kuat?",
            "Jelaskan mengapa larutan penyangga mampu mempertahankan nilai pH",
            "Apa perbedaan hidrolisis parsial dan hidrolisis total garam?",
            "Beri clue untuk menghitung pH larutan garam CH3COONa 0.1 M"
        ),
        8 to listOf(
            "Tuliskan jembatan keledai menghafal KRAO dan KaP-AnN",
            "Bagaimana menentukan kutub anoda dan katoda pada sel elektrolisis?",
            "Bimbing saya menghitung massa endapan perak dengan hukum Faraday 1",
            "Jelaskan mekanisme pencegahan korosi dengan perlindungan katodik"
        ),
        9 to listOf(
            "Jelaskan aturan Markovnikov pada reaksi adisi alkena dengan analogi",
            "Apa perbedaan alkohol primer, sekunder, dan tersier?",
            "Bagaimana cara membedakan aldehida dan keton menggunakan pereaksi Fehling?",
            "Jelaskan reaksi esterifikasi pembuatan etil etanoat"
        ),
        10 to listOf(
            "Apa perbedaan larutan sejati, koloid, dan suspensi?",
            "Jelaskan efek Tyndall dan gerak Brown pada partikel koloid",
            "Bagaimana cara pembuatan koloid secara kondensasi dan dispersi?",
            "Apa fungsi tawas pada proses penjernihan air minum?"
        )
    )

    LaunchedEffect(uiState.chatMessages.size) {
        if (uiState.chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.chatMessages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ChemBgLight)
            .testTag("ai_tutor_screen")
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = ChemWhite,
            shadowElevation = 1.dp
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(ChemIndigoSubtle)
                                .border(1.dp, ChemIndigoBorder, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = ChemIndigo,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "CHEMIA AI",
                                    color = ChemTextDark,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = ChemEmeraldBg,
                                    border = BorderStroke(1.dp, ChemEmerald.copy(alpha = 0.3f))
                                ) {
                                    Text(
                                        text = "Socratic Tutor",
                                        color = ChemEmerald,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Bimbingan konsep, analogi, hitungan, & diagram kimia SMA",
                                color = ChemTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Reset / Clear Chat Button
                    IconButton(
                        onClick = onClearChat,
                        modifier = Modifier.testTag("btn_clear_chat")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Hapus Percakapan",
                            tint = ChemTextMuted
                        )
                    }
                }

                // Socratic Approach Mode Bar
                ScrollableTabRow(
                    selectedTabIndex = selectedModeIndex,
                    edgePadding = 12.dp,
                    containerColor = ChemWhite,
                    indicator = { tabPositions ->
                        SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedModeIndex]),
                            color = socraticModes[selectedModeIndex].color,
                            height = 3.dp
                        )
                    },
                    divider = {}
                ) {
                    socraticModes.forEachIndexed { index, mode ->
                        val isSelected = selectedModeIndex == index
                        Tab(
                            selected = isSelected,
                            onClick = { selectedModeIndex = index },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = mode.icon,
                                        contentDescription = null,
                                        tint = if (isSelected) mode.color else ChemTextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = mode.title,
                                        color = if (isSelected) ChemTextDark else ChemTextMuted,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        )
                    }
                }

                HorizontalDivider(color = ChemBorder, thickness = 1.dp)
            }
        }

        // Active Mode Description Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = socraticModes[selectedModeIndex].color.copy(alpha = 0.08f),
            border = BorderStroke(1.dp, socraticModes[selectedModeIndex].color.copy(alpha = 0.2f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = socraticModes[selectedModeIndex].icon,
                    contentDescription = null,
                    tint = socraticModes[selectedModeIndex].color,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Mode Aktif: ${socraticModes[selectedModeIndex].title} — ${socraticModes[selectedModeIndex].description}",
                    color = ChemTextDark,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Chat Message Stream
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(uiState.chatMessages) { message ->
                EnhancedChatBubble(
                    message = message,
                    onFollowUpClick = { followUpText ->
                        val currentMode = socraticModes[selectedModeIndex].id
                        onSendMessage(followUpText, currentMode)
                    }
                )
            }

            if (uiState.isAiThinking) {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ChemWhite,
                        border = BorderStroke(1.dp, ChemIndigoBorder),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = ChemIndigo,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "CHEMIA AI sedang menyusun penalaran Socratic...",
                                color = ChemIndigo,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Topic Tabs for Quick Prompts
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = ChemWhite
        ) {
            Column {
                HorizontalDivider(color = ChemBorder, thickness = 1.dp)

                ScrollableTabRow(
                    selectedTabIndex = selectedCategoryIndex,
                    edgePadding = 12.dp,
                    containerColor = ChemWhite,
                    indicator = {},
                    divider = {}
                ) {
                    topicCategories.forEachIndexed { index, catName ->
                        val isSelected = selectedCategoryIndex == index
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) ChemIndigoSubtle else ChemSurfaceSubtle,
                            border = BorderStroke(1.dp, if (isSelected) ChemIndigoBorder else ChemBorder),
                            modifier = Modifier
                                .padding(horizontal = 4.dp, vertical = 6.dp)
                                .clickable { selectedCategoryIndex = index }
                        ) {
                            Text(
                                text = catName,
                                color = if (isSelected) ChemIndigo else ChemTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Quick Prompt Chips
                val promptsForCategory = categorizedPrompts[selectedCategoryIndex] ?: categorizedPrompts[0]!!
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(promptsForCategory) { prompt ->
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable {
                                    val currentMode = socraticModes[selectedModeIndex].id
                                    onSendMessage(prompt, currentMode)
                                },
                            color = ChemBgLight,
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, ChemBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = ChemAmber,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = prompt,
                                    color = ChemTextDark,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Input Bar
                HorizontalDivider(color = ChemBorder, thickness = 1.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("ai_tutor_input"),
                        placeholder = {
                            Text(
                                "Tanyakan soal, minta clue, atau bedah konsep...",
                                color = ChemTextMuted,
                                fontSize = 13.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ChemIndigo,
                            unfocusedBorderColor = ChemBorder,
                            focusedTextColor = ChemTextDark,
                            unfocusedTextColor = ChemTextDark,
                            focusedContainerColor = ChemSurfaceSubtle,
                            unfocusedContainerColor = ChemSurfaceSubtle
                        ),
                        shape = RoundedCornerShape(20.dp),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (textInput.isNotBlank()) {
                                val currentMode = socraticModes[selectedModeIndex].id
                                onSendMessage(textInput, currentMode)
                                textInput = ""
                            }
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(ChemIndigo)
                            .testTag("btn_send_ai_message")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Kirim",
                            tint = ChemWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EnhancedChatBubble(
    message: ChatMessage,
    onFollowUpClick: (String) -> Unit
) {
    val isAi = message.sender == "ai"

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isAi) Alignment.Start else Alignment.End
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (isAi) Arrangement.Start else Arrangement.End
        ) {
            if (isAi) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(ChemIndigoSubtle)
                        .border(1.dp, ChemIndigoBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = ChemIndigo,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Surface(
                modifier = Modifier.widthIn(max = 340.dp),
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isAi) 4.dp else 16.dp,
                    bottomEnd = if (isAi) 16.dp else 4.dp
                ),
                color = if (isAi) ChemWhite else ChemIndigo,
                border = if (isAi) BorderStroke(1.dp, ChemBorder) else null,
                shadowElevation = if (isAi) 1.dp else 0.dp
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    if (isAi) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 6.dp)
                        ) {
                            Text(
                                text = "CHEMIA AI",
                                color = ChemIndigo,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Render structured response with enhanced typography & code blocks
                    RenderFormattedMessage(text = message.text, isAi = isAi)
                }
            }
        }

        // Suggested Follow-Up Actions for Socratic Scaffolding
        if (isAi && message.suggestedFollowUps.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                modifier = Modifier
                    .padding(start = 40.dp, top = 2.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                message.suggestedFollowUps.forEach { followUp ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ChemWhite,
                        border = BorderStroke(1.dp, ChemIndigoBorder),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onFollowUpClick(followUp) }
                    ) {
                        Text(
                            text = followUp,
                            color = ChemIndigo,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RenderFormattedMessage(text: String, isAi: Boolean) {
    val lines = text.split("\n")
    var isInCodeBlock = false
    val codeBlockLines = mutableListOf<String>()

    lines.forEach { line ->
        if (line.trim().startsWith("```")) {
            if (isInCodeBlock) {
                // Render accumulated code/diagram block
                val codeContent = codeBlockLines.joinToString("\n")
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ChemSurfaceSubtle,
                    border = BorderStroke(1.dp, ChemBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Text(
                        text = codeContent,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        color = ChemTextDark,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                codeBlockLines.clear()
                isInCodeBlock = false
            } else {
                isInCodeBlock = true
            }
        } else if (isInCodeBlock) {
            codeBlockLines.add(line)
        } else {
            // Normal lines
            if (line.isNotBlank()) {
                val isCallout = line.startsWith("⚠️") || line.startsWith("💡") || line.startsWith("🔬") || line.startsWith("📊") || line.startsWith("⭐")
                
                if (isCallout) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (line.startsWith("⚠️")) ChemRoseBg else if (line.startsWith("💡")) ChemAmberBg else ChemIndigoSubtle,
                        border = BorderStroke(
                            1.dp,
                            if (line.startsWith("⚠️")) ChemRose.copy(alpha = 0.3f) else if (line.startsWith("💡")) ChemAmber.copy(alpha = 0.3f) else ChemIndigoBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = line,
                            color = ChemTextDark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 17.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                } else {
                    Text(
                        text = line,
                        color = if (isAi) ChemTextDark else ChemWhite,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        fontWeight = if (line.startsWith("**") && line.endsWith("**")) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
