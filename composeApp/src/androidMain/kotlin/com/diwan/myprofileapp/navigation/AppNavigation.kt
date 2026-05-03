package com.diwan.myprofileapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.diwan.myprofileapp.screens.*
import com.diwan.myprofileapp.shared.viewmodel.ChatViewModel
import com.diwan.myprofileapp.shared.viewmodel.NoteViewModel
import com.diwan.myprofileapp.shared.viewmodel.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val noteViewModel: NoteViewModel = koinViewModel()
    val profileViewModel: ProfileViewModel = koinViewModel()
    val chatViewModel: ChatViewModel = koinViewModel()

    val uiState by profileViewModel.uiState.collectAsState()

    val bottomItems = listOf(
        Triple(Screen.NoteList.route, "Notes", Icons.Default.Home),
        Triple(Screen.Favorites.route, "Favorites", Icons.Default.Favorite),
        Triple(Screen.Chat.route, "Chat", Icons.Default.ChatBubble),
        Triple(Screen.Profile.route, "Profile", Icons.Default.Person)
    )

    AppThemeWrapper(isDarkMode = uiState.isDarkMode) {
        Scaffold(
            bottomBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val showBottom = bottomItems.any { it.first == currentRoute }
                if (showBottom) {
                    NavigationBar {
                        bottomItems.forEach { (route, label, icon) ->
                            NavigationBarItem(
                                icon = { Icon(icon, contentDescription = label) },
                                label = { Text(label) },
                                selected = currentRoute == route,
                                onClick = {
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.NoteList.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.NoteList.route) {
                    NoteListScreen(
                        viewModel = noteViewModel,
                        onNoteClick = { noteId ->
                            navController.navigate(Screen.NoteDetail.createRoute(noteId))
                        },
                        onAddNote = { navController.navigate(Screen.AddNote.route) }
                    )
                }
                composable(Screen.AddNote.route) {
                    AddNoteScreen(
                        viewModel = noteViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(
                    route = Screen.NoteDetail.route,
                    arguments = listOf(navArgument("noteId") { type = NavType.LongType })
                ) { backStack ->
                    val noteId = backStack.arguments?.getLong("noteId") ?: return@composable
                    NoteDetailScreen(
                        noteId = noteId,
                        viewModel = noteViewModel,
                        onBack = { navController.popBackStack() },
                        onEdit = { navController.navigate(Screen.EditNote.createRoute(noteId)) }
                    )
                }
                composable(
                    route = Screen.EditNote.route,
                    arguments = listOf(navArgument("noteId") { type = NavType.LongType })
                ) { backStack ->
                    val noteId = backStack.arguments?.getLong("noteId") ?: return@composable
                    EditNoteScreen(
                        noteId = noteId,
                        viewModel = noteViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(Screen.Favorites.route) {
                    FavoritesScreen(
                        viewModel = noteViewModel,
                        onNoteClick = { noteId ->
                            navController.navigate(Screen.NoteDetail.createRoute(noteId))
                        }
                    )
                }
                composable(Screen.Chat.route) {
                    ChatScreen(viewModel = chatViewModel)
                }
                composable(Screen.Profile.route) {
                    ProfileScreen(viewModel = profileViewModel)
                }
            }
        }
    }
}

@Composable
fun AppThemeWrapper(isDarkMode: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isDarkMode) darkColorScheme() else lightColorScheme(),
        content = content
    )
}