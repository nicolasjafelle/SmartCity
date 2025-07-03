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
import test.citron.domain.model.CityError
import test.citron.domain.model.CityErrorHandler
import test.citron.domain.repository.CityRepository
import test.citron.foundation.result.Result

class AddFavoriteUseCaseTest {

    private val repository: CityRepository = mockk()
    private val useCase = AddFavoriteUseCase(repository)

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
    fun `invoke should return Success true when repository adds favorite successfully`() = runTest {
        // Given
        val cityId = 1L
        coEvery { repository.addFavorite(cityId) } returns true

        // When
        val result = useCase.invoke(cityId)

        // Then
        result shouldBe Result.Success(true)
        coVerify(exactly = 1) { repository.addFavorite(cityId) }
    }

    @Test
    fun `invoke should return Error Unknown when repository fails to add favorite`() = runTest {
        // Given
        val cityId = 1L
        coEvery { repository.addFavorite(cityId) } returns false

        // When
        val result = useCase.invoke(cityId)

        // Then
        result shouldBe Result.Error(CityError.Unknown)
        coVerify(exactly = 1) { repository.addFavorite(cityId) }
    }

    @Test
    fun `invoke should return Error when repository throws exception`() = runTest {
        // Given
        val cityId = 1L
        val exception = RuntimeException("Network error")
        coEvery { repository.addFavorite(cityId) } throws exception

        // When
        val result = useCase.invoke(cityId)

        // Then
        result shouldBe Result.Error(CityError.Unknown)
        coVerify(exactly = 1) { repository.addFavorite(cityId) }
        coVerify(exactly = 1) { CityErrorHandler.handleError<CityError>(exception) }
    }
}
