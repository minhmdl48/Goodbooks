package com.minhmdl.goodbooks.utils

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GoodbooksDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outlineVariant.copy(alpha=0.12f),
    thickness: Dp = 1.dp
){
    HorizontalDivider(
        modifier = modifier,
        color = color,
        thickness = thickness
    )
}

