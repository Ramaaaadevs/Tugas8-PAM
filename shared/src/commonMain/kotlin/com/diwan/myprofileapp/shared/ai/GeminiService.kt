package com.diwan.myprofileapp.shared.ai

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class GeminiService(
    private val client: HttpClient,
    private val apiKey: String
) {
    private val baseUrl = "https://generativelanguage.googleapis.com/v1beta"
    private val model = "gemini-2.0-flash"

    private val conversationHistory = mutableListOf<GeminiContent>()

    private val systemPrompt = GeminiContent(
        role = "user",
        parts = listOf(
            GeminiPart(
                text = """
                    Kamu adalah asisten pintar dalam aplikasi Notes.
                    Tugasmu membantu pengguna mengelola catatan, menjawab pertanyaan,
                    dan memberikan saran yang relevan.
                    Selalu jawab dalam Bahasa Indonesia kecuali user bertanya dalam bahasa lain.
                    Jawaban singkat, jelas, dan helpful.
                """.trimIndent()
            )
        )
    )

    suspend fun chat(userMessage: String): Result<String> = runCatching {
        conversationHistory.add(
            GeminiContent(role = "user", parts = listOf(GeminiPart(userMessage)))
        )

        val request = GeminiRequest(
            contents = conversationHistory.toList(),
            systemInstruction = systemPrompt,
            generationConfig = GenerationConfig(
                temperature = 0.7,
                maxOutputTokens = 1000
            )
        )

        val response: GeminiResponse = client.post(
            "$baseUrl/models/$model:generateContent"
        ) {
            contentType(ContentType.Application.Json)
            parameter("key", apiKey)
            setBody(request)
        }.body()

        if (response.error != null) {
            throw Exception(response.error.message)
        }

        val reply = response.candidates
            ?.firstOrNull()
            ?.content
            ?.parts
            ?.firstOrNull()
            ?.text
            ?: throw Exception("Tidak ada respons dari AI")

        conversationHistory.add(
            GeminiContent(role = "model", parts = listOf(GeminiPart(reply)))
        )

        reply
    }

    fun clearHistory() {
        conversationHistory.clear()
    }
}
