package com.example.spirelab_pankajverma.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spirelab_pankajverma.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@Composable
fun CartItem(
    viewModel: ProductViewModel, navController: NavController, onProductClick: (Int) -> Unit
) {

    val cartList by viewModel.dbCartItem.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 30.dp)
        ) {
            Icon(modifier = Modifier.clickable {
                navController.popBackStack()
            }, imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            Spacer(modifier = Modifier.weight(1f))
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
        ) {
            items(cartList, key = { it.productId }) {
                Spacer(modifier = Modifier.height(4.dp))
                val remainingStock = it.stock - it.count
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .padding(end = 8.dp)
                            .border(
                                color = Color.DarkGray,
                                shape = RoundedCornerShape(8.dp),
                                width = 0.5.dp
                            )
                            .clickable {
                                onProductClick(it.productId)
                            }
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            modifier = Modifier
                                .weight(0.35f)
                                .aspectRatio(1f),
                            model = it.images.firstOrNull(),
                            contentDescription = it.title
                        )
                        Column(
                            modifier = Modifier
                                .weight(0.70f)
                                .fillMaxHeight()
                                .padding(start = 12.dp),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = it.title,
                                maxLines = 1,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                style = TextStyle(
                                    textMotion = TextMotion.Animated
                                ),
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Available Stock: $remainingStock",
                                color = if (remainingStock <= 5) Color.Red else Color.Gray,
                                fontSize = 14.sp
                            )
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Rating: ${it.rating}",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                ProductItemCount(
                                    modifier = Modifier.weight(0.70f),
                                    count = it.count,
                                    onIncrease = {
                                        if (it.count < it.stock) {
                                            viewModel.updateCount(it.productId, it.count + 1)
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "You reached the maximum quantity",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    },
                                    onDecrease = {
                                        if (it.count > 1) {
                                            viewModel.updateCount(it.productId, it.count - 1)
                                        } else {
                                            viewModel.removeFromCart(it.productId)
                                        }
                                    })
                                Spacer(Modifier.width(5.dp))
                                Text(modifier = Modifier
                                    .clickable {
                                        viewModel.removeFromCart(it.productId)
                                    }
                                    .fillMaxWidth()
                                    .weight(0.25f),
                                    text = "Delete",
                                    maxLines = 1,
                                    fontSize = 20.sp,
                                    overflow = TextOverflow.Ellipsis)
                            }
                        }
                    }
                }
            }

        }


    }


}

@Composable
fun ProductItemCount(
    modifier: Modifier = Modifier, count: Int, onIncrease: () -> Unit, onDecrease: () -> Unit
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(modifier = Modifier
            .clickable { onDecrease() }
            .border(width = 0.5.dp, color = Color.Gray, shape = RoundedCornerShape(5.dp))
            .padding(vertical = 4.dp, horizontal = 12.dp),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            text = "-")
        Text(
            text = count.toString(),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(modifier = Modifier
            .clickable { onIncrease() }
            .border(width = 0.5.dp, color = Color.Gray, shape = RoundedCornerShape(5.dp))
            .padding(vertical = 4.dp, horizontal = 12.dp),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            text = "+")
    }
}



// Helper function to show any dynamic message
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun showBottomMessage(message: String) {
    val snackbarHostState = remember { SnackbarHostState() }
    rememberCoroutineScope().launch {
        // Dismiss active snackbar if one is already showing
        snackbarHostState.currentSnackbarData?.dismiss()
        snackbarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Short
        )
    }
}