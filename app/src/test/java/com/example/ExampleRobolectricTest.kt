package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.CppCodeSimulator
import com.example.ui.viewmodel.CppViewModel
import com.example.util.SystemIntegrationHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("C++ Master", appName)
    }

    @Test
    fun `workspace storage and notification channel initialize cleanly`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        SystemIntegrationHelper.createNotificationChannel(context)
        val savedFile = SystemIntegrationHelper.saveCppFileToWorkspace(
            context,
            "test_main.cpp",
            "#include <iostream>\nint main() { std::cout << \"OK\"; return 0; }"
        )
        assertNotNull(savedFile)
        assertTrue(savedFile!!.exists())
        assertTrue(SystemIntegrationHelper.getSavedWorkspaceFilesCount(context) >= 1)
    }

    @Test
    fun `onboarding state persists and simulator evaluates accurately`() {
        val app = ApplicationProvider.getApplicationContext<android.app.Application>()
        app.getSharedPreferences(CppViewModel.PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()

        val viewModel = CppViewModel(app)
        assertFalse(viewModel.hasCompletedOnboarding.value)

        viewModel.completeOnboarding()
        assertTrue(viewModel.hasCompletedOnboarding.value)

        val simResult = CppCodeSimulator.runPlayground(
            "#include <iostream>\nint main() { std::cout << \"Halo C++20\"; return 0; }"
        )
        assertTrue(simResult.isSuccess)
        assertEquals("Halo C++20", simResult.output)
    }
}
