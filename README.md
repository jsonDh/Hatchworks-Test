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
  
## Documentation

The project includes documentation to help developers understand the codebase and project structure. The `docs` folder contains detailed documentation generated from code comments using tools like KDoc and Markdown.

### Contents of the `docs` folder:

- **ViewModel Documentation:** Includes documentation for the ViewModel classes, their methods, and states.
- **Repository Documentation:** Contains documentation for the repository interfaces and implementations.
- **Other Modules Documentation:** Additional documentation for specific modules or components of the project.

Feel free to explore the `docs` folder to gain insights into the project architecture and implementation details.

## Usage
- Upon launching the app, you will see a list of Rick & Morty characters.
- Click on a character to view more details.
- Use the app in both day and night modes to experience the different themes.
- Rotate the screen to see how the app handles configuration changes.


## Screenshots

### Characters List Empty View
![Screenshot_20240524-111206](https://github.com/jsonDh/Hatchworks-Test/assets/10732606/ac4fb4b0-7c95-4da7-8afb-d00937b1ad91)
![Screenshot_20240524-112914](https://github.com/jsonDh/Hatchworks-Test/assets/10732606/d701dac7-3501-49b8-b3e6-140e4d06291c)

### Characters List
![Screenshot_20240524-111217](https://github.com/jsonDh/Hatchworks-Test/assets/10732606/0123eb63-a9a6-45c9-82ea-9857923c21bf)
![Screenshot_20240524-112919](https://github.com/jsonDh/Hatchworks-Test/assets/10732606/9dfa53c8-b68f-4e4f-8c76-5f7897a3e59d)

### Character Details Empty View
![Screenshot_20240524-111221](https://github.com/jsonDh/Hatchworks-Test/assets/10732606/22438c79-f0bc-46ca-8b03-fba6c8bec628)
![Screenshot_20240524-112931](https://github.com/jsonDh/Hatchworks-Test/assets/10732606/848df2f4-3990-4a52-be78-695cb3c91311)

### Character Details
![Screenshot_20240524-111225](https://github.com/jsonDh/Hatchworks-Test/assets/10732606/be933eb9-4a47-4b55-bc2b-0d15c4beb542)
![Screenshot_20240524-112925](https://github.com/jsonDh/Hatchworks-Test/assets/10732606/f06077f5-6b38-44a6-a131-b5501f32906e)


