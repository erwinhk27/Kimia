package com.example.data.chemistry

import java.util.Locale
import kotlin.math.log10
import kotlin.math.sqrt

data class CalcStep(
    val stepTitle: String,
    val formulaUsed: String,
    val calculation: String,
    val explanation: String
)

data class CalcResult(
    val finalAnswer: String,
    val summary: String,
    val steps: List<CalcStep>
)

object CalculatorsEngine {

    val atomicMasses = mapOf(
        "H" to 1.008, "He" to 4.0026, "Li" to 6.94, "Be" to 9.0122, "B" to 10.81,
        "C" to 12.011, "N" to 14.007, "O" to 15.999, "F" to 18.998, "Ne" to 20.180,
        "Na" to 22.990, "Mg" to 24.305, "Al" to 26.982, "Si" to 28.085, "P" to 30.974,
        "S" to 32.06, "Cl" to 35.45, "Ar" to 39.948, "K" to 39.098, "Ca" to 40.078,
        "Sc" to 44.956, "Ti" to 47.867, "V" to 50.942, "Cr" to 51.996, "Mn" to 54.938,
        "Fe" to 55.845, "Co" to 58.933, "Ni" to 58.693, "Cu" to 63.546, "Zn" to 65.38,
        "Ga" to 69.723, "Ge" to 72.630, "As" to 74.922, "Se" to 78.971, "Br" to 79.904,
        "Kr" to 83.798, "Rb" to 85.468, "Sr" to 87.62, "Y" to 88.906, "Zr" to 91.224,
        "Ag" to 107.87, "Cd" to 112.41, "Sn" to 118.71, "I" to 126.90, "Ba" to 137.33,
        "Au" to 196.97, "Hg" to 200.59, "Pb" to 207.2, "Bi" to 208.98, "U" to 238.03
    )

    // Helper: Parse formula like "H2SO4", "Ca(OH)2", "Al2(SO4)3"
    fun parseChemicalFormula(formula: String): Map<String, Int> {
        val clean = formula.replace(" ", "").trim()
        if (clean.isEmpty()) return emptyMap()

        val counts = mutableMapOf<String, Int>()

        fun parseGroup(sub: String, multiplier: Int) {
            val regex = Regex("([A-Z][a-z]?)([0-9]*)")
            val matches = regex.findAll(sub)
            for (m in matches) {
                val element = m.groupValues[1]
                val countStr = m.groupValues[2]
                val count = if (countStr.isEmpty()) 1 else countStr.toIntOrNull() ?: 1
                counts[element] = (counts[element] ?: 0) + (count * multiplier)
            }
        }

        // Handle parentheses: e.g. Ca(OH)2 or Al2(SO4)3
        val parenRegex = Regex("\\(([^)]+)\\)([0-9]*)")
        var remaining = clean

        val parenMatches = parenRegex.findAll(clean).toList()
        for (pm in parenMatches) {
            val inside = pm.groupValues[1]
            val multStr = pm.groupValues[2]
            val mult = if (multStr.isEmpty()) 1 else multStr.toIntOrNull() ?: 1
            parseGroup(inside, mult)
            remaining = remaining.replace(pm.value, "")
        }

        // Parse remaining parts without parens
        parseGroup(remaining, 1)

        return counts
    }

    // 1. Molar Mass Calculation with Full Pedagogical Steps
    fun calculateMolarMass(formula: String): CalcResult {
        val clean = formula.trim()
        if (clean.isEmpty()) {
            return CalcResult(
                finalAnswer = "0 g/mol",
                summary = "Masukkan rumus kimia yang valid (contoh: H2SO4, Ca(OH)2, C6H12O6).",
                steps = emptyList()
            )
        }

        val parsed = parseChemicalFormula(clean)
        if (parsed.isEmpty()) {
            return CalcResult(
                finalAnswer = "Format tidak valid",
                summary = "Format rumus kimia '$formula' tidak dikenali.",
                steps = emptyList()
            )
        }

        var totalMr = 0.0
        val steps = mutableListOf<CalcStep>()
        val breakdownList = mutableListOf<String>()

        for ((element, count) in parsed) {
            val ar = atomicMasses[element]
            if (ar != null) {
                val subtotal = count * ar
                totalMr += subtotal
                breakdownList.add("• $count × Ar($element) = $count × $ar = ${String.format(Locale.US, "%.3f", subtotal)} g/mol")
            } else {
                breakdownList.add("• $count × Ar($element) = [Unsur '$element' tidak ditemukan dalam database]")
            }
        }

        steps.add(
            CalcStep(
                stepTitle = "Identifikasi Unsur & Indeks",
                formulaUsed = "Mr = Σ (n_i × Ar_i)",
                calculation = parsed.entries.joinToString(", ") { "${it.key}: ${it.value} atom" },
                explanation = "Menghitung jumlah total setiap atom unsur penyusun molekul $formula."
            )
        )

        steps.add(
            CalcStep(
                stepTitle = "Penjumlahan Massa Atom Relatif (Ar)",
                formulaUsed = "Mr($formula) = Ar_1 + Ar_2 + ...",
                calculation = breakdownList.joinToString("\n"),
                explanation = "Mengalikan indeks masing-masing atom dengan massa atom relatif (Ar) dari tabel periodik standar IUPAC."
            )
        )

        val formattedMr = String.format(Locale.US, "%.3f", totalMr)
        return CalcResult(
            finalAnswer = "$formattedMr g/mol",
            summary = "Massa Molar (Mr) dari $formula adalah $formattedMr g/mol.",
            steps = steps
        )
    }

    // 2. Mole Conversion (Mass <-> Mole <-> Particles <-> STP Volume)
    fun convertMoles(
        inputVal: Double,
        inputType: String, // "Massa (g)", "Mol (mol)", "Jumlah Partikel", "Volume STP (L)"
        molarMass: Double
    ): CalcResult {
        val na = 6.022e23
        val vStpMolar = 22.4

        val moles: Double = when (inputType) {
            "Massa (g)" -> inputVal / molarMass
            "Mol (mol)" -> inputVal
            "Jumlah Partikel" -> inputVal / na
            "Volume STP (L)" -> inputVal / vStpMolar
            else -> inputVal
        }

        val mass = moles * molarMass
        val particles = moles * na
        val vStp = moles * vStpMolar

        val steps = listOf(
            CalcStep(
                stepTitle = "Konversi Input ke Jumlah Mol (n)",
                formulaUsed = when (inputType) {
                    "Massa (g)" -> "n = massa / Mr"
                    "Jumlah Partikel" -> "n = N / N_A"
                    "Volume STP (L)" -> "n = V_STP / 22.4"
                    else -> "n = input"
                },
                calculation = "$inputVal ($inputType) → n = ${String.format(Locale.US, "%.4f", moles)} mol",
                explanation = "Mol adalah jembatan utama konversi kuantitas zat dalam stoikiometri kimia."
            ),
            CalcStep(
                stepTitle = "Ekspansi ke Besaran Kimia Lainnya",
                formulaUsed = "massa = n × Mr,  N = n × 6.022 × 10²³,  V_STP = n × 22.4 L",
                calculation = "• Massa: ${String.format(Locale.US, "%.4f", mass)} g\n• Partikel: ${String.format(Locale.US, "%.3e", particles)} partikel\n• Volume STP (0°C, 1 atm): ${String.format(Locale.US, "%.4f", vStp)} Liter",
                explanation = "Semua besaran stoikiometri diturunkan dari jumlah mol zat."
            )
        )

        return CalcResult(
            finalAnswer = "${String.format(Locale.US, "%.4f", moles)} mol",
            summary = "Kuantitas setara: ${String.format(Locale.US, "%.3f", mass)} g | ${String.format(Locale.US, "%.3e", particles)} partikel | ${String.format(Locale.US, "%.3f", vStp)} L (STP)",
            steps = steps
        )
    }

    // 3. Molarity Calculator: M = (mass / Mr) * (1000 / volumeMl)
    fun calculateMolarity(massG: Double, mr: Double, volumeMl: Double): CalcResult {
        if (mr <= 0 || volumeMl <= 0) {
            return CalcResult("0.0 M", "Nilai Mr dan Volume harus bernilai positif > 0", emptyList())
        }

        val moles = massG / mr
        val volumeL = volumeMl / 1000.0
        val molarity = moles / volumeL

        val steps = listOf(
            CalcStep(
                stepTitle = "Langkah 1: Menghitung Mol Zat Terlarut",
                formulaUsed = "n = massa / Mr",
                calculation = "n = $massG g / $mr g/mol = ${String.format(Locale.US, "%.4f", moles)} mol",
                explanation = "Menentukan jumlah zat terlarut dalam satuan mol."
            ),
            CalcStep(
                stepTitle = "Langkah 2: Menghitung Konsentrasi Molaritas",
                formulaUsed = "M = n / V(Liter) = (massa / Mr) × (1000 / V_mL)",
                calculation = "M = ${String.format(Locale.US, "%.4f", moles)} mol / ${String.format(Locale.US, "%.3f", volumeL)} L = ${String.format(Locale.US, "%.4f", molarity)} M",
                explanation = "Molaritas menyatakan jumlah mol zat terlarut dalam setiap 1 Liter larutan."
            )
        )

        return CalcResult(
            finalAnswer = "${String.format(Locale.US, "%.4f", molarity)} M (molar)",
            summary = "Larutan mengandung ${String.format(Locale.US, "%.4f", molarity)} mol terlarut per Liter.",
            steps = steps
        )
    }

    // 4. Dilution Law: M1 * V1 = M2 * V2
    fun calculateDilution(
        m1: Double,
        v1: Double,
        m2: Double,
        v2: Double,
        target: String = "V2 (Volume Akhir mL)"
    ): CalcResult {
        var calculated = 0.0
        var calcStr = ""

        when (target) {
            "V2 (Volume Akhir mL)" -> {
                calculated = (m1 * v1) / m2
                calcStr = "V2 = ($m1 × $v1) / $m2 = ${String.format(Locale.US, "%.2f", calculated)} mL"
            }
            "M2 (Konsentrasi Akhir M)" -> {
                calculated = (m1 * v1) / v2
                calcStr = "M2 = ($m1 × $v1) / $v2 = ${String.format(Locale.US, "%.4f", calculated)} M"
            }
            "V1 (Volume Awal mL)" -> {
                calculated = (m2 * v2) / m1
                calcStr = "V1 = ($m2 × $v2) / $m1 = ${String.format(Locale.US, "%.2f", calculated)} mL"
            }
            else -> {
                calculated = (m2 * v2) / v1
                calcStr = "M1 = ($m2 × $v2) / $v1 = ${String.format(Locale.US, "%.4f", calculated)} M"
            }
        }

        val steps = listOf(
            CalcStep(
                stepTitle = "Hukum Pengenceran",
                formulaUsed = "M1 × V1 = M2 × V2",
                calculation = calcStr,
                explanation = "Jumlah mol zat terlarut sebelum dan sesudah penambahan pelarut (air) adalah tetap: n1 = n2."
            )
        )

        return CalcResult(
            finalAnswer = String.format(Locale.US, "%.3f", calculated),
            summary = "Hasil perhitungan pengenceran: $target = ${String.format(Locale.US, "%.3f", calculated)}",
            steps = steps
        )
    }

    // 5. pH Calculator
    fun calculatePh(
        substanceType: String, // "Asam Kuat", "Basa Kuat", "Asam Lemah", "Basa Lemah"
        concentrationM: Double,
        valency: Int = 1,
        kVal: Double = 1e-5 // Ka or Kb
    ): CalcResult {
        var hPlus = 0.0
        var ohMinus = 0.0
        var ph = 7.0
        var poh = 7.0
        val steps = mutableListOf<CalcStep>()

        when (substanceType) {
            "Asam Kuat" -> {
                hPlus = concentrationM * valency
                ph = -log10(hPlus)
                poh = 14.0 - ph
                steps.add(
                    CalcStep(
                        stepTitle = "Ionik Asam Kuat (Terionisasi Sempurna α = 1)",
                        formulaUsed = "[H⁺] = a × M",
                        calculation = "[H⁺] = $valency × $concentrationM = ${String.format(Locale.US, "%.4e", hPlus)} M",
                        explanation = "Asam kuat melepaskan semua proton H⁺ dalam air sesuai valensi asamnya (a)."
                    )
                )
                steps.add(
                    CalcStep(
                        stepTitle = "Perhitungan Nilai pH",
                        formulaUsed = "pH = -log [H⁺]",
                        calculation = "pH = -log(${String.format(Locale.US, "%.4e", hPlus)}) = ${String.format(Locale.US, "%.2f", ph)}",
                        explanation = "pH dihitung dari logaritma negatif konsentrasi ion hidrogen."
                    )
                )
            }
            "Basa Kuat" -> {
                ohMinus = concentrationM * valency
                poh = -log10(ohMinus)
                ph = 14.0 - poh
                steps.add(
                    CalcStep(
                        stepTitle = "Ionik Basa Kuat (Terionisasi Sempurna α = 1)",
                        formulaUsed = "[OH⁻] = b × M",
                        calculation = "[OH⁻] = $valency × $concentrationM = ${String.format(Locale.US, "%.4e", ohMinus)} M",
                        explanation = "Basa kuat melepaskan semua ion hidroksida OH⁻ sesuai valensi basanya (b)."
                    )
                )
                steps.add(
                    CalcStep(
                        stepTitle = "Perhitungan pOH dan pH",
                        formulaUsed = "pOH = -log[OH⁻],  pH = 14 - pOH",
                        calculation = "pOH = ${String.format(Locale.US, "%.2f", poh)} → pH = 14 - ${String.format(Locale.US, "%.2f", poh)} = ${String.format(Locale.US, "%.2f", ph)}",
                        explanation = "Hubungan kesetimbangan autoionisasi air: pH + pOH = 14 pada 25°C."
                    )
                )
            }
            "Asam Lemah" -> {
                hPlus = sqrt(kVal * concentrationM)
                ph = -log10(hPlus)
                poh = 14.0 - ph
                steps.add(
                    CalcStep(
                        stepTitle = "Kesetimbangan Ionisasi Asam Lemah",
                        formulaUsed = "[H⁺] = √(Ka × M)",
                        calculation = "[H⁺] = √($kVal × $concentrationM) = ${String.format(Locale.US, "%.4e", hPlus)} M",
                        explanation = "Asam lemah hanya terionisasi sebagian, dikendalikan oleh tetapan kesetimbangan Ka."
                    )
                )
                steps.add(
                    CalcStep(
                        stepTitle = "Perhitungan Nilai pH",
                        formulaUsed = "pH = -log [H⁺]",
                        calculation = "pH = ${String.format(Locale.US, "%.2f", ph)}",
                        explanation = "pH dihitung dari [H⁺] hasil kesetimbangan."
                    )
                )
            }
            "Basa Lemah" -> {
                ohMinus = sqrt(kVal * concentrationM)
                poh = -log10(ohMinus)
                ph = 14.0 - poh
                steps.add(
                    CalcStep(
                        stepTitle = "Kesetimbangan Ionisasi Basa Lemah",
                        formulaUsed = "[OH⁻] = √(Kb × M)",
                        calculation = "[OH⁻] = √($kVal × $concentrationM) = ${String.format(Locale.US, "%.4e", ohMinus)} M",
                        explanation = "Basa lemah terionisasi sebagian sesuai tetapan ionisasi basa Kb."
                    )
                )
                steps.add(
                    CalcStep(
                        stepTitle = "Perhitungan pOH dan pH",
                        formulaUsed = "pOH = -log[OH⁻],  pH = 14 - pOH",
                        calculation = "pOH = ${String.format(Locale.US, "%.2f", poh)} → pH = 14 - ${String.format(Locale.US, "%.2f", poh)} = ${String.format(Locale.US, "%.2f", ph)}",
                        explanation = "pH larutan basa diperoleh dari 14 - pOH."
                    )
                )
            }
        }

        val nature = when {
            ph < 6.8 -> "Asam"
            ph > 7.2 -> "Basa / Alkali"
            else -> "Netral"
        }

        return CalcResult(
            finalAnswer = "pH = ${String.format(Locale.US, "%.2f", ph)} ($nature)",
            summary = "pH: ${String.format(Locale.US, "%.2f", ph)} | pOH: ${String.format(Locale.US, "%.2f", poh)} | Sifat: $nature",
            steps = steps
        )
    }

    // 6. Ideal Gas Law (PV = nRT)
    fun calculateGasLaw(
        pressureAtm: Double?,
        volumeL: Double?,
        molesN: Double?,
        tempCelsius: Double?
    ): CalcResult {
        val r = 0.0821 // L.atm / (mol.K)
        val tempK = if (tempCelsius != null) tempCelsius + 273.15 else null

        var calculatedVar = ""
        var finalVal = 0.0
        var formula = ""
        var calcStr = ""

        if (pressureAtm == null && volumeL != null && molesN != null && tempK != null) {
            calculatedVar = "Tekanan (P)"
            finalVal = (molesN * r * tempK) / volumeL
            formula = "P = nRT / V"
            calcStr = "P = ($molesN × 0.0821 × ${String.format(Locale.US, "%.2f", tempK)}) / $volumeL = ${String.format(Locale.US, "%.3f", finalVal)} atm"
        } else if (volumeL == null && pressureAtm != null && molesN != null && tempK != null) {
            calculatedVar = "Volume (V)"
            finalVal = (molesN * r * tempK) / pressureAtm
            formula = "V = nRT / P"
            calcStr = "V = ($molesN × 0.0821 × ${String.format(Locale.US, "%.2f", tempK)}) / $pressureAtm = ${String.format(Locale.US, "%.3f", finalVal)} Liter"
        } else if (molesN == null && pressureAtm != null && volumeL != null && tempK != null) {
            calculatedVar = "Jumlah Mol (n)"
            finalVal = (pressureAtm * volumeL) / (r * tempK)
            formula = "n = PV / RT"
            calcStr = "n = ($pressureAtm × $volumeL) / (0.0821 × ${String.format(Locale.US, "%.2f", tempK)}) = ${String.format(Locale.US, "%.4f", finalVal)} mol"
        } else if (tempCelsius == null && pressureAtm != null && volumeL != null && molesN != null) {
            calculatedVar = "Suhu (T)"
            val calcK = (pressureAtm * volumeL) / (molesN * r)
            finalVal = calcK - 273.15
            formula = "T = PV / nR"
            calcStr = "T = ${String.format(Locale.US, "%.2f", calcK)} K = ${String.format(Locale.US, "%.2f", finalVal)} °C"
        } else {
            return CalcResult("Error", "Kosongkan tepat 1 variabel yang ingin dihitung.", emptyList())
        }

        val steps = listOf(
            CalcStep(
                stepTitle = "Konversi Suhu Mutlak (Kelvin)",
                formulaUsed = "T(K) = T(°C) + 273.15",
                calculation = if (tempK != null) "T = $tempCelsius°C + 273.15 = ${String.format(Locale.US, "%.2f", tempK)} K" else "T dihitung dari persamaan",
                explanation = "Persamaan gas ideal mensyaratkan suhu dalam skala termodinamika Kelvin."
            ),
            CalcStep(
                stepTitle = "Aplikasi Persamaan Gas Ideal",
                formulaUsed = formula,
                calculation = calcStr,
                explanation = "Tetapan gas ideal R = 0.0821 L·atm/(mol·K)."
            )
        )

        return CalcResult(
            finalAnswer = "$calculatedVar = ${String.format(Locale.US, "%.3f", finalVal)}",
            summary = "Hasil $calculatedVar berdasarkan hukum gas ideal: ${String.format(Locale.US, "%.3f", finalVal)}",
            steps = steps
        )
    }

    // 7. Stoichiometry Limiting Reagent Calculator: aA + bB -> Products
    fun calculateLimitingReagent(
        coeffA: Int,
        molesA: Double,
        coeffB: Int,
        molesB: Double,
        nameA: String = "Reaktan A",
        nameB: String = "Reaktan B"
    ): CalcResult {
        val ratioA = molesA / coeffA
        val ratioB = molesB / coeffB

        val limitingName: String
        val excessName: String
        val excessRemaining: Double

        if (ratioA < ratioB) {
            limitingName = nameA
            excessName = nameB
            val molesBUsed = (coeffB.toDouble() / coeffA.toDouble()) * molesA
            excessRemaining = molesB - molesBUsed
        } else {
            limitingName = nameB
            excessName = nameA
            val molesAUsed = (coeffA.toDouble() / coeffB.toDouble()) * molesB
            excessRemaining = molesA - molesAUsed
        }

        val steps = listOf(
            CalcStep(
                stepTitle = "Langkah 1: Perbandingan Mol terhadap Koefisien (Ratio Test)",
                formulaUsed = "Rasio = n / Koefisien",
                calculation = "• $nameA: $molesA / $coeffA = ${String.format(Locale.US, "%.4f", ratioA)}\n• $nameB: $molesB / $coeffB = ${String.format(Locale.US, "%.4f", ratioB)}",
                explanation = "Zat dengan nilai rasio n/koefisien terkecil akan habis bereaksi pertama kali dan menjadi pereaksi pembatas (limiting reactant)."
            ),
            CalcStep(
                stepTitle = "Langkah 2: Menghitung Sisa Pereaksi Berlebih",
                formulaUsed = "n_sisa = n_mula-mula - n_bereaksi",
                calculation = "• Pereaksi Pembatas: $limitingName (habis = 0 mol)\n• Sisa $excessName: ${String.format(Locale.US, "%.4f", excessRemaining)} mol",
                explanation = "Semua perhitungan produk reaksi didasarkan pada mol pereaksi pembatas ($limitingName)."
            )
        )

        return CalcResult(
            finalAnswer = "Pereaksi Pembatas: $limitingName",
            summary = "$limitingName habis bereaksi. Sisa $excessName = ${String.format(Locale.US, "%.4f", excessRemaining)} mol.",
            steps = steps
        )
    }

    // 8. Percentage Composition Calculator
    fun calculatePercentageComposition(formula: String): CalcResult {
        val parsed = parseChemicalFormula(formula)
        if (parsed.isEmpty()) return calculateMolarMass(formula)

        var totalMr = 0.0
        for ((elem, count) in parsed) {
            val ar = atomicMasses[elem] ?: 0.0
            totalMr += count * ar
        }

        val steps = mutableListOf<CalcStep>()
        val breakdown = mutableListOf<String>()

        for ((elem, count) in parsed) {
            val ar = atomicMasses[elem] ?: 0.0
            val massElem = count * ar
            val percent = (massElem / totalMr) * 100.0
            breakdown.add("• % $elem: ($count × $ar / ${String.format(Locale.US, "%.2f", totalMr)}) × 100% = ${String.format(Locale.US, "%.2f", percent)}%")
        }

        steps.add(
            CalcStep(
                stepTitle = "Rumus Komposisi Persen Massa",
                formulaUsed = "% Unsur = (n × Ar / Mr) × 100%",
                calculation = breakdown.joinToString("\n"),
                explanation = "Komposisi persen massa menunjukkan fraksi kontribusi massa masing-masing unsur dalam 100 gram senyawa."
            )
        )

        return CalcResult(
            finalAnswer = "Mr = ${String.format(Locale.US, "%.2f", totalMr)} g/mol",
            summary = "Komposisi unsur dari $formula selesai dihitung.",
            steps = steps
        )
    }

    // 9. Significant Figures Calculator
    fun calculateSignificantFigures(inputStr: String): CalcResult {
        val trimmed = inputStr.trim()
        val clean = trimmed

        var count = 0
        var explanation = ""

        if (clean.contains("e") || clean.contains("E")) {
            val parts = clean.split(Regex("[eE]"))
            val base = parts[0]
            val baseWithoutDot = base.replace(".", "").replace("-", "").replace("+", "")
            count = baseWithoutDot.length
            explanation = "Dalam notasi ilmiah, semua angka pada bilangan pokok (mantissa '$base') adalah angka penting."
        } else if (clean.contains(".")) {
            // Decimal number
            val withoutSign = clean.replace("-", "").replace("+", "")
            val withoutLeadingZeros = withoutSign.replace(Regex("^0+"), "")
            val finalClean = withoutLeadingZeros.replace(".", "")
            count = finalClean.length
            explanation = "Untuk bilangan desimal, angka nol di sebelah kiri angka bukan-nol bukan AP, namun angka nol di belakang desimal adalah angka penting."
        } else {
            // Integer without decimal
            val withoutSign = clean.replace("-", "").replace("+", "")
            val withoutTrailingZeros = withoutSign.replace(Regex("0+$"), "")
            val countWithoutTrailing = withoutTrailingZeros.length
            val countWithTrailing = withoutSign.length
            count = countWithoutTrailing
            explanation = "Untuk bilangan bulat tanpa tanda desimal di akhir, nol di bagian belakang bersifat ambigu (biasanya dianggap bukan angka penting kecuali ada tanda khusus). Diperkirakan: $countWithoutTrailing hingga $countWithTrailing AP."
        }

        val steps = listOf(
            CalcStep(
                stepTitle = "Aturan Angka Penting (Significant Figures Rules)",
                formulaUsed = "Analisis Notasi Angka",
                calculation = "Input: '$trimmed' → $count Angka Penting",
                explanation = explanation
            )
        )

        return CalcResult(
            finalAnswer = "$count Angka Penting",
            summary = "Bilangan '$trimmed' memiliki $count angka penting.",
            steps = steps
        )
    }

    // 10. Oxidation Number (Biloks) Calculator
    fun calculateOxidationNumber(compound: String): CalcResult {
        val clean = compound.trim()
        val rules = mutableListOf<String>()
        var summary = ""

        when (clean.uppercase()) {
            "KMNO4" -> {
                summary = "Biloks Mn = +7"
                rules.add("Biloks K (Gol IA) = +1")
                rules.add("Biloks O (Oksida umum) = -2 (total 4 × -2 = -8)")
                rules.add("Muatan total senyawa netral = 0")
                rules.add("Persamaan: (+1) + Biloks(Mn) + (-8) = 0 → Biloks(Mn) = +7")
            }
            "K2CR2O7" -> {
                summary = "Biloks Cr = +6"
                rules.add("Biloks K (Gol IA) = +1 (total 2 × +1 = +2)")
                rules.add("Biloks O = -2 (total 7 × -2 = -14)")
                rules.add("Muatan total = 0")
                rules.add("Persamaan: (+2) + 2·Biloks(Cr) + (-14) = 0 → 2·Biloks(Cr) = +12 → Biloks(Cr) = +6")
            }
            "H2SO4" -> {
                summary = "Biloks S = +6"
                rules.add("Biloks H = +1 (total 2 × +1 = +2)")
                rules.add("Biloks O = -2 (total 4 × -2 = -8)")
                rules.add("Persamaan: (+2) + Biloks(S) + (-8) = 0 → Biloks(S) = +6")
            }
            "HNO3" -> {
                summary = "Biloks N = +5"
                rules.add("Biloks H = +1, Biloks O = -2 (total 3 × -2 = -6)")
                rules.add("Persamaan: (+1) + Biloks(N) + (-6) = 0 → Biloks(N) = +5")
            }
            "CR2O7^2-", "CR2O7 2-", "CR2O72-" -> {
                summary = "Biloks Cr = +6"
                rules.add("Ion poliatomik dengan muatan -2")
                rules.add("Biloks O = -2 (total 7 × -2 = -14)")
                rules.add("Persamaan: 2·Biloks(Cr) + (-14) = -2 → 2·Biloks(Cr) = +12 → Biloks(Cr) = +6")
            }
            "MNO4^-", "MNO4 -", "MNO4-" -> {
                summary = "Biloks Mn = +7"
                rules.add("Ion poliatomik dengan muatan -1")
                rules.add("Biloks O = -2 (total 4 × -2 = -8)")
                rules.add("Persamaan: Biloks(Mn) + (-8) = -1 → Biloks(Mn) = +7")
            }
            "H2O2" -> {
                summary = "Biloks O = -1 (Anomali Peroksida)"
                rules.add("Biloks H = +1 (total 2 × +1 = +2)")
                rules.add("Dalam peroksida, Biloks O = -1")
            }
            "OF2" -> {
                summary = "Biloks O = +2 (Anomali Fluorida)"
                rules.add("Biloks F selalu -1 (karena F lebih elektronegatif)")
                rules.add("Persamaan: Biloks(O) + 2(-1) = 0 → Biloks(O) = +2")
            }
            else -> {
                summary = "Senyawa $compound: Terapkan aturan prioritas biloks (F > IA/IIA > H > O)."
                rules.add("1. Bilangan oksidasi unsur bebas = 0 (contoh: O₂, Fe, H₂, N₂)")
                rules.add("2. Biloks ion monoatom = muatannya (contoh: Na⁺ = +1, Cl⁻ = -1, Fe³⁺ = +3)")
                rules.add("3. Logam Golongan IA = +1, Golongan IIA = +2, Al = +3")
                rules.add("4. Biloks F selalu -1 dalam senyawanya")
                rules.add("5. Biloks H = +1 (kecuali dalam hidrida logam seperti NaH = -1)")
                rules.add("6. Biloks O = -2 (kecuali peroksida = -1, superoksida = -1/2, OF₂ = +2)")
                rules.add("7. Jumlah aljabar biloks dalam senyawa netral = 0, dalam ion poliatom = muatan ion")
            }
        }

        val steps = listOf(
            CalcStep(
                stepTitle = "Aturan Bilangan Oksidasi (IUPAC Rules)",
                formulaUsed = "Σ Biloks = Muatan Total",
                calculation = rules.joinToString("\n"),
                explanation = "Bilangan oksidasi menunjukkan muatan formal suatu atom jika semua ikatan dianggap 100% ionik."
            )
        )

        return CalcResult(
            finalAnswer = summary,
            summary = summary,
            steps = steps
        )
    }
}
