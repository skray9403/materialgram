package org.gaziz.birgram.feature.chatlist.ui.component

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val materialGramMark: ImageVector = ImageVector.Builder(
    name = "MaterialGramMark",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
).apply {
    path(fill = SolidColor(Color.Black)) {
        moveTo(12f, 3f)
        quadTo(4f, 3f, 4f, 10f)
        quadTo(4f, 16f, 10f, 17f)
        lineTo(7f, 21f)
        lineTo(15f, 17f)
        quadTo(20f, 16f, 20f, 10f)
        quadTo(20f, 3f, 12f, 3f)
        close()
    }
}.build()

val personalTabIcon: ImageVector = ImageVector.Builder(
    name = "PersonalTab",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
).apply {
    path(fill = SolidColor(Color.Black)) {
        moveTo(12f, 12f)
        quadTo(8f, 12f, 8f, 8f)
        quadTo(8f, 4f, 12f, 4f)
        quadTo(16f, 4f, 16f, 8f)
        quadTo(16f, 12f, 12f, 12f)
        close()
        moveTo(4f, 20f)
        verticalLineTo(18.5f)
        quadTo(4f, 14f, 12f, 14f)
        quadTo(20f, 14f, 20f, 18.5f)
        verticalLineTo(20f)
        close()
    }
}.build()

val groupTabIcon: ImageVector = ImageVector.Builder(
    name = "GroupTab",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
).apply {
    path(fill = SolidColor(Color.Black)) {
        moveTo(9f, 11f)
        quadTo(6f, 11f, 6f, 8f)
        quadTo(6f, 5f, 9f, 5f)
        quadTo(12f, 5f, 12f, 8f)
        quadTo(12f, 11f, 9f, 11f)
        close()
        moveTo(17f, 11f)
        quadTo(14f, 11f, 14f, 8f)
        quadTo(14f, 5f, 17f, 5f)
        quadTo(20f, 5f, 20f, 8f)
        quadTo(20f, 11f, 17f, 11f)
        close()
        moveTo(2f, 19f)
        verticalLineTo(17.5f)
        quadTo(2f, 13f, 9f, 13f)
        quadTo(16f, 13f, 16f, 17.5f)
        verticalLineTo(19f)
        close()
        moveTo(14f, 13f)
        quadTo(22f, 13f, 22f, 17.5f)
        verticalLineTo(19f)
        horizontalLineTo(18f)
        verticalLineTo(17.5f)
        quadTo(18f, 15f, 14f, 14.2f)
        close()
    }
}.build()

val settingsTabIcon: ImageVector = ImageVector.Builder(
    name = "SettingsTab",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
).apply {
    path(fill = SolidColor(Color.Black)) {
        moveTo(3f, 6f)
        horizontalLineTo(21f)
        verticalLineTo(8f)
        horizontalLineTo(3f)
        close()
        moveTo(3f, 11f)
        horizontalLineTo(21f)
        verticalLineTo(13f)
        horizontalLineTo(3f)
        close()
        moveTo(3f, 16f)
        horizontalLineTo(21f)
        verticalLineTo(18f)
        horizontalLineTo(3f)
        close()
        moveTo(8f, 4f)
        quadTo(9.5f, 4f, 9.5f, 6f)
        quadTo(9.5f, 8f, 8f, 8f)
        quadTo(6.5f, 8f, 6.5f, 6f)
        quadTo(6.5f, 4f, 8f, 4f)
        close()
        moveTo(15f, 9f)
        quadTo(16.5f, 9f, 16.5f, 11f)
        quadTo(16.5f, 13f, 15f, 13f)
        quadTo(13.5f, 13f, 13.5f, 11f)
        quadTo(13.5f, 9f, 15f, 9f)
        close()
        moveTo(10f, 14f)
        quadTo(11.5f, 14f, 11.5f, 16f)
        quadTo(11.5f, 18f, 10f, 18f)
        quadTo(8.5f, 18f, 8.5f, 16f)
        quadTo(8.5f, 14f, 10f, 14f)
        close()
    }
}.build()
