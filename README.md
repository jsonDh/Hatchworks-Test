# Rick & Morty Character Viewer

## Overview
Rick & Morty Character Viewer is an Android application that allows users to browse a list of characters from the Rick & Morty TV show. Users can click on a character to view detailed information about them.

## Features
- Display a list of characters from Rick & Morty
- View detailed information about each character
- Supports night and day themes
- Handles screen rotation
- Shows loading states for lists and details
- Implements a clean architecture

## Architecture
The project follows a single-activity architecture with Jetpack Compose for UI and the following key components:
- **Activity:** Hosts the navigation component.
- **Navigation Component with Compose:** Manages navigation between the list and detail screens.
- **ViewModel:** Manages UI-related data in a lifecycle-conscious way.
- **Dagger Hilt:** For dependency injection.
- **Apollo:** For making GraphQL queries.

## Tech Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Navigation:** Navigation Component with Compose
- **State Management:** ViewModel
- **Dependency Injection:** Dagger Hilt
- **Networking:** Apollo for GraphQL
- **Themes:** Day/Night support


## Setup and Installation
  
1. **Clone the Repository:**
   ```sh
   git clone https://github.com/jsonDh/Hatchworks-Test.git
   ```
2. **Open in Android Studio:**
   - Open Android Studio
   - Choose "Open an existing Android Studio project"
   - Select the project directory

3. **Build the Project:**
   - Let Android Studio download all the dependencies and build the project.

4. **Run the Application:**
   - Connect your Android device or start an emulator.
   - Click on the "Run" button in Android Studio.

## Usage
- Upon launching the app, you will see a list of Rick & Morty characters.
- Click on a character to view more details.
- Use the app in both day and night modes to experience the different themes.
- Rotate the screen to see how the app handles configuration changes.


## Screenshots

### Characters List Empty View - Dark Mode
![Screenshot_20240524-111206](https://github.com/jsonDh/Hatchworks-Test/assets/10732606/ac4fb4b0-7c95-4da7-8afb-d00937b1ad91)
### Characters List - Dark Mode
![Screenshot_20240524-111217](https://github.com/jsonDh/Hatchworks-Test/assets/10732606/0123eb63-a9a6-45c9-82ea-9857923c21bf)
### Character Details Empty View - Dark Mode
![Screenshot_20240524-111221](https://github.com/jsonDh/Hatchworks-Test/assets/10732606/22438c79-f0bc-46ca-8b03-fba6c8bec628)
### Character Details - Dark Mode
![Screenshot_20240524-111225](https://github.com/jsonDh/Hatchworks-Test/assets/10732606/be933eb9-4a47-4b55-bc2b-0d15c4beb542)
