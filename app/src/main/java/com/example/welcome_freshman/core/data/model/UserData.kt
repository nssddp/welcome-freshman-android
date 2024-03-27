package com.example.welcome_freshman.core.data.model

/**
 *@date 2024/3/27 22:08
 *@author GFCoder
 */
data class UserData(
    /*    val bookmarkedNewsResources: Set<String>,
        val viewedNewsResources: Set<String>,
        val followedTopics: Set<String>,*/
    val userId: Int,
    val validState: Boolean,
    val darkThemeConfig: DarkThemeConfig,
    /*   val useDynamicColor: Boolean,
       val shouldHideOnboarding: Boolean,*/
)