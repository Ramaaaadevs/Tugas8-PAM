package com.diwan.myprofileapp.shared.viewmodel

import app.cash.turbine.test
import com.diwan.myprofileapp.shared.ai.GeminiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mockGeminiService: GeminiService
    private lateinit var viewModel: ChatViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockGeminiService = mockk()
        viewModel = ChatViewModel(mockGeminiService)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Test 1 (Turbine Flow)
    @Test
    fun `sendMessage sukses menambahkan balasan AI ke messages`() = runTest {
        coEvery { mockGeminiService.chat(any()) } returns Result.success("Saya siap membantu!")

        viewModel.uiState.test {
            awaitItem() // state awal

            viewModel.sendMessage("Halo AI")
            testDispatcher.scheduler.advanceUntilIdle()

            awaitItem() // loading state
            val finalState = awaitItem() // success state

            assertFalse(finalState.isLoading)
            assertEquals(2, finalState.messages.size)
            assertFalse(finalState.messages.last().isUser)
            assertEquals("Saya siap membantu!", finalState.messages.last().text)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // Test 2 (Turbine Flow)
    @Test
    fun `sendMessage gagal menyimpan pesan error ke uiState`() = runTest {
        coEvery { mockGeminiService.chat(any()) } returns
                Result.failure(Exception("Rate limit exceeded"))

        viewModel.uiState.test {
            awaitItem() // state awal

            viewModel.sendMessage("Coba kirim")
            testDispatcher.scheduler.advanceUntilIdle()

            awaitItem() // loading state
            val errorState = awaitItem()

            assertFalse(errorState.isLoading)
            assertEquals("Rate limit exceeded", errorState.error)
            assertEquals(1, errorState.messages.size)

            cancelAndIgnoreRemainingEvents()
        }
    }
}