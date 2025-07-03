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

class RemoveFavoriteUseCaseTest {

    private val repository: CityRepository = mockk()
    private val useCase = RemoveFavoriteUseCase(repository)

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
    fun `invoke should return Success true when repository removes favorite successfully`() =
        runTest {
            val cityId = 1L
            coEvery { repository.removeFavorite(cityId) } returns true

            val result = useCase.invoke(cityId)

            result shouldBe Result.Success(true)
            coVerify(exactly = 1) { repository.removeFavorite(cityId) }
        }

    @Test
    fun `invoke should return Error Unknown when repository fails to remove favorite`() = runTest {
        val cityId = 1L
        coEvery { repository.removeFavorite(cityId) } returns false

        val result = useCase.invoke(cityId)

        result shouldBe Result.Error(CityError.Unknown)
        coVerify(exactly = 1) { repository.removeFavorite(cityId) }
    }

    @Test
    fun `invoke should return Error when repository throws exception`() = runTest {
        val cityId = 1L
        val exception = RuntimeException("DB write error")
        coEvery { repository.removeFavorite(cityId) } throws exception

        val result = useCase.invoke(cityId)

        result shouldBe Result.Error(CityError.Unknown)
        coVerify(exactly = 1) { repository.removeFavorite(cityId) }
        coVerify(exactly = 1) { CityErrorHandler.handleError<CityError>(exception) }
    }
}
