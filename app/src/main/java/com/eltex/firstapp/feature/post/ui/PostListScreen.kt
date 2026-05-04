package com.eltex.firstapp.feature.post.ui

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
import com.eltex.firstapp.ui.theme.FirstAppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PostListScreenRoute(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues.Zero,
    viewModel: PostListViewModel,
    onEditPost: (Long) -> Unit = {},
) {
    PostListScreen(
        state = viewModel.state,
        modifier = modifier,
        contentPadding = contentPadding,
        onMessage = viewModel::accept,
        onEditPost = onEditPost,
    )
}

@Composable
fun PostListScreen(
    state: PostListState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues.Zero,
    onMessage: (PostListMessage) -> Unit = {},
    onEditPost: (Long) -> Unit = {},
) {
    val layoutDirection = LocalLayoutDirection.current

    val combinedPadding = PaddingValues(
        start = contentPadding.calculateStartPadding(layoutDirection) + 8.dp,
        end = contentPadding.calculateEndPadding(layoutDirection) + 8.dp,
        top = contentPadding.calculateTopPadding(),
        bottom = contentPadding.calculateBottomPadding(),
    )

    val listItems = remember(state.posts) {
        buildListWithSeparators(state.posts)
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
                    is PostListItem.DateSeparator -> "sep_${item.epochDay}"
                    is PostListItem.Post -> "post_${item.post.id}"
                }
            },
            contentType = { item ->
                when (item) {
                    is PostListItem.DateSeparator -> "separator"
                    is PostListItem.Post -> "post"
                }
            }
        ) { item ->
            when (item) {
                is PostListItem.DateSeparator -> PostDateSeparatorItem(label = item.label)
                is PostListItem.Post -> PostCard(
                    modifier = Modifier.animateItem(),
                    post = item.post,
                    onEditClicked = { onEditPost(item.post.id) },
                    onDeleteClicked = { onMessage(PostListMessage.Delete(item.post.id)) },
                    likeClicked = { onMessage(PostListMessage.Like(item.post.id)) },
                )
            }
        }
    }
}

@Composable
private fun PostDateSeparatorItem(label: String) {
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

private val postLongDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())

private fun buildListWithSeparators(posts: List<PostUiModel>): List<PostListItem> {
    if (posts.isEmpty()) return emptyList()

    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    return posts
        .sortedByDescending { it.publishedAt }
        .groupBy { it.publishedAt.toLocalDate() }
        .flatMap { (date, postsOnDay) ->
            val label = when (date) {
                today -> "Сегодня"
                yesterday -> "Вчера"
                else -> date.format(postLongDateFormatter)
            }

            buildList {
                add(PostListItem.DateSeparator(label = label, epochDay = date.toEpochDay()))
                postsOnDay.mapTo(this) { PostListItem.Post(it) }
            }
        }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PostListScreenPreview() {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    FirstAppTheme {
        PostListScreen(
            PostListState(
                posts = listOf(
                    PostUiModel(id = 1, publishedAt = today.atTime(10, 0),
                        author = "Lydia Westervelt", published = "today",
                        content = "Пост сегодня"),
                    PostUiModel(id = 2, publishedAt = yesterday.atTime(15, 30),
                        author = "Lydia Westervelt", published = "yesterday",
                        content = "Пост вчера", likes = 3, likedByMe = true),
                )
            )
        )
    }
}
