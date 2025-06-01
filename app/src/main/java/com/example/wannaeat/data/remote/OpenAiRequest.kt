package com.example.wannaeat.data.remote

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class OpenAiRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Double,
    val tools: List<Tool>,
    @SerializedName("tool_choice")
    val toolChoice: ToolChoice
)

data class Message(val role: String, val content: String)

data class Tool(
    val type: String = "function",
    val function: FunctionSchema
)

data class ToolChoice(
    val type: String = "function",
    val function: FunctionName
)

data class FunctionName(val name: String)

data class FunctionSchema(
    val name: String,
    val description: String,
    val parameters: JsonObject
)

