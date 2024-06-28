package com.minhmdl.goodbooks.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.minhmdl.goodbooks.utils.NavBar
import com.minhmdl.goodbooks.utils.rememberMarker
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.DefaultDimens
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.AxisPosition.Vertical.Start
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.formatter.DecimalFormatAxisValueFormatter
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.extension.half

@Composable
fun StatsScreen(
    navController: NavController,
    statsViewModel: StatsViewModel
) {
    val userId = Firebase.auth.currentUser?.uid
    var readCount by remember{ mutableStateOf(0) }
    var numBook: Map<Int, Int> by remember { mutableStateOf(mapOf()) }

    LaunchedEffect(key1 = Unit) {
        readCount = statsViewModel.getReadCount(userId)
        numBook = statsViewModel.getNumberOfBooksReadEachMonth(userId)
    }


    val defaultColumns = currentChartStyle.columnChart.columns

    Scaffold(
        bottomBar = { NavBar(navController = navController)},
        content = {paddingValues->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                var entries = (0..11).map { month ->
                    month to numBook.getOrDefault(month + 1, 0)
                }.toTypedArray()
                Chart(
                    chart = columnChart(
                        columns = remember(defaultColumns) {
                            defaultColumns.map { defaultColumn ->
                                LineComponent(defaultColumn.color, COLUMN_WIDTH_DP, defaultColumn.shape)
                            }
                        },
                    ),
                    model = entryModelOf(*entries),
                    startAxis = startAxis(valueFormatter = startAxisValueFormatter, maxLabelCount = START_AXIS_LABEL_COUNT),
                    bottomAxis = bottomAxis(itemPlacer = bottomAxisItemPlacer, valueFormatter = bottomAxisValueFormatter),
                    marker = rememberMarker(),
                    horizontalLayout = horizontalLayout,
                )
                Text(
                    text = "Number of books read per month",
                    modifier = Modifier.padding(10.dp))
            }
        }
    )
}
@Composable
fun YearPickerDialog(currentYear: Int, onYearSelected: (Int) -> Unit) {
    val years = (currentYear - 10)..(currentYear + 10) // Adjust the range as needed
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Select Year") },
        text = {
            LazyColumn {
                items(years.toList()) { year ->
                    TextButton(onClick = { onYearSelected(year) }) {
                        Text(year.toString())
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = { Button(onClick = {}) { Text("Cancel") } }
    )
}

private const val COLOR_1_CODE = 0xffff5500
private const val COLOR_2_CODE = 0xffd3d826
private const val COLUMN_WIDTH_DP = 16f
private const val THRESHOLD_LINE_VALUE = 13f
private const val START_AXIS_LABEL_COUNT = 8
private const val BOTTOM_AXIS_ITEM_SPACING = 1
private const val BOTTOM_AXIS_ITEM_OFFSET = 0

private val color1 = Color(COLOR_1_CODE)
private val color2 = Color(COLOR_2_CODE)
private val chartColors = listOf(color1)
private val thresholdLineLabelMarginValue = 4.dp
private val thresholdLineLabelHorizontalPaddingValue = 8.dp
private val thresholdLineLabelVerticalPaddingValue = 2.dp
private val thresholdLineLabelPadding =
    dimensionsOf(thresholdLineLabelHorizontalPaddingValue, thresholdLineLabelVerticalPaddingValue)
private val thresholdLineLabelMargins = dimensionsOf(thresholdLineLabelMarginValue)
private val startAxisValueFormatter = DecimalFormatAxisValueFormatter<Start>()
private val horizontalLayout = HorizontalLayout.FullWidth(
    startPaddingDp = DefaultDimens.COLUMN_OUTSIDE_SPACING.half,
    endPaddingDp = DefaultDimens.COLUMN_OUTSIDE_SPACING.half,
)
private val bottomAxisItemPlacer = AxisItemPlacer.Horizontal.default(BOTTOM_AXIS_ITEM_SPACING, BOTTOM_AXIS_ITEM_OFFSET)
private val monthsOfYear = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
private val bottomAxisValueFormatter =
    AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ -> monthsOfYear[x.toInt() % monthsOfYear.size] }
