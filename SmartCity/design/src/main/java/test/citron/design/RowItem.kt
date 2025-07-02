package test.citron.design

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import test.citron.design.theme.CitronTheme

@Composable
fun RowFavoriteItem(
    modifier: Modifier = Modifier,
    text: String,
    isFavorite: Boolean = false,
    onRowClicked: () -> Unit = {},
    onFavoriteClicked: (() -> Unit) = {}
) {
    Row(
        modifier =
        modifier
            .fillMaxWidth()
            .height(CitronTheme.sizing.rowHeight)
            .clickable(enabled = true, onClick = onRowClicked)
            .padding(horizontal = CitronTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, modifier = Modifier.weight(1f))

        IconButton(
            modifier = modifier.height(CitronTheme.sizing.favoriteIconSize),
            onClick = onFavoriteClicked
        ) {
            val icon =
                if (isFavorite) {
                    Icons.Outlined.Favorite
                } else {
                    Icons.Outlined.FavoriteBorder
                }

            Image(
                imageVector = icon,
                contentDescription = "favorite"
            )
        }
    }
}

@Composable
fun RowItem(
    modifier: Modifier = Modifier,
    text: String,
    isClickable: Boolean = false,
    onRowClicked: () -> Unit = {}
) {
    Row(
        modifier =
        modifier
            .fillMaxWidth()
            .height(CitronTheme.sizing.rowHeight)
            .clickable(enabled = isClickable, onClick = onRowClicked)
            .padding(horizontal = CitronTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
fun RowItemPreview() {
    RowItem(
        text = "Sample Text"
    )
}

@Preview
@Composable
fun RowItemNoFavoritePreview() {
    RowFavoriteItem(
        text = "Sample Text",
        isFavorite = false
    )
}

@Preview
@Composable
fun RowItemFavoritePreview() {
    RowFavoriteItem(
        text = "Sample Text",
        isFavorite = true
    )
}
