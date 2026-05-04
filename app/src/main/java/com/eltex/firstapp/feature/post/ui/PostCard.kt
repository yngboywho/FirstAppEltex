package com.eltex.firstapp.feature.post.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eltex.firstapp.R
import com.eltex.firstapp.ui.theme.FirstAppTheme

@Composable
fun PostCard(
    post: PostUiModel,
    modifier: Modifier = Modifier,
    onEditClicked: () -> Unit = {},
    onDeleteClicked: () -> Unit = {},
    likeClicked: () -> Unit = {},
    shareClicked: () -> Unit = {},
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(modifier.fillMaxWidth()) {
        Column(Modifier.padding(top = 12.dp, bottom = 16.dp, start = 16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primary, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = post.author.take(1), color = MaterialTheme.colorScheme.onPrimary)
                }

                Spacer(Modifier.width(16.dp))

                Column(Modifier.weight(1F)) {
                    Text(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        text = post.author,
                    )
                    Text(fontSize = 14.sp, text = post.published)
                }

                Box {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = null)
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.menu_edit)) },
                            onClick = {
                                menuExpanded = false
                                onEditClicked()
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.post_menu_delete)) },
                            onClick = {
                                menuExpanded = false
                                onDeleteClicked()
                            },
                        )
                    }
                }

                Spacer(Modifier.width(4.dp))
            }

            Spacer(Modifier.height(12.dp))

            Text(modifier = Modifier.padding(top = 16.dp, end = 16.dp), text = post.content)

            if (post.link.isNotBlank()) {
                Spacer(Modifier.height(16.dp))
                Text(
                    fontSize = 14.sp,
                    text = post.link,
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                )
            }

            Spacer(Modifier.height(32.dp))

            Row {
                TextButton(likeClicked) {
                    Icon(
                        if (post.likedByMe) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        null,
                    )

                    Spacer(Modifier.width(width = 8.dp))

                    Text(post.likes.toString())
                }

                Spacer(Modifier.width(8.dp))

                IconButton(
                    shareClicked,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                    )
                ) {
                    Icon(
                        Icons.Default.Share,
                        null,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PostCardPreview() {
    FirstAppTheme {
        PostCard(
            PostUiModel(
                author = "Lydia Westervelt",
                published = "11.05.22 11:21",
                content = "Сегодня поделюсь интересными находками из последних проектов.",
                link = "https://m2.material.io/components/cards",
                likes = 2,
                likedByMe = true,
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun PostCardPreviewDark() {
    FirstAppTheme {
        PostCard(
            PostUiModel(
                author = "Lydia Westervelt",
                published = "11.05.22 11:21",
                content = "Сегодня поделюсь интересными находками из последних проектов.",
                likes = 5,
                likedByMe = false,
            )
        )
    }
}
