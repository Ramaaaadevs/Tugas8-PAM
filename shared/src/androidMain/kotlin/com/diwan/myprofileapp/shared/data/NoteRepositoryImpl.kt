package com.diwan.myprofileapp.shared.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.diwan.myprofileapp.db.NotesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class NoteRepositoryImpl(driverFactory: DatabaseDriverFactory) : NoteRepository {

    private val db      = NotesDatabase(driverFactory.createDriver())
    private val queries = db.noteQueries

    override fun getAllNotes(): Flow<List<NoteItem>> =
        queries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toNoteItem() } }

    override fun getFavoriteNotes(): Flow<List<NoteItem>> =
        queries.selectFavorites()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toNoteItem() } }

    override fun searchNotes(query: String): Flow<List<NoteItem>> =
        queries.search(query)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toNoteItem() } }

    override suspend fun getNoteById(id: Long): NoteItem? = withContext(Dispatchers.Default) {
        queries.selectById(id).executeAsOneOrNull()?.toNoteItem()
    }

    override suspend fun insertNote(title: String, content: String) =
        withContext(Dispatchers.Default) {
            val now = System.currentTimeMillis()
            queries.insert(title, content, now, now)
        }

    override suspend fun updateNote(id: Long, title: String, content: String) =
        withContext(Dispatchers.Default) {
            queries.update(title, content, System.currentTimeMillis(), id)
        }

    override suspend fun toggleFavorite(id: Long) = withContext(Dispatchers.Default) {
        queries.toggleFavorite(id)
    }

    override suspend fun deleteNote(id: Long) = withContext(Dispatchers.Default) {
        queries.delete(id)
    }

    // ── Mapper ────────────────────────────────────────────────────────────
    private fun com.diwan.myprofileapp.db.Note.toNoteItem() = NoteItem(
        id         = id,
        title      = title,
        content    = content,
        isFavorite = isFavorite,
        createdAt  = created_at
    )
}
