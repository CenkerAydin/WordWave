# WordWave

WordWave is an Android application designed to help users learn words in multiple languages by displaying translations between English and Turkish. Users can track learned words and navigate through a list of at least 50 words using various interactive features.

## Features

- **Word List:** Displays a list of 50+ words with English and Turkish translations.
- **Mark as Learned:** Users can mark words as 'learned' and filter them from the main list.
- **Shuffle Words:** Refresh the word list with a new random order using swipe-to-refresh.
- **View Word Details:** Tap on a word to view its detailed meaning in a popup.
- **Bottom Navigation:** Easily navigate between different sections using a Bottom Navigation View.
- **Persistent Data:** Words marked as learned are saved locally using SharedPreferences.

## Video


https://github.com/user-attachments/assets/222aa80d-b921-421c-9ec8-e021603f02c9



## Tech Stack

- **Language:** Kotlin
- **Navigation:** Jetpack Navigation Component
- **Data Persistence:** SharedPreferences
- **UI:** RecyclerView, SwipeRefreshLayout, BottomNavigationView, Dialogs
- **Design & Animations:** Material Design Components, Lottie Animations, Custom Gradient Text

## How It Works

1. **Word List:** Displays a list of words with English and Turkish translations. Users can scroll through and select words.
2. **Mark as Learned:** When a word is marked as learned, it is removed from the main word list and stored using SharedPreferences.
3. **Shuffle Feature:** Swipe down on the main screen to shuffle the order of words.
4. **Word Details:** Clicking on a word shows detailed information in a popup dialog.

## Future Enhancements

- Add support for more languages.
- Implement a quiz feature to test word knowledge.
- Add user authentication and cloud sync for learned words.

## Ongoing Development
WordWave will continue to receive updates and improvements. I will keep enhancing the app with new features and optimizations in the future.
