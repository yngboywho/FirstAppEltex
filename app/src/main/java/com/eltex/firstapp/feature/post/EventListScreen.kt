package com.eltex.firstapp.feature.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eltex.firstapp.ui.theme.FirstAppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun EventListScreenRoute(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues.Zero,
    viewModel: EventListViewModel = viewModel(),
) {
    EventListScreen(
        state = viewModel.state,
        modifier = modifier,
        contentPadding = contentPadding,
        onMessage = viewModel::accept,
    )
}

@Composable
fun EventListScreen(
    state: EventListState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues.Zero,
    onMessage: (EventListMessage) -> Unit = {},
) {
    val layoutDirection = LocalLayoutDirection.current

    val combinedPadding = PaddingValues(
        start = contentPadding.calculateStartPadding(layoutDirection) + 8.dp,
        end = contentPadding.calculateEndPadding(layoutDirection) + 8.dp,
        top = contentPadding.calculateTopPadding(),
        bottom = contentPadding.calculateBottomPadding(),
    )

    val listItems = remember(state.events) {
        buildListWithSeparators(state.events)
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = combinedPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = listItems,
            key = { item ->
                when (item) {
                    is EventListItem.DateSeparator -> "sep_${item.epochDay}"
                    is EventListItem.Event -> "event_${item.event.id}"
                }
            },
            contentType = { item ->
                when (item) {
                    is EventListItem.DateSeparator -> "separator"
                    is EventListItem.Event -> "event"
                }
            }
        ) { item ->
            when (item) {
                is EventListItem.DateSeparator -> DateSeparatorItem(label = item.label)
                is EventListItem.Event -> EventCard(
                    modifier = Modifier.animateItem(),
                    event = item.event,
                    likeClicked = { onMessage(EventListMessage.Like(item.event.id)) },
                    participateClicked = { onMessage(EventListMessage.Participate(item.event.id)) },
                )
            }
        }
    }
}

@Composable
private fun DateSeparatorItem(label: String) {
    Text(
        text = label,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
    )
}

private val longDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())

private fun buildListWithSeparators(events: List<EventUiModel>): List<EventListItem> {
    if (events.isEmpty()) return emptyList()

    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    return events
        .sortedByDescending { it.publishedAt }
        .groupBy { it.publishedAt.toLocalDate() }
        .flatMap { (date, eventsOnDay) ->
            val label = when (date) {
                today -> "Сегодня"
                yesterday -> "Вчера"
                else -> date.format(longDateFormatter)
            }

            buildList {
                add(EventListItem.DateSeparator(label = label, epochDay = date.toEpochDay()))
                eventsOnDay.mapTo(this) { EventListItem.Event(it) }
            }
        }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun EventListScreenPreview() {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)
    val older = today.minusDays(3)

    FirstAppTheme {
        EventListScreen(
            EventListState(
                events = listOf(
                    EventUiModel(
                        id = 1,
                        publishedAt = today.atTime(10, 0),
                        author = "Lydia Westervelt",
                        published = "today",
                        status = "Online",
                        visit = "soon",
                        content = "Событие сегодня",
                    ),
                    EventUiModel(
                        id = 2,
                        publishedAt = yesterday.atTime(15, 30),
                        author = "Lydia Westervelt",
                        published = "yesterday",
                        status = "Offline",
                        visit = "soon",
                        content = "Событие вчера",
                        likes = 3,
                        likedByMe = true,
                        participants = 2,
                        participantsByMe = true,
                    ),
                    EventUiModel(
                        id = 3,
                        publishedAt = older.atTime(9, 0),
                        author = "Lydia Westervelt",
                        published = "older",
                        status = "Offline",
                        visit = "past",
                        content = "Событие несколько дней назад",
                        link = "https://example.com",
                    ),
                )
            )
        )
    }
}