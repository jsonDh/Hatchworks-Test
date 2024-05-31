package com.json.hatchworks_test.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.exception.ApolloException
import com.json.hatchworks_test.CharacterQuery
import com.json.hatchworks_test.domain.repository.CharacterRepository
import com.json.hatchworks_test.utils.NetworkHelper
import com.json.hatchworks_test.utils.ResourceProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import timber.log.Timber
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class CharacterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var repository: CharacterRepository = mockk()
    private var networkHelper : NetworkHelper = mockk()
    private var resourceProvider: ResourceProvider = mockk()

    private lateinit var viewModel: CharacterViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        viewModel = CharacterViewModel(repository, networkHelper, resourceProvider)
        Dispatchers.setMain(testDispatcher)
        Timber.plant(TestTree())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        Timber.uprootAll()
    }

    @Test
    fun `getCharacterDetails success`() = testScope.runTest {
        // Mock data
        val characterId = "1"
        val characterData = createCharacterData("Character 1", "human")
        val response = createApolloResponse(characterData)

        // Mock that the internet is working
        every { networkHelper.isInternetAvailable() } returns true

        // Mock the repository response
        coEvery { repository.getCharacterDetails(characterId) } returns response

        // Observe the ViewModel's state
        viewModel.characterState.test {
            viewModel.getCharacterDetails(characterId)

            assertEquals(CharacterState.Initial, awaitItem())
            assertEquals(CharacterState.Loading, awaitItem())
            assertEquals(CharacterState.Success(characterData), awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getCharacterDetails error`() = testScope.runTest {
        // Mock an ApolloException
        val characterId = "1"
        val errorMessage = "Network error"
        coEvery { repository.getCharacterDetails(characterId) } throws ApolloException(errorMessage)

        // Mock that the internet is working
        every { networkHelper.isInternetAvailable() } returns true

        // Mock the error message
        every { resourceProvider.getString(any()) } returns errorMessage

        // Call the request method
        viewModel.getCharacterDetails(characterId)

        // Observe the ViewModel's state
        viewModel.characterState.test {
            assertEquals(CharacterState.Loading, awaitItem())
            assertEquals(CharacterState.Error(errorMessage), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getCharacterDetails no internet connection`() = testScope.runTest {
        // Mock that the internet is not available
        every { networkHelper.isInternetAvailable() } returns false

        // Mock an internet issue message
        val internetIssue = "There is no internet connection"
        every { resourceProvider.getString(any()) } returns internetIssue

        // Observe the ViewModel's state
        viewModel.characterState.test {
            viewModel.getCharacterDetails("1")

            assertEquals(CharacterState.Initial, awaitItem())
            assertEquals(CharacterState.Loading, awaitItem())
            assertEquals(
                CharacterState.Error(internetIssue),
                awaitItem()
            )

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getCharacterDetails unexpected repository exception`() = testScope.runTest {
        // Mock that the internet is working
        every { networkHelper.isInternetAvailable() } returns true

        // Mock an Unexpected exception message
        val unexpected = "Unexpected exception"
        every { resourceProvider.getString(any()) } returns unexpected

        // Mock the repository response to throw an unexpected exception
        coEvery { repository.getCharacterDetails(any()) } throws RuntimeException(unexpected)

        // Observe the ViewModel's state
        viewModel.characterState.test {
            viewModel.getCharacterDetails("1")

            assertEquals(CharacterState.Initial, awaitItem())
            assertEquals(CharacterState.Loading, awaitItem())
            assertEquals(
                CharacterState.Error(unexpected),
                awaitItem()
            )

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun clearData() = testScope.runTest {
        // Set the state to an error state
        viewModel.clearData()

        // Observe the ViewModel's state
        viewModel.characterState.test {
            assertEquals(CharacterState.Initial, awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    private fun createCharacterData(name: String, status: String): CharacterQuery.Data {
        val character = CharacterQuery.Character(
            id = "1",
            name = name,
            status = status,
            created = "24-05-2024",
            episode = emptyList(),
            gender = "neutral",
            image = "",
            location = CharacterQuery.Location("The Universe"),
            origin = CharacterQuery.Origin("The Big Bang"),
            species = "human",
            type = "?"
        )
        return CharacterQuery.Data(character)
    }

    private fun createApolloResponse(data: CharacterQuery.Data): ApolloResponse<CharacterQuery.Data> {
        return ApolloResponse.Builder(
            data = data,
            operation = mockk(),
            requestUuid = UUID.randomUUID()
        ).build()
    }

    class TestTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            // No-op
        }
    }
}