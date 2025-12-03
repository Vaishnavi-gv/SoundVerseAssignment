# Soundverse Android com.example.android_assignment.App

A modern Android application built with Kotlin, Jetpack Compose, and Firebase Cloud Messaging. This app features a chat interface, notification system, and Instagram Stories integration.

## Features

- **Chat Interface**: OpenAI-style message bubbles with smooth animations
- **Side Menu**: Slide-out menu accessible via profile icon or swipe gesture
- **Notifications**: Firebase Cloud Messaging integration with foreground alert dialogs
- **Export State Screen**: Beautiful success screen with Instagram Stories sharing
- **Smooth Animations**: Pixel-perfect UI with spring animations and transitions
- **Notification Permissions**: Proper handling of Android 13+ notification permissions

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Backend Services**: Firebase Cloud Messaging (FCM)
- **Architecture**: MVVM with ViewModel
- **Navigation**: Jetpack Navigation Compose
- **Minimum SDK**: Android 8.0 (API 26)
- **Target SDK**: Android 14 (API 34)

## Project Structure

```
app/
├── src/main/
│   ├── java/com/soundverse/app/
│   │   ├── MainActivity.kt              # Main activity with notification handling
│   │   ├── navigation/
│   │   │   └── SoundverseNavigation.kt  # Navigation setup
│   │   ├── screens/
│   │   │   ├── ChatScreen.kt            # Main chat interface
│   │   │   ├── NotificationsScreen.kt   # Notifications list
│   │   │   └── ExportStateScreen.kt     # Export success screen
│   │   ├── ui/
│   │   │   ├── components/
│   │   │   │   └── SideMenu.kt          # Side menu component
│   │   │   └── theme/                   # Theme and styling
│   │   ├── viewmodel/
│   │   │   ├── ChatViewModel.kt         # Chat state management
│   │   │   └── NotificationsViewModel.kt # Notifications state
│   │   ├── services/
│   │   │   └── FirebaseMessagingService.kt # FCM service
│   │   └── utils/
│   │       ├── NotificationPermissionHelper.kt
│   │       └── LocalNotificationHelper.kt
│   └── res/                             # Resources
└── build.gradle.kts                     # com.example.android_assignment.App dependencies
```

## Setup Instructions

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17 or later
- Android SDK with API 26+ installed
- Firebase project (for FCM)

### Firebase Setup

1. Create a Firebase project 
2. Add an Android app to your Firebase project
3. Download `google-services.json` and replace the placeholder file in `app/google-services.json`
4. Enable Cloud Messaging in Firebase Console


## Key Features Implementation

### 1. Chat Screen
- Message bubbles with different styles for user and assistant
- Smooth scroll animations
- Profile icon opens side menu
- Notification bell navigates to notifications screen

### 2. Side Menu
- Opens via profile icon click
- Swipe right to open, swipe left to close
- Smooth spring animations
- Overlay with alpha animation

### 3. Notifications
- FCM integration for push notifications
- Foreground notification alert dialog
- Notification permission handling (Android 13+)
- Clicking notification navigates to Export State screen

### 4. Export State Screen
- Success animation with scale effect
- Instagram Stories sharing button
- "Done" button navigates back to notifications

### 5. Instagram Sharing
- Opens Instagram app with intent
- Falls back to browser if Instagram not installed
- Prepares video content for Stories

## Permissions

The app requires the following permissions:
- `INTERNET` - For network requests
- `POST_NOTIFICATIONS` - For displaying notifications (Android 13+)
- `VIBRATE` - For notification vibration
