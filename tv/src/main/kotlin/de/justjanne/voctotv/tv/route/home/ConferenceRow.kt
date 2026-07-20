/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv.route.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
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
import de.justjanne.voctotv.tv.ui.util.WithRestorableFocus
import de.justjanne.voctotv.voctoweb.model.ConferenceModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ConferenceRow(
    title: String,
    items: List<ConferenceModel>,
    navigate: (NavKey) -> Unit,
) {
    Text(title, modifier = Modifier.padding(horizontal = GridGutter))

    WithRestorableFocus(items.size) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(GridPadding),
            contentPadding = PaddingValues(vertical = GridPadding, horizontal = GridGutter),
            modifier = Modifier.restorableFocusGroup(),
        ) {
            itemsIndexed(items, key = { _, item -> item.acronym }) { index, conference ->
                StandardCardContainer(
                    modifier = Modifier.width(192.dp),
                    imageCard = { interactionSource ->
                        Card(
                            onClick = { navigate(Routes.Conference(conference.acronym)) },
                            modifier =
                                Modifier
                                    .restorableFocusItem(index)
                                    .width(192.dp)
                                    .aspectRatio(16f / 9f),
                            interactionSource = interactionSource,
                            colors =
                                CardDefaults.colors(
                                    containerColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    pressedContainerColor = Color.Transparent,
                                ),
                        ) {
                            ConferenceLogo(
                                conference,
                                Modifier.fillMaxSize(),
                                Modifier
                                    .background(MaterialTheme.colorScheme.inverseSurface)
                                    .padding(8.dp),
                            )
                        }
                    },
                    title = {
                        Text(
                            text = conference.title,
                            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
                            minLines = 2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                )
            }
        }
    }
}
