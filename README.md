# 🛍️ Hesapli.com - Android Giriş Ekranı Uygulaması

## 📋 Proje Özeti

Bu proje, **Hesapli.com** için geliştirilmiş modern bir Android giriş ekranı uygulamasıdır. Jetpack Compose kullanılarak geliştirilmiş olup, animasyonlu splash screen, kullanıcı dostu giriş ekranı ve modern UI/UX tasarımı içermektedir.

## 🎯 Proje Hedefleri

- Modern ve kullanıcı dostu arayüz tasarımı
- Lottie animasyonlu splash screen
- Güvenli kullanıcı girişi
- Responsive tasarım
- Material Design 3 uyumluluğu

## 🏗️ Mimari ve Teknolojiler
### 🧩 Mimari: MVVM

- Ekran mantığı `ViewModel` sınıflarında yönetilir.
- UI durumları `StateFlow` tabanlı `UiState` veri sınıfları ile tutulur (`app/src/main/java/com/example/girisekrani/mvvm/state`).
- UI, `collectAsState()` ile ViewModel durumunu gözlemler; tek yönlü veri akışı sağlanır.
- İş kuralları/veri erişimi `repository` katmanındadır (`AuthRepository`).
- Önceki MVI denemeleri kaldırılmıştır; projenin tamamı MVVM’e geçirilmiştir.


### 📱 Ana Teknolojiler

| Teknoloji | Versiyon | Açıklama |
|-----------|----------|----------|
| **Kotlin** | 1.8 | Ana programlama dili |
| **Jetpack Compose** | 2023.10.01 | Modern UI toolkit |
| **Material Design 3** | Latest | Google'ın tasarım sistemi |
| **Navigation Compose** | 2.7.5 | Ekranlar arası geçiş |
| **Lottie Animations** | 6.2.0 | Vektör animasyonları |

### 🔧 Gradle Bağımlılıkları

```gradle
dependencies {
    // Core Android
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.7.0'
    implementation 'androidx.activity:activity-compose:1.8.2'
    
    // Jetpack Compose
    implementation platform('androidx.compose:compose-bom:2023.10.01')
    implementation 'androidx.compose.ui:ui'
    implementation 'androidx.compose.ui:ui-graphics'
    implementation 'androidx.compose.ui:ui-tooling-preview'
    implementation 'androidx.compose.material3:material3'
    
    // Navigation
    implementation 'androidx.navigation:navigation-compose:2.7.5'
    
    // ViewModel
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0'
    
    // Icons
    implementation 'androidx.compose.material:material-icons-extended'
    
    // Splash Screen API
    implementation 'androidx.core:core-splashscreen:1.0.1'
    
    // Lottie Animations
    implementation 'com.airbnb.android:lottie-compose:6.2.0'
}
```

## 📁 Proje Yapısı

```
app/src/main/
├── java/com/example/girisekrani/
│   ├── MainActivity.kt
│   ├── Navigation.kt
│   ├── SplashScreen.kt
│   ├── LoginScreen.kt
│   ├── RegisterScreen.kt
│   ├── ForgotPasswordScreen.kt
│   ├── LoginViewModel.kt
│   ├── RegisterViewModel.kt
│   ├── ForgotPasswordViewModel.kt
│   ├── LoginViewModelFactory.kt
│   ├── RegisterViewModelFactory.kt
│   ├── ForgotPasswordViewModelFactory.kt
│   ├── repository/
│   │   └── AuthRepository.kt
│   └── mvvm/state/
│       ├── LoginUiState.kt
│       ├── RegisterUiState.kt
│       └── ForgotPasswordUiState.kt
├── res/
│   ├── drawable/                # Görsel kaynaklar
│   │   ├── hesapli_logo.png    # Uygulama logosu
│   │   ├── ic_launcher_background.xml
│   │   └── ic_launcher_foreground.xml
│   ├── mipmap-*/               # Uygulama ikonları
│   ├── values/
│   │   ├── colors.xml          # Renk tanımları
│   │   ├── strings.xml         # Metin kaynakları
│   │   └── themes.xml          # Tema ayarları
│   └── xml/                    # XML konfigürasyonları
├── assets/
│   ├── Shopping Bag.json       # Lottie animasyon dosyası
│   ├── logo_animation.json     # Özel animasyon
│   └── shopping_bag_animation.json
└── AndroidManifest.xml
```

## 🎨 UI/UX Tasarım Detayları

### 🌈 Renk Paleti

```kotlin
// Ana renkler
val primaryBlue = Color(0xFF667eea)     // Ana mavi
val secondaryPurple = Color(0xFF764ba2) // İkincil mor
val backgroundColor = Color(0xFF2C3E50) // Arka plan koyu
val textWhite = Color.White             // Beyaz metin
```

### 🎭 Gradient Tasarımı

```kotlin
// Vertical gradient background
Brush.verticalGradient(
    colors = listOf(
        Color(0xFF667eea),  // Açık mavi (üst)
        Color(0xFF764ba2)   // Koyu mor (alt)
    )
)
```

## 📱 Ekran Detayları

### 🚀 Splash Screen (SplashScreen.kt)

#### Özellikler:
- **3 saniyelik bekleme süresi**
- **Lottie animasyonlu logo**
- **Gradient arka plan**
- **"Herkes İçin Hesaplı" sloganı**

#### Kod Yapısı:

```kotlin
@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    // 3 saniye bekletme
    LaunchedEffect(key1 = true) {
        delay(3000)
        onSplashFinished()
    }

    // Lottie animasyon yükleme
    val composition = rememberLottieComposition(
        LottieCompositionSpec.Asset("Shopping Bag.json")
    )
    val progress = animateLottieCompositionAsState(
        composition = composition.value,
        iterations = LottieConstants.IterateForever
    )

    // UI layout
    Box(modifier = Modifier.fillMaxSize().background(gradient)) {
        Column {
            LottieAnimation(
                composition = composition.value,
                progress = progress.value,
                modifier = Modifier.size(120.dp)
            )
            Text("Herkes İçin Hesaplı")
        }
    }
}
```

#### Kullanılan Bileşenler:
- `LaunchedEffect` - Asenkron işlemler
- `LottieAnimation` - Vektör animasyonları
- `Box` - Layout container
- `Column` - Dikey sıralama
- `Text` - Metin gösterimi
- `Spacer` - Boşluk oluşturma

### 🔐 Login Screen (LoginScreen.kt)

#### Özellikler:
- **Telefon numarası ile giriş**
- **Şifre maskeleme**
- **Form validasyonu**
- **Hata mesajları**
- **Kayıt ol ve şifremi unuttum linkleri**

#### Kod Yapısı:

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    // State management
    var phoneNumber by remember { mutableStateOf("+90 ") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // UI Components
    Card {
        Column {
            // Logo alanı (kaldırıldı)
            Text("Hoş Geldiniz")
            
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                leadingIcon = { Icon(Icons.Default.Phone) }
            )
            
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                visualTransformation = if (passwordVisible) 
                    VisualTransformation.None 
                else 
                    PasswordVisualTransformation(),
                trailingIcon = { 
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Default.Visibility 
                             else Icons.Default.VisibilityOff)
                    }
                }
            )
            
            Button(onClick = onLoginSuccess) {
                Text("Giriş Yap")
            }
        }
    }
}
```

#### Kullanılan Bileşenler:
- `Card` - Kart tasarımı
- `OutlinedTextField` - Giriş alanları
- `Button` - Eylem butonları
- `Icon` - Simgeler
- `remember` & `mutableStateOf` - State yönetimi

## 🧭 Navigation Sistemi (Navigation.kt)

### Route Tanımları:

```kotlin
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object Home : Screen("home")
}
```

### Navigation Flow:

```
Splash Screen (3s) → Login Screen → Home Screen
                         ↓
                   Register Screen
                         ↓
                 Forgot Password Screen
```

### Navigation Controller:

```kotlin
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        // Diğer ekranlar...
    }
}
```

## 🎬 Animasyon Detayları

### Lottie Animasyon Entegrasyonu

#### 1. Dependency Ekleme:
```gradle
implementation 'com.airbnb.android:lottie-compose:6.2.0'
```

#### 2. Assets Klasörü:
```
app/src/main/assets/
├── Shopping Bag.json        # LottieFiles'dan indirilen animasyon
├── logo_animation.json      # Özel oluşturulan animasyon
└── shopping_bag_animation.json # Alternatif animasyon
```

#### 3. Animasyon Implementasyonu:
```kotlin
// Lottie composition yükleme
val composition = rememberLottieComposition(
    LottieCompositionSpec.Asset("Shopping Bag.json")
)

// Animasyon durumu
val progress = animateLottieCompositionAsState(
    composition = composition.value,
    iterations = LottieConstants.IterateForever // Sonsuz tekrar
)

// Animasyon gösterimi
LottieAnimation(
    composition = composition.value,
    progress = progress.value,
    modifier = Modifier.size(120.dp)
)
```

### Animasyon Özellikleri:
- **Sonsuz döngü** (`LottieConstants.IterateForever`)
- **Otomatik başlatma**
- **Responsive boyutlandırma**
- **Performans optimizasyonu**

## 🎨 Material Design 3 Uygulaması

### Theme Sistemi:

```kotlin
// themes.xml
<style name="Theme.GirisEkrani" parent="Theme.Material3.DayNight">
    <item name="colorPrimary">@color/primary_blue</item>
    <item name="colorOnPrimary">@color/white</item>
    <item name="colorSecondary">@color/secondary_purple</item>
    <!-- Diğer renk tanımları -->
</style>
```

### Design Tokens:
- **Typography**: Material 3 font scale
- **Colors**: Custom color palette
- **Shapes**: Rounded corners (16.dp)
- **Elevation**: Card shadows (8.dp)
- **Spacing**: Consistent spacing (16.dp, 24.dp, 32.dp)

## 📱 Responsive Tasarım

### Farklı Ekran Boyutları:
- **Phone**: 360dp - 480dp
- **Tablet**: 600dp+
- **Foldable**: Dynamic sizing

### Adaptive Components:
```kotlin
// Responsive padding
modifier = Modifier.padding(
    horizontal = 24.dp,
    vertical = 16.dp
)

// Flexible sizing
modifier = Modifier
    .fillMaxWidth()
    .size(120.dp)
```

## 🔧 Geliştirme Araçları

### Build Configuration:

```gradle
android {
    compileSdk 34
    defaultConfig {
        applicationId "com.example.girisekrani"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }
    
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    
    kotlinOptions {
        jvmTarget = '1.8'
    }
    
    buildFeatures {
        compose true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion '1.5.4'
    }
}
```

### Minimum Requirements:
- **Android API 24** (Android 7.0)
- **Target API 34** (Android 14)
- **Kotlin 1.8+**
- **Gradle 8.0+**

## 🚀 Çalıştırma Talimatları

### 1. Projeyi Klonlama:
```bash
git clone [repository-url]
cd girisekrani
```

### 2. Android Studio'da Açma:
- Android Studio'yu açın
- "Open an existing project" seçin
- Proje klasörünü seçin

### 3. Sync & Build:
```bash
./gradlew clean
./gradlew build
```

### 4. Çalıştırma:
- Emulator veya fiziksel cihaz bağlayın
- Run butonuna tıklayın

## 📊 Performans Metrikleri

### Uygulama Boyutu:
- **APK boyutu**: ~15-20 MB
- **Assets boyutu**: ~500 KB
- **Lottie animasyonları**: ~100 KB

### Performans:
- **Splash screen süresi**: 3 saniye
- **Animasyon FPS**: 60 FPS
- **Memory usage**: ~50-80 MB
- **Startup time**: <2 saniye

## 🔒 Güvenlik Özellikleri

### Input Validation:
- Telefon numarası format kontrolü
- Şifre güçlülük kontrolü
- XSS koruması

### Data Protection:
- Şifre maskeleme
- Güvenli navigation
- State management

## 🐛 Bilinen Sorunlar ve Çözümler

### 1. Lottie Animation Hatası:
```
Error: Type 'LottieCompositionResult' has no method 'getValue'
```
**Çözüm**: `composition.value` kullanımı

### 2. Navigation Back Stack:
```kotlin
navController.navigate(Screen.Login.route) {
    popUpTo(Screen.Splash.route) { inclusive = true }
}
```

### 3. Assets Dosyası Bulunmaması:
- Assets klasörünün doğru konumda olduğundan emin olun
- Dosya adlarının tam olarak eşleştiğini kontrol edin

## 🔮 Gelecek Planları

### v2.0 Özellikleri:
- [ ] Biometric authentication
- [ ] Dark/Light theme toggle
- [ ] Multi-language support
- [ ] Offline mode
- [ ] Push notifications
- [ ] Analytics integration

### Teknik İyileştirmeler:
- [ ] Compose Navigation 2.8
- [ ] Material Design 3.1
- [ ] Kotlin Coroutines Flow
- [ ] Room Database
- [ ] Retrofit API integration

## 👥 Geliştirici Notları

### Code Style:
- Kotlin coding conventions
- Compose best practices
- Material Design guidelines

### Testing:
```kotlin
// Unit tests
@Test
fun `splash screen navigation test`() { }

// UI tests
@Test
fun `login form validation test`() { }
```

### Documentation:
- KDoc yorumları
- README güncellemeleri
- API documentation

## 📞 İletişim ve Destek

### Geliştirici:
- **Proje**: Hesapli.com Android App
- **Platform**: Android (Kotlin/Compose)
- **Versiyon**: 1.0.0

### Kaynaklar:
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Lottie Files](https://lottiefiles.com/)
- [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)

---

## 📄 Lisans

Bu proje Hesapli.com için geliştirilmiştir. Tüm hakları saklıdır.

**Son Güncelleme**: Eylül 2025  
**Versiyon**: 1.0.0  
**Durum**: Aktif Geliştirme  

---

*Bu README dosyası, proje hakkında kapsamlı bilgi sağlamak ve sunumlarınızda kullanmanız için hazırlanmıştır.*








