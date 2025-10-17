package pe.edu.idat.dsi.dami.idatgram.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import pe.edu.idat.dsi.dami.idatgram.R
import pe.edu.idat.dsi.dami.idatgram.data.entity.*
import pe.edu.idat.dsi.dami.idatgram.ui.components.*
import pe.edu.idat.dsi.dami.idatgram.ui.theme.*

/**
 * Pantalla principal del feed de Instagram
 * 
 * Implementa:
 * - Barra superior con logo y botones
 * - Sección de historias horizontales
 * - Feed vertical de posts
 * - Pull to refresh
 * - Interacciones con posts (like, comentar, guardar)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    posts: List<PostWithUser> = emptyList(),
    stories: List<StoryWithUser> = emptyList(),
    onPostLike: (String) -> Unit = {},
    onPostComment: (String) -> Unit = {},
    onPostSave: (String) -> Unit = {},
    onPostShare: (String) -> Unit = {},
    onUserProfileClick: (String) -> Unit = {},
    onStoryClick: (String) -> Unit = {},
    onCameraClick: () -> Unit = {},
    onDirectMessagesClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        
        // === Barra Superior ===
        TopAppBar(
            title = {
                Text(
                    text = "Idatgram",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            navigationIcon = {
                IconButton(onClick = onCameraClick) {
                    Icon(
                        imageVector = Icons.Default.Camera,
                        contentDescription = stringResource(R.string.camera_button),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            actions = {
                IconButton(onClick = onDirectMessagesClick) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = stringResource(R.string.direct_messages),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        // === Contenido Principal ===
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            
            // === Sección de Historias ===
            item {
                StoriesSection(
                    stories = stories,
                    onStoryClick = onStoryClick,
                    onAddStoryClick = { /* TODO: Implementar agregar historia */ },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            // === Divisor ===
            item {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                    thickness = 0.5.dp
                )
            }
            
            // === Feed de Posts ===
            items(
                items = posts,
                key = { post -> post.post.id }
            ) { postWithUser ->
                PostCard(
                    postWithUser = postWithUser,
                    onLikeClick = { onPostLike(postWithUser.post.id) },
                    onCommentClick = { onPostComment(postWithUser.post.id) },
                    onSaveClick = { onPostSave(postWithUser.post.id) },
                    onShareClick = { onPostShare(postWithUser.post.id) },
                    onUserClick = { onUserProfileClick(postWithUser.user.id) },
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
            
            // === Estado vacío ===
            if (posts.isEmpty()) {
                item {
                    EmptyFeedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp)
                    )
                }
            }
        }
    }
}

/**
 * Sección horizontal de historias
 */
@Composable
private fun StoriesSection(
    stories: List<StoryWithUser>,
    onStoryClick: (String) -> Unit,
    onAddStoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        
        // Tu historia (botón para agregar)
        item {
            YourStoryCard(
                onClick = onAddStoryClick
            )
        }
        
        // Historias de otros usuarios
        items(
            items = stories,
            key = { story -> story.story.id }
        ) { storyWithUser ->
            StoryCard(
                storyWithUser = storyWithUser,
                onClick = { onStoryClick(storyWithUser.story.id) }
            )
        }
    }
}

/**
 * Card para tu propia historia
 */
@Composable
private fun YourStoryCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            InstagramBlue,
                            InstagramPurple,
                            InstagramPink
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onClick,
                modifier = Modifier.size(68.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_story),
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        Text(
            text = stringResource(R.string.your_story),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

/**
 * Card individual de historia
 */
@Composable
private fun StoryCard(
    storyWithUser: StoryWithUser,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(
                    brush = if (!storyWithUser.isViewed) {
                        Brush.linearGradient(
                            colors = listOf(
                                InstagramBlue,
                                InstagramPurple,
                                InstagramPink
                            )
                        )
                    } else {
                        Brush.linearGradient(
                            colors = listOf(
                                Color.Gray.copy(alpha = 0.5f),
                                Color.Gray.copy(alpha = 0.3f)
                            )
                        )
                    }
                )
                .padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(storyWithUser.user.profileImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.user_story, storyWithUser.user.username),
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        
        Text(
            text = storyWithUser.user.username,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp),
            maxLines = 1
        )
    }
}

/**
 * Card que se muestra cuando el feed está vacío
 */
@Composable
private fun EmptyFeedCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Camera,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = stringResource(R.string.empty_feed_title),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = stringResource(R.string.empty_feed_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    IdatgramTheme {
        HomeScreen(
            posts = samplePosts(),
            stories = sampleStories()
        )
    }
}

// Datos de ejemplo para preview
private fun samplePosts(): List<PostWithUser> {
    val sampleUser = User(
        id = "1",
        username = "johndoe",
        email = "john@example.com",
        displayName = "John Doe",
        bio = "Fotógrafo profesional",
        profileImageUrl = "https://via.placeholder.com/150",
        isVerified = true
    )
    
    val samplePost = Post(
        id = "1",
        userId = "1",
        caption = "Una hermosa puesta de sol 🌅",
        imageUrl = "https://via.placeholder.com/400",
        likesCount = 127,
        commentsCount = 23,
        location = "Lima, Perú"
    )
    
    return listOf(
        PostWithUser(
            post = samplePost,
            user = sampleUser,
            isLiked = false,
            isSaved = false
        )
    )
}

private fun sampleStories(): List<StoryWithUser> {
    val sampleUser = User(
        id = "1",
        username = "johndoe",
        email = "john@example.com",
        displayName = "John Doe",
        profileImageUrl = "https://via.placeholder.com/150"
    )
    
    val sampleStory = Story(
        id = "1",
        userId = "1",
        imageUrl = "https://via.placeholder.com/150"
    )
    
    return listOf(
        StoryWithUser(
            story = sampleStory,
            user = sampleUser,
            isViewed = false
        )
    )
}