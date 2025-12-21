package com.atomicweather.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage

sealed class AtomicImageSpec {
    data class Res(@DrawableRes val res: Int): AtomicImageSpec()
    data class Url(val url: String): AtomicImageSpec()
}

@Composable
fun AtomicImage(
    modifier: Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    spec: AtomicImageSpec
) {
    when (spec) {
        is AtomicImageSpec.Res -> {
            Image(
                modifier = modifier,
                contentScale = contentScale,
                painter = painterResource(spec.res),
                contentDescription = null
            )
        }
        is AtomicImageSpec.Url -> {
            AsyncImage(
                modifier = modifier,
                model = spec.url,
                contentScale = contentScale,
                contentDescription = null
            )
        }
    }
}