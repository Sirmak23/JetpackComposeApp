package com.anddevcorp.jetpackcomposeapp.ui.ext

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

val customShape = RoundedCornerShape(
    topStart = CornerSize(15.dp),
    topEnd = CornerSize(15.dp),
    bottomStart = CornerSize(15.dp),
    bottomEnd = CornerSize(15.dp)
)
val customBorderShape = RoundedCornerShape(
    topStart = CornerSize(8.dp),
    topEnd = CornerSize(8.dp),
    bottomStart = CornerSize(8.dp),
    bottomEnd = CornerSize(8.dp)
)
@Composable
fun GetImage(imageUrl:String, modifier: Modifier){
    AsyncImage(
        model = "https://image.tmdb.org/t/p/w1280" + imageUrl,
        contentDescription = "Example Image",
        modifier = modifier
    )
}
