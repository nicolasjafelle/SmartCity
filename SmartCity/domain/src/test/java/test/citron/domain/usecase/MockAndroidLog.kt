package test.citron.domain.usecase

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic

// Mock Android Log for all tests
class MockAndroidLog {

    companion object {
        @JvmStatic
        fun mock() {
            mockkStatic(Log::class)
            every { Log.e(any(), any<String>(), any()) } returns 0
            every { Log.w(any(), any<String>(), any()) } returns 0
            every { Log.i(any(), any<String>()) } returns 0
        }

        @JvmStatic
        fun unMock() {
            unmockkStatic(Log::class)
        }
    }
}
