# MyNewsAppKt

MyNewsAppKt is a Kotlin MVVM showcase that wraps the [NewsAPI.org](https://newsapi.org) REST endpoints inside a modern Android stack (Jetpack Navigation, Room, Retrofit, Coroutines, and Material components). The app lets users browse top headlines, run keyword searches, read full articles in-app, and keep a personal “saved” reading list that persists offline.

---

## At A Glance
- **Architecture**: Single-module Android app using MVVM + Repository + Room.
- **UI Flow**: `NewsActivity` hosts a `NavHostFragment` whose `BottomNavigationView` switches between Breaking, Search, Saved, and Article detail screens.
- **Data Flow**: Retrofit `NewsAPI` → `NewsRepository` → `NewsViewModel` (LiveData + `Resource`) → Fragments/Adapter → RecyclerViews. Saved items go through Room (`ArticleDatabase`, `ArticleDao`).
- **Offline**: Saved articles survive restarts; network requests are guarded by `hasInternetConnection()`.
- **Asynchrony**: Kotlin coroutines (`viewModelScope`) for network/database calls; `AsyncListDiffer` for RecyclerView diffing; debounced search via coroutine `Job`.

---

## Project Structure
```
app/src/main/java/com/example/mymvvmnewsapp
├── adapters/NewsAdapter.kt               # RecyclerView adapter with AsyncListDiffer
├── api/                                  # Retrofit layer
│   ├── NewsAPI.kt                        # Endpoint definitions
│   └── RetrofitInstance.kt               # Retrofit + OkHttp + logging
├── db/                                   # Room persistence
│   ├── ArticleDao.kt
│   ├── ArticleDatabase.kt
│   └── Converters.kt                     # Source <-> String
├── models/                               # DTOs/entities shared by API, Room, UI
├── repository/NewsRepository.kt          # Single source of truth
├── ui/                                   # Activity, ViewModel, Nav fragments
│   ├── NewsActivity.kt                   # Hosts bottom nav + NavHostFragment
│   ├── NewsViewModel.kt                  # Business logic, pagination, state
│   ├── NewsViewModelProviderFactory.kt
│   └── fragments/                        # Breaking, Search, Saved, Article
├── util/                                 # Constants + Resource sealed class
└── NewsApplication.kt                    # Application scope for connectivity
```

### Key Components
- `NewsActivity`: Entry point. Creates `NewsRepository`, wires `NewsViewModel` via `NewsViewModelProviderFactory`, and binds `BottomNavigationView` to the `NavController` declared in `news_nav_graph.xml`.
- `NewsViewModel`: Orchestrates fetching, pagination bookkeeping, debounced search, database mutations, and network safety (`safeBreakingNewsCall`, `safeSearchNewsCall`). Exposes `MutableLiveData<Resource<NewsResponse>>` for UI.
- `NewsRepository`: Thin abstraction that delegates to `RetrofitInstance.api` for remote data and to `ArticleDao` for persistence.
- `NewsAdapter`: `RecyclerView.Adapter` that uses Glide for thumbnails and exposes `setOnItemClickListener` for navigation hooks.
- Fragments:
  - `BreakingNewsFragment`: Shows top headlines with endless scroll + progress bar.
  - `SearchNewsFragment`: Debounced text search, same pagination UX.
  - `SavedNewsFragment`: Room-backed list with swipe-to-delete + undo and type-safe SafeArgs navigation to detail.
  - `ArticleNewsFragment`: Displays article URL inside `WebView` and offers a FAB to persist the article.
- Room stack (`Article`, `ArticleDao`, `ArticleDatabase`, `Converters`): stores serialized articles locally so users can revisit content offline.
- Retrofit stack (`NewsAPI`, `RetrofitInstance`, `Constants`): handles HTTP calls, query params, and API keys with logging for debugging.
- Utilities:
  - `Resource<T>`: Encapsulates Loading/Success/Error states, minimizing duplicated UI state handling.
  - `Constants`: API key, base URL, pagination size, debounce delay.

---

## Data & UI Workflow
1. **App launch** (`NewsActivity.onCreate`):
   - Build `ArticleDatabase` and `NewsRepository`.
   - Instantiate `NewsViewModel` (via factory) and inject into fragments through the host activity.
   - Inflate `activity_news.xml`, attach `BottomNavigationView` to Navigation component.

2. **Breaking news feed** (`BreakingNewsFragment`):
   - Subscribes to `viewModel.breakingNews`.
   - On `Resource.Loading`, shows `paginationProgressBar`.
   - On `Resource.Success`, submits the merged article list to `NewsAdapter` and updates pagination flags (`isLastPage`, `isLoading`).
   - Scroll listener triggers `viewModel.getBreakingNews("us")` when the user nears the end.

3. **Search flow** (`SearchNewsFragment`):
   - Debounces input using a coroutine `Job` + `SEARCH_NEWS_TIME_DELAY`.
   - Calls `viewModel.searchNews(query)`; observes `searchNews` LiveData with the same loading/error UI pattern as Breaking news.
   - Paginated scroll fetches additional pages for the current query.

4. **Article detail** (`ArticleNewsFragment`):
   - Receives `Article` via SafeArgs (from Breaking/Search/Saved).
   - Loads the URL inside a `WebView`.
   - Floating action button persists the article by calling `viewModel.saveArticle(article)` and shows confirmation via `Snackbar`.

5. **Saved news** (`SavedNewsFragment`):
   - Observes `viewModel.getSavedNews()` (a `LiveData<List<Article>>` from Room).
   - RecyclerView supports swipe gestures through `ItemTouchHelper`; deletes use `viewModel.deleteArticle`, with undo by re-saving.

6. **Networking + Resilience**:
   - `safeBreakingNewsCall` / `safeSearchNewsCall` guard each remote call with `hasInternetConnection()`, emit descriptive `Resource.Error` messages, and catch `IOException` separately from other exceptions.
   - Retrofit client logs request/response bodies to help debugging.

7. **Pagination mechanics**:
   - Response handlers (`handleBreakingNewsResponse`, `handleSearchNewsResponse`) increment `page` counters and merge new `articles` into the cached mutable list, enabling endless scroll while keeping the latest dataset in memory.

---

## How To Run
1. **Prerequisites**:
   - Android Studio Giraffe or newer.
   - Android SDK 34+, Kotlin plugin.
   - A valid NewsAPI API key (replace `Constants.API_KEY` with your own to avoid rate limits).

2. **Steps**:
   - Clone or copy the repo to your machine.
   - Open the `MyMVVMNewsApp` folder in Android Studio.
   - Let Gradle sync; ensure the API key is set in `app/src/main/java/.../util/Constants.kt`.
   - Connect a device or launch an emulator running Android 8.0 (API 26) or higher.
   - Click “Run” to install and start the app.

3. **Optional tweaks**:
   - Change the default country code in `NewsViewModel.getBreakingNews("us")`.
   - Update `QUERY_PAGE_SIZE` to test different pagination sizes.
   - Replace the `slide_in/slide_out` animations under `res/anim` if you want custom transitions.

---

## Testing & Validation
- Instrumentation/Unit tests live under `app/src/androidTest` and `app/src/test`.
- Current sample tests are placeholders (`ExampleInstrumentedTest`, `ExampleUnitTest`). Extend them to cover:
  - `NewsViewModel` pagination state changes.
  - `ArticleDao` CRUD operations via an in-memory Room database.
  - Navigation flows with the FragmentScenario APIs.

---

## Extending The App
- Add categories (business, sports, etc.) by extending `NewsAPI` endpoints and augmenting the bottom navigation.
- Introduce caching strategies (e.g., store breaking news in Room and expose it when offline).
- Replace `WebView` with Chrome Custom Tabs for a richer reading experience.
- Hook into WorkManager to refresh headlines periodically.

---

## Troubleshooting
- **HTTP 401**: API key missing/invalid—update `Constants.API_KEY`.
- **Empty lists**: NewsAPI free tier may block certain queries; inspect logcat (OkHttp logging is enabled).
- **No network**: `hasInternetConnection()` currently checks Wi-Fi/Cellular/Ethernet transports; add VPN/other transports if needed.

---

Happy hacking! Feel free to adapt this project as a learning playground for MVVM + Jetpack best practices.