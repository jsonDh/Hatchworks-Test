package com.json.hatchworks_test.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.exception.ApolloException
import com.json.hatchworks_test.CharacterQuery
import com.json.hatchworks_test.domain.repository.CharacterRepository
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
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import timber.log.Timber
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CharacterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: CharacterRepository
    private lateinit var viewModel: CharacterViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = CharacterViewModel(repository)
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

        // Mock the repository response
        `when`(repository.getCharacterDetails(characterId)).thenReturn(response)

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
        `when`(repository.getCharacterDetails(characterId)).thenThrow(ApolloException(errorMessage))

        // Call the request method
        viewModel.getCharacterDetails(characterId)

        // Observe the ViewModel's state
        viewModel.characterState.test {
            assertEquals(CharacterState.Loading, awaitItem())
            assertEquals(CharacterState.Error(errorMessage), awaitItem())
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
            operation = createMockOperation(),
            requestUuid = UUID.randomUUID()
        ).build()
    }

    private fun createMockOperation(): Operation<CharacterQuery.Data> {
        return mock(Operation::class.java) as Operation<CharacterQuery.Data>
    }

    class TestTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            // No-op
        }
    }
}