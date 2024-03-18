package com.example.welcome_freshman.core.data.repository

import com.example.welcome_freshman.core.data.network.RetrofitWfNetwork
import javax.inject.Inject

/**
 *@date 2024/3/18 18:00
 *@author GFCoder
 */
interface AdRepository {
    suspend fun getAd(userId: Int): String?

}

class MainAdRepository @Inject constructor(
    private val network: RetrofitWfNetwork,

    ) : AdRepository {
    override suspend fun getAd(userId: Int): String? = network.getAd(userId)
}