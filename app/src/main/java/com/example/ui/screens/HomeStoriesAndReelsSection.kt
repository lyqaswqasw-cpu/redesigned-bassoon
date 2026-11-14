package com.example.ui.screens

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.ContactMessageItem
import com.example.data.model.HomeCustomSectionItem
import com.example.data.model.HomeManagementConfig
import com.example.data.model.LeagueMatchesGroup
import com.example.data.model.LibraryMediaItem
import com.example.data.model.LiveChannel
import com.example.data.model.LiveEventMatch
import com.example.data.model.MatchItem
import com.example.data.model.MatchPublishedChannel
import com.example.data.model.PublishedEventItem
import com.example.data.model.ReelItem
import com.example.data.model.SocialUserProfile
import com.example.data.model.StoryCircleGroup
import com.example.data.model.StoryItem
import com.example.data.repository.FavoritesManager
import com.example.ui.components.FavoritesHeartTopBarButton
import com.example.ui.components.TelegramTopBarIconButton
import com.example.ui.components.tvFocusableClickable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random
import java.util.TimeZone

// ============================================================================
// FOUL TV Official Home Screen Brand Palette (Pure Black Dominance + Subtle Purple Accent)
// ============================================================================
val FoulHomeBgPrimary = Color(0xFF000000)
val FoulHomeBgSecondary = Color(0xFF17171D)
val FoulHomeCardBg = Color(0xFF101014)
val FoulHomePurplePrimary = Color(0xFF7625D9)
val FoulHomePurpleBright = Color(0xFF963EFF)
val FoulHomePurpleLight = Color(0xFF963EFF)
val FoulHomeTextPrimary = Color(0xFFFFFFFF)
val FoulHomeTextSecondary = Color(0xFFA1A1AA)
val FoulHomeVerifiedBlue = Color(0xFF1D9BF0)
val FoulHomeBorderDark = Color(0xFF292930)
val FoulHomeBorderPurple = Color(0xFF292930)
val FoulHomeWatchedRing = Color(0xFF24242C)

// In-memory caches for extracted Reel cover frames & durations so scrolling is 60fps smooth
private val HomeReelCoverBitmapCache = mutableStateMapOf<String, Bitmap>()
private val HomeReelDurationCache = mutableStateMapOf<String, String>()

/**
 * ============================================================================
 * 1. FOUL TV PROFESSIONAL ICON-ONLY HEADER (الشريط العلوي الاحترافي — مطابق للصورة المرجعية)
 * ============================================================================
 * Equal 40.dp rounded square buttons (#101014 background, #292930 border):
 * - Right side (in RTL): Matches Schedule icon button (purple soccer icon) + Heart Favorites icon button
 * - Left side (in RTL): Refresh icon button (white refresh icon) + Official Telegram Channel icon button (blue)
 */
@Composable
fun FoulHomeBroadcastHeader(
    telegramUrl: String,
    onOpenMatchesSchedule: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(FoulHomeBgPrimary)
            .statusBarsPadding()
            .testTag("foul_home_header")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Right Side (in RTL): Matches Schedule Icon + Heart Favorites Icon (40.dp equal boxes)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(FoulHomeCardBg)
                        .border(
                            width = 1.dp,
                            color = FoulHomeBorderDark,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .tvFocusableClickable(
                            shape = RoundedCornerShape(12.dp),
                            scaleOnFocus = 1.06f,
                            onClick = onOpenMatchesSchedule
                        )
                        .testTag("home_top_matches_schedule_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsSoccer,
                        contentDescription = "جدول المباريات",
                        tint = FoulHomePurpleBright,
                        modifier = Modifier.size(20.dp)
                    )
                }

                FavoritesHeartTopBarButton()
            }

            // Left Side (in RTL): Refresh Icon + Official Telegram Channel Icon (40.dp equal boxes)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(FoulHomeCardBg)
                        .border(1.dp, FoulHomeBorderDark, RoundedCornerShape(12.dp))
                        .tvFocusableClickable(
                            shape = RoundedCornerShape(12.dp),
                            scaleOnFocus = 1.06f,
                            onClick = onRefresh
                        )
                        .testTag("home_refresh_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "تحديث",
                        tint = FoulHomeTextPrimary,
                        modifier = Modifier.size(19.dp)
                    )
                }

                TelegramTopBarIconButton(
                    telegramUrl = telegramUrl,
                    modifier = Modifier.testTag("home_telegram_button")
                )
            }
        }
    }
}

/**
 * ============================================================================
 * 1B. HOME APP-WIDE SEARCH BAR + FILTER BUTTON (شريط البحث الفعلي + زر الفلترة)
 * ============================================================================
 * Placed directly underneath the Top Bar on the Home Screen:
 * - Right side (in RTL): Filter button (46.dp rounded square, #101014 bg, #292930 border)
 * - Left side (in RTL): Search box ("ابحث في التطبيق...", #101014 bg, #292930 border, search icon on left)
 * - Collapsible filter pills ("الكل", "الريلز", "المباريات", "القنوات", "الأفلام والمسلسلات")
 */
enum class HomeSearchFilterCategory(val label: String) {
    ALL("الكل"),
    REELS("الريلز"),
    MATCHES("المباريات"),
    CHANNELS("القنوات"),
    MEDIA("الأفلام والمسلسلات")
}

@Composable
fun FoulHomeSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: HomeSearchFilterCategory,
    onSelectCategory: (HomeSearchFilterCategory) -> Unit,
    showFilterBar: Boolean,
    onToggleFilterBar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(FoulHomeBgPrimary)
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .testTag("foul_home_search_section"),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Right Side in RTL: Filter Button (matching reference screenshot)
            val isFilterHighlighted = showFilterBar || selectedCategory != HomeSearchFilterCategory.ALL
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isFilterHighlighted) FoulHomeBgSecondary else FoulHomeCardBg)
                    .border(
                        width = 1.dp,
                        color = if (isFilterHighlighted) FoulHomePurpleBright else FoulHomeBorderDark,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .tvFocusableClickable(
                        shape = RoundedCornerShape(14.dp),
                        scaleOnFocus = 1.06f,
                        onClick = onToggleFilterBar
                    )
                    .testTag("home_search_filter_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "فلترة البحث",
                    tint = if (isFilterHighlighted) FoulHomePurpleBright else Color.White,
                    modifier = Modifier.size(21.dp)
                )
            }

            // Search Input Box (expands to fill remaining width)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(FoulHomeCardBg)
                    .border(
                        width = 1.dp,
                        color = if (searchQuery.isNotBlank()) FoulHomePurpleBright.copy(alpha = 0.75f) else FoulHomeBorderDark,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(horizontal = 14.dp)
                    .testTag("home_search_input_box"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Text input on the right (RTL start)
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "ابحث في التطبيق...",
                                color = FoulHomeTextSecondary,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        androidx.compose.foundation.text.BasicTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            singleLine = true,
                            textStyle = androidx.compose.ui.text.TextStyle(
                                color = FoulHomeTextPrimary,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Start
                            ),
                            cursorBrush = androidx.compose.ui.graphics.SolidColor(FoulHomePurpleBright),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("home_search_text_field")
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Search / Clear icon on the left (RTL end)
                    if (searchQuery.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(FoulHomeBgSecondary)
                                .clickable { onSearchQueryChange("") }
                                .testTag("home_search_clear_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "مسح البحث",
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "بحث",
                            tint = FoulHomeTextSecondary,
                            modifier = Modifier.size(21.dp)
                        )
                    }
                }
            }
        }

        // Collapsible Filter Category Chips Row
        AnimatedVisibility(visible = showFilterBar || searchQuery.isNotBlank()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_search_filter_chips_row"),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(HomeSearchFilterCategory.entries) { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        color = if (isSelected) FoulHomePurplePrimary else FoulHomeCardBg,
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (isSelected) FoulHomePurpleBright else FoulHomeBorderDark
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable { onSelectCategory(category) }
                            .testTag("home_filter_chip_${category.name.lowercase()}")
                    ) {
                        Text(
                            text = category.label,
                            color = if (isSelected) Color.White else FoulHomeTextSecondary,
                            fontSize = 11.5.sp,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeGlowingDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(1.dp)
            .background(Color(0xFF16161C))
    )
}

/**
 * ============================================================================
 * 2. SECTION HEADER COMPONENT (Matches Reference Image Layout)
 * ============================================================================
 * In RTL:
 * - Right side (Start): Section Icon Badge + Section Title in bold white
 * - Left side (End): "مشاهدة الكل <" pill button (#101014 bg, #292930 border)
 */
@Composable
fun FoulHomeSectionHeader(
    title: String,
    iconBadge: @Composable () -> Unit,
    seeAllText: String = "مشاهدة الكل",
    seeAllTestTag: String,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Right side (in RTL): Icon Badge + Bold Section Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            iconBadge()
            Text(
                text = title,
                color = FoulHomeTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        // Left side (in RTL): "مشاهدة الكل <" Pill Button (#101014 bg, #292930 border)
        Surface(
            color = FoulHomeCardBg,
            shape = RoundedCornerShape(50),
            border = BorderStroke(1.dp, FoulHomeBorderDark),
            modifier = Modifier
                .height(30.dp)
                .clip(RoundedCornerShape(50))
                .tvFocusableClickable(
                    shape = RoundedCornerShape(50),
                    scaleOnFocus = 1.05f,
                    onClick = onSeeAllClick
                )
                .testTag(seeAllTestTag)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = seeAllText,
                    color = FoulHomeTextPrimary,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = seeAllText,
                    tint = FoulHomeTextSecondary,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

/**
 * ============================================================================
 * 3. STORIES SECTION (قسم القصص — مطابق للصورة المرجعية)
 * ============================================================================
 * Displays:
 * 1. كرة القدم (Verified)
 * 2. FOUL TV (Verified)
 * 3. المباريات (Verified)
 * 4. الأفلام (Verified)
 * 5. المسلسلات (Verified)
 * Unified dark gray circular borders (#292930 / #383842) — NO purple neon rings!
 */
@Composable
fun HomeStoriesSection(
    storyGroups: List<StoryCircleGroup>,
    onOpenStoryGroup: (StoryCircleGroup) -> Unit,
    onSeeAllStoriesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 10.dp)
            .testTag("home_stories_section")
    ) {
        FoulHomeSectionHeader(
            title = "القصص",
            iconBadge = {
                // Vertical purple accent bar matching the reference image (| القصص)
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(FoulHomePurpleBright, FoulHomePurplePrimary)
                            )
                        )
                )
            },
            seeAllTestTag = "stories_see_all_button",
            onSeeAllClick = onSeeAllStoriesClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("home_stories_row"),
            contentPadding = PaddingValues(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(
                items = storyGroups,
                key = { it.id }
            ) { group ->
                StoryCircleItem(
                    group = group,
                    onClick = { onOpenStoryGroup(group) }
                )
            }
        }
    }
}

@Composable
private fun StoryCircleItem(
    group: StoryCircleGroup,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    // Unified dark gray border matching the reference image (no purple/gradient ring)
    val outerBorderColor = if (group.isAllWatched) {
        Color(0xFF222229)
    } else {
        Color(0xFF353540)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .width(78.dp)
            .tvFocusableClickable(
                shape = RoundedCornerShape(14.dp),
                scaleOnFocus = 1.05f,
                onClick = onClick
            )
            .testTag("story_circle_${group.id}")
    ) {
        Box(
            modifier = Modifier
                .size(74.dp)
                .clip(CircleShape)
                .background(Color(0xFF0A0A0E))
                .border(
                    width = 2.dp,
                    color = outerBorderColor,
                    shape = CircleShape
                )
                .padding(4.dp)
                .clip(CircleShape)
                .background(Color(0xFF0D0D11))
                .border(
                    width = 1.dp,
                    color = FoulHomeBorderDark,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (group.isOfficialFoulLogo) {
                Image(
                    painter = painterResource(id = R.drawable.ic_foul_tv_launcher),
                    contentDescription = group.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                // Fallback category icon underneath AsyncImage (white icon on dark #0D0D11 circle)
                CategoryStoryArtworkPlaceholder(categoryKey = group.categoryKey)

                if (group.avatarUrl.isNotBlank()) {
                    val imgReq = remember(group.avatarUrl) {
                        ImageRequest.Builder(context)
                            .data(group.avatarUrl)
                            .size(220)
                            .crossfade(true)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .build()
                    }
                    AsyncImage(
                        model = imgReq,
                        contentDescription = group.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
            }
        }

        // Title + Official Verified Badge (✓) underneath
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = group.title,
                color = if (group.isAllWatched) FoulHomeTextSecondary else FoulHomeTextPrimary,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            if (group.isVerified) {
                Spacer(modifier = Modifier.width(3.dp))
                VerifiedStoryCheckBadge()
            }
        }
    }
}

@Composable
private fun VerifiedStoryCheckBadge() {
    Box(
        modifier = Modifier
            .size(13.dp)
            .clip(CircleShape)
            .background(FoulHomeVerifiedBlue),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "موثق",
            tint = Color.White,
            modifier = Modifier.size(9.dp)
        )
    }
}

@Composable
private fun CategoryStoryArtworkPlaceholder(categoryKey: String) {
    val icon = when (categoryKey) {
        "matches" -> Icons.Default.Public
        "movies" -> Icons.Default.Movie
        "series" -> Icons.Default.LiveTv
        "football" -> Icons.Default.SportsSoccer
        else -> Icons.Default.SmartDisplay
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D11)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}

/**
 * ============================================================================
 * 4. REELS HORIZONTAL SECTION (قسم الريلز — مطابق للصورة المرجعية)
 * ============================================================================
 * Displays horizontal scrollable Reel cards with:
 * - Large vertical video thumbnail (168.dp x 224.dp) with dark gray border (#292930)
 * - Top-left duration badge (00:59)
 * - Top-right 3-dots options icon button (opens smart link share/copy dialog)
 * - Bottom row: Creator name pill badge (#17171D bg, #292930 border) + Heart & Likes counter
 * - Short caption below in bold white
 */
@Composable
fun HomeReelsHorizontalSection(
    reels: List<ReelItem>,
    isLoading: Boolean,
    onReelClick: (ReelItem) -> Unit,
    onSeeAllReelsClick: () -> Unit,
    onReelOptionsClick: (ReelItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag("home_reels_section")
    ) {
        FoulHomeSectionHeader(
            title = "ريلز",
            iconBadge = {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(FoulHomePurpleBright, FoulHomePurplePrimary)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "ريلز",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            },
            seeAllTestTag = "home_reels_see_all_button",
            onSeeAllClick = onSeeAllReelsClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        when {
            isLoading && reels.isEmpty() -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .width(168.dp)
                                .height(224.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(FoulHomeCardBg)
                                .border(1.dp, FoulHomeBorderDark, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = FoulHomePurpleBright,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            reels.isEmpty() -> {
                Surface(
                    color = FoulHomeCardBg,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, FoulHomeBorderDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "لا توجد مقاطع ريلز منشورة حالياً",
                                color = FoulHomeTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "انتقل إلى قسم الريلز لمشاهدة أحدث المقاطع أو نشر مقطعك الأول",
                                color = FoulHomeTextSecondary,
                                fontSize = 11.5.sp
                            )
                        }
                        Button(
                            onClick = onSeeAllReelsClick,
                            colors = ButtonDefaults.buttonColors(containerColor = FoulHomePurplePrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("فتح ريلز", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            else -> {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_reels_lazy_row"),
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(
                        items = reels,
                        key = { _, reel -> "home_reel_top_${reel.id}" }
                    ) { index, reel ->
                        HomeReelVideoCard(
                            reel = reel,
                            cardIndex = index,
                            showViewsInsteadOfLikes = false,
                            onClick = { onReelClick(reel) },
                            onOptionsClick = { onReelOptionsClick(reel) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * ============================================================================
 * 5. ADDITIONAL REELS SECTION ("فيديوهات قد تعجبك" — أسفل قسم المباريات المقترحة)
 * ============================================================================
 * Placed after Suggested Matches to highlight additional Reels videos from the
 * same Reels data source with distinct ordering, duration, options icon, and caption.
 */
@Composable
fun HomeMoreReelsVideosSection(
    reels: List<ReelItem>,
    onReelClick: (ReelItem) -> Unit,
    onSeeAllReelsClick: () -> Unit,
    onReelOptionsClick: (ReelItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (reels.isEmpty()) return

    val distinctReels = remember(reels) {
        if (reels.size > 3) {
            reels.drop(2) + reels.take(2)
        } else {
            reels.reversed()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag("home_more_reels_section")
    ) {
        FoulHomeSectionHeader(
            title = "فيديوهات قد تعجبك",
            iconBadge = {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(FoulHomePurpleBright, FoulHomePurplePrimary)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "فيديوهات قد تعجبك",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            },
            seeAllTestTag = "home_more_reels_see_all_button",
            onSeeAllClick = onSeeAllReelsClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("home_more_reels_lazy_row"),
            contentPadding = PaddingValues(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(
                items = distinctReels,
                key = { _, reel -> "home_reel_more_${reel.id}" }
            ) { index, reel ->
                HomeReelVideoCard(
                    reel = reel,
                    cardIndex = index + 4,
                    showViewsInsteadOfLikes = false,
                    onClick = { onReelClick(reel) },
                    onOptionsClick = { onReelOptionsClick(reel) }
                )
            }
        }
    }
}

@Composable
private fun HomeReelVideoCard(
    reel: ReelItem,
    cardIndex: Int,
    showViewsInsteadOfLikes: Boolean,
    onClick: () -> Unit,
    onOptionsClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var coverBitmap by remember(reel.id) {
        mutableStateOf(HomeReelCoverBitmapCache[reel.id])
    }
    var durationText by remember(reel.id) {
        mutableStateOf(HomeReelDurationCache[reel.id] ?: fallbackDurationForReel(reel.id, cardIndex))
    }

    // Asynchronously extract video cover frame & real duration without blocking UI or starting playback
    LaunchedEffect(reel.id, reel.streamUrl, reel.videoUrl) {
        if (coverBitmap != null && HomeReelDurationCache.containsKey(reel.id)) return@LaunchedEffect
        val targetUrl = reel.streamUrl.ifBlank { reel.videoUrl }
        if (targetUrl.isBlank()) return@LaunchedEffect

        withContext(Dispatchers.IO) {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(
                    targetUrl,
                    hashMapOf("User-Agent" to "FOUL-TV-Android/1.2.2")
                )
                val durMsStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                val durMs = durMsStr?.toLongOrNull() ?: 0L
                if (durMs > 0L) {
                    val totalSecs = (durMs / 1000L).toInt().coerceAtLeast(1)
                    val mins = totalSecs / 60
                    val secs = totalSecs % 60
                    val formatted = String.format(Locale.US, "%02d:%02d", mins, secs)
                    HomeReelDurationCache[reel.id] = formatted
                    durationText = formatted
                }

                if (coverBitmap == null && reel.thumbnailUrl.isBlank()) {
                    val rawFrame = retriever.getFrameAtTime(
                        1_000_000L,
                        MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                    ) ?: retriever.frameAtTime
                    if (rawFrame != null) {
                        val targetW = 300
                        val ratio = rawFrame.height.toFloat() / rawFrame.width.toFloat().coerceAtLeast(1f)
                        val targetH = (targetW * ratio).toInt().coerceIn(220, 520)
                        val scaled = Bitmap.createScaledBitmap(rawFrame, targetW, targetH, true)
                        HomeReelCoverBitmapCache[reel.id] = scaled
                        coverBitmap = scaled
                    }
                }
            } catch (_: Exception) {
            } finally {
                runCatching { retriever.release() }
            }
        }
    }

    val cardShape = RoundedCornerShape(16.dp)
    val creatorShortName = reel.userPersonalName.trim()
        .ifBlank { reel.userName.trim() }
        .ifBlank { "FOUL TV" }
    val displayTitle = reel.caption.trim()
        .ifBlank { reel.overlayText.trim() }
        .ifBlank { "مقطع ريلز • $creatorShortName" }

    Card(
        shape = cardShape,
        colors = CardDefaults.cardColors(containerColor = FoulHomeCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .width(168.dp)
            .height(224.dp)
            .clip(cardShape)
            .border(
                width = 1.dp,
                color = FoulHomeBorderDark,
                shape = cardShape
            )
            .tvFocusableClickable(
                shape = cardShape,
                scaleOnFocus = 1.04f,
                onClick = onClick
            )
            .testTag("home_reel_card_${reel.id}")
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. Background Thumbnail / Extracted Cover Frame / Creator Photo Fallback
            when {
                coverBitmap != null -> {
                    Image(
                        bitmap = coverBitmap!!.asImageBitmap(),
                        contentDescription = displayTitle,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                reel.thumbnailUrl.isNotBlank() -> {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(reel.thumbnailUrl)
                            .size(360)
                            .crossfade(true)
                            .build(),
                        contentDescription = displayTitle,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                reel.userPhotoUrl.isNotBlank() -> {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(reel.userPhotoUrl)
                            .size(360)
                            .crossfade(true)
                            .build(),
                        contentDescription = displayTitle,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF191922),
                                        FoulHomeCardBg,
                                        FoulHomeBgPrimary
                                    )
                                )
                            )
                    )
                }
            }

            // 2. Smooth top & bottom dark gradient overlay for crisp text legibility
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x66000000),
                                Color.Transparent,
                                Color(0x99000000),
                                Color(0xF0000000)
                            )
                        )
                    )
            )

            // 3. Top Row: Right side (in RTL) = 3-dots options button, Left side (in RTL) = Duration badge ("00:59")
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Right side in RTL: 3-dots options icon button (matching reference image)
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.45f))
                        .clickable { onOptionsClick() }
                        .testTag("home_reel_options_${reel.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "خيارات الريلز",
                        tint = Color.White,
                        modifier = Modifier.size(17.dp)
                    )
                }

                // Left side in RTL: Duration pill ("00:59")
                Surface(
                    color = Color(0xCC0A0A0E),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(0.8.dp, FoulHomeBorderDark)
                ) {
                    Text(
                        text = durationText,
                        color = Color.White,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp)
                    )
                }
            }

            // 4. Bottom Metadata: Creator Pill + Likes Counter + Short Caption
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Right side in RTL: Creator Name Pill Badge (matching reference image: "علي" inside dark pill)
                    Surface(
                        color = Color(0xE617171D),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, FoulHomeBorderDark),
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Text(
                            text = creatorShortName,
                            color = FoulHomePurpleBright,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Left side in RTL: Likes or Views badge (matching reference image ♥ 234K)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val metricValue = if (showViewsInsteadOfLikes) {
                            reel.viewsCount
                        } else {
                            reel.likesCount
                        }
                        Text(
                            text = formatHomeMetricCount(metricValue),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Icon(
                            imageVector = if (showViewsInsteadOfLikes) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.Favorite
                            },
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }

                // Short Caption below in bold white
                Text(
                    text = displayTitle,
                    color = FoulHomeTextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * ============================================================================
 * 6. SUGGESTED MATCHES SECTION (قسم المباريات المقترحة — مطابق للصورة المرجعية)
 * ============================================================================
 * Strictly uses real match schedule data (`leagueGroups` from 365Scores +
 * `matchPublishedChannels` + `allChannels`), sorted by:
 * 1. Live matches right now
 * 2. Upcoming matches closest in time
 * 3. Important matches by tournament priority
 * 4. Remaining matches in the schedule
 */
@Composable
fun HomeSuggestedMatchesSection(
    leagueGroups: List<LeagueMatchesGroup>,
    allChannels: List<LiveChannel>,
    liveEvents: List<LiveEventMatch>,
    matchPublishedChannels: List<MatchPublishedChannel>,
    selectedDateString: String,
    isLoadingMatches: Boolean,
    onMatchClick: (MatchItem) -> Unit,
    onWatchMatchChannels: (MatchItem, List<LiveChannel>) -> Unit,
    onSeeAllMatchesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Build prioritized list of real matches
    val suggestedMatches = remember(leagueGroups) {
        val allWithPriority = leagueGroups.flatMap { group ->
            group.matches.map { match -> match to group.priorityRank }
        }
        allWithPriority.sortedWith(
            compareBy<Pair<MatchItem, Int>> { (match, _) ->
                when {
                    match.isLive -> 0 // 1. Live matches now
                    !match.isFinished -> 1 // 2. Upcoming matches
                    else -> 2 // 3. Finished matches last
                }
            }.thenBy { (match, priorityRank) ->
                if (!match.isFinished) priorityRank else priorityRank + 1000
            }.thenBy { (match, _) ->
                match.startTimeIso
            }
        ).map { it.first }.take(10)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag("home_suggested_matches_section")
    ) {
        FoulHomeSectionHeader(
            title = "مباريات مقترحة",
            iconBadge = {
                Icon(
                    imageVector = Icons.Default.SportsSoccer,
                    contentDescription = "مباريات مقترحة",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            },
            seeAllTestTag = "home_matches_see_all_button",
            onSeeAllClick = onSeeAllMatchesClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        when {
            isLoadingMatches && suggestedMatches.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(178.dp)
                        .padding(horizontal = 14.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(FoulHomeCardBg)
                        .border(1.dp, FoulHomeBorderDark, RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CircularProgressIndicator(
                            color = FoulHomePurpleBright,
                            strokeWidth = 2.2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "جاري جلب جدول المباريات المقترحة...",
                            color = FoulHomeTextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            suggestedMatches.isEmpty() -> {
                Surface(
                    color = FoulHomeCardBg,
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, FoulHomeBorderDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "لا توجد مباريات مجدولة في هذا التوقيت",
                                color = FoulHomeTextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "اضغط على مشاهدة الكل لتصفح جدول المباريات الكامل حسب التاريخ",
                                color = FoulHomeTextSecondary,
                                fontSize = 11.5.sp
                            )
                        }
                        Button(
                            onClick = onSeeAllMatchesClick,
                            colors = ButtonDefaults.buttonColors(containerColor = FoulHomePurplePrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("جدول المباريات", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            suggestedMatches.size == 1 -> {
                val match = suggestedMatches.first()
                val playableChannels = remember(match, matchPublishedChannels, allChannels, liveEvents) {
                    resolvePlayableChannelsForHomeMatch(
                        match = match,
                        matchPublishedChannels = matchPublishedChannels,
                        allChannels = allChannels,
                        liveEvents = liveEvents
                    )
                }
                Box(modifier = Modifier.padding(horizontal = 14.dp)) {
                    SuggestedMatchLuxuryCard(
                        match = match,
                        selectedDateString = selectedDateString,
                        playableChannels = playableChannels,
                        onMatchClick = { onMatchClick(match) },
                        onWatchMatchClick = {
                            if (playableChannels.isNotEmpty()) {
                                onWatchMatchChannels(match, playableChannels)
                            } else {
                                onMatchClick(match)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            else -> {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_suggested_matches_row"),
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = suggestedMatches,
                        key = { "suggested_match_${it.id}" }
                    ) { match ->
                        val playableChannels = remember(match, matchPublishedChannels, allChannels, liveEvents) {
                            resolvePlayableChannelsForHomeMatch(
                                match = match,
                                matchPublishedChannels = matchPublishedChannels,
                                allChannels = allChannels,
                                liveEvents = liveEvents
                            )
                        }
                        SuggestedMatchLuxuryCard(
                            match = match,
                            selectedDateString = selectedDateString,
                            playableChannels = playableChannels,
                            onMatchClick = { onMatchClick(match) },
                            onWatchMatchClick = {
                                if (playableChannels.isNotEmpty()) {
                                    onWatchMatchChannels(match, playableChannels)
                                } else {
                                    onMatchClick(match)
                                }
                            },
                            modifier = Modifier.width(338.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Resolves all genuinely playable LiveChannels for a MatchItem:
 * 1. Channels explicitly linked via `matchPublishedChannels` or `liveEvents`.
 * 2. Playable channels matching any of the match's `tvBroadcasters` names.
 */
private fun resolvePlayableChannelsForHomeMatch(
    match: MatchItem,
    matchPublishedChannels: List<MatchPublishedChannel>,
    allChannels: List<LiveChannel>,
    liveEvents: List<LiveEventMatch>
): List<LiveChannel> {
    val direct = resolveChannelsForMatch(
        match = match,
        matchPublishedChannels = matchPublishedChannels,
        allChannels = allChannels,
        liveEvents = liveEvents
    ).toMutableList()

    val seenIds = direct.map { it.id }.toMutableSet()
    for (broadcaster in match.tvBroadcasters) {
        val matchedCh = findPlayableChannelForBroadcaster(
            broadcasterName = broadcaster.name,
            publishedChannels = direct,
            allChannels = allChannels
        )
        if (matchedCh != null && seenIds.add(matchedCh.id)) {
            direct.add(matchedCh)
        }
    }
    return direct
}

/**
 * Dark-Bordered Broadcast Match Card closely matching the reference image:
 * - Pure dark charcoal/black card background (#0B0B0F -> #101014) with 1.dp #292930 border
 * - Competition name & status badge at top
 * - Home Team & Away Team equal circular crest badges (52.dp) + names in white
 * - Center purple kickoff time/score pill (#7625D9 / #963EFF) + bold white "VS"
 * - Formatted Arabic match date + purple "مشاهدة المباراة ▶" button at bottom
 */
@Composable
private fun SuggestedMatchLuxuryCard(
    match: MatchItem,
    selectedDateString: String,
    playableChannels: List<LiveChannel>,
    onMatchClick: () -> Unit,
    onWatchMatchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val cardShape = RoundedCornerShape(18.dp)
    val formattedDateAr = remember(match.startTimeIso, selectedDateString) {
        formatMatchDateArabic(match.startTimeIso, selectedDateString)
    }

    val statusLabel = when {
        match.isLive -> "مباشرة الآن"
        match.isFinished -> "انتهت"
        else -> "قادمة"
    }

    Card(
        shape = cardShape,
        colors = CardDefaults.cardColors(containerColor = FoulHomeCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .height(184.dp)
            .clip(cardShape)
            .border(
                width = 1.dp,
                color = FoulHomeBorderDark,
                shape = cardShape
            )
            .tvFocusableClickable(
                shape = cardShape,
                scaleOnFocus = 1.02f,
                onClick = onMatchClick
            )
            .testTag("suggested_match_card_${match.id}")
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF13131A),
                            Color(0xFF0D0D12),
                            Color(0xFF09090D)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 1. Top Row: Competition Logo + Competition Name + Status Pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF17171D))
                                .border(1.dp, FoulHomeBorderDark, CircleShape)
                                .padding(3.5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(match.competitionLogoUrl)
                                    .size(90)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = match.competitionName,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Text(
                            text = match.competitionName,
                            color = FoulHomeTextSecondary,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Match Status Badge ("مباشرة الآن" / "قادمة" / "انتهت")
                    Surface(
                        color = when {
                            match.isLive -> Color(0xFFDC2626).copy(alpha = 0.20f)
                            match.isFinished -> Color(0xFF17171D)
                            else -> Color(0xFF17171D)
                        },
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(
                            1.dp,
                            when {
                                match.isLive -> Color(0xFFEF4444)
                                else -> FoulHomeBorderDark
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 2.5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            if (match.isLive) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEF4444))
                                )
                            }
                            Text(
                                text = if (match.isLive) "LIVE • $statusLabel" else statusLabel,
                                color = if (match.isLive) Color(0xFFFCA5A5) else FoulHomeTextSecondary,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                // 2. Center Row: Team 1 vs Team 2 + Time/Score Pill
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Home Team (Team 1)
                    MatchTeamBadgeColumn(
                        teamName = match.homeTeam.name,
                        logoUrl = match.homeTeam.logoUrl,
                        modifier = Modifier.weight(1f)
                    )

                    // Center Kickoff Time or Live/Final Score + VS
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        // Purple Pill for Time (upcoming) or Score (live/finished) matching reference image
                        Surface(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = if (match.isLive) {
                                            listOf(Color(0xFFDC2626), FoulHomePurplePrimary)
                                        } else {
                                            listOf(FoulHomePurpleBright, FoulHomePurplePrimary)
                                        }
                                    )
                                )
                        ) {
                            val centerPillText = when {
                                match.isLive || match.isFinished || (match.homeTeam.hasScore && match.awayTeam.hasScore) -> {
                                    "${match.homeTeam.score} - ${match.awayTeam.score}"
                                }
                                else -> match.displayTime.ifBlank { "--:--" }
                            }
                            Text(
                                text = centerPillText,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 3.5.dp)
                            )
                        }

                        Text(
                            text = if (match.isLive && match.gameTimeDisplay.isNotBlank()) {
                                match.gameTimeDisplay
                            } else {
                                "VS"
                            },
                            color = Color.White,
                            fontSize = 15.5.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }

                    // Away Team (Team 2)
                    MatchTeamBadgeColumn(
                        teamName = match.awayTeam.name,
                        logoUrl = match.awayTeam.logoUrl,
                        modifier = Modifier.weight(1f)
                    )
                }

                // 3. Bottom Row: Match Date + "مشاهدة المباراة" Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formattedDateAr,
                        color = FoulHomeTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    // Show "مشاهدة المباراة" button when playable channels exist (or to open match broadcast details)
                    Surface(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, FoulHomePurpleBright.copy(alpha = 0.7f)),
                        modifier = Modifier
                            .height(32.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(FoulHomePurplePrimary, Color(0xFF5B18B0))
                                )
                            )
                            .tvFocusableClickable(
                                shape = RoundedCornerShape(50),
                                scaleOnFocus = 1.05f,
                                onClick = onWatchMatchClick
                            )
                            .testTag("watch_suggested_match_button_${match.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "مشاهدة المباراة",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MatchTeamBadgeColumn(
    teamName: String,
    logoUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Color(0xFF14141B))
                .border(
                    width = 1.dp,
                    color = FoulHomeBorderDark,
                    shape = CircleShape
                )
                .padding(7.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(logoUrl)
                    .size(140)
                    .crossfade(true)
                    .build(),
                contentDescription = teamName,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }

        Text(
            text = teamName,
            color = FoulHomeTextPrimary,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ChampionshipTrophyWatermark(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val trophyColor = FoulHomePurpleLight.copy(alpha = 0.12f)
        val stroke = Stroke(width = 2.dp.toPx())

        // Cup body
        val cupPath = Path().apply {
            moveTo(w * 0.28f, h * 0.18f)
            lineTo(w * 0.72f, h * 0.18f)
            cubicTo(w * 0.70f, h * 0.52f, w * 0.58f, h * 0.62f, w * 0.50f, h * 0.64f)
            cubicTo(w * 0.42f, h * 0.62f, w * 0.30f, h * 0.52f, w * 0.28f, h * 0.18f)
            close()
        }
        drawPath(cupPath, color = trophyColor)
        drawPath(cupPath, color = FoulHomePurpleBright.copy(alpha = 0.22f), style = stroke)

        // Handles
        drawCircle(
            color = FoulHomePurpleBright.copy(alpha = 0.16f),
            radius = w * 0.16f,
            center = Offset(w * 0.26f, h * 0.34f),
            style = stroke
        )
        drawCircle(
            color = FoulHomePurpleBright.copy(alpha = 0.16f),
            radius = w * 0.16f,
            center = Offset(w * 0.74f, h * 0.34f),
            style = stroke
        )

        // Stem & Base
        drawLine(
            color = FoulHomePurpleBright.copy(alpha = 0.22f),
            start = Offset(w * 0.50f, h * 0.64f),
            end = Offset(w * 0.50f, h * 0.80f),
            strokeWidth = 3.dp.toPx()
        )
        drawLine(
            color = FoulHomePurpleBright.copy(alpha = 0.26f),
            start = Offset(w * 0.32f, h * 0.82f),
            end = Offset(w * 0.68f, h * 0.82f),
            strokeWidth = 4.dp.toPx()
        )
    }
}

/**
 * ============================================================================
 * 7. FULL-SCREEN INTERACTIVE STORY VIEWER (عارض قصص كامل الشاشة)
 * ============================================================================
 * Supports:
 * - Progress indicator bars for all stories in the selected circle
 * - Previous / Next story navigation (tap right/left or swipe/buttons)
 * - Video playback via ExoPlayer or Image/Match/Movie preview
 * - Action button to jump directly to the Reel, Match, or Movie/Series
 */
@OptIn(UnstableApi::class)
@Composable
fun FullScreenStoryViewerDialog(
    group: StoryCircleGroup,
    allGroups: List<StoryCircleGroup>,
    onMarkStoryWatched: (String) -> Unit,
    onOpenLinkedReel: (String) -> Unit,
    onOpenLinkedMatch: (Int) -> Unit,
    onOpenLinkedMedia: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var currentGroupIndex by remember(group.id) {
        mutableIntStateOf(allGroups.indexOfFirst { it.id == group.id }.coerceAtLeast(0))
    }
    val activeGroup = allGroups.getOrNull(currentGroupIndex) ?: group
    var currentStoryIndex by remember(activeGroup.id) { mutableIntStateOf(0) }
    val stories = activeGroup.stories
    val activeStory = stories.getOrNull(currentStoryIndex)

    LaunchedEffect(activeStory?.id) {
        if (activeStory != null) {
            onMarkStoryWatched(activeStory.id)
        }
    }

    val advanceNext: () -> Unit = {
        if (currentStoryIndex < stories.lastIndex) {
            currentStoryIndex += 1
        } else if (currentGroupIndex < allGroups.lastIndex) {
            currentGroupIndex += 1
            currentStoryIndex = 0
        } else {
            onDismiss()
        }
    }

    val goBackPrevious: () -> Unit = {
        if (currentStoryIndex > 0) {
            currentStoryIndex -= 1
        } else if (currentGroupIndex > 0) {
            currentGroupIndex -= 1
            currentStoryIndex = 0
        }
    }

    // Auto-advance timer for image/match/media stories (7 seconds)
    LaunchedEffect(activeStory?.id, activeStory?.mediaType) {
        if (activeStory != null && activeStory.mediaType != "VIDEO") {
            delay(7000L)
            advanceNext()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FoulHomeBgPrimary)
                .statusBarsPadding()
                .testTag("fullscreen_story_viewer")
        ) {
            if (activeStory == null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "لا توجد قصص متاحة حالياً في هذا القسم",
                        color = FoulHomeTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = FoulHomePurplePrimary)
                    ) {
                        Text("إغلاق", color = Color.White)
                    }
                }
            } else {
                // Media Content Layer
                if (activeStory.mediaType == "VIDEO" && activeStory.mediaUrl.isNotBlank()) {
                    val exoPlayer = remember(activeStory.id) {
                        ExoPlayer.Builder(context).build().apply {
                            setMediaItem(MediaItem.fromUri(activeStory.mediaUrl))
                            repeatMode = Player.REPEAT_MODE_ONE
                            playWhenReady = true
                            prepare()
                        }
                    }
                    DisposableEffect(activeStory.id) {
                        onDispose {
                            runCatching { exoPlayer.release() }
                        }
                    }
                    AndroidView(
                        factory = { ctx ->
                            PlayerView(ctx).apply {
                                useController = false
                                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                                layoutParams = FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                player = exoPlayer
                            }
                        },
                        update = { view -> view.player = exoPlayer },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // High-res Image / Match / Movie / Series Story Card
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF1E0B3B),
                                        FoulHomeBgPrimary,
                                        Color(0xFF120624)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (activeStory.mediaUrl.isNotBlank()) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(activeStory.mediaUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = activeStory.caption,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            CategoryStoryArtworkPlaceholder(categoryKey = activeGroup.categoryKey)
                        }
                    }
                }

                // Left / Right Invisible Tap Zones for Next / Previous Story
                Row(modifier = Modifier.fillMaxSize()) {
                    // Right side in RTL = Previous
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                                indication = null,
                                onClick = goBackPrevious
                            )
                    )
                    // Left side in RTL = Next
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                                indication = null,
                                onClick = advanceNext
                            )
                    )
                }

                // Top Overlay: Segmented Progress Bars + Account Header + Close Button
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xCC05030B), Color.Transparent)
                            )
                        )
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Segmented Progress Bars
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        stories.forEachIndexed { idx, _ ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(
                                        when {
                                            idx < currentStoryIndex -> FoulHomePurpleBright
                                            idx == currentStoryIndex -> Color.White
                                            else -> Color.White.copy(alpha = 0.28f)
                                        }
                                    )
                            )
                        }
                    }

                    // Story Author / Category Info Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(9.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, FoulHomePurpleBright, CircleShape)
                                    .background(FoulHomeCardBg),
                                contentAlignment = Alignment.Center
                            ) {
                                if (activeGroup.isOfficialFoulLogo) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_foul_tv_launcher),
                                        contentDescription = activeGroup.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else if (activeStory.userPhotoUrl.isNotBlank()) {
                                    AsyncImage(
                                        model = activeStory.userPhotoUrl,
                                        contentDescription = activeGroup.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }

                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = activeGroup.title,
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    if (activeGroup.isVerified) {
                                        VerifiedStoryCheckBadge()
                                    }
                                }
                                if (activeStory.subtitle.isNotBlank()) {
                                    Text(
                                        text = activeStory.subtitle,
                                        color = FoulHomeTextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            IconButton(onClick = goBackPrevious) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "السابق",
                                    tint = Color.White
                                )
                            }
                            IconButton(onClick = advanceNext) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "التالي",
                                    tint = Color.White
                                )
                            }
                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier.testTag("close_story_viewer_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "إغلاق",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                // Bottom Overlay: Caption + Action Button
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0xE605030B))
                            )
                        )
                        .padding(horizontal = 18.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (activeStory.caption.isNotBlank()) {
                        Text(
                            text = activeStory.caption,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Contextual Action Button if story links to a Reel, Match, or Movie/Series
                    when {
                        activeStory.linkedReelId.isNotBlank() -> {
                            Button(
                                onClick = {
                                    onDismiss()
                                    onOpenLinkedReel(activeStory.linkedReelId)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = FoulHomePurplePrimary),
                                shape = RoundedCornerShape(50)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                                Spacer(Modifier.width(6.dp))
                                Text("مشاهدة في قسم الريلز", color = Color.White, fontWeight = FontWeight.ExtraBold)
                            }
                        }

                        activeStory.linkedMatchId > 0 -> {
                            Button(
                                onClick = {
                                    onDismiss()
                                    onOpenLinkedMatch(activeStory.linkedMatchId)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = FoulHomePurplePrimary),
                                shape = RoundedCornerShape(50)
                            ) {
                                Icon(Icons.Default.SportsSoccer, contentDescription = null, tint = Color.White)
                                Spacer(Modifier.width(6.dp))
                                Text("فتح تفاصيل المباراة", color = Color.White, fontWeight = FontWeight.ExtraBold)
                            }
                        }

                        activeStory.linkedMediaId.isNotBlank() -> {
                            Button(
                                onClick = {
                                    onDismiss()
                                    onOpenLinkedMedia(activeStory.linkedMediaId)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = FoulHomePurplePrimary),
                                shape = RoundedCornerShape(50)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                                Spacer(Modifier.width(6.dp))
                                Text("مشاهدة الآن في المكتبة", color = Color.White, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * ============================================================================
 * 8. PUBLISH NEW STORY DIALOG (واجهة نشر قصة جديدة)
 * ============================================================================
 * Allows choosing an image or video from the device and publishing it to
 * Firebase (`stories`) and Telegram Storage.
 */
@Composable
fun PublishStoryModalDialog(
    selectedUri: Uri?,
    onPickMedia: () -> Unit,
    onConfirmPublish: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var caption by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = FoulHomeCardBg,
            border = BorderStroke(1.2.dp, FoulHomePurpleBright.copy(alpha = 0.7f)),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("publish_story_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "إضافة قصة جديدة • FOUL TV",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = FoulHomeTextSecondary)
                    }
                }

                Text(
                    text = "اختر صورة أو مقطع فيديو قصير من جهازك لنشره في قسم القصص بالصفحة الرئيسية.",
                    color = FoulHomeTextSecondary,
                    fontSize = 12.5.sp
                )

                Surface(
                    onClick = onPickMedia,
                    color = FoulHomeBgSecondary,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, FoulHomePurpleBright.copy(alpha = 0.55f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (selectedUri != null) "تم اختيار ملف القصة بنجاح ✓" else "اختر صورة أو فيديو من الجهاز",
                            color = if (selectedUri != null) Color(0xFF34D399) else Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = FoulHomePurpleLight
                        )
                    }
                }

                OutlinedTextField(
                    value = caption,
                    onValueChange = { caption = it },
                    placeholder = {
                        Text("اكتب عنواناً أو وصفاً للقصة (اختياري)...", color = FoulHomeTextSecondary, fontSize = 12.5.sp)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = FoulHomeBgSecondary,
                        unfocusedContainerColor = FoulHomeBgSecondary,
                        focusedBorderColor = FoulHomePurpleBright,
                        unfocusedBorderColor = FoulHomeBorderPurple,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        onClick = onDismiss,
                        color = Color.White.copy(alpha = 0.06f),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("إلغاء", color = FoulHomeTextSecondary, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = { onConfirmPublish(caption.trim()) },
                        enabled = selectedUri != null,
                        colors = ButtonDefaults.buttonColors(containerColor = FoulHomePurplePrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("confirm_publish_story_button")
                    ) {
                        Text("نشر القصة الآن", color = Color.White, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}

/**
 * ============================================================================
 * 9. NOTIFICATIONS BOTTOM SHEET (إشعارات FOUL TV)
 * ============================================================================
 */
@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeNotificationsBottomSheet(
    publishedEvents: List<PublishedEventItem>,
    liveMatches: List<MatchItem>,
    contactReplies: List<ContactMessageItem>,
    onSelectEvent: (PublishedEventItem) -> Unit,
    onSelectMatch: (MatchItem) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = FoulHomeBgSecondary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "الإشعارات والتنبيهات المباشرة",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )

            if (publishedEvents.isEmpty() && liveMatches.isEmpty() && contactReplies.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "لا توجد إشعارات جديدة حالياً",
                        color = FoulHomeTextSecondary,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(liveMatches, key = { "notif_match_${it.id}" }) { match ->
                        Surface(
                            onClick = {
                                onDismiss()
                                onSelectMatch(match)
                            },
                            color = FoulHomeCardBg,
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, FoulHomeBorderPurple),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(Icons.Default.SportsSoccer, contentDescription = null, tint = FoulHomePurpleLight)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "مباراة جارية الآن • ${match.competitionName}",
                                        color = FoulHomePurpleLight,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${match.homeTeam.name} ${match.homeTeam.score} - ${match.awayTeam.score} ${match.awayTeam.name}",
                                        color = Color.White,
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }
                    }

                    items(publishedEvents, key = { "notif_ev_${it.id}" }) { ev ->
                        Surface(
                            onClick = {
                                onDismiss()
                                onSelectEvent(ev)
                            },
                            color = FoulHomeCardBg,
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, FoulHomeBorderPurple),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(Icons.Default.SmartDisplay, contentDescription = null, tint = FoulHomePurpleBright)
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = ev.title.ifBlank { "حدث مميز على FOUL TV" },
                                        color = Color.White,
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    if (ev.description.isNotBlank()) {
                                        Text(
                                            text = ev.description,
                                            color = FoulHomeTextSecondary,
                                            fontSize = 11.5.sp,
                                            maxLines = 2
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * ============================================================================
 * 10. SUGGESTED CHANNELS SECTION (قنوات مقترحة تتبدل كل يوم — مثل beIN Sports 1 / 2)
 * ============================================================================
 */
@Composable
fun HomeSuggestedChannelsSection(
    allChannels: List<LiveChannel>,
    customFeaturedIds: List<String> = emptyList(),
    onChannelClick: (LiveChannel) -> Unit,
    onSeeAllChannelsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dailySuggestedChannels = remember(allChannels, customFeaturedIds) {
        selectDailySuggestedChannels(allChannels, customFeaturedIds)
    }

    if (dailySuggestedChannels.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag("home_suggested_channels_section")
    ) {
        FoulHomeSectionHeader(
            title = "قنوات مقترحة",
            iconBadge = {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(FoulHomePurpleBright, FoulHomePurplePrimary)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Tv,
                        contentDescription = "قنوات مقترحة",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                }
            },
            seeAllTestTag = "home_suggested_channels_see_all_button",
            onSeeAllClick = onSeeAllChannelsClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        val favChannelIds by FavoritesManager.favoriteChannelIds.collectAsState()

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("home_suggested_channels_row"),
            contentPadding = PaddingValues(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = dailySuggestedChannels,
                key = { "sugg_ch_${it.id}" }
            ) { channel ->
                val isFav = favChannelIds.contains(channel.id)
                Box(
                    modifier = Modifier
                        .width(152.dp)
                        .height(142.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(FoulHomeCardBg)
                        .border(
                            width = 1.dp,
                            color = FoulHomeBorderDark,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .tvFocusableClickable(
                            shape = RoundedCornerShape(16.dp),
                            scaleOnFocus = 1.04f,
                            onClick = { onChannelClick(channel) }
                        )
                        .testTag("suggested_channel_card_${channel.id}")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Top Live Badge + Favorite Heart Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = FoulHomeBgSecondary,
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(0.8.dp, FoulHomeBorderDark)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF10B981))
                                    )
                                    Text(
                                        text = "مباشر",
                                        color = Color.White,
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(FoulHomeBgSecondary)
                                    .border(0.8.dp, FoulHomeBorderDark, CircleShape)
                                    .clickable {
                                        FavoritesManager.toggleChannelFavorite(context, channel)
                                    }
                                    .testTag("sugg_channel_fav_${channel.id}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "مفضلة",
                                    tint = if (isFav) Color(0xFFF43F5E) else Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }

                        // Channel Logo
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF0A0A0E))
                                .border(1.dp, FoulHomeBorderDark, RoundedCornerShape(14.dp))
                                .padding(6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (channel.imageUrl.isNotBlank()) {
                                val req = remember(channel.imageUrl) {
                                    ImageRequest.Builder(context)
                                        .data(channel.imageUrl)
                                        .size(180)
                                        .crossfade(true)
                                        .memoryCachePolicy(CachePolicy.ENABLED)
                                        .diskCachePolicy(CachePolicy.ENABLED)
                                        .build()
                                }
                                AsyncImage(
                                    model = req,
                                    contentDescription = channel.name,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Tv,
                                    contentDescription = channel.name,
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }

                        // Channel Name & Category
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = channel.name,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                            if (channel.category.isNotBlank()) {
                                Text(
                                    text = channel.category,
                                    color = FoulHomeTextSecondary,
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Selects daily-rotated Suggested Channels (prioritizing beIN Sports 1 / beIN Sports 2 and top sports/featured channels).
 */
fun selectDailySuggestedChannels(
    allChannels: List<LiveChannel>,
    customFeaturedIds: List<String> = emptyList()
): List<LiveChannel> {
    if (allChannels.isEmpty()) return emptyList()
    val dailySeed = currentDailyRotationSeed()
    val result = mutableListOf<LiveChannel>()
    val addedIds = mutableSetOf<String>()

    // 1. Developer explicitly pinned featured channels
    customFeaturedIds.forEach { cid ->
        val found = allChannels.find { it.id == cid }
        if (found != null && addedIds.add(found.id)) {
            result.add(found)
        }
    }

    // 2. Priority flagship sports channels (e.g., beIN Sports 1, beIN Sports 2, SSC, Alkass) rotated daily
    val flagshipKeywords = listOf(
        "bein sports 1", "bein sport 1", "بي ان سبورت 1", "بي إن سبورت 1",
        "bein sports 2", "bein sport 2", "بي ان سبورت 2", "بي إن سبورت 2",
        "bein sports 3", "bein sport 3", "bein", "بي ان", "بي إن",
        "ssc", "الكأس", "alkass", "الرياضية", "sport"
    )
    val flagshipPool = allChannels.filter { ch ->
        val lower = "${ch.name} ${ch.category}".lowercase()
        flagshipKeywords.any { kw -> lower.contains(kw) }
    }

    // Ensure beIN Sports 1 & 2 are included at the top when present, with the rest rotated daily
    val bein1Or2 = flagshipPool.filter { ch ->
        val lower = ch.name.lowercase()
        (lower.contains("bein") || lower.contains("بي ان") || lower.contains("بي إن")) &&
            (lower.contains("1") || lower.contains("2"))
    }.shuffled(Random(dailySeed.toLong() + 11L))

    bein1Or2.take(3).forEach { ch ->
        if (addedIds.add(ch.id)) result.add(ch)
    }

    val rotatedFlagship = flagshipPool
        .filterNot { addedIds.contains(it.id) }
        .shuffled(Random(dailySeed.toLong() + 29L))

    rotatedFlagship.take(6).forEach { ch ->
        if (result.size < 8 && addedIds.add(ch.id)) {
            result.add(ch)
        }
    }

    // 3. Fill up to 8 channels from the remaining channels rotated daily
    if (result.size < 8) {
        val remainingRotated = allChannels
            .filterNot { addedIds.contains(it.id) }
            .shuffled(Random(dailySeed.toLong() + 53L))
        remainingRotated.take(8 - result.size).forEach { ch ->
            if (addedIds.add(ch.id)) result.add(ch)
        }
    }

    return result
}

/**
 * Renders any Custom Developer Sections published via "إدارة الرئيسية" in Developer Control Panel.
 */
@Composable
fun HomeDeveloperCustomSectionsList(
    sections: List<HomeCustomSectionItem>,
    onSectionClick: (HomeCustomSectionItem) -> Unit,
    modifier: Modifier = Modifier
) {
    if (sections.isEmpty()) return
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        sections.forEach { sec ->
            Surface(
                color = FoulHomeCardBg,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, FoulHomeBorderDark),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .tvFocusableClickable(
                        shape = RoundedCornerShape(16.dp),
                        scaleOnFocus = 1.02f,
                        onClick = { onSectionClick(sec) }
                    )
                    .testTag("home_custom_section_${sec.id}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0A0A0E))
                            .border(1.dp, FoulHomeBorderDark, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (sec.imageUrl.isNotBlank()) {
                            val req = remember(sec.imageUrl) {
                                ImageRequest.Builder(context)
                                    .data(sec.imageUrl)
                                    .size(160)
                                    .crossfade(true)
                                    .build()
                            }
                            AsyncImage(
                                model = req,
                                contentDescription = sec.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.SmartDisplay,
                                contentDescription = sec.title,
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = sec.title,
                            color = Color.White,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (sec.subtitle.isNotBlank()) {
                            Text(
                                text = sec.subtitle,
                                color = FoulHomeTextSecondary,
                                fontSize = 11.5.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Surface(
                        color = FoulHomePurplePrimary,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "فتح",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun currentDailyRotationSeed(): Int {
    val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Baghdad"))
    return cal.get(Calendar.YEAR) * 400 + cal.get(Calendar.DAY_OF_YEAR)
}

/**
 * Builds the Story Circle Groups required on the Home Screen:
 * - Order matches the reference design (RTL right-to-left):
 *   1. كرة القدم
 *   2. FOUL TV
 *   3. المباريات
 *   4. الأفلام
 *   5. المسلسلات
 * - FOUL TV: ONLY shows what the developer publishes (NO Reels!).
 * - المباريات, الأفلام, المسلسلات, كرة القدم: Rotate automatically every day + include developer stories.
 * - Respects Developer HomeManagementConfig visibility & pinning rules.
 */
fun buildHomeStoryCircleGroups(
    userStories: List<StoryItem>,
    allReels: List<ReelItem>,
    leagueGroups: List<LeagueMatchesGroup>,
    libraryItems: List<LibraryMediaItem>,
    publishedEvents: List<PublishedEventItem>,
    watchedStoryIds: Set<String>,
    homeConfig: HomeManagementConfig = HomeManagementConfig()
): List<StoryCircleGroup> {
    val dailySeed = currentDailyRotationSeed()
    val allMatches = leagueGroups.flatMap { it.matches }
    val movies = libraryItems.filter { !it.isSeries }
    val series = libraryItems.filter { it.isSeries }

    val sortedDevStories = userStories.sortedWith(
        compareByDescending<StoryItem> { it.isPinnedFirst }
            .thenByDescending { it.createdAt }
    )

    // 1. FOUL TV Official Stories — Strictly Developer-Published Stories & Developer Published Events ONLY (NO Reels!)
    val foulTvDevStories = sortedDevStories.filter {
        it.categoryKey.equals("foul_tv", ignoreCase = true) ||
            it.categoryKey.equals("user", ignoreCase = true)
    }
    val foulTvStoryList = buildList {
        addAll(foulTvDevStories)
        publishedEvents.take(5).forEach { ev ->
            add(
                StoryItem(
                    id = "story_foultv_ev_${ev.id}",
                    categoryKey = "foul_tv",
                    userName = "FOUL TV",
                    userHandle = "@foultv",
                    isVerified = true,
                    mediaType = if (ev.videoUrl.isNotBlank()) "VIDEO" else "IMAGE",
                    mediaUrl = ev.videoUrl.ifBlank { ev.imageUrl },
                    thumbnailUrl = ev.imageUrl,
                    caption = ev.title.ifBlank { ev.description }.ifBlank { "حدث حصري من المطور عبر FOUL TV" },
                    subtitle = "FOUL TV الرسمي"
                )
            )
        }
        if (isEmpty()) {
            add(
                StoryItem(
                    id = "story_foultv_welcome",
                    categoryKey = "foul_tv",
                    userName = "FOUL TV",
                    userHandle = "@foultv",
                    isVerified = true,
                    mediaType = "IMAGE",
                    mediaUrl = SocialUserProfile.DEFAULT_PROFILE_PHOTO_URL,
                    caption = "مرحباً بك في منصة FOUL TV الرسمية — إعلانات وتحديثات المطور الحصرية",
                    subtitle = "FOUL TV الرسمي"
                )
            )
        }
    }

    // 2. Matches Stories (قصص المباريات — تتبدل كل يوم)
    val dailyMatches = if (allMatches.size > 1) {
        val liveNow = allMatches.filter { it.isLive }
        val othersRotated = allMatches.filterNot { it.isLive }
            .shuffled(Random(dailySeed.toLong() + 101L))
        liveNow + othersRotated
    } else {
        allMatches
    }
    val matchesDevStories = sortedDevStories.filter { it.categoryKey.equals("matches", ignoreCase = true) }
    val matchesStoryList = buildList {
        addAll(matchesDevStories)
        dailyMatches.take(6).forEach { m ->
            val scoreOrTime = if (m.isLive || m.isFinished) {
                "${m.homeTeam.score} - ${m.awayTeam.score}"
            } else {
                m.displayTime
            }
            add(
                StoryItem(
                    id = "story_match_${m.id}_$dailySeed",
                    categoryKey = "matches",
                    userName = "المباريات",
                    userHandle = m.competitionName,
                    userPhotoUrl = m.competitionLogoUrl,
                    isVerified = true,
                    mediaType = "MATCH",
                    mediaUrl = m.homeTeam.logoUrl,
                    thumbnailUrl = m.competitionLogoUrl,
                    caption = "${m.homeTeam.name} ($scoreOrTime) ${m.awayTeam.name} • ${m.competitionName}",
                    subtitle = m.competitionName,
                    linkedMatchId = m.id
                )
            )
        }
    }
    // Use developer custom thumbnail if uploaded, otherwise empty so the clean white icon renders inside the circle (matching reference image)
    val matchesAvatar = matchesDevStories.firstOrNull()?.thumbnailUrl?.takeIf { it.isNotBlank() }.orEmpty()

    // 3. Movies Stories (قصص الأفلام — تتبدل كل يوم)
    val dailyMovies = if (movies.size > 1) {
        movies.shuffled(Random(dailySeed.toLong() + 202L))
    } else {
        movies
    }
    val moviesDevStories = sortedDevStories.filter { it.categoryKey.equals("movies", ignoreCase = true) }
    val moviesStoryList = buildList {
        addAll(moviesDevStories)
        dailyMovies.take(6).forEach { movie ->
            add(
                StoryItem(
                    id = "story_movie_${movie.id}_$dailySeed",
                    categoryKey = "movies",
                    userName = "الأفلام",
                    userHandle = movie.releaseYear.ifBlank { "سينما FOUL TV" },
                    userPhotoUrl = movie.posterUrl,
                    isVerified = true,
                    mediaType = "MEDIA",
                    mediaUrl = movie.posterUrl,
                    thumbnailUrl = movie.posterUrl,
                    caption = "${movie.name} ${if (movie.rating.isNotBlank()) "⭐ ${movie.rating}" else ""}",
                    subtitle = "أفلام اليوم المقترحة",
                    linkedMediaId = movie.id
                )
            )
        }
    }
    // Use developer custom thumbnail if uploaded, otherwise empty so the clean white clapperboard icon renders inside the circle (matching reference image)
    val moviesAvatar = moviesDevStories.firstOrNull()?.thumbnailUrl?.takeIf { it.isNotBlank() }.orEmpty()

    // 4. Series Stories (قصص المسلسلات — تتبدل كل يوم)
    val dailySeries = if (series.size > 1) {
        series.shuffled(Random(dailySeed.toLong() + 303L))
    } else {
        series
    }
    val seriesDevStories = sortedDevStories.filter { it.categoryKey.equals("series", ignoreCase = true) }
    val seriesStoryList = buildList {
        addAll(seriesDevStories)
        dailySeries.take(6).forEach { s ->
            add(
                StoryItem(
                    id = "story_series_${s.id}_$dailySeed",
                    categoryKey = "series",
                    userName = "المسلسلات",
                    userHandle = s.releaseYear.ifBlank { "مسلسلات FOUL TV" },
                    userPhotoUrl = s.posterUrl,
                    isVerified = true,
                    mediaType = "MEDIA",
                    mediaUrl = s.posterUrl,
                    thumbnailUrl = s.posterUrl,
                    caption = "${s.name} ${if (s.rating.isNotBlank()) "⭐ ${s.rating}" else ""}",
                    subtitle = "مسلسلات اليوم المقترحة",
                    linkedMediaId = s.id
                )
            )
        }
    }
    val seriesAvatar = seriesDevStories.firstOrNull()?.thumbnailUrl?.takeIf { it.isNotBlank() }
        ?: dailySeries.firstOrNull { it.posterUrl.isNotBlank() }?.posterUrl
        ?: SocialUserProfile.DEFAULT_PROFILE_PHOTO_URL

    // 5. Football Stories (قصص كرة القدم — تتبدل كل يوم)
    val dailyFootballReels = if (allReels.size > 1) {
        allReels.shuffled(Random(dailySeed.toLong() + 404L))
    } else {
        allReels
    }
    val dailyFootballMatches = if (allMatches.size > 1) {
        allMatches.shuffled(Random(dailySeed.toLong() + 505L))
    } else {
        allMatches
    }
    val footballDevStories = sortedDevStories.filter { it.categoryKey.equals("football", ignoreCase = true) }
    val footballStoryList = buildList {
        addAll(footballDevStories)
        dailyFootballReels.take(4).forEach { reel ->
            add(
                StoryItem(
                    id = "story_football_reel_${reel.id}_$dailySeed",
                    categoryKey = "football",
                    userName = "كرة القدم",
                    userHandle = reel.userHandle,
                    userPhotoUrl = reel.userPhotoUrl,
                    isVerified = true,
                    mediaType = "VIDEO",
                    mediaUrl = reel.streamUrl.ifBlank { reel.videoUrl },
                    thumbnailUrl = reel.thumbnailUrl.ifBlank { reel.userPhotoUrl },
                    caption = reel.caption.ifBlank { "أهداف ومهارات كرة القدم اليوم" },
                    subtitle = reel.userPersonalName,
                    linkedReelId = reel.id
                )
            )
        }
        dailyFootballMatches.take(3).forEach { m ->
            add(
                StoryItem(
                    id = "story_football_match_${m.id}_$dailySeed",
                    categoryKey = "football",
                    userName = "كرة القدم",
                    userHandle = m.competitionName,
                    userPhotoUrl = m.homeTeam.logoUrl,
                    isVerified = true,
                    mediaType = "MATCH",
                    mediaUrl = m.homeTeam.logoUrl,
                    thumbnailUrl = m.homeTeam.logoUrl,
                    caption = "${m.homeTeam.name} ضد ${m.awayTeam.name} • ${m.displayTime}",
                    subtitle = m.competitionName,
                    linkedMatchId = m.id
                )
            )
        }
    }
    val footballAvatar = footballDevStories.firstOrNull()?.thumbnailUrl?.takeIf { it.isNotBlank() }
        ?: dailyFootballReels.firstOrNull { it.thumbnailUrl.isNotBlank() }?.thumbnailUrl
        ?: dailyFootballMatches.firstOrNull()?.homeTeam?.logoUrl
            ?.ifBlank { SocialUserProfile.DEFAULT_PROFILE_PHOTO_URL }
        ?: SocialUserProfile.DEFAULT_PROFILE_PHOTO_URL

    val groups = mutableListOf<StoryCircleGroup>()

    // Order in RTL (right to left) matching the reference screenshot:
    // 1. كرة القدم, 2. FOUL TV, 3. المباريات, 4. الأفلام, 5. المسلسلات
    if (homeConfig.showFootballStory) {
        groups.add(
            StoryCircleGroup(
                id = "football",
                title = "كرة القدم",
                avatarUrl = footballAvatar,
                isVerified = true,
                categoryKey = "football",
                stories = footballStoryList,
                isAllWatched = footballStoryList.isNotEmpty() && footballStoryList.all { watchedStoryIds.contains(it.id) },
                isPinnedFirst = footballDevStories.any { it.isPinnedFirst }
            )
        )
    }
    if (homeConfig.showFoulTvStory) {
        groups.add(
            StoryCircleGroup(
                id = "foul_tv",
                title = "FOUL TV",
                avatarUrl = "",
                isOfficialFoulLogo = true,
                isVerified = true,
                categoryKey = "foul_tv",
                stories = foulTvStoryList,
                isAllWatched = foulTvStoryList.isNotEmpty() && foulTvStoryList.all { watchedStoryIds.contains(it.id) },
                isPinnedFirst = foulTvDevStories.any { it.isPinnedFirst }
            )
        )
    }
    if (homeConfig.showMatchesStory) {
        groups.add(
            StoryCircleGroup(
                id = "matches",
                title = "المباريات",
                avatarUrl = matchesAvatar,
                isVerified = true,
                categoryKey = "matches",
                stories = matchesStoryList,
                isAllWatched = matchesStoryList.isNotEmpty() && matchesStoryList.all { watchedStoryIds.contains(it.id) },
                isPinnedFirst = matchesDevStories.any { it.isPinnedFirst }
            )
        )
    }
    if (homeConfig.showMoviesStory) {
        groups.add(
            StoryCircleGroup(
                id = "movies",
                title = "الأفلام",
                avatarUrl = moviesAvatar,
                isVerified = true,
                categoryKey = "movies",
                stories = moviesStoryList,
                isAllWatched = moviesStoryList.isNotEmpty() && moviesStoryList.all { watchedStoryIds.contains(it.id) },
                isPinnedFirst = moviesDevStories.any { it.isPinnedFirst }
            )
        )
    }
    if (homeConfig.showSeriesStory) {
        groups.add(
            StoryCircleGroup(
                id = "series",
                title = "المسلسلات",
                avatarUrl = seriesAvatar,
                isVerified = true,
                categoryKey = "series",
                stories = seriesStoryList,
                isAllWatched = seriesStoryList.isNotEmpty() && seriesStoryList.all { watchedStoryIds.contains(it.id) },
                isPinnedFirst = seriesDevStories.any { it.isPinnedFirst }
            )
        )
    }

    // Custom named developer story groups (if the developer created a custom categoryKey)
    val standardKeys = setOf("foul_tv", "user", "matches", "movies", "series", "football")
    val customDevGroups = sortedDevStories
        .filterNot { standardKeys.contains(it.categoryKey.lowercase()) }
        .groupBy { it.categoryKey }
        .map { (customKey, items) ->
            val first = items.first()
            StoryCircleGroup(
                id = "dev_custom_$customKey",
                title = first.userName.ifBlank { customKey },
                avatarUrl = first.thumbnailUrl.ifBlank { first.mediaUrl },
                isVerified = true,
                categoryKey = customKey,
                stories = items,
                isAllWatched = items.all { watchedStoryIds.contains(it.id) },
                isPinnedFirst = items.any { it.isPinnedFirst }
            )
        }

    return (groups + customDevGroups).sortedByDescending { it.isPinnedFirst }
}

private fun fallbackDurationForReel(reelId: String, index: Int): String {
    val hash = (reelId.hashCode().let { if (it < 0) -it else it } + index * 17)
    val totalSecs = 22 + (hash % 64) // 00:22 to 01:25 until real metadata loads
    val mins = totalSecs / 60
    val secs = totalSecs % 60
    return String.format(Locale.US, "%02d:%02d", mins, secs)
}

private fun formatHomeMetricCount(value: Int): String {
    return when {
        value >= 1_000_000 -> String.format(Locale.US, "%.1fM", value / 1_000_000f)
        value >= 1_000 -> String.format(Locale.US, "%.1fK", value / 1_000f)
        else -> value.toString()
    }
}

private fun formatMatchDateArabic(startTimeIso: String, fallbackDateStr: String): String {
    if (startTimeIso.isNotBlank()) {
        val patterns = listOf(
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss"
        )
        for (p in patterns) {
            try {
                val sdf = SimpleDateFormat(p, Locale.US)
                val parsed = sdf.parse(startTimeIso)
                if (parsed != null) {
                    val outFmt = SimpleDateFormat("EEEE d MMMM yyyy", Locale("ar")).apply {
                        timeZone = TimeZone.getDefault()
                    }
                    return outFmt.format(parsed)
                }
            } catch (_: Exception) {
            }
        }
    }
    return fallbackDateStr.ifBlank { "اليوم" }
}
