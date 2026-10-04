package com.example.spirelab_pankajverma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spirelab_pankajverma.data.utility.Constant
import com.example.spirelab_pankajverma.data.db.DatabaseProvider
import com.example.spirelab_pankajverma.data.item.Product
import com.example.spirelab_pankajverma.data.repository.ProductRepository
import com.example.spirelab_pankajverma.data.retrofit.ProductRetrofitApi
import com.example.spirelab_pankajverma.data.utility.NetworkObserver
import com.example.spirelab_pankajverma.ui.screen.CartItem
import com.example.spirelab_pankajverma.ui.screen.ProductDetail
import com.example.spirelab_pankajverma.ui.screen.ProductList
import com.example.spirelab_pankajverma.ui.theme.SPIRELab_PankajVermaTheme
import com.example.spirelab_pankajverma.viewmodel.ProductViewModel
import com.example.spirelab_pankajverma.viewmodel.ProductViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: ProductViewModel
    private lateinit var viewModelFactory: ProductViewModelFactory
    private lateinit var repository: ProductRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        val database = DatabaseProvider.provideDataBase(applicationContext)

        val productDao = database.cartDao()
        val api = ProductRetrofitApi()
        // create repository first
        repository = ProductRepository(api, productDao,applicationContext)
        val networkObserver = NetworkObserver(applicationContext)
        viewModelFactory = ProductViewModelFactory(repository,networkObserver)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProductViewModel::class.java]

        setContent {
            val products by viewModel.list.collectAsState()
            val loading by viewModel.loader.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.getProduct()
            }

            SPIRELab_PankajVermaTheme {
                if (loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.navigationBars),
                        contentAlignment = Alignment.Center,

                        ) {
                        CircularProgressIndicator()
                    }
                } else {
                    App(products, viewModel)
                }
            }
        }
    }
}

@Composable
fun App(products: List<Product>, viewModel: ProductViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Constant.PRODUCT_LIST,
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        composable(Constant.PRODUCT_LIST) {
            ProductList(products, viewModel, onProductClick = { productId ->
                navController.navigate("${Constant.PRODUCT_DETAIL}/$productId")
            })
        }

        composable(route = "${Constant.PRODUCT_DETAIL}/{productId}") { backStackEntry ->

            val productId = backStackEntry
                .arguments
                ?.getString("productId")
                ?.toIntOrNull()

            ProductDetail(
                productId = productId,
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(Constant.CART_DETAIL) {
            CartItem(viewModel= viewModel,navController = navController,
                onProductClick = { productId ->
                    navController.navigate("${Constant.PRODUCT_DETAIL}/$productId")
                })
        }


    }
//    ProductList(products, viewModel)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SPIRELab_PankajVermaTheme {
//        App()
    }
}