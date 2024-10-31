# A brief app overview
Using Clean Architecture and MVVM 

Clean Architecture:
models: Acts as the Entity or Domain Model layer, holding data models like CurrencyDTO and CurrencyWithCountryName.\\
use_case: Contains the Use Cases, implementing the business logic, such as GetExchangeRateUseCase and HistoryUseCase.\\
network and repositories: The network folder represents the Data Source layer, handling API or database connections, while repositories act as intermediaries between the use_case layer and data sources.\\
presentation: This layer represents the UI / Presentation layer, managing ViewModels and other UI components.\\

MVVM (Model-View-ViewModel):\\
Model: Includes classes in models and use_case for handling data and business logic.\\
ViewModel: Contains classes in presentation, responsible for providing data to the View and handling user actions. ViewModels often call Use Cases from the use_case layer.\\
View: The user interface, which receives data from the ViewModel and updates the UI.\\

# Steps to build and run the app

# Step 1: Setup Project and Dependencies
  Create a new project in Android Studio.
  Add dependencies for Jetpack Compose, Retrofit (for network calls), Room (for local storage), and Hilt for dependency injection.
# Step 2: Define Data Models and API Service
  Define data classes for API responses (e.g., CurrencyDTO, ConversionRates).
  Set up a Retrofit API service interface (CurrencyAPI) to fetch currency conversion rates.
# Step 3: Implement the Repository
  Create a CurrencyRepository interface.
  Implement CurrencyRepositoryImpl in the data layer, which fetches data from the API or database.
# Step 4: Create Use Cases
  Write use cases for core functionalities like GetExchangeRatesUseCase and ConvertCurrencyUseCase to manage business logic.
# Step 5: Build the ViewModel
  Implement CurrencyViewModel, which uses the use cases to provide data to the UI.
  Manage UI state and error handling in the ViewModel.
# Step 6: Design the UI with Jetpack Compose
  Build composables for the currency dropdown, input fields, and conversion result.
  Use State to bind UI components with the ViewModel data.
# Step 7: Error handling like network failure and invalid input
# Step 8: Run and Test the App
Run the app on an emulator or device and test currency conversion functionality.
Check for UI responsiveness, network calls, and error handling.

# Any additional notes or challenges encountered
The difficulty is not responsiveness to be compatible with many different screens, not creating bug - free to control errors
