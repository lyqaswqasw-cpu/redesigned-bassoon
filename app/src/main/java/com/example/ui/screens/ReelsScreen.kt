package com.example.ui.screens

import android.net.Uri
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import com.example.data.model.ReelModerationStage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.example.data.model.ReelCommentItem
import com.example.data.model.ReelItem
import com.example.data.model.SocialUserProfile
import com.example.ui.components.copyTextToClipboard
import com.example.ui.theme.FoulBlackBg
import com.example.ui.theme.FoulCardDark
import com.example.ui.theme.FoulGoldStar
import com.example.ui.theme.FoulLiveGreen
import com.example.ui.theme.FoulLiveRed
import com.example.ui.theme.FoulPurpleBright
import com.example.ui.theme.FoulPurplePrimary
import com.example.ui.theme.FoulTextSecondary

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.KeyEvent
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.VideoSize
import com.example.ui.components.tvFocusableClickable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val FoulGoldAccent = FoulGoldStar
private val FoulPurpleSoft = Color(0xFFB388FF)

private data class InspectedUserSeed(
    val userId: String,
    val personalName: String,
    val userHandle: String,
    val userPhotoUrl: String
)

@Composable
fun ReelsScreen(
    reels: List<ReelItem>,
    allReels: List<ReelItem> = reels,
    watchedReelIds: Set<String> = emptySet(),
    currentUserId: String = "",
    followingUserIds: Set<String> = emptySet(),
    hidePublishReelButton: Boolean = false,
    isLoading: Boolean,
    isUploading: Boolean,
    moderationStage: ReelModerationStage = ReelModerationStage.IDLE,
    uploadStatusMessage: String?,
    selectedReelIdFromDeepLink: String?,
    officialChannelUrl: String,
    commentsForActiveReel: List<ReelCommentItem>,
    isLoadingComments: Boolean,
    onRefreshReels: () -> Unit,
    onUploadReelVideo: (Uri, String, String) -> Unit,
    onToggleLike: (ReelItem) -> Unit,
    onRecordView: (ReelItem) -> Unit,
    onRecordShare: (ReelItem) -> Unit,
    onOpenComments: (ReelItem) -> Unit,
    onAddComment: (ReelItem, String) -> Unit,
    onDeleteReel: (ReelItem) -> Unit = {},
    onToggleFollowUser: (String) -> Unit = {},
    onFetchPublicUserProfile: (suspend (String, String, String, String) -> SocialUserProfile)? = null,
    onFullscreenLandscapeChanged: (Boolean) -> Unit = {},
    onConsumeDeepLinkReelId: () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current
    val configuration = LocalConfiguration.current
    val coroutineScope = rememberCoroutineScope()

    // Keep screen awake inside Reels at all times
    DisposableEffect(view) {
        view.keepScreenOn = true
        (context as? Activity)?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose { }
    }

    var isFullScreenPublishOpen by remember { mutableStateOf(false) }
    var activeCommentsReel by remember { mutableStateOf<ReelItem?>(null) }
    var activeOptionsReel by remember { mutableStateOf<ReelItem?>(null) }
    var showPublishAccountRequiredDialog by remember { mutableStateOf(false) }
    var isAnyEdgeFastForwarding by remember { mutableStateOf(false) }
    var inspectedUserSeed by remember { mutableStateOf<InspectedUserSeed?>(null) }
    var isReelsSearchOpen by remember { mutableStateOf(false) }
    var reelsSearchQuery by remember { mutableStateOf("") }
    var savedReelIds by rememberSaveable { mutableStateOf(setOf<String>()) }

    // Fullscreen Landscape Mode state ("اضف زر في الأعلى بشكل ملء شاشة لمشاهدة الفيديو ويكون في الوضع الأفقي ملء شاشة فقط المشغل")
    var isFullscreenLandscapeRequested by rememberSaveable { mutableStateOf(false) }
    val isDeviceLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isLandscapeFullscreen = isFullscreenLandscapeRequested || isDeviceLandscape

    val toggleFullscreenLandscape: (Boolean) -> Unit = { targetLandscape ->
        isFullscreenLandscapeRequested = targetLandscape
        val activity = context as? Activity
        if (activity != null) {
            activity.requestedOrientation = if (targetLandscape) {
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            } else {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            val window = activity.window
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            if (targetLandscape) {
                insetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                insetsController.hide(WindowInsetsCompat.Type.systemBars())
            } else {
                insetsController.show(WindowInsetsCompat.Type.systemBars())
            }
        }
        onFullscreenLandscapeChanged(targetLandscape)
    }

    LaunchedEffect(isLandscapeFullscreen) {
        onFullscreenLandscapeChanged(isLandscapeFullscreen)
    }

    DisposableEffect(Unit) {
        onDispose {
            val activity = context as? Activity
            if (activity != null && isFullscreenLandscapeRequested) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                val insetsController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
                insetsController.show(WindowInsetsCompat.Type.systemBars())
            }
            onFullscreenLandscapeChanged(false)
        }
    }

    if (isFullscreenLandscapeRequested) {
        BackHandler {
            if (activeCommentsReel != null) {
                activeCommentsReel = null
            } else {
                toggleFullscreenLandscape(false)
            }
        }
    }

    if (isReelsSearchOpen || reelsSearchQuery.isNotBlank()) {
        BackHandler {
            reelsSearchQuery = ""
            isReelsSearchOpen = false
        }
    }

    // Automatically close full-screen publish page when upload succeeds
    LaunchedEffect(isUploading, uploadStatusMessage) {
        if (!isUploading && isFullScreenPublishOpen && uploadStatusMessage != null) {
            if (uploadStatusMessage.contains("تم رفع ونشر") || uploadStatusMessage.contains("بنجاح")) {
                isFullScreenPublishOpen = false
            }
        }
    }

    val triggerPublishReel: () -> Unit = {
        if (!com.example.data.repository.SocialFirebaseRepository.isUserAuthenticatedWithCompleteProfile()) {
            showPublishAccountRequiredDialog = true
        } else {
            isFullScreenPublishOpen = true
        }
    }

    val trimmedSearch = reelsSearchQuery.trim()
    val isSearchModeActive = trimmedSearch.isNotEmpty()

    // When searching, search across ALL reels (including previously watched reels).
    // When NOT searching, use `reels` (which strictly excludes watched reels on load/tab switch).
    val displayedReels = remember(reels, allReels, trimmedSearch) {
        if (trimmedSearch.isNotEmpty()) {
            val pool = if (allReels.isNotEmpty()) allReels else reels
            pool.filter { r ->
                r.caption.contains(trimmedSearch, ignoreCase = true) ||
                    r.userPersonalName.contains(trimmedSearch, ignoreCase = true) ||
                    r.userName.contains(trimmedSearch, ignoreCase = true) ||
                    r.userHandle.contains(trimmedSearch, ignoreCase = true)
            }
        } else {
            reels
        }
    }

    val areAllAvailableVideosWatched = remember(allReels, watchedReelIds, displayedReels, isSearchModeActive) {
        !isSearchModeActive && allReels.isNotEmpty() &&
            (displayedReels.isEmpty() || allReels.all { watchedReelIds.contains(it.id) })
    }

    // Add 1 extra page at the end when all videos have been watched during the current session so swiping down shows "قمت أنت بمشاهدة كل الفيديوهات"
    val showEndWatchedPageInPager = !isSearchModeActive && displayedReels.isNotEmpty() && areAllAvailableVideosWatched
    val totalPagerPages = (displayedReels.size + if (showEndWatchedPageInPager) 1 else 0).coerceAtLeast(1)

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { totalPagerPages }
    )

    // Scroll to deep-linked Reel if present
    LaunchedEffect(selectedReelIdFromDeepLink, displayedReels.size) {
        val targetId = selectedReelIdFromDeepLink?.trim().orEmpty()
        if (targetId.isNotEmpty() && displayedReels.isNotEmpty()) {
            val idx = displayedReels.indexOfFirst {
                it.id.equals(targetId, ignoreCase = true) ||
                    "${it.chatId}_${it.messageId}".equals(targetId, ignoreCase = true)
            }
            if (idx >= 0) {
                pagerState.scrollToPage(idx)
                onConsumeDeepLinkReelId()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FoulBlackBg)
            .onPreviewKeyEvent { keyEvent ->
                if (keyEvent.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                    when (keyEvent.nativeKeyEvent.keyCode) {
                        KeyEvent.KEYCODE_DPAD_DOWN,
                        KeyEvent.KEYCODE_CHANNEL_DOWN -> {
                            if (pagerState.currentPage < totalPagerPages - 1) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                                true
                            } else false
                        }
                        KeyEvent.KEYCODE_DPAD_UP,
                        KeyEvent.KEYCODE_CHANNEL_UP -> {
                            if (pagerState.currentPage > 0) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                                true
                            } else false
                        }
                        else -> false
                    }
                } else {
                    false
                }
            }
            .testTag("reels_screen_root")
    ) {
        if (isLoading && displayedReels.isEmpty() && allReels.isEmpty()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CircularProgressIndicator(color = Color.White)
                Text(
                    text = "جاري تحميل مقاطع FOUL REELS...",
                    color = FoulTextSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        } else if (displayedReels.isEmpty()) {
            if (isSearchModeActive) {
                // No search matches state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "لا توجد مقاطع مطابقة لبحثك: \"$trimmedSearch\"",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            reelsSearchQuery = ""
                            isReelsSearchOpen = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = FoulPurpleBright),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("مسح البحث", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            } else if (allReels.isNotEmpty()) {
                // User has watched all available videos! ("وعند مشاهدة كل الفيديوهات اضهر قمت أنت بمشاهدة كل الفيديوهات")
                AllReelsWatchedEmptyState(
                    watchedCount = watchedReelIds.size.coerceAtLeast(allReels.size),
                    hidePublishReelButton = hidePublishReelButton,
                    onOpenSearchClick = { isReelsSearchOpen = true },
                    onPublishClick = triggerPublishReel,
                    onRefreshClick = onRefreshReels
                )
            } else {
                EmptyReelsState(
                    officialChannelUrl = officialChannelUrl,
                    hidePublishReelButton = hidePublishReelButton,
                    onPublishClick = triggerPublishReel,
                    onRefreshClick = onRefreshReels
                )
            }
        } else {
            VerticalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("reels_vertical_pager"),
                beyondViewportPageCount = 1,
                key = { page ->
                    if (page < displayedReels.size) {
                        displayedReels[page].id
                    } else {
                        "all_watched_end_page"
                    }
                }
            ) { page ->
                if (page >= displayedReels.size) {
                    AllReelsWatchedEmptyState(
                        watchedCount = watchedReelIds.size.coerceAtLeast(allReels.size),
                        hidePublishReelButton = hidePublishReelButton,
                        onOpenSearchClick = { isReelsSearchOpen = true },
                        onPublishClick = triggerPublishReel,
                        onRefreshClick = onRefreshReels
                    )
                } else {
                    val reel = displayedReels.getOrNull(page)
                    if (reel != null) {
                        val isCurrentPage = pagerState.settledPage == page
                        val isSaved = savedReelIds.contains(reel.id)
                        ReelPagePlayerItem(
                            reel = reel,
                            currentUserId = currentUserId,
                            isFollowingCreator = followingUserIds.contains(reel.userId),
                            isCurrentPage = isCurrentPage,
                            isLandscapeFullscreen = isLandscapeFullscreen,
                            isSaved = isSaved,
                            onLikeClick = { onToggleLike(reel) },
                            onDoubleTapLike = {
                                if (!reel.isLikedByMe) {
                                    onToggleLike(reel)
                                }
                            },
                            onCommentClick = {
                                activeCommentsReel = reel
                                onOpenComments(reel)
                            },
                            onToggleSaveClick = {
                                val nextSaved = if (savedReelIds.contains(reel.id)) {
                                    savedReelIds - reel.id
                                } else {
                                    savedReelIds + reel.id
                                }
                                savedReelIds = nextSaved
                                android.widget.Toast.makeText(
                                    context,
                                    if (nextSaved.contains(reel.id)) "تم حفظ الفيديو في المحفوظات" else "تمت إزالة الفيديو من المحفوظات",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            },
                            onToggleFullscreenLandscape = {
                                toggleFullscreenLandscape(!isLandscapeFullscreen)
                            },
                            onViewRecorded = { onRecordView(reel) },
                            onOpenLongPressOptions = {
                                activeOptionsReel = reel
                            },
                            onToggleFollowCreator = {
                                if (reel.userId.isNotBlank()) {
                                    onToggleFollowUser(reel.userId)
                                }
                            },
                            onOpenCreatorProfile = {
                                inspectedUserSeed = InspectedUserSeed(
                                    userId = reel.userId.ifBlank { "foul_user" },
                                    personalName = reel.userPersonalName.ifBlank { reel.userName.ifBlank { "مستخدم FOUL TV" } },
                                    userHandle = reel.userHandle.ifBlank { "@foultv" },
                                    userPhotoUrl = reel.userPhotoUrl.ifBlank { SocialUserProfile.DEFAULT_PROFILE_PHOTO_URL }
                                )
                            },
                            onEdgeFastForwardChanged = { active ->
                                isAnyEdgeFastForwarding = active
                            }
                        )
                    }
                }
            }
        }

        // Top Overlay Bar (Publish '+' + Fullscreen Button + Search Watched/Any Reels + Refresh)
        // Hidden when in Landscape Fullscreen Player-Only mode ("ويكون في الوضع الأفقي ملء شاشة فقط المشغل")
        AnimatedVisibility(
            visible = !isAnyEdgeFastForwarding && !isLandscapeFullscreen,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Pure '+' Publish Icon Button in high-contrast White & Black (Hidden when disabled in Developer Panel)
                        if (!hidePublishReelButton) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(1.5.dp, Color.Black.copy(alpha = 0.35f), CircleShape)
                                    .tvFocusableClickable(
                                        shape = CircleShape,
                                        onClick = triggerPublishReel
                                    )
                                    .testTag("publish_reel_top_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "نشر ريلز",
                                    tint = Color.Black,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        // Fullscreen Landscape Toggle Button ("اضف زر في الأعلى بشكل ملء شاشة لمشاهدة الفيديو")
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = Color.Black.copy(alpha = 0.68f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                            modifier = Modifier
                                .height(40.dp)
                                .tvFocusableClickable(
                                    shape = RoundedCornerShape(999.dp),
                                    onClick = { toggleFullscreenLandscape(true) }
                                )
                                .testTag("reels_fullscreen_top_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Fullscreen,
                                    contentDescription = "ملء شاشة",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "ملء شاشة",
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 11.5.sp
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Search Button in Reels (allows searching any watched or unwatched Reel)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (isReelsSearchOpen || isSearchModeActive) FoulPurpleBright else Color.Black.copy(alpha = 0.62f))
                                .border(1.dp, Color.White.copy(alpha = 0.22f), CircleShape)
                                .tvFocusableClickable(
                                    shape = CircleShape,
                                    onClick = {
                                        isReelsSearchOpen = !isReelsSearchOpen
                                        if (!isReelsSearchOpen) {
                                            reelsSearchQuery = ""
                                        }
                                    }
                                )
                                .testTag("reels_search_toggle_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isReelsSearchOpen) Icons.Default.Close else Icons.Default.Search,
                                contentDescription = "بحث في الريلز",
                                tint = Color.White,
                                modifier = Modifier.size(19.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.62f))
                                .border(1.dp, Color.White.copy(alpha = 0.22f), CircleShape)
                                .tvFocusableClickable(
                                    shape = CircleShape,
                                    onClick = onRefreshReels
                                )
                                .testTag("reels_refresh_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "تحديث الريلز",
                                tint = Color.White,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }
                }

                // Expandable Search Bar inside Reels (finds any video even if previously watched)
                AnimatedVisibility(visible = isReelsSearchOpen || isSearchModeActive) {
                    OutlinedTextField(
                        value = reelsSearchQuery,
                        onValueChange = { reelsSearchQuery = it },
                        placeholder = {
                            Text(
                                text = "ابحث عن أي فيديو ريلز (حتى لو شاهدته سابقاً)...",
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 12.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        trailingIcon = {
                            if (reelsSearchQuery.isNotEmpty()) {
                                IconButton(onClick = { reelsSearchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "مسح",
                                        tint = Color.White
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF0E0E14).copy(alpha = 0.94f),
                            unfocusedContainerColor = Color(0xFF0E0E14).copy(alpha = 0.94f),
                            focusedBorderColor = FoulPurpleBright,
                            unfocusedBorderColor = Color(0xFF292930),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 540.dp)
                            .testTag("reels_search_input")
                    )
                }
            }
        }

        // Upload progress banner if uploading
        AnimatedVisibility(
            visible = isUploading || !uploadStatusMessage.isNullOrBlank(),
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 62.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF0A0A0E).copy(alpha = 0.96f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.22f)),
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (isUploading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = uploadStatusMessage ?: "جاري رفع ونشر الريلز على FOUL TV...",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    // Full-Screen Professional Video Publish Screen ("فتح صفحة بملء كامل واحترافية جداً ومنظمة")
    if (isFullScreenPublishOpen) {
        ReelsPublishFullScreen(
            isUploading = isUploading,
            moderationStage = moderationStage,
            externalStatusMessage = uploadStatusMessage,
            onPublishVideo = { videoUri, caption ->
                onUploadReelVideo(videoUri, caption, caption)
            },
            onClose = {
                if (!isUploading) {
                    isFullScreenPublishOpen = false
                }
            }
        )
    }

    // Comments Sheet:
    // - In Portrait: Bottom-to-Half-Screen Black & White Comments Sheet ("فتح من تحت إلى المنتصف")
    // - In Landscape Fullscreen: Quarter-Screen Side Panel ("وتفتح بشكل ربع من شاشة")
    if (activeCommentsReel != null) {
        ReelCommentsDialog(
            reel = activeCommentsReel!!,
            comments = commentsForActiveReel,
            isLoading = isLoadingComments,
            isQuarterScreenLandscape = isLandscapeFullscreen,
            onDismiss = { activeCommentsReel = null },
            onOpenUserProfile = { comment ->
                inspectedUserSeed = InspectedUserSeed(
                    userId = comment.userId.ifBlank { "foul_user" },
                    personalName = comment.userPersonalName.ifBlank { comment.userName.ifBlank { "مشاهد FOUL TV" } },
                    userHandle = comment.userHandle.ifBlank { "@${comment.userName.ifBlank { "foultv" }}" },
                    userPhotoUrl = comment.userPhotoUrl.ifBlank { SocialUserProfile.DEFAULT_PROFILE_PHOTO_URL }
                )
            },
            onSendComment = { commentText ->
                onAddComment(activeCommentsReel!!, commentText)
            }
        )
    }

    // Public User Profile Dialog (opened from Reel Creator OR Comments)
    if (inspectedUserSeed != null) {
        val seed = inspectedUserSeed!!
        val combinedReels = remember(reels, allReels) {
            (allReels + reels).distinctBy { it.id }
        }
        PublicUserProfileDialog(
            seed = seed,
            currentUserId = currentUserId,
            isFollowing = followingUserIds.contains(seed.userId),
            allReels = combinedReels,
            onFetchProfile = onFetchPublicUserProfile,
            onToggleFollow = {
                if (seed.userId.isNotBlank()) {
                    onToggleFollowUser(seed.userId)
                }
            },
            onSelectReel = { selectedReel ->
                inspectedUserSeed = null
                activeCommentsReel = null
                val targetIndex = reels.indexOfFirst { it.id == selectedReel.id }
                if (targetIndex >= 0) {
                    coroutineScope.launch {
                        pagerState.scrollToPage(targetIndex)
                    }
                }
            },
            onDismiss = { inspectedUserSeed = null }
        )
    }

    // Long-Press Options Dialog:
    // 1. "نسخ رابط الفيديو" (Smart Share Link + Caption + Official Telegram Channel URL ONLY)
    // 2. "حذف الريلز" (Visible ONLY to the owner who published the Reel)
    if (activeOptionsReel != null) {
        val targetReel = activeOptionsReel!!
        val isOwner = currentUserId.isNotBlank() && targetReel.userId == currentUserId
        ReelLongPressOptionsDialog(
            reel = targetReel,
            isOwner = isOwner,
            officialChannelUrl = officialChannelUrl,
            onDismiss = { activeOptionsReel = null },
            onCopySmartVideoLink = {
                onRecordShare(targetReel)
                val telegramUrl = targetReel.channelUrl.ifBlank { officialChannelUrl }
                val smartCopyText = buildString {
                    append("🎬 شاهد هذا الفيديو على تطبيق FOUL TV:\n")
                    if (targetReel.caption.isNotBlank()) {
                        append("${targetReel.caption}\n\n")
                    }
                    append("🔗 رابط الفيديو الذكي:\n")
                    append("${targetReel.shareUrl}\n\n")
                    append("📢 قناتنا الرسمية على تيلجرام:\n")
                    append(telegramUrl)
                }
                copyTextToClipboard(
                    context = context,
                    label = "FOUL Smart Video Link",
                    text = smartCopyText,
                    toastMessage = "تم نسخ رابط الفيديو الذكي مع رابط قناة التيلجرام!"
                )
                activeOptionsReel = null
            },
            onDeleteReel = {
                activeOptionsReel = null
                onDeleteReel(targetReel)
            }
        )
    }

    // Account Required Dialog when a Guest/Skipped user tries to Publish a Reel
    if (showPublishAccountRequiredDialog) {
        Dialog(onDismissRequest = { showPublishAccountRequiredDialog = false }) {
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = Color(0xFF0A0A0E),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.18f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "نشر الريلز يتطلب إنشاء حساب",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "لقد اخترت تخطي إنشاء الحساب عند الدخول. لا يمكنك نشر مقاطع ريلز أو نسخ أي رابط فيديو أو قنوات إلا بعد إكمال المطلوب وإنشاء حساب أو تسجيل الدخول.",
                        color = FoulTextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = {
                            showPublishAccountRequiredDialog = false
                            com.example.data.repository.SocialFirebaseRepository.requestOpenFullAuthScreen()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = FoulPurplePrimary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("reels_publish_locked_create_account_button")
                    ) {
                        Text(
                            text = "إنشاء حساب / تسجيل الدخول الآن",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                    }
                    Surface(
                        onClick = { showPublishAccountRequiredDialog = false },
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White.copy(alpha = 0.06f),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.14f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "إغلاق",
                                color = FoulTextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun ReelPagePlayerItem(
    reel: ReelItem,
    currentUserId: String,
    isFollowingCreator: Boolean,
    isCurrentPage: Boolean,
    isLandscapeFullscreen: Boolean,
    isSaved: Boolean,
    onLikeClick: () -> Unit,
    onDoubleTapLike: () -> Unit,
    onCommentClick: () -> Unit,
    onToggleSaveClick: () -> Unit,
    onToggleFullscreenLandscape: () -> Unit,
    onViewRecorded: () -> Unit,
    onOpenLongPressOptions: () -> Unit,
    onToggleFollowCreator: () -> Unit,
    onOpenCreatorProfile: () -> Unit,
    onEdgeFastForwardChanged: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isPausedManually by remember(reel.id) { mutableStateOf(false) }
    var isBuffering by remember(reel.id) { mutableStateOf(true) }
    var hasRecordedView by remember(reel.id) { mutableStateOf(false) }
    var usedFallbackUrl by remember(reel.id) { mutableStateOf(false) }
    var isFastForward2x by remember(reel.id) { mutableStateOf(false) }

    // Toggleable Google ExoPlayer Fast Controls (-10s, Play/Pause, +10s) ("إمكانية إخفاء وظهور ازرار التحكم في تقديم وتأخير 10 ثواني وايقاف وتشغيل")
    var showExoFastControls by remember(reel.id) { mutableStateOf(false) }
    var controlsInteractTimestamp by remember(reel.id) { mutableLongStateOf(0L) }

    // Video Native Aspect Ratio detection ("يظهر الفيديو بشكل تصميم مرتب حسب الفيديو إذا كان 9:16 يعرض حسب ذالك وإذا كان 16:9 يعرض أيضا حسب ذلك وليس قصص وملء وأيضا بنفس العرض عامودي طبيعي وإذا 1:1 نفسه من دون قص وبقية الأحجام أيضا")
    var detectedVideoAspectRatio by remember(reel.id) { mutableStateOf<Float?>(null) }

    // Center Double-Tap Heart Burst Animation State ("وعند النقر مرتين في المنتصف يحفظ لايك")
    var showDoubleTapHeartBurst by remember(reel.id) { mutableStateOf(false) }
    var doubleTapBurstKey by remember(reel.id) { mutableLongStateOf(0L) }

    // Video Progress / Timeline State for English LTR Seek Bar ("ومسار الفيديو بالجهة الانكليزية وحسب خط الفيديو")
    var currentPositionMs by remember(reel.id) { mutableLongStateOf(0L) }
    var durationMs by remember(reel.id) { mutableLongStateOf(0L) }
    var isScrubbingProgress by remember(reel.id) { mutableStateOf(false) }
    var scrubFraction by remember(reel.id) { mutableStateOf(0f) }

    // Ultra-Fast Google Media3 ExoPlayer ("مشغل قوقل exo فائق السرعة")
    val exoPlayer = remember(reel.id) {
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setUserAgent("FOUL-TV-ExoPlayer/2.0-UltraFast")
            .setAllowCrossProtocolRedirects(true)
            .setConnectTimeoutMs(10_000)
            .setReadTimeoutMs(15_000)

        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                800,
                18_000,
                350,
                700
            )
            .setPrioritizeTimeOverSizeThresholds(true)
            .build()

        ExoPlayer.Builder(context)
            .setMediaSourceFactory(DefaultMediaSourceFactory(httpDataSourceFactory))
            .setLoadControl(loadControl)
            .setSeekBackIncrementMs(10_000L)
            .setSeekForwardIncrementMs(10_000L)
            .build()
            .apply {
                repeatMode = Player.REPEAT_MODE_ONE
                // Respect the video's true aspect ratio (9:16, 16:9, 1:1, 4:3, etc.) without cropping
                videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                val initialUrl = reel.streamUrl.ifBlank { reel.videoUrl }
                if (initialUrl.isNotBlank()) {
                    setMediaItem(MediaItem.fromUri(initialUrl))
                    prepare()
                }
            }
    }

    // Auto-hide ExoPlayer +/-10s & Play/Pause controls 3.5 seconds after last interaction when playing
    LaunchedEffect(showExoFastControls, controlsInteractTimestamp, isPausedManually) {
        if (showExoFastControls && !isPausedManually) {
            delay(3_500L)
            showExoFastControls = false
        }
    }

    LaunchedEffect(doubleTapBurstKey) {
        if (doubleTapBurstKey > 0L) {
            showDoubleTapHeartBurst = true
            delay(850L)
            showDoubleTapHeartBurst = false
        }
    }

    // Poll ExoPlayer playback position & duration for the LTR Video Timeline Bar
    LaunchedEffect(exoPlayer, isCurrentPage, isScrubbingProgress) {
        while (isCurrentPage) {
            val dur = exoPlayer.duration.coerceAtLeast(0L)
            val pos = exoPlayer.currentPosition.coerceAtLeast(0L)
            durationMs = dur
            if (!isScrubbingProgress) {
                currentPositionMs = pos
            }
            delay(100L)
        }
    }

    LaunchedEffect(isFastForward2x) {
        exoPlayer.setPlaybackSpeed(if (isFastForward2x) 2.0f else 1.0f)
        onEdgeFastForwardChanged(isFastForward2x)
    }

    DisposableEffect(exoPlayer, reel.id) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                isBuffering = playbackState == Player.STATE_BUFFERING
                if (playbackState == Player.STATE_READY) {
                    durationMs = exoPlayer.duration.coerceAtLeast(0L)
                    if (isCurrentPage && !hasRecordedView) {
                        hasRecordedView = true
                        onViewRecorded()
                    }
                }
            }

            override fun onVideoSizeChanged(videoSize: VideoSize) {
                if (videoSize.width > 0 && videoSize.height > 0) {
                    val ratio = (videoSize.width.toFloat() * videoSize.pixelWidthHeightRatio) / videoSize.height.toFloat()
                    if (ratio.isFinite() && ratio > 0.1f) {
                        detectedVideoAspectRatio = ratio
                    }
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                isBuffering = false
                if (!usedFallbackUrl && reel.videoUrl.isNotBlank() && reel.videoUrl != reel.streamUrl) {
                    usedFallbackUrl = true
                    exoPlayer.setMediaItem(MediaItem.fromUri(reel.videoUrl))
                    exoPlayer.prepare()
                    if (isCurrentPage && !isPausedManually) {
                        exoPlayer.playWhenReady = true
                    }
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            onEdgeFastForwardChanged(false)
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    LaunchedEffect(isCurrentPage, isPausedManually) {
        if (isCurrentPage && !isPausedManually) {
            exoPlayer.playWhenReady = true
            exoPlayer.play()
        } else {
            exoPlayer.playWhenReady = false
            exoPlayer.pause()
        }
    }

    DisposableEffect(lifecycleOwner, exoPlayer, isCurrentPage) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> exoPlayer.pause()
                Lifecycle.Event.ON_RESUME -> {
                    if (isCurrentPage && !isPausedManually) {
                        exoPlayer.play()
                    }
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val seekByDeltaMs: (Long) -> Unit = { deltaMs ->
        val dur = exoPlayer.duration.coerceAtLeast(0L)
        val target = if (dur > 0L) {
            (exoPlayer.currentPosition + deltaMs).coerceIn(0L, dur)
        } else {
            (exoPlayer.currentPosition + deltaMs).coerceAtLeast(0L)
        }
        exoPlayer.seekTo(target)
        currentPositionMs = target
        controlsInteractTimestamp = System.currentTimeMillis()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .focusable()
            .onPreviewKeyEvent { keyEvent ->
                if (keyEvent.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                    when (keyEvent.nativeKeyEvent.keyCode) {
                        KeyEvent.KEYCODE_DPAD_CENTER,
                        KeyEvent.KEYCODE_ENTER,
                        KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> {
                            showExoFastControls = true
                            controlsInteractTimestamp = System.currentTimeMillis()
                            isPausedManually = !isPausedManually
                            true
                        }
                        KeyEvent.KEYCODE_DPAD_RIGHT,
                        KeyEvent.KEYCODE_MEDIA_FAST_FORWARD -> {
                            showExoFastControls = true
                            seekByDeltaMs(10_000L)
                            true
                        }
                        KeyEvent.KEYCODE_DPAD_LEFT,
                        KeyEvent.KEYCODE_MEDIA_REWIND -> {
                            showExoFastControls = true
                            seekByDeltaMs(-10_000L)
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            }
            .pointerInput(reel.id) {
                detectTapGestures(
                    onPress = { offset ->
                        val w = size.width.toFloat().coerceAtLeast(1f)
                        val isEdgeTouch = offset.x < w * 0.18f || offset.x > w * 0.82f
                        if (isEdgeTouch) {
                            isFastForward2x = true
                            try {
                                tryAwaitRelease()
                            } finally {
                                isFastForward2x = false
                            }
                        }
                    },
                    onDoubleTap = { offset ->
                        val w = size.width.toFloat().coerceAtLeast(1f)
                        val isCenterTouch = offset.x in (w * 0.18f)..(w * 0.82f)
                        if (isCenterTouch) {
                            doubleTapBurstKey = System.currentTimeMillis()
                            onDoubleTapLike()
                        }
                    },
                    onLongPress = { offset ->
                        val w = size.width.toFloat().coerceAtLeast(1f)
                        val isCenterTouch = offset.x in (w * 0.20f)..(w * 0.80f)
                        if (isCenterTouch) {
                            onOpenLongPressOptions()
                        }
                    },
                    onTap = { offset ->
                        val w = size.width.toFloat().coerceAtLeast(1f)
                        val isCenterTouch = offset.x in (w * 0.18f)..(w * 0.82f)
                        if (isCenterTouch) {
                            // Toggle show/hide of Google ExoPlayer +/-10s & Play/Pause controls
                            showExoFastControls = !showExoFastControls
                            controlsInteractTimestamp = System.currentTimeMillis()
                        }
                    }
                )
            }
    ) {
        // Google Media3 ExoPlayer Surface — displays video in its TRUE native aspect ratio (9:16, 16:9, 1:1, 4:3, etc.) without cropping
        val ratio = detectedVideoAspectRatio
        val isHorizontalOrSquareInPortrait = !isLandscapeFullscreen && ratio != null && ratio >= 0.80f

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = if (isHorizontalOrSquareInPortrait && ratio != null) {
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(ratio.coerceIn(0.5f, 2.4f))
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF07070B))
                        .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
                } else {
                    Modifier.fillMaxSize()
                },
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            useController = false
                            keepScreenOn = true
                            // RESIZE_MODE_FIT ensures 9:16, 16:9, 1:1, and all other aspect ratios render cleanly without cropping
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                            layoutParams = FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            player = exoPlayer
                        }
                    },
                    update = { view ->
                        view.keepScreenOn = true
                        view.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                        if (view.player !== exoPlayer) {
                            view.player = exoPlayer
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("reel_exoplayer_view_${reel.id}")
                )
            }
        }

        // Subtle 2x Fast-Forward Pill Indicator when holding screen edges
        AnimatedVisibility(
            visible = isFastForward2x,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 18.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = Color.Black.copy(alpha = 0.78f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FastForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "تسريع الفيديو 2x",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Center Double-Tap Heart Burst Animation ("وعند النقر مرتين في المنتصف يحفظ لايك")
        AnimatedVisibility(
            visible = showDoubleTapHeartBurst,
            enter = fadeIn(tween(140)) + scaleIn(initialScale = 0.45f, animationSpec = tween(220)),
            exit = fadeOut(tween(260)) + scaleOut(targetScale = 1.35f, animationSpec = tween(260)),
            modifier = Modifier
                .align(Alignment.Center)
                .testTag("reel_double_tap_heart_burst_${reel.id}")
        ) {
            val heartScale by animateFloatAsState(
                targetValue = if (showDoubleTapHeartBurst) 1.15f else 0.7f,
                animationSpec = tween(220),
                label = "double_tap_heart_scale"
            )
            Box(
                modifier = Modifier
                    .size(116.dp)
                    .scale(heartScale)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "إعجاب",
                    tint = Color(0xFFFF1744),
                    modifier = Modifier.size(84.dp)
                )
            }
        }

        // Toggleable Center Google ExoPlayer Fast Controls: -10s | Play/Pause | +10s
        // ("إمكانية إخفاء وظهور ازرار التحكم في تقديم وتأخير 10 ثواني وايقاف وتشغيل")
        AnimatedVisibility(
            visible = (showExoFastControls || isPausedManually) && !isFastForward2x && !showDoubleTapHeartBurst,
            enter = fadeIn(tween(160)),
            exit = fadeOut(tween(180)),
            modifier = Modifier
                .align(Alignment.Center)
                .testTag("reel_exo_fast_controls_${reel.id}")
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color.Black.copy(alpha = 0.52f))
                        .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(999.dp))
                        .padding(horizontal = 22.dp, vertical = 12.dp)
                ) {
                    // -10 Seconds Rewind Button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(CircleShape)
                            .tvFocusableClickable(
                                shape = CircleShape,
                                onClick = { seekByDeltaMs(-10_000L) }
                            )
                            .padding(8.dp)
                            .testTag("reel_rewind_10s_button_${reel.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay10,
                            contentDescription = "تأخير 10 ثواني",
                            tint = Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                        Text(
                            text = "-10s",
                            color = Color.White.copy(alpha = 0.88f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    // Center Play / Pause Button
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .tvFocusableClickable(
                                shape = CircleShape,
                                onClick = {
                                    isPausedManually = !isPausedManually
                                    controlsInteractTimestamp = System.currentTimeMillis()
                                }
                            )
                            .testTag("reel_play_pause_button_${reel.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPausedManually) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = if (isPausedManually) "تشغيل" else "إيقاف مؤقت",
                            tint = Color.Black,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // +10 Seconds Fast-Forward Button
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(CircleShape)
                            .tvFocusableClickable(
                                shape = CircleShape,
                                onClick = { seekByDeltaMs(10_000L) }
                            )
                            .padding(8.dp)
                            .testTag("reel_forward_10s_button_${reel.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Forward10,
                            contentDescription = "تقديم 10 ثواني",
                            tint = Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                        Text(
                            text = "+10s",
                            color = Color.White.copy(alpha = 0.88f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }

        if (isBuffering && isCurrentPage) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.5.dp,
                modifier = Modifier
                    .size(38.dp)
                    .align(Alignment.Center)
            )
        }

        // English LTR Video Progress & Interactive Seek Bar helper values
        val safeDuration = durationMs.coerceAtLeast(1L)
        val progressFraction = if (isScrubbingProgress) {
            scrubFraction.coerceIn(0f, 1f)
        } else if (durationMs > 0L) {
            (currentPositionMs.toFloat() / safeDuration.toFloat()).coerceIn(0f, 1f)
        } else {
            0f
        }

        if (isLandscapeFullscreen) {
            // =========================================================================
            // LANDSCAPE FULLSCREEN PLAYER-ONLY MODE
            // ("ويكون في الوضع الأفقي ملء شاشة فقط المشغل وإمكانية لايك وتعليق وتفتح بشكل ربع من شاشة وإمكانية حفظ")
            // =========================================================================
            AnimatedVisibility(
                visible = !isFastForward2x,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Top bar in Landscape Fullscreen: Exit Fullscreen + Video Title
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.78f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = Color.Black.copy(alpha = 0.72f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                            modifier = Modifier
                                .tvFocusableClickable(
                                    shape = RoundedCornerShape(999.dp),
                                    onClick = onToggleFullscreenLandscape
                                )
                                .testTag("reel_exit_fullscreen_landscape_button_${reel.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FullscreenExit,
                                    contentDescription = "تصغير الشاشة",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "إنهاء ملء الشاشة",
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        if (reel.caption.isNotBlank()) {
                            Text(
                                text = reel.caption,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 16.dp)
                            )
                        }
                    }

                    // Bottom bar in Landscape Fullscreen: Like + Comment (opens 1/4 screen) + Save + LTR Seekbar
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.85f)
                                    )
                                )
                            )
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Landscape Action Buttons: Like + Comment (1/4 screen) + Save + Controls Toggle
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Like Button
                                Surface(
                                    shape = RoundedCornerShape(999.dp),
                                    color = Color(0xFF121218).copy(alpha = 0.90f),
                                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.22f)),
                                    modifier = Modifier
                                        .tvFocusableClickable(
                                            shape = RoundedCornerShape(999.dp),
                                            onClick = onLikeClick
                                        )
                                        .testTag("reel_landscape_like_button_${reel.id}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (reel.isLikedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = "لايك",
                                            tint = if (reel.isLikedByMe) Color(0xFFFF1744) else Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = "لايك (${reel.likesCount})",
                                            color = Color.White,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                // Comment Button (Opens 1/4 screen side panel in landscape!)
                                Surface(
                                    shape = RoundedCornerShape(999.dp),
                                    color = Color(0xFF121218).copy(alpha = 0.90f),
                                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.22f)),
                                    modifier = Modifier
                                        .tvFocusableClickable(
                                            shape = RoundedCornerShape(999.dp),
                                            onClick = onCommentClick
                                        )
                                        .testTag("reel_landscape_comment_button_${reel.id}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.ChatBubbleOutline,
                                            contentDescription = "تعليق",
                                            tint = Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = "تعليق (${reel.commentsCount})",
                                            color = Color.White,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                // Save Button ("وإمكانية حفظ")
                                Surface(
                                    shape = RoundedCornerShape(999.dp),
                                    color = if (isSaved) Color.White else Color(0xFF121218).copy(alpha = 0.90f),
                                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                                    modifier = Modifier
                                        .tvFocusableClickable(
                                            shape = RoundedCornerShape(999.dp),
                                            onClick = onToggleSaveClick
                                        )
                                        .testTag("reel_landscape_save_button_${reel.id}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                            contentDescription = "حفظ",
                                            tint = if (isSaved) Color.Black else Color.White,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = if (isSaved) "تم الحفظ" else "حفظ",
                                            color = if (isSaved) Color.Black else Color.White,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }

                            // Toggle +/-10s Controls visibility pill
                            Surface(
                                shape = RoundedCornerShape(999.dp),
                                color = Color(0xFF121218).copy(alpha = 0.85f),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.20f)),
                                modifier = Modifier
                                    .tvFocusableClickable(
                                        shape = RoundedCornerShape(999.dp),
                                        onClick = {
                                            showExoFastControls = !showExoFastControls
                                            controlsInteractTimestamp = System.currentTimeMillis()
                                        }
                                    )
                            ) {
                                Text(
                                    text = if (showExoFastControls) "إخفاء التحكم" else "إظهار التحكم ±10s",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }

                        // LTR Seekbar in Landscape Mode
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                if (durationMs > 0L) {
                                    Text(
                                        text = "${formatReelTimeMmSs(if (isScrubbingProgress) (scrubFraction * safeDuration).toLong() else currentPositionMs)} / ${formatReelTimeMmSs(durationMs)}",
                                        color = Color.White.copy(alpha = 0.88f),
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(18.dp)
                                        .pointerInput(reel.id, durationMs) {
                                            detectTapGestures { offset ->
                                                val w = size.width.toFloat().coerceAtLeast(1f)
                                                val targetFrac = (offset.x / w).coerceIn(0f, 1f)
                                                val dur = exoPlayer.duration.coerceAtLeast(0L)
                                                if (dur > 0L) {
                                                    val targetMs = (targetFrac * dur).toLong()
                                                    exoPlayer.seekTo(targetMs)
                                                    currentPositionMs = targetMs
                                                }
                                            }
                                        },
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(999.dp))
                                            .background(Color.White.copy(alpha = 0.28f))
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(progressFraction)
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(999.dp))
                                            .background(Color.White)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // =========================================================================
            // PORTRAIT MODE OVERLAY UI
            // =========================================================================
            AnimatedVisibility(
                visible = !isFastForward2x,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Bottom gradient scrim for readability
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(235.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.52f),
                                        Color.Black.copy(alpha = 0.92f)
                                    )
                                )
                            )
                    )

                    // Right-Side Interactive Action Rail: Like + Comment + Save + Share
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 14.dp, bottom = 46.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ReelActionCircleButton(
                            icon = if (reel.isLikedByMe) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            iconTint = if (reel.isLikedByMe) Color(0xFFFF1744) else Color.White,
                            label = reel.likesCount.toString(),
                            testTag = "reel_like_button_${reel.id}",
                            onClick = onLikeClick
                        )

                        ReelActionCircleButton(
                            icon = Icons.Outlined.ChatBubbleOutline,
                            iconTint = Color.White,
                            label = reel.commentsCount.toString(),
                            testTag = "reel_comment_button_${reel.id}",
                            onClick = onCommentClick
                        )

                        ReelActionCircleButton(
                            icon = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            iconTint = if (isSaved) FoulGoldStar else Color.White,
                            label = if (isSaved) "محفوظ" else "حفظ",
                            testTag = "reel_save_button_${reel.id}",
                            onClick = onToggleSaveClick
                        )

                        ReelActionCircleButton(
                            icon = Icons.Default.Share,
                            iconTint = Color.White,
                            label = "مشاركة",
                            testTag = "reel_options_button_${reel.id}",
                            onClick = onOpenLongPressOptions
                        )
                    }

                    // Bottom-Start Creator Info (Clickable to open Creator's Public Profile!) & Caption
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth(0.78f)
                            .padding(start = 16.dp, end = 12.dp, bottom = 44.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Clickable Creator Avatar + Name + @handle ("إمكانية رؤية حساب الشخصي لصاحب الريلز")
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(24.dp))
                                    .tvFocusableClickable(
                                        shape = RoundedCornerShape(24.dp),
                                        onClick = onOpenCreatorProfile
                                    )
                                    .padding(end = 4.dp)
                                    .testTag("reel_creator_profile_trigger_${reel.id}")
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF121218),
                                    border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.85f)),
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    AsyncImage(
                                        model = reel.userPhotoUrl.ifBlank { SocialUserProfile.DEFAULT_PROFILE_PHOTO_URL },
                                        contentDescription = reel.userPersonalName.ifBlank { reel.userName },
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                    )
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                    Text(
                                        text = reel.userPersonalName.ifBlank { reel.userName.ifBlank { "مستخدم FOUL TV" } },
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = if (reel.userHandle.startsWith("@")) reel.userHandle else "@${reel.userHandle.ifBlank { "foultv" }}",
                                        color = Color.White.copy(alpha = 0.72f),
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            // Follow / Following Pill if not current user
                            if (reel.userId.isNotBlank() && reel.userId != currentUserId && reel.userId != "foul_user") {
                                Surface(
                                    shape = RoundedCornerShape(999.dp),
                                    color = if (isFollowingCreator) {
                                        Color.White.copy(alpha = 0.14f)
                                    } else {
                                        Color.White
                                    },
                                    border = BorderStroke(
                                        1.dp,
                                        if (isFollowingCreator) Color.White.copy(alpha = 0.35f) else Color.White
                                    ),
                                    modifier = Modifier
                                        .tvFocusableClickable(
                                            shape = RoundedCornerShape(999.dp),
                                            onClick = onToggleFollowCreator
                                        )
                                        .testTag("reel_follow_creator_button_${reel.id}")
                                ) {
                                    Text(
                                        text = if (isFollowingCreator) "تتابعه" else "متابعة",
                                        color = if (isFollowingCreator) Color.White else Color.Black,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        if (reel.caption.isNotBlank()) {
                            Text(
                                text = reel.caption,
                                color = Color.White,
                                fontSize = 13.5.sp,
                                lineHeight = 19.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // English LTR Video Progress & Interactive Seek Bar at the very bottom ("ومسار الفيديو بالجهة الانكليزية وحسب خط الفيديو")
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("reel_ltr_progress_bar_container_${reel.id}"),
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            if (durationMs > 0L) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${formatReelTimeMmSs(if (isScrubbingProgress) (scrubFraction * safeDuration).toLong() else currentPositionMs)} / ${formatReelTimeMmSs(durationMs)}",
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Interactive Left-to-Right Video Seek Track
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(18.dp)
                                    .pointerInput(reel.id, durationMs) {
                                        detectTapGestures { offset ->
                                            val w = size.width.toFloat().coerceAtLeast(1f)
                                            val targetFrac = (offset.x / w).coerceIn(0f, 1f)
                                            val dur = exoPlayer.duration.coerceAtLeast(0L)
                                            if (dur > 0L) {
                                                val targetMs = (targetFrac * dur).toLong()
                                                exoPlayer.seekTo(targetMs)
                                                currentPositionMs = targetMs
                                            }
                                        }
                                    }
                                    .pointerInput(reel.id, durationMs) {
                                        detectHorizontalDragGestures(
                                            onDragStart = { offset ->
                                                val w = size.width.toFloat().coerceAtLeast(1f)
                                                isScrubbingProgress = true
                                                scrubFraction = (offset.x / w).coerceIn(0f, 1f)
                                            },
                                            onHorizontalDrag = { change, _ ->
                                                change.consume()
                                                val w = size.width.toFloat().coerceAtLeast(1f)
                                                scrubFraction = (change.position.x / w).coerceIn(0f, 1f)
                                            },
                                            onDragEnd = {
                                                val dur = exoPlayer.duration.coerceAtLeast(0L)
                                                if (dur > 0L) {
                                                    val targetMs = (scrubFraction * dur).toLong()
                                                    exoPlayer.seekTo(targetMs)
                                                    currentPositionMs = targetMs
                                                }
                                                isScrubbingProgress = false
                                            },
                                            onDragCancel = {
                                                isScrubbingProgress = false
                                            }
                                        )
                                    }
                                    .testTag("reel_ltr_seek_track_${reel.id}"),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                // Unplayed background track
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(if (isScrubbingProgress) 6.dp else 3.5.dp)
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(Color.White.copy(alpha = 0.28f))
                                )
                                // Played progress fill from LEFT to RIGHT (English direction)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(progressFraction)
                                        .height(if (isScrubbingProgress) 6.dp else 3.5.dp)
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                colors = listOf(Color.White, FoulPurpleBright)
                                            )
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatReelTimeMmSs(ms: Long): String {
    val totalSec = (ms.coerceAtLeast(0L) / 1000L).toInt()
    val mins = totalSec / 60
    val secs = totalSec % 60
    return String.format(java.util.Locale.US, "%02d:%02d", mins, secs)
}

/**
 * Long-Press Options Dialog for a Reel:
 * - "نسخ رابط الفيديو": Copies ONLY the Smart Share Link formatted with caption and Official Telegram Channel URL.
 * - "حذف الريلز": Displayed strictly ONLY to the publisher who owns the Reel (`isOwner == true`).
 */
@Composable
private fun ReelLongPressOptionsDialog(
    reel: ReelItem,
    isOwner: Boolean,
    officialChannelUrl: String,
    onDismiss: () -> Unit,
    onCopySmartVideoLink: () -> Unit,
    onDeleteReel: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFF0A0A0E),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.16f)),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("reel_long_press_options_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "خيارات المقطع",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(30.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = FoulTextSecondary
                        )
                    }
                }

                // Option 1: نسخ رابط الفيديو
                Surface(
                    onClick = onCopySmartVideoLink,
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF14141C),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.16f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reel_option_copy_smart_video_link")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "نسخ رابط الفيديو",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.5.sp
                            )
                            Text(
                                text = "نسخ الرابط الذكي للمقطع مع رابط قناة التيلجرام الرسمية (${officialChannelUrl.removePrefix("https://")})",
                                color = FoulTextSecondary,
                                fontSize = 11.5.sp
                            )
                        }
                    }
                }

                // Option 2: حذف الريلز (Visible ONLY if the current user published this Reel)
                if (isOwner) {
                    Surface(
                        onClick = onDeleteReel,
                        shape = RoundedCornerShape(16.dp),
                        color = FoulLiveRed.copy(alpha = 0.14f),
                        border = BorderStroke(1.dp, FoulLiveRed.copy(alpha = 0.55f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reel_option_delete_own_reel")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = null,
                                tint = Color(0xFFFF6E6E),
                                modifier = Modifier.size(22.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "حذف الريلز",
                                    color = Color(0xFFFF8A80),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.5.sp
                                )
                                Text(
                                    text = "حذف هذا المقطع الذي قمت بنشره نهائياً",
                                    color = FoulTextSecondary,
                                    fontSize = 11.5.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReelActionCircleButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    label: String,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .tvFocusableClickable(
                shape = RoundedCornerShape(14.dp),
                onClick = onClick
            )
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.56f))
                .border(1.dp, Color.White.copy(alpha = 0.20f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(23.dp)
            )
        }
        Text(
            text = label,
            color = Color.White,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

/**
 * Displayed when the user has watched all available Reels ("وعند مشاهدة كل الفيديوهات اضهر قمت أنت بمشاهدة كل الفيديوهات").
 * Watched videos are saved to the database and never shown again in the regular feed,
 * but can still be found via Search (`البحث عن فيديو`).
 */
@Composable
private fun AllReelsWatchedEmptyState(
    watchedCount: Int,
    hidePublishReelButton: Boolean = false,
    onOpenSearchClick: () -> Unit,
    onPublishClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(28.dp)
            .testTag("all_reels_watched_state"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 460.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF101014),
                border = BorderStroke(1.5.dp, FoulPurpleBright.copy(alpha = 0.65f)),
                modifier = Modifier.size(86.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = FoulLiveGreen,
                        modifier = Modifier.size(46.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "قمت أنت بمشاهدة كل الفيديوهات",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 21.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("all_reels_watched_title")
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "تم حفظ جميع الفيديوهات التي شاهدتها ($watchedCount فيديو) في قاعدة البيانات ولن تتكرر لك مرة ثانية. يمكنك البحث عن أي فيديو تريده في أي وقت أو تحديث القائمة.",
                color = FoulTextSecondary,
                fontSize = 13.5.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = onOpenSearchClick,
                colors = ButtonDefaults.buttonColors(containerColor = FoulPurpleBright),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("all_reels_watched_search_button")
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "البحث عن فيديو معين",
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.5.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (!hidePublishReelButton) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .tvFocusableClickable(
                                shape = RoundedCornerShape(14.dp),
                                onClick = onPublishClick
                            )
                            .testTag("all_reels_watched_publish_button")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "نشر فيديو",
                                color = Color.Black,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.5.sp
                            )
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF101014),
                    border = BorderStroke(1.dp, Color(0xFF292930)),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .tvFocusableClickable(
                            shape = RoundedCornerShape(14.dp),
                            onClick = onRefreshClick
                        )
                        .testTag("all_reels_watched_refresh_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "تحديث الفيديوهات الجديدة",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyReelsState(
    officialChannelUrl: String,
    hidePublishReelButton: Boolean = false,
    onPublishClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFF121218),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.22f)),
            modifier = Modifier.size(84.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(44.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "مرحباً بك في قسم FOUL REELS",
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "متصل بقاعدة بيانات FOUL ومشغل Google Media3 ExoPlayer فائق السرعة.",
            color = FoulTextSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(22.dp))
        if (!hidePublishReelButton) {
            Button(
                onClick = onPublishClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("empty_reels_publish_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "نشر مقطع ريلز من الهاتف",
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                onClick = onRefreshClick,
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF101016),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.14f)),
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "تحديث المقاطع",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
            Surface(
                onClick = {
                    copyTextToClipboard(
                        context = context,
                        label = "FOUL Official Channel",
                        text = officialChannelUrl,
                        toastMessage = "تم نسخ رابط القناة الرسمية!"
                    )
                },
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF101016),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.22f)),
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "نسخ رابط القناة",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

/**
 * Comments Sheet:
 * - Portrait mode: Bottom-to-Half-Screen (`fillMaxHeight(0.52f)`).
 * - Landscape Fullscreen mode: Quarter-Screen Side Panel (`fillMaxWidth(0.28f)` / `widthIn(min = 240.dp, max = 330.dp)` - "وتفتح بشكل ربع من شاشة").
 */
@Composable
private fun ReelCommentsDialog(
    reel: ReelItem,
    comments: List<ReelCommentItem>,
    isLoading: Boolean,
    isQuarterScreenLandscape: Boolean = false,
    onDismiss: () -> Unit,
    onOpenUserProfile: (ReelCommentItem) -> Unit,
    onSendComment: (String) -> Unit
) {
    var inputText by remember { mutableStateOf("") }

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
                .background(Color.Black.copy(alpha = if (isQuarterScreenLandscape) 0.25f else 0.52f))
                .clickable(onClick = onDismiss)
        ) {
            Surface(
                shape = if (isQuarterScreenLandscape) {
                    RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                } else {
                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                },
                color = Color(0xFF060608).copy(alpha = 0.96f),
                border = BorderStroke(0.8.dp, Color.White.copy(alpha = 0.16f)),
                modifier = if (isQuarterScreenLandscape) {
                    Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxWidth(0.28f)
                        .widthIn(min = 240.dp, max = 340.dp)
                        .fillMaxHeight()
                        .clickable(enabled = false) {}
                        .testTag("reel_comments_bottom_sheet")
                } else {
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .fillMaxHeight(0.52f)
                        .clickable(enabled = false) {}
                        .testTag("reel_comments_bottom_sheet")
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    // Top drag handle bar in subtle white
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .width(38.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color.White.copy(alpha = 0.25f))
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Header row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "التعليقات (${comments.size.coerceAtLeast(reel.commentsCount)})",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.5.sp
                        )
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.08f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "إغلاق",
                                tint = Color.White.copy(alpha = 0.85f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.09f), thickness = 0.7.dp)
                    Spacer(modifier = Modifier.height(6.dp))

                    // Comments list
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        if (isLoading && comments.isEmpty()) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier
                                    .size(28.dp)
                                    .align(Alignment.Center)
                            )
                        } else if (comments.isEmpty()) {
                            Text(
                                text = "لا توجد تعليقات بعد. كن أول من يعلق!",
                                color = Color.White.copy(alpha = 0.55f),
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(vertical = 6.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(comments, key = { it.id }) { comment ->
                                    val displayPersonal = comment.userPersonalName.ifBlank { comment.userName.ifBlank { "مشاهد FOUL TV" } }
                                    val displayHandle = if (comment.userHandle.startsWith("@")) {
                                        comment.userHandle
                                    } else {
                                        "@${comment.userHandle.ifBlank { comment.userName.ifBlank { "foultv" } }}"
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        // Clickable commenter avatar
                                        Surface(
                                            shape = CircleShape,
                                            color = Color(0xFF14141A),
                                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clickable { onOpenUserProfile(comment) }
                                                .testTag("comment_user_avatar_${comment.id}")
                                        ) {
                                            AsyncImage(
                                                model = comment.userPhotoUrl.ifBlank { SocialUserProfile.DEFAULT_PROFILE_PHOTO_URL },
                                                contentDescription = displayPersonal,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(CircleShape)
                                            )
                                        }

                                        Column(
                                            modifier = Modifier.weight(1f),
                                            verticalArrangement = Arrangement.spacedBy(3.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .clickable { onOpenUserProfile(comment) }
                                                    .testTag("comment_user_name_${comment.id}")
                                            ) {
                                                Text(
                                                    text = displayPersonal,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 12.5.sp,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                                Text(
                                                    text = displayHandle,
                                                    color = Color.White.copy(alpha = 0.50f),
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 11.sp,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                            Text(
                                                text = comment.text,
                                                color = Color.White.copy(alpha = 0.90f),
                                                fontSize = 13.5.sp,
                                                lineHeight = 18.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = Color.White.copy(alpha = 0.09f), thickness = 0.7.dp)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Minimalist Black & White Comment Input Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = {
                                Text(
                                    text = "أضف تعليقاً...",
                                    color = Color.White.copy(alpha = 0.45f),
                                    fontSize = 13.sp
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(999.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF111116),
                                unfocusedContainerColor = Color(0xFF111116),
                                focusedBorderColor = Color.White.copy(alpha = 0.45f),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.14f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("reel_comment_input")
                        )
                        IconButton(
                            onClick = {
                                val trimmed = inputText.trim()
                                if (trimmed.isNotEmpty()) {
                                    onSendComment(trimmed)
                                    inputText = ""
                                }
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .testTag("reel_comment_send_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "إرسال التعليق",
                                tint = Color.Black,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Full Public User Profile Page (`إمكانية الدخول للحسابات الأشخاص وإمكانية فتح الحسابات وعرض بشكل صفحة عرض جميلة ومرتبة`)
 * - Can be opened from both the Reel Creator header AND any Commenter in the Comments Sheet.
 * - Shows Profile Photo, Personal Name, `@username`, Follow/Following button, Posts/Followers/Following counters,
 *   and a 3-column grid of all Reels published by that user.
 */
@Composable
private fun PublicUserProfileDialog(
    seed: InspectedUserSeed,
    currentUserId: String,
    isFollowing: Boolean,
    allReels: List<ReelItem>,
    onFetchProfile: (suspend (String, String, String, String) -> SocialUserProfile)?,
    onToggleFollow: () -> Unit,
    onSelectReel: (ReelItem) -> Unit,
    onDismiss: () -> Unit
) {
    var loadedProfile by remember(seed) {
        mutableStateOf(
            SocialUserProfile(
                uid = seed.userId,
                email = "",
                username = seed.userHandle.removePrefix("@"),
                avatarId = "avatar_1",
                personalName = seed.personalName,
                profilePhotoUrl = seed.userPhotoUrl,
                isProfileComplete = true,
                hasCompletedPhotoStep = true
            )
        )
    }

    LaunchedEffect(seed) {
        if (onFetchProfile != null) {
            val fetched = runCatching {
                onFetchProfile(
                    seed.userId,
                    seed.personalName,
                    seed.userHandle,
                    seed.userPhotoUrl
                )
            }.getOrNull()
            if (fetched != null) {
                loadedProfile = fetched
            }
        }
    }

    val cleanSeedHandle = seed.userHandle.removePrefix("@").lowercase()
    val userReels = remember(allReels, seed, loadedProfile) {
        allReels.filter { r ->
            (seed.userId.isNotBlank() && seed.userId != "foul_user" && r.userId == seed.userId) ||
                (cleanSeedHandle.isNotBlank() && r.userHandle.removePrefix("@").lowercase() == cleanSeedHandle)
        }.sortedByDescending { it.createdAt }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Surface(
            color = Color(0xFF040406),
            modifier = Modifier
                .fillMaxSize()
                .testTag("public_user_profile_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                // Top Header Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF121218))
                            .border(1.dp, Color.White.copy(alpha = 0.14f), CircleShape)
                            .testTag("close_public_profile_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع",
                            tint = Color.White,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    Text(
                        text = "@${loadedProfile.username.removePrefix("@").ifBlank { "foultv" }}",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.size(38.dp))
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.08f), thickness = 0.7.dp)

                // Profile Info Header (Instagram-style layout)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Profile Photo
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF121218),
                            border = BorderStroke(1.8.dp, Color.White.copy(alpha = 0.85f)),
                            modifier = Modifier.size(82.dp)
                        ) {
                            AsyncImage(
                                model = loadedProfile.profilePhotoUrl.ifBlank { SocialUserProfile.DEFAULT_PROFILE_PHOTO_URL },
                                contentDescription = loadedProfile.personalName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }

                        // 3 Counters: المنشورات | المتابِعون | يتابع
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PublicProfileStatItem(count = userReels.size, label = "المنشورات")
                            PublicProfileStatItem(
                                count = (loadedProfile.followersCount + if (isFollowing) 1 else 0).coerceAtLeast(0),
                                label = "المتابِعون"
                            )
                            PublicProfileStatItem(count = loadedProfile.followingCount, label = "يتابع")
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Personal Name & @handle & Bio aligned to Start
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = loadedProfile.personalName.ifBlank { loadedProfile.username.ifBlank { "مستخدم FOUL TV" } },
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "@${loadedProfile.username.removePrefix("@").ifBlank { "foultv" }}",
                            color = Color.White.copy(alpha = 0.58f),
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                        if (loadedProfile.bio.isNotBlank()) {
                            Text(
                                text = loadedProfile.bio,
                                color = Color(0xFFE0E0E6),
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }

                    // Follow / Following Action Button
                    if (seed.userId.isNotBlank() && seed.userId != currentUserId && seed.userId != "foul_user") {
                        Spacer(modifier = Modifier.height(14.dp))
                        Surface(
                            onClick = onToggleFollow,
                            shape = RoundedCornerShape(12.dp),
                            color = if (isFollowing) Color(0xFF14141C) else Color.White,
                            border = BorderStroke(
                                1.dp,
                                if (isFollowing) Color.White.copy(alpha = 0.25f) else Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp)
                                .testTag("public_profile_follow_button")
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PersonAddAlt1,
                                    contentDescription = null,
                                    tint = if (isFollowing) Color.White else Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isFollowing) "تتابعه" else "متابعة الحساب",
                                    color = if (isFollowing) Color.White else Color.Black,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                // Divider between Profile Header and Reels Grid
                HorizontalDivider(color = Color.White.copy(alpha = 0.12f), thickness = 0.8.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF08080C))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.GridOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "المنشورات (${userReels.size})",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.5.sp
                    )
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.08f), thickness = 0.6.dp)

                // 3-Column Grid of User's Reels
                if (userReels.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "لا توجد مقاطع ريلز منشورة لهذا الحساب بعد.",
                            color = Color.White.copy(alpha = 0.50f),
                            fontSize = 13.5.sp
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        items(userReels, key = { it.id }) { item ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(9f / 14f)
                                    .background(Color(0xFF0E0E14))
                                    .clickable { onSelectReel(item) }
                                    .testTag("public_profile_reel_${item.id}")
                            ) {
                                AsyncImage(
                                    model = item.thumbnailUrl.ifBlank { item.userPhotoUrl.ifBlank { SocialUserProfile.DEFAULT_PROFILE_PHOTO_URL } },
                                    contentDescription = item.caption,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    Color.Black.copy(alpha = 0.72f)
                                                )
                                            )
                                        )
                                )
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Text(
                                        text = item.viewsCount.toString(),
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
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

@Composable
private fun PublicProfileStatItem(
    count: Int,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = count.toString(),
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 17.sp
        )
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.65f),
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        )
    }
}

