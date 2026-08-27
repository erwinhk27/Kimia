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
    val topicTag: String? = null
)

object AiTutorEngine {

    val promptSuggestions = listOf(
        "Jelaskan konsep mol dengan analogi kehidupan sehari-hari",
        "Bagaimana cara menentukan pereaksi pembatas dengan cepat?",
        "Jelaskan prinsip pergeseran kesetimbangan Le Chatelier",
        "Apa perbedaan mendasar ikatan ion dan ikatan kovalen?",
        "Bagaimana cara menghitung pH larutan penyangga (buffer)?",
        "Berikan trik menghafal deret Volta dan potensial sel"
    )

    private const val SYSTEM_PROMPT = """
Anda adalah CHEMIA AI — Tutor Cerdas Kimia SMA & Spesialis Persiapan TKA (Tes Kemampuan Akademik).
Gaya bimbingan Anda:
1. Socratic & Pedagogical: Jangan langsung memberikan jawaban akhir secara mentah pada soal latihan, bimbinglah dengan pertanyaan pemandu (scaffolding), analogi intuitif, dan langkah berpikir sistematis.
2. Jelas & Terstruktur: Gunakan format [Konsep Inti] → [Langkah Berpikir / Analisis] → [Contoh Perhitungan] → [Peringatan Kesalahan Umum (Misconception Alert)].
3. Bahasa Indonesia yang ramah, akademis, memotivasi, dan mudah dipahami oleh siswa SMA.
4. Dukung notasi kimia dan matematika yang rapi.
"""

    suspend fun getTutorResponse(history: List<ChatMessage>, userPrompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig::class.java.getField("GEMINI_API_KEY").get(null) as? String ?: ""
        } catch (_: Exception) {
            ""
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
                val recentHistory = history.takeLast(4)
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

                // Add current prompt
                val currentObj = JSONObject()
                currentObj.put("role", "user")
                val curParts = JSONArray()
                val curPart = JSONObject()
                curPart.put("text", userPrompt)
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
                            return@withContext parts.getJSONObject(0).getString("text")
                        }
                    }
                }
            } catch (_: Exception) {
                // Fallback to intelligent local reasoning engine
            }
        }

        // Offline / Fallback Intelligent Chemistry Expert
        return@withContext generateLocalTutorResponse(userPrompt)
    }

    private fun generateLocalTutorResponse(prompt: String): String {
        val query = prompt.lowercase()

        return when {
            query.contains("mol") || query.contains("avogadro") -> """
                🔬 **Konsep Mol — Jembatan Kuantitas Kimia**
                
                **1. Analogi Sederhana:**
                Bayangkan 1 lusin telur = 12 butir telur. Di dunia mikroskopis atom yang teramat kecil, kita menggunakan satuan **1 mol zat = 6.022 × 10²³ partikel** (Bilangan Avogadro, N_A).
                
                **2. Jembatan Konversi Stoikiometri:**
                • **Massa ke Mol:** n = massa (gram) / Mr (g/mol)
                • **Mol ke Partikel:** N = n × 6.022 × 10²³
                • **Mol ke Volume Gas (STP: 0°C, 1 atm):** V = n × 22.4 Liter
                • **Mol ke Molaritas Larutan:** M = n / V (Liter)
                
                ⚠️ **Misconception Alert:**
                Volume 22.4 L/mol **HANYA** berlaku untuk zat berwujud **GAS** pada kondisi standar (0°C, 1 atm). Jangan gunakan 22.4 L untuk cairan murni (seperti air murni) atau padatan!
                
                💡 *Apakah ada contoh soal perhitungan mol tertentu yang ingin kita bedah bersama?*
            """.trimIndent()

            query.contains("pembatas") || query.contains("limiting") -> """
                ⚖️ **Cara Cepat Menentukan Pereaksi Pembatas**
                
                **1. Analogi Membuat Roti Lapis (Sandwich):**
                Untuk membuat 1 sandwich butuh **2 potong roti + 1 lembar keju**.
                Jika Anda punya 10 potong roti dan 3 lembar keju, berapa sandwich yang bisa dibuat?
                Jawabannya hanya 3 sandwich, dan keju habis pertama kali. Maka keju adalah **pereaksi pembatas**!
                
                **2. Metode Uji Rasio (Ratio Test):**
                1. Tulis persamaan reaksi yang sudah setara (aA + bB → cC).
                2. Hitung mol awal masing-masing reaktan (n_A dan n_B).
                3. Bagi mol dengan koefisien reaksinya:
                   Rasio A = n_A / a,    Rasio B = n_B / b
                4. **Rasio terkecil adalah PEREAKSI PEMBATAS (habis bereaksi = 0 mol).**
                5. Gunakan mol pereaksi pembatas untuk menghitung produk yang terbentuk.
                
                💡 *Coba kirimkan reaksi dan mol zat yang sedang Anda kerjakan, saya akan bimbing tahap per tahap!*
            """.trimIndent()

            query.contains("kesetimbangan") || query.contains("le chatelier") -> """
                🔄 **Prinsip Pergeseran Kesetimbangan Le Chatelier**
                
                **Inti Asas:** Sistem kesetimbangan selalu melawan setiap aksi perubahan luar untuk mengembalikan kestabilan.
                
                **Matriks Aturan Praktis TKA:**
                1. **Konsentrasi:**
                   • Zat ditambah → Sistem bergeser **menjauhi** zat tersebut.
                   • Zat diambil/dikurangi → Sistem bergeser **mendekati** zat tersebut.
                2. **Tekanan & Volume (Fase Gas):**
                   • Tekanan dinaikkan (Volume diperkecil) → Bergeser ke sisi dengan **jumlah koefisien gas lebih kecil**.
                3. **Suhu (Satu-satunya yang mengubah nilai K):**
                   • Suhu dinaikkan → Bergeser ke arah reaksi **Endoterm** (ΔH > 0).
                   • Suhu diturunkan → Bergeser ke arah reaksi **Eksoterm** (ΔH < 0).
                4. **Katalis:**
                   • Hanya mempercepat laju ke kanan dan ke kiri sama besar. **TIDAK MENGGESER** kesetimbangan sama sekali!
            """.trimIndent()

            query.contains("buffer") || query.contains("penyangga") || query.contains("ph") -> """
                🧪 **Larutan Penyangga (Buffer) & Rahasia Kestabilan pH**
                
                **1. Mengapa pH Larutan Penyangga Stabil?**
                Buffer mengandung pasangan asam lemah dan basa konjugasinya (atau basa lemah dan asam konjugasinya) dalam jumlah signifikan.
                
                **2. Formula Henderson-Hasselbalch:**
                • **Buffer Asam (HA / A⁻):**
                  [H⁺] = Ka × (mol Asam Lemah / mol Basa Konjugasi)
                  pH = pKa + log([Basa Konjugasi] / [Asam Lemah])
                • **Buffer Basa (B / BH⁺):**
                  [OH⁻] = Kb × (mol Basa Lemah / mol Asam Konjugasi)
                  pOH = pKb + log([Asam Konjugasi] / [Basa Lemah])
                
                💡 *Contoh nyata: Sistem buffer bikarbonat (H₂CO₃ / HCO₃⁻) di dalam darah manusia menjaga pH darah tetap konstan di sekitar 7.4!*
            """.trimIndent()

            query.contains("volta") || query.contains("redoks") || query.contains("katoda") -> """
                ⚡ **Sel Volta & Jembatan Keledai Elektrokimia**
                
                **Jembatan Ingatan Praktis:**
                • **KRAO:** **K**atoda **R**eduksi, **A**noda **O**ksidasi.
                • **KaP-AnN:** **Ka**toda **P**ositif (+), **An**oda **N**egatif (-) pada Sel Volta.
                
                **Potensial Sel Standar (E°sel):**
                E°sel = E°Katoda(Reduksi) - E°Anoda(Oksidasi)
                • Reaksi berlangsung **spontan** jika E°sel > 0.
                • Unsur dengan E° lebih positif selalu bertindak sebagai katoda (mengalami reduksi).
            """.trimIndent()

            else -> """
                Halo! Saya **Chemia AI**, asisten belajarmu untuk menguasai konsep dan penalaran Kimia SMA hingga siap menghadapi TKA.
                
                Anda bisa menanyakan:
                1. Penjelasan konsep (Struktur Atom, Ikatan, Stoikiometri, Termokimia, Kesetimbangan, Asam-Basa, Redoks, Organik).
                2. Bimbingan langkah-demi-langkah mengerjakan soal yang sulit.
                3. Trik mengingat dan analogi visual sains.
                4. Analisis mengapa jawaban suatu soal bisa keliru.
                
                Silakan ketik pertanyaan Anda atau pilih salah satu tombol saran di atas!
            """.trimIndent()
        }
    }
}
