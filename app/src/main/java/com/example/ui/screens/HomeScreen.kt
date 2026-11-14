package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.data.repository.FavoritesManager
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.data.model.ChannelCategory
import com.example.data.model.ChannelEngagementStats
import com.example.data.model.ContactMessageItem
import com.example.data.model.EventBanner
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
import com.example.data.model.StoryCircleGroup
import com.example.data.model.StoryItem
import com.example.ui.components.AdMobBanner
import com.example.ui.components.CompactTopBarIconButton
import com.example.ui.components.FoulCustomPackageIcon
import com.example.ui.components.FoulSectionSubBar
import com.example.ui.components.FoulSmartTopBar
import com.example.ui.components.tvFocusableClickable
import com.example.ui.theme.FoulBlackBg
import com.example.ui.theme.FoulCardDark
import com.example.ui.theme.FoulDivider
import com.example.ui.theme.FoulGoldStar
import com.example.ui.theme.FoulLiveGreen
import com.example.ui.theme.FoulLiveRed
import com.example.ui.theme.FoulPurpleBright
import com.example.ui.theme.FoulPurpleDeep
import com.example.ui.theme.FoulPurpleGlow
import com.example.ui.theme.FoulPurplePrimary
import com.example.ui.theme.FoulTextMuted
import com.example.ui.theme.FoulTextPrimary
import com.example.ui.theme.FoulTextSecondary
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.Refresh

@Composable
fun HomeScreen(
    isLoading: Boolean,
    errorMessage: String?,
    currentBranch: ChannelCategory?,
    visibleBranches: List<ChannelCategory>,
    visibleChannels: List<LiveChannel>,
    allCategories: List<ChannelCategory>,
    allChannels: List<LiveChannel>,
    banners: List<EventBanner> = emptyList(),
    publishedEvents: List<PublishedEventItem> = emptyList(),
    channelStatsMap: Map<String, ChannelEngagementStats> = emptyMap(),
    showChannelViewsAndLikes: Boolean = true,
    showAdsInApp: Boolean = true,
    telegramUrl: String = "https://t.me/jdj_q",
    // Stories, Reels, Matches & Home Management Data
    stories: List<StoryItem> = emptyList(),
    watchedStoryIds: Set<String> = emptySet(),
    isUploadingStory: Boolean = false,
    storyUploadStatusMessage: String? = null,
    reels: List<ReelItem> = emptyList(),
    allReels: List<ReelItem> = emptyList(),
    watchedReelIds: Set<String> = emptySet(),
    isLoadingReels: Boolean = false,
    leagueGroups: List<LeagueMatchesGroup> = emptyList(),
    liveEvents: List<LiveEventMatch> = emptyList(),
    matchPublishedChannels: List<MatchPublishedChannel> = emptyList(),
    selectedDateString: String = "",
    isLoadingMatches: Boolean = false,
    libraryItems: List<LibraryMediaItem> = emptyList(),
    contactMessages: List<ContactMessageItem> = emptyList(),
    homeManagementConfig: HomeManagementConfig = HomeManagementConfig(),
    // Actions
    onOpenBranch: (ChannelCategory) -> Unit,
    onNavigateBackBranch: () -> Unit,
    onChannelClick: (LiveChannel) -> Unit,
    onSeeAllChannelsClick: () -> Unit = {},
    onBannerClick: (EventBanner) -> Unit = {},
    onPlayPublishedEventVideo: (PublishedEventItem) -> Unit = {},
    onOpenReel: (ReelItem) -> Unit = {},
    onSeeAllReelsClick: () -> Unit = {},
    onMatchClick: (MatchItem) -> Unit = {},
    onSeeAllMatchesClick: () -> Unit = {},
    onOpenLibraryItem: (LibraryMediaItem) -> Unit = {},
    onUploadStoryMedia: (Uri, String) -> Unit = { _, _ -> },
    onMarkStoryWatched: (String) -> Unit = {},
    onRefresh: () -> Unit
) {
    val context = LocalContext.current
    var activeStoryGroupForViewer by remember { mutableStateOf<StoryCircleGroup?>(null) }

    // Search & Filter state for the Home Screen Search Bar
    var homeSearchQuery by remember { mutableStateOf("") }
    var selectedSearchCategory by remember { mutableStateOf(HomeSearchFilterCategory.ALL) }
    var showSearchFilterBar by remember { mutableStateOf(false) }

    // Reel 3-dots options dialog state (smart link copy/share)
    var selectedReelForOptions by remember { mutableStateOf<ReelItem?>(null) }

    if (homeSearchQuery.isNotBlank() || selectedSearchCategory != HomeSearchFilterCategory.ALL) {
        BackHandler {
            homeSearchQuery = ""
            selectedSearchCategory = HomeSearchFilterCategory.ALL
            showSearchFilterBar = false
        }
    }

    val activeOptionsReel = selectedReelForOptions
    if (activeOptionsReel != null) {
        com.example.ui.components.FoulCopyShareDialog(
            title = activeOptionsReel.caption.ifBlank { "مقطع ريلز • ${activeOptionsReel.userPersonalName.ifBlank { activeOptionsReel.userName }}" },
            videoStreamUrl = activeOptionsReel.streamUrl.ifBlank { activeOptionsReel.videoUrl },
            smartShareUrl = "foultv://reel/${activeOptionsReel.id}",
            officialChannelUrl = telegramUrl,
            onDismiss = { selectedReelForOptions = null }
        )
    }

    val handleReelOptionsClick: (ReelItem) -> Unit = { reel ->
        selectedReelForOptions = reel
    }

    var pickerTitle by remember { mutableStateOf("") }
    var pickerSubtitle by remember { mutableStateOf("") }
    var pickerChannels by remember { mutableStateOf<List<LiveChannel>>(emptyList()) }

    if (pickerChannels.isNotEmpty()) {
        MatchAndEventChannelPickerDialog(
            title = pickerTitle,
            subtitle = pickerSubtitle,
            channels = pickerChannels,
            onSelectChannel = { ch ->
                pickerChannels = emptyList()
                onChannelClick(ch)
            },
            onDismiss = { pickerChannels = emptyList() }
        )
    }

    val effectiveAllReels = remember(allReels, reels) {
        if (allReels.isNotEmpty()) allReels else reels
    }
    val unwatchedHomeReels = remember(effectiveAllReels, watchedReelIds) {
        effectiveAllReels.filterNot { watchedReelIds.contains(it.id) }
    }
    val storyGroups = remember(
        stories,
        unwatchedHomeReels,
        leagueGroups,
        libraryItems,
        publishedEvents,
        watchedStoryIds,
        homeManagementConfig
    ) {
        buildHomeStoryCircleGroups(
            userStories = stories,
            allReels = unwatchedHomeReels,
            leagueGroups = leagueGroups,
            libraryItems = libraryItems,
            publishedEvents = publishedEvents,
            watchedStoryIds = watchedStoryIds,
            homeConfig = homeManagementConfig
        )
    }

    val primaryReelsList = remember(unwatchedHomeReels) {
        unwatchedHomeReels.take(10)
    }

    // Filtered collections when the user searches or selects a filter chip
    val trimmedQuery = homeSearchQuery.trim()
    val isSearchOrFilterActive = trimmedQuery.isNotEmpty() || selectedSearchCategory != HomeSearchFilterCategory.ALL

    val filteredReels = remember(effectiveAllReels, unwatchedHomeReels, trimmedQuery) {
        if (trimmedQuery.isEmpty()) unwatchedHomeReels
        else effectiveAllReels.filter { r ->
            r.caption.contains(trimmedQuery, ignoreCase = true) ||
                r.userPersonalName.contains(trimmedQuery, ignoreCase = true) ||
                r.userName.contains(trimmedQuery, ignoreCase = true) ||
                r.userHandle.contains(trimmedQuery, ignoreCase = true)
        }
    }

    val filteredLeagueGroups = remember(leagueGroups, trimmedQuery) {
        if (trimmedQuery.isEmpty()) leagueGroups
        else leagueGroups.mapNotNull { g ->
            val matched = g.matches.filter { m ->
                m.homeTeam.name.contains(trimmedQuery, ignoreCase = true) ||
                    m.awayTeam.name.contains(trimmedQuery, ignoreCase = true) ||
                    m.competitionName.contains(trimmedQuery, ignoreCase = true)
            }
            if (matched.isNotEmpty()) g.copy(matches = matched) else null
        }
    }

    val filteredChannels = remember(allChannels, trimmedQuery) {
        if (trimmedQuery.isEmpty()) allChannels
        else allChannels.filter { ch ->
            ch.name.contains(trimmedQuery, ignoreCase = true) ||
                ch.category.contains(trimmedQuery, ignoreCase = true)
        }
    }

    val filteredMediaItems = remember(libraryItems, trimmedQuery) {
        if (trimmedQuery.isEmpty()) libraryItems
        else libraryItems.filter { item ->
            item.name.contains(trimmedQuery, ignoreCase = true) ||
                item.releaseYear.contains(trimmedQuery, ignoreCase = true)
        }
    }

    val pinnedCustomSections = remember(homeManagementConfig.customSections) {
        homeManagementConfig.customSections.filter { it.isPinnedFirst }
    }
    val regularCustomSections = remember(homeManagementConfig.customSections) {
        homeManagementConfig.customSections.filterNot { it.isPinnedFirst }
    }

    val handleCustomSectionAction: (HomeCustomSectionItem) -> Unit = { sec ->
        when (sec.actionType.uppercase()) {
            "REELS" -> onSeeAllReelsClick()
            "MATCHES" -> onSeeAllMatchesClick()
            "CHANNELS" -> onSeeAllChannelsClick()
            "CHANNEL" -> {
                val targetCh = allChannels.find {
                    it.id.equals(sec.targetValue, ignoreCase = true) ||
                        it.name.contains(sec.targetValue, ignoreCase = true)
                }
                if (targetCh != null) {
                    onChannelClick(targetCh)
                } else {
                    onSeeAllChannelsClick()
                }
            }
            "URL" -> {
                val rawUrl = sec.targetValue.trim()
                if (rawUrl.isNotBlank()) {
                    runCatching {
                        val intent = android.content.Intent(
                            android.content.Intent.ACTION_VIEW,
                            Uri.parse(if (rawUrl.startsWith("http")) rawUrl else "https://$rawUrl")
                        ).apply { addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK) }
                        context.startActivity(intent)
                    }
                }
            }
            else -> onSeeAllChannelsClick()
        }
    }

    // Fullscreen Story Viewer Dialog
    val currentViewerGroup = activeStoryGroupForViewer
    if (currentViewerGroup != null) {
        FullScreenStoryViewerDialog(
            group = currentViewerGroup,
            allGroups = storyGroups,
            onMarkStoryWatched = onMarkStoryWatched,
            onOpenLinkedReel = { reelId ->
                val targetReel = effectiveAllReels.find { it.id == reelId }
                if (targetReel != null) {
                    onOpenReel(targetReel)
                } else {
                    onSeeAllReelsClick()
                }
            },
            onOpenLinkedMatch = { matchId ->
                val targetMatch = leagueGroups.flatMap { it.matches }.find { it.id == matchId }
                if (targetMatch != null) {
                    onMatchClick(targetMatch)
                } else {
                    onSeeAllMatchesClick()
                }
            },
            onOpenLinkedMedia = { mediaId ->
                val targetMedia = libraryItems.find { it.id == mediaId }
                if (targetMedia != null) {
                    onOpenLibraryItem(targetMedia)
                }
            },
            onDismiss = { activeStoryGroupForViewer = null }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
            .testTag("home_screen")
    ) {
        // 1. Clean Top Bar: Matches + Favorites Heart on Right, Refresh + Telegram on Left
        FoulHomeBroadcastHeader(
            telegramUrl = telegramUrl,
            onOpenMatchesSchedule = onSeeAllMatchesClick,
            onRefresh = onRefresh
        )

        // 1B. Real App-Wide Search Bar + Filter Button directly below Top Bar
        FoulHomeSearchBar(
            searchQuery = homeSearchQuery,
            onSearchQueryChange = { homeSearchQuery = it },
            selectedCategory = selectedSearchCategory,
            onSelectCategory = { selectedSearchCategory = it },
            showFilterBar = showSearchFilterBar,
            onToggleFilterBar = { showSearchFilterBar = !showSearchFilterBar }
        )

        Box(modifier = Modifier.weight(1f)) {
            when {
                isLoading && allChannels.isEmpty() && allCategories.isEmpty() && effectiveAllReels.isEmpty() && leagueGroups.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(color = FoulHomePurpleBright)
                            Text(
                                text = "جاري تحميل محتوى FOUL TV...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = FoulHomeTextSecondary
                            )
                        }
                    }
                }

                errorMessage != null && allChannels.isEmpty() && allCategories.isEmpty() && effectiveAllReels.isEmpty() && leagueGroups.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = FoulHomeCardBg),
                            shape = RoundedCornerShape(18.dp),
                            border = BorderStroke(1.dp, FoulHomeBorderDark),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "تعذر الاتصال بقاعدة البيانات",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = FoulLiveRed,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = errorMessage,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = FoulHomeTextSecondary,
                                    textAlign = TextAlign.Center
                                )
                                Button(
                                    onClick = onRefresh,
                                    colors = ButtonDefaults.buttonColors(containerColor = FoulHomePurplePrimary),
                                    modifier = Modifier.testTag("retry_home_button")
                                ) {
                                    Text("إعادة المحاولة", color = Color.White)
                                }
                            }
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF000000))
                            .testTag("home_content_list"),
                        contentPadding = PaddingValues(bottom = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (isSearchOrFilterActive) {
                            // Live Search / Filter Results across Reels, Matches, Channels, and Movies/Series
                            val showReelsFilter = selectedSearchCategory == HomeSearchFilterCategory.ALL ||
                                selectedSearchCategory == HomeSearchFilterCategory.REELS
                            val showMatchesFilter = selectedSearchCategory == HomeSearchFilterCategory.ALL ||
                                selectedSearchCategory == HomeSearchFilterCategory.MATCHES
                            val showChannelsFilter = selectedSearchCategory == HomeSearchFilterCategory.ALL ||
                                selectedSearchCategory == HomeSearchFilterCategory.CHANNELS
                            val showMediaFilter = selectedSearchCategory == HomeSearchFilterCategory.ALL ||
                                selectedSearchCategory == HomeSearchFilterCategory.MEDIA

                            if (showReelsFilter && filteredReels.isNotEmpty()) {
                                item(key = "search_results_reels") {
                                    HomeReelsHorizontalSection(
                                        reels = filteredReels.take(15),
                                        isLoading = false,
                                        onReelClick = onOpenReel,
                                        onSeeAllReelsClick = onSeeAllReelsClick,
                                        onReelOptionsClick = handleReelOptionsClick
                                    )
                                }
                            }

                            if (showMatchesFilter && filteredLeagueGroups.isNotEmpty()) {
                                item(key = "search_results_matches") {
                                    HomeSuggestedMatchesSection(
                                        leagueGroups = filteredLeagueGroups,
                                        allChannels = allChannels,
                                        liveEvents = liveEvents,
                                        matchPublishedChannels = matchPublishedChannels,
                                        selectedDateString = selectedDateString,
                                        isLoadingMatches = false,
                                        onMatchClick = onMatchClick,
                                        onWatchMatchChannels = { match, channels ->
                                            if (channels.size == 1) {
                                                onChannelClick(channels.first())
                                            } else if (channels.size > 1) {
                                                pickerTitle = "${match.homeTeam.name} ضد ${match.awayTeam.name}"
                                                pickerSubtitle = "اختر القناة الناقلة لمشاهدة المباراة مباشرة"
                                                pickerChannels = channels
                                            }
                                        },
                                        onSeeAllMatchesClick = onSeeAllMatchesClick
                                    )
                                }
                            }

                            if (showChannelsFilter && filteredChannels.isNotEmpty()) {
                                item(key = "search_results_channels") {
                                    HomeSuggestedChannelsSection(
                                        allChannels = filteredChannels.take(16),
                                        customFeaturedIds = filteredChannels.take(16).map { it.id },
                                        onChannelClick = onChannelClick,
                                        onSeeAllChannelsClick = onSeeAllChannelsClick
                                    )
                                }
                            }

                            if (showMediaFilter && filteredMediaItems.isNotEmpty()) {
                                item(key = "search_results_media") {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                    ) {
                                        FoulHomeSectionHeader(
                                            title = "الأفلام والمسلسلات (${filteredMediaItems.size})",
                                            iconBadge = {
                                                Box(
                                                    modifier = Modifier
                                                        .width(4.dp)
                                                        .height(20.dp)
                                                        .clip(RoundedCornerShape(50))
                                                        .background(FoulHomePurpleBright)
                                                )
                                            },
                                            seeAllTestTag = "search_media_see_all",
                                            onSeeAllClick = { filteredMediaItems.firstOrNull()?.let(onOpenLibraryItem) }
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        androidx.compose.foundation.lazy.LazyRow(
                                            contentPadding = PaddingValues(horizontal = 14.dp),
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            items(
                                                count = filteredMediaItems.take(15).size,
                                                key = { idx -> "search_media_${filteredMediaItems[idx].id}" }
                                            ) { idx ->
                                                val media = filteredMediaItems[idx]
                                                Surface(
                                                    color = FoulHomeCardBg,
                                                    shape = RoundedCornerShape(14.dp),
                                                    border = BorderStroke(1.dp, FoulHomeBorderDark),
                                                    modifier = Modifier
                                                        .width(136.dp)
                                                        .height(190.dp)
                                                        .clip(RoundedCornerShape(14.dp))
                                                        .clickable { onOpenLibraryItem(media) }
                                                ) {
                                                    Box(modifier = Modifier.fillMaxSize()) {
                                                        if (media.posterUrl.isNotBlank()) {
                                                            AsyncImage(
                                                                model = media.posterUrl,
                                                                contentDescription = media.name,
                                                                contentScale = ContentScale.Crop,
                                                                modifier = Modifier.fillMaxSize()
                                                            )
                                                        }
                                                        Box(
                                                            modifier = Modifier
                                                                .fillMaxSize()
                                                                .background(
                                                                    Brush.verticalGradient(
                                                                        colors = listOf(Color.Transparent, Color(0xE6000000))
                                                                    )
                                                                )
                                                        )
                                                        Text(
                                                            text = media.name,
                                                            color = Color.White,
                                                            fontSize = 11.5.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            maxLines = 2,
                                                            overflow = TextOverflow.Ellipsis,
                                                            modifier = Modifier
                                                                .align(Alignment.BottomStart)
                                                                .padding(8.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            val hasAnyMatch = (showReelsFilter && filteredReels.isNotEmpty()) ||
                                (showMatchesFilter && filteredLeagueGroups.isNotEmpty()) ||
                                (showChannelsFilter && filteredChannels.isNotEmpty()) ||
                                (showMediaFilter && filteredMediaItems.isNotEmpty())

                            if (!hasAnyMatch) {
                                item(key = "search_no_results") {
                                    Surface(
                                        color = FoulHomeCardBg,
                                        shape = RoundedCornerShape(16.dp),
                                        border = BorderStroke(1.dp, FoulHomeBorderDark),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 14.dp, vertical = 16.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(24.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Search,
                                                contentDescription = null,
                                                tint = FoulHomeTextSecondary,
                                                modifier = Modifier.size(32.dp)
                                            )
                                            Text(
                                                text = "لا توجد نتائج مطابقة لبحثك",
                                                color = Color.White,
                                                fontSize = 14.5.sp,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                            Text(
                                                text = "جرب البحث بكلمات أخرى أو تغيير تصنيف الفلترة",
                                                color = FoulHomeTextSecondary,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            // Pinned First Custom Developer Sections (if any)
                            if (pinnedCustomSections.isNotEmpty()) {
                                item(key = "home_pinned_custom_sections") {
                                    Column {
                                        HomeDeveloperCustomSectionsList(
                                            sections = pinnedCustomSections,
                                            onSectionClick = handleCustomSectionAction
                                        )
                                        HomeGlowingDivider()
                                    }
                                }
                            }

                            // 2. STORIES SECTION (قسم القصص — فقط قصص المطور والأقسام اليومية المتجددة)
                            if (homeManagementConfig.showStoriesSection && storyGroups.isNotEmpty()) {
                                item(key = "home_section_stories") {
                                    Column {
                                        HomeStoriesSection(
                                            storyGroups = storyGroups,
                                            onOpenStoryGroup = { group ->
                                                activeStoryGroupForViewer = group
                                            },
                                            onSeeAllStoriesClick = {
                                                activeStoryGroupForViewer = storyGroups.firstOrNull()
                                            }
                                        )
                                        HomeGlowingDivider()
                                    }
                                }
                            }

                            // 3. REELS SECTION 1 (قسم الريلز الأول)
                            if (homeManagementConfig.showReelsSection) {
                                item(key = "home_section_reels") {
                                    Column {
                                        HomeReelsHorizontalSection(
                                            reels = primaryReelsList,
                                            isLoading = isLoadingReels,
                                            onReelClick = onOpenReel,
                                            onSeeAllReelsClick = onSeeAllReelsClick,
                                            onReelOptionsClick = handleReelOptionsClick
                                        )
                                        HomeGlowingDivider()
                                    }
                                }
                            }

                            // 4. SUGGESTED MATCHES SECTION (قسم المباريات المقترحة)
                            if (homeManagementConfig.showSuggestedMatchesSection) {
                                item(key = "home_section_suggested_matches") {
                                    Column {
                                        HomeSuggestedMatchesSection(
                                            leagueGroups = leagueGroups,
                                            allChannels = allChannels,
                                            liveEvents = liveEvents,
                                            matchPublishedChannels = matchPublishedChannels,
                                            selectedDateString = selectedDateString,
                                            isLoadingMatches = isLoadingMatches,
                                            onMatchClick = onMatchClick,
                                            onWatchMatchChannels = { match, channels ->
                                                if (channels.size == 1) {
                                                    onChannelClick(channels.first())
                                                } else if (channels.size > 1) {
                                                    pickerTitle = "${match.homeTeam.name} ضد ${match.awayTeam.name}"
                                                    pickerSubtitle = "اختر القناة الناقلة لمشاهدة المباراة مباشرة"
                                                    pickerChannels = channels
                                                }
                                            },
                                            onSeeAllMatchesClick = onSeeAllMatchesClick
                                        )
                                        HomeGlowingDivider()
                                    }
                                }
                            }

                            // 5. ADDITIONAL REELS SECTION ("فيديوهات قد تعجبك" — أسفل قسم المباريات المقترحة)
                            if (homeManagementConfig.showReelsSection && effectiveAllReels.isNotEmpty()) {
                                item(key = "home_section_more_reels") {
                                    Column {
                                        HomeMoreReelsVideosSection(
                                            reels = effectiveAllReels,
                                            onReelClick = onOpenReel,
                                            onSeeAllReelsClick = onSeeAllReelsClick,
                                            onReelOptionsClick = handleReelOptionsClick
                                        )
                                        HomeGlowingDivider()
                                    }
                                }
                            }

                            // 6. SUGGESTED CHANNELS SECTION (قسم القنوات المقترحة — مثل beIN Sports 1 / 2 وتتبدل كل يوم)
                            if (homeManagementConfig.showSuggestedChannelsSection && allChannels.isNotEmpty()) {
                                item(key = "home_section_suggested_channels") {
                                    Column {
                                        HomeSuggestedChannelsSection(
                                            allChannels = allChannels,
                                            customFeaturedIds = homeManagementConfig.customFeaturedChannelIds,
                                            onChannelClick = onChannelClick,
                                            onSeeAllChannelsClick = onSeeAllChannelsClick
                                        )
                                        HomeGlowingDivider()
                                    }
                                }
                            }

                            // 7. PUBLISHED EVENTS CAROUSEL (الأحداث المباشرة المنشورة من المطور)
                            if (publishedEvents.isNotEmpty()) {
                                item(key = "published_events_carousel") {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        PublishedEventsAutoPagerCarousel(
                                            events = publishedEvents,
                                            allChannels = allChannels,
                                            onWatchEvent = { eventItem, resolvedChannels ->
                                                val isVideo = eventItem.eventType.equals("VIDEO", ignoreCase = true) ||
                                                    eventItem.videoUrl.isNotBlank()
                                                if (isVideo && eventItem.videoUrl.isNotBlank()) {
                                                    onPlayPublishedEventVideo(eventItem)
                                                } else if (resolvedChannels.size == 1) {
                                                    onChannelClick(resolvedChannels.first())
                                                } else if (resolvedChannels.size > 1) {
                                                    pickerTitle = eventItem.title.ifBlank { "القنوات الناقلة للحدث" }
                                                    pickerSubtitle = "اختر القناة الناقلة لمشاهدة الحدث مباشرة"
                                                    pickerChannels = resolvedChannels
                                                }
                                            }
                                        )
                                        HomeGlowingDivider()
                                    }
                                }
                            }

                            // 8. Regular Custom Developer Sections (if any)
                            if (regularCustomSections.isNotEmpty()) {
                                item(key = "home_regular_custom_sections") {
                                    HomeDeveloperCustomSectionsList(
                                        sections = regularCustomSections,
                                        onSectionClick = handleCustomSectionAction
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showAdsInApp) {
            AdMobBanner()
        }
    }
}

/**
 * ============================================================================
 * DEDICATED CHANNELS & PACKAGES SCREEN ("إضافة قسم بجانب الرئيسية باسم القنوات وعند الضغط تظهر الباقات والقنوات المباشرة")
 * ============================================================================
 */
@Composable
fun ChannelsScreen(
    isLoading: Boolean,
    errorMessage: String?,
    currentBranch: ChannelCategory?,
    visibleBranches: List<ChannelCategory>,
    visibleChannels: List<LiveChannel>,
    allCategories: List<ChannelCategory>,
    allChannels: List<LiveChannel>,
    publishedEvents: List<PublishedEventItem> = emptyList(),
    channelStatsMap: Map<String, ChannelEngagementStats> = emptyMap(),
    showChannelViewsAndLikes: Boolean = true,
    showAdsInApp: Boolean = true,
    telegramUrl: String = "https://t.me/jdj_q",
    onOpenBranch: (ChannelCategory) -> Unit,
    onNavigateBackBranch: () -> Unit,
    onChannelClick: (LiveChannel) -> Unit,
    onPlayPublishedEventVideo: (PublishedEventItem) -> Unit = {},
    onRefresh: () -> Unit
) {
    var isChannelsSearchOpen by remember { mutableStateOf(false) }
    var channelsSearchQuery by remember { mutableStateOf("") }

    if (isChannelsSearchOpen) {
        BackHandler {
            isChannelsSearchOpen = false
            channelsSearchQuery = ""
        }
    } else if (currentBranch != null) {
        BackHandler {
            onNavigateBackBranch()
        }
    }

    var pickerTitle by remember { mutableStateOf("") }
    var pickerSubtitle by remember { mutableStateOf("") }
    var pickerChannels by remember { mutableStateOf<List<LiveChannel>>(emptyList()) }

    if (pickerChannels.isNotEmpty()) {
        MatchAndEventChannelPickerDialog(
            title = pickerTitle,
            subtitle = pickerSubtitle,
            channels = pickerChannels,
            onSelectChannel = { ch ->
                pickerChannels = emptyList()
                onChannelClick(ch)
            },
            onDismiss = { pickerChannels = emptyList() }
        )
    }

    val filteredBranches = remember(visibleBranches, allCategories, isChannelsSearchOpen, channelsSearchQuery) {
        val q = channelsSearchQuery.trim()
        if (!isChannelsSearchOpen || q.isEmpty()) {
            visibleBranches
        } else {
            allCategories.filter { it.name.contains(q, ignoreCase = true) }
        }
    }

    val filteredChannels = remember(visibleChannels, allChannels, isChannelsSearchOpen, channelsSearchQuery) {
        val q = channelsSearchQuery.trim()
        if (!isChannelsSearchOpen || q.isEmpty()) {
            visibleChannels
        } else {
            allChannels.filter {
                it.name.contains(q, ignoreCase = true) ||
                    it.category.contains(q, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
            .testTag("channels_screen")
    ) {
        FoulSmartTopBar(
            telegramUrl = telegramUrl,
            telegramTestTag = "channels_telegram_button"
        )

        FoulSectionSubBar(
            title = currentBranch?.name ?: "القنوات والباقات المباشرة",
            subtitle = if (currentBranch != null) {
                "تصفح محتويات (${currentBranch.name})"
            } else {
                "جميع الباقات والقنوات المباشرة (${allChannels.size} قناة)"
            },
            leadingIcon = Icons.Default.LiveTv,
            onBackClick = if (currentBranch != null) onNavigateBackBranch else null,
            backButtonTestTag = "channels_branch_back_button",
            onSearchClick = {
                isChannelsSearchOpen = !isChannelsSearchOpen
                if (!isChannelsSearchOpen) channelsSearchQuery = ""
            },
            searchButtonLabel = if (isChannelsSearchOpen) "إغلاق" else "بحث",
            searchButtonTestTag = "channels_search_button",
            actions = {
                CompactTopBarIconButton(
                    icon = Icons.Default.Refresh,
                    contentDescription = "تحديث القنوات",
                    onClick = onRefresh,
                    modifier = Modifier.testTag("channels_refresh_button")
                )
            }
        )

        AnimatedVisibility(visible = isChannelsSearchOpen) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF040308))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = channelsSearchQuery,
                    onValueChange = { channelsSearchQuery = it },
                    placeholder = {
                        Text(
                            text = "ابحث عن باقة أو قناة مباشرة...",
                            color = FoulHomeTextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "بحث",
                            tint = Color.White
                        )
                    },
                    trailingIcon = {
                        if (channelsSearchQuery.isNotEmpty()) {
                            IconButton(onClick = { channelsSearchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "مسح",
                                    tint = FoulHomeTextSecondary
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = FoulHomeCardBg,
                        unfocusedContainerColor = FoulHomeCardBg,
                        focusedBorderColor = FoulHomePurpleBright,
                        unfocusedBorderColor = FoulHomeBorderPurple,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("channels_search_input")
                )
            }
        }

        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            val gridColumns = when {
                maxWidth >= 960.dp -> 5
                maxWidth >= 660.dp -> 4
                else -> 3
            }

            when {
                isLoading && allChannels.isEmpty() && allCategories.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = FoulHomePurpleBright)
                    }
                }

                errorMessage != null && allChannels.isEmpty() && allCategories.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = onRefresh,
                            colors = ButtonDefaults.buttonColors(containerColor = FoulHomePurplePrimary)
                        ) {
                            Text("إعادة المحاولة", color = Color.White)
                        }
                    }
                }

                else -> {
                    val gridState = rememberLazyGridState()
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(gridColumns),
                        state = gridState,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF000000))
                            .testTag("channels_grid"),
                        contentPadding = PaddingValues(top = 10.dp, bottom = 18.dp),
                        horizontalArrangement = Arrangement.spacedBy(9.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (filteredBranches.isEmpty() && filteredChannels.isEmpty()) {
                            item(
                                key = "empty_branch_or_search",
                                span = { GridItemSpan(gridColumns) }
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    FoulCustomPackageIcon(
                                        tint = FoulPurpleGlow,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Text(
                                        text = if (currentBranch != null) {
                                            "هذا القسم (${currentBranch.name}) فارغ حالياً"
                                        } else {
                                            "لا توجد باقات أو قنوات مطابقة للبحث"
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        color = FoulTextPrimary,
                                        textAlign = TextAlign.Center
                                    )
                                    if (currentBranch != null) {
                                        OutlinedButton(
                                            onClick = onNavigateBackBranch,
                                            border = BorderStroke(1.dp, FoulHomePurpleBright)
                                        ) {
                                            Text("العودة للباقات الرئيسية", color = Color.White)
                                        }
                                    }
                                }
                            }
                        }

                        if (filteredBranches.isNotEmpty()) {
                            items(
                                items = filteredBranches,
                                key = { "branch_${it.id}" },
                                span = { GridItemSpan(gridColumns) },
                                contentType = { "branch_folder" }
                            ) { branch ->
                                val subBranchesCount = remember(allCategories, branch.id) {
                                    allCategories.count { it.parentId == branch.id }
                                }
                                val directChannelsCount = remember(allChannels, branch) {
                                    allChannels.count {
                                        it.branchId == branch.id ||
                                            (it.branchId.isBlank() && it.category.equals(branch.name, ignoreCase = true))
                                    }
                                }

                                Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                                    BranchFolderCard(
                                        branch = branch,
                                        subBranchesCount = subBranchesCount,
                                        directChannelsCount = directChannelsCount,
                                        onClick = {
                                            if (isChannelsSearchOpen) {
                                                isChannelsSearchOpen = false
                                                channelsSearchQuery = ""
                                            }
                                            onOpenBranch(branch)
                                        }
                                    )
                                }
                            }
                        }

                        if (filteredBranches.isNotEmpty() && filteredChannels.isNotEmpty()) {
                            item(
                                key = "channels_section_header",
                                span = { GridItemSpan(gridColumns) },
                                contentType = "section_header"
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(4.dp)
                                            .height(16.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(FoulHomePurpleBright)
                                    )
                                    Text(
                                        text = "القنوات المباشرة المتوفرة (${filteredChannels.size})",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }

                        items(
                            items = filteredChannels,
                            key = { "channel_${it.id}" },
                            span = { GridItemSpan(1) },
                            contentType = { "live_channel_card" }
                        ) { channel ->
                            val socialStats = channelStatsMap[channel.id]
                            val totalViews = maxOf(channel.views, socialStats?.views ?: 0)
                            val totalLikes = socialStats?.likes ?: 0
                            Box(modifier = Modifier.padding(horizontal = 5.dp)) {
                                LiveChannelGridCard(
                                    channel = channel,
                                    totalViews = totalViews,
                                    totalLikes = totalLikes,
                                    showViewsAndLikes = showChannelViewsAndLikes,
                                    onClick = { onChannelClick(channel) }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showAdsInApp) {
            AdMobBanner()
        }
    }
}

private val BranchCardShape = RoundedCornerShape(16.dp)

private val BranchCardBorderBrush = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF282834),
        Color(0xFF1A1A24),
        Color(0xFF16161E)
    )
)

private val BranchCardBgBrush = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF0E0E14),
        Color(0xFF09090D),
        Color(0xFF050508)
    )
)

private val ChannelCardShape = RoundedCornerShape(15.dp)

private val ChannelCardBorderBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF242430),
        Color(0xFF171720)
    )
)

private val ChannelCardBgBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF0C0C11),
        Color(0xFF060609)
    )
)

@Composable
private fun BranchFolderCard(
    branch: ChannelCategory,
    subBranchesCount: Int,
    directChannelsCount: Int,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val imageRequest = remember(branch.imageUrl) {
        if (branch.imageUrl.isNotBlank()) {
            ImageRequest.Builder(context)
                .data(branch.imageUrl)
                .size(160)
                .allowHardware(true)
                .crossfade(false)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build()
        } else null
    }

    Card(
        shape = BranchCardShape,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF08080C)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
            .clip(BranchCardShape)
            .tvFocusableClickable(
                shape = BranchCardShape,
                scaleOnFocus = 1.02f,
                onClick = onClick
            )
            .border(
                width = 1.dp,
                brush = BranchCardBorderBrush,
                shape = BranchCardShape
            )
            .testTag("branch_folder_${branch.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(BranchCardBgBrush)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF121218))
                        .border(1.dp, Color(0xFF282834), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageRequest != null) {
                        AsyncImage(
                            model = imageRequest,
                            contentDescription = branch.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        FoulCustomPackageIcon(
                            tint = Color.White,
                            modifier = Modifier.size(23.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = branch.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = when {
                            subBranchesCount > 0 -> "يحتوي على $subBranchesCount قسم فرعي • اضغط للفتح"
                            directChannelsCount > 0 -> "باقة تضم $directChannelsCount قناة مباشرة • اضغط للفتح"
                            else -> "اضغط لتصفح محتويات هذه الباقة"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF9494A6),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val badgeText = when {
                    subBranchesCount > 0 && directChannelsCount > 0 -> "$subBranchesCount قسم • $directChannelsCount قناة"
                    subBranchesCount > 0 -> "$subBranchesCount أقسام"
                    directChannelsCount > 0 -> "$directChannelsCount قناة"
                    else -> "فتح"
                }

                Surface(
                    color = Color(0xFF121218),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(0.8.dp, Color(0xFF262632))
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(Color(0xFF16161F))
                        .border(1.dp, Color(0xFF2E2E3D), RoundedCornerShape(9.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "فتح القسم",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun LiveChannelGridCard(
    channel: LiveChannel,
    totalViews: Int,
    totalLikes: Int,
    showViewsAndLikes: Boolean = true,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val favChannelIds by FavoritesManager.favoriteChannelIds.collectAsState()
    val isFavorite = favChannelIds.contains(channel.id)
    var isLogoLoaded by remember(channel.imageUrl) { mutableStateOf(false) }
    val imageRequest = remember(channel.imageUrl) {
        if (channel.imageUrl.isNotBlank()) {
            ImageRequest.Builder(context)
                .data(channel.imageUrl)
                .size(160)
                .allowHardware(true)
                .crossfade(false)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .build()
        } else null
    }

    Card(
        shape = ChannelCardShape,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF08080C)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = if (showViewsAndLikes) 156.dp else 138.dp)
            .clip(ChannelCardShape)
            .tvFocusableClickable(
                shape = ChannelCardShape,
                scaleOnFocus = 1.04f,
                onClick = onClick
            )
            .border(
                width = 1.dp,
                brush = ChannelCardBorderBrush,
                shape = ChannelCardShape
            )
            .testTag("channel_card_${channel.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ChannelCardBgBrush)
                .padding(horizontal = 6.dp, vertical = 7.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(5.5.dp)
                            .clip(CircleShape)
                            .background(FoulLiveGreen)
                    )
                    Text(
                        text = "LIVE",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                        color = FoulLiveGreen,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (channel.servers.size > 1) {
                        Surface(
                            color = Color(0xFF101016),
                            shape = RoundedCornerShape(5.dp),
                            border = BorderStroke(0.6.dp, Color(0xFF252532))
                        ) {
                            Text(
                                text = "${channel.servers.size} سيرفر",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                                color = Color(0xFF9E9EB0),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF101016))
                            .border(
                                0.7.dp,
                                if (isFavorite) Color(0xFFF43F5E) else Color(0xFF252532),
                                CircleShape
                            )
                            .clickable {
                                FavoritesManager.toggleChannelFavorite(context, channel)
                            }
                            .testTag("channel_favorite_button_${channel.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "إضافة للمفضلة",
                            tint = if (isFavorite) Color(0xFFF43F5E) else Color(0xFFA0A0B2),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF111117))
                    .border(1.dp, Color(0xFF282836), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (!isLogoLoaded) {
                    Icon(
                        imageVector = Icons.Default.LiveTv,
                        contentDescription = channel.name,
                        tint = Color.White.copy(alpha = 0.75f),
                        modifier = Modifier.size(22.dp)
                    )
                }
                if (imageRequest != null) {
                    AsyncImage(
                        model = imageRequest,
                        contentDescription = channel.name,
                        onSuccess = { isLogoLoaded = true },
                        onError = { isLogoLoaded = false },
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0A0A0E))
                    .border(0.6.dp, Color(0xFF1E1E28), RoundedCornerShape(8.dp))
                    .padding(horizontal = 5.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = channel.name,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = if (channel.name.length > 22) 9.5.sp else 10.5.sp,
                        lineHeight = 12.5.sp
                    ),
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    softWrap = true,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (showViewsAndLikes) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(7.dp))
                        .background(Color(0xFF0A0A0E))
                        .border(0.7.dp, Color(0xFF1E1E28), RoundedCornerShape(7.dp))
                        .padding(horizontal = 5.dp, vertical = 2.5.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "المشاهدات",
                            tint = FoulHomeTextSecondary,
                            modifier = Modifier.size(10.dp)
                        )
                        Text(
                            text = formatCompactCount(totalViews),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            color = FoulHomeTextSecondary
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "الإعجابات",
                            tint = FoulGoldStar,
                            modifier = Modifier.size(10.dp)
                        )
                        Text(
                            text = formatCompactCount(totalLikes),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            color = FoulGoldStar,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            var showChannelShareDialog by remember(channel.id) { mutableStateOf(false) }
            Surface(
                onClick = { showChannelShareDialog = true },
                color = FoulHomeBgPrimary,
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(0.6.dp, FoulHomePurpleBright.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("channel_copy_share_${channel.id}")
            ) {
                Text(
                    text = "نسخ ومشاركة",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.5.sp),
                    color = FoulHomePurpleLight,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 2.5.dp)
                )
            }

            if (showChannelShareDialog) {
                val streamUrl = channel.servers.firstOrNull()?.streamUrl.orEmpty()
                val smartUrl = com.example.data.repository.ReelsRepository.buildSmartShareUrl(
                    type = "channel",
                    id = channel.id,
                    title = channel.name,
                    streamUrl = streamUrl
                )
                com.example.ui.components.FoulCopyShareDialog(
                    title = channel.name,
                    videoStreamUrl = streamUrl,
                    smartShareUrl = smartUrl,
                    onDismiss = { showChannelShareDialog = false }
                )
            }
        }
    }
}

private fun formatCompactCount(value: Int): String {
    return when {
        value >= 1_000_000 -> String.format("%.1fM", value / 1_000_000f)
        value >= 1_000 -> String.format("%.1fK", value / 1_000f)
        else -> value.toString()
    }
}
