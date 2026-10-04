package com.example.spirelab_pankajverma.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spirelab_pankajverma.data.utility.Constant
import com.example.spirelab_pankajverma.viewmodel.ProductViewModel
import org.intellij.lang.annotations.JdkConstants

@Composable
fun ProductDetail(
    modifier: Modifier = Modifier, productId: Int?, viewModel: ProductViewModel,
    navController: NavController
) {
    val productList by viewModel.list.collectAsState()
    var showImageDialog by remember { mutableStateOf(false) }
    val totalCartCount by viewModel.cartTotalCount.collectAsState()
    val product = productList.find { it.id == productId }
    val onCartClick = rememberDebounceClick { navController.navigate(Constant.CART_DETAIL)}
    if (product != null) {
        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row() {
                Icon(modifier = modifier.clickable {
                    navController.popBackStack()
                },imageVector = Icons.Default.ArrowBack,contentDescription = "Back")
                Spacer(modifier = Modifier.weight(1f))
                CartCountButton(totalCartCount, onCartItem = {onCartClick()})

            }
            AsyncImage(
                modifier = modifier
                    .size(200.dp)
                    .clickable {
                        showImageDialog = true
                    }, model = product.images.firstOrNull()?:"", contentDescription = product.title?:""
            )

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Product Name:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.75f)
                )

                Text(
                    text = product.title?:"",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1.25f)
                )
            }

            Spacer(Modifier.height(3.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Description:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .weight(0.75f)
                        .align(Alignment.Top)
                )

                Text(
                    text = product.description?:"",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1.25f)
                )
            }
            Spacer(Modifier.height(3.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Price:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.75f)
                )

                Text(
                    text = "${product.price}"?:"",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1.25f)
                )
            }
            Spacer(Modifier.height(3.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rating:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.75f)
                )

                Text(
                    text = "${product.rating}"?:"",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1.25f)
                )
            }
            Spacer(Modifier.height(3.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Category:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.75f)
                )

                Text(
                    text = product.category ?: "",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1.25f)
                )
            }
            Spacer(Modifier.height(3.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Brand:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.75f)
                )

                Text(
                    text = product.brand ?: "N/A",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1.25f)
                )
            }
            Spacer(Modifier.height(3.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Stock:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(0.75f)
                )

                Text(
                    text = "${product.stock}"?: "",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1.25f)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 0.5.dp, color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(Color.Gray, RoundedCornerShape(8.dp))
                    .clickable {
                        viewModel.addToCart(product)
                        Log.d("SPIRE--->", "Product added to cart: ${viewModel.addToCart(product)}")
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                text = "Add to Cart",
                fontSize = 34.sp,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }

        if (showImageDialog) {
            Dialog(onDismissRequest = {
                showImageDialog = false
            }) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    AsyncImage(
                        model = product.images.firstOrNull()?:"",
                        contentDescription = product.title?:"",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentScale = ContentScale.Fit
                    )

                    IconButton(
                        onClick = {
                            showImageDialog = false
                        },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartCountButton(itemCount:Int,
                    onCartItem: () -> Unit) {
    IconButton(onClick = onCartItem) {
        BadgedBox(badge = {
            if (itemCount > 0) {
                Badge {
                    Text(text = if (itemCount > 99) "99+" else itemCount.toString())
                }
            }

        }) {
            Icon(modifier = Modifier,imageVector = Icons.Default.ShoppingCart,contentDescription = "Cart Items")
        }
    }

}