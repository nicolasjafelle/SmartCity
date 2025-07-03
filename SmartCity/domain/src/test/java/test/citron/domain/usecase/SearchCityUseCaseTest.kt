package test.citron.domain.usecase

import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test.citron.domain.model.City
import test.citron.domain.model.CityError
import test.citron.domain.model.CityErrorHandler
import test.citron.domain.model.Coordinate
import test.citron.domain.repository.CityRepository
import test.citron.foundation.result.Result

class SearchCityUseCaseTest {

    private val repository: CityRepository = mockk()
    private val useCase = SearchCityUseCase(repository)

    val dummyCityList = listOf(
        City(
            id = 1L,
            name = "New York",
            countryCode = "USA",
            isFavorite = true,
            coordinate = Coordinate(
                latitude = -33.1234,
                longitude = -66.6666

            )
        ),
        City(
            id = 2L,
            name = "Cordoba",
            countryCode = "Argentina",
            isFavorite = false,
            coordinate = Coordinate(
                latitude = -31.4201,
                longitude = -64.1888
            )
        )
    )

    @BeforeEach
    fun setup() {
        MockAndroidLog.mock()
        mockkObject(CityErrorHandler)
        every { CityErrorHandler.handleError<CityError>(any()) } returns
            Result.Error(CityError.Unknown)
    }

    @AfterEach
    fun tearDown() {
        MockAndroidLog.unMock()
        unmockkObject(CityErrorHandler)
    }

    @Test
    fun `invoke should emit Success with list when repository search is successful`() = runTest {
        // Given
        val aSearch = "Buenos"
        val expectedList = dummyCityList
        coEvery { repository.searchCity(aSearch) } returns flowOf(expectedList)

        // When
        useCase.invoke(aSearch).test {
            awaitItem() shouldBe Result.Success(expectedList)
            awaitComplete()
        }

        // Then
        coVerify(exactly = 1) { repository.searchCity(aSearch) }
    }

    @Test
    fun `invoke should emit Error when repository search throws exception`() = runTest {
        // Given
        val aSearch = "Error"
        val exception = RuntimeException("Search API failed")
        val expectedError = Result.Error<CityError>(CityError.Unknown)

        coEvery { repository.searchCity(aSearch) } returns flow { throw exception }
        every { CityErrorHandler.handleError<CityError>(exception) } returns expectedError

        // When
        useCase.invoke(aSearch).test {
            awaitItem() shouldBe CityErrorHandler.handleError<CityError>(exception)
            awaitComplete()
        }

        // Then
        coVerify(exactly = 1) { repository.searchCity(aSearch) }
        coVerify(exactly = 1) { CityErrorHandler.handleError<CityError>(exception) }
    }
}
