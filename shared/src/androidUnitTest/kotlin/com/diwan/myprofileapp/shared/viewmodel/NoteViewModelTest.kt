package com.diwan.myprofileapp.shared.viewmodel

import app.cash.turbine.test
import com.diwan.myprofileapp.shared.data.FakeNoteRepository
import com.diwan.myprofileapp.shared.data.NoteItem
import com.diwan.myprofileapp.shared.data.NoteRepository
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockRepository: NoteRepository
    private lateinit var viewModelWithMock: NoteViewModel

    private lateinit var fakeRepository: FakeNoteRepository
    private lateinit var viewModelWithFake: NoteViewModel

    private val testNote = NoteItem(
        id = 1L,
        title = "Test Note",
        content = "Test Content",
        isFavorite = 0L,
        createdAt = 0L
    )

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockRepository = mockk<NoteRepository>()
        coEvery { mockRepository.getAllNotes() } returns flowOf(listOf(testNote))
        coEvery { mockRepository.getFavoriteNotes() } returns flowOf(emptyList())
        viewModelWithMock = NoteViewModel(mockRepository)

        fakeRepository = FakeNoteRepository()
        viewModelWithFake = NoteViewModel(fakeRepository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Test 1
    @Test
    fun `addNote memanggil repository insertNote dengan title dan content yang benar`() = runTest {
        coJustRun { mockRepository.insertNote(any(), any()) }

        viewModelWithMock.addNote("Judul Baru", "Isi Baru")
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { mockRepository.insertNote("Judul Baru", "Isi Baru") }
    }

    // Test 2
    @Test
    fun `deleteNote memanggil repository deleteNote dengan id yang benar`() = runTest {
        coJustRun { mockRepository.deleteNote(any()) }

        viewModelWithMock.deleteNote(testNote.id)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { mockRepository.deleteNote(testNote.id) }
    }

    // Test 3
    @Test
    fun `notes flow mengembalikan catatan yang baru ditambahkan`() = runTest {
        viewModelWithFake.notes.test {
            val initial = awaitItem()
            assertTrue(initial.isEmpty())

            viewModelWithFake.addNote("Note Flow", "Isi Flow")
            testDispatcher.scheduler.advanceUntilIdle()

            val updated = awaitItem()
            assertEquals(1, updated.size)
            assertEquals("Note Flow", updated.first().title)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // Test 4
    @Test
    fun `setSearchQuery memfilter catatan berdasarkan keyword`() = runTest {
        viewModelWithFake.notes.test {
            // Subscribe dulu, baru seed
            awaitItem() // emisi awal kosong

            fakeRepository.seedNotes(
                NoteItem(1L, "Android Kotlin", "Belajar KMP", 0L, 0L),
                NoteItem(2L, "Resep Masakan", "Nasi Goreng", 0L, 0L)
            )
            testDispatcher.scheduler.advanceUntilIdle()

            val all = awaitItem()
            assertEquals(2, all.size)

            viewModelWithFake.setSearchQuery("kotlin")
            testDispatcher.scheduler.advanceUntilIdle()

            val filtered = awaitItem()
            assertEquals(1, filtered.size)
            assertTrue(filtered.first().title.contains("Kotlin", ignoreCase = true))

            cancelAndIgnoreRemainingEvents()
        }
    }
}