package com.salim.mypokedex.profile

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ProfileUseCase {

    fun getProfileFlow(): StateFlow<Profile?>
    fun getProfileUpdatedEventFlow(): SharedFlow<Boolean>
    suspend fun getProfileFromCache()
    suspend fun saveProfileToCache(profile: Profile)
}
