package com.example.wannaeat.data.remote

import com.google.gson.annotations.SerializedName

data class OpenAiResponse(
    @SerializedName("choices")
    val choices: List<Choice>
)

data class Choice(
    @SerializedName("message")
    val message: MessageWithToolCall?
)

data class MessageWithToolCall(
    val tool_calls: List<ToolCall>?
)

data class ToolCall(
    val function: ToolFunction
)

data class ToolFunction(
    val name: String,
    val arguments: String
)
