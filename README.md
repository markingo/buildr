# BuildR - PC Builder Application

BuildR is an Android application that helps users create and manage PC configurations.

## Quick Start (Test the Application)

There are two ways to test the BuildR application:

### Option 1: Download and Install the APK (Recommended)

1. Download the latest APK from the [Releases](https://github.com/markingo/buildr/releases) page
2. Install it on your Android device (you may need to enable "Install from Unknown Sources" in your settings)
3. Open the application and start building PC configurations!

### Option 2: Build from Source

To build the application from source, follow these steps:
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

## Latest Updates: Third Milestone Completed!

The BuildR app has completed its third milestone with the following new features and improvements:

- **Enhanced Security**: Improved permission controls for configuration management and removed sensitive credentials from Git history
- **Git Integration**: Added proper .gitignore settings for sensitive files including Firebase admin credentials
- **Code Refactoring**: Fixed permission check in the deleteConfiguration method ensuring only owners can delete configurations
- **UI Improvements**: Further polished the user interface and navigation flows
- **Documentation Updates**: Comprehensive README documentation for the project

### Previous Milestone (Second):

- **Component Selection**: Users can now browse and add CPUs, GPUs, and RAM to their configurations
- **Real-time Price Calculation**: Total price updates automatically as components are added
- **Power Consumption Estimation**: Estimated wattage is calculated based on component TDP values
- **Enhanced UI**: Polished user interface with Material Design components throughout the app
- **Configuration Builder**: Complete workflow for creating, editing, and saving configurations
- **Firestore Integration**: Configurations are saved to Firestore with proper permission controls
- **Back Navigation**: Added proper back button navigation throughout the app

## Milestone Requirements Implementation

### First Milestone ✅

| Requirement | Implementation Details |
|-------------|------------------------|
| No compilation errors | ✅ Project builds successfully with Gradle |
| No runtime errors | ✅ Application runs stably with robust error handling |
| Firebase Authentication | ✅ Implemented in `LoginActivity` and `RegisterActivity` with email/password authentication |
| Input field types | ✅ Password fields use `inputType="textPassword"`, email fields use `inputType="textEmailAddress"` with appropriate keyboard types |
| Layout types | ✅ `ConstraintLayout` used in all main screens with `LinearLayout` for lists and component layouts |
| Responsive design | ✅ Layouts adapt to different screen sizes and orientations using constraints, weight, and appropriate dimensions |
| Animation | ✅ Logo animation in `SplashActivity`, transition animations between activities |
| Intent navigation | ✅ All activities are accessible via intents (SplashActivity → LoginActivity → MainActivity → Configuration screens) |
| UI quality | ✅ Material Design principles with custom BuildR theme, consistent typography and spacing |

### Second Milestone ✅

| Requirement | Implementation Details |
|-------------|------------------------|
| Data model | ✅ `Configuration` and component models (`CPU`, `GPU`, `RAM`) defined and stored in Firestore |
| 4+ Activities | ✅ `SplashActivity`, `LoginActivity`, `RegisterActivity`, `MainActivity`, `ConfigurationBuilderActivity`, `ConfigurationDetailActivity`, `ComponentPickerActivity` |
| 2+ Animations | ✅ Splash screen animation, RecyclerView item animations, shared element transitions between activities |
| Lifecycle hooks | ✅ `onResume()` in `MainActivity` refreshes configurations, `onPause()` in configuration editor saves draft state |
| Permission resources | ✅ `INTERNET` and `ACCESS_NETWORK_STATE` permissions for Firebase connectivity |
| System services | ✅ `NotificationService` for alerts, `ReminderService` using AlarmManager for daily reminders |
| CRUD operations | ✅ Create, Read, Update, Delete operations on configurations in `FirestoreUtil.java` |
| Complex queries | ✅ `getUserConfigurations()` with filters, `getConfiguration()` with security checks, component queries with sorting and filtering |
| UI quality | ✅ Polished UI with consistent styling, intuitive workflows, and responsive feedback |

## Project Structure

- `app/src/main/java/com/markingo/buildr/model/` - Model classes for components and configurations
- `app/src/main/java/com/markingo/buildr/ui/` - UI components and activities
- `app/src/main/java/com/markingo/buildr/util/` - Utility classes and Firebase helpers
- `app/src/main/res/` - Resources (layouts, strings, colors, drawables, etc.)

## Technologies Used

- Java for Android development
- Firebase Authentication for user management
- Firestore for cloud data storage
- Material Design components for modern UI
- RecyclerView for efficient list displays
- Fragment-based navigation
- Vector drawables for crisp icons at any resolution

## Key Features

- **User Authentication**: Secure login and registration system
- **Configuration Management**: Create, edit, and delete PC configurations
- **Component Selection**: Browse and select from various PC components
- **Price Tracking**: Real-time calculation of total price as components are added
- **Power Estimation**: Estimated power consumption based on component specifications
- **Responsive Design**: Works across different Android device sizes

## Future Development

Future milestones will focus on:
- Compatibility checking between components
- More detailed component specifications
- Configuration sharing features
- Performance benchmarks estimation
- Extended component database

## License

This project is licensed under the MIT License - see the LICENSE file for details. 