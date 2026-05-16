package com.diwan.myprofileapp.shared.data

import kotlinx.coroutines.flow.Flow

/**
 * Interface NoteRepository — memisahkan kontrak dari implementasi
 * sehingga mudah di-mock pada unit test.
 */
interface NoteRepository {
    fun getAllNotes(): Flow<List<NoteItem>>
    fun getFavoriteNotes(): Flow<List<NoteItem>>
    fun searchNotes(query: String): Flow<List<NoteItem>>
    suspend fun getNoteById(id: Long): NoteItem?
    suspend fun insertNote(title: String, content: String)
    suspend fun updateNote(id: Long, title: String, content: String)
    suspend fun toggleFavorite(id: Long)
    suspend fun deleteNote(id: Long)
}