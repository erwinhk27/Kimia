package com.example.data.chemistry

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: String, // "user" or "ai"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSuggestedPrompt: Boolean = false,
    val topicTag: String? = null,
    val socraticMode: String? = null, // "socratic", "clue", "analogy", "calculation", "diagram", "misconception"
    val suggestedFollowUps: List<String> = emptyList()
)

object AiTutorEngine {

    val promptSuggestions = listOf(
        "Bimbing saya menentukan pereaksi pembatas pada reaksi kimia",
        "Jelaskan prinsip pergeseran kesetimbangan Le Chatelier dengan analogi",
        "Bagaimana cara membedakan larutan buffer dan hidrolisis garam?",
        "Gambarkan diagram energi reaksi eksoterm vs endoterm",
        "Beri clue untuk menyetarakan reaksi redoks metode ion elektron",
        "Mengapa jari-jari atom dalam satu periode dari kiri ke kanan makin kecil?",
        "Jelaskan isomer geometri cis-trans pada alkena dengan contoh",
        "Apa perbedaan sel Volta dan sel Elektrolisis?"
    )

    private const val SYSTEM_PROMPT = """
Anda adalah **CHEMIA AI**, Tutor Cerdas Kimia SMA dan Spesialis Persiapan Tes Kemampuan Akademik (TKA/UTBK Kimia).

PRINSIP BIMBINGAN SOCRATIC & PEDAGOGIS:
1. **Socratic Scaffolding**: Jika siswa menanyakan soal atau meminta solusi perhitungan, JANGAN langsung memberikan jawaban akhir. Berikan pertanyaan pemandu, identifikasi besaran yang diketahui, atau berikan petunjuk (clue) langkah ke-1 agar siswa berpikir mandiri terlebih dahulu.
2. **Analogi & Dunia Riil**: Gunakan analogi kehidupan nyata yang intuitif (misal: analogi resep sandwich untuk pereaksi pembatas, analogi kursi bus untuk kaidah Hund, analogi jungkat-jungkit untuk kesetimbangan Le Chatelier, sistem buffer darah untuk kestabilan pH).
3. **Langkah Perhitungan Sistematis**: Tuliskan rumus yang digunakan, konversi satuan dengan jelas, dan sertakan trik "Solusi Smart" yang mempersingkat waktu pengerjaan soal ujian.
4. **Diagram & Representasi Visual**: Gunakan representasi ASCII/diagram terstruktur (seperti kotak orbital [↑↓], diagram tingkat energi ΔH, rumus titik Lewis, atau skema sel elektrokimia) bila relevan.
5. **Peringatan Miskonsepsi**: Selalu sertakan peringatan kesalahan umum yang sering menjebak siswa pada topik terkait.
6. **Bahasa**: Bahasa Indonesia yang ramah, akademis, memotivasi, dan mudah dipahami oleh siswa SMA.
"""

    suspend fun getTutorResponse(
        history: List<ChatMessage>,
        userPrompt: String,
        activeMode: String = "socratic"
    ): Pair<String, List<String>> = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig::class.java.getField("GEMINI_API_KEY").get(null) as? String ?: ""
        } catch (_: Exception) {
            ""
        }

        val modePromptModifier = when (activeMode) {
            "clue" -> "\n[Instruksi Khusus Mode: Berikan HANYA clue/petunjuk langkah berikutnya dan 1 pertanyaan pemandu, jangan berikan jawaban akhir]"
            "analogy" -> "\n[Instruksi Khusus Mode: Fokuskan penjelasan menggunakan analogi kehidupan nyata dan contoh konkret]"
            "calculation" -> "\n[Instruksi Khusus Mode: Berikan penurunan rumus dan langkah matematis detail berserta trik Solusi Smart]"
            "diagram" -> "\n[Instruksi Khusus Mode: Sertakan diagram visual ASCII/skema terstruktur yang jelas]"
            "misconception" -> "\n[Instruksi Khusus Mode: Sorot kesalahan paling umum/miskonsepsi siswa pada topik ini]"
            else -> "\n[Instruksi Khusus Mode: Pendekatan Socratic dialogis berimbang dengan pertanyaan pemandu]"
        }

        if (apiKey.isNotBlank()) {
            try {
                val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey
                val url = URL(endpoint)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json; utf-8")
                conn.doOutput = true
                conn.connectTimeout = 10000
                conn.readTimeout = 20000

                val contentsArray = JSONArray()

                // Append recent conversation context
                val recentHistory = history.takeLast(6)
                for (msg in recentHistory) {
                    val role = if (msg.sender == "user") "user" else "model"
                    val itemObj = JSONObject()
                    itemObj.put("role", role)
                    val parts = JSONArray()
                    val partObj = JSONObject()
                    partObj.put("text", msg.text)
                    parts.put(partObj)
                    itemObj.put("parts", parts)
                    contentsArray.put(itemObj)
                }

                // Add current prompt with mode modifier
                val currentObj = JSONObject()
                currentObj.put("role", "user")
                val curParts = JSONArray()
                val curPart = JSONObject()
                curPart.put("text", userPrompt + modePromptModifier)
                curParts.put(curPart)
                currentObj.put("parts", curParts)
                contentsArray.put(currentObj)

                val body = JSONObject()
                val systemInstruction = JSONObject()
                val sysParts = JSONArray()
                val sysPart = JSONObject()
                sysPart.put("text", SYSTEM_PROMPT)
                sysParts.put(sysPart)
                systemInstruction.put("parts", sysParts)
                body.put("systemInstruction", systemInstruction)
                body.put("contents", contentsArray)

                OutputStreamWriter(conn.outputStream).use { writer ->
                    writer.write(body.toString())
                    writer.flush()
                }

                val responseCode = conn.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(conn.inputStream))
                    val responseStr = reader.use { it.readText() }
                    val jsonResp = JSONObject(responseStr)
                    val candidates = jsonResp.getJSONArray("candidates")
                    if (candidates.length() > 0) {
                        val content = candidates.getJSONObject(0).getJSONObject("content")
                        val parts = content.getJSONArray("parts")
                        if (parts.length() > 0) {
                            val text = parts.getJSONObject(0).getString("text")
                            val followUps = generateDynamicFollowUps(userPrompt, activeMode)
                            return@withContext Pair(text, followUps)
                        }
                    }
                }
            } catch (_: Exception) {
                // Fallback to intelligent local reasoning engine
            }
        }

        // Offline Fallback Intelligent Socratic Chemistry Tutor
        val localResponse = generateLocalSocraticResponse(userPrompt, activeMode)
        val followUps = generateDynamicFollowUps(userPrompt, activeMode)
        return@withContext Pair(localResponse, followUps)
    }

    private fun generateDynamicFollowUps(prompt: String, mode: String): List<String> {
        val q = prompt.lowercase()
        return when {
            q.contains("pembatas") || q.contains("mol") -> listOf(
                "💡 Beri petunjuk langkah berikutnya",
                "🔬 Jelaskan dengan analogi sehari-hari",
                "📐 Tunjukkan langkah perhitungan matematis",
                "🎯 Berikan contoh soal serupa"
            )
            q.contains("kesetimbangan") || q.contains("chatelier") -> listOf(
                "📊 Tampilkan diagram pergeseran arah reaksi",
                "🔬 Apa contoh penerapannya di industri?",
                "⚠️ Mengapa katalis tidak menggeser kesetimbangan?",
                "💡 Trik cepat menyelesaikan soal Kc/Kp"
            )
            q.contains("buffer") || q.contains("penyangga") || q.contains("hidrolisis") -> listOf(
                "🧪 Bagaimana cara membedakan buffer dan hidrolisis?",
                "📐 Contoh perhitungan pH campuran asam-basa",
                "🔬 Bagaimana sistem buffer menjaga pH darah manusia?",
                "⚠️ Peringatan kesalahan hitung valensi garam"
            )
            q.contains("redoks") || q.contains("volta") || q.contains("elektrolisis") -> listOf(
                "⚡ Apa jembatan keledai menghafal KRAO & KaP-AnN?",
                "📐 Tunjukkan langkah penyetaraan ion-elektron",
                "📊 Skema aliran elektron sel Volta",
                "💡 Hukum Faraday dan perhitungannya"
            )
            q.contains("atom") || q.contains("kuantum") || q.contains("orbital") -> listOf(
                "📊 Tampilkan diagram pengisian orbital",
                "🔬 Apa analogi mudah aturan Hund dan Pauli?",
                "📐 Cara menentukan 4 bilangan kuantum (n, l, m, s)",
                "⚠️ Anomali kestabilan subkulit d (Cr dan Cu)"
            )
            q.contains("organik") || q.contains("alkena") || q.contains("alkohol") || q.contains("isomer") -> listOf(
                "🔬 Apa beda isomer rangka, posisi, dan fungsi?",
                "📐 Aturan Markovnikov pada reaksi adisi",
                "📊 Skema reaksi oksidasi alkohol primer/sekunder",
                "💡 Uji pembeda alkohol vs eter (reaksi Na & PCl5)"
            )
            else -> listOf(
                "💡 Berikan clue/petunjuk berpikir",
                "🔬 Jelaskan dengan analogi sederhana",
                "📐 Bedah rumus & perhitungan",
                "📊 Tampilkan skema/diagram konsep"
            )
        }
    }

    private fun generateLocalSocraticResponse(prompt: String, mode: String): String {
        val q = prompt.lowercase()

        // 1. PEREAKSI PEMBATAS (LIMITING REAGENT)
        if (q.contains("pembatas") || q.contains("limiting") || q.contains("habis bereaksi")) {
            return when (mode) {
                "clue" -> """
                    💡 **Petunjuk Socratic: Menentukan Pereaksi Pembatas**
                    
                    **Pertanyaan Pemandu untuk Anda:**
                    1. Apakah persamaan reaksi kimia yang Anda miliki sudah **setara** koefisiennya?
                    2. Sudahkah Anda mengubah semua massa zat yang diketahui menjadi satuan **mol (n = gram / Mr)**?
                    
                    **Clue Kunci:**
                    Bagi masing-masing mol reaktan dengan koefisiennya:
                    Rasio = n / Koefisien
                    
                    👉 *Coba periksa: Manakah reaktan yang menghasilkan nilai rasio paling kecil? Zat itulah pereaksi pembatasnya.*
                """.trimIndent()

                "analogy" -> """
                    🔬 **Analogi Dunia Riil: Pabrik Roti Lapis (Sandwich)**
                    
                    Bayangkan resep 1 Sandwich:
                    2 Roti + 1 Keju -> 1 Sandwich
                    
                    Jika di dapur tersedia:
                    • **10 potong Roti** (cukup untuk 5 sandwich)
                    • **3 lembar Keju** (hanya cukup untuk 3 sandwich)
                    
                    Berapa sandwich yang dapat dibuat? **Hanya 3 sandwich!**
                    Keju habis total terlebih dahulu, sementara roti tersisa (10 - 6 = 4 potong).
                    
                    Dalam kimia:
                    • **Keju = Pereaksi Pembatas** (menentukan jumlah produk maksimal).
                    • **Roti = Pereaksi Berlebih** (tersisa setelah reaksi usai).
                """.trimIndent()

                "diagram" -> """
                    📊 **Diagram Alur Analisis Pereaksi Pembatas**
                    
                    ```
                    [Massa / Vol Reaktan A]      [Massa / Vol Reaktan B]
                               |                              |
                         (bagi Mr / 22.4)               (bagi Mr / 22.4)
                               v                              v
                          [ Mol A (n_A) ]                [ Mol B (n_B) ]
                               |                              |
                        (bagi koefisien a)             (bagi koefisien b)
                               v                              v
                          [ Rasio A: n_A/a ]  vs   [ Rasio B: n_B/b ]
                                       \              /
                                  Bandingkan Nilai Terkecil
                                             |
                                             v
                            ⭐ PEREAKSI PEMBATAS (Habis = 0)
                                             |
                      Gunakan mol zat ini untuk hitung semua Produk & Sisa!
                    ```
                """.trimIndent()

                else -> """
                    🔬 **Konsep & Strategi Pereaksi Pembatas (Limiting Reagent)**
                    
                    **1. Konsep Inti:**
                    Dalam suatu reaksi kimia, reaktan sering kali tidak dicampurkan dalam rasio mol yang tepat sesuai persamaan setara. Reaktan yang memiliki perbandingan mol terhadap koefisien paling kecil akan **habis bereaksi terlebih dahulu** dan membatasi pembentukan produk.
                    
                    **2. Langkah Berpikir Sistematis (Metode Uji Rasio):**
                    1. Pastikan persamaan reaksi sudah setara: aA + bB -> cC + dD
                    2. Hitung mol mula-mula masing-masing pereaksi (n_A dan n_B).
                    3. Hitung rasio: Rasio A = n_A / a dan Rasio B = n_B / b.
                    4. **Pereaksi Pembatas = Reaktan dengan rasio terkecil.**
                    5. Mol zat yang bereaksi = koefisien x rasio pereaksi pembatas.
                    
                    **3. Trik Solusi Smart TKA:**
                    n_produk = koefisien produk x min(n_A / a, n_B / b)
                    
                    ⚠️ **Peringatan Miskonsepsi:**
                    Jangan memilih zat yang memiliki **massa paling sedikit** atau **mol paling sedikit** sebagai pembatas sebelum membaginya dengan koefisien reaksi masing-masing!
                """.trimIndent()
            }
        }

        // 2. KESETIMBANGAN & ASAS LE CHATELIER
        if (q.contains("kesetimbangan") || q.contains("le chatelier") || q.contains("kc") || q.contains("kp")) {
            return when (mode) {
                "analogy" -> """
                    🔬 **Analogi: Balon Udara & Jungkat-Jungkit Keseimbangan**
                    
                    Bayangkan dua wadah air yang dihubungkan pipa di bagian bawah.
                    • Jika Anda **menambah air di wadah kiri**, air akan mengalir ke **kanan** untuk meratakan permukaan.
                    • Jika Anda **mengambil air dari wadah kiri**, air dari kanan akan mengalir ke **kiri** untuk mengisi kekosongan.
                    
                    Inilah asas **Henri Louis Le Chatelier (1884)**:
                    *"Jika pada sistem kesetimbangan diberikan gangguan (aksi), sistem akan mengadakan penyesuaian (reaksi) untuk meminimalkan pengaruh gangguan tersebut."*
                """.trimIndent()

                "diagram" -> """
                    📊 **Matriks Aksi-Reaksi Asas Le Chatelier**
                    
                    ```
                    AKSI DIBERIKAN            | ARAH PERGESERAN SISTEM
                    ========================================================================
                    [Zat] Ditambah            | Bergeser MENJAUHI zat tersebut
                    [Zat] Dikurangi           | Bergeser MENDEKATI zat tersebut
                    Tekanan Dinaikkan         | Bergeser ke sisi dengan JUMLAH KOEFISIEN GAS KECIL
                    Volume Diperbesar         | Bergeser ke sisi dengan JUMLAH KOEFISIEN GAS BESAR
                    Suhu Dinaikkan (T ↑)      | Bergeser ke arah ENDOTERM (ΔH > 0) [K berubah!]
                    Suhu Diturunkan (T ↓)     | Bergeser ke arah EKSOTERM (ΔH < 0) [K berubah!]
                    Katalis Ditambahkan       | TIDAK MENGGESER! Hanya mempercepat laju reaksi.
                    ```
                """.trimIndent()

                "clue" -> """
                    💡 **Petunjuk Socratic: Menganalisis Pergeseran Reaksi**
                    
                    Perhatikan persamaan reaksi berikut:
                    N2(g) + 3H2(g) <=> 2NH3(g)    ΔH = -92 kJ
                    
                    **Pertanyaan Pemandu:**
                    1. Hitung total koefisien gas di ruas kiri (pereaksi): 1 + 3 = 4.
                    2. Hitung total koefisien gas di ruas kanan (hasil reaksi): 2.
                    3. Apakah reaksi pembentukan amonia bersifat eksoterm atau endoterm? (Perhatikan tanda minus pada ΔH).
                    
                    👉 *Jika suhu diturunkan dan tekanan dinaikkan, ke arah manakah kesetimbangan bergeser untuk memaksimalkan produksi amonia?*
                """.trimIndent()

                else -> """
                    🔄 **Prinsip Kesetimbangan Kimia & Nilai Tetapan Kc / Kp**
                    
                    **1. Konsep Dinamis:**
                    Kesetimbangan kimia bersifat **dinamis mikroskopis**, di mana laju reaksi maju (v_1) sama besar dengan laju reaksi balik (v_2), sehingga konsentrasi zat-zat dalam sistem tetap konstan.
                    
                    **2. Rumus Tetapan Kesetimbangan:**
                    Untuk reaksi: aA(g) + bB(s) <=> cC(g) + dD(aq)
                    Kc = [C]^c / [A]^a    (Hanya fase gas dan larutan aq yang masuk perhitungan)
                    Kp = (P_C)^c / (P_A)^a    (Hanya fase gas)
                    
                    **3. Hubungan Kp dan Kc:**
                    Kp = Kc x (R x T)^Δn, di mana Δn = koef gas kanan - koef gas kiri
                    
                    ⚠️ **Peringatan Miskonsepsi TKA:**
                    Hanya perubahan **SUHU** yang dapat mengubah nilai tetapan kesetimbangan K. Perubahan konsentrasi, tekanan, volume, atau penambahan katalis **TIDAK MENGUBAH** harga K!
                """.trimIndent()
            }
        }

        // 3. ASAM BASA, BUFFER & HIDROLISIS
        if (q.contains("buffer") || q.contains("penyangga") || q.contains("hidrolisis") || q.contains("ph") || q.contains("asam") || q.contains("basa")) {
            return when (mode) {
                "clue" -> """
                    💡 **Pohon Keputusan Identifikasi Campuran Asam-Basa**
                    
                    Jika Asam Lemah (HA) direaksikan dengan Basa Kuat (BOH):
                    1. Hitung mmol HA = V_A x M_A dan mmol BOH = V_B x M_B.
                    2. Buat tabel MRS (Mula-mula, Reaksi, Sisa):
                    
                    • **Kondisi 1: Sisa Asam Lemah (HA) dan terbentuk Garam (A-)?**
                      -> **LARUTAN PENYANGGA (BUFFER ASAM)**
                      [H+] = Ka x (mmol Asam Sisa / mmol Garam Konjugasi)
                    
                    • **Kondisi 2: Asam Lemah dan Basa Kuat SAMA-SAMA HABIS?**
                      -> **HIDROLISIS GARAM (Sifat Basa)**
                      [OH-] = akar( (Kw / Ka) x [Garam] )
                    
                    • **Kondisi 3: Sisa Basa Kuat?**
                      -> pH ditentukan langsung oleh konsentrasi sisa ion [OH-] basa kuat.
                """.trimIndent()

                "analogy" -> """
                    🔬 **Analogi & Dunia Riil: Penjaga pH Darah Manusia**
                    
                    pH darah manusia normal harus berada pada rentang sangat sempit: **7,35 - 7,45**.
                    Jika pH darah turun di bawah 7,0 atau naik di atas 7,8, tubuh bisa mengalami asidosis/alkalosis fatal.
                    
                    **Bagaimana tubuh menjaga kestabilan ini?**
                    Melalui sistem **Buffer Bikarbonat**:
                    CO2(g) + H2O(l) <=> H2CO3(aq) <=> H+(aq) + HCO3-(aq)
                    
                    • Saat kita berolahraga berat dan asam laktat masuk ke darah (H+ naik), ion HCO3- langsung mengikatnya menjadi H2CO3 lalu dikeluarkan lewat paru-paru sebagai CO2.
                    • Inilah keajaiban larutan penyangga: **menahan perubahan pH drastis** meski ditambah asam/basa!
                """.trimIndent()

                "diagram" -> """
                    📊 **Klasifikasi Hidrolisis Garam Berdasarkan Asal Pembentuknya**
                    
                    ```
                    JENIS GARAM                 | KOMPONEN TERHIDROLISIS | SIFAT LARUTAN | RUMUS ION [H+] / [OH-]
                    ===============================================================================================
                    Asam Kuat + Basa Kuat       | Tidak terhidrolisis    | Netral (pH=7) | [H+] = 10^-7 M
                    (Contoh: NaCl, K2SO4)       |                        |               |
                    -----------------------------------------------------------------------------------------------
                    Asam Lemah + Basa Kuat      | Anion terhidrolisis    | Basa (pH > 7) | [OH-] = √( (Kw/Ka) × [Garam] )
                    (Contoh: CH3COONa, KCN)     | (Hidrolisis sebagian)  |               |
                    -----------------------------------------------------------------------------------------------
                    Asam Kuat + Basa Lemah      | Kation terhidrolisis   | Asam (pH < 7) | [H+] = √( (Kw/Kb) × [Garam] )
                    (Contoh: NH4Cl, (NH4)2SO4)  | (Hidrolisis sebagian)  |               |
                    -----------------------------------------------------------------------------------------------
                    Asam Lemah + Basa Lemah     | Kation & Anion         | Tergantung Ka | [H+] = √( (Kw × Ka) / Kb )
                    (Contoh: CH3COONH4)         | (Hidrolisis total)     | vs Kb         |
                    ```
                """.trimIndent()

                else -> """
                    🧪 **Kupas Tuntas: Teori Asam Basa, Larutan Penyangga & Hidrolisis**
                    
                    **1. Tiga Teori Asam Basa Utama:**
                    • **Arrhenius**: Asam melepaskan H+, Basa melepaskan OH- dalam pelarut air.
                    • **Bronsted-Lowry**: Asam = Donor Proton (H+), Basa = Akseptor Proton (H+). Membentuk pasangan asam-basa konjugasi.
                    • **Lewis**: Asam = Akseptor pasangan elektron bebas (PEB), Basa = Donor PEB.
                    
                    **2. Larutan Penyangga (Buffer):**
                    Campuran asam lemah + garam konjugasinya, atau basa lemah + garam konjugasinya.
                    pH buffer asam = pKa - log( mmol Asam / (mmol Garam x valensi) )
                    
                    **3. Hidrolisis Garam:**
                    Reaksi ion garam dari asam/basa lemah dengan molekul air menghasilkan ion H+ atau OH-.
                    
                    ⚠️ **Peringatan Miskonsepsi TKA:**
                    Perhatikan **valensi anion/kation** pada garam polivalen!
                    Contoh: (NH4)2SO4 -> 2 NH4+ + SO4^2-.
                    Konsentrasi kation terhidrolisis adalah 2 x [Garam]!
                """.trimIndent()
            }
        }

        // 4. STRUKTUR ATOM & BILANGAN KUANTUM
        if (q.contains("atom") || q.contains("kuantum") || q.contains("konfigurasi") || q.contains("aufbau") || q.contains("hund") || q.contains("pauli") || q.contains("orbital")) {
            return when (mode) {
                "analogy" -> """
                    🔬 **Analogi: 4 Bilangan Kuantum = Alamat Kamar Hotel Elektron**
                    
                    Bayangkan sebuah Hotel Elektron:
                    1. **n (Utama): Nomor Lantai Hotel** (n = 1, 2, 3, 4...). Makin tinggi lantai, energi makin besar.
                    2. **l (Azimut): Tipe Kamar** (l = 0 -> subkulit s, 1 -> p, 2 -> d, 3 -> f). Menentukan bentuk geometri orbital.
                    3. **m (Magnetik): Nomor Ranjang Spesifik** (m = -l sampai +l). Orientasi spasial orbital.
                    4. **s (Spin): Arah Posisi Tidur Elektron** (s = +1/2 [spin atas ↑] atau -1/2 [spin bawah ↓]).
                    
                    **Larangan Pauli:** Tidak boleh ada dua elektron di satu atom yang memiliki keempat bilangan kuantum (n, l, m, s) persis sama!
                """.trimIndent()

                "diagram" -> """
                    📊 **Diagram Tingkat Energi Asas Aufbau & Pengisian Orbital**
                    
                    ```
                    Tingkat Energi: 1s -> 2s -> 2p -> 3s -> 3p -> 4s -> 3d -> 4p -> 5s -> 4d -> 5p -> 6s ...
                    
                    Contoh Pengisian Orbital 3d^8 pada atom Ni (Z = 28):
                    Konfigurasi: [Ar] 4s² 3d⁸
                    
                    Subkulit 4s:  [ ↑↓ ] (m = 0)
                    
                    Subkulit 3d:  [ ↑↓ ] [ ↑↓ ] [ ↑↓ ] [ ↑  ] [ ↑  ]
                    Nilai m:       -2     -1      0     +1     +2
                    
                    Jumlah elektron tidak berpasangan = 2 elektron (Paramagnetik)
                    Bilangan kuantum elektron terakhir (ke-28):
                    n = 3,  l = 2,  m = 0,  s = -1/2
                    ```
                """.trimIndent()

                "clue" -> """
                    💡 **Petunjuk Socratic: Menentukan Bilangan Kuantum Elektron Terakhir**
                    
                    Misalkan Anda ingin menentukan bilangan kuantum elektron terakhir dari 26Fe:
                    1. Tulis konfigurasi elektronnya: 26Fe = 1s2 2s2 2p6 3s2 3p6 4s2 3d6.
                    2. Fokus pada subkulit terakhir: **3d^6**.
                    
                    **Pertanyaan Pemandu:**
                    • Berapakah nilai kulit utama n? (Angka di depan subkulit: 3)
                    • Untuk subkulit d, berapakah nilai l? (s=0, p=1, d=2, f=3)
                    • Jika 6 elektron diisikan ke dalam 5 orbital (m = -2, -1, 0, +1, +2) satu per satu terlebih dahulu lalu berpasangan, di kotak m manakah elektron ke-6 jatuh dan bagaimana arah spinnya?
                """.trimIndent()

                else -> """
                    ⚛️ **Struktur Atom, Konfigurasi Elektron & 4 Bilangan Kuantum**
                    
                    **1. Tiga Aturan Pengisian Elektron:**
                    • **Asas Aufbau**: Pengisian dimulai dari tingkat energi terendah ke tingkat energi yang lebih tinggi (1s -> 2s -> 2p -> 3s -> 3p -> 4s -> 3d...).
                    • **Kaidah Hund**: Orbital dengan tingkat energi setara diisi satu per satu dengan spin sejajar (↑) sebelum berpasangan (↑↓).
                    • **Larangan Pauli**: Tidak ada 2 elektron dalam 1 atom yang boleh memiliki 4 bilangan kuantum identik.
                    
                    **2. Anomali Kestabilan Subkulit d Penuh / Setengah Penuh:**
                    • 24Cr: Konfigurasi adalah [Ar] 4s1 3d5 (setengah penuh lebih stabil), bukan [Ar] 4s2 3d4.
                    • 29Cu: Konfigurasi adalah [Ar] 4s1 3d10 (penuh lebih stabil), bukan [Ar] 4s2 3d9.
                    
                    **3. Konfigurasi Ion Positif:**
                    Saat atom membentuk kation (X^n+), elektron dilepaskan terlebih dahulu dari **kulit terluar (n terbesar)**, bukan dari subkulit berenergi tertinggi.
                    Contoh: Fe: [Ar] 4s2 3d6 -> Fe^2+: [Ar] 3d6 (elektron 4s yang lepas!).
                """.trimIndent()
            }
        }

        // 5. REDOKS & ELEKTROKIMIA (SEL VOLTA & ELEKTROLISIS)
        if (q.contains("redoks") || q.contains("volta") || q.contains("elektrolisis") || q.contains("faraday") || q.contains("korosi") || q.contains("biloks")) {
            return when (mode) {
                "analogy" -> """
                    ⚡ **Jembatan Keledai Super Elektrokimia**
                    
                    1. **KRAO (Berlaku untuk SEMUA Sel Listrik):**
                       • **K**atoda = Tempat reaksi **R**eduksi (menerima elektron).
                       • **A**noda = Tempat reaksi **O**ksidasi (melepas elektron).
                    
                    2. **KaP-AnN (Hanya untuk Sel Volta / Baterai):**
                       • **Ka**toda = Kutub **P**ositif (+)
                       • **An**oda = Kutub **N**egatif (-)
                    
                    3. **KNAP (Untuk Sel Elektrolisis):**
                       • **K**atoda = **N**egatif (-)
                       • **A**noda = **P**ositif (+)
                """.trimIndent()

                "diagram" -> """
                    📊 **Perbandingan Sel Volta vs Sel Elektrolisis**
                    
                    ```
                    PARAMETER          | SEL VOLTA / GALVANI               | SEL ELEKTROLISIS
                    ============================================================================================
                    Prinsip Energi     | Energi Kimia -> Energi Listrik     | Energi Listrik -> Energi Kimia
                    Spontanitas        | Spontan (E°sel > 0)               | Tidak spontan (butuh sumber arus DC)
                    Kutub Katoda       | Positif (+) (Tempat Reduksi)       | Negatif (-) (Tempat Reduksi)
                    Kutub Anoda        | Negatif (-) (Tempat Oksidasi)      | Positif (+) (Tempat Oksidasi)
                    Arah Aliran e-     | Dari Anoda (-) menuju Katoda (+)   | Dari Anoda (+) menuju Katoda (-)
                    Aplikasi Nyata     | Aki, Baterai Kering, Sel Bahan Bakar| Penyepuhan logam (plating), Pemurnian Cu
                    ```
                """.trimIndent()

                "clue" -> """
                    💡 **Petunjuk Socratic: Menghitung Potensial Sel Standar (E°sel)**
                    
                    Diberikan data:
                    • Zn^2+ + 2e- -> Zn    E° = -0.76 V
                    • Cu^2+ + 2e- -> Cu    E° = +0.34 V
                    
                    **Pertanyaan Pemandu:**
                    1. Unsur dengan nilai E° lebih positif selalu bertindak sebagai **Katoda (Reduksi)**. Siapakah yang lebih positif, Cu atau Zn?
                    2. Gunakan rumus standar:
                       E°sel = E°Katoda - E°Anoda
                    
                    👉 *Berapakah nilai E°sel reaksi tersebut? Apakah reaksinya berlangsung spontan?*
                """.trimIndent()

                else -> """
                    ⚡ **Redoks, Sel Volta, Sel Elektrolisis & Hukum Faraday**
                    
                    **1. Penyetaraan Reaksi Redoks (Metode Ion Elektron):**
                    1. Bagi menjadi setengah reaksi reduksi dan oksidasi.
                    2. Setarakan atom utama selain O dan H.
                    3. Setarakan atom O: Tambah H2O di ruas kekurangan O (suasana asam) atau di ruas kelebihan O (suasana basa).
                    4. Setarakan atom H: Tambah H+ (suasana asam) atau OH- (suasana basa).
                    5. Setarakan muatan dengan menambahkan elektron (e-).
                    
                    **2. Deret Volta:**
                    Li - K - Ba - Ca - Na - Mg - Al - Mn - (H2O) - Zn - Cr - Fe - Cd - Co - Ni - Sn - Pb - (H) - Sb - Bi - Cu - Hg - Ag - Pt - Au
                    • Makin ke kiri: E° makin negatif, makin reaktif, reduktor makin kuat, mudah teroksidasi.
                    • Makin ke kanan: E° makin positif, oksidator makin kuat, mudah tereduksi.
                    
                    **3. Hukum Faraday Elektrolisis:**
                    w = (e x i x t) / 96500, di mana e = Ar / valensi
                """.trimIndent()
            }
        }

        // 6. KIMIA ORGANIK & HIDROKARBON
        if (q.contains("karbon") || q.contains("organik") || q.contains("alkana") || q.contains("alkena") || q.contains("alkuna") || q.contains("alkohol") || q.contains("eter") || q.contains("ester") || q.contains("benzena")) {
            return when (mode) {
                "diagram" -> """
                    📊 **Peta Gugus Fungsi Senyawa Karbon & Isomer Fungsinya**
                    
                    ```
                    RUMUS UMUM     | PASANGAN GUGUS FUNGSI           | STRUKTUR
                    ========================================================================
                    C_n H_{2n+2} O | Alkohol (Alkanol)               | R - OH
                                   | Eter (Alkoksi Alkana)           | R - O - R'
                    ------------------------------------------------------------------------
                    C_n H_{2n} O   | Aldehida (Alkanal)              | R - CHO
                                   | Keton (Alkanon)                 | R - CO - R'
                    ------------------------------------------------------------------------
                    C_n H_{2n} O_2 | Asam Karboksilat (Asam Alkanoat)| R - COOH
                                   | Ester (Alkil Alkanoat)          | R - COO - R'
                    ```
                """.trimIndent()

                "analogy" -> """
                    🔬 **Aturan Markovnikov: "Yang Kaya Makin Kaya"**
                    
                    Pada reaksi adisi alkena asimetris dengan asam halida (HX atau H2O):
                    Contoh: CH3-CH=CH2 + HCl -> ...
                    
                    • Atom C_1 mengikat 2 atom H (lebih kaya H).
                    • Atom C_2 hanya mengikat 1 atom H (kurang H).
                    
                    Aturan Markovnikov menyatakan:
                    **Atom H dari asam halida akan masuk ke atom C berikatan rangkap yang sudah memiliki atom H lebih banyak.**
                    Sehingga produk utamanya adalah **2-kloropropana** (CH3-CH(Cl)-CH3), bukan 1-kloropropana!
                """.trimIndent()

                else -> """
                    🧪 **Karakteristik Senyawa Karbon & Tata Nama IUPAC**
                    
                    **1. Keunikan Atom Karbon:**
                    • Memiliki 4 elektron valensi (membentuk 4 ikatan kovalen stabil).
                    • Jari-jari relatif kecil sehingga ikatan kovalennya sangat kuat.
                    • Mampu membentuk rantai karbon lurus, bercabang, cincin alisiklik, maupun cincin aromatis benzena.
                    
                    **2. Reaksi Khas Kimia Organik:**
                    • **Substitusi**: Penggantian atom/gugus (khas pada alkana dan benzena).
                    • **Adisi**: Pemutusan ikatan rangkap menjadi tunggal (khas pada alkena dan alkuna).
                    • **Eliminasi**: Pembentukan ikatan rangkap dengan melepas molekul kecil (kebalikan adisi).
                    • **Esterifikasi**: Asam Karboksilat + Alkohol -> Ester + Air (katalis H2SO4 pekat).
                """.trimIndent()
            }
        }

        // DEFAULT PEDAGOGICAL SOCRATIC OVERVIEW
        return """
            Halo! Saya **CHEMIA AI**, tutor belajar kimia interaktif Anda.
            
            Saya siap membimbing Anda menguasai materi Kimia SMA & TKA UTBK dengan pendekatan:
            1. **Socratic Scaffolding**: Bimbingan bertahap dengan pertanyaan pemandu.
            2. **Analogi Intuitif**: Konsep rumit dijelaskan dengan perumpamaan dunia nyata.
            3. **Langkah Perhitungan**: Penurunan rumus sistematis dan trik *Solusi Smart*.
            4. **Diagram & Skema**: Visualisasi representasi mikroskopis dan alur reaksi.
            5. **Misconception Buster**: Mencegah jebakan soal ujian yang sering terjadi.
            
            Silakan ajukan pertanyaan atau pilih salah satu tombol saran di atas!
        """.trimIndent()
    }
}
