package com.salim.mypokedex.profile

import kotlinx.coroutines.flow.*

class FakeProfileUseCase: ProfileUseCase {

    private var profileToReceiveFromCache = Profile(
        name = "",
        email = "",
        avatarImageLocation = ""
    )

    private val _profileFlow = MutableStateFlow<Profile?>(null)

    private val _profileUpdatedEventFlow = MutableSharedFlow<Boolean>()

    override fun getProfileFlow(): StateFlow<Profile?> {
        return _profileFlow.asStateFlow()
    }

    override fun getProfileUpdatedEventFlow(): SharedFlow<Boolean> {
        return _profileUpdatedEventFlow.asSharedFlow()
    }

    override suspend fun getProfileFromCache() {
        _profileFlow.emit(profileToReceiveFromCache)
    }

    override suspend fun saveProfileToCache(profile: Profile) {
        _profileFlow.emit(profile)
        _profileUpdatedEventFlow.emit(true)
    }

    fun setProfileToReceiveFromCache(profile: Profile) {
        profileToReceiveFromCache = profile
    }
}