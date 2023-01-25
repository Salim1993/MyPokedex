package com.salim.mypokedex.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase
): ViewModel() {

    val profileFlow = profileUseCase.getProfileFlow()
    val profileUpdatedEventFlow = profileUseCase.getProfileUpdatedEventFlow()

    private val _showAvatarDialogEvent = MutableSharedFlow<Boolean>()
    val showAvatarDialogEvent = _showAvatarDialogEvent.asSharedFlow()

    init {
        getProfileFromCache()
    }

    fun submitNewAvatar(imageLocation: String) = viewModelScope.launch {
        val newProfile = profileFlow.value?.copy(avatarImageLocation = imageLocation)
            ?: Profile("", "", imageLocation)

        profileUseCase.saveProfileToCache(newProfile)
    }

    fun submitNewProfileInfo(name: String, email: String) = viewModelScope.launch {
        val newProfile = profileFlow.value?.copy(name = name, email = email)
            ?: Profile(name, email, "")

        profileUseCase.saveProfileToCache(newProfile)
    }

    private fun getProfileFromCache() = viewModelScope.launch {
        profileUseCase.getProfileFromCache()
    }

    fun triggerShowAvatarOptionsDialogEvent() = viewModelScope.launch {
        _showAvatarDialogEvent.emit(true)
    }
}