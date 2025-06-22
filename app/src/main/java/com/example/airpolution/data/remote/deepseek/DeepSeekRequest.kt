package com.example.airpolution.data.remote.deepseek

data class DeepSeekRequest(
    val model: String = "deepseek-chat",
    val messages: List<Message>,
    val temperature: Double = 0.7,
)

data class Message(
    val role: String, // "system" or "user"
    val content: String,
)

data class DeepSeekResponse(
    val choices: List<Choice>,
)

data class Choice(
    val message: Message,
)