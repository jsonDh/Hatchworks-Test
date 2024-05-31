package com.json.hatchworks_test.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.exception.ApolloException
import com.json.hatchworks_test.CharactersListQuery
import com.json.hatchworks_test.domain.repository.CharacterRepository
import com.json.hatchworks_test.utils.NetworkHelper
import com.json.hatchworks_test.utils.ResourceProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.*
import org.junit.runners.JUnit4
import timber.log.Timber
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class CharacterListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var repository: CharacterRepository = mockk()
    private var networkHelper: NetworkHelper = mockk()
    private var resourceProvider: ResourceProvider = mockk()

    private lateinit var viewModel: CharacterListViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        viewModel = CharacterListViewModel(repository, networkHelper, resourceProvider)
        Dispatchers.setMain(testDispatcher)
        Timber.plant(TestTree())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        Timber.uprootAll()
    }

    @Test
    fun `getCharacters success`() = testScope.runTest {
        // Mock data
        val characters = createMockCharacters()
        val charactersListQuery = CharactersListQuery.Characters(characters)
        val response = createApolloResponse(CharactersListQuery.Data(charactersListQuery))

        // Mock that the internet is working
        every { networkHelper.isInternetAvailable() } returns true

        // Mock the repository response
        coEvery { repository.getCharacterList() } returns response

        // Observe the ViewModel's state
        viewModel.charactersListState.test {
            viewModel.getCharacters()

            assertEquals(CharactersListState.Initial, awaitItem())
            assertEquals(CharactersListState.Loading, awaitItem())
            assertEquals(CharactersListState.Success(characters), awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getCharacters empty list`() = testScope.runTest {
        // Mock data
        val characters = emptyList<CharactersListQuery.Result>()
        val charactersListQuery = CharactersListQuery.Characters(characters)
        val response = createApolloResponse(CharactersListQuery.Data(charactersListQuery))

        // Mock that the internet is working
        every { networkHelper.isInternetAvailable() } returns true

        // Mock the repository response
        coEvery { repository.getCharacterList() } returns response

        // Observe the ViewModel's state
        viewModel.charactersListState.test {
            viewModel.getCharacters()

            assertEquals(CharactersListState.Initial, awaitItem())
            assertEquals(CharactersListState.Loading, awaitItem())
            assertEquals(CharactersListState.Success(characters), awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getCharacters partial data`() = testScope.runTest {
        // Mock data with null values
        val characters = listOf(
            CharactersListQuery.Result("1", "Character 1", "human", null),
            CharactersListQuery.Result("2", null, "alien", "")
        )
        val charactersListQuery = CharactersListQuery.Characters(characters)
        val response = createApolloResponse(CharactersListQuery.Data(charactersListQuery))

        // Mock that the internet is working
        every { networkHelper.isInternetAvailable() } returns true

        // Mock the repository response
        coEvery { repository.getCharacterList() } returns response

        // Observe the ViewModel's state
        viewModel.charactersListState.test {
            viewModel.getCharacters()

            assertEquals(CharactersListState.Initial, awaitItem())
            assertEquals(CharactersListState.Loading, awaitItem())
            assertEquals(CharactersListState.Success(characters), awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getCharacters no internet connection`() = testScope.runTest {
        // Mock that the internet is not available
        every { networkHelper.isInternetAvailable() } returns false

        // Mock an internet issue message
        val internetIssue = "There is no internet connection"
        every { resourceProvider.getString(any()) } returns internetIssue

        // Observe the ViewModel's state
        viewModel.charactersListState.test {
            viewModel.getCharacters()

            assertEquals(CharactersListState.Initial, awaitItem())
            assertEquals(CharactersListState.Loading, awaitItem())
            assertEquals(CharactersListState.Error(internetIssue), awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getCharacters unexpected repository exception`() = testScope.runTest {
        // Mock that the internet is working
        every { networkHelper.isInternetAvailable() } returns true

        // Mock an Unexpected exception message
        val unexpected = "Unexpected exception"
        every { resourceProvider.getString(any()) } returns unexpected

        // Mock the repository response to throw an unexpected exception
        coEvery { repository.getCharacterList() } throws RuntimeException(unexpected)

        // Observe the ViewModel's state
        viewModel.charactersListState.test {
            viewModel.getCharacters()

            assertEquals(CharactersListState.Initial, awaitItem())
            assertEquals(CharactersListState.Loading, awaitItem())
            assertEquals(CharactersListState.Error(unexpected), awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getCharacters error`() = testScope.runTest {
        // Mock an ApolloException
        val errorMessage = "500 Error"

        // Mock that the internet is working
        every { networkHelper.isInternetAvailable() } returns true

        // Mock the repository response
        coEvery { repository.getCharacterList() } throws  ApolloException(errorMessage)

        every { resourceProvider.getString(any()) } returns errorMessage

        // Observe the ViewModel's state
        viewModel.charactersListState.test {
            viewModel.getCharacters()

            assertEquals(CharactersListState.Initial, awaitItem())
            assertEquals(CharactersListState.Loading, awaitItem())
            assertEquals(CharactersListState.Error(errorMessage), awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun reloadAfterError() = testScope.runTest {
        // Set the state to an error state
        viewModel.reloadAfterError()

        // Observe the ViewModel's state
        viewModel.charactersListState.test {
            assertEquals(CharactersListState.Initial, awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    private fun createMockCharacters() = listOf(
        CharactersListQuery.Result("1", "Character 1", "human", ""),
        CharactersListQuery.Result("2", "Character 2", "human", "")
    )

    private fun createApolloResponse(data: CharactersListQuery.Data): ApolloResponse<CharactersListQuery.Data> {
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