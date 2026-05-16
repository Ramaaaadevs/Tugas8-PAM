package com.diwan.myprofileapp.shared.data

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class NoteRepositoryTest {

    private lateinit var repository: FakeNoteRepository

    @BeforeTest
    fun setup() {
        repository = FakeNoteRepository()
    }

    // Test 1
    @Test
    fun `insertNote menambahkan catatan baru ke dalam list`() = runTest {
        repository.insertNote("Belanja", "Susu, Roti, Telur")

        repository.getAllNotes().test {
            val notes = awaitItem()
            assertEquals(1, notes.size)
            assertEquals("Belanja", notes.first().title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Test 2
    @Test
    fun `getAllNotes mengembalikan seluruh catatan yang ada`() = runTest {
        repository.insertNote("Catatan 1", "Isi 1")
        repository.insertNote("Catatan 2", "Isi 2")
        repository.insertNote("Catatan 3", "Isi 3")

        repository.getAllNotes().test {
            val notes = awaitItem()
            assertEquals(3, notes.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Test 3
    @Test
    fun `getNoteById mengembalikan catatan yang sesuai dengan id`() = runTest {
        repository.seedNotes(
            NoteItem(id = 10L, title = "Target", content = "Isi target", isFavorite = 0L, createdAt = 0L)
        )

        val found = repository.getNoteById(10L)
        assertNotNull(found)
        assertEquals("Target", found.title)
    }

    // Test 4
    @Test
    fun `deleteNote menghapus catatan dari list`() = runTest {
        repository.seedNotes(
            NoteItem(id = 1L, title = "Hapus", content = "Isi", isFavorite = 0L, createdAt = 0L)
        )

        repository.deleteNote(1L)

        repository.getAllNotes().test {
            val notes = awaitItem()
            assertTrue(notes.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Test 5
    @Test
    fun `toggleFavorite mengubah status favorit catatan bolak-balik`() = runTest {
        repository.seedNotes(
            NoteItem(id = 1L, title = "Note", content = "Isi", isFavorite = 0L, createdAt = 0L)
        )

        repository.toggleFavorite(1L)
        var note = repository.getNoteById(1L)
        assertEquals(1L, note?.isFavorite)

        repository.toggleFavorite(1L)
        note = repository.getNoteById(1L)
        assertEquals(0L, note?.isFavorite)
    }
}