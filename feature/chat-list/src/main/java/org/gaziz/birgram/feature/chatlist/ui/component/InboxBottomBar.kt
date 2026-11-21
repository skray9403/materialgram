package org.gaziz.birgram.feature.chatlist.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.core.ui.icon.search

enum class InboxTab(val label: String) {
    PERSONAL("Personal"),
    GROUP("Group"),
    SETTINGS("Settings")
}

@Composable
fun InboxBottomBar(
    selectedTab: InboxTab,
    onTabSelected: (InboxTab) -> Unit,
    onSearchClick: () -> Unit,
    isDark: Boolean,
    modifier: Modifier = Modifier,
) {
    val dockColor = if (isDark) Color(0xFF272727) else Color(0xFFF1EDEB)
    val selectedColor = if (isDark) Color(0xFF414141) else Color(0xFFDAD5D1)
    val fabColor = if (isDark) Color(0xFF343434) else Color(0xFF1C1C1C)

    Row(
        modifier = modifier
            .widthIn(max = 600.dp)
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.weight(1f),
            shape = CircleShape,
            color = dockColor,
            shadowElevation = 4.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(4.dp)
                    .selectableGroup(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InboxTab.entries.forEach { tab ->
                    InboxTabItem(
                        tab = tab,
                        selected = selectedTab == tab,
                        selectedColor = selectedColor,
                        onSelected = { onTabSelected(tab) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = onSearchClick,
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            containerColor = fabColor,
            contentColor = Color.White,
        ) {
            Icon(
                imageVector = search,
                contentDescription = "Search chats",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun InboxTabItem(
    tab: InboxTab,
    selected: Boolean,
    selectedColor: Color,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val icon = when (tab) {
        InboxTab.PERSONAL -> personalTabIcon
        InboxTab.GROUP -> groupTabIcon
        InboxTab.SETTINGS -> settingsTabIcon
    }
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(CircleShape)
            .background(if (selected) selectedColor else Color.Transparent)
            .selectable(
                selected = selected,
                onClick = onSelected,
                role = Role.Tab
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = tab.label,
                color = contentColor,
                fontSize = 12.sp,
                maxLines = 1
            )
        }
    }
}
