package com.example.data.chemistry

import com.example.data.models.CommonMistake
import com.example.data.models.ErrorType
import com.example.data.models.GuidedPractice
import com.example.data.models.GuidedStep
import com.example.data.models.Lesson
import com.example.data.models.Question
import com.example.data.models.QuestionDifficulty
import com.example.data.models.Topic
import com.example.data.models.VisualCard
import com.example.data.models.WorkedExample

object CurriculumData {

    val topics: List<Topic> = listOf(
        // LEVEL 0
        Topic(
            id = "topic_0_foundation",
            levelNumber = 0,
            levelName = "LEVEL 0 — CHEMISTRY FOUNDATION",
            title = "Fondasi Kimia & Matematika Sains",
            description = "Materi, sifat fisika & kimia, pemisahan campuran, angka penting, notasi ilmiah, dan konversi satuan.",
            learningObjectives = listOf(
                "Membedakan unsur, senyawa, dan campuran serta metode pemisahannya.",
                "Menguasai aturan angka penting dan notasi ilmiah untuk perhitungan kuantitatif.",
                "Melakukan konversi satuan massa, volume, dan konsentrasi dengan presisi."
            ),
            lessons = listOf(
                Lesson(
                    id = "lesson_0_1",
                    topicId = "topic_0_foundation",
                    title = "Materi, Unsur, Senyawa & Pemisahan Campuran",
                    subtitle = "Klasifikasi materi dan fenomena perubahannya di alam",
                    durationMinutes = 15,
                    learningObjectives = listOf(
                        "Mengklasifikasikan zat murni (unsur/senyawa) vs campuran.",
                        "Menentukan metode pemisahan campuran (filtrasi, destilasi, kromatografi, kristalisasi)."
                    ),
                    preTest = listOf(
                        Question(
                            id = "pt_0_1",
                            topicId = "topic_0_foundation",
                            difficulty = QuestionDifficulty.LEVEL_1_RECALL,
                            question = "Manakah di antara zat berikut yang tergolong sebagai senyawa murni?",
                            options = listOf("Udara bersih", "Air murni (H₂O)", "Baja tahan karat", "Air teh manis"),
                            correctAnswerIndex = 1,
                            explanation = "Air murni (H₂O) adalah senyawa kimia murni yang terdiri dari hidrogen dan oksigen dengan perbandingan massa tetap.",
                            conceptTested = "Klasifikasi Materi (Senyawa vs Campuran)"
                        )
                    ),
                    conceptExplanation = """
                        Kimia adalah ilmu yang mempelajari susunan, struktur, sifat, dan perubahan materi serta energi yang menyertainya.
                        
                        1. Materi: Segala sesuatu yang memiliki massa dan menempati ruang.
                        2. Unsur: Zat tunggal yang tidak dapat diuraikan lagi menjadi zat yang lebih sederhana secara reaksi kimia biasa (contoh: Fe, Au, O₂, C).
                        3. Senyawa: Gabungan dua atau lebih unsur berbeda dengan perbandingan massa tetap melalui ikatan kimia (contoh: H₂O, NaCl, CO₂).
                        4. Campuran: Penggabungan fisik dua zat atau lebih tanpa reaksi kimia, dapat berupa homogen (larutan) atau heterogen (suspensi/koloid).
                        
                        Metode Pemisahan Campuran:
                        • Filtrasi: Pemisahan berdasarkan perbedaan ukuran partikel (contoh: menyaring pasir dari air).
                        • Destilasi: Pemisahan berdasarkan perbedaan titik didih (contoh: memisahkan alkohol dan air).
                        • Kromatografi: Pemisahan berdasarkan perbedaan kecepatan perambatan fase gerak pada fase diam (contoh: zat pewarna tinta).
                        • Kristalisasi: Pemisahan zat padat terlarut dari larutan jenuhnya (contoh: pembuatan garam dari air laut).
                    """.trimIndent(),
                    visualCards = listOf(
                        VisualCard(
                            title = "Pohon Klasifikasi Materi",
                            formulaOrSymbol = "Materi → Zat Murni (Unsur/Senyawa) | Campuran (Homogen/Heterogen)",
                            explanation = "Zat murni memiliki komposisi tetap dan titik leleh/didih tajam, sedangkan campuran memiliki komposisi variabel."
                        )
                    ),
                    workedExamples = listOf(
                        WorkedExample(
                            title = "Menentukan Metode Pemisahan Campuran",
                            problem = "Bagaimana cara memisahkan campuran air (titik didih 100°C) dan etanol (titik didih 78°C)?",
                            strategy = "Identifikasi perbedaan sifat fisika utama antara komponen penyusun campuran.",
                            calculation = "\\Delta T_b = 100^\\circ\\text{C} - 78^\\circ\\text{C} = 22^\\circ\\text{C} (Perbedaan titik didih signifikan)",
                            answer = "Destilasi Bertingkat (Fractional Distillation)",
                            explanation = "Karena ada perbedaan titik didih yang cukup besar, etanol akan menguap terlebih dahulu pada 78°C, uapnya didinginkan oleh kondensor dan ditampung sebagai distilat murni."
                        )
                    ),
                    commonMistakes = listOf(
                        CommonMistake(
                            mistake = "Menganggap udara adalah senyawa kimia karena tidak terlihat batas fasenya.",
                            correctConcept = "Udara adalah campuran homogen (larutan gas) dari N₂, O₂, Ar, CO₂, dan uap air.",
                            explanation = "Campuran homogen tidak memiliki ikatan kimia tetap antar gas penyusunnya."
                        )
                    ),
                    guidedPractices = listOf(
                        GuidedPractice(
                            problem = "Tentukan metode pemisahan untuk mengekstraksi iodin (padatan yang mudah menyublim) dari campuran pasir kotor!",
                            steps = listOf(
                                GuidedStep(
                                    stepNumber = 1,
                                    instruction = "Perhatikan sifat fisik khas iodin saat dipanaskan.",
                                    hint = "Iodin padat langsung berubah menjadi gas ungu saat dipanaskan tanpa meleleh terlebih dahulu.",
                                    solution = "Sifat menyublim (sublimasi)."
                                ),
                                GuidedStep(
                                    stepNumber = 2,
                                    instruction = "Pilih metode pemisahan yang memanfaatkan perubahan fase padat-gas tersebut.",
                                    hint = "Gunakan kaca arloji berisi es batu di atas cawan pemanas.",
                                    solution = "Metode Sublimasi."
                                )
                            ),
                            finalAnswer = "Sublimasi"
                        )
                    ),
                    exitTest = listOf(
                        Question(
                            id = "ex_0_1",
                            topicId = "topic_0_foundation",
                            difficulty = QuestionDifficulty.LEVEL_2_UNDERSTANDING,
                            question = "Pemisahan fraksi minyak bumi seperti bensin, kerosin, dan solar dari minyak mentah didasarkan pada metode...",
                            options = listOf("Filtrasi gravitasi", "Destilasi bertingkat", "Kromatografi kertas", "Kristalisasi penguapan"),
                            correctAnswerIndex = 1,
                            explanation = "Minyak mentah dipisahkan dalam kolom fraksionasi menggunakan prinsip destilasi bertingkat berdasarkan urutan titik didih hidrokarbon.",
                            conceptTested = "Pemisahan Campuran Industri"
                        )
                    )
                )
            )
        ),

        // LEVEL 1
        Topic(
            id = "topic_1_atomic",
            levelNumber = 1,
            levelName = "LEVEL 1 — ATOMIC WORLD",
            title = "Struktur Atom & Konfigurasi Elektron",
            description = "Partikel subatomik (proton, neutron, elektron), nomor atom, nomor massa, isotop, konfigurasi Bohr & mekanika kuantum, dan bilangan kuantum.",
            learningObjectives = listOf(
                "Menghitung jumlah proton, neutron, dan elektron pada atom netral maupun ion bermuatan.",
                "Menuliskan konfigurasi elektron berdasarkan prinsip Aufbau, larangan Pauli, dan aturan Hund.",
                "Menentukan 4 bilangan kuantum (n, l, m, s) untuk elektron valensi atau elektron terakhir."
            ),
            lessons = listOf(
                Lesson(
                    id = "lesson_1_1",
                    topicId = "topic_1_atomic",
                    title = "Struktur Atom & Notasi Nuklida",
                    subtitle = "Memahami inti atom, proton, neutron, dan elektron",
                    durationMinutes = 15,
                    learningObjectives = listOf(
                        "Menghitung p, e, n dari notasi nuklida ^A_Z X.",
                        "Membedakan isotop, isobar, dan isoton."
                    ),
                    conceptExplanation = """
                        Atom tersusun atas:
                        1. Inti Atom (Nukleus): Mengandung proton (p^+) bermuatan positif dan neutron (n^0) yang netral.
                        2. Kulit Atom: Ditempati elektron (e^-) bermuatan negatif yang bergerak mengelilingi inti.
                        
                        Notasi Nuklida:
                        ^{A}_{Z}\text{X}
                        • Z = Nomor Atom = Jumlah Proton = Jumlah Elektron (pada atom netral)
                        • A = Nomor Massa = Jumlah Proton + Jumlah Neutron
                        • Jumlah Neutron = A - Z
                        
                        Ion:
                        • Kation (X^{n+}): Atom yang melepaskan n elektron \rightarrow e = Z - n
                        • Anion (X^{n-}): Atom yang menangkap n elektron \rightarrow e = Z + n
                        
                        Relasi Nuklida:
                        • Isotop: Z sama, A beda (contoh: ^{12}_6C dan ^{14}_6C)
                        • Isobar: A sama, Z beda (contoh: ^{14}_6C dan ^{14}_7N)
                        • Isoton: Jumlah neutron (A-Z) sama (contoh: ^{14}_6C [n=8] dan ^{16}_8O [n=8])
                    """.trimIndent(),
                    visualCards = listOf(
                        VisualCard(
                            title = "Notasi Atom Standar",
                            formulaOrSymbol = "^{A}_{Z}\\text{X}^{\\pm q}",
                            explanation = "Z = nomor atom (proton), A = nomor massa (p+n), q = muatan ion."
                        )
                    ),
                    workedExamples = listOf(
                        WorkedExample(
                            title = "Menghitung Partikel Subatomik Ion Besi Fe³⁺",
                            problem = "Tentukan jumlah proton, neutron, dan elektron pada ion ^{56}_{26}\\text{Fe}^{3+}!",
                            strategy = "Gunakan rumus p = Z, n = A - Z, dan e = Z - \\text{muatan}.",
                            calculation = "• Proton = Z = 26\n• Neutron = A - Z = 56 - 26 = 30\n• Elektron = 26 - (+3) = 23 elektron",
                            answer = "Proton = 26, Neutron = 30, Elektron = 23",
                            explanation = "Karena bermuatan +3, atom besi melepaskan 3 elektron dari kulit terluarnya, sedangkan jumlah proton dan neutron di dalam inti tetap."
                        )
                    ),
                    commonMistakes = listOf(
                        CommonMistake(
                            mistake = "Mengubah jumlah proton ketika atom membentuk ion positif atau negatif.",
                            correctConcept = "Pembentukan ion HANYA melibatkan perpindahan (pelepasan/penangkapan) elektron pada kulit luar. Jumlah proton di inti TIDAK PERNAH berubah pada reaksi kimia biasa.",
                            explanation = "Perubahan jumlah proton hanya terjadi pada reaksi fisi/fusi nuklir."
                        )
                    ),
                    guidedPractices = listOf(
                        GuidedPractice(
                            problem = "Tentukan jumlah elektron pada ion ^{32}_{16}\\text{S}^{2-}!",
                            steps = listOf(
                                GuidedStep(
                                    stepNumber = 1,
                                    instruction = "Identifikasi nomor atom (Z) belerang.",
                                    hint = "Lihat angka di subskrip kiri bawah.",
                                    solution = "Z = 16 (artinya ada 16 proton)."
                                ),
                                GuidedStep(
                                    stepNumber = 2,
                                    instruction = "Perhatikan muatan ion (-2) yang menandakan penangkapan elektron.",
                                    hint = "Elektron = Z + 2.",
                                    solution = "e = 16 + 2 = 18 elektron."
                                )
                            ),
                            finalAnswer = "18 elektron"
                        )
                    ),
                    exitTest = listOf(
                        Question(
                            id = "ex_1_1",
                            topicId = "topic_1_atomic",
                            difficulty = QuestionDifficulty.LEVEL_3_APPLICATION,
                            question = "Suatu ion X^{2+} memiliki 18 elektron dan 20 neutron. Nomor massa atom X tersebut adalah...",
                            options = listOf("38", "40", "36", "20"),
                            correctAnswerIndex = 1,
                            explanation = "Elektron = 18 pada ion X^{2+}, maka nomor atom netral Z = 18 + 2 = 20. Nomor massa A = Z + n = 20 + 20 = 40. (Atom Ca).",
                            conceptTested = "Perhitungan Partikel Subatomik dan Nomor Massa"
                        )
                    )
                )
            )
        ),

        // LEVEL 2
        Topic(
            id = "topic_2_periodic",
            levelNumber = 2,
            levelName = "LEVEL 2 — PERIODIC TABLE",
            title = "Tabel Periodik & Sifat Keperiodikan",
            description = "Golongan, periode, jari-jari atom, energi ionisasi, afinitas elektron, keelektronegatifan, dan sifat logam/nonlogam.",
            learningObjectives = listOf(
                "Menentukan letak golongan dan periode unsur dari konfigurasi elektron.",
                "Menganalisis kecenderungan periodik (jari-jari, energi ionisasi, elektronegativitas) dalam satu golongan dan periode.",
                "Menjelaskan anomali energi ionisasi pada golongan IIA vs IIIA dan VA vs VIA."
            ),
            lessons = listOf(
                Lesson(
                    id = "lesson_2_1",
                    topicId = "topic_2_periodic",
                    title = "Sifat Keperiodikan Unsur",
                    subtitle = "Jari-jari atom, energi ionisasi, dan keelektronegatifan",
                    durationMinutes = 15,
                    learningObjectives = listOf(
                        "Menjelaskan hubungan muatan inti efektif (Z_{eff}) dan jumlah kulit terhadap sifat periodik."
                    ),
                    conceptExplanation = """
                        Sifat Keperiodikan Unsur:
                        
                        1. Jari-jari Atom:
                        • Dalam satu golongan (atas ke bawah): Jari-jari MAKIN BESAR karena jumlah kulit atom bertambah.
                        • Dalam satu periode (kiri ke kanan): Jari-jari MAKIN KECIL karena muatan inti efektif (Z_{eff}) bertambah, menarik elektron lebih kuat ke inti.
                        
                        2. Energi Ionisasi (EI):
                        • Energi minimum yang dibutuhkan untuk melepaskan 1 elektron terluar dari atom berwujud gas.
                        • Kiri ke kanan makin BESAR; Atas ke bawah makin KECIL.
                        • Anomali: EI Golongan IIA (s^2) > IIIA (s^2 p^1) dan Golongan VA (p^3, setengah penuh stabil) > VIA (p^4).
                        
                        3. Keelektronegatifan:
                        • Kemampuan relatif suatu atom untuk menarik pasangan elektron ikatan ke arah dirinya.
                        • Fluor (F) memiliki keelektronegatifan tertinggi (3.98 skala Pauling), diikuti O (3.44), N (3.04), dan Cl (3.16).
                    """.trimIndent(),
                    visualCards = listOf(
                        VisualCard(
                            title = "Aturan Tren Periodik",
                            formulaOrSymbol = "\\text{Kiri-ke-Kanan: Jari-jari } \\downarrow, \\text{ EI } \\uparrow, \\text{ Elektronegativitas } \\uparrow",
                            explanation = "Atas-ke-Bawah: Kulit bertambah -> Jari-jari membesar -> Elektron luar mudah lepas -> EI menurun."
                        )
                    ),
                    workedExamples = listOf(
                        WorkedExample(
                            title = "Mengurutkan Jari-Jari Atom Unsur Seperiode",
                            problem = "Urutkan unsur ^{11}\\text{Na}, ^{12}\\text{Mg}, dan ^{17}\\text{Cl} berdasarkan jari-jari atomnya dari yang terbesar ke terkecil!",
                            strategy = "Ketiga unsur berada pada periode ke-3 (kulit n=3 sama). Perhatikan jumlah proton inti.",
                            calculation = "• Na (Z=11 proton): Tarikan inti paling lemah \\rightarrow Jari-jari terbesar\n• Mg (Z=12 proton)\n• Cl (Z=17 proton): Tarikan inti paling kuat \\rightarrow Jari-jari terkecil",
                            answer = "Na > Mg > Cl",
                            explanation = "Dalam satu periode dari kiri ke kanan, muatan inti bertambah sementara kulit tetap, sehingga awan elektron tertarik lebih rapat ke inti."
                        )
                    ),
                    exitTest = listOf(
                        Question(
                            id = "ex_2_1",
                            topicId = "topic_2_periodic",
                            difficulty = QuestionDifficulty.LEVEL_4_ANALYSIS,
                            question = "Mengapa energi ionisasi pertama unsur ^{7}\\text{N} (Golongan VA) lebih tinggi daripada ^{8}\\text{O} (Golongan VIA)?",
                            options = listOf(
                                "Karena orbital 2p pada nitrogen terisi setengah penuh (2p³) yang sangat stabil",
                                "Karena jari-jari atom oksigen jauh lebih besar daripada nitrogen",
                                "Karena nitrogen tergolong sebagai logam transisi",
                                "Karena oksigen memiliki jumlah kulit yang lebih banyak daripada nitrogen"
                            ),
                            correctAnswerIndex = 0,
                            explanation = "Konfigurasi elektron nitrogen adalah [He] 2s² 2p³. Subkulit 2p yang terisi setengah penuh memiliki simetri dan stabilitas pertukaran yang tinggi, sehingga membutuhkan energi lebih besar untuk melepaskan elektronnya dibanding oksigen (2p⁴) yang memiliki tolakan antar pasangan elektron.",
                            conceptTested = "Anomali Energi Ionisasi & Stabilitas Subkulit Setengah Penuh"
                        )
                    )
                )
            )
        ),

        // LEVEL 3
        Topic(
            id = "topic_3_bonding",
            levelNumber = 3,
            levelName = "LEVEL 3 — CHEMICAL BONDING",
            title = "Ikatan Kimia & Geometri Molekul",
            description = "Ikatan ion, kovalen tunggal/rangkap/koordinasi, ikatan logam, struktur Lewis, teori VSEPR, bentuk molekul, dan kepolaran.",
            learningObjectives = listOf(
                "Membedakan ikatan ion, kovalen, dan kovalen koordinasi berdasarkan transfer/sharing elektron.",
                "Meramalkan bentuk geometri molekul menggunakan rumus tipe AX_m E_n (Teori VSEPR/Domain Elektron).",
                "Menentukan kepolaran molekul berdasarkan momen dipol dan simetri geometri."
            ),
            lessons = listOf(
                Lesson(
                    id = "lesson_3_1",
                    topicId = "topic_3_bonding",
                    title = "Teori VSEPR & Bentuk Molekul",
                    subtitle = "Menentukan geometri molekul dari pasangan elektron ikatan (PEI) dan bebas (PEB)",
                    durationMinutes = 20,
                    learningObjectives = listOf(
                        "Menghitung domain elektron, PEI, dan PEB.",
                        "Menentukan bentuk molekul: Linier, Trigonal planar, Tetrahedral, Trigonal piramida, Bentuk V, Bipiramida trigonal, Oktahedral."
                    ),
                    conceptExplanation = """
                        Teori Domain Elektron / VSEPR (Valence Shell Electron Pair Repulsion):
                        Pasangan elektron valensi di sekitar atom pusat saling tolak-menolak sehingga menata diri sedemikian rupa untuk meminimalkan gaya tolak.
                        
                        Kekuatan Tolakan:
                        PEB - PEB > PEB - PEI > PEI - PEI
                        
                        Rumus Tipe Molekul:
                        AX_m E_n
                        • A = Atom pusat
                        • X = Pasangan Elektron Ikatan (PEI), m = jumlah PEI
                        • E = Pasangan Elektron Bebas (PEB), n = jumlah PEB
                        
                        Rumus menghitung PEB (E):
                        E = \\frac{EV - (X \\times \\text{valensi terikat})}{2}
                        
                        Contoh Bentuk Molekul Populer:
                        • AX_2 (contoh: BeCl_2, CO_2) \\rightarrow Linier (180°)
                        • AX_3 (contoh: BF_3, SO_3) \\rightarrow Trigonal Planar (120°)
                        • AX_2 E (contoh: SO_2) \\rightarrow Bentuk V / Bengkok (<120°)
                        • AX_4 (contoh: CH_4, CCl_4) \\rightarrow Tetrahedral (109.5°)
                        • AX_3 E (contoh: NH_3) \\rightarrow Trigonal Piramida (107°)
                        • AX_2 E_2 (contoh: H_2O) \\rightarrow Bentuk V / Bengkok (104.5°)
                        • AX_5 (contoh: PCl_5) \\rightarrow Bipiramida Trigonal
                        • AX_4 E (contoh: SF_4) \\rightarrow Tetrahedral Terdistorsi (Jungkat-jungkit / Seesaw)
                        • AX_3 E_2 (contoh: ClF_3) \\rightarrow Bentuk T
                        • AX_2 E_3 (contoh: XeF_2) \\rightarrow Linier
                        • AX_6 (contoh: SF_6) \\rightarrow Oktahedral (90°)
                        • AX_4 E_2 (contoh: XeF_4) \\rightarrow Segiempat Planar
                    """.trimIndent(),
                    visualCards = listOf(
                        VisualCard(
                            title = "Rumus Menghitung PEB (E)",
                            formulaOrSymbol = "E = \\frac{\\text{EV(Pusat)} - \\sum \\text{elektron terpakai ikatan}}{2}",
                            explanation = "PEB menyebabkan penyempitan sudut ikatan karena gaya tolak PEB lebih dominan dibanding PEI."
                        )
                    ),
                    workedExamples = listOf(
                        WorkedExample(
                            title = "Meramalkan Bentuk Geometri Molekul Air (H₂O)",
                            problem = "Tentukan tipe molekul dan bentuk geometri molekul H_2O (Nomor atom H=1, O=8)!",
                            strategy = "Tentukan atom pusat, elektron valensi, jumlah PEI, dan hitung PEB.",
                            calculation = "• Atom pusat: O (EV = 6)\n• Atom terikat: 2 atom H (masing-masing butuh 1e -> total 2e dipakai ikatan)\n• PEI (m) = 2\n• PEB (n) = (6 - 2)/2 = 4/2 = 2\n• Tipe Molekul = AX₂E₂",
                            answer = "Tipe AX₂E₂ -> Bentuk V / Bengkok (Bent), sudut ~104.5°",
                            explanation = "Adanya 2 pasang elektron bebas (PEB) menekan sudut ikatan H-O-H dari sudut tetrahedral ideal 109.5° menjadi 104.5°."
                        )
                    ),
                    exitTest = listOf(
                        Question(
                            id = "ex_3_1",
                            topicId = "topic_3_bonding",
                            difficulty = QuestionDifficulty.LEVEL_4_ANALYSIS,
                            question = "Senyawa XeF_4 (Xe nomor atom 54, F nomor atom 9) memiliki bentuk geometri molekul...",
                            options = listOf("Tetrahedral", "Segiempat planar (Square planar)", "Bipiramida trigonal", "Jungkat-jungkit (Seesaw)"),
                            correctAnswerIndex = 1,
                            explanation = "Elektron valensi Xe = 8. Mengikat 4 atom F (PEI = 4). Sisa elektron bebas = 8 - 4 = 4 elektron = 2 pasang PEB (E = 2). Tipe molekul adalah AX_4 E_2, yang memiliki geometri dasar oktahedral dengan 2 PEB di posisi aksial, sehingga bentuk molekul teramati adalah segiempat planar (square planar) dan bersifat nonpolar karena simetris.",
                            conceptTested = "Bentuk Molekul Oktet Berkembang (VSEPR)"
                        )
                    )
                )
            )
        ),

        // LEVEL 4
        Topic(
            id = "topic_4_mole",
            levelNumber = 4,
            levelName = "LEVEL 4 — MOLE CONCEPT",
            title = "Konsep Mol & Jembatan Stoikiometri",
            description = "Mol, bilangan Avogadro (6.022 × 10²³), massa molar, volume molar gas (STP, RTP), rumus empiris, dan penyetaraan reaksi.",
            learningObjectives = listOf(
                "Mengonversi massa zat, jumlah partikel, dan volume gas ke dalam satuan mol.",
                "Menyetarakan persamaan reaksi kimia secara matematis.",
                "Menentukan rumus empiris dan rumus molekul dari data persentase massa."
            ),
            lessons = listOf(
                Lesson(
                    id = "lesson_4_1",
                    topicId = "topic_4_mole",
                    title = "Konsep Mol & Konversi Kuantitatif",
                    subtitle = "Jembatan penghubung massa, partikel, dan volume gas",
                    durationMinutes = 15,
                    learningObjectives = listOf(
                        "Menguasai hubungan n = gr/Mr, N = n \\times N_A, V_{STP} = n \\times 22.4."
                    ),
                    conceptExplanation = """
                        Satu mol zat adalah banyaknya zat yang mengandung partikel sebanyak jumlah atom dalam 12 gram karbon-12 (^{12}C), yaitu 6.022 \\times 10^{23} partikel (Bilangan Avogadro, N_A).
                        
                        Rumus Jembatan Mol:
                        1. Mol terhadap Massa:
                           n = \\frac{\\text{massa (gram)}}{Mr \\text{ (g/mol)}}
                        
                        2. Mol terhadap Jumlah Partikel (N):
                           N = n \\times 6.022 \\times 10^{23}
                        
                        3. Mol terhadap Volume Gas:
                           • Kondisi Standar STP (0°C, 1 atm): V = n \\times 22.4\\text{ Liter}
                           • Kondisi Kamar RTP (25°C, 1 atm): V = n \\times 24.0\\text{ Liter}
                           • Kondisi Gas Ideal Umum: V = \\frac{nRT}{P}
                           • Perbandingan Gas pada T, P Sama: \\frac{V_1}{V_2} = \\frac{n_1}{n_2} (Hukum Avogadro)
                    """.trimIndent(),
                    visualCards = listOf(
                        VisualCard(
                            title = "Peta Konsep Jembatan Mol",
                            formulaOrSymbol = "\\text{Massa (g)} \\xleftrightarrow{\\div / \\times Mr} \\text{MOL (n)} \\xleftrightarrow{\\times / \\div N_A} \\text{Partikel (N)} \\xleftrightarrow{\\times / \\div 22.4} \\text{Volume STP (L)}",
                            explanation = "Mol selalu menjadi pusat konversi kuantitas zat dalam seluruh perhitungan kimia."
                        )
                    ),
                    workedExamples = listOf(
                        WorkedExample(
                            title = "Menghitung Jumlah Molekul Gas Karbon Dioksida",
                            problem = "Berapa jumlah molekul yang terdapat dalam 8.8 gram gas CO_2 (Ar C=12, O=16)?",
                            strategy = "Hitung Mr CO₂, lalu hitung mol (n), kemudian kalikan dengan bilangan Avogadro.",
                            calculation = "• Mr CO₂ = 12 + 2(16) = 44 g/mol\n• n = 8.8 g / 44 g/mol = 0.2 mol\n• Jumlah Molekul N = 0.2 x 6.022 x 10²³ = 1.2044 x 10²³ molekul",
                            answer = "1.2044 × 10²³ molekul CO₂",
                            explanation = "Setiap 1 mol zat mengandung 6.022 × 10²³ partikel. Karena ada 0.2 mol, maka jumlah molekulnya adalah seperlima dari bilangan Avogadro."
                        )
                    ),
                    exitTest = listOf(
                        Question(
                            id = "ex_4_1",
                            topicId = "topic_4_mole",
                            difficulty = QuestionDifficulty.LEVEL_3_APPLICATION,
                            question = "Pada kondisi STP, 5.6 Liter suatu gas hidrokarbon memiliki massa 11 gram. Massa molar (M_r) gas tersebut adalah...",
                            options = listOf("28 g/mol", "44 g/mol", "58 g/mol", "32 g/mol"),
                            correctAnswerIndex = 1,
                            explanation = "Mol gas pada STP: n = \\frac{V}{22.4} = \\frac{5.6}{22.4} = 0.25\\text{ mol}. Maka Mr = \\frac{\\text{massa}}{n} = \\frac{11\\text{ g}}{0.25\\text{ mol}} = 44\\text{ g/mol} (gas propana C_3H_8 atau CO_2).",
                            conceptTested = "Hubungan Massa Molar dan Volume Molar STP"
                        )
                    )
                )
            )
        ),

        // LEVEL 5
        Topic(
            id = "topic_5_stoichiometry",
            levelNumber = 5,
            levelName = "LEVEL 5 — STOICHIOMETRY",
            title = "Stoikiometri Reaksi & Pereaksi Pembatas",
            description = "Perbandingan koefisien reaksi, pereaksi pembatas (limiting reactant), persen hasil (percent yield), dan kemurnian zat.",
            learningObjectives = listOf(
                "Menentukan pereaksi pembatas menggunakan uji rasio mol/koefisien.",
                "Menghitung massa atau volume produk teoritis yang terbentuk dari reaksi kimia.",
                "Menghitung persen hasil reaksi \\% \\text{ Yield} = \\frac{\\text{Hasil Aktual}}{\\text{Hasil Teoretis}} \\times 100\\%."
            ),
            lessons = listOf(
                Lesson(
                    id = "lesson_5_1",
                    topicId = "topic_5_stoichiometry",
                    title = "Pereaksi Pembatas & Hasil Reaksi",
                    subtitle = "Langkah sistematis menyelesaikan soal stoikiometri multi-reaktan",
                    durationMinutes = 20,
                    learningObjectives = listOf(
                        "Menentukan pereaksi pembatas dan sisa pereaksi berlebih.",
                        "Menghitung massa endapan atau gas produk yang dihasilkan."
                    ),
                    conceptExplanation = """
                        Dalam suatu reaksi kimia, perbandingan koefisien reaksi setara menyatakan perbandingan mol zat-zat yang bereaksi dan dihasilkan.
                        
                        Konsep Pereaksi Pembatas:
                        Jika dua reaktan dicampurkan tidak dalam perbandingan stoikiometri yang pas, salah satu reaktan akan habis terlebih dahulu dan menghentikan reaksi. Zat ini disebut Pereaksi Pembatas (Limiting Reactant).
                        
                        Langkah Menentukan Pereaksi Pembatas:
                        1. Tulis dan setarakan persamaan reaksi kimia.
                        2. Ubah kuantitas semua reaktan menjadi satuan Mol (n).
                        3. Bagi mol masing-masing reaktan dengan koefisiennya:
                           \\text{Rasio} = \\frac{n_{\\text{reaktan}}}{\\text{koefisien}}
                        4. Zat dengan nilai rasio terkecil adalah PEREAKSI PEMBATAS.
                        5. Gunakan mol pereaksi pembatas untuk menghitung mol produk dan mol reaktan lain yang bereaksi.
                    """.trimIndent(),
                    visualCards = listOf(
                        VisualCard(
                            title = "Tabel M-B-S (Mula-Mula, Bereaksi, Sisa)",
                            formulaOrSymbol = "\\text{Rasio terkecil } \\frac{n}{\\text{koef}} = \\text{Pereaksi Pembatas (Habis)}",
                            explanation = "Baris 'Bereaksi' selalu sebanding dengan koefisien reaksi setara."
                        )
                    ),
                    workedExamples = listOf(
                        WorkedExample(
                            title = "Reaksi Pembentukan Amonia (Haber-Bosch)",
                            problem = "Sebanyak 3 mol gas nitrogen (N_2) direaksikan dengan 6 mol gas hidrogen (H_2) menurut reaksi: N_2(g) + 3H_2(g) \\rightarrow 2NH_3(g). Tentukan pereaksi pembatas dan jumlah mol NH_3 yang terbentuk!",
                            strategy = "Hitung rasio mol/koefisien untuk N_2 dan H_2.",
                            calculation = "• Rasio N₂ = 3 / 1 = 3\n• Rasio H₂ = 6 / 3 = 2\n• Karena 2 < 3, maka H₂ adalah PEREAKSI PEMBATAS (habis bereaksi).\n• Mol NH₃ terbentuk = (2/3) x 6 mol H₂ = 4 mol NH₃.\n• Sisa N₂ = 3 - (1/3 x 6) = 3 - 2 = 1 mol N₂.",
                            answer = "Pereaksi Pembatas = H₂; Produk NH₃ = 4 mol (68 gram)",
                            explanation = "Gas hidrogen habis terlebih dahulu sehingga membatasi pembentukan amonia maksimal hanya 4 mol."
                        )
                    ),
                    exitTest = listOf(
                        Question(
                            id = "ex_5_1",
                            topicId = "topic_5_stoichiometry",
                            difficulty = QuestionDifficulty.LEVEL_4_ANALYSIS,
                            question = "Logam aluminium (Ar=27) seberat 5.4 gram direaksikan dengan 100 mL larutan HCl 2 M menurut reaksi: 2Al(s) + 6HCl(aq) \\rightarrow 2AlCl_3(aq) + 3H_2(g). Volume gas H_2 yang dihasilkan pada kondisi STP adalah...",
                            options = listOf("2.24 L", "6.72 L", "4.48 L", "3.36 L"),
                            correctAnswerIndex = 0,
                            explanation = "• Mol Al = 5.4 / 27 = 0.2 mol. Rasio Al = 0.2 / 2 = 0.1\n• Mol HCl = 0.1 L x 2 M = 0.2 mol. Rasio HCl = 0.2 / 6 = 0.0333\n• HCl memiliki rasio terkecil -> HCl adalah pereaksi pembatas (habis).\n• Mol H₂ yang terbentuk = (3 / 6) x mol HCl = 0.5 x 0.2 = 0.1 mol H₂.\n• Volume STP H₂ = 0.1 mol x 22.4 L/mol = 2.24 Liter.",
                            conceptTested = "Pereaksi Pembatas dan Stoikiometri Gas STP"
                        )
                    )
                )
            )
        ),

        // LEVEL 10
        Topic(
            id = "topic_10_equilibrium",
            levelNumber = 10,
            levelName = "LEVEL 10 — CHEMICAL EQUILIBRIUM",
            title = "Kesetimbangan Kimia & Asas Le Chatelier",
            description = "Kesetimbangan dinamis, tetapan kesetimbangan K_c & K_p, pergeseran kesetimbangan (konsentrasi, suhu, tekanan/volume, katalis).",
            learningObjectives = listOf(
                "Menuliskan ungkapan tetapan kesetimbangan K_c dan K_p untuk reaksi homogen dan heterogen.",
                "Meramalkan arah pergeseran kesetimbangan berdasarkan Asas Le Chatelier.",
                "Menghubungkan nilai K_p dan K_c melalui rumus K_p = K_c (RT)^{\\Delta n}."
            ),
            lessons = listOf(
                Lesson(
                    id = "lesson_10_1",
                    topicId = "topic_10_equilibrium",
                    title = "Asas Le Chatelier & Faktor Pergeseran",
                    subtitle = "Respon sistem kesetimbangan dinamis terhadap gangguan luar",
                    durationMinutes = 20,
                    learningObjectives = listOf(
                        "Menganalisis efek penambahan konsentrasi, pengubahan volume/tekanan, dan perubahan suhu terhadap arah reaksi."
                    ),
                    conceptExplanation = """
                        Asas Le Chatelier:
                        Jika suatu sistem kesetimbangan diberikan aksi/gangguan luar, sistem akan mengadakan reaksi/pergeseran untuk meminimalkan pengaruh aksi tersebut.
                        
                        Faktor-Faktor Pergeseran Kesetimbangan:
                        1. Perubahan Konsentrasi:
                           • Zat ditambah \\rightarrow Kesetimbangan bergeser MENJAUHI zat tersebut.
                           • Zat dikurangi \\rightarrow Kesetimbangan bergeser MENDEKATI zat tersebut.
                        
                        2. Perubahan Tekanan & Volume (Khusus Fase Gas):
                           • Tekanan diperbesar (Volume diperkecil) \\rightarrow Bergeser ke arah jumlah KOEFISIEN GAS LEBIH KECIL.
                           • Tekanan diperkecil (Volume diperbesar) \\rightarrow Bergeser ke arah jumlah KOEFISIEN GAS LEBIH BESAR.
                        
                        3. Perubahan Suhu (Satu-satunya faktor yang mengubah nilai K):
                           • Suhu dinaikkan \\rightarrow Bergeser ke arah reaksi ENDOTERM (\\Delta H > 0).
                           • Suhu diturunkan \\rightarrow Bergeser ke arah reaksi EKSOTERM (\\Delta H < 0).
                        
                        4. Katalis:
                           • Katalis TIDAK MENGGESER kesetimbangan dan TIDAK MENGUBAH nilai K, hanya mempercepat tercapainya keadaan setimbang.
                    """.trimIndent(),
                    visualCards = listOf(
                        VisualCard(
                            title = "Matriks Le Chatelier",
                            formulaOrSymbol = "\\text{Suhu } \\uparrow \\rightarrow \\text{Endoterm} | \\text{Tekanan } \\uparrow \\rightarrow \\text{Koef Gas Kecil}",
                            explanation = "Hanya suhu yang dapat mengubah nilai numerik tetapan kesetimbangan Kc atau Kp."
                        )
                    ),
                    exitTest = listOf(
                        Question(
                            id = "ex_10_1",
                            topicId = "topic_10_equilibrium",
                            difficulty = QuestionDifficulty.LEVEL_5_EVALUATION,
                            question = "Pada reaksi kesetimbangan: 2SO_2(g) + O_2(g) \\rightleftharpoons 2SO_3(g) \\quad \\Delta H = -198\\text{ kJ}. Agar produksi gas SO_3 meningkat secara maksimal, perlakuan yang tepat adalah...",
                            options = listOf(
                                "Menaikkan suhu dan memperbesar volume wadah",
                                "Menurunkan suhu dan memperbesar tekanan sistem",
                                "Menambahkan katalis dan menaikkan suhu",
                                "Mengurangi konsentrasi gas O_2 dan menurunkan tekanan"
                            ),
                            correctAnswerIndex = 1,
                            explanation = "Reaksi pembentukan SO_3 bersifat eksoterm (\\Delta H < 0). Maka menurunkan suhu akan menggeser reaksi ke arah eksoterm (kanan/SO_3). Selain itu, koefisien gas kiri (2+1=3) lebih besar daripada kanan (2), sehingga memperbesar tekanan akan menggeser kesetimbangan ke arah koefisien gas lebih kecil (kanan/SO_3). Kombinasi penurunan suhu dan penaikan tekanan akan memaksimalkan hasil SO_3.",
                            conceptTested = "Optimasi Reaksi Kesetimbangan Industri (Proses Kontak)"
                        )
                    )
                )
            )
        ),

        // LEVEL 11
        Topic(
            id = "topic_11_acidbase",
            levelNumber = 11,
            levelName = "LEVEL 11 — ACID BASE",
            title = "Larutan Asam-Basa, Buffer & Titrasi",
            description = "Teori asam-basa, perhitungan pH/pOH asam-basa kuat & lemah, larutan penyangga (buffer), hidrolisis garam, dan kurva titrasi.",
            learningObjectives = listOf(
                "Menghitung pH larutan asam/basa kuat, lemah, campuran penyangga, dan garam terhidrolisis.",
                "Menganalisis sifat larutan penyangga dalam mempertahankan pH tubuh dan laboratorium.",
                "Menginterpretasikan kurva titrasi asam-basa dan pemilihan indikator yang tepat."
            ),
            lessons = listOf(
                Lesson(
                    id = "lesson_11_1",
                    topicId = "topic_11_acidbase",
                    title = "Larutan Penyangga (Buffer) & Hidrolisis Garam",
                    subtitle = "Mekanisme kestabilan pH dan penguraian ion garam dalam air",
                    durationMinutes = 25,
                    learningObjectives = listOf(
                        "Membedakan komponen buffer asam (HA/A^-) dan buffer basa (B/BH^+).",
                        "Menghitung pH hidrolisis garam kation/anion lemah."
                    ),
                    conceptExplanation = """
                        1. Larutan Penyangga (Buffer):
                        Larutan yang dapat mempertahankan pH-nya terhadap sedikit penambahan asam, basa, atau pengenceran.
                        
                        • Buffer Asam: Campuran Asam Lemah + Basa Konjugasinya (contoh: CH_3COOH + CH_3COONa)
                          [H^+] = K_a \\times \\frac{\\text{mol Asam Lemah}}{\\text{mol Basa Konjugasi}}
                        
                        • Buffer Basa: Campuran Basa Lemah + Asam Konjugasinya (contoh: NH_3 + NH_4Cl)
                          [OH^-] = K_b \\times \\frac{\\text{mol Basa Lemah}}{\\text{mol Asam Konjugasi}}
                        
                        2. Hidrolisis Garam:
                        Reaksi antara ion garam (dari asam/basa lemah) dengan molekul air.
                        • Garam dari Asam Kuat + Basa Kuat (contoh: NaCl) \\rightarrow Tidak terhidrolisis, pH = 7 (Netral).
                        • Garam dari Asam Lemah + Basa Kuat (contoh: CH_3COONa) \\rightarrow Hidrolisis Sebagian (Anion), bersifat Basa:
                          [OH^-] = \\sqrt{\\frac{K_w}{K_a} \\times [\\text{Anion}]}
                        • Garam dari Asam Kuat + Basa Lemah (contoh: NH_4Cl) \\rightarrow Hidrolisis Sebagian (Kation), bersifat Asam:
                          [H^+] = \\sqrt{\\frac{K_w}{K_b} \\times [\\text{Kation}]}
                    """.trimIndent(),
                    visualCards = listOf(
                        VisualCard(
                            title = "Formula Henderson-Hasselbalch",
                            formulaOrSymbol = "pH = pK_a + \\log \\frac{[\\text{Basa Konjugasi}]}{[\\text{Asam Lemah}]}",
                            explanation = "Kapasitas buffer maksimal tercapai saat konsentrasi asam lemah sama dengan basa konjugasinya (pH = pKa)."
                        )
                    ),
                    exitTest = listOf(
                        Question(
                            id = "ex_11_1",
                            topicId = "topic_11_acidbase",
                            difficulty = QuestionDifficulty.LEVEL_4_ANALYSIS,
                            question = "Sebanyak 100 mL larutan CH_3COOH 0.1 M (K_a = 10^{-5}) dicampurkan dengan 50 mL larutan NaOH 0.1 M. Nilai pH larutan yang dihasilkan adalah...",
                            options = listOf("5", "9", "5 - log 2", "5 + log 2"),
                            correctAnswerIndex = 0,
                            explanation = "• Mol CH_3COOH = 100 mL x 0.1 M = 10 mmol\n• Mol NaOH = 50 mL x 0.1 M = 5 mmol\n• Reaksi: CH_3COOH + NaOH \\rightarrow CH_3COONa + H_2O\n• Sisa CH_3COOH = 10 - 5 = 5 mmol (Asam Lemah)\n• Terbentuk CH_3COONa = 5 mmol (Basa Konjugasi)\n• Terbentuk sistem BUFFER ASAM: [H^+] = K_a \\times \\frac{\\text{mol asam}}{\\text{mol garam}} = 10^{-5} \\times \\frac{5}{5} = 10^{-5} \\text{ M}\n• pH = -\\log(10^{-5}) = 5.",
                            conceptTested = "Perhitungan pH Larutan Penyangga (Buffer Asam)"
                        )
                    )
                )
            )
        ),

        // LEVEL 12
        Topic(
            id = "topic_12_redox",
            levelNumber = 12,
            levelName = "LEVEL 12 — REDOX & ELECTROCHEMISTRY",
            title = "Reaksi Redoks & Sel Elektrokimia",
            description = "Bilangan oksidasi, penyetaraan reaksi redoks, sel Volta (Galvani), potensial sel standar (E^\\circ_{sel}), deret Volta, dan sel elektrolisis (Hukum Faraday).",
            learningObjectives = listOf(
                "Menyetarakan reaksi redoks menggunakan metode setengah reaksi dan metode perubahan biloks.",
                "Menghitung potensial sel standar E^\\circ_{sel} = E^\\circ_{katoda} - E^\\circ_{anoda} dan meramalkan kespontanan reaksi.",
                "Menerapkan Hukum Faraday I & II untuk menghitung massa endapan logam pada proses elektrolisis."
            ),
            lessons = listOf(
                Lesson(
                    id = "lesson_12_1",
                    topicId = "topic_12_redox",
                    title = "Sel Volta & Potensial Reduksi Standar",
                    subtitle = "Mengubah energi kimia reaksi redoks spontan menjadi energi listrik",
                    durationMinutes = 20,
                    learningObjectives = listOf(
                        "Menentukan anoda (oksidasi) dan katoda (reduksi) dalam sel volta.",
                        "Menuliskan notasi sel volta standar."
                    ),
                    conceptExplanation = """
                        Sel Elektrokimia terdiri dari dua jenis:
                        1. Sel Volta (Galvani): Reaksi redoks SPONTAN menghasilkan arus listrik (E^\\circ_{sel} > 0).
                        2. Sel Elektrolisis: Menggunakan arus listrik dari luar untuk mendorong reaksi redoks TIDAK SPONTAN (E^\\circ_{sel} < 0).
                        
                        Prinsip Sel Volta (Jembatan Keledai: KRAO):
                        • Katoda: Tempat terjadinya REDUKSI (Kutub Positif) \\rightarrow Elektroda dengan E^\\circ lebih besar (lebih positif).
                        • Anoda: Tempat terjadinya OKSIDASI (Kutub Negatif) \\rightarrow Elektroda dengan E^\\circ lebih kecil (lebih negatif).
                        
                        Rumus Potensial Sel Standar (E^\\circ_{sel}):
                        E^\\circ_{sel} = E^\\circ_{\\text{Katoda (Reduksi)}} - E^\\circ_{\\text{Anoda (Oksidasi)}}
                        
                        Notasi Sel Volta:
                        \\text{Anoda } | \\text{ Ion Anoda } || \\text{ Ion Katoda } | \\text{ Katoda}
                        Contoh: Zn | Zn^{2+} || Cu^{2+} | Cu
                    """.trimIndent(),
                    visualCards = listOf(
                        VisualCard(
                            title = "Notasi Sel Volta",
                            formulaOrSymbol = "\\text{Anoda (Oksidasi)} \\parallel \\text{Katoda (Reduksi)}",
                            explanation = "Aliran elektron mengalir secara spontan melalui kawat dari Anoda (-) menuju Katoda (+)."
                        )
                    ),
                    exitTest = listOf(
                        Question(
                            id = "ex_12_1",
                            topicId = "topic_12_redox",
                            difficulty = QuestionDifficulty.LEVEL_4_ANALYSIS,
                            question = "Diketahui potensial reduksi standar:\nZn^{2+} + 2e^- \\rightarrow Zn \\quad E^\\circ = -0.76\\text{ V}\nAg^+ + e^- \\rightarrow Ag \\quad E^\\circ = +0.80\\text{ V}\nNilai potensial sel standar (E^\\circ_{sel}) untuk sel volta yang disusun dari kedua elektroda tersebut adalah...",
                            options = listOf("+1.56 V", "+0.04 V", "-1.56 V", "+2.36 V"),
                            correctAnswerIndex = 0,
                            explanation = "• Ag memiliki E^\\circ lebih positif (+0.80 V) -> bertindak sebagai KATODA (Reduksi)\n• Zn memiliki E^\\circ lebih negatif (-0.76 V) -> bertindak sebagai ANODA (Oksidasi)\n• E^\\circ_{sel} = E^\\circ_{\\text{Katoda}} - E^\\circ_{\\text{Anoda}} = (+0.80) - (-0.76) = +1.56\\text{ Volt}. Karena bernilai positif, reaksi berlangsung spontan.",
                            conceptTested = "Perhitungan Potensial Sel Standar Sel Volta"
                        )
                    )
                )
            )
        ),

        // LEVEL 13
        Topic(
            id = "topic_13_organic",
            levelNumber = 13,
            levelName = "LEVEL 13 — ORGANIC CHEMISTRY",
            title = "Kimia Karbon & Gugus Fungsi Organik",
            description = "Hidrokarbon (alkana, alkena, alkuna), isomer, gugus fungsi (alkohol, eter, aldehid, keton, asam karboksilat, ester), reaksi senyawa karbon, dan polimer.",
            learningObjectives = listOf(
                "Mengidentifikasi gugus fungsi senyawa organik dan tata nama IUPAC/Trivial.",
                "Menganalisis keisomeran fungsi (alkohol-eter, aldehid-keton, asam karboksilat-ester).",
                "Membedakan reaksi adisi, substitusi, eliminasi, esterifikasi, dan hidrolisis."
            ),
            lessons = listOf(
                Lesson(
                    id = "lesson_13_1",
                    topicId = "topic_13_organic",
                    title = "Gugus Fungsi Senyawa Karbon & Isomer",
                    subtitle = "Struktur, identifikasi laboratorium, dan pasangan isomer fungsi",
                    durationMinutes = 20,
                    learningObjectives = listOf(
                        "Menguasai rumus umum dan pasangan isomer gugus fungsi senyawa organik."
                    ),
                    conceptExplanation = """
                        Pasangan Isomer Gugus Fungsi (Rumus Molekul Sama, Gugus Fungsi Berbeda):
                        
                        1. Rumus Umum C_n H_{2n+2} O:
                        • Alkohol (Alkanol): Memiliki gugus -OH. Bereaksi dengan logam Na menghasilkan gas H_2.
                        • Eter (Alkoksi Alkana): Memiliki gugus -O-. Tidak bereaksi dengan logam Na.
                        
                        2. Rumus Umum C_n H_{2n} O:
                        • Aldehida (Alkanal): Memiliki gugus -CHO. Memberikan uji positif terhadap pereaksi Tollens (cermin perak) dan Fehling (endapan merah bata Cu_2O).
                        • Keton (Alkanon): Memiliki gugus -CO-. Tidak dapat dioksidasi oleh pereaksi Tollens maupun Fehling.
                        
                        3. Rumus Umum C_n H_{2n} O_2:
                        • Asam Karboksilat (Asam Alkanoat): Memiliki gugus -COOH. Bersifat asam lemah, memerahkan lakmus biru.
                        • Ester (Alkil Alkanoat): Memiliki gugus -COO-. Beraroma harum khas buah-buahan, dihasilkan dari reaksi esterifikasi asam karboksilat + alkohol.
                    """.trimIndent(),
                    visualCards = listOf(
                        VisualCard(
                            title = "Ringkasan Uji Laboratorium Organik",
                            formulaOrSymbol = "\\text{Tollens (+) } \\rightarrow \\text{Aldehida} | \\text{Logam Na (+) } \\rightarrow \\text{Alkohol}",
                            explanation = "Uji kimia kualitatif membedakan senyawa yang memiliki rumus molekul identik."
                        )
                    ),
                    exitTest = listOf(
                        Question(
                            id = "ex_13_1",
                            topicId = "topic_13_organic",
                            difficulty = QuestionDifficulty.LEVEL_4_ANALYSIS,
                            question = "Suatu senyawa organik dengan rumus molekul C_3H_6O tidak bereaksi dengan pereaksi Fehling dan tidak membentuk cermin perak dengan pereaksi Tollens. Senyawa tersebut adalah...",
                            options = listOf("Propanal", "Propanon (Aseton)", "1-Propanol", "Asam propanoat"),
                            correctAnswerIndex = 1,
                            explanation = "Rumus molekul C_3H_6O memiliki rumus umum C_n H_{2n} O yang dapat berupa aldehida (propanal) atau keton (propanon). Karena tidak dapat mereduksi pereaksi Tollens dan Fehling, maka senyawa tersebut adalah golongan keton, yaitu propanon (aseton).",
                            conceptTested = "Identifikasi Gugus Fungsi Aldehid vs Keton"
                        )
                    )
                )
            )
        )
    )
}
