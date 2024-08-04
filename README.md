# Destinator
Create personalized travel itineraries with the help of Google's Gemini. This project was developed as part of the [Gemini API Developer Competition](https://ai.google.dev/competition).

## How is Gemini used in this project?
Destinator leverages Gemini APIs for two primary purposes:
- **Generate travel itineraries**: Based on user inputs such as city, dates, travel preferences, and more.
- **Validate city inputs**: Ensure the city exists, correct any mistakes, and disambiguate between cities with similar names.

## Other integrations
- Google Maps API: Used to display maps and places locations.
- Google Places API: Once Gemini generates the itinerary, itinerary data is hydrated with additional information from the Places API: photos, correct location, and links to the respective Google Maps entries (in case user clicks on a place).

## Project setup
Locate your Gradle User Home Directory. This is typically located at `C:\Users\<Your Username>\.gradle` on Windows or `~/.gradle` on Unix systems.
In this location, create a file named `gradle.properties` if it does not already exist.
Add the following lines to the `gradle.properties` file:
```
G_API_KEY=<Add your Gemini API Key here>
MAPS_API_KEY=<Add your Google Maps API Key here>
```
Open the project using Android Studio. Gradle should automatically sync the project and download the required dependencies, but if it does not, click the "Sync Project with Gradle Files" button in the toolbar.