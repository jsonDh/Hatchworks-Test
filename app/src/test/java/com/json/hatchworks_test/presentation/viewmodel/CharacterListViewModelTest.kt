package com.json.hatchworks_test.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.exception.ApolloException
import com.json.hatchworks_test.CharactersListQuery
import com.json.hatchworks_test.domain.repository.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.*
import org.mockito.*
import org.mockito.junit.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import timber.log.Timber
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CharacterListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: CharacterRepository
    private lateinit var viewModel: CharacterListViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = CharacterListViewModel(repository)
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

        // Mock the repository response
        `when`(repository.getCharacterList()).thenReturn(response)

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

        // Mock the repository response
        `when`(repository.getCharacterList()).thenReturn(response)

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

        // Mock the repository response
        `when`(repository.getCharacterList()).thenReturn(response)

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
    fun `getCharacters error`() = testScope.runTest {
        // Mock an ApolloException
        val errorMessage = "Network error"
        `when`(repository.getCharacterList()).thenThrow(ApolloException(errorMessage))

        // Observe the ViewModel's state
        viewModel.charactersListState.test {
            viewModel.getCharacters()

            assertEquals(CharactersListState.Initial, awaitItem())
            assertEquals(CharactersListState.Loading, awaitItem())
            assertEquals(CharactersListState.Error(errorMessage), awaitItem())

            cancelAndConsumeRemainingEvents()
        }
    }

    private fun createMockCharacters() = listOf(
        CharactersListQuery.Result("1", "Character 1", "human", ""),
        CharactersListQuery.Result("2", "Character 2", "human", "")
    )

    private fun createMockOperation(): Operation<CharactersListQuery.Data> {
        return mock(Operation::class.java) as Operation<CharactersListQuery.Data>
    }

    private fun createApolloResponse(data: CharactersListQuery.Data): ApolloResponse<CharactersListQuery.Data> {
        return ApolloResponse.Builder(
            data = data,
            operation = createMockOperation(),
            requestUuid = UUID.randomUUID()
        ).build()
    }

    class TestTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            // No-op
        }
    }
}