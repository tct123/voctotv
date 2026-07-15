package de.justjanne.voctotv.common

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import de.justjanne.voctotv.voctoweb.model.ConferenceModel

@Composable
fun ConferenceLogo(
    conference: ConferenceModel,
    modifier: Modifier = Modifier,
    fallbackModifier: Modifier = Modifier,
) {
    val bannerPainter = rememberAsyncImagePainter(conference.bannerUrl)
    val fallbackPainter = rememberAsyncImagePainter(conference.logoUrl)

    val bannerState = bannerPainter.state.collectAsState()
    val useBanner =
        remember {
            derivedStateOf {
                bannerState.value !is AsyncImagePainter.State.Error
            }
        }

    if (useBanner.value) {
        Image(
            painter = bannerPainter,
            contentDescription = conference.title,
            modifier = modifier,
        )
    } else {
        Image(
            painter = fallbackPainter,
            contentDescription = conference.title,
            modifier = modifier.then(fallbackModifier),
        )
    }
}
