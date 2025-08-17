# Kontest Buddy - Architecture Documentation

## 🏗️ Refactored Architecture Overview

The Kontest Buddy application has been refactored to follow clean architecture principles and standard Android development patterns. This document outlines the new structure and how to work with it.

## 📁 New Project Structure

```
📁 app/src/main/java/kontestbuddybyamitmaity/example/kontestbuddy/
├── 📁 data/                           # Data Layer
│   ├── 📁 api/                        # API Services
│   │   └── APIServices.kt             # Network API calls
│   ├── 📁 local/                      # Local Data Sources
│   │   └── SharedPrefServices.kt      # SharedPreferences wrapper
│   ├── 📁 model/                      # Data Models
│   │   ├── User.kt                    # User entity
│   │   ├── UserStats.kt               # User statistics
│   │   ├── QuestionStats.kt           # Question statistics
│   │   └── RankingModel.kt            # Ranking information
│   └── 📁 repository/                 # Repository Layer
│       └── UserRepository.kt          # Main repository for data operations
├── 📁 domain/                         # Domain Layer
│   └── 📁 usecase/                    # Use Cases
│       ├── GetUserStatsUseCase.kt     # Get user statistics
│       ├── GetLeetCodeStatsUseCase.kt # Get LeetCode stats
│       └── GetAllPublicUsersUseCase.kt # Get public users
├── 📁 presentation/                   # Presentation Layer
│   ├── 📁 ui/                         # UI Components
│   │   ├── 📁 activity/               # Activities
│   │   ├── 📁 fragment/               # Fragments
│   │   └── 📁 adapter/                # RecyclerView Adapters
│   └── 📁 viewmodel/                  # ViewModels
│       ├── HomeViewModel.kt           # Home screen ViewModel
│       └── FriendsViewModel.kt        # Friends screen ViewModel
├── 📁 utils/                          # Utilities
│   ├── Constants.kt                   # App constants
│   └── Utils.kt                       # Utility functions
└── 📁 di/                             # Dependency Injection
    └── AppModule.kt                   # Dependency injection module
```

## 🎯 Architecture Layers

### 1. **Data Layer** (`data/`)
The data layer is responsible for managing data sources and implementing data operations.

#### **API Package** (`data/api/`)
- **APIServices.kt**: Handles all network API calls to external services
- Uses OkHttp for HTTP requests
- Supports LeetCode, CodeForces, and CodeChef APIs

#### **Local Package** (`data/local/`)
- **SharedPrefServices.kt**: Manages local data storage using SharedPreferences
- Handles user preferences, cached data, and offline storage

#### **Model Package** (`data/model/`)
- **User.kt**: Represents user information
- **UserStats.kt**: Represents user statistics from different platforms
- **QuestionStats.kt**: Represents LeetCode question statistics
- **RankingModel.kt**: Represents ranking information

#### **Repository Package** (`data/repository/`)
- **UserRepository.kt**: Central repository for all user-related data operations
- Implements repository pattern for data abstraction
- Handles both local and remote data sources

### 2. **Domain Layer** (`domain/`)
The domain layer contains business logic and use cases.

#### **Use Cases** (`domain/usecase/`)
- **GetUserStatsUseCase.kt**: Retrieves user statistics from multiple platforms
- **GetLeetCodeStatsUseCase.kt**: Fetches LeetCode-specific statistics
- **GetAllPublicUsersUseCase.kt**: Retrieves all public users for social features

### 3. **Presentation Layer** (`presentation/`)
The presentation layer handles UI logic and user interactions.

#### **ViewModels** (`presentation/viewmodel/`)
- **HomeViewModel.kt**: Manages home screen data and business logic
- **FriendsViewModel.kt**: Manages friends screen data and operations

#### **UI Components** (`presentation/ui/`)
- **Activities**: Main UI screens
- **Fragments**: Reusable UI components
- **Adapters**: RecyclerView adapters for lists

### 4. **Utilities** (`utils/`)
- **Constants.kt**: Application constants and configuration
- **Utils.kt**: Utility functions and helper methods

### 5. **Dependency Injection** (`di/`)
- **AppModule.kt**: Provides dependencies for the application

## 🔄 Data Flow

### 1. **User Statistics Flow**
```
UI (Fragment/Activity) 
    ↓
ViewModel 
    ↓
UseCase 
    ↓
Repository 
    ↓
API/Local Data Sources
```

### 2. **Example: Loading User Stats**
1. **Fragment** calls `viewModel.refreshUserStats()`
2. **ViewModel** executes `GetUserStatsUseCase`
3. **UseCase** calls `UserRepository.fetchUserStats()`
4. **Repository** fetches data from API and local storage
5. **Data** flows back through the layers to update UI

## 🛠️ Key Benefits of New Architecture

### **1. Separation of Concerns**
- Each layer has a specific responsibility
- Business logic is separated from UI logic
- Data operations are abstracted through repositories

### **2. Testability**
- Use cases can be easily unit tested
- ViewModels can be tested independently
- Repository pattern allows for easy mocking

### **3. Maintainability**
- Clear structure makes code easier to understand
- Changes in one layer don't affect others
- Consistent patterns across the application

### **4. Scalability**
- Easy to add new features
- Simple to modify existing functionality
- Clear extension points

## 📝 Migration Guide

### **For Existing Code**
1. **Activities**: Move to `presentation/ui/activity/`
2. **Fragments**: Move to `presentation/ui/fragment/`
3. **Adapters**: Move to `presentation/ui/adapter/`
4. **Models**: Move to `data/model/`
5. **Services**: Move to `data/api/` or `data/local/`

### **For New Features**
1. Create data models in `data/model/`
2. Add repository methods in `data/repository/`
3. Create use cases in `domain/usecase/`
4. Add ViewModels in `presentation/viewmodel/`
5. Update UI components in `presentation/ui/`

## 🔧 Dependency Injection

The application uses manual dependency injection through `AppModule.kt`:

```kotlin
// Example usage in Activity/Fragment
val homeViewModel = AppModule.provideHomeViewModel(this)
val friendsViewModel = AppModule.provideFriendsViewModel(this)
```

## 🧪 Testing Strategy

### **Unit Tests**
- Test use cases independently
- Mock repositories for ViewModel testing
- Test data models and utilities

### **Integration Tests**
- Test repository implementations
- Test API service integration
- Test local storage operations

### **UI Tests**
- Test ViewModels with LiveData
- Test fragment/activity interactions
- Test user flows

## 🚀 Best Practices

### **1. Repository Pattern**
- Always access data through repositories
- Don't call APIs directly from ViewModels
- Use local storage for caching

### **2. Use Cases**
- Keep use cases focused and single-purpose
- Don't put business logic in ViewModels
- Use cases should be testable

### **3. ViewModels**
- Use LiveData for reactive UI updates
- Handle loading and error states
- Don't hold references to Views

### **4. Data Models**
- Use data classes for models
- Keep models simple and focused
- Use nullable types appropriately

## 📚 Additional Resources

- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture)
- [Repository Pattern](https://martinfowler.com/eaaCatalog/repository.html)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [MVVM Pattern](https://developer.android.com/jetpack/guide#recommended-app-arch)

---

This architecture provides a solid foundation for the Kontest Buddy application, making it more maintainable, testable, and scalable for future development.
