# 🛍️ Spire E-Commerce Android App

Project Screen short or Video
https://drive.google.com/drive/folders/1MBUtPz8Cv3VSMVSyJJPP1TjqZgy_g7vq?usp=sharing

An Android e-commerce application built with
**Kotlin**, **Jetpack Compose**, **Room Database**, and **Retrofit**. 
It satisfies all functional requirements by fetching products from the DummyJSON API and providing a fully functional, locally persisted shopping cart that works online and offline.

---

## 🌟 Features Implemented

### 1. Product Listing
- Fetches real-time products from the [DummyJSON Products API](https://dummyjson.com/docs/products).
- Displays product image, title, price, and rating.
- Handles all essential states: loading indicator, empty results, and network error fallbacks.
- Background image caching via **Coil** so product images remain visible offline.

### 2. Product Search
- Real-time search with a 300ms debounce to avoid spamming the network/database.
- Graceful offline fallback: when the device has no internet, searches through the cached catalog in Room.

### 3. Product Details
- Selecting an item displays its title, high-res images, price, rating, stock, category, brand, and full description.
- Users can add the item directly to the shopping cart with inventory checks (prevents adding more than available stock).

### 4. Shopping Cart (Offline-First)
- **Add to Cart:** Add products with instant database persistence.
- **Quantity Controls:** Increment and decrement item count with live UI updates and stock limit checks.
- **Delete Items:** Easily remove items from the cart.
- **Cart Summary:** Live calculation of total items and total price.
- **Persistent Storage:** Stored using **Room Database**—all cart items and quantities stay intact even after closing or restarting the app.

### 5. Offline Support
- **Isolated Cart & Cache Tables:**
  - `product_cart_item`: Manages the user's cart items.
  - `product_catalog_cache`: Stores the browsed catalog for offline reading.
- **Network Observer:** Real-time connectivity checks notify the user via UI messages when the internet drops or reconnects.
- Full offline cart operations: viewing items, changing quantities, deleting items, and price calculations work without any internet connection.

---

## 🏗️ Architecture & Tech Stack

The project follows the standard **MVVM (Model-View-ViewModel)** architectural pattern recommended by Google.

- **UI Layer (View):** Built entirely with Jetpack Compose. Screens observe state exposed by the ViewModel and emit user intent/events back to the ViewModel.
- **ViewModel Layer:** Handles presentation logic, debouncing search input, and managing coroutine lifecycles via `viewModelScope`. Exposes state to the UI via `StateFlow` and single-event alerts via `SharedFlow`.
- **Data/Repository Layer:** The single source of truth for the app. `ProductRepository` arbitrates between network responses and local Room database caching, managing offline fallbacks gracefully.

---

## 📦 Libraries Used

| Library / Component | Purpose |
| :--- | :--- |
| **Jetpack Compose (Material 3)** | Modern, declarative native Android UI toolkit. |
| **Room Database** | SQLite object mapping abstraction layer for offline persistence. |
| **Retrofit 2** | Type-safe HTTP networking client for REST API communication. |
| **Gson & Converter-Gson** | JSON serialization and deserialization for network and Room converters. |
| **Kotlin Coroutines** | Asynchronous execution, background threading, and concurrency. |
| **Kotlin Flow (`StateFlow`, `SharedFlow`)** | Reactive data streams connecting Room, Repository, ViewModel, and UI. |
| **Coil (Compose)** | Kotlin-first asynchronous image loading and multi-layer disk caching. |
| **Lifecycle Runtime & ViewModel Compose** | Lifecycle-aware state observation (`collectAsStateWithLifecycle`) and ViewModel bindings. |

---

## 💾 Local Storage Approach

The app uses **Room (SQLite)** with an **isolated two-table data strategy** to avoid cross-contamination between browsed items and actual cart items:

1. **`product_catalog_cache` Table (`ProductCatalogEntity`):**
   - Automatically populated whenever the app successfully fetches catalog data from the remote API.
   - Serves as the fallback database for offline catalog browsing and offline search.
   - Upserted with `OnConflictStrategy.REPLACE` so products are refreshed whenever new data arrives.

2. **`product_cart_item` Table (`ProductEntity`):**
   - Stores **only** items the user explicitly adds to their cart.
   - Keeps track of user-selected `count`, stock constraints, and cart item metadata.
   - Read reactively using Room `Flow<List<ProductEntity>>`, guaranteeing immediate UI updates across quantity changes.

3. **Room TypeConverters:**
   - Includes custom converters (`Converters.kt`) to serialize non-primitive types like `List<String>` (image URLs) into JSON strings for SQLite storage.

4. **Coil Dedicated Disk Cache:**
   - Configured with a dedicated 150 MB disk cache directory (`image_cache`) and `.respectCacheHeaders(false)` so remote product images are saved locally to physical device storage for offline viewing.

---

## 🎯 Important Design Decisions

1. **Two Separate Database Tables Over a Single Table Flag:**
   - Instead of using a single table with an `isInCart` boolean flag, we split the schema into `product_catalog_cache` and `product_cart_item`. This ensures that deleting an item from the cart never removes it from the offline browsing catalog, and syncing 100 products from the API never pollutes the cart screen.
2. **Debounced Search (`debounce(300)`):**
   - Implemented a 300ms debounce on search queries using Kotlin Flow operators (`debounce`, `distinctUntilChanged`, `collectLatest`). This prevents firing unnecessary network calls or Room queries on every single keystroke.
3. **One-Off Network Status Checks During User Actions:**
   - In user-driven search, network connectivity is queried using `networkObserver.isConnected.first()` rather than persistent `.collect { }` blocks. This avoids leaking perpetual collectors and prevents duplicate Toast alerts.
4. **SharedFlow for UI Event Messages:**
   - Used `SharedFlow` instead of `StateFlow` or raw `Context` calls inside the ViewModel for network status alerts. This guarantees that one-off events (like "Internet connection restored") are consumed once and never re-fired on configuration changes.

---

## ⚠️ Known Limitations

1. **No Background Cart Synchronization to Backend:**
   - Since the DummyJSON API is a mock/read-only testing API, the shopping cart is persisted purely on the client side. Any items added to the cart while offline are saved to local Room, but are not synchronized with an authenticated user account on a remote server.
2. **First-Launch Offline Restriction:**
   - If the app is launched for the very first time with no internet connection, the local Room catalog and image disk cache will be empty. Products will only be viewable offline once an initial online fetch has populated the local database.
3. **Pagination / Infinite Scroll:**
   - The app currently fetches the primary batch of products from DummyJSON (`/products`). Pagination with a Paging 3 library is not implemented for the initial product grid.

---

## 📂 Project Structure

```text
com.example.spirelab_pankajverma/
│
├── data/
│   ├── api/          # Retrofit API interface
│   ├── db/           # Room Database, Entities, TypeConverters, and DAOs
│   ├── item/         # Product domain data models
│   ├── repository/   # Repository managing API & Room data sources
│   ├── retrofit/     # Retrofit client instance
│   └── utility/      # NetworkObserver (ConnectivityManager)
│
├── ui/
│   ├── screen/       # Compose Screens (ProductList, CartItem, ProductDetail)
│   └── theme/        # Compose Theme & Styling
│
└── viewmodel/        # ProductViewModel and ViewModelFactory
