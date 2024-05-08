package com.minhmdl.goodbooks.screens.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.grayseal.bookshelf.ui.theme.poppinsFamily
import com.minhmdl.goodbooks.navigation.GoodbooksDestinations
import com.minhmdl.goodbooks.screens.search.Results
import com.minhmdl.goodbooks.screens.search.SearchViewModel
import com.minhmdl.goodbooks.ui.theme.Gray200


@Composable
fun CategoryScreen(
    navController: NavController,
    viewModel: SearchViewModel,
    category: String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "Close Icon",
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .clickable(enabled = true, onClick = {
                        navController.navigate(GoodbooksDestinations.HOME_ROUTE)
                    })
            )
            Text(
                category.toString(),
                fontFamily = poppinsFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )

        }
        if (category != null) {
            viewModel.loading.value = true
            viewModel.searchBooks(category)
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(top = 10.dp), color = Gray200)
        Results(navController = navController, searchViewModel = viewModel)
    }
}