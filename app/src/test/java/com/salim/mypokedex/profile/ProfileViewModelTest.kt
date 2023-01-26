package com.salim.mypokedex.profile

import com.salim.mypokedex.CustomMainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule

import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    @get:Rule
    val customMainDispatcherRule = CustomMainDispatcherRule()

    @RelaxedMockK
    lateinit var profileUseCase: ProfileUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getProfileFlow should have profile when initialized if use case return a profile`() = runTest {
        // arrange
        every { profileUseCase.getProfileFlow() } returns MutableStateFlow(Profile("salim", "", ""))

        // act
        val unitToTest = ProfileViewModel(profileUseCase)

        // assert
        val actualProfile = unitToTest.profileFlow.value
        val expectedProfile = Profile("salim", "", "")
        assertEquals(actualProfile, expectedProfile)
    }

    @Test
    fun `getProfileFlow should have null in flow when use case does not find a profile`() = runTest {
        // arrange
        every { profileUseCase.getProfileFlow() } returns MutableStateFlow(null)

        // act
        val unitToTest = ProfileViewModel(profileUseCase)

        // assert
        val actualProfile = unitToTest.profileFlow.value
        assertNull(actualProfile)
    }

    @Test
    fun `submitNewAvatar updates new profile and returns it in the flow`() = runTest {
        // arrange
        val fakeProfileUseCase = FakeProfileUseCase()
        val expectedImage = "image"

        // act
        val unitToTest = ProfileViewModel(fakeProfileUseCase)
        val job = unitToTest.submitNewAvatar(expectedImage)
        job.join()

        // assert
        val expectedProfile = Profile("", "", "image")
        val actualProfile = unitToTest.profileFlow.value
        assertEquals(expectedProfile, actualProfile)
    }

    @Test
    fun `submitNewAvatar updates old profile with new avatar and returns it in the flow`() = runTest {
        // arrange
        val oldProfile = Profile("salim", "lmao@gmail.com", "")
        val fakeProfileUseCase = FakeProfileUseCase()
        fakeProfileUseCase.setProfileToReceiveFromCache(oldProfile)
        val expectedImage = "image"

        // act
        val unitToTest = ProfileViewModel(fakeProfileUseCase)
        val job = unitToTest.submitNewAvatar(expectedImage)
        job.join()

        // assert
        val expectedProfile = Profile("salim", "lmao@gmail.com", "image")
        val actualProfile = unitToTest.profileFlow.value
        assertEquals(expectedProfile, actualProfile)
    }

    @Test
    fun `submitNewProfileInfo updates new profile and returns it in the flow`() = runTest {
        // arrange
        val fakeProfileUseCase = FakeProfileUseCase()
        val expectedName = "salim"

        // act
        val unitToTest = ProfileViewModel(fakeProfileUseCase)
        val job = unitToTest.submitNewProfileInfo(expectedName, "")
        job.join()

        // assert
        val expectedProfile = Profile("salim", "", "")
        val actualProfile = unitToTest.profileFlow.value
        assertEquals(expectedProfile, actualProfile)
    }

    @Test
    fun `submitNewProfileInfo updates old profile with new name and email and returns it in the flow`() = runTest {
        // arrange
        val oldProfile = Profile("george", "lmao@gmail.com", "image")
        val fakeProfileUseCase = FakeProfileUseCase()
        fakeProfileUseCase.setProfileToReceiveFromCache(oldProfile)
        val expectedName = "salim"
        val expectedEmail = "rofl@yahoo.ca"

        // act
        val unitToTest = ProfileViewModel(fakeProfileUseCase)
        val job = unitToTest.submitNewProfileInfo(expectedName, expectedEmail)
        job.join()

        // assert
        val expectedProfile = Profile("salim", "rofl@yahoo.ca", "image")
        val actualProfile = unitToTest.profileFlow.value
        assertEquals(expectedProfile, actualProfile)
    }

    @Test
    fun `submitNewProfileInfo updates old profile with empty name and email and returns it in the flow with new fields empty`() = runTest {
        // arrange
        val oldProfile = Profile("george", "lmao@gmail.com", "image")
        val fakeProfileUseCase = FakeProfileUseCase()
        fakeProfileUseCase.setProfileToReceiveFromCache(oldProfile)
        val expectedName = ""
        val expectedEmail = ""

        // act
        val unitToTest = ProfileViewModel(fakeProfileUseCase)
        val job = unitToTest.submitNewProfileInfo(expectedName, expectedEmail)
        job.join()

        // assert
        val expectedProfile = Profile("", "", "image")
        val actualProfile = unitToTest.profileFlow.value
        assertEquals(expectedProfile, actualProfile)
    }

    @Test
    fun `triggerShowAvatarOptionsDialogEvent should show true in event flow`() = runTest {
        // act
        val unitToTest = ProfileViewModel(profileUseCase)
        unitToTest.triggerShowAvatarOptionsDialogEvent()

        // assert
        val actualValue = unitToTest.showAvatarDialogEvent.first()
        assertTrue(actualValue)
    }
}
