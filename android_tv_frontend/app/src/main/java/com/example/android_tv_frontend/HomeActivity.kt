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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.layout.matchParentSize
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.android_tv_frontend.ui.StreamXTVTheme
import com.example.android_tv_frontend.ui.TvColors
import com.example.android_tv_frontend.ui.TvRadius
import com.example.android_tv_frontend.ui.TvTypography
import com.example.android_tv_frontend.ui.TvSpacing
import com.example.android_tv_frontend.ui.TagChip
import com.example.android_tv_frontend.ui.TvIconButton
import com.example.android_tv_frontend.ui.HeroBanner
import com.example.android_tv_frontend.ui.RowSection
import com.example.android_tv_frontend.ui.PosterCard
import com.example.android_tv_frontend.ui.SvgImage
import com.example.android_tv_frontend.ui.figmaImage
import com.example.android_tv_frontend.ui.borderGlow
import java.io.File

/**
HTML -> Composable mapping checklist (Home Screen)
[✓] header (header) -> HeaderBar()
    [✓] logo pieces (header__logo) -> LogoGroup() with layered SvgImage icons
    [✓] nav buttons (Inicio, Películas, Series, TV en vivo, Kids, Mis Contenidos) -> FocusablePillText within HeaderBar()
    [✓] active pill (header__active-pill) -> Focusable pill background on selected item
    [✓] search icons (header__search--234/235/236) -> SearchIconGroup()
    [✓] avatar (header__avatar) -> Avatar circle with image
[✓] highlights (highlights__main-hero) -> HeroBanner(imageFileName="figma_image_1_13.png")
[✓] "Seguí viendo" section -> RowSection with PosterCard items (poster + progress + title + icons for card 1)
[✓] "Canales de TV" section -> RowSection with TvChannelCard items (left image + play + info + EN VIVO chip)
[✓] icons/badges/overlays -> SvgImage for SVG icons; TagChip("EN VIVO"); overlay image support on TvChannelCard
[✓] focus ring -> borderGlow + pill highlight; focusable() set on all interactive components
[✓] accessibility -> contentDescription, semantics role labels, button roles
[✓] images -> loaded via file:// using figmaImage helper from UI kit
**/

// PUBLIC_INTERFACE
class HomeActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StreamXTVTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = TvColors.Background) {
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
            .background(TvColors.Background)
            .padding(horizontal = 48.dp)
            .padding(top = 36.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        HeaderBar(
            items = listOf("Inicio", "Películas", "Series", "TV en vivo", "Kids", "Mis Contenidos")
        )
        Spacer(Modifier.height(32.dp))

        // Hero banner
        HeroBanner(
            imageFileName = "figma_image_1_13.png",
            width = 1744.dp,
            height = 444.dp
        )

        Spacer(Modifier.height(24.dp))

        // Seguí viendo: Poster cards with progress and icons on the first card
        val svItems = listOf(
            CardItem("Rogue One", "figma_image_1_41.png", trailingIcons = listOf("figma_image_1_63.svg", "figma_image_1_61.svg", "figma_image_1_65.svg")),
            CardItem("Ex Machina", "figma_image_1_68.png"),
            CardItem("Sing Street", "figma_image_1_85.png"),
            CardItem("2012", "figma_image_1_102.png"),
            CardItem("Ad Astra", "figma_image_1_119.png"),
        )
        RowSection(
            title = "Seguí viendo",
            items = svItems
        ) { item ->
            PosterCard(
                width = 412.dp,
                height = 312.dp,
                imageFileName = item.image,
                title = item.title,
                progress = 0.4f,
                trailingIcons = item.trailingIcons
            )
        }

        Spacer(Modifier.height(24.dp))

        // Canales de TV
        val tvItems = listOf(
            CardItem("Marca Claro Radio", "figma_image_1_154.png", subtitle = "004 | Claro sports"),
            CardItem("E.T.", "figma_image_1_179.png", subtitle = "005 | HBO Channel", overlay = "figma_image_1_180.png"),
            CardItem("Marca Claro Radio", "figma_image_1_218.png", subtitle = "004 | Claro sports"),
        )
        RowSection(
            title = "Canales de TV",
            items = tvItems
        ) { item ->
            TvChannelCard(
                item = item,
                width = 745.dp,
                height = 212.dp
            )
        }
    }
}

data class CardItem(
    val title: String,
    val image: String,
    val subtitle: String? = null,
    val overlay: String? = null,
    val trailingIcons: List<String> = emptyList()
)

@Composable
private fun HeaderBar(items: List<String>) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxSize()
            .height(74.dp)
    ) {
        // Logo pieces group (assembled from svg parts)
        LogoGroup(modifier = Modifier.width(170.dp).height(36.dp))

        Spacer(Modifier.width(24.dp))

        // Top navigation background with rounded pill
        Box(
            modifier = Modifier
                .weight(1f)
                .height(64.dp)
                .clip(RoundedCornerShape(TvRadius.R34))
                .background(TvColors.NavBg)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Search Icon composition (three svg shapes)
                SearchIconGroup()

                items.forEachIndexed { index, label ->
                    val focusedColor = Color.White
                    val unfocusedColor = TvColors.GrayText
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
                .background(TvColors.AccentRed.copy(alpha = 0.15f))
                .semantics { role = Role.Image }
                .focusable(true),
        ) {
            // Load actual avatar image (56x56)
            SvgOrImage("figma_image_1_231.png", "Avatar", Modifier.matchParentSize().clip(CircleShape))
        }
    }
}

@Composable
private fun SearchIconGroup() {
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(28.dp)
    ) {
        // Layer three search shapes to mimic HTML composition
        com.example.android_tv_frontend.ui.SvgImage(
            fileName = "figma_image_1_234.svg",
            contentDescription = "Search decorative A",
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.CenterStart)
        )
        com.example.android_tv_frontend.ui.SvgImage(
            fileName = "figma_image_1_236.svg",
            contentDescription = "Search decorative C",
            modifier = Modifier
                .size(22.dp)
                .align(Alignment.Center)
        )
        com.example.android_tv_frontend.ui.SvgImage(
            fileName = "figma_image_1_235.svg",
            contentDescription = "Search decorative B",
            modifier = Modifier
                .size(15.dp)
                .align(Alignment.BottomEnd)
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
    val pillColor = if (selected || focused) TvColors.ActivePill else Color.Transparent
    val textColor = if (selected || focused) focusedColor else unfocusedColor
    val requester = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(TvRadius.R37))
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
            style = TvTypography.Typo28.copy(color = textColor),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun LogoGroup(modifier: Modifier = Modifier) {
    // Assemble small logo glyphs as in HTML; placed within a small Box using offsets
    Box(modifier = modifier.clip(RoundedCornerShape(8.dp)).background(TvColors.NavBg)) {
        // Using offsets based on the HTML/CSS positions
        // 9 pieces compose the logo (positions approximated to fit Box)
        SvgImage("figma_image_1_249.svg", null, Modifier.size(16.dp).align(Alignment.CenterStart))
        SvgImage("figma_image_1_255.svg", null, Modifier.size(15.dp).align(Alignment.CenterStart).offset(x = 18.dp))
        SvgImage("figma_image_1_258.svg", null, Modifier.size(16.dp).align(Alignment.CenterStart).offset(x = 36.dp, y = (-4).dp))
        SvgImage("figma_image_1_261.svg", null, Modifier.size(15.dp).align(Alignment.CenterStart).offset(x = 54.dp, y = (-4).dp))
        SvgImage("figma_image_1_263.svg", null, Modifier.size(14.dp).align(Alignment.CenterStart).offset(x = 70.dp, y = (-12).dp))
        SvgImage("figma_image_1_267.svg", null, Modifier.size(19.dp).align(Alignment.CenterStart).offset(x = 86.dp))
        SvgImage("figma_image_1_268.svg", null, Modifier.size(10.dp).align(Alignment.CenterStart).offset(x = 107.dp, y = 6.dp))
        SvgImage("figma_image_1_269.svg", null, Modifier.size(23.dp).align(Alignment.CenterStart).offset(x = 122.dp))
        SvgImage("figma_image_1_274.svg", null, Modifier.size(11.dp).align(Alignment.CenterStart).offset(x = 150.dp, y = (-2).dp))
    }
}

@Composable
private fun SvgImage(fileName: String, contentDescription: String?, modifier: Modifier = Modifier) {
    com.example.android_tv_frontend.ui.SvgImage(fileName = fileName, contentDescription = contentDescription, modifier = modifier)
}

@Composable
private fun SvgOrImage(fileName: String, contentDescription: String?, modifier: Modifier = Modifier) {
    // Convenience: load either svg or png using AsyncImage with figmaImage helper
    val context = androidx.compose.ui.platform.LocalContext.current
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(figmaImage(fileName))
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
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
                .clip(RoundedCornerShape(TvRadius.R8))
                .background(TvColors.ProgressBg)
        ) {
            // Base image
            val context = androidx.compose.ui.platform.LocalContext.current
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(figmaImage(item.image))
                    .crossfade(true)
                    .build(),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .borderGlow(borderColor)
            )
            // Optional overlay image
            item.overlay?.let {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(figmaImage(it))
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            }

            // Play button (circular)
            TvIconButton(
                size = 92.dp,
                iconFileName = "figma_image_1_162.svg",
                contentDescription = "Reproducir",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = (-24).dp)
            )

            // Small progress
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .width(207.dp)
                    .height(10.dp)
                    .clip(RoundedCornerShape(TvRadius.R2_4))
                    .background(TvColors.ProgressBg.copy(alpha = 0.8f))
            ) {
                Box(
                    modifier = Modifier
                        .padding(0.8.dp)
                        .width(80.dp)
                        .height(8.8.dp)
                        .clip(RoundedCornerShape(TvRadius.R1_6))
                        .background(TvColors.ProgressFill)
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
                style = TvTypography.Typo25,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            item.subtitle?.let {
                Spacer(Modifier.height(6.dp))
                androidx.compose.material3.Text(
                    text = it,
                    style = TvTypography.Typo26,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.height(6.dp))
            androidx.compose.material3.Text(
                text = "11:30 - 12:30",
                style = TvTypography.Typo26
            )
            Spacer(Modifier.height(8.dp))
            TagChip(label = "EN VIVO")
        }
    }
}
