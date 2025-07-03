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

class FetchCityListUseCaseTest {

    private val repository: CityRepository = mockk()
    private val useCase = FetchCityListUseCase(repository)

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
    fun `invoke should return Success true when repository fetches city list successfully`() =
        runTest {
            coEvery { repository.fetchCityList() } returns true

            val result = useCase.invoke()

            result shouldBe Result.Success(true)
            coVerify(exactly = 1) { repository.fetchCityList() }
        }

    @Test
    fun `invoke should return Error when repository throws exception during fetch`() = runTest {
        val exception = RuntimeException("Database error")
        coEvery { repository.fetchCityList() } throws exception

        val result = useCase.invoke()

        result shouldBe Result.Error(CityError.Unknown)
        coVerify(exactly = 1) { repository.fetchCityList() }
        coVerify(exactly = 1) { CityErrorHandler.handleError<CityError>(exception) }
    }
}
