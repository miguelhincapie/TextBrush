package com.example.textbrush.compose

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.textbrush.R
import com.example.textbrush.customview.SplineTextView
import com.example.textbrush.ui.theme.TextBrushTheme

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MainScreen() {
    TextBrushTheme {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            val (bg, drawer, xButton, bgButton, textButton) = createRefs()
            GlideImage(
                model = R.drawable.bg1,
                contentDescription = null,
                modifier = Modifier.constrainAs(bg) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
                contentScale = ContentScale.Crop,
            )
            AndroidView(
                modifier = Modifier.constrainAs(drawer) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(400.dp)
                },
                factory = { context ->
                    SplineTextView(context, null)
                },
                update = { view ->

                }
            )
            Image(
                modifier = Modifier
                    .constrainAs(xButton) {
                        start.linkTo(parent.start, 16.dp)
                        top.linkTo(parent.top, 30.dp)
                    }
                    .size(40.dp)
                    .clickable {

                    },
                painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
            )
            Image(
                modifier = Modifier
                    .constrainAs(textButton) {
                        end.linkTo(parent.end, 16.dp)
                        top.linkTo(parent.top, 30.dp)
                    }
                    .size(40.dp)
                    .clickable {

                    },
                painter = painterResource(id = android.R.drawable.ic_menu_edit),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
            )
            Image(
                modifier = Modifier
                    .constrainAs(bgButton) {
                        end.linkTo(parent.end, 16.dp)
                        top.linkTo(textButton.bottom, 30.dp)
                    }
                    .size(40.dp)
                    .clickable {

                    },
                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
            )
        }
    }
}

@Composable
@Preview(
    device = Devices.PIXEL_4,
    showBackground = true
)
private fun MainScreenPreview() {
    MainScreen()
}

@Composable
@Preview(
    device = Devices.PIXEL_4,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
private fun MainScreenPreviewDark() {
    MainScreen()
}