# RS Authenticator App

RS Authenticator is a secure and feature-rich authentication application that allows users to generate time-based OTPs (TOTP), manage authentication codes, and ensure app security with biometric and PIN-based unlocking.

## 🚀 Features

- **🔐 Authentication System:** User Login, Registration, and Forgot Password functionality.
- **🔑 App Security:** Unlock/Lock using PIN or Biometrics.
- **🕒 Authenticator Code Generation:** Supports TOTP-based 2FA authentication.
- **🗄️ Database Preservation:** Securely stores authentication data with SQLite.
- **📸 QR Code Scanning:** Easily scan QR codes to add authentication accounts.

## 📱 Screenshots

(Include screenshots here to showcase the app's UI)

## 🛠️ Installation

### Prerequisites
- Android Studio (Latest Version)
- Kotlin & Jetpack Compose Support

### Clone the Repository
```sh
 git clone https://github.com/rasel-mahmud-dev/rs-authenticator-app.git
 cd rs-authenticator-app
```

### Open in Android Studio
1. Open **Android Studio**
2. Click **Open an existing project**
3. Select the **rs-authenticator-app** folder
4. Wait for Gradle to sync dependencies

### Build & Run
- Connect a physical device or start an emulator
- Click on **Run ▶️** to launch the app


## 📜 Usage Guide

- **Login/Register** to start using the app.
- **Scan QR Codes** to add 2FA accounts.
- **View Generated OTPs** securely within the app.
- **Enable App Lock** for additional security.

## 👨‍💻 Technologies Used

- **Language:** Kotlin
- **Framework:** Jetpack Compose
- **Database:** SQLite
- **Authentication:** Server
- **Navigation:** Jetpack Navigation
- **UI Components:** Material 3 + Custom components.

## Project structures


screens
AboutScreen.kt
AppsScreen.kt
BackupRestore.kt
ConnectAppScreen.kt
FeaturesScreen.kt
ForgotPasswordScreen.kt
HomeScreen.kt
LoginScreen.kt
ProfileScreen.kt
RegistrationScreen.kt
ScanQRCodeScreen.kt
SecurityScreen.kt
TokenScreen.kt
TourScreen.kt
TrashScreen.kt



components
form
security
settings
AuthenticatorItem.kt
BottomSheet.kt
CustomText.kt
GradientCircularProgressIndicator.kt
HomeBottomNav.kt
MainSidebar.kt
PrimaryButton.kt
ProtectedRoute.kt
RsColumn.kt
RsIconButton.kt
RsRow.kt
ScreenHeader.kt
Toast.kt
UnlockWrapperScreen.kt

rsauthenticator
apis
components
database
dto
http
layout
screens
state
theme
utils
AppNavigation.kt
MainActivity
here my jetpack compose screen and component
 

## 📞 Contact
For any queries, reach out to **[Rasel Mahmud](https://www.linkedin.com/in/rasel-mahmud-dev)**

---

**Happy Coding! 🚀**

