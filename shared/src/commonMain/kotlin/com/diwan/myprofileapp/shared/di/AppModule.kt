package com.diwan.myprofileapp.shared.di

import com.diwan.myprofileapp.shared.data.NoteRepository
import com.diwan.myprofileapp.shared.viewmodel.ChatViewModel
import com.diwan.myprofileapp.shared.viewmodel.NoteViewModel
import com.diwan.myprofileapp.shared.viewmodel.ProfileViewModel
import org.koin.dsl.module

val commonModule = module {
    single { NoteRepository(get()) }
    factory { NoteViewModel(get()) }
    factory { ProfileViewModel(get()) }
    factory { ChatViewModel(get()) }
}