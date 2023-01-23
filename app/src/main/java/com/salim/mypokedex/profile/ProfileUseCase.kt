package com.salim.mypokedex.profile

import com.salim.mypokedex.utilities.SharedPreferencesWrapper
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ProfileUseCase @Inject constructor(
    private val preferences: SharedPreferencesWrapper,
    moshi: Moshi
) {

    private val profileAdapter = moshi.adapter(Profile::class.java)

    private val _profileFlow = MutableStateFlow<Profile?>(null)
    val profileFlow = _profileFlow.asStateFlow()

    private val _profileUpdatedEventFlow = MutableSharedFlow<Boolean>()
    val profileUpdatedEventFlow = _profileUpdatedEventFlow.asSharedFlow()

    suspend fun getProfileFromCache() {
        val profileString = preferences.getString(PROFILE_EXTRA)
        if (profileString.isNotEmpty()) {
            val profile = profileAdapter.fromJson(profileString)
            _profileFlow.emit(profile)
        }
    }

    suspend fun saveProfileToCache(profile: Profile) {
        val profileString = profileAdapter.toJson(profile)
        preferences.saveString(PROFILE_EXTRA, profileString)
        _profileFlow.emit(profile)
        _profileUpdatedEventFlow.emit(true)
    }

    companion object {
        private const val PROFILE_EXTRA = "PROFILE"
    }
}