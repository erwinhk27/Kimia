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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.chemistry.CalcResult
import com.example.data.chemistry.CalculatorsEngine
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
fun CalculatorsScreen() {
    var selectedCalcIndex by remember { mutableIntStateOf(0) }
    val calcTitles = listOf(
        "Massa Molar (Mr)",
        "Konversi Mol",
        "Molaritas (M)",
        "Pengenceran (M₁V₁)",
        "Kalkulator pH",
        "Gas Ideal (PV=nRT)",
        "Pereaksi Pembatas",
        "Komposisi Persen",
        "Angka Penting",
        "Bilangan Oksidasi"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ChemBgLight)
            .testTag("calculators_screen")
    ) {
        // Tab Header
        ScrollableTabRow(
            selectedTabIndex = selectedCalcIndex,
            containerColor = ChemWhite,
            contentColor = ChemIndigo,
            edgePadding = 16.dp,
            divider = { HorizontalDivider(color = ChemBorder, thickness = 1.dp) },
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedCalcIndex]),
                    color = ChemIndigo
                )
            }
        ) {
            calcTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedCalcIndex == index,
                    onClick = { selectedCalcIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (selectedCalcIndex == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedCalcIndex == index) ChemIndigo else ChemTextSecondary
                        )
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            when (selectedCalcIndex) {
                0 -> item { MolarMassCalculatorView() }
                1 -> item { MoleConverterView() }
                2 -> item { MolarityCalculatorView() }
                3 -> item { DilutionCalculatorView() }
                4 -> item { PhCalculatorView() }
                5 -> item { GasLawCalculatorView() }
                6 -> item { LimitingReagentCalculatorView() }
                7 -> item { PercentCompositionView() }
                8 -> item { SignificantFiguresView() }
                9 -> item { OxidationNumberView() }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

// 1. MOLAR MASS CALCULATOR
@Composable
fun MolarMassCalculatorView() {
    var formulaInput by remember { mutableStateOf("H2SO4") }
    var result by remember { mutableStateOf<CalcResult?>(CalculatorsEngine.calculateMolarMass("H2SO4")) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ChemWhite),
        border = BorderStroke(1.dp, ChemBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Kalkulator Massa Molar (Mr)", color = ChemTextDark, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = "Ketik rumus kimia lengkap (contoh: H2SO4, Ca(OH)2, Al2(SO4)3, C6H12O6)", color = ChemTextSecondary, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = formulaInput,
                onValueChange = {
                    formulaInput = it
                    result = CalculatorsEngine.calculateMolarMass(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mr_formula_input"),
                label = { Text("Rumus Kimia Senyawa", color = ChemIndigo) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ChemIndigo,
                    unfocusedBorderColor = ChemBorder,
                    focusedTextColor = ChemTextDark,
                    unfocusedTextColor = ChemTextDark,
                    focusedContainerColor = ChemSurfaceSubtle,
                    unfocusedContainerColor = ChemSurfaceSubtle
                ),
                shape = RoundedCornerShape(10.dp)
            )

            result?.let { res ->
                Spacer(modifier = Modifier.height(14.dp))
                ResultStepBreakdown(result = res)
            }
        }
    }
}

// 2. MOLE CONVERTER
@Composable
fun MoleConverterView() {
    var inputVal by remember { mutableStateOf("9.8") }
    var molarMass by remember { mutableStateOf("98") }
    var inputType by remember { mutableStateOf("Massa (g)") }
    var result by remember { mutableStateOf<CalcResult?>(CalculatorsEngine.convertMoles(9.8, "Massa (g)", 98.0)) }

    fun calculate() {
        val v = inputVal.toDoubleOrNull() ?: 0.0
        val mr = molarMass.toDoubleOrNull() ?: 98.0
        result = CalculatorsEngine.convertMoles(v, inputType, mr)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ChemWhite),
        border = BorderStroke(1.dp, ChemBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Jembatan Konversi Mol Stoikiometri", color = ChemTextDark, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Massa (g)", "Mol (mol)", "Volume STP (L)").forEach { type ->
                    val isSel = inputType == type
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp)),
                        color = if (isSel) ChemIndigo else ChemSurfaceSubtle,
                        border = BorderStroke(1.dp, if (isSel) ChemIndigo else ChemBorder)
                    ) {
                        Text(
                            text = type,
                            color = if (isSel) ChemWhite else ChemTextDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .clickable {
                                    inputType = type
                                    calculate()
                                },
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = inputVal,
                onValueChange = { inputVal = it; calculate() },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nilai Input ($inputType)", color = ChemIndigo) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ChemIndigo,
                    unfocusedBorderColor = ChemBorder,
                    focusedTextColor = ChemTextDark,
                    unfocusedTextColor = ChemTextDark,
                    focusedContainerColor = ChemSurfaceSubtle,
                    unfocusedContainerColor = ChemSurfaceSubtle
                ),
                shape = RoundedCornerShape(10.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = molarMass,
                onValueChange = { molarMass = it; calculate() },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Massa Molar Mr (g/mol)", color = ChemIndigo) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ChemIndigo,
                    unfocusedBorderColor = ChemBorder,
                    focusedTextColor = ChemTextDark,
                    unfocusedTextColor = ChemTextDark,
                    focusedContainerColor = ChemSurfaceSubtle,
                    unfocusedContainerColor = ChemSurfaceSubtle
                ),
                shape = RoundedCornerShape(10.dp)
            )

            result?.let { res ->
                Spacer(modifier = Modifier.height(14.dp))
                ResultStepBreakdown(result = res)
            }
        }
    }
}

// 3. MOLARITY CALCULATOR
@Composable
fun MolarityCalculatorView() {
    var mass by remember { mutableStateOf("4.0") } // g NaOH
    var mr by remember { mutableStateOf("40") }
    var volMl by remember { mutableStateOf("500") }
    var result by remember { mutableStateOf<CalcResult?>(CalculatorsEngine.calculateMolarity(4.0, 40.0, 500.0)) }

    fun calc() {
        val m = mass.toDoubleOrNull() ?: 0.0
        val r = mr.toDoubleOrNull() ?: 40.0
        val v = volMl.toDoubleOrNull() ?: 500.0
        result = CalculatorsEngine.calculateMolarity(m, r, v)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ChemWhite),
        border = BorderStroke(1.dp, ChemBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Kalkulator Molaritas (M)", color = ChemTextDark, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = mass,
                onValueChange = { mass = it; calc() },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Massa Zat Terlarut (gram)", color = ChemIndigo) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ChemIndigo,
                    unfocusedBorderColor = ChemBorder,
                    focusedTextColor = ChemTextDark,
                    unfocusedTextColor = ChemTextDark,
                    focusedContainerColor = ChemSurfaceSubtle,
                    unfocusedContainerColor = ChemSurfaceSubtle
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = mr,
                onValueChange = { mr = it; calc() },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Massa Molar Mr (g/mol)", color = ChemIndigo) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ChemIndigo,
                    unfocusedBorderColor = ChemBorder,
                    focusedTextColor = ChemTextDark,
                    unfocusedTextColor = ChemTextDark,
                    focusedContainerColor = ChemSurfaceSubtle,
                    unfocusedContainerColor = ChemSurfaceSubtle
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = volMl,
                onValueChange = { volMl = it; calc() },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Volume Larutan (mL)", color = ChemIndigo) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ChemIndigo,
                    unfocusedBorderColor = ChemBorder,
                    focusedTextColor = ChemTextDark,
                    unfocusedTextColor = ChemTextDark,
                    focusedContainerColor = ChemSurfaceSubtle,
                    unfocusedContainerColor = ChemSurfaceSubtle
                )
            )

            result?.let { res ->
                Spacer(modifier = Modifier.height(14.dp))
                ResultStepBreakdown(result = res)
            }
        }
    }
}

// 4. DILUTION CALCULATOR
@Composable
fun DilutionCalculatorView() {
    var m1 by remember { mutableStateOf("2.0") }
    var v1 by remember { mutableStateOf("50") }
    var m2 by remember { mutableStateOf("0.5") }
    var v2 by remember { mutableStateOf("200") }
    var target by remember { mutableStateOf("V2 (Volume Akhir mL)") }
    var result by remember { mutableStateOf<CalcResult?>(CalculatorsEngine.calculateDilution(2.0, 50.0, 0.5, 200.0, "V2 (Volume Akhir mL)")) }

    fun calc() {
        val numM1 = m1.toDoubleOrNull() ?: 1.0
        val numV1 = v1.toDoubleOrNull() ?: 1.0
        val numM2 = m2.toDoubleOrNull() ?: 1.0
        val numV2 = v2.toDoubleOrNull() ?: 1.0
        result = CalculatorsEngine.calculateDilution(numM1, numV1, numM2, numV2, target)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ChemWhite),
        border = BorderStroke(1.dp, ChemBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Hukum Pengenceran (M₁ × V₁ = M₂ × V₂)", color = ChemTextDark, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(value = m1, onValueChange = { m1 = it; calc() }, modifier = Modifier.fillMaxWidth(), label = { Text("M1: Konsentrasi Awal (M)", color = ChemIndigo) }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ChemIndigo, unfocusedBorderColor = ChemBorder, focusedTextColor = ChemTextDark, unfocusedTextColor = ChemTextDark, focusedContainerColor = ChemSurfaceSubtle, unfocusedContainerColor = ChemSurfaceSubtle))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(value = v1, onValueChange = { v1 = it; calc() }, modifier = Modifier.fillMaxWidth(), label = { Text("V1: Volume Awal (mL)", color = ChemIndigo) }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ChemIndigo, unfocusedBorderColor = ChemBorder, focusedTextColor = ChemTextDark, unfocusedTextColor = ChemTextDark, focusedContainerColor = ChemSurfaceSubtle, unfocusedContainerColor = ChemSurfaceSubtle))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(value = m2, onValueChange = { m2 = it; calc() }, modifier = Modifier.fillMaxWidth(), label = { Text("M2: Konsentrasi Akhir (M)", color = ChemIndigo) }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ChemIndigo, unfocusedBorderColor = ChemBorder, focusedTextColor = ChemTextDark, unfocusedTextColor = ChemTextDark, focusedContainerColor = ChemSurfaceSubtle, unfocusedContainerColor = ChemSurfaceSubtle))

            result?.let { res ->
                Spacer(modifier = Modifier.height(14.dp))
                ResultStepBreakdown(result = res)
            }
        }
    }
}

// 5. PH CALCULATOR
@Composable
fun PhCalculatorView() {
    var subType by remember { mutableStateOf("Asam Kuat") }
    var concM by remember { mutableStateOf("0.01") }
    var valency by remember { mutableStateOf("1") }
    var result by remember { mutableStateOf<CalcResult?>(CalculatorsEngine.calculatePh("Asam Kuat", 0.01, 1)) }

    fun calc() {
        val c = concM.toDoubleOrNull() ?: 0.01
        val v = valency.toIntOrNull() ?: 1
        result = CalculatorsEngine.calculatePh(subType, c, v)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ChemWhite),
        border = BorderStroke(1.dp, ChemBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Kalkulator pH & pOH Asam-Basa", color = ChemTextDark, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("Asam Kuat", "Basa Kuat", "Asam Lemah", "Basa Lemah").forEach { t ->
                    val isSel = subType == t
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp)),
                        color = if (isSel) ChemIndigo else ChemSurfaceSubtle,
                        border = BorderStroke(1.dp, if (isSel) ChemIndigo else ChemBorder)
                    ) {
                        Text(
                            text = t,
                            color = if (isSel) ChemWhite else ChemTextDark,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .clickable { subType = t; calc() },
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(value = concM, onValueChange = { concM = it; calc() }, modifier = Modifier.fillMaxWidth(), label = { Text("Konsentrasi Molaritas (M)", color = ChemIndigo) }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ChemIndigo, unfocusedBorderColor = ChemBorder, focusedTextColor = ChemTextDark, unfocusedTextColor = ChemTextDark, focusedContainerColor = ChemSurfaceSubtle, unfocusedContainerColor = ChemSurfaceSubtle))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(value = valency, onValueChange = { valency = it; calc() }, modifier = Modifier.fillMaxWidth(), label = { Text("Valensi Asam/Basa (misal: 1 utk HCl/NaOH, 2 utk H2SO4/Ca(OH)2)", color = ChemIndigo) }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ChemIndigo, unfocusedBorderColor = ChemBorder, focusedTextColor = ChemTextDark, unfocusedTextColor = ChemTextDark, focusedContainerColor = ChemSurfaceSubtle, unfocusedContainerColor = ChemSurfaceSubtle))

            result?.let { res ->
                Spacer(modifier = Modifier.height(14.dp))
                ResultStepBreakdown(result = res)
            }
        }
    }
}

// 6. GAS LAW CALCULATOR
@Composable
fun GasLawCalculatorView() {
    var p by remember { mutableStateOf("1.0") }
    var v by remember { mutableStateOf("") } // target
    var n by remember { mutableStateOf("0.5") }
    var t by remember { mutableStateOf("25") }
    var result by remember { mutableStateOf<CalcResult?>(CalculatorsEngine.calculateGasLaw(1.0, null, 0.5, 25.0)) }

    fun calc() {
        val numP = p.toDoubleOrNull()
        val numV = v.toDoubleOrNull()
        val numN = n.toDoubleOrNull()
        val numT = t.toDoubleOrNull()
        result = CalculatorsEngine.calculateGasLaw(numP, numV, numN, numT)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ChemWhite),
        border = BorderStroke(1.dp, ChemBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Hukum Gas Ideal (PV = nRT)", color = ChemTextDark, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = "Kosongkan tepat 1 variabel yang ingin dihitung (P, V, n, atau T)", color = ChemTextSecondary, fontSize = 11.sp)

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(value = p, onValueChange = { p = it; calc() }, modifier = Modifier.fillMaxWidth(), label = { Text("Tekanan P (atm)", color = ChemIndigo) }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ChemIndigo, unfocusedBorderColor = ChemBorder, focusedTextColor = ChemTextDark, unfocusedTextColor = ChemTextDark, focusedContainerColor = ChemSurfaceSubtle, unfocusedContainerColor = ChemSurfaceSubtle))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(value = v, onValueChange = { v = it; calc() }, modifier = Modifier.fillMaxWidth(), label = { Text("Volume V (Liter) [Kosongkan jika dihitung]", color = ChemIndigo) }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ChemIndigo, unfocusedBorderColor = ChemBorder, focusedTextColor = ChemTextDark, unfocusedTextColor = ChemTextDark, focusedContainerColor = ChemSurfaceSubtle, unfocusedContainerColor = ChemSurfaceSubtle))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(value = n, onValueChange = { n = it; calc() }, modifier = Modifier.fillMaxWidth(), label = { Text("Mol n (mol)", color = ChemIndigo) }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ChemIndigo, unfocusedBorderColor = ChemBorder, focusedTextColor = ChemTextDark, unfocusedTextColor = ChemTextDark, focusedContainerColor = ChemSurfaceSubtle, unfocusedContainerColor = ChemSurfaceSubtle))
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(value = t, onValueChange = { t = it; calc() }, modifier = Modifier.fillMaxWidth(), label = { Text("Suhu T (°C)", color = ChemIndigo) }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ChemIndigo, unfocusedBorderColor = ChemBorder, focusedTextColor = ChemTextDark, unfocusedTextColor = ChemTextDark, focusedContainerColor = ChemSurfaceSubtle, unfocusedContainerColor = ChemSurfaceSubtle))

            result?.let { res ->
                Spacer(modifier = Modifier.height(14.dp))
                ResultStepBreakdown(result = res)
            }
        }
    }
}

// 7. LIMITING REAGENT CALCULATOR
@Composable
fun LimitingReagentCalculatorView() {
    var coeffA by remember { mutableStateOf("1") }
    var molA by remember { mutableStateOf("2.0") }
    var coeffB by remember { mutableStateOf("3") }
    var molB by remember { mutableStateOf("5.0") }
    var result by remember { mutableStateOf<CalcResult?>(CalculatorsEngine.calculateLimitingReagent(1, 2.0, 3, 5.0, "N2", "H2")) }

    fun calc() {
        val cA = coeffA.toIntOrNull() ?: 1
        val mA = molA.toDoubleOrNull() ?: 2.0
        val cB = coeffB.toIntOrNull() ?: 1
        val mB = molB.toDoubleOrNull() ?: 5.0
        result = CalculatorsEngine.calculateLimitingReagent(cA, mA, cB, mB, "Reaktan A", "Reaktan B")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ChemWhite),
        border = BorderStroke(1.dp, ChemBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Kalkulator Pereaksi Pembatas (aA + bB)", color = ChemTextDark, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = coeffA, onValueChange = { coeffA = it; calc() }, modifier = Modifier.weight(1f), label = { Text("Koefisien A", color = ChemIndigo, fontSize = 11.sp) }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ChemIndigo, unfocusedBorderColor = ChemBorder, focusedTextColor = ChemTextDark, unfocusedTextColor = ChemTextDark, focusedContainerColor = ChemSurfaceSubtle, unfocusedContainerColor = ChemSurfaceSubtle))
                OutlinedTextField(value = molA, onValueChange = { molA = it; calc() }, modifier = Modifier.weight(1f), label = { Text("Mol A (mol)", color = ChemIndigo, fontSize = 11.sp) }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ChemIndigo, unfocusedBorderColor = ChemBorder, focusedTextColor = ChemTextDark, unfocusedTextColor = ChemTextDark, focusedContainerColor = ChemSurfaceSubtle, unfocusedContainerColor = ChemSurfaceSubtle))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = coeffB, onValueChange = { coeffB = it; calc() }, modifier = Modifier.weight(1f), label = { Text("Koefisien B", color = ChemIndigo, fontSize = 11.sp) }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ChemIndigo, unfocusedBorderColor = ChemBorder, focusedTextColor = ChemTextDark, unfocusedTextColor = ChemTextDark, focusedContainerColor = ChemSurfaceSubtle, unfocusedContainerColor = ChemSurfaceSubtle))
                OutlinedTextField(value = molB, onValueChange = { molB = it; calc() }, modifier = Modifier.weight(1f), label = { Text("Mol B (mol)", color = ChemIndigo, fontSize = 11.sp) }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ChemIndigo, unfocusedBorderColor = ChemBorder, focusedTextColor = ChemTextDark, unfocusedTextColor = ChemTextDark, focusedContainerColor = ChemSurfaceSubtle, unfocusedContainerColor = ChemSurfaceSubtle))
            }

            result?.let { res ->
                Spacer(modifier = Modifier.height(14.dp))
                ResultStepBreakdown(result = res)
            }
        }
    }
}

// 8. PERCENT COMPOSITION
@Composable
fun PercentCompositionView() {
    var formula by remember { mutableStateOf("C6H12O6") }
    var result by remember { mutableStateOf<CalcResult?>(CalculatorsEngine.calculatePercentageComposition("C6H12O6")) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ChemWhite),
        border = BorderStroke(1.dp, ChemBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Komposisi Persentase Massa Unsur", color = ChemTextDark, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = formula,
                onValueChange = {
                    formula = it
                    result = CalculatorsEngine.calculatePercentageComposition(it)
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Rumus Molekul", color = ChemIndigo) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ChemIndigo,
                    unfocusedBorderColor = ChemBorder,
                    focusedTextColor = ChemTextDark,
                    unfocusedTextColor = ChemTextDark,
                    focusedContainerColor = ChemSurfaceSubtle,
                    unfocusedContainerColor = ChemSurfaceSubtle
                )
            )

            result?.let { res ->
                Spacer(modifier = Modifier.height(14.dp))
                ResultStepBreakdown(result = res)
            }
        }
    }
}

// 9. SIGNIFICANT FIGURES
@Composable
fun SignificantFiguresView() {
    var numStr by remember { mutableStateOf("0.00450") }
    var result by remember { mutableStateOf<CalcResult?>(CalculatorsEngine.calculateSignificantFigures("0.00450")) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ChemWhite),
        border = BorderStroke(1.dp, ChemBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Analisis Angka Penting & Notasi Ilmiah", color = ChemTextDark, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = numStr,
                onValueChange = {
                    numStr = it
                    result = CalculatorsEngine.calculateSignificantFigures(it)
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Ketik Bilangan", color = ChemIndigo) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ChemIndigo,
                    unfocusedBorderColor = ChemBorder,
                    focusedTextColor = ChemTextDark,
                    unfocusedTextColor = ChemTextDark,
                    focusedContainerColor = ChemSurfaceSubtle,
                    unfocusedContainerColor = ChemSurfaceSubtle
                )
            )

            result?.let { res ->
                Spacer(modifier = Modifier.height(14.dp))
                ResultStepBreakdown(result = res)
            }
        }
    }
}

// 10. OXIDATION NUMBER
@Composable
fun OxidationNumberView() {
    var compound by remember { mutableStateOf("KMnO4") }
    var result by remember { mutableStateOf<CalcResult?>(CalculatorsEngine.calculateOxidationNumber("KMnO4")) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ChemWhite),
        border = BorderStroke(1.dp, ChemBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Kalkulator Bilangan Oksidasi (Biloks)", color = ChemTextDark, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = "Contoh: KMnO4, K2Cr2O7, H2SO4, HNO3, Cr2O7 2-, H2O2, OF2", color = ChemTextSecondary, fontSize = 11.sp)

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = compound,
                onValueChange = {
                    compound = it
                    result = CalculatorsEngine.calculateOxidationNumber(it)
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Senyawa / Ion", color = ChemIndigo) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ChemIndigo,
                    unfocusedBorderColor = ChemBorder,
                    focusedTextColor = ChemTextDark,
                    unfocusedTextColor = ChemTextDark,
                    focusedContainerColor = ChemSurfaceSubtle,
                    unfocusedContainerColor = ChemSurfaceSubtle
                )
            )

            result?.let { res ->
                Spacer(modifier = Modifier.height(14.dp))
                ResultStepBreakdown(result = res)
            }
        }
    }
}

@Composable
fun ResultStepBreakdown(result: CalcResult) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(ChemEmeraldBg)
            .border(1.dp, ChemEmerald.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = ChemEmerald, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Hasil Perhitungan:", color = ChemEmerald, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = result.finalAnswer, color = ChemTextDark, fontSize = 18.sp, fontWeight = FontWeight.Black)

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = result.summary, color = ChemTextPrimary, fontSize = 12.sp, lineHeight = 17.sp)

        if (result.steps.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Tahapan Perhitungan Matematis:", color = ChemIndigo, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))

            result.steps.forEachIndexed { sIdx, step ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(ChemWhite)
                        .border(1.dp, ChemBorder, RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    Text(text = "${sIdx + 1}. ${step.stepTitle}", color = ChemAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = step.formulaUsed, color = ChemIndigo, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = step.calculation, color = ChemTextDark, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = step.explanation, color = ChemTextSecondary, fontSize = 10.sp)
                }
            }
        }
    }
}
