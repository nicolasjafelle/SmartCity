package test.citron.domain.usecase

import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
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

class GetCityUseCaseTest {

    private val repository: CityRepository = mockk()
    private val useCase = GetCityUseCase(repository)

    // Dummy data for tests
    val dummyCity = City(
        id = 1L,
        name = "Buenos Aires",
        countryCode = "Argentina",
        isFavorite = false,
        coordinate = Coordinate(
            latitude = -34.6037,
            longitude = -58.3816
        )
    )

    @BeforeEach
    fun setup() {
        mockkObject(CityErrorHandler)
        every { CityErrorHandler.handleError<CityError>(any()) } returns
            Result.Error(CityError.Unknown)
    }

    @AfterEach
    fun tearDown() {
        unmockkObject(CityErrorHandler)
    }

    @Test
    fun `invoke should return Success City when city is found`() = runTest {
        val cityId = 1L
        coEvery { repository.findById(cityId) } returns dummyCity

        val result = useCase.invoke(cityId)

        result shouldBe Result.Success(dummyCity)
        coVerify(exactly = 1) { repository.findById(cityId) }
    }

    @Test
    fun `invoke should return Error CityNotFound when city is not found`() = runTest {
        val cityId = 99L
        coEvery { repository.findById(cityId) } returns null

        val result = useCase.invoke(cityId)

        result shouldBe Result.Error(CityError.CityNotFound)
        coVerify(exactly = 1) { repository.findById(cityId) }
    }

    @Test
    fun `invoke should return Error when repository throws exception`() = runTest {
        val cityId = 1L
        val exception = IllegalStateException("DB connection lost")
        coEvery { repository.findById(cityId) } throws exception

        val result = useCase.invoke(cityId)

        result shouldBe Result.Error(CityError.Unknown)
        coVerify(exactly = 1) { repository.findById(cityId) }
        coVerify(exactly = 1) { CityErrorHandler.handleError<CityError>(exception) }
    }
}
