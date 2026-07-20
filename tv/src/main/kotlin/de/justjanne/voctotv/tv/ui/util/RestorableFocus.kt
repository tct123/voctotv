package de.justjanne.voctotv.tv.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.StandardCardContainer
import androidx.tv.material3.Text
import de.justjanne.voctotv.common.ConferenceLogo
import de.justjanne.voctotv.tv.Routes
import de.justjanne.voctotv.tv.ui.theme.GridGutter
import de.justjanne.voctotv.tv.ui.theme.GridPadding
import de.justjanne.voctotv.voctoweb.model.ConferenceModel

interface RestorableFocusScope {
    fun Modifier.restorableFocusGroup(): Modifier
    fun Modifier.restorableFocusItem(index: Int): Modifier
}

@Stable
class RestorableFocusScopeImpl(
    val focusedItem: MutableIntState,
    itemCount: Int
): RestorableFocusScope {
    val focusRequesters = List(itemCount) { FocusRequester() }

    override fun Modifier.restorableFocusGroup(): Modifier = this
        .focusGroup()
        .focusProperties {
            onEnter = {
                focusRequesters[focusedItem.intValue].requestFocus()
            }
        }

    override fun Modifier.restorableFocusItem(index: Int): Modifier = this
        .focusRequester(focusRequesters[index])
        .onFocusChanged {
            if (it.isFocused) {
                focusedItem.intValue = index
            }
        }
}

@Composable
fun WithRestorableFocus(itemCount: Int, content: @Composable RestorableFocusScope.() -> Unit) {
    val focusedItem = rememberSaveable(itemCount) { mutableIntStateOf(0) }

    val scope = remember(itemCount) { RestorableFocusScopeImpl(focusedItem, itemCount) }

    scope.content()
}
