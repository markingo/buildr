# BuildR - PC Builder Application

BuildR is an Android application that helps users create and manage PC configurations.

## Latest Updates: Third Milestone Completed!

The BuildR app has completed its third milestone with the following new features and improvements:

- **Enhanced Security**: Improved permission controls for configuration management
- **Git Integration**: Added proper .gitignore settings for sensitive files
- **Code Refactoring**: Fixed permission check in the deleteConfiguration method
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

## Building the Application

To build the application, follow these steps:
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