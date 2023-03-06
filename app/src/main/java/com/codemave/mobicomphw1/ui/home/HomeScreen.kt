package com.codemave.mobicomphw1.ui.home

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.codemave.mobicomphw1.data.entity.Category
import com.codemave.mobicomphw1.ui.home.notifications.Notification
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun HomeScreen(
    navController: NavController,
    context: Context,
    viewModel: HomeViewModel = viewModel()
) {
    val viewState by viewModel.state.collectAsState()

    val selectedCategory = viewState.selectedCategory

    if (viewState.categories.isNotEmpty() && selectedCategory != null) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            HomeContent(
                selectedCategory = selectedCategory,
                categories = viewState.categories,
                onCategorySelected = viewModel::onCategorySelected,
                navController = navController,
                context = context
            )
        }
    }

//    Scaffold(
//        modifier = Modifier.padding(bottom = 24.dp),
//        floatingActionButton = {
//            FloatingActionButton(
//                onClick = {navController.navigate("add")},
//                modifier = Modifier.padding(all = 20.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = null
//                )
//            }
//        }
//    ) {
//        Column(
//            modifier = Modifier
//                .systemBarsPadding()
//                .fillMaxWidth()
//        ) {
//            val appBarColor = MaterialTheme.colors.surface.copy(alpha = 0.87f)
//
//            HomeAppBar(
//                navController,
//                backgroundColor = appBarColor
//            )
//
//            Notification(
//                modifier = Modifier.fillMaxSize(),
//                context,
//                navController
//            )
//        }
//    }
}

@Composable
private fun HomeContent(
    selectedCategory: Category,
    categories: List<Category>,
    onCategorySelected: (Category) -> Unit,
    navController: NavController,
    context: Context
) {
    Scaffold(
        modifier = Modifier.padding(bottom = 24.dp),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {navController.navigate("add")},
                modifier = Modifier.padding(all = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxWidth()
        ) {
            val appBarColor = MaterialTheme.colors.surface.copy(alpha = 0.87f)

            HomeAppBar(
                navController,
                backgroundColor = appBarColor
            )

            CategoryTabs(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected
            )

            Notification(
                modifier = Modifier.fillMaxSize(),
                context,
                navController,
                selectedCategory
            )
        }
    }
}

@Composable
private fun CategoryTabs(
    categories: List<Category>,
    selectedCategory: Category,
    onCategorySelected: (Category) -> Unit
) {
    val selectedIndex = categories.indexOfFirst { it == selectedCategory }
    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        edgePadding = 24.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onCategorySelected(category) }
            ) {
                ChoiceChipContent(
                    text = category.name,
                    selected = index == selectedIndex,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun ChoiceChipContent(
    text: String,
    selected: Boolean,
    modifier: Modifier
) {
    Surface(
        color = when {
            selected -> MaterialTheme.colors.primary.copy(alpha = 0.08f)
            else -> MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
        },
        contentColor = when {
            selected -> MaterialTheme.colors.primary
            else -> MaterialTheme.colors.onSurface
        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun HomeAppBar(
    navController: NavController,
    backgroundColor: Color
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(com.codemave.mobicomphw1.R.string.app_name),
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp)
            )
        },
        backgroundColor = backgroundColor,
        actions = {
//            IconButton(onClick = {}) {
//                Icon(
//                    imageVector = Icons.Filled.Search,
//                    contentDescription = stringResource(com.codemave.mobicomphw1.R.string.search)
//                )
//            }
            IconButton(onClick = {navController.navigate("profile")}) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = stringResource(com.codemave.mobicomphw1.R.string.profile)
                )
            }
        }
    )
}