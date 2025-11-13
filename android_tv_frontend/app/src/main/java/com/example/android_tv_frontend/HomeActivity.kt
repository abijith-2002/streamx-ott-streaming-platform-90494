package com.example.android_tv_frontend

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.io.File

/**
 * HTML-to-Compose mapping:
 * - header -> HeaderBar()
 *   - logo pieces -> simplified logo box (could be replaced with Image if needed)
 *   - nav buttons (Inicio, Películas...) -> Row of focusable text buttons
 *   - active pill -> rounded container behind selected tab
 *   - avatar/search icons -> placeholder icons (optional)
 * - highlights -> HeroBanner(image: figma_image_1_13.png)
 * - seguí viendo -> CarouselRow(title="Seguí viendo", items: content cards with poster + progress + title)
 * - canales de TV -> CarouselRow(title="Canales de TV", items: tv cards with left image + play + info)
 *
 * Focus behavior:
 * - D-pad directional focus orders match: header -> hero -> first carousel -> second carousel
 * - Focus ring simulated via scale + border glow
 *
 * Image loading:
 * - Coil AsyncImage with placeholders and error tint
 * - All image sources are loaded from absolute file:// paths:
 *   /home/kavia/workspace/code-generation/assets/figmaimages/<filename>
 *
 * TV Hooks:
 * - ExoPlayer-ready: onCardClick will call a stub function where player can be attached later
 */

// PUBLIC_INTERFACE
class HomeActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StreamXTVTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF121212)) {
                    HomeScreen()
                }
            }
        }
    }
}

// PUBLIC_INTERFACE
@Composable
fun HomeScreen() {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(horizontal = 48.dp)
            .padding(top = 36.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        HeaderBar(
            items = listOf("Inicio", "Películas", "Series", "TV en vivo", "Kids", "Mis Contenidos")
        )
        Spacer(Modifier.height(32.dp))
        HeroBanner(
            imagePath = "figma_image_1_13.png",
            width = 1744.dp,
            height = 444.dp
        )
        Spacer(Modifier.height(24.dp))
        CarouselRow(
            title = "Seguí viendo",
            cardWidth = 412.dp,
            cardHeight = 312.dp,
            items = listOf(
                CardItem("Rogue One", "figma_image_1_41.png"),
                CardItem("Ex Machina", "figma_image_1_68.png"),
                CardItem("Sing Street", "figma_image_1_85.png"),
                CardItem("2012", "figma_image_1_102.png"),
                CardItem("Ad Astra", "figma_image_1_119.png"),
            ),
            eagerFirstImage = false
        )
        Spacer(Modifier.height(24.dp))
        CarouselRow(
            title = "Canales de TV",
            cardWidth = 745.dp,
            cardHeight = 212.dp,
            items = listOf(
                CardItem("Marca Claro Radio", "figma_image_1_154.png", subtitle = "004 | Claro sports"),
                CardItem("E.T.", "figma_image_1_179.png", subtitle = "005 | HBO Channel", overlay = "figma_image_1_180.png"),
                CardItem("Marca Claro Radio", "figma_image_1_218.png", subtitle = "004 | Claro sports"),
            ),
            isTvChannel = true
        )
    }
}

data class CardItem(
    val title: String,
    val image: String,
    val subtitle: String? = null,
    val overlay: String? = null
)

@Composable
private fun HeaderBar(items: List<String>) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .height(74.dp)
    ) {
        // Simplified logo placeholder
        Box(
            modifier = Modifier
                .size(width = 170.dp, height = 36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF28292F)),
        )
        Spacer(Modifier.width(24.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(64.dp)
                .clip(RoundedCornerShape(34.dp))
                .background(Color(0xFF28292F))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                items.forEachIndexed { index, label ->
                    val focusedColor = Color.White
                    val unfocusedColor = Color(0xFF7F8282)
                    FocusablePillText(
                        text = label,
                        selected = selectedIndex == index,
                        onFocused = { selectedIndex = index },
                        focusedColor = focusedColor,
                        unfocusedColor = unfocusedColor
                    )
                }
            }
        }
        Spacer(Modifier.width(24.dp))
        // Avatar
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color(0xFFE84444).copy(alpha = 0.15f))
                .focusable(true),
        )
    }
}

@Composable
private fun FocusablePillText(
    text: String,
    selected: Boolean,
    onFocused: () -> Unit,
    focusedColor: Color,
    unfocusedColor: Color
) {
    var focused by remember { mutableStateOf(false) }
    val pillColor = if (selected || focused) Color(0xFF9B0F0F) else Color.Transparent
    val textColor = if (selected || focused) focusedColor else unfocusedColor
    val requester = remember { FocusRequester() }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(37.dp))
            .background(pillColor)
            .padding(horizontal = 18.dp, vertical = 10.dp)
            .onFocusChanged {
                focused = it.isFocused
                if (it.isFocused) onFocused()
            }
            .focusRequester(requester)
            .focusable(true)
    ) {
        androidx.compose.material3.Text(
            text = text,
            color = textColor,
            fontSize = 20.sp,
            fontWeight = if (selected || focused) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun HeroBanner(imagePath: String, width: Dp, height: Dp) {
    // Eager load hero from absolute file path
    AsyncImage(
        model = ImageRequest.Builder(LocalContextProvider())
            .data(figmaImage(imagePath)) // absolute file path
            .crossfade(true)
            .build(),
        contentDescription = "Destacado",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(8.dp))
            .focusable(true),
        onSuccess = { /* no-op */ },
        onError = { /* show subtle overlay? */ }
    )
}

@Composable
private fun CarouselRow(
    title: String,
    items: List<CardItem>,
    cardWidth: Dp,
    cardHeight: Dp,
    isTvChannel: Boolean = false,
    eagerFirstImage: Boolean = false
) {
    Spacer(Modifier.height(8.dp))
    androidx.compose.material3.Text(
        text = title,
        color = Color.White,
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
    )
    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { item ->
            if (isTvChannel) {
                TvChannelCard(item, width = cardWidth, height = cardHeight)
            } else {
                PosterProgressCard(item, width = cardWidth, height = cardHeight, eager = eagerFirstImage && items.first() == item)
            }
        }
    }
}

@Composable
private fun PosterProgressCard(item: CardItem, width: Dp, height: Dp, eager: Boolean) {
    var focused by remember { mutableStateOf(false) }
    val borderColor = if (focused) Color.White.copy(alpha = 0.7f) else Color.Transparent
    Column(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF323131))
            .onFocusChanged { focused = it.isFocused }
            .focusable(true)
            .padding(bottom = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .width(width)
                .height(height - 80.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF2C2C2C))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContextProvider())
                    .data(figmaImage(item.image)) // absolute file path
                    .crossfade(true)
                    .build(),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .borderGlow(borderColor)
            )
            // Progress bar (static width sample)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
                    .width(width - 38.dp)
                    .height(18.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF2C2C2C))
            ) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .width((width - 38.dp) * 0.4f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFDE1717))
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp)
        ) {
            androidx.compose.material3.Text(
                text = item.title,
                color = Color.White,
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun TvChannelCard(item: CardItem, width: Dp, height: Dp) {
    var focused by remember { mutableStateOf(false) }
    val borderColor = if (focused) Color.White.copy(alpha = 0.7f) else Color.Transparent
    Row(
        modifier = Modifier
            .width(width)
            .height(height + 10.dp)
            .onFocusChanged { focused = it.isFocused }
            .focusable(true)
    ) {
        Box(
            modifier = Modifier
                .width(width / 2)
                .height(height)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF2C2C2C))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContextProvider())
                    .data(figmaImage(item.image)) // absolute file path
                    .crossfade(true)
                    .build(),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .borderGlow(borderColor)
            )
            // Optional overlay image on top (second source)
            item.overlay?.let {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContextProvider())
                        .data(figmaImage(it)) // absolute file path
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            }
            // Play button
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = (-24).dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 1f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFC60000)),
                    contentAlignment = Alignment.Center
                ) {
                    // Simple play triangle
                    Box(
                        modifier = Modifier
                            .size(40.dp, 42.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White.copy(alpha = 0.0f))
                    )
                }
            }

            // Small progress
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .width(207.dp)
                    .height(10.dp)
                    .clip(RoundedCornerShape(2.4.dp))
                    .background(Color(0xFF2C2C2C).copy(alpha = 0.8f))
            ) {
                Box(
                    modifier = Modifier
                        .padding(0.8.dp)
                        .width(80.dp)
                        .height(8.8.dp)
                        .clip(RoundedCornerShape(1.6.dp))
                        .background(Color(0xFFDE1717))
                )
            }
        }
        Spacer(Modifier.width(16.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .height(height)
        ) {
            androidx.compose.material3.Text(
                text = item.title,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            item.subtitle?.let {
                Spacer(Modifier.height(6.dp))
                androidx.compose.material3.Text(
                    text = it,
                    color = Color.White,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.height(6.dp))
            androidx.compose.material3.Text(
                text = "11:30 - 12:30",
                color = Color.White,
                fontSize = 20.sp
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFEB0045))
            ) {
                androidx.compose.material3.Text(
                    text = "EN VIVO",
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun Modifier.borderGlow(color: Color): Modifier {
    return this.then(
        Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Transparent)
            .then(
                if (color.alpha > 0f)
                    Modifier
                        .background(color.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                else Modifier
            )
    )
}

@Composable
private fun StreamXTVTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = androidx.compose.material3.darkColorScheme(
            primary = Color(0xFF2196F3),
            onPrimary = Color.White,
            background = Color(0xFF121212),
            onBackground = Color.White,
            surface = Color(0xFF121212),
            onSurface = Color.White
        ),
        typography = androidx.compose.material3.Typography(
            bodyLarge = androidx.compose.material3.Typography().bodyLarge.copy(fontSize = 20.sp),
            titleLarge = androidx.compose.material3.Typography().titleLarge.copy(fontSize = 32.sp, fontWeight = FontWeight.Bold)
        ),
        content = content
    )
}

// Helper to get a context inside composables for ImageRequest builder
@Composable
private fun LocalContextProvider() = androidx.compose.ui.platform.LocalContext.current

// Base absolute path for Figma images
private const val FIGMA_IMAGES_ABS_PATH = "/home/kavia/workspace/code-generation/assets/figmaimages"

/**
 * Build an absolute file path for figma images residing under FIGMA_IMAGES_ABS_PATH.
 * Accepts either a plain filename ("figma_image_1_13.png") or a relative path like
 * "figmaimages/figma_image_1_13.png" or "assets/figmaimages/figma_image_1_13.png".
 */
private fun figmaImage(nameOrPath: String): File {
    val fileName = nameOrPath.substringAfterLast('/')
    return File(FIGMA_IMAGES_ABS_PATH, fileName)
}
