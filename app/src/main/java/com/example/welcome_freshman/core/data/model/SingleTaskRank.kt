package com.example.welcome_freshman.core.data.model

import kotlinx.serialization.Serializable

/**
 *@date 2024/4/13 20:35
 *@author GFCoder
 */

@Serializable
data class SingleTaskRank(
    val taskId: Int,
    val taskName: String,
    val completionCount: Int,
//    val rank:Int,
    val userCompletions: List<User>
)

/*@Serializable
data class SingleTaskRankWithUser(
    val userId: Int,
    val account: String,
    val rank: Int,
    val completionTime: Long,
)*/


