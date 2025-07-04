# SmartCity ![android_ci](https://github.com/nicolasjafelle/SmartCity/actions/workflows/android.yml/badge.svg)

## 🙌 Introduction

Smart City is a well organized android project aimed to deliver fast pacing development by splitting crucial development in modules and provides out of the box integration with latest Junit libs. It also adds basic CI integration with Github Actions.

## ✨ Features

- 🔍 Realtime city search sorted by City
- 🏢 Clean Architecture + MVVM + Compose
- 💾 Favorite cities locally stored
- 🗺️ Google Maps integration (including maps api key)
- ☁️ Networking with Ktor Client
- 💉 Hilt DI
- 🧪 Unit Tests with JUnit5, MockK, Turbine

## 💻 Development Requirements

* Android Studio Narwhal Feature Drop | 2025.1.2 Canary 7  
Build #AI-251.26094.121.2512.13699665, built on June 26, 2025
* Android Minimum SDK 28
* Android Compile SDK 36
* Android Build Tools 36.0.0
* Kotlin 2.2.0
* Android Gradle Plugin 8.1 or later

## 📖 How To install guide

### Clone the repository
```
Assuming github ssh is already added
~ git clone git@github.com:nicolasjafelle/SmartCity.git
```
### Android Studio
```
1. Open Android Studio
2. Go To File -> New -> Import project
3. Select Root Android project directory -> ~/SmartCity/SmartCity 
4. Wait till import and download all dependencies
5. Done
```
### Note: Maps API KEY is provided in this repo just for a short time. It will be replaced and later removed from repository. 

---
## 🧱 Tech stack

| Library | Description |
|------------|-------------|
| Kotlin | selected programming language |
| Jetpack Compose | Declarative UI |
| Hilt | Dependency Injection |
| Ktor | HTTP Client |
| ViewModel & StateFlow (UDF) | Presentation layer and state management |
| Clean Architecture | Data & Domain separation of concern | 
| Navigation Compose | Compose built-in navigation |
| JUnit 5 + MockK + Turbine | Unit Test |

---

## 🗂️ Project Structure and Modules
Following is the project structure sorted by Android Studio "Android" view
```
SmartCity Root
	|--- app 
	|--- data
	|--- design
	|--- domain
	|--- features
			|--- search
			|--- maps
	|--- foundation
	|--- maps
	|--- testlibs
```
#### Module Breakdown

* **:app**: Android Application module. Provides the entry point to initialize, deploy and launch the application. Handles everything related to specific device capabilities such as orientation, hardware specs.
* **:data**: module related to the "Data Layer" in a Clean Architecture skeleton. Provides access to Repositories but encapsulates data source layer such as API client and internal storage.
* **:design**: module related to everything UI shared components. Provides easy access to reusable components for fast UI development.
* **:domain**: module related to the "Domain Layer" in a Clean Architecture skeleton. It provides access to everything related to "domain" in your business model such as Use Cases and domain model objects.
* **:features:search**: module related to the "Presentation Layer" in a Clean Architecture skeleton. Presentation layer should be "connected" with "Domain Layer" but not with "Data Layer". This module represents everything related to search capabilities and provides how to navigate.
* **:features:maps**: module related to the "Presentation Layer" in a Clean Architecture skeleton. Presentation layer should be "connected" with "Domain Layer" but not with "Data Layer". This module represents everything related to maps capabilities and provides how to navigate.
* **:foundation**: module core which contains the core foundations of the project such as MVVM way of work, core navigation interfaces, networking client and API provider. 
* **:maps**: this module encapsulates the chosen maps platform (e.g., Google Maps, Mapbox). It provides a bridge implementation that decouples the domain and feature modules from the specific map SDK details, ensuring platform independence for mapping functionalities.
* **:testlibs**: this module provides proper implementation to be able to add Junit 5 test suite accross all modules.

## 📐 Project Architecture

Smart City's architecture is based on **Clean Architecture** with the **MVVM (Model-View-ViewModel)** pattern applied in the presentation layer. As a result, the project is well-structured **into dedicated modules** that prioritize **SOLID principles**, fostering easy understanding and maintainability of each module's purpose.

### Project's Clean Architecture diagram
![clean_arch](https://github.com/nicolasjafelle/SmartCity/blob/feature/readme/resources/clean_arch.png)

### Presentation Diagram
![ui_updates](https://github.com/nicolasjafelle/SmartCity/blob/feature/readme/resources/ui_updates.png)

## 🚆 Continous Integration

For a simple yet effective Continuous Integration (CI) workflow, this project leverages GitHub Actions. Our built-in basic pipeline is configured to automatically build the application, execute all unit tests, and perform static code analysis on every code push, ensuring immediate feedback on code quality and preventing regressions.

### KtLint - Linter tool

Ktlint is a static code analysis tool (linter) specifically designed for Kotlin code. Its primary purpose in Android development (and any Kotlin project) is to enforce a consistent coding style and identify potential code quality issues without actually compiling or running the code. 

### CI Steps

```
1. Setup environment with Java JDK 17
2. Give gradlew bash execution permissions
3. Run ktlint check to keep code style in check with code quality standards
4. Run all unit test in the project
5. Build debug variant
```

### Unit testing

Smart City is configured to build unit tests using the **JUnit 5** test suite, providing a robust framework for test execution. To facilitate advanced mocking capabilities for dependencies, we leverage **MockK** lib. Asynchronous operations and `Kotlin Flow`s are thoroughly tested using **Kotlin Coroutines Test** and **Turbine**, ensuring reliable validation of reactive streams and suspend functions.
As of today unit test are present in:
* AddFavoriteUseCase
* FetchCityListUseCase
* GetCityUseCase
* RemoveFavoriteUseCase
* SearchCityUseCase
* CityRepository

Notable missing due to lack of time:
* CitiApiClient
* CityLocalStorage
* favoriteLocalStorage
* ViewModels
