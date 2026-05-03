# MyProfileApp - Week 9: Integrasi AI API

## Deskripsi
MyProfileApp adalah aplikasi Android berbasis Kotlin Multiplatform (KMP) yang dilengkapi dengan fitur AI Chatbot menggunakan Google Gemini API.

## Fitur Utama
- 📝 **Notes Management** — Buat, edit, hapus, dan favorit catatan
- ❤️ **Favorites** — Koleksi catatan favorit
- 💬 **AI Chatbot** — Smart assistant powered by Google Gemini
- 👤 **Profile** — Informasi profil dengan device info dan battery status
- 🌐 **Network Monitor** — Indikator koneksi internet
- 🌙 **Dark Mode** — Toggle tema gelap/terang

## Fitur AI (Week 9)

### Smart Chatbot dengan Gemini API
- **Model**: Google Gemini 2.5 Flash
- **Fitur**: Multi-turn conversation (AI mengingat konteks percakapan)
- **Bahasa**: Bahasa Indonesia (default)
- **System Prompt**: Diarahkan sebagai asisten Notes app

### Implementasi
- `GeminiService.kt` — Service layer untuk komunikasi dengan Gemini API
- `GeminiModels.kt` — Data models (request/response DTOs)
- `ChatViewModel.kt` — State management dengan MVVM pattern
- `ChatScreen.kt` — UI dengan loading states dan typing indicator
- `ChatMessage.kt` — Model data pesan dan UI state

### Prompt Engineering
System prompt dirancang untuk:
- Menjawab dalam Bahasa Indonesia
- Berperan sebagai asisten aplikasi Notes
- Memberikan jawaban singkat dan relevan

### Error Handling
- Menampilkan pesan error yang user-friendly
- Tombol dismiss untuk menutup error
- Menangani rate limit dan network error

## Tech Stack
- **Language**: Kotlin Multiplatform (KMP)
- **UI**: Jetpack Compose
- **AI**: Google Gemini API (v1beta)
- **HTTP Client**: Ktor
- **DI**: Koin
- **Database**: SQLDelight
- **Architecture**: MVVM + Clean Architecture

## Setup

### Prasyarat
- Android Studio
- JDK 11+
- Google Gemini API Key

### Konfigurasi API Key
1. Buat API key di [Google AI Studio](https://aistudio.google.com)
2. Tambahkan ke `local.properties`: 
GEMINI_API_KEY=your_api_key_here

### Menjalankan Aplikasi
```bash
./gradlew :composeApp:assembleDebug
```

## Bonus Features
- ✅ Multi-turn conversation (+5%)

## Repository
- **Week 8**: [Tugas8-PAM](https://github.com/Ramaaaadevs/Tugas8-PAM)
- **Week 9**: [Tugas9-PAM](https://github.com/Ramaaaadevs/Tugas8-PAM/tree/Tugas9-PAM)
