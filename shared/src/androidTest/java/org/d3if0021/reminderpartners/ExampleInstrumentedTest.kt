package org.d3if0021.reminderpartners

// shared/src/androidTest/java/org/d3if0021/reminderpartners/ExampleInstrumentedTest.kt
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("org.d3if0021.reminderpartners.test", appContext.packageName)
    }
}