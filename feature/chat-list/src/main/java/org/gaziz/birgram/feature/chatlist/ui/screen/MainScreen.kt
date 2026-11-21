package org.gaziz.birgram.feature.chatlist.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.gaziz.birgram.core.ui.icon.archive
import org.gaziz.birgram.core.telegram.ui.model.AvatarUiState
import org.gaziz.birgram.feature.chatlist.R
import org.gaziz.birgram.feature.chatlist.domain.model.ChatListItem
import org.gaziz.birgram.feature.chatlist.ui.ChatListViewModel
import org.gaziz.birgram.feature.chatlist.ui.component.ChatCard
import org.gaziz.birgram.feature.chatlist.ui.component.InboxBottomBar
import org.gaziz.birgram.feature.chatlist.ui.component.InboxTab
import org.gaziz.birgram.feature.chatlist.ui.component.MainScreenMenu
import org.gaziz.birgram.feature.chatlist.ui.component.MainScreenTopBar
import org.gaziz.birgram.feature.chatlist.ui.component.chatCard.DraftMessagePreview
import org.gaziz.birgram.feature.chatlist.ui.component.chatCard.LastMessagePreview
import org.gaziz.birgram.feature.chatlist.ui.component.chatCard.OnlineIndicator
import org.gaziz.birgram.feature.chatlist.ui.model.CardTextUiState
import org.gaziz.birgram.feature.chatlist.ui.model.LastMsgUiState
import org.gaziz.birgram.feature.chatlist.ui.model.PhotoUiState
import org.gaziz.birgram.feature.chatlist.ui.model.UnreadBadgeUiState

private enum class InboxSection {
    PERSONAL,
    GROUP,
    SETTINGS
}

private class InboxTitleScrollConnection(
    private val collapseThresholdPx: Float,
    private val expandThresholdPx: Float,
    private val onCollapse: () -> Unit,
    private val onExpand: () -> Unit,
) : NestedScrollConnection {
    private var accumulatedScroll = 0f

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        val dy = consumed.y
        if (dy == 0f) return Offset.Zero

        if (accumulatedScroll != 0f && (accumulatedScroll > 0f) != (dy > 0f)) {
            accumulatedScroll = 0f
        }
        accumulatedScroll += dy

        when {
            accumulatedScroll < -collapseThresholdPx -> {
                onCollapse()
                accumulatedScroll = 0f
            }
            accumulatedScroll > expandThresholdPx -> {
                onExpand()
                accumulatedScroll = 0f
            }
        }
        return Offset.Zero
    }

    fun reset() {
        accumulatedScroll = 0f
        onExpand()
    }
}

@Composable
fun MainScreen(
    onSearchClick: () -> Unit,
    onChatClick: (Long) -> Unit,
    onArchiveClick: () -> Unit
) {
    val viewModel = hiltViewModel<ChatListViewModel>()
    val mainChats by viewModel.mainChatList.collectAsState()
    val archivedChats by viewModel.archiveChatList.collectAsState()
    val isDark by viewModel.isDark.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var isLogOut by rememberSaveable { mutableStateOf(false) }
    var selectedSection by rememberSaveable { mutableStateOf(InboxSection.PERSONAL) }
    var titleCollapsed by rememberSaveable { mutableStateOf(false) }
    val density = LocalDensity.current

    val scrollConnection = remember(density) {
        InboxTitleScrollConnection(
            collapseThresholdPx = with(density) { 28.dp.toPx() },
            expandThresholdPx = with(density) { 18.dp.toPx() },
            onCollapse = { titleCollapsed = true },
            onExpand = { titleCollapsed = false },
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
        }
            .distinctUntilChanged()
            .collect { atTop ->
                if (atTop) scrollConnection.reset()
            }
    }

    val visibleChats = remember(mainChats, selectedSection) {
        when (selectedSection) {
            InboxSection.GROUP -> mainChats.filter { it.isGroup }
            InboxSection.PERSONAL, InboxSection.SETTINGS -> mainChats.filterNot { it.isGroup }
        }
    }
    val pageColor = if (isDark) Color(0xFF111111) else Color(0xFFF9F7F5)
    val deletedAccount = stringResource(R.string.deleted_account)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MainScreenMenu(
                isDark = isDark,
                switchIsDark = viewModel::switchIsDark,
                onLogOut = { isLogOut = true }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(pageColor)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                MainScreenTopBar(
                    titleCollapsed = titleCollapsed,
                    onMenu = { scope.launch { drawerState.open() } }
                )

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .nestedScroll(scrollConnection),
                    contentPadding = PaddingValues(bottom = 104.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    if (selectedSection != InboxSection.GROUP && archivedChats.isNotEmpty()) {
                        item(key = "archived") {
                            val archivedLabel = stringResource(R.string.archived_chats)
                            ChatCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(78.dp),
                                photo = PhotoUiState(
                                    photo = AvatarUiState.Icon(
                                        imageVector = archive,
                                        background = MaterialTheme.colorScheme.surfaceContainerHigh
                                    ),
                                    size = 54.dp,
                                ),
                                title = CardTextUiState(
                                    text = archivedLabel,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                onClick = onArchiveClick
                            )
                            InboxRowDivider()
                        }
                    }

                    items(items = visibleChats, key = ChatListItem::id) { item ->
                        ChatCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(78.dp),
                            photo = PhotoUiState(
                                photo = item.avatar,
                                size = 54.dp,
                                overlay = {
                                    OnlineIndicator(
                                        size = 10.dp,
                                        isOnline = item.isOnline,
                                        indicatorColor = MaterialTheme.colorScheme.tertiary,
                                        backgroundColor = pageColor,
                                        alignment = Alignment.BottomEnd,
                                    )
                                }
                            ),
                            title = CardTextUiState(
                                text = if (item.isDeleted) deletedAccount else item.title,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            lastMessage = LastMsgUiState(
                                component = { modifier ->
                                    if (item.isDraftMsg) {
                                        item.draftMessage?.let { draft ->
                                            DraftMessagePreview(
                                                modifier = modifier,
                                                draftMessage = draft,
                                                fontSize = 14.sp
                                            )
                                        }
                                    } else {
                                        item.lastMessage?.let { message ->
                                            LastMessagePreview(
                                                modifier = modifier,
                                                lastMessage = message,
                                                fontSize = 14.sp,
                                                sender = item.messageSender
                                            )
                                        }
                                    }
                                },
                                date = item.lastMsgDate,
                                fontSize = 12.sp
                            ),
                            unreadBadge = UnreadBadgeUiState(
                                unreadCount = item.unreadCount,
                                mentionCount = item.mentionCount,
                                reactionCount = item.reactionCount,
                                fontSize = 12.sp
                            ),
                            onClick = { onChatClick(item.id) },
                        )
                        InboxRowDivider()
                    }

                    if (visibleChats.isEmpty() && (selectedSection == InboxSection.PERSONAL || selectedSection == InboxSection.GROUP)) {
                        item(key = "empty") {
                            Text(
                                text = stringResource(R.string.no_conversations),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp)
                            )
                        }
                    }
                }
            }

            InboxBottomBar(
                selectedTab = when (selectedSection) {
                    InboxSection.PERSONAL -> InboxTab.PERSONAL
                    InboxSection.GROUP -> InboxTab.GROUP
                    InboxSection.SETTINGS -> InboxTab.SETTINGS
                },
                onTabSelected = { tab ->
                    selectedSection = when (tab) {
                        InboxTab.PERSONAL -> InboxSection.PERSONAL
                        InboxTab.GROUP -> InboxSection.GROUP
                        InboxTab.SETTINGS -> InboxSection.SETTINGS
                    }
                    if (tab == InboxTab.SETTINGS) {
                        scope.launch { drawerState.open() }
                    } else {
                        scrollConnection.reset()
                        scope.launch { listState.animateScrollToItem(0) }
                    }
                },
                onSearchClick = onSearchClick,
                isDark = isDark,
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            if (isLogOut) {
                val dialogText = stringArrayResource(R.array.log_out_cnt)
                AlertDialog(
                    onDismissRequest = { isLogOut = false },
                    title = { Text(dialogText[0]) },
                    text = { Text(dialogText[1]) },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                isLogOut = false
                                viewModel.logOut { scope.launch { drawerState.close() } }
                            }
                        ) {
                            Text(dialogText[0], color = MaterialTheme.colorScheme.error)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { isLogOut = false }) {
                            Text(dialogText[2])
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun InboxRowDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 78.dp, end = 20.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.65f)
    )
}
