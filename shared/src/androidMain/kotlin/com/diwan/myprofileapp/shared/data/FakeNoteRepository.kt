package com.diwan.myprofileapp.shared.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * FakeNoteRepository — implementasi in-memory dari NoteRepository.
 * Dipakai di unit test agar tidak bergantung pada SQLDelight / database asli.
 */
class FakeNoteRepository : NoteRepository {

    // State internal berbentuk MutableStateFlow agar bisa diobservasi Flow
    private val _notes = MutableStateFlow<List<NoteItem>>(emptyList())
    private var nextId = 1L

    // ── Read operations ───────────────────────────────────────────────────

    override fun getAllNotes(): Flow<List<NoteItem>> = _notes

    override fun getFavoriteNotes(): Flow<List<NoteItem>> =
        _notes.map { list -> list.filter { it.isFavorite == 1L } }

    override fun searchNotes(query: String): Flow<List<NoteItem>> =
        _notes.map { list ->
            list.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.content.contains(query, ignoreCase = true)
            }
        }

    override suspend fun getNoteById(id: Long): NoteItem? =
        _notes.value.find { it.id == id }

    // ── Write operations ──────────────────────────────────────────────────

    override suspend fun insertNote(title: String, content: String) {
        val note = NoteItem(
            id         = nextId++,
            title      = title,
            content    = content,
            isFavorite = 0L,
            createdAt  = System.currentTimeMillis()
        )
        _notes.update { it + note }
    }

    override suspend fun updateNote(id: Long, title: String, content: String) {
        _notes.update { list ->
            list.map { note ->
                if (note.id == id) note.copy(title = title, content = content) else note
            }
        }
    }

    override suspend fun toggleFavorite(id: Long) {
        _notes.update { list ->
            list.map { note ->
                if (note.id == id)
                    note.copy(isFavorite = if (note.isFavorite == 1L) 0L else 1L)
                else note
            }
        }
    }

    override suspend fun deleteNote(id: Long) {
        _notes.update { list -> list.filter { it.id != id } }
    }

    // ── Test helpers ──────────────────────────────────────────────────────

    /** Seed data awal untuk test yang butuh notes sudah ada. */
    fun seedNotes(vararg notes: NoteItem) {
        _notes.value = notes.toList()
        nextId = (notes.maxOfOrNull { it.id } ?: 0L) + 1
    }
}
