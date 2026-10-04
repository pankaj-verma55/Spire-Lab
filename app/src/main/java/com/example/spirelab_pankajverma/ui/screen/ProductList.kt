package com.example.spirelab_pankajverma.ui.screen

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Query
import coil.compose.AsyncImage
import com.example.spirelab_pankajverma.data.item.Product
import com.example.spirelab_pankajverma.viewmodel.ProductViewModel

@Composable
fun ProductList(product: List<Product>,
                viewModel: ProductViewModel,
                onProductClick: (Int) -> Unit) {

    val searchItem = rememberSaveable(saver = TextFieldState.Saver) {
        TextFieldState()
    }
    Column(modifier = Modifier) {
        SimpleSearchBar(
            viewModel = viewModel,
            textFieldState = searchItem
            )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(product) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(end = 8.dp)
                        .border(
                            color = Color.DarkGray,
                            shape = RoundedCornerShape(8.dp), width = 0.5.dp
                        )
                        .clickable{
                            onProductClick(it.id)
                        }
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                            text = it.description,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Rating: ${it.rating}",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    viewModel: ProductViewModel,
    textFieldState: TextFieldState,
    modifier: Modifier = Modifier
) {
    // Controls expansion state of the search bar
    var expanded by rememberSaveable { mutableStateOf(false) }
    val items = rememberSaveable { mutableStateListOf("") }

    Box(
        modifier
            .fillMaxWidth()
            .semantics { isTraversalGroup = true }) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = {
                        textFieldState.edit { replace(0, length, it) }
                        viewModel.searchProduct(it)
                    },
                    onSearch = {
                        items.add(textFieldState.toString())
                        expanded = false
                        Log.d("SPIRE search--->", "Search: $it")
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Search") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    },
                    trailingIcon = {
                        if (expanded) {
                            Icon(
                                modifier = modifier.clickable {
                                    if (textFieldState.text.isEmpty()) {
                                        expanded = false
                                        viewModel.getProduct()
                                    } else {
                                        textFieldState.edit { replace(0, length, "") }
                                    }
                                },
                                imageVector = Icons.Default.Close, contentDescription = null
                            )
                        }
                    }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            // Display search results in a scrollable column
            Column(Modifier.verticalScroll(rememberScrollState())) {
                items.forEach { result ->
                    ListItem(
                        headlineContent = { Text(result) }, modifier = Modifier
                            .clickable {
//                                textFieldState.edit { replace(0, length, result) }
                                expanded = false
                            }
                            .fillMaxWidth())
                }
            }
        }
    }
}

