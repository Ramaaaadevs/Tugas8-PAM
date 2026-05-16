package com.diwan.myprofileapp.util

/**
 * TestTags — konstanta semantic tag untuk Compose UI testing.
 *
 * Dipakai dengan Modifier.testTag() di composable, dan
 * onNodeWithTag() di instrumented test.
 *
 * Keuntungan: tidak bergantung pada teks UI yang bisa berubah karena
 * localization atau refactor copy.
 */
object TestTags {
    // NoteList Screen
    const val NOTE_LIST          = "note_list"
    const val NOTE_LIST_EMPTY    = "note_list_empty"
    const val NOTE_ITEM          = "note_item"
    const val NOTE_ITEM_TITLE    = "note_item_title"
    const val SEARCH_FIELD       = "search_field"
    const val FAB_ADD_NOTE       = "fab_add_note"

    // AddNote / EditNote Screen
    const val INPUT_TITLE        = "input_title"
    const val INPUT_CONTENT      = "input_content"
    const val BUTTON_SAVE        = "button_save"

    // Chat Screen
    const val CHAT_INPUT         = "chat_input"
    const val CHAT_SEND_BUTTON   = "chat_send_button"
    const val CHAT_MESSAGES_LIST = "chat_messages_list"
    const val CHAT_EMPTY_STATE   = "chat_empty_state"
    const val CHAT_TYPING        = "chat_typing_indicator"
    const val CHAT_ERROR_BANNER  = "chat_error_banner"
}
