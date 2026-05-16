# MyProfileApp - Week 10: Testing & Dependency Injection

## Deskripsi
MyProfileApp adalah aplikasi Android berbasis Kotlin Multiplatform (KMP) dengan fitur AI Chatbot (Gemini), Notes Management, dan kini dilengkapi dengan **Dependency Injection (Koin)** serta **Testing** yang komprehensif.

---

## Fitur Utama
- 📝 **Notes Management** — Buat, edit, hapus, dan favorit catatan
- ❤️ **Favorites** — Koleksi catatan favorit
- 💬 **AI Chatbot** — Smart assistant powered by Google Gemini
- 👤 **Profile** — Informasi profil dengan device info dan battery status
- 🌐 **Network Monitor** — Indikator koneksi internet
- 🌙 **Dark Mode** — Toggle tema gelap/terang

---

## Week 10: Dependency Injection & Testing

### Dependency Injection (Koin)

**Modules yang dikonfigurasi:**

| Module | Isi |
|---|---|
| `commonModule` | `NoteViewModel`, `ProfileViewModel`, `ChatViewModel` |
| `androidModule` | `DatabaseDriverFactory`, `NoteRepositoryImpl`, `SettingsRepository`, `HttpClient`, `GeminiService`, `DeviceInfo`, `NetworkMonitor`, `BatteryInfo` |

**Refactoring untuk testability:**
- `NoteRepository` dibuat sebagai **interface** (`NoteRepository.kt`) di `commonMain`
- Implementasi konkret dipindah ke `NoteRepositoryImpl.kt` di `androidMain`
- `androidModule` menggunakan `single { NoteRepositoryImpl(get()) } bind NoteRepository::class`
- `FakeNoteRepository` sebagai in-memory fake untuk testing di `androidMain`

---

### Testing

#### Unit Tests (`shared/src/androidUnitTest/`)

##### `NoteRepositoryTest.kt` — 5 Test Cases
Menggunakan `FakeNoteRepository` (in-memory, tanpa database asli):

| # | Test | Deskripsi |
|---|---|---|
| 1 | `insertNote menambahkan catatan baru` | Verifikasi note tersimpan di list |
| 2 | `getAllNotes mengembalikan seluruh catatan` | Verifikasi semua note dikembalikan |
| 3 | `getNoteById mengembalikan catatan yang sesuai` | Verifikasi pencarian by id benar |
| 4 | `deleteNote menghapus catatan` | Verifikasi catatan terhapus |
| 5 | `toggleFavorite mengubah status bolak-balik` | Verifikasi toggle on/off |

##### `NoteViewModelTest.kt` — 4 Test Cases
Menggunakan **MockK** (untuk verify interaksi) dan **FakeNoteRepository** (untuk state flow):

| # | Test | Library |
|---|---|---|
| 1 | `addNote memanggil insertNote dengan parameter benar` | MockK + coVerify |
| 2 | `deleteNote memanggil deleteNote dengan id benar` | MockK + coVerify |
| 3 | `notes flow mengembalikan catatan yang baru ditambahkan` | Turbine |
| 4 | `setSearchQuery memfilter catatan berdasarkan keyword` | Turbine |

##### `ChatViewModelTest.kt` — 2 Flow Test Cases (dengan Turbine)

| # | Test |
|---|---|
| 1 | `sendMessage sukses menambahkan balasan AI ke messages` |
| 2 | `sendMessage gagal menyimpan error ke uiState` |

#### UI Tests (`composeApp/src/androidInstrumentedTest/`)

##### `NoteListScreenTest.kt` — 3 UI Test Cases

| # | Test |
|---|---|
| 1 | `emptyState_tampilPesanTidakAdaCatatan` |
| 2 | `notesList_tampilCardCatatan_ketikaNoteAda` |
| 3 | `fab_tampilDanBisaDiklik` |

---

### Test Coverage Summary

| Komponen | Test Cases | Library |
|---|---|---|
| NoteRepository | 5 | kotlin.test + Turbine |
| NoteViewModel | 4 | MockK + Turbine |
| ChatViewModel | 2 | MockK + Turbine |
| NoteListScreen (UI) | 3 | Compose Test |
| **Total** | **14** | |

---

## Tech Stack
- **Language**: Kotlin Multiplatform (KMP)
- **UI**: Jetpack Compose
- **AI**: Google Gemini API (gemini-2.5-flash)
- **HTTP Client**: Ktor
- **DI**: Koin 3.5.3
- **Database**: SQLDelight 2.0.2
- **Architecture**: MVVM + Clean Architecture
- **Unit Test**: kotlin.test + MockK 1.13.12 + Turbine 1.1.0
- **UI Test**: Compose Test (androidx.compose.ui:ui-test-junit4)

---

## Setup

### Prasyarat
- Android Studio Hedgehog+
- JDK 11+
- Google Gemini API Key

### Konfigurasi API Key
```
# local.properties
GEMINI_API_KEY=your_api_key_here
```

### Menjalankan Aplikasi
```bash
./gradlew :composeApp:assembleDebug
```

### Menjalankan Unit Tests
```bash
# Unit tests (shared module)
./gradlew :shared:testDebugUnitTest
```

### Menjalankan UI Tests
```bash
# Perlu emulator/device
./gradlew :composeApp:connectedDebugAndroidTest
```

---

## Struktur File Testing

```
shared/src/androidMain/kotlin/
└── com/diwan/myprofileapp/shared/
    └── data/
        ├── FakeNoteRepository.kt      ← In-memory fake untuk test
        ├── NoteRepository.kt          ← Interface
        └── NoteRepositoryImpl.kt      ← Implementasi SQLDelight

shared/src/androidUnitTest/kotlin/
└── com/diwan/myprofileapp/shared/
    ├── data/
    │   └── NoteRepositoryTest.kt      ← 5 test cases
    └── viewmodel/
        ├── NoteViewModelTest.kt       ← 4 test cases (MockK + Turbine)
        └── ChatViewModelTest.kt       ← 2 test cases (Turbine flow test)

composeApp/src/androidMain/kotlin/
└── com/diwan/myprofileapp/
    └── util/
        └── TestTags.kt                ← Konstanta semantic tags

composeApp/src/androidInstrumentedTest/kotlin/
└── com/diwan/myprofileapp/
    └── NoteListScreenTest.kt          ← 3 UI test cases
```

---

## Repository
- **Week 8**: [Tugas8-PAM](https://github.com/Ramaaaadevs/Tugas8-PAM)
- **Week 9**: [Tugas9-PAM](https://github.com/Ramaaaadevs/Tugas8-PAM/tree/Tugas9-PAM)
- **Week 10**: [Tugas10-PAM](https://github.com/Ramaaaadevs/Tugas8-PAM/tree/Tugas10-PAM)

### Bonus Features
- ✅ Multi-turn conversation (Week 9, +5%)
- ✅ Flow test dengan Turbine