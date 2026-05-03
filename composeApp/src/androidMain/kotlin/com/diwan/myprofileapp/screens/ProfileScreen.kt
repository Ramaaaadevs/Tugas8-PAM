package com.diwan.myprofileapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.diwan.myprofileapp.shared.platform.BatteryInfo
import com.diwan.myprofileapp.shared.platform.DeviceInfo
import com.diwan.myprofileapp.shared.viewmodel.ProfileViewModel
import org.koin.compose.koinInject

@Composable
fun ProfileHeader(nama: String, bio: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A237E), Color(0xFF283593))
                )
            )
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.size(100.dp).clip(CircleShape)
                    .background(Color(0xFF5C6BC0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, "Foto Profil",
                    tint = Color.White, modifier = Modifier.size(60.dp))
            }
            Text(nama, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(bio, fontSize = 14.sp, color = Color(0xFFB0BEC5),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp))
        }
    }
}

@Composable
fun InfoItem(
    icon: ImageVector,
    label: String,
    value: String,
    textColor: Color = Color(0xFF212121)
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape)
                .background(Color(0xFFE8EAF6)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, label, tint = Color(0xFF3949AB), modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Text(value, fontSize = 15.sp, color = textColor, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun ProfileCard(
    email: String,
    phone: String,
    location: String,
    cardColor: Color = Color.White,
    textColor: Color = Color(0xFF212121)
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Informasi Kontak", fontSize = 16.sp,
                fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
            Spacer(Modifier.height(4.dp))
            HorizontalDivider(color = Color(0xFFE0E0E0))
            Spacer(Modifier.height(8.dp))
            InfoItem(Icons.Default.Email,      "Email",    email,    textColor)
            InfoItem(Icons.Default.Phone,      "Phone",    phone,    textColor)
            InfoItem(Icons.Default.LocationOn, "Location", location, textColor)
        }
    }
}

@Composable
fun DeviceInfoCard(
    cardColor: Color = Color.White,
    textColor: Color = Color(0xFF212121)
) {
    val deviceInfo: DeviceInfo = koinInject()
    val batteryInfo: BatteryInfo = koinInject()

    val batteryLevel = remember { batteryInfo.getBatteryLevel() }
    val charging = remember { batteryInfo.isCharging() }
    val batteryText = "$batteryLevel%" + if (charging) " ⚡" else ""

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Informasi Perangkat", fontSize = 16.sp,
                fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
            Spacer(Modifier.height(4.dp))
            HorizontalDivider(color = Color(0xFFE0E0E0))
            Spacer(Modifier.height(8.dp))
            InfoItem(Icons.Default.Info,     "Device",  deviceInfo.getDeviceName(), textColor)
            InfoItem(Icons.Default.Settings, "OS",      deviceInfo.getOsVersion(),  textColor)
            InfoItem(Icons.Default.Star,     "Battery", batteryText,                textColor)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var showEditScreen by remember { mutableStateOf(false) }

    val bgColor   = if (uiState.isDarkMode) Color(0xFF121212) else Color(0xFFF5F5F5)
    val cardColor = if (uiState.isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (uiState.isDarkMode) Color.White      else Color(0xFF212121)

    if (showEditScreen) {
        EditProfileScreen(
            currentNama = uiState.nama,
            currentBio  = uiState.bio,
            onSave      = { nama, bio -> viewModel.saveProfile(nama, bio) },
            onBack      = { showEditScreen = false }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .verticalScroll(rememberScrollState())
    ) {
        ProfileHeader(nama = uiState.nama, bio = uiState.bio)
        Spacer(Modifier.height(20.dp))
        ProfileCard(
            email     = uiState.email,
            phone     = uiState.phone,
            location  = uiState.location,
            cardColor = cardColor,
            textColor = textColor
        )
        Spacer(Modifier.height(16.dp))
        DeviceInfoCard(cardColor = cardColor, textColor = textColor)
        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (uiState.isDarkMode) "Dark Mode" else "Light Mode",
                    fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = textColor
                )
                Switch(
                    checked = uiState.isDarkMode,
                    onCheckedChange = { viewModel.toggleDarkMode() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF1A237E)
                    )
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { viewModel.toggleFollow() },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (uiState.isFollowing) Color(0xFF5C6BC0) else Color(0xFF1A237E)
                )
            ) {
                Text(
                    if (uiState.isFollowing) "Following ✓" else "Follow",
                    fontWeight = FontWeight.Bold
                )
            }
            OutlinedButton(
                onClick = {},
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Pesan", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = { showEditScreen = true },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Edit Profile", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(24.dp))
    }
}

// ─── EDIT PROFILE ────────────────────────────────────────────────────────────

@Composable
fun EditTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true
) {
    Column {
        Text(label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            shape = RoundedCornerShape(10.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    currentNama: String,
    currentBio: String,
    onSave: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var inputNama by remember { mutableStateOf(currentNama) }
    var inputBio  by remember { mutableStateOf(currentBio) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A237E),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).padding(20.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            EditTextField("Nama Lengkap", inputNama, { inputNama = it })
            EditTextField("Bio", inputBio, { inputBio = it }, singleLine = false)
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { onSave(inputNama, inputBio); onBack() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E))
            ) {
                Text("Simpan", fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}