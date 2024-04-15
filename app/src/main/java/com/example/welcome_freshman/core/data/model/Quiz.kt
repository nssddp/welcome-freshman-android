package com.example.welcome_freshman.core.data.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 *@date 2024/4/8 20:21
 *@author GFCoder
 */

@Serializable
data class QuizDto(
    @SerialName("questionEntity")
    val quiz: Quiz? = null,

    @SerialName("answersEntityList")
    val options: List<Option>? = null,
)

@Serializable
data class Quiz(
    @SerialName("questionId")
    val quizId: Int? = null,

    @SerialName("category")
    val quizName: String? = null,

    @SerialName("questionText")
    val quizDescription: String? = null,

    @SerialName("image")
    val quizPic: String? = null,

//    val options: List<Option>? = null,
    @SerialName("answerId")
    val correctOptionId: Int? = null,
)

@Serializable
data class Option(

    @SerialName("answerId")
    val optionId: Int? = null,
    @SerialName("questionId")
    val quizId: Int? = null,
    @SerialName("answerText")
    val optionText: String? = null,

    @SerialName("isCorrectAnswer")
    val isCorrect: Int? = null,

    var isUnselected: MutableState<Boolean> = mutableStateOf(true),
)