package com.diwan.myprofileapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.diwan.myprofileapp.screens.NoteListScreen
import com.diwan.myprofileapp.shared.data.FakeNoteRepository
import com.diwan.myprofileapp.shared.data.NoteItem
import com.diwan.myprofileapp.shared.viewmodel.NoteViewModel
import com.diwan.myprofileapp.util.TestTags
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class NoteListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var fakeRepository: FakeNoteRepository
    private lateinit var viewModel: NoteViewModel

    @Before
    fun setup() {
        fakeRepository = FakeNoteRepository()
        viewModel = NoteViewModel(fakeRepository)
    }

    // Test 1
    @Test
    fun emptyState_tampilPesanTidakAdaCatatan() {
        composeTestRule.setContent {
            NoteListScreen(
                viewModel = viewModel,
                onNoteClick = {},
                onAddNote = {}
            )
        }

        composeTestRule
            .onNodeWithTag(TestTags.NOTE_LIST_EMPTY)
            .assertIsDisplayed()
    }

    // Test 2
    @Test
    fun notesList_tampilCardCatatan_ketikaNoteAda() {
        fakeRepository.seedNotes(
            NoteItem(1L, "Android Kotlin", "Belajar KMP", 0L, 0L),
            NoteItem(2L, "Resep Masakan", "Nasi Goreng", 0L, 0L)
        )

        composeTestRule.setContent {
            NoteListScreen(
                viewModel = viewModel,
                onNoteClick = {},
                onAddNote = {}
            )
        }

        composeTestRule
            .onNodeWithTag(TestTags.NOTE_LIST)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Android Kotlin").assertIsDisplayed()
        composeTestRule.onNodeWithText("Resep Masakan").assertIsDisplayed()
    }

    // Test 3
    @Test
    fun fab_tampilDanBisaDiklik() {
        var addNoteClicked = false

        composeTestRule.setContent {
            NoteListScreen(
                viewModel = viewModel,
                onNoteClick = {},
                onAddNote = { addNoteClicked = true }
            )
        }

        composeTestRule
            .onNodeWithTag(TestTags.FAB_ADD_NOTE)
            .assertIsDisplayed()
            .performClick()

        assert(addNoteClicked) { "onAddNote seharusnya dipanggil saat FAB diklik" }
    }
}