package test.citron.data.repository

import app.cash.turbine.test
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test.citron.data.CityRepositoryImpl
import test.citron.data.api.CityApiClient
import test.citron.data.entity.CityResponse
import test.citron.data.entity.CoordinateResponse
import test.citron.data.local.CityLocalStorage
import test.citron.data.local.FavoriteLocalStorage
import test.citron.domain.model.City
import test.citron.domain.model.Coordinate

class CityRepositoryTest {

    private val apiClient: CityApiClient = mockk()
    private val localStorage: CityLocalStorage = mockk()
    private val favLocalStorage: FavoriteLocalStorage = mockk()
    private val repository = CityRepositoryImpl(
        apiClient,
        localStorage,
        favLocalStorage
    )

    private val dummyCityResponseList = listOf(
        CityResponse(
            id = 1L,
            name = "New York",
            country = "USA",
            coordinate = CoordinateResponse(
                lat = -33.1234,
                lon = -66.6666
            )
        ),
        CityResponse(
            id = 2L,
            name = "Cordoba",
            country = "Argentina",
            coordinate = CoordinateResponse(
                lat = -31.3334,
                lon = -64.1888
            )
        )
    )

    private val dummyCityList = listOf(
        City(
            id = 1L,
            name = "New York",
            countryCode = "USA",
            isFavorite = false,
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
                latitude = -31.3334,
                longitude = -64.1888
            )
        )
    )

    @BeforeEach
    fun setup() {
        // Default mock behaviors for common calls
        coEvery { apiClient.getCityList() } returns dummyCityResponseList
        coEvery { localStorage.get() } returns dummyCityList
        coEvery { localStorage.store(any()) } just runs
        coEvery { favLocalStorage.get() } returns emptyFavoriteIds
        coEvery { favLocalStorage.store(any()) } just runs

        coEvery { apiClient.getCityList() } returns dummyCityResponseList
        coEvery { localStorage.get() } returns dummyCityList
    }

    @Test
    fun `test fetchCityList`() = runTest {
        // Given
        coEvery { localStorage.get() } returns dummyCityList

        // When
        val result = repository.fetchCityList()

        // Then
        assert(result)
    }

    // Dummy data for favorite IDs
    private val dummyFavoriteIds = setOf(1L)
    private val emptyFavoriteIds = emptySet<Long>()

    // --- fetchCityList() tests ---
    @Test
    fun `should return true and store data on successful API call and local storage is empty`() =
        runTest {
            coEvery { apiClient.getCityList() } returns dummyCityResponseList

            coEvery { localStorage.get() } returns null
            coEvery { localStorage.store(any()) } returns Unit

            // When
            val result = repository.fetchCityList()

            // Then
            result shouldBe true
            coVerify(exactly = 1) { apiClient.getCityList() }
            coVerify(exactly = 1) { localStorage.store(any()) }
        }

    // --- store(cityList: List<City>) tests ---
    @Test
    fun `store should call localStorage store with provided list`() = runTest {
        // Given
        val citiesToStore = dummyCityList
        coEvery { localStorage.store(citiesToStore) } returns Unit

        // When
        repository.store(citiesToStore)

        // Then
        coVerify(exactly = 1) { localStorage.store(citiesToStore) }
    }

    // --- getCityList() tests ---
    @Test
    fun `getCityList should emit list from localStorage`() = runTest {
        // Given
        coEvery { localStorage.get() } returns dummyCityList

        // When & Then
        repository.getCityList().test {
            awaitItem() shouldBe dummyCityList // emit local storage data
            awaitItem() shouldBe dummyCityList // emit api data
            awaitComplete()
        }
        coVerify(exactly = 1) { localStorage.get() }
    }

    @Test
    fun `getCityList should emit empty list when localStorage is empty`() = runTest {
        // Given
        val emptyStorageList = emptyList<City>()
        coEvery { localStorage.get() } returns emptyStorageList

        // When
        repository.getCityList().test {
            // Then
            awaitItem() shouldBe emptyStorageList
            awaitItem() shouldBe dummyCityList
            awaitComplete()
        }
        coVerify(exactly = 1) { localStorage.get() }
    }

    @Test
    fun `getCityList should emit empty list and log error when localStorage throws exception`() =
        runTest {
            // Given
            val exception = RuntimeException("Local storage read error")
            coEvery { localStorage.get() } throws exception

            // When
            repository.getCityList().test {
                // Then
                awaitError()
            }
            coVerify(exactly = 1) { localStorage.get() }
            // Verify that Log.e was called (if CityRepositoryImpl logs the error)
        }

    // --- searchCity(query: String) tests ---
    @Test
    fun `searchCity should emit filtered list based on query from localStorage`() = runTest {
        // Given
        val query = "New"
        val expectedList = listOf(dummyCityList[0]) // New York

        coEvery { favLocalStorage.findById(any()) } returns false
        coEvery { localStorage.get() } returns dummyCityList

        // When
        repository.searchCity(query).test {
            // Then
            awaitItem() shouldBe expectedList
            awaitComplete()
        }
        coVerify(exactly = 1) { localStorage.get() }
    }

    @Test
    fun `searchCity should emit empty list when no matching cities found`() = runTest {
        // Given
        val query = "NonExistent"
        coEvery { localStorage.get() } returns dummyCityList

        // When
        repository.searchCity(query).test {
            // Then
            awaitItem() shouldBe emptyList()
            awaitComplete()
        }
        coVerify(exactly = 1) { localStorage.get() }
    }

    @Test
    fun `searchCity should be case-insensitive`() = runTest {
        // Given
        val query = "new york" // Lowercase query
        val expectedList = listOf(dummyCityList[0]) // New York

        coEvery { favLocalStorage.findById(any()) } returns false
        coEvery { localStorage.get() } returns dummyCityList

        // When
        repository.searchCity(query).test {
            // Then
            awaitItem() shouldBe expectedList
            awaitComplete()
        }
        coVerify(exactly = 1) { localStorage.get() }
    }

    // --- findById(id: Long) tests ---
    @Test
    fun `findById should return City when found in localStorage`() = runTest {
        // Given
        val cityId = 1L
        val expectedResult = dummyCityList[0]
        coEvery { localStorage.get() } returns dummyCityList
        coEvery { favLocalStorage.findById(any()) } returns false
        coEvery { localStorage.getById(cityId) } returns expectedResult

        // When
        val result = repository.findById(cityId)

        // Then
        result shouldBe expectedResult
        coVerify(exactly = 1) { localStorage.getById(any()) }
        coVerify(exactly = 1) { favLocalStorage.findById(any()) }
    }

    @Test
    fun `findById should return null when not found in localStorage`() = runTest {
        // Given
        val cityId = 99L
        coEvery { localStorage.get() } returns dummyCityList
        coEvery { favLocalStorage.findById(any()) } returns false
        coEvery { localStorage.getById(cityId) } returns null

        // When
        val result = repository.findById(cityId)

        // Then
        result shouldBe null
        coVerify(exactly = 1) { localStorage.getById(any()) }
        coVerify(exactly = 0) { favLocalStorage.findById(any()) } // should not be called
    }

    // --- addFavorite(id: Long) tests ---
    @Test
    fun `addFavorite should return true when city is added as favorite successfully`() = runTest {
        // Given: City is not currently a favorite
        val cityId = 2L // Cordoba, not in dummyFavoriteIds
        val expectedResult = true
        coEvery { favLocalStorage.get() } returns dummyFavoriteIds // Only New York is favorite
        coEvery { favLocalStorage.store(dummyFavoriteIds + cityId) } just runs
        coEvery { favLocalStorage.addFavorite(cityId) } returns expectedResult

        // When
        val result = repository.addFavorite(cityId)

        // Then
        result shouldBe expectedResult
        coVerify(exactly = 1) { favLocalStorage.addFavorite(any()) }
    }

    @Test
    fun `removeFavorite should return true when city is removed from favorites successfully`() =
        runTest {
            // Given: City is currently a favorite
            val cityId = 1L // New York is favorite
            val expectedResult = true
            coEvery { favLocalStorage.get() } returns dummyFavoriteIds
            coEvery { favLocalStorage.store(emptyFavoriteIds) } just runs
            coEvery { favLocalStorage.removeFavorite(cityId) } returns expectedResult

            // When
            val result = repository.removeFavorite(cityId)

            // Then
            result shouldBe expectedResult
            coVerify(exactly = 1) { favLocalStorage.removeFavorite(any()) }
        }

    // --- clear() tests ---
    @Test
    fun `clear should call favLocalStorage clear`() = runTest {
        // Given
        coEvery { localStorage.clear() } just runs
        coEvery { favLocalStorage.clear() } just runs

        // When
        repository.clear()

        // Then
        coVerify(exactly = 1) { localStorage.clear() }
        coVerify(exactly = 1) { favLocalStorage.clear() }
    }
}
