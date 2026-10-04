# 🛍️ Spire E-Commerce Android App (Offline-First)

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
