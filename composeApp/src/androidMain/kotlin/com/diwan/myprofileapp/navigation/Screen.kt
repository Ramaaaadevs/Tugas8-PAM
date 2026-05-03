package com.diwan.myprofileapp.navigation

sealed class Screen(val route: String) {
    object NoteList : Screen("note_list")
    object AddNote : Screen("add_note")
    object Favorites : Screen("favorites")
    object Chat : Screen("chat")
    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")

    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Long) = "note_detail/$noteId"
    }

    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Long) = "edit_note/$noteId"
    }
}