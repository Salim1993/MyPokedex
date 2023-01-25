package com.salim.mypokedex.profile

import com.salim.mypokedex.utilities.SharedPreferencesWrapper
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor(
    private val preferences: SharedPreferencesWrapper,
    moshi: Moshi
): ProfileUseCase {

    private val profileAdapter = moshi.adapter(Profile::class.java)

    private val _profileFlow = MutableStateFlow<Profile?>(null)

    private val _profileUpdatedEventFlow = MutableSharedFlow<Boolean>()

    override fun getProfileFlow(): StateFlow<Profile?> {
        return _profileFlow.asStateFlow()
    }

    override fun getProfileUpdatedEventFlow(): SharedFlow<Boolean> {
        return _profileUpdatedEventFlow.asSharedFlow()
    }

    override suspend fun getProfileFromCache() {
        val profileString = preferences.getString(PROFILE_EXTRA)
        if (profileString.isNotEmpty()) {
            val profile = profileAdapter.fromJson(profileString)
            _profileFlow.emit(profile)
        }
    }

    override suspend fun saveProfileToCache(profile: Profile) {
        val profileString = profileAdapter.toJson(profile)
        preferences.saveString(PROFILE_EXTRA, profileString)
        _profileUpdatedEventFlow.emit(true)
        _profileFlow.emit(profile)
    }

    companion object {
        private const val PROFILE_EXTRA = "PROFILE"
    }
}