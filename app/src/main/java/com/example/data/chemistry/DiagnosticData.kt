package com.example.data.chemistry

import com.example.data.models.DiagnosticResult
import com.example.data.models.ErrorType
import com.example.data.models.Question
import com.example.data.models.QuestionDifficulty

object DiagnosticData {

    val diagnosticQuestions: List<Question> = listOf(
        // FONDASI MATEMATIKA & SAINS (Q1 - Q5)
        Question(
            id = "diag_1",
            topicId = "topic_0_foundation",
            difficulty = QuestionDifficulty.LEVEL_2_UNDERSTANDING,
            question = "[Fondasi Matematika Kimia] Jika perbandingan mol zat A dan zat B dalam suatu reaksi adalah 2 : 3, dan tersedia 0.6 mol zat A, berapa mol zat B yang tepat bereaksi habis?",
            options = listOf("0.4 mol", "0.9 mol", "1.2 mol", "0.6 mol"),
            correctAnswerIndex = 1,
            explanation = "Perbandingan mol = 2 : 3. Maka n_B = \\frac{3}{2} \\times n_A = \\frac{3}{2} \\times 0.6 = 0.9\\text{ mol}.",
            whyExplanation = "Prinsip perbandingan senilai pada koefisien reaksi kimia.",
            conceptTested = "Perbandingan Pecahan & Aljabar Stoikiometri Dasar",
            errorType = ErrorType.CALCULATION,
            rightThinkingMethod = "Gunakan perbandingan \\frac{n_B}{n_A} = \\frac{3}{2} \\Rightarrow n_B = 1.5 \\times 0.6 = 0.9."
        ),
        Question(
            id = "diag_2",
            topicId = "topic_0_foundation",
            difficulty = QuestionDifficulty.LEVEL_2_UNDERSTANDING,
            question = "[Notasi Ilmiah & Angka Penting] Bilangan 0.000450 jika dituliskan dalam notasi ilmiah standar dengan jumlah angka penting yang tepat adalah...",
            options = listOf("4.5 × 10⁻⁴ (2 AP)", "4.50 × 10⁻⁴ (3 AP)", "45 × 10⁻⁵ (2 AP)", "4.500 × 10⁻⁴ (4 AP)"),
            correctAnswerIndex = 1,
            explanation = "Angka 0.000450 memiliki 3 angka penting (angka 4, 5, dan nol di belakang). Dalam notasi ilmiah ditulis sebagai 4.50 \\times 10^{-4}.",
            whyExplanation = "Nol di belakang koma desimal setelah angka bukan nol adalah angka penting.",
            conceptTested = "Notasi Ilmiah & Angka Penting",
            errorType = ErrorType.CONCEPTUAL
        ),
        Question(
            id = "diag_3",
            topicId = "topic_0_foundation",
            difficulty = QuestionDifficulty.LEVEL_3_APPLICATION,
            question = "[Konversi Satuan] Sebanyak 500 mL larutan memiliki massa jenis 1.2 g/mL. Berapa massa total larutan tersebut dalam satuan kilogram (kg)?",
            options = listOf("0.60 kg", "6.00 kg", "0.41 kg", "600 kg"),
            correctAnswerIndex = 0,
            explanation = "Massa = Volume x Massa Jenis = 500 mL x 1.2 g/mL = 600 gram. Konversi ke kg = 600 / 1000 = 0.60 kg.",
            conceptTested = "Konversi Satuan Massa dan Volume Densitas",
            errorType = ErrorType.UNIT
        ),

        // STRUKTUR ATOM & TABEL PERIODIK (Q4 - Q6)
        Question(
            id = "diag_4",
            topicId = "topic_1_atomic",
            difficulty = QuestionDifficulty.LEVEL_2_UNDERSTANDING,
            question = "[Struktur Atom] Suatu ion X^{3+} memiliki 10 elektron dan nomor massa 27. Berapakah jumlah neutron dalam inti atom X tersebut?",
            options = listOf("14", "13", "10", "17"),
            correctAnswerIndex = 0,
            explanation = "Ion X^{3+} memiliki 10 elektron, maka atom netral memiliki Z = 10 + 3 = 13 proton (Aluminium). Jumlah neutron = A - Z = 27 - 13 = 14 neutron.",
            conceptTested = "Nomor Atom, Nomor Massa, dan Ion",
            errorType = ErrorType.CONCEPTUAL
        ),
        Question(
            id = "diag_5",
            topicId = "topic_2_periodic",
            difficulty = QuestionDifficulty.LEVEL_3_APPLICATION,
            question = "[Tabel Periodik] Di antara unsur ^{11}\\text{Na}, ^{12}\\text{Mg}, ^{13}\\text{Al}, dan ^{19}\\text{K}, unsur manakah yang memiliki jari-jari atom paling besar?",
            options = listOf("Na", "Mg", "Al", "K"),
            correctAnswerIndex = 3,
            explanation = "Kalium (^{19}K) berada pada Periode ke-4 (memiliki 4 kulit atom), sedangkan Na, Mg, dan Al berada pada Periode ke-3 (3 kulit). Unsur dengan jumlah kulit terbanyak memiliki jari-jari atom paling besar.",
            conceptTested = "Kecenderungan Jari-Jari Atom Periodik",
            errorType = ErrorType.REASONING
        ),

        // IKATAN KIMIA (Q6 - Q7)
        Question(
            id = "diag_6",
            topicId = "topic_3_bonding",
            difficulty = QuestionDifficulty.LEVEL_3_APPLICATION,
            question = "[Ikatan Kimia] Senyawa yang terbentuk antara unsur ^{12}\\text{Mg} dan unsur ^{9}\\text{F} memiliki jenis ikatan dan rumus kimia...",
            options = listOf("Kovalen polar, MgF", "Ionik, MgF₂", "Kovalen nonpolar, MgF₂", "Ionik, Mg₂F"),
            correctAnswerIndex = 1,
            explanation = "Mg adalah logam alkali tanah (melepas 2 elektron menjadi Mg^{2+}). F adalah nonlogam halogen (menangkap 1 elektron menjadi F^-). Terjadi transfer elektron menghasilkan ikatan ionik dengan rumus MgF_2.",
            conceptTested = "Ikatan Ion vs Kovalen",
            errorType = ErrorType.CONCEPTUAL
        ),

        // KONSEP MOL & STOIKIOMETRI (Q8 - Q10)
        Question(
            id = "diag_7",
            topicId = "topic_4_mole",
            difficulty = QuestionDifficulty.LEVEL_3_APPLICATION,
            question = "[Konsep Mol] Berapakah jumlah mol yang terkandung dalam 49 gram asam sulfat H_2SO_4 (Ar H=1, S=32, O=16)?",
            options = listOf("0.25 mol", "0.50 mol", "1.00 mol", "2.00 mol"),
            correctAnswerIndex = 1,
            explanation = "Mr(H_2SO_4) = 2(1) + 32 + 4(16) = 98\\text{ g/mol}. Mol n = \\frac{49\\text{ g}}{98\\text{ g/mol}} = 0.50\\text{ mol}.",
            conceptTested = "Konversi Massa ke Mol",
            errorType = ErrorType.CALCULATION
        ),
        Question(
            id = "diag_8",
            topicId = "topic_5_stoichiometry",
            difficulty = QuestionDifficulty.LEVEL_4_ANALYSIS,
            question = "[Stoikiometri] Pada reaksi 2H_2(g) + O_2(g) \\rightarrow 2H_2O(g), jika 4 mol H_2 direaksikan dengan 3 mol O_2, zat manakah yang bertindak sebagai pereaksi pembatas?",
            options = listOf("H₂", "O₂", "Keduanya habis bersamaan", "H₂O"),
            correctAnswerIndex = 0,
            explanation = "Rasio H_2 = 4 / 2 = 2. Rasio O_2 = 3 / 1 = 3. Karena rasio H_2 lebih kecil (2 < 3), maka gas H_2 habis bereaksi terlebih dahulu dan menjadi pereaksi pembatas.",
            conceptTested = "Penentuan Pereaksi Pembatas",
            errorType = ErrorType.REASONING
        ),

        // ASAM-BASA & KESETIMBANGAN (Q9 - Q11)
        Question(
            id = "diag_9",
            topicId = "topic_11_acidbase",
            difficulty = QuestionDifficulty.LEVEL_3_APPLICATION,
            question = "[Asam-Basa] Suatu larutan HCl 0.01 M memiliki nilai pH sebesar...",
            options = listOf("1", "2", "12", "13"),
            correctAnswerIndex = 1,
            explanation = "HCl adalah asam kuat monoprotik ([H^+] = 1 \\times 0.01 = 10^{-2}\\text{ M}). Maka pH = -\\log(10^{-2}) = 2.",
            conceptTested = "Perhitungan pH Asam Kuat",
            errorType = ErrorType.CALCULATION
        ),
        Question(
            id = "diag_10",
            topicId = "topic_10_equilibrium",
            difficulty = QuestionDifficulty.LEVEL_4_ANALYSIS,
            question = "[Kesetimbangan] Pada kesetimbangan eksoterm: N_2(g) + 3H_2(g) \\rightleftharpoons 2NH_3(g) \\quad \\Delta H = -92\\text{ kJ}, jika suhu sistem dinaikkan, maka...",
            options = listOf(
                "Kesetimbangan bergeser ke kanan dan jumlah NH₃ bertambah",
                "Kesetimbangan bergeser ke kiri dan jumlah NH₃ berkurang",
                "Kesetimbangan tidak bergeser sama sekali",
                "Nilai tetapan kesetimbangan (Kc) akan bertambah besar"
            ),
            correctAnswerIndex = 1,
            explanation = "Reaksi ke kanan bersifat eksoterm (\\Delta H < 0). Menurut Asas Le Chatelier, penaikan suhu akan menggeser kesetimbangan ke arah reaksi endoterm (kiri / pembentukan reaktan N_2 dan H_2), sehingga jumlah NH_3 berkurang dan nilai K_c menurun.",
            conceptTested = "Pengaruh Suhu terhadap Kesetimbangan Le Chatelier",
            errorType = ErrorType.CONCEPTUAL
        ),

        // REDOKS & KIMIA ORGANIK (Q11 - Q12)
        Question(
            id = "diag_11",
            topicId = "topic_12_redox",
            difficulty = QuestionDifficulty.LEVEL_3_APPLICATION,
            question = "[Bilangan Oksidasi Redoks] Bilangan oksidasi unsur Mangan (Mn) pada senyawa kalium permanganat KMnO_4 adalah...",
            options = listOf("+4", "+6", "+7", "+2"),
            correctAnswerIndex = 2,
            explanation = "Biloks K = +1, Biloks O = -2 (4 atom = -8). Senyawa netral: (+1) + \\text{Biloks}(Mn) + (-8) = 0 \\Rightarrow \\text{Biloks}(Mn) = +7.",
            conceptTested = "Penentuan Bilangan Oksidasi",
            errorType = ErrorType.CALCULATION
        ),
        Question(
            id = "diag_12",
            topicId = "topic_13_organic",
            difficulty = QuestionDifficulty.LEVEL_3_APPLICATION,
            question = "[Kimia Organik] Pasangan senyawa berikut yang merupakan pasangan isomer gugus fungsi adalah...",
            options = listOf(
                "Etanol (CH_3CH_2OH) dan Dimetil eter (CH_3OCH_3)",
                "Metana (CH_4) dan Etana (C_2H_6)",
                "Propanol dan Butanol",
                "Asam asetat dan Aseton"
            ),
            correctAnswerIndex = 0,
            explanation = "Etanol (alkohol) dan Dimetil eter (eter) keduanya memiliki rumus molekul yang sama yaitu C_2H_6O, namun memiliki gugus fungsi yang berbeda (-OH vs -O-).",
            conceptTested = "Isomer Gugus Fungsi Senyawa Karbon",
            errorType = ErrorType.CONCEPTUAL
        )
    )

    fun evaluateDiagnostic(userAnswers: Map<String, Int>): DiagnosticResult {
        var correctCount = 0
        var foundationCorrect = 0
        var foundationTotal = 0
        var chemCorrect = 0
        var chemTotal = 0

        val topicCorrectMap = mutableMapOf<String, Int>()
        val topicTotalMap = mutableMapOf<String, Int>()
        val misconceptions = mutableListOf<String>()

        diagnosticQuestions.forEach { q ->
            val userSelected = userAnswers[q.id]
            val isCorrect = userSelected == q.correctAnswerIndex

            val isFoundation = q.topicId == "topic_0_foundation"
            if (isFoundation) {
                foundationTotal++
                if (isCorrect) foundationCorrect++
            } else {
                chemTotal++
                if (isCorrect) chemCorrect++
            }

            topicTotalMap[q.topicId] = (topicTotalMap[q.topicId] ?: 0) + 1
            if (isCorrect) {
                correctCount++
                topicCorrectMap[q.topicId] = (topicCorrectMap[q.topicId] ?: 0) + 1
            } else {
                // Record misconception
                val tip = if (q.whyExplanation.isNotEmpty()) q.whyExplanation else q.explanation
                misconceptions.add("Perlu penguatan pada konsep: ${q.conceptTested} ($tip)")
            }
        }

        val totalQuestions = diagnosticQuestions.size
        val overallScore = if (totalQuestions > 0) (correctCount * 100) / totalQuestions else 0
        val foundationScore = if (foundationTotal > 0) (foundationCorrect * 100) / foundationTotal else 0
        val chemistryScore = if (chemTotal > 0) (chemCorrect * 100) / chemTotal else 0

        val topicScores = mutableMapOf<String, Int>()
        val strengths = mutableListOf<String>()
        val weaknesses = mutableListOf<String>()
        val recommendedTopicIds = mutableListOf<String>()

        val topicNames = mapOf(
            "topic_0_foundation" to "Fondasi Matematika Kimia",
            "topic_1_atomic" to "Struktur Atom & Notasi Nuklida",
            "topic_2_periodic" to "Tabel Periodik & Sifat Keperiodikan",
            "topic_3_bonding" to "Ikatan Kimia & Geometri Molekul",
            "topic_4_mole" to "Konsep Mol & Konversi Zat",
            "topic_5_stoichiometry" to "Stoikiometri & Pereaksi Pembatas",
            "topic_10_equilibrium" to "Kesetimbangan Kimia",
            "topic_11_acidbase" to "Larutan Asam-Basa",
            "topic_12_redox" to "Redoks & Elektrokimia",
            "topic_13_organic" to "Kimia Organik & Gugus Fungsi"
        )

        topicTotalMap.forEach { (topicId, total) ->
            val correct = topicCorrectMap[topicId] ?: 0
            val pct = (correct * 100) / total
            topicScores[topicId] = pct
            val name = topicNames[topicId] ?: topicId
            if (pct >= 75) {
                strengths.add("$name ($pct%)")
            } else {
                weaknesses.add("$name ($pct%)")
                recommendedTopicIds.add(topicId)
            }
        }

        val summary = when {
            overallScore >= 85 -> "Luar biasa! Penguasaan konsep kimia dasar Anda sudah sangat matang (TKA Ready). Disarankan langsung mendalami soal-soal penalaran HOTS dan Simulasi Ujian TKA."
            overallScore >= 65 -> "Fondasi kimia Anda sudah cukup baik (Developing / Competent). Fokuskan penguatan pada topik stoikiometri kuantitatif dan penalaran kesetimbangan."
            else -> "Terdeteksi beberapa celah pada konsep fondasi mol dan matematika kimia. Disarankan mulai belajar dari Level 0 (Fondasi Kimia) dan Level 4 (Konsep Mol) sebelum beralih ke soal lanjutan."
        }

        return DiagnosticResult(
            overallScore = overallScore,
            foundationScore = foundationScore,
            chemistryScore = chemistryScore,
            topicScores = topicScores,
            strengths = strengths,
            weaknesses = weaknesses,
            misconceptions = misconceptions,
            recommendedTopicIds = recommendedTopicIds,
            recommendationSummary = summary
        )
    }
}
