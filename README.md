# BuildR - PC Builder Application

BuildR is an Android application that helps users create and manage PC configurations.

## Second Milestone Completed!

The second milestone for the BuildR app has been successfully completed. The following features are now implemented:

- Firebase Firestore integration for storing and retrieving PC configurations
- Configuration builder interface for creating custom PC builds
- User-specific configuration management (view, create, edit, delete)
- Secure permission checks for configuration operations
- Swipe-to-delete functionality with confirmation
- Improved UI with support for empty states
- Pull-to-refresh for configuration updates

## First Milestone Completed!

The first milestone for the BuildR app has been successfully completed. The following features are now implemented:

- Basic application structure and navigation
- User interface with Material Design components
- Model classes for all PC components (CPU, GPU, RAM, etc.)
- Firebase authentication integration
- Initial screens (login, registration, main screen)
- Notification system with proper icon
- Project documentation

## Building the Application

To build the application, follow these steps:

1. Clone this repository:
```
git clone https://github.com/markingo/buildr.git
```

2. Open the project in Android Studio

3. Connect Firebase:
   - Create a project in Firebase Console
   - Add your Android app to the Firebase project
   - Download `google-services.json` and place it in the app module directory
   - Follow the Firebase setup instructions

4. Build the project using Gradle:
```
./gradlew assembleDebug
```

5. The APK will be generated at:
```
app/build/outputs/apk/debug/app-debug.apk
```

## Project Structure

- `app/src/main/java/com/markingo/buildr/model/` - Model classes
- `app/src/main/java/com/markingo/buildr/ui/` - UI components
- `app/src/main/java/com/markingo/buildr/util/` - Utility classes including FirestoreUtil
- `app/src/main/res/` - Resources (layouts, strings, colors, etc.)

## Technologies Used

- Java for Android development
- Firebase Authentication for user management
- Firestore for data storage and retrieval
- Material Design components for UI
- RecyclerView with SwipeRefreshLayout
- Custom adapters for list management

## Future Development

The next phase will focus on component compatibility checking, price calculation, and configuration sharing features.

## License

This project is licensed under the MIT License - see the LICENSE file for details. 