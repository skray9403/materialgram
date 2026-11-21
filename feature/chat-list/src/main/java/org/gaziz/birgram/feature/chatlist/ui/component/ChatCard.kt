package org.gaziz.birgram.feature.chatlist.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.gaziz.birgram.core.telegram.ui.component.Avatar
import org.gaziz.birgram.feature.chatlist.ui.component.chatCard.ChatTime
import org.gaziz.birgram.feature.chatlist.ui.component.chatCard.ChatUnreadBadge
import org.gaziz.birgram.feature.chatlist.ui.model.CardTextUiState
import org.gaziz.birgram.feature.chatlist.ui.model.LastMsgUiState
import org.gaziz.birgram.feature.chatlist.ui.model.PhotoUiState
import org.gaziz.birgram.feature.chatlist.ui.model.UnreadBadgeUiState

@Composable
fun ChatCard(
    modifier: Modifier,
    photo: PhotoUiState,
    title: CardTextUiState,
    lastMessage: LastMsgUiState? = null,
    unreadBadge: UnreadBadgeUiState? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.clickable { onClick() },
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            Avatar(
                modifier = Modifier.size(photo.size),
                avatar = photo.photo,
                overlay = photo.overlay
            )
            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = title.text,
                        color = title.color,
                        fontSize = title.fontSize,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if(lastMessage != null) {
                        ChatTime(
                            date = lastMessage.date,
                            fontSize = lastMessage.fontSize
                        )
                    }
                }
                if(lastMessage != null || unreadBadge != null) {
                    Spacer(Modifier.height(4.dp))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if(lastMessage != null) {
                        lastMessage.component(Modifier.weight(1f))
                    }
                    if(unreadBadge != null) {
                        if(lastMessage == null) {
                            Spacer(Modifier.weight(1f))
                        }
                        ChatUnreadBadge(unreadBadge)
                    }
                }
            }
        }
    }
}
