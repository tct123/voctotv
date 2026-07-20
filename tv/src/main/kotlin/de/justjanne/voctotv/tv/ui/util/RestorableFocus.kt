package de.justjanne.voctotv.tv.ui.util

import androidx.compose.foundation.focusGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged

interface RestorableFocusScope {
    fun Modifier.restorableFocusGroup(): Modifier

    fun Modifier.restorableFocusItem(index: Int): Modifier
}

@Stable
class RestorableFocusScopeImpl(
    val focusedItem: MutableIntState,
    itemCount: Int,
) : RestorableFocusScope {
    val focusRequesters = List(itemCount) { FocusRequester() }

    override fun Modifier.restorableFocusGroup(): Modifier =
        this
            .focusGroup()
            .focusProperties {
                onEnter = {
                    focusRequesters[focusedItem.intValue].requestFocus()
                }
            }

    override fun Modifier.restorableFocusItem(index: Int): Modifier =
        this
            .focusRequester(focusRequesters[index])
            .onFocusChanged {
                if (it.isFocused) {
                    focusedItem.intValue = index
                }
            }
}

@Composable
fun WithRestorableFocus(
    itemCount: Int,
    content: @Composable RestorableFocusScope.() -> Unit,
) {
    val focusedItem = rememberSaveable(itemCount) { mutableIntStateOf(0) }

    val scope = remember(itemCount) { RestorableFocusScopeImpl(focusedItem, itemCount) }

    scope.content()
}
