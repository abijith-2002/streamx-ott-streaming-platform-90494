package com.example.android_tv_frontend.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.matchParentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.io.File

// PUBLIC_INTERFACE
object TvColors {
    /** Core color tokens mapped from CSS variables. */
    val Background = Color(0xFF121212) // --color-121212
    val White = Color(0xFFFFFFFF)      // --color-ffffff
    val D8D8D8 = Color(0xFFD8D8D8)     // --color-d8d8d8
    val NavBg = Color(0xFF28292F)      // --color-28292f
    val AccentRed = Color(0xFFE84444)  // --color-e84444
    val GrayText = Color(0xFF7F8282)   // --color-7f8282
    val ActivePill = Color(0xFF9B0F0F) // --color-9b0f0f
    val SurfaceCard = Color(0xFF323131)// --color-323131
    val ProgressBg = Color(0xFF2C2C2C) // --color-2c2c2c
    val ProgressFill = Color(0xFFDE1717)// --color-de1717
    val TagLiveRed = Color(0xFFEB0045) // --color-eb0045
    val PlayRed = Color(0xFFC60000)    // --color-c60000
}

// PUBLIC_INTERFACE
object TvSpacing {
    /** Spacing tokens mapped from CSS variables. */
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
}

// PUBLIC_INTERFACE
object TvRadius {
    /** Radius tokens mapped from CSS variables. */
    val R34 = 34.dp
    val R37 = 37.dp
    val R8 = 8.dp
    val R4 = 4.dp
    val R3_81 = 3.81.dp
    val R2_4 = 2.4.dp
    val R1_6 = 1.6.dp
}

// PUBLIC_INTERFACE
object TvElevation {
    /** Elevation tokens approximating CSS shadow_3 (0px 2px 5px rgba(0,0,0,1)). */
    val Shadow3 = 5.dp
}

// PUBLIC_INTERFACE
object TvTypography {
    /** Typography tokens for this screen. */
    val Typo23 = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Medium, lineHeight = 32.sp, letterSpacing = (-1.9).sp, color = TvColors.White)
    val Typo24 = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal, lineHeight = 32.sp, color = TvColors.White)
    val Typo25 = TextStyle(fontSize = 36.sp, fontWeight = FontWeight.Bold, lineHeight = 42.1875.sp, color = TvColors.White)
    val Typo26 = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Normal, lineHeight = 35.15625.sp, color = TvColors.White)
    val Typo27 = TextStyle(fontSize = 21.333333.sp, fontWeight = FontWeight.Medium, lineHeight = 21.sp, color = TvColors.White)
    val Typo28 = TextStyle(fontSize = 29.sp, fontWeight = FontWeight.Normal, color = TvColors.GrayText)
    val Typo29 = TextStyle(fontSize = 29.sp, fontWeight = FontWeight.Bold, color = TvColors.White)
}

// PUBLIC_INTERFACE
@Composable
fun StreamXTVTheme(content: @Composable () -> Unit) {
    /** Dark theme tuned for TV and this screen tokens. */
    MaterialTheme(
        colorScheme = androidx.compose.material3.darkColorScheme(
            primary = TvColors.PlayRed,
            onPrimary = TvColors.White,
            background = TvColors.Background,
            onBackground = TvColors.White,
            surface = TvColors.Background,
            onSurface = TvColors.White
        ),
        typography = Typography(
            titleLarge = TvTypography.Typo25,
            bodyLarge = TvTypography.Typo26,
            labelLarge = TvTypography.Typo27
        ),
        content = content
    )
}

// Base absolute path for Figma images
private const val FIGMA_IMAGES_ABS_PATH = "/home/kavia/workspace/code-generation/assets/figmaimages"

// PUBLIC_INTERFACE
fun figmaImage(nameOrPath: String): File {
    /** Build absolute File for figma images under the configured base path. */
    val fileName = nameOrPath.substringAfterLast('/')
    return File(FIGMA_IMAGES_ABS_PATH, fileName)
}

// PUBLIC_INTERFACE
@Composable
fun SvgImage(
    fileName: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    /** Loads a local SVG/PNG using Coil; requires coil-svg dependency. */
    val context = LocalContext.current
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(figmaImage(fileName))
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
    )
}

// PUBLIC_INTERFACE
@Composable
fun HeroBanner(
    imageFileName: String,
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier
) {
    /** Large cropped hero banner image. */
    val context = LocalContext.current
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(figmaImage(imageFileName))
            .crossfade(true)
            .build(),
        contentDescription = "Destacado",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(TvRadius.R8))
            .focusable(true)
    )
}

// PUBLIC_INTERFACE
@Composable
fun <T> RowSection(
    title: String,
    items: List<T>,
    contentPadding: PaddingValues = PaddingValues(horizontal = TvSpacing.sm),
    itemSpacing: Dp = 16.dp,
    titleStyle: TextStyle = TvTypography.Typo24,
    itemContent: @Composable (item: T) -> Unit
) {
    /** Generic horizontal row/carousel section with a title and items. */
    androidx.compose.material3.Text(
        text = title,
        style = titleStyle,
        modifier = Modifier.padding(start = TvSpacing.sm, bottom = TvSpacing.sm)
    )
    LazyRow(
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        items(items) { item ->
            itemContent(item)
        }
    }
}

// PUBLIC_INTERFACE
@Composable
fun TagChip(
    label: String,
    backgroundColor: Color = TvColors.TagLiveRed,
    textStyle: TextStyle = TvTypography.Typo27,
    modifier: Modifier = Modifier
) {
    /** Small rounded chip (e.g., EN VIVO). */
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(TvRadius.R3_81))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .semantics { role = Role.Button }
            .focusable(true),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Text(text = label, style = textStyle)
    }
}

// PUBLIC_INTERFACE
@Composable
fun TvIconButton(
    size: Dp = 92.dp,
    outerBackground: Color = TvColors.White,
    innerBackground: Color = TvColors.PlayRed,
    iconFileName: String? = null,
    iconSize: Dp = 40.dp,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    /** Circular icon button with optional inner icon overlay. */
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(outerBackground)
            .shadow(TvElevation.Shadow3, CircleShape)
            .semantics { role = Role.Button }
            .focusable(true),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size - 4.dp)
                .clip(CircleShape)
                .background(innerBackground)
        )
        if (iconFileName != null) {
            SvgImage(
                fileName = iconFileName,
                contentDescription = contentDescription,
                modifier = Modifier.size(iconSize),
                contentScale = ContentScale.Fit
            )
        }
    }
}

// PUBLIC_INTERFACE
@Composable
fun PosterCard(
    width: Dp,
    height: Dp,
    imageFileName: String,
    title: String? = null,
    progress: Float? = null, // 0.0..1.0
    trailingIcons: List<String> = emptyList(),
    modifier: Modifier = Modifier
) {
    /** Poster card with optional progress bar and trailing action icons over the title bar. */
    var focused by remember { mutableStateOf(false) }
    val borderColor = if (focused) TvColors.White.copy(alpha = 0.7f) else Color.Transparent

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(TvRadius.R8))
            .background(TvColors.SurfaceCard)
            .focusable(true)
            .onFocusChanged { focused = it.isFocused }
    ) {
        // Poster
        val context = LocalContext.current
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(figmaImage(imageFileName))
                .crossfade(true)
                .build(),
            contentDescription = title ?: "Poster",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(TvRadius.R8))
                .borderGlow(borderColor)
        )

        // Progress bar
        if (progress != null) {
            val outerWidth = width - 38.dp
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
                    .width(outerWidth)
                    .height(18.dp)
                    .clip(RoundedCornerShape(TvRadius.R8))
                    .background(TvColors.ProgressBg)
            ) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .width(outerWidth * progress)
                        .height(10.dp)
                        .clip(RoundedCornerShape(TvRadius.R4))
                        .background(TvColors.ProgressFill)
                )
            }
        }

        // Title bar + trailing icons
        if (title != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .height(80.dp)
                    .width(width)
                    .background(TvColors.SurfaceCard.copy(alpha = 0.95f))
            ) {
                androidx.compose.material3.Text(
                    text = title,
                    style = TvTypography.Typo23,
                    modifier = Modifier.padding(start = 16.dp, top = 12.dp)
                )
                if (trailingIcons.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 16.dp, bottom = 16.dp)
                    ) {
                        trailingIcons.forEach { iconName ->
                            SvgImage(
                                fileName = iconName,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// PUBLIC_INTERFACE
fun Modifier.borderGlow(color: Color): Modifier {
    /** Simple glow approximation used for focus highlight. */
    return this.then(
        if (color.alpha > 0f)
            Modifier
                .border(width = 2.dp, color = color, shape = RoundedCornerShape(TvRadius.R8))
        else Modifier
    )
}
