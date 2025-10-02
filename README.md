# Metronome

A native Android application built with Kotlin and Jetpack Compose that serves as a feature-rich metronome for musicians.

## 🚀 Features

- **Adjustable Tempo**: Easily set the beats per minute (BPM) from 1 to 300.
- **Sound Customization**: Choose from a variety of sounds to personalize your practice session.
- **Visual Feedback**: A clean and intuitive UI provides visual cues for each beat.
- **Offline Capabilities**: Utilizes a local database to store preferences and settings, ensuring a seamless experience even without an internet connection.

## 🛠️ Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Architecture**: Follows modern Android architecture principles.
- **Local Database**: [Room](https://developer.android.com/training/data-storage/room)
- **Navigation**: [Jetpack Navigation for Compose](https://developer.android.com/jetpack/compose/navigation)
- **Build Tool**: [Gradle](https://gradle.org/)

## ⚙️ Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (latest version recommended)
- An Android device or emulator with API level 24 or higher.

### Installation & Setup

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/castro-bot/Metronomo.git
    ```
2.  **Open in Android Studio & Sync Gradle:**
    - Android Studio should automatically start syncing the Gradle project. If not, click the "Sync Project with Gradle Files" button in the toolbar.
3.  **Run the application:**
    - Select a run configuration (usually `app`).
    - Choose a target device (emulator or physical device).
    - Click the "Run" button.

Alternatively, you can build the project from the command line using Gradle:

```sh
# Build a debug APK
./gradlew assembleDebug

# Install the debug APK on a connected device
./gradlew installDebug
```

## 📁 Project Structure

The project follows a standard Android application structure:

```
.
├── app/                # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/metronomo/metronomo/  # Core source code
│   │   │   ├── res/                           # Resources (layouts, drawables, etc.)
│   │   │   └── AndroidManifest.xml
│   └── build.gradle.kts  # App-level build script
│
├── gradle/             # Gradle wrapper files
└── build.gradle.kts    # Top-level build script
```

## ✍️ Authors

This app was created with ❤️ by:

- Adolfo Castro 🎷
- Leonela Sornoza 🎹
- Cristopher Delgado 🥁
