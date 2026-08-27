package com.example.data.chemistry

import com.example.data.models.ErrorType
import com.example.data.models.Question
import com.example.data.models.QuestionDifficulty

object TkaQuestionBank {

    val tkaQuestions: List<Question> = listOf(
        // SOAL TKA 1: STOIKIOMETRI MULTI-REAKTAN & RENDEMEN
        Question(
            id = "tka_1",
            topicId = "topic_5_stoichiometry",
            difficulty = QuestionDifficulty.LEVEL_6_TKA_CHALLENGE,
            questionType = "TKA Soal Penalaran Kimia",
            contextText = "Sebanyak 10 gram batu kapur yang mengandung kalsium karbonat (CaCO_3, M_r = 100) tidak murni direaksikan dengan asam klorida berlebih menurut reaksi:\nCaCO_3(s) + 2HCl(aq) \\rightarrow CaCl_2(aq) + H_2O(l) + CO_2(g)\nGas CO_2 yang terbentuk dialirkan ke dalam larutan kalsium hidroksida berlebih sehingga menghasilkan 8.0 gram endapan kalsium karbonat (CaCO_3).",
            question = "Kadar kemurnian (%\\text{ massa}) CaCO_3 dalam sampel batu kapur awal adalah...",
            options = listOf("60%", "80%", "75%", "90%", "85%"),
            correctAnswerIndex = 1,
            explanation = "1. Reaksi penangkapan gas: CO_2(g) + Ca(OH)_2(aq) \\rightarrow CaCO_3(s) + H_2O(l).\n2. Mol endapan CaCO_3 = 8.0 g / 100 g/mol = 0.08 mol.\n3. Mol gas CO_2 = mol endapan = 0.08 mol.\n4. Dari reaksi pertama: Mol CaCO_3 murni dalam sampel batu kapur = mol CO_2 = 0.08 mol.\n5. Massa CaCO_3 murni = 0.08 mol x 100 g/mol = 8.0 gram.\n6. Kadar kemurnian = (8.0 g / 10.0 g) x 100% = 80%.",
            whyExplanation = "Hukum kekekalan massa atom karbon menghubungkan endapan akhir dengan sampel awal.",
            conceptTested = "Analisis Gravimetri & Stoikiometri Bertingkat Kadar Zat",
            errorType = ErrorType.CALCULATION,
            rightThinkingMethod = "Lacak jejak atom C dari endapan akhir (8.0 g) kembali ke sampel reaktan awal."
        ),

        // SOAL TKA 2: STRUKTUR ATOM & MEKANIKA KUANTUM
        Question(
            id = "tka_2",
            topicId = "topic_1_atomic",
            difficulty = QuestionDifficulty.LEVEL_5_EVALUATION,
            questionType = "TKA Soal Analisis Kuantum",
            contextText = "Suatu unsur transisi periode 4 memiliki 4 elektron tidak berpasangan pada subkulit 3d dan bersifat paramagnetik kuat.",
            question = "Nomor atom unsur tersebut dan bilangan kuantum elektron terakhir yang mungkin berturut-turut adalah...",
            options = listOf(
                "Z = 24; n=3, l=2, m=+2, s=+1/2",
                "Z = 26; n=3, l=2, m=-2, s=-1/2",
                "Z = 28; n=3, l=2, m=0, s=-1/2",
                "Z = 22; n=4, l=0, m=0, s=+1/2",
                "Z = 25; n=3, l=2, m=+2, s=+1/2"
            ),
            correctAnswerIndex = 1,
            explanation = "• Konfigurasi elektron besi (^{26}Fe): [Ar] 4s^2 3d^6.\n• Pengisian 6 elektron pada 5 orbital 3d (m = -2, -1, 0, +1, +2):\n  - 1 orbital berpasangan (m = -2, \\uparrow\\downarrow)\n  - 4 orbital terisi tunggal (m = -1, 0, +1, +2, \\uparrow), sehingga tepat memiliki 4 elektron tidak berpasangan.\n• Elektron terakhir (elektron ke-6 pada 3d): n = 3, l = 2 (subkulit d), m = -2, s = -1/2.",
            whyExplanation = "Aturan Hund menyatakan pengisian orbital berenergi sama dilakukan satu per satu dengan spin sejajar sebelum berpasangan.",
            conceptTested = "Aturan Hund, Sifat Magnetik & Bilangan Kuantum",
            errorType = ErrorType.CONCEPTUAL,
            rightThinkingMethod = "Gambarkan diagram 5 kotak orbital subkulit d lalu isi 6 elektron sesuai aturan Hund."
        ),

        // SOAL TKA 3: TERMODINAMIKA & HUKUM HESS
        Question(
            id = "tka_3",
            topicId = "topic_0_foundation",
            difficulty = QuestionDifficulty.LEVEL_5_EVALUATION,
            questionType = "TKA Termokimia & Energi Ikatan",
            contextText = "Diketahui energi ikatan rata-rata:\n• C-H = 413\\text{ kJ/mol}\n• Cl-Cl = 242\\text{ kJ/mol}\n• C-Cl = 328\\text{ kJ/mol}\n• H-Cl = 431\\text{ kJ/mol}",
            question = "Perubahan entalpi (\\Delta H) untuk reaksi klorinasi metana:\nCH_4(g) + Cl_2(g) \\rightarrow CH_3Cl(g) + HCl(g) adalah...",
            options = listOf("-104 kJ/mol", "+104 kJ/mol", "-208 kJ/mol", "+52 kJ/mol", "-416 kJ/mol"),
            correctAnswerIndex = 0,
            explanation = "\\Delta H = \\sum \\text{Energi Pemutusan (Kiri)} - \\sum \\text{Energi Pembentukan (Kanan)}\n• Ikatan yang diputuskan di reaktan: 1 ikatan C-H + 1 ikatan Cl-Cl = 413 + 242 = 655 kJ/mol\n• Ikatan yang terbentuk di produk: 1 ikatan C-Cl + 1 ikatan H-Cl = 328 + 431 = 759 kJ/mol\n• \\Delta H = 655 - 759 = -104\\text{ kJ/mol} (Reaksi Eksoterm).",
            whyExplanation = "Pembentukan ikatan baru melepaskan lebih banyak energi daripada energi yang dibutuhkan untuk memutuskan ikatan lama.",
            conceptTested = "Perhitungan Entalpi dari Energi Ikatan Rata-Rata",
            errorType = ErrorType.CARELESS,
            rightThinkingMethod = "Hanya hitung ikatan yang berubah (C-H dan Cl-Cl menjadi C-Cl dan H-Cl) untuk menghemat waktu."
        ),

        // SOAL TKA 4: LAJU REAKSI & ORDE
        Question(
            id = "tka_4",
            topicId = "topic_0_foundation",
            difficulty = QuestionDifficulty.LEVEL_5_EVALUATION,
            questionType = "TKA Kinetika Kimia Orde Reaksi",
            contextText = "Data eksperimen untuk reaksi 2A + B \\rightarrow C:\n1. [A] = 0.1\\text{ M}, [B] = 0.1\\text{ M} \\rightarrow v = 2 \\times 10^{-3}\\text{ M/s}\n2. [A] = 0.2\\text{ M}, [B] = 0.1\\text{ M} \\rightarrow v = 8 \\times 10^{-3}\\text{ M/s}\n3. [A] = 0.1\\text{ M}, [B] = 0.3\\text{ M} \\rightarrow v = 6 \\times 10^{-3}\\text{ M/s}",
            question = "Persamaan laju reaksi dan nilai tetapan laju reaksi (k) beserta satuannya berturut-turut adalah...",
            options = listOf(
                "v = k [A]² [B]; k = 2 M⁻² s⁻¹",
                "v = k [A] [B]; k = 0.2 M⁻¹ s⁻¹",
                "v = k [A]² [B]²; k = 20 M⁻³ s⁻¹",
                "v = k [A] [B]²; k = 2 M⁻² s⁻¹",
                "v = k [A]² [B]; k = 0.02 M⁻² s⁻¹"
            ),
            correctAnswerIndex = 0,
            explanation = "1. Orde terhadap A (percobaan 1 & 2): (0.2/0.1)^x = (8 \\times 10^{-3} / 2 \\times 10^{-3}) \\Rightarrow 2^x = 4 \\Rightarrow x = 2.\n2. Orde terhadap B (percobaan 1 & 3): (0.3/0.1)^y = (6 \\times 10^{-3} / 2 \\times 10^{-3}) \\Rightarrow 3^y = 3 \\Rightarrow y = 1.\n3. Persamaan laju: v = k [A]^2 [B]. Orde total = 2 + 1 = 3.\n4. Hitung k: 2 \\times 10^{-3} = k (0.1)^2 (0.1) = k (10^{-3}) \\Rightarrow k = 2\\text{ M}^{-2}\\text{s}^{-1}.",
            whyExplanation = "Orde total 3 menghasilkan satuan tetapan laju M^{1-3} s^{-1} = M^{-2} s^{-1}.",
            conceptTested = "Penentuan Orde Reaksi, Persamaan Laju & Satuan Tetapan K",
            errorType = ErrorType.CALCULATION
        ),

        // SOAL TKA 5: KESETIMBANGAN KIMIA KP & KC
        Question(
            id = "tka_5",
            topicId = "topic_10_equilibrium",
            difficulty = QuestionDifficulty.LEVEL_6_TKA_CHALLENGE,
            questionType = "TKA Kesetimbangan Gas Homogen",
            contextText = "Dalam wadah tertutup bervolume 1 Liter pada suhu tertentu, dimasukkan 1.0 mol gas PCl_5. Gas tersebut terdisosiasi sebagian menurut reaksi:\nPCl_5(g) \\rightleftharpoons PCl_3(g) + Cl_2(g)\nSaat tercapai kesetimbangan, perbandingan mol gas PCl_5 terhadap PCl_3 adalah 1 : 4.",
            question = "Derajat disosiasi (\\alpha) dan nilai tetapan kesetimbangan K_c untuk reaksi tersebut adalah...",
            options = listOf(
                "α = 0.8; Kc = 3.2",
                "α = 0.4; Kc = 1.6",
                "α = 0.8; Kc = 0.8",
                "α = 0.2; Kc = 0.05",
                "α = 0.5; Kc = 2.0"
            ),
            correctAnswerIndex = 0,
            explanation = "• Mula-mula: PCl_5 = 1.0\\text{ mol}\n• Terurai = x\\text{ mol}, Terbentuk PCl_3 = x\\text{ mol}, Cl_2 = x\\text{ mol}\n• Saat setimbang: PCl_5 = (1 - x), PCl_3 = x\n• Diketahui perbandingan setimbang: \\frac{1-x}{x} = \\frac{1}{4} \\Rightarrow 4 - 4x = x \\Rightarrow 5x = 4 \\Rightarrow x = 0.8\\text{ mol}\n• Derajat disosiasi \\alpha = 0.8 / 1.0 = 0.8 (80%)\n• Konsentrasi setimbang dalam V = 1 L:\n  [PCl_5] = 0.2\\text{ M}, [PCl_3] = 0.8\\text{ M}, [Cl_2] = 0.8\\text{ M}\n• K_c = \\frac{[PCl_3][Cl_2]}{[PCl_5]} = \\frac{0.8 \\times 0.8}{0.2} = \\frac{0.64}{0.2} = 3.2.",
            whyExplanation = "Perbandingan mol setimbang menentukan fraksi penguraian dan konsentrasi kesetimbangan.",
            conceptTested = "Derajat Disosiasi & Perhitungan Tetapan Kc",
            errorType = ErrorType.CALCULATION
        ),

        // SOAL TKA 6: LARUTAN PENYANGGA & PERUBAHAN PH
        Question(
            id = "tka_6",
            topicId = "topic_11_acidbase",
            difficulty = QuestionDifficulty.LEVEL_6_TKA_CHALLENGE,
            questionType = "TKA Buffer & Daya Tahan pH",
            contextText = "Suatu larutan penyangga dibuat dengan mencampurkan 200 mL larutan NH_3 0.15 M (K_b = 1.8 \\times 10^{-5}) dengan 100 mL larutan NH_4Cl 0.30 M.",
            question = "Berapakah nilai pH larutan penyangga tersebut, dan berapakah pH setelah ditambahkan 1.0 mmol gas HCl?",
            options = listOf(
                "pH awal = 9 + log 1.8; pH akhir = 9 + log 1.69",
                "pH awal = 5 - log 1.8; pH akhir = 5 - log 1.69",
                "pH awal = 9; pH akhir = 7",
                "pH awal = 9 + log 1.8; pH akhir = 9 + log 1.95",
                "pH awal = 8.5; pH akhir = 8.2"
            ),
            correctAnswerIndex = 0,
            explanation = "1. Mol awal:\n• Mol NH_3 (basa lemah) = 200 mL x 0.15 M = 30 mmol\n• Mol NH_4^+ (asam konjugasi) = 100 mL x 0.30 M = 30 mmol\n• [OH^-] = K_b \\times (30/30) = 1.8 \\times 10^{-5}\\text{ M} \\Rightarrow pOH = 5 - \\log 1.8 \\Rightarrow pH = 9 + \\log 1.8.\n\n2. Penambahan 1.0 mmol HCl:\n• HCl bereaksi dengan NH_3: NH_3 + H^+ \\rightarrow NH_4^+\n• Mol NH_3 baru = 30 - 1 = 29 mmol\n• Mol NH_4^+ baru = 30 + 1 = 31 mmol\n• [OH^-] = 1.8 \\times 10^{-5} \\times (29/31) = 1.684 \\times 10^{-5}\\text{ M}\n• pH = 14 - (5 - \\log 1.684) = 9 + \\log 1.684 \\approx 9 + \\log 1.69. pH relatif stabil berkat sistem buffer.",
            whyExplanation = "Asam kuat yang ditambahkan dinetralkan oleh komponen basa lemah penyangga sehingga penurunan pH sangat kecil.",
            conceptTested = "Kapasitas Larutan Penyangga & Aksi Penambahan Asam",
            errorType = ErrorType.CONCEPTUAL
        ),

        // SOAL TKA 7: ELEKTROKIMIA & HUKUM FARADAY
        Question(
            id = "tka_7",
            topicId = "topic_12_redox",
            difficulty = QuestionDifficulty.LEVEL_6_TKA_CHALLENGE,
            questionType = "TKA Elektrolisis & Stoikiometri Gas Katoda-Anoda",
            contextText = "Larutan CuSO_4 dielektrolisis menggunakan arus listrik sebesar 9.65 Ampere selama 1000 detik dengan elektroda karbon (C inert).",
            question = "Massa tembaga (Ar Cu = 63.5) yang mengendap di katoda dan volume gas O_2 (STP) yang dihasilkan di anoda berturut-turut adalah...",
            options = listOf(
                "3.175 gram dan 0.56 Liter",
                "6.350 gram dan 1.12 Liter",
                "3.175 gram dan 1.12 Liter",
                "1.587 gram dan 0.28 Liter",
                "6.350 gram dan 0.56 Liter"
            ),
            correctAnswerIndex = 0,
            explanation = "1. Jumlah muatan listrik: F = \\frac{I \\times t}{96500} = \\frac{9.65 \\times 1000}{96500} = 0.1\\text{ Faraday} = 0.1\\text{ mol elektron}.\n2. Reaksi Katoda: Cu^{2+} + 2e^- \\rightarrow Cu\n• Mol Cu = 0.1 / 2 = 0.05 mol\n• Massa Cu = 0.05 mol x 63.5 g/mol = 3.175 gram.\n3. Reaksi Anoda (Inert, sisa asam oksi): 2H_2O \\rightarrow 4H^+ + O_2(g) + 4e^-\n• Mol O_2 = 0.1 / 4 = 0.025 mol\n• Volume STP O_2 = 0.025 mol x 22.4 L/mol = 0.56 Liter.",
            whyExplanation = "Jumlah elektron yang mengalir pada katoda dan anoda sama persis (0.1 mol elektron).",
            conceptTested = "Hukum Faraday I & Stoikiometri Reaksi Anoda-Katoda",
            errorType = ErrorType.CALCULATION
        ),

        // SOAL TKA 8: SINTESIS ORGANIK & POLIMERISASI
        Question(
            id = "tka_8",
            topicId = "topic_13_organic",
            difficulty = QuestionDifficulty.LEVEL_5_EVALUATION,
            questionType = "TKA Kimia Organik & Polimer",
            contextText = "Suatu ester beraroma pisang (isopentil asetat) disintesis melalui reaksi refluks antara asam asetat (CH_3COOH) dengan isopentil alkohol (C_5H_{11}OH) dengan bantuan katalis asam H_2SO_4 pekat.",
            question = "Jenis reaksi yang terjadi dan cara untuk meningkatkan rendemen produk ester tersebut menurut asas kesetimbangan adalah...",
            options = listOf(
                "Esterifikasi Fischer; memisahkan air produk secara berkala dari labu reaksi",
                "Saponifikasi; menambahkan air berlebih",
                "Substitusi nukleofilik; menetralkan katalis asam sulfat",
                "Adisi elektrofilik; menurunkan suhu ke 0°C",
                "Eliminasi; menguapkan asam asetat reaktan"
            ),
            correctAnswerIndex = 0,
            explanation = "Reaksi pembentukan ester dari asam karboksilat dan alkohol disebut reaksi Esterifikasi Fischer: R-COOH + R'-OH \\rightleftharpoons R-COO-R' + H_2O. Karena reaksi ini bersifat kesetimbangan dinamis yang reversibel, memisahkan salah satu produk (air) dengan aparatus Dean-Stark akan menggeser kesetimbangan ke arah kanan (pembentukan ester) sehingga memaksimalkan rendemen.",
            whyExplanation = "Pengurangan konsentrasi produk memicu pergeseran kesetimbangan ke kanan menurut Asas Le Chatelier.",
            conceptTested = "Esterifikasi Fischer & Manipulasi Kesetimbangan Organik",
            errorType = ErrorType.REASONING
        )
    )
}
