package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.chemistry.PeriodicTableData
import com.example.data.models.PeriodicElement
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
import com.example.ui.theme.ChemRose
import com.example.ui.theme.ChemRoseBg
import com.example.ui.theme.ChemSurfaceSubtle
import com.example.ui.theme.ChemTextDark
import com.example.ui.theme.ChemTextMuted
import com.example.ui.theme.ChemTextPrimary
import com.example.ui.theme.ChemTextSecondary
import com.example.ui.theme.ChemWhite
import com.example.viewmodel.MainUiState
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SimulationsScreen(
    uiState: MainUiState,
    onSelectElement: (PeriodicElement) -> Unit,
    onSearchElement: (String) -> Unit,
    onFilterElementCategory: (String) -> Unit,
    onUpdateAtomSubatomic: (Int, Int, Int) -> Unit,
    onUpdateGas: (Float?, Float?, Float?, Float?) -> Unit,
    onUpdateTitration: (Float) -> Unit
) {
    var selectedSimTab by remember { mutableIntStateOf(0) }
    val simTabs = listOf(
        "Tabel Periodik",
        "Model Atom Bohr",
        "Hukum Gas Ideal",
        "Asas Le Chatelier",
        "Simulasi Titrasi pH"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ChemBgLight)
            .testTag("simulations_screen")
    ) {
        // Tab Selector
        ScrollableTabRow(
            selectedTabIndex = selectedSimTab,
            containerColor = ChemWhite,
            contentColor = ChemIndigo,
            edgePadding = 16.dp,
            divider = { HorizontalDivider(color = ChemBorder, thickness = 1.dp) },
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedSimTab]),
                    color = ChemIndigo
                )
            }
        ) {
            simTabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedSimTab == index,
                    onClick = { selectedSimTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (selectedSimTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedSimTab == index) ChemIndigo else ChemTextSecondary
                        )
                    }
                )
            }
        }

        when (selectedSimTab) {
            0 -> PeriodicTableExplorerView(
                uiState = uiState,
                onSelectElement = onSelectElement,
                onSearchElement = onSearchElement,
                onFilterCategory = onFilterElementCategory
            )
            1 -> AtomBuilderView(
                protons = uiState.simAtomProtons,
                neutrons = uiState.simAtomNeutrons,
                electrons = uiState.simAtomElectrons,
                onUpdate = onUpdateAtomSubatomic
            )
            2 -> IdealGasLawView(
                pressure = uiState.simGasPressure,
                volume = uiState.simGasVolume,
                temp = uiState.simGasTemp,
                moles = uiState.simGasMoles,
                onUpdate = onUpdateGas
            )
            3 -> LeChatelierSimulatorView()
            4 -> TitrationSimulatorView(
                volumeBasaAdded = uiState.simTitrationVolBasaAdded,
                onVolumeChange = onUpdateTitration
            )
        }
    }
}

// 1. PERIODIC TABLE EXPLORER
@Composable
fun PeriodicTableExplorerView(
    uiState: MainUiState,
    onSelectElement: (PeriodicElement) -> Unit,
    onSearchElement: (String) -> Unit,
    onFilterCategory: (String) -> Unit
) {
    val categories = listOf("Semua", "Nonlogam", "Logam Alkali", "Logam Alkali Tanah", "Logam Transisi", "Halogen", "Gas Mulia", "Metaloid")
    val selectedElement = uiState.selectedElement

    val filteredElements = PeriodicTableData.elements.filter { elem ->
        val matchesSearch = elem.symbol.contains(uiState.elementSearchQuery, ignoreCase = true) ||
                elem.indonesianName.contains(uiState.elementSearchQuery, ignoreCase = true) ||
                elem.atomicNumber.toString() == uiState.elementSearchQuery.trim()
        val matchesCategory = uiState.elementCategoryFilter == "Semua" || elem.category == uiState.elementCategoryFilter
        matchesSearch && matchesCategory
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Search Input
            OutlinedTextField(
                value = uiState.elementSearchQuery,
                onValueChange = onSearchElement,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("element_search_input"),
                placeholder = { Text("Cari unsur (misal: Na, Fe, Hidrogen, 17)", color = ChemTextMuted, fontSize = 13.sp) },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = ChemIndigo) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ChemIndigo,
                    unfocusedBorderColor = ChemBorder,
                    focusedTextColor = ChemTextDark,
                    unfocusedTextColor = ChemTextDark,
                    focusedContainerColor = ChemWhite,
                    unfocusedContainerColor = ChemWhite
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            // Category Chips Row
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = uiState.elementCategoryFilter == cat
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onFilterCategory(cat) },
                        color = if (isSelected) ChemIndigo else ChemWhite,
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, if (isSelected) ChemIndigo else ChemBorder)
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) ChemWhite else ChemTextDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        item {
            // Horizontal Elements Cards
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filteredElements) { element ->
                    val isSelected = element.atomicNumber == selectedElement.atomicNumber
                    Card(
                        modifier = Modifier
                            .size(72.dp, 80.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onSelectElement(element) }
                            .testTag("element_cell_${element.symbol}"),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) ChemIndigoSubtle else ChemWhite
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) ChemIndigo else ChemBorder
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "${element.atomicNumber}", color = ChemTextMuted, fontSize = 9.sp)
                            Text(text = element.symbol, color = Color(element.colorHex), fontSize = 18.sp, fontWeight = FontWeight.Black)
                            Text(text = element.indonesianName, color = ChemTextDark, fontSize = 8.sp, maxLines = 1)
                        }
                    }
                }
            }
        }

        // Element Detail Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("element_detail_card"),
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
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(selectedElement.colorHex).copy(alpha = 0.15f))
                                    .border(1.dp, Color(selectedElement.colorHex).copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = selectedElement.symbol,
                                    color = Color(selectedElement.colorHex),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "${selectedElement.indonesianName} (${selectedElement.name})",
                                    color = ChemTextDark,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Nomor Atom: ${selectedElement.atomicNumber} • Massa: ${selectedElement.atomicMass} g/mol",
                                    color = ChemIndigo,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = ChemSurfaceSubtle,
                            border = BorderStroke(1.dp, ChemBorder)
                        ) {
                            Text(
                                text = selectedElement.category,
                                color = Color(selectedElement.colorHex),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Physical & Periodic Properties Grid
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(ChemSurfaceSubtle)
                            .border(1.dp, ChemBorder, RoundedCornerShape(10.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Golongan / Periode:", color = ChemTextSecondary, fontSize = 12.sp)
                            Text(text = "Golongan ${selectedElement.group}, Periode ${selectedElement.period}", color = ChemTextDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Konfigurasi Elektron:", color = ChemTextSecondary, fontSize = 12.sp)
                            Text(text = selectedElement.electronConfig, color = ChemIndigo, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Keelektronegatifan:", color = ChemTextSecondary, fontSize = 12.sp)
                            Text(text = "${selectedElement.electronegativity} (Pauling)", color = ChemTextDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Energi Ionisasi Pertama:", color = ChemTextSecondary, fontSize = 12.sp)
                            Text(text = "${selectedElement.ionizationEnergyKj} kJ/mol", color = ChemTextDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Jari-Jari Atom:", color = ChemTextSecondary, fontSize = 12.sp)
                            Text(text = "${selectedElement.atomicRadiusPm} pm", color = ChemTextDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "Ringkasan Kimia:", color = ChemIndigo, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = selectedElement.summary, color = ChemTextPrimary, fontSize = 12.sp, lineHeight = 16.sp)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "💡 Fakta Menarik:", color = ChemAmber, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = selectedElement.funFact, color = ChemTextPrimary, fontSize = 12.sp, lineHeight = 16.sp)
                }
            }
        }
    }
}

// 2. ATOM BUILDER & SHELL VISUALIZER
@Composable
fun AtomBuilderView(
    protons: Int,
    neutrons: Int,
    electrons: Int,
    onUpdate: (Int, Int, Int) -> Unit
) {
    val massNumber = protons + neutrons
    val charge = protons - electrons
    val chargeLabel = when {
        charge > 0 -> "+$charge (Kation)"
        charge < 0 -> "$charge (Anion)"
        else -> "0 (Netral)"
    }

    // Electron Shell Calculation (Bohr: K=2, L=8, M=8, N=2 up to 20)
    var remainingE = electrons
    val shellK = remainingE.coerceAtMost(2).also { remainingE -= it }
    val shellL = remainingE.coerceAtMost(8).also { remainingE -= it }
    val shellM = remainingE.coerceAtMost(8).also { remainingE -= it }
    val shellN = remainingE.coerceAtMost(2)

    val elementName = when (protons) {
        1 -> "Hidrogen (H)"
        2 -> "Helium (He)"
        3 -> "Litium (Li)"
        4 -> "Berilium (Be)"
        5 -> "Boron (B)"
        6 -> "Karbon (C)"
        7 -> "Nitrogen (N)"
        8 -> "Oksigen (O)"
        9 -> "Fluor (F)"
        10 -> "Neon (Ne)"
        11 -> "Natrium (Na)"
        12 -> "Magnesium (Mg)"
        13 -> "Aluminium (Al)"
        14 -> "Silikon (Si)"
        15 -> "Fosfor (P)"
        16 -> "Belerang (S)"
        17 -> "Klorin (Cl)"
        18 -> "Argon (Ar)"
        19 -> "Kalium (K)"
        20 -> "Kalsium (Ca)"
        else -> "Unsur Z=$protons"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ChemWhite),
                border = BorderStroke(1.dp, ChemBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Model Atom & Kulit Elektron Bohr",
                        color = ChemIndigo,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$elementName • Massa: $massNumber • Muatan: $chargeLabel",
                        color = ChemTextDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Concentric Shell Canvas
                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .clip(CircleShape)
                            .background(ChemSurfaceSubtle)
                            .border(1.dp, ChemBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(240.dp)) {
                            val center = Offset(size.width / 2f, size.height / 2f)

                            // Nucleus
                            drawCircle(color = ChemIndigo, radius = 24.dp.toPx(), center = center)

                            // Shell K
                            drawCircle(color = Color(0xFFCBD5E1), radius = 45.dp.toPx(), center = center, style = Stroke(width = 1.5f))
                            for (i in 0 until shellK) {
                                val angle = (i * 2 * Math.PI / shellK).toFloat()
                                val x = center.x + 45.dp.toPx() * cos(angle)
                                val y = center.y + 45.dp.toPx() * sin(angle)
                                drawCircle(color = ChemIndigo, radius = 4.dp.toPx(), center = Offset(x, y))
                            }

                            // Shell L
                            if (shellL > 0) {
                                drawCircle(color = Color(0xFFCBD5E1), radius = 70.dp.toPx(), center = center, style = Stroke(width = 1.5f))
                                for (i in 0 until shellL) {
                                    val angle = (i * 2 * Math.PI / shellL).toFloat()
                                    val x = center.x + 70.dp.toPx() * cos(angle)
                                    val y = center.y + 70.dp.toPx() * sin(angle)
                                    drawCircle(color = ChemIndigo, radius = 4.dp.toPx(), center = Offset(x, y))
                                }
                            }

                            // Shell M
                            if (shellM > 0) {
                                drawCircle(color = Color(0xFFCBD5E1), radius = 95.dp.toPx(), center = center, style = Stroke(width = 1.5f))
                                for (i in 0 until shellM) {
                                    val angle = (i * 2 * Math.PI / shellM).toFloat()
                                    val x = center.x + 95.dp.toPx() * cos(angle)
                                    val y = center.y + 95.dp.toPx() * sin(angle)
                                    drawCircle(color = ChemIndigo, radius = 4.dp.toPx(), center = Offset(x, y))
                                }
                            }

                            // Shell N
                            if (shellN > 0) {
                                drawCircle(color = Color(0xFFCBD5E1), radius = 115.dp.toPx(), center = center, style = Stroke(width = 1.5f))
                                for (i in 0 until shellN) {
                                    val angle = (i * 2 * Math.PI / shellN).toFloat()
                                    val x = center.x + 115.dp.toPx() * cos(angle)
                                    val y = center.y + 115.dp.toPx() * sin(angle)
                                    drawCircle(color = ChemIndigo, radius = 4.dp.toPx(), center = Offset(x, y))
                                }
                            }
                        }

                        Text(
                            text = "${protons}p\n${neutrons}n",
                            color = ChemWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Distribusi Kulit: K=$shellK, L=$shellL, M=$shellM, N=$shellN",
                        color = ChemIndigo,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Sliders for P, N, E
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ChemWhite),
                border = BorderStroke(1.dp, ChemBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Kontrol Partikel Subatomik:", color = ChemTextDark, fontSize = 14.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "Proton (Nomor Atom Z): $protons", color = ChemRose, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Slider(
                        value = protons.toFloat(),
                        onValueChange = { onUpdate(it.toInt(), neutrons, electrons) },
                        valueRange = 1f..20f,
                        steps = 18,
                        colors = SliderDefaults.colors(thumbColor = ChemRose, activeTrackColor = ChemRose)
                    )

                    Text(text = "Neutron (n⁰): $neutrons", color = ChemAmber, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Slider(
                        value = neutrons.toFloat(),
                        onValueChange = { onUpdate(protons, it.toInt(), electrons) },
                        valueRange = 0f..24f,
                        steps = 23,
                        colors = SliderDefaults.colors(thumbColor = ChemAmber, activeTrackColor = ChemAmber)
                    )

                    Text(text = "Elektron (e⁻): $electrons", color = ChemIndigo, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Slider(
                        value = electrons.toFloat(),
                        onValueChange = { onUpdate(protons, neutrons, it.toInt()) },
                        valueRange = 1f..20f,
                        steps = 18,
                        colors = SliderDefaults.colors(thumbColor = ChemIndigo, activeTrackColor = ChemIndigo)
                    )
                }
            }
        }
    }
}

// 3. IDEAL GAS LAW SIMULATOR
@Composable
fun IdealGasLawView(
    pressure: Float,
    volume: Float,
    temp: Float,
    moles: Float,
    onUpdate: (Float?, Float?, Float?, Float?) -> Unit
) {
    val r = 0.0821f
    val calculatedPressure = (moles * r * temp) / volume

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ChemWhite),
                border = BorderStroke(1.dp, ChemBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Persamaan Keadaan Gas Ideal (PV = nRT)", color = ChemIndigo, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tekanan P = ${String.format("%.3f", calculatedPressure)} atm",
                        color = ChemEmerald,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .size(200.dp, (volume * 6).coerceIn(60f, 180f).dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ChemSurfaceSubtle)
                            .border(1.dp, ChemBorder, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ruang Gas: ${String.format("%.1f", volume)} L\nSuhu: ${String.format("%.1f", temp)} K (${String.format("%.1f", temp - 273.15f)}°C)",
                            color = ChemTextDark,
                            fontSize = 11.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ChemWhite),
                border = BorderStroke(1.dp, ChemBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Volume Wadah (V): ${String.format("%.1f", volume)} Liter", color = ChemIndigo, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Slider(
                        value = volume,
                        onValueChange = { onUpdate(null, it, null, null) },
                        valueRange = 5f..50f,
                        colors = SliderDefaults.colors(thumbColor = ChemIndigo, activeTrackColor = ChemIndigo)
                    )

                    Text(text = "Suhu Gas (T): ${String.format("%.1f", temp)} Kelvin", color = ChemAmber, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Slider(
                        value = temp,
                        onValueChange = { onUpdate(null, null, it, null) },
                        valueRange = 100f..600f,
                        colors = SliderDefaults.colors(thumbColor = ChemAmber, activeTrackColor = ChemAmber)
                    )

                    Text(text = "Jumlah Mol Gas (n): ${String.format("%.2f", moles)} mol", color = ChemEmerald, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Slider(
                        value = moles,
                        onValueChange = { onUpdate(null, null, null, it) },
                        valueRange = 0.2f..5.0f,
                        colors = SliderDefaults.colors(thumbColor = ChemEmerald, activeTrackColor = ChemEmerald)
                    )
                }
            }
        }
    }
}

// 4. LE CHATELIER EQUILIBRIUM SIMULATOR
@Composable
fun LeChatelierSimulatorView() {
    var temp by remember { mutableIntStateOf(300) }
    var pressureMultiplier by remember { mutableIntStateOf(1) }
    var n2Add by remember { mutableIntStateOf(0) }

    val shiftDirection = when {
        temp > 300 -> "Bergeser ke KIRI (Endoterm/Reaktan N₂ + H₂)"
        temp < 300 -> "Bergeser ke KANAN (Eksoterm/Produk NH₃)"
        pressureMultiplier > 1 -> "Bergeser ke KANAN (Koefisien gas kanan 2 < kiri 4)"
        n2Add > 0 -> "Bergeser ke KANAN (Menjauhi N₂)"
        else -> "Berada dalam Kesetimbangan Dinamis"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ChemWhite),
                border = BorderStroke(1.dp, ChemBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Simulasi Pergeseran Asas Le Chatelier",
                        color = ChemIndigo,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(ChemSurfaceSubtle)
                            .border(1.dp, ChemBorder, RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "N₂(g) + 3H₂(g) ⇌ 2NH₃(g)   ΔH = -92 kJ",
                            color = ChemAmber,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Respon Sistem:", color = ChemTextDark, fontSize = 12.sp)
                    Text(
                        text = shiftDirection,
                        color = if (shiftDirection.contains("KANAN")) ChemEmerald else if (shiftDirection.contains("KIRI")) ChemRose else ChemIndigo,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ChemWhite),
                border = BorderStroke(1.dp, ChemBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Suhu Sistem: $temp K", color = ChemTextDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Slider(
                        value = temp.toFloat(),
                        onValueChange = { temp = it.toInt() },
                        valueRange = 200f..600f,
                        colors = SliderDefaults.colors(thumbColor = ChemAmber, activeTrackColor = ChemAmber)
                    )

                    Text(text = "Tekanan Gas: ${pressureMultiplier}x", color = ChemTextDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Slider(
                        value = pressureMultiplier.toFloat(),
                        onValueChange = { pressureMultiplier = it.toInt() },
                        valueRange = 1f..5f,
                        steps = 3,
                        colors = SliderDefaults.colors(thumbColor = ChemIndigo, activeTrackColor = ChemIndigo)
                    )
                }
            }
        }
    }
}

// 5. TITRATION & PH SIMULATOR
@Composable
fun TitrationSimulatorView(
    volumeBasaAdded: Float,
    onVolumeChange: (Float) -> Unit
) {
    // Titration of 25 mL 0.1 M HCl with 0.1 M NaOH
    // Equivalence point is at exactly 25.0 mL
    val ph: Float = when {
        volumeBasaAdded < 25f -> {
            val hLeft = (25f * 0.1f) - (volumeBasaAdded * 0.1f)
            val totalV = 25f + volumeBasaAdded
            val concH = (hLeft / totalV).coerceAtLeast(1e-7f)
            (-kotlin.math.log10(concH.toDouble())).toFloat()
        }
        volumeBasaAdded == 25f -> 7.0f
        else -> {
            val ohExcess = (volumeBasaAdded * 0.1f) - (25f * 0.1f)
            val totalV = 25f + volumeBasaAdded
            val concOh = (ohExcess / totalV).coerceAtLeast(1e-7f)
            val poh = -kotlin.math.log10(concOh.toDouble()).toFloat()
            14.0f - poh
        }
    }

    val indicatorColor = when {
        ph < 8.2f -> Color.Transparent
        ph in 8.2f..10.0f -> Color(0xFFF472B6).copy(alpha = 0.5f)
        else -> Color(0xFFE11D48).copy(alpha = 0.85f)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ChemWhite),
                border = BorderStroke(1.dp, ChemBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Titrasi Asam Kuat (HCl) dengan Basa Kuat (NaOH)",
                        color = ChemIndigo,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "pH = ${String.format("%.2f", ph)}",
                        color = if (ph < 7f) ChemRose else if (ph > 7f) ChemIndigo else ChemEmerald,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Erlenmeyer Flask Sim
                    Box(
                        modifier = Modifier
                            .size(120.dp, 100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ChemSurfaceSubtle)
                            .border(1.dp, ChemBorder, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(indicatorColor, RoundedCornerShape(12.dp))
                        )
                        Text(
                            text = "Indikator PP:\n${if (ph >= 8.2f) "Merah Muda (Basa)" else "Tak Berwarna (Asam)"}",
                            color = ChemTextDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ChemWhite),
                border = BorderStroke(1.dp, ChemBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Volume NaOH 0.1 M Ditambahkan: ${String.format("%.1f", volumeBasaAdded)} mL",
                        color = ChemTextDark,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Titik Ekuivalen (pH = 7) tercapai tepat pada 25.0 mL",
                        color = ChemIndigo,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Slider(
                        value = volumeBasaAdded,
                        onValueChange = onVolumeChange,
                        valueRange = 0f..50f,
                        colors = SliderDefaults.colors(thumbColor = ChemIndigo, activeTrackColor = ChemIndigo)
                    )
                }
            }
        }
    }
}
