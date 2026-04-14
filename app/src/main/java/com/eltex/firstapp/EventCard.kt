package com.eltex.firstapp

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
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eltex.firstapp.ui.theme.FirstAppTheme

@Composable
fun EventCard(
    event: EventUiModel,
    modifier: Modifier = Modifier,
    menuClicked: () -> Unit = {},
    likeClicked: () -> Unit = {},
    shareClicked: () -> Unit = {},
    participateClicked: () -> Unit = {},
) {
    Card(modifier.fillMaxWidth()) {
        Column(Modifier.padding(top = 12.dp, bottom = 16.dp, start = 16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primary, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = event.author.take(1), color = MaterialTheme.colorScheme.onPrimary)
                }

                Spacer(Modifier.width(16.dp))

                Column(Modifier.weight(1F)) {
                    Text(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        text = event.author,
                    )
                    Text(fontSize = 14.sp, text = event.published)
                }

                IconButton(menuClicked) {
                    Icon(Icons.Default.MoreVert, null)
                }

                Spacer(Modifier.width(4.dp))
            }

            Spacer(Modifier.height(12.dp))

            Text(
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                text = event.status
            )
            Text(fontSize = 14.sp, text = event.visit)

            Spacer(Modifier.height(12.dp))

            Text(modifier = Modifier.padding(top = 16.dp, end = 16.dp), text = event.content)

            if (event.link.isNotBlank()){
                Spacer(Modifier.height(16.dp))
                Text(
                    fontSize = 14.sp,
                    text = event.link,
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                )
            }

            Spacer(Modifier.height(32.dp))

            Row {
                TextButton(likeClicked) {
                    Icon(
                        if (event.likedByMe) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        null,
                    )

                    Spacer(Modifier.width(width = 8.dp))

                    Text(event.likes.toString())
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

                Spacer(Modifier.weight(1F))

                TextButton(participateClicked) {
                    Icon(
                        if (event.participantsByMe) {
                            Icons.Default.Group
                        } else {
                            Icons.Default.GroupAdd
                        },
                        contentDescription = null,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(event.participants.toString())
                }
            }
        }
    }
}

@Preview
@Composable
fun EventCardPreview() {
    FirstAppTheme {
        EventCard(
            EventUiModel(
                author = "Lydia Westervelt",
                published = "11.05.22 11:21",
                status = "Offline",
                visit = "16.05.22 12:00",
                content = "Приглашаю провести уютный вечер за увлекательными играми! " +
                        "У нас есть несколько вариантов настолок, " +
                        "подходящих для любой компании.",
                link = "https://m2.material.io/components/cards",
                likes = 2,
                likedByMe = true,
                participants = 2,
                participantsByMe = false,
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun EventCardPreviewDark() {
    FirstAppTheme {
        EventCard(
            EventUiModel(
                author = "Lydia Westervelt",
                published = "11.05.22 11:21",
                status = "Offline",
                visit = "16.05.22 12:00",
                content = "Приглашаю провести уютный вечер за увлекательными играми! " +
                        "У нас есть несколько вариантов настолок, " +
                        "подходящих для любой компании.",
                link = "https://m2.material.io/components/cards",
                likes = 2,
                likedByMe = true,
                participants = 2,
                participantsByMe = true,
            )
        )
    }
}