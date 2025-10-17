package pe.edu.idat.dsi.dami.idatgram.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context
import pe.edu.idat.dsi.dami.idatgram.data.dao.CommentDao
import pe.edu.idat.dsi.dami.idatgram.data.dao.PostDao
import pe.edu.idat.dsi.dami.idatgram.data.dao.StoryDao
import pe.edu.idat.dsi.dami.idatgram.data.dao.UserDao
import pe.edu.idat.dsi.dami.idatgram.data.entity.*

/**
 * Base de datos principal de la aplicación Instagram Clone
 * 
 * Incluye todas las entidades y DAOs necesarios para:
 * - Gestión de usuarios y seguimientos
 * - Posts con likes y guardados
 * - Comentarios con respuestas
 * - Stories con visualizaciones
 * 
 * Configurada con migraciones automáticas y datos de ejemplo para desarrollo
 */
@Database(
    entities = [
        User::class,
        UserFollow::class,
        Post::class,
        PostLike::class,
        SavedPost::class,
        Comment::class,
        CommentLike::class,
        Story::class,
        StoryView::class
    ],
    version = 1,
    exportSchema = false
)
abstract class IdatgramDatabase : RoomDatabase() {
    
    // DAOs abstractos
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    abstract fun storyDao(): StoryDao
    
    companion object {
        @Volatile
        private var INSTANCE: IdatgramDatabase? = null
        
        fun getDatabase(context: Context): IdatgramDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IdatgramDatabase::class.java,
                    "idatgram_database"
                )
                    .addCallback(DatabaseCallback())
                    .addMigrations(*getAllMigrations())
                    .build()
                INSTANCE = instance
                instance
            }
        }
        
        /**
         * Callback para inicializar la base de datos con datos de ejemplo
         */
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Los datos de ejemplo se insertarán via Repository en la primera ejecución
            }
        }
        
        /**
         * Migraciones de la base de datos para futuras versiones
         */
        private fun getAllMigrations(): Array<Migration> {
            return arrayOf(
                // Ejemplo de migración para versión futura
                // MIGRATION_1_2,
                // MIGRATION_2_3,
            )
        }
        
        // Ejemplo de migración para versiones futuras
        /*
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Agregar nueva columna
                database.execSQL("ALTER TABLE users ADD COLUMN phone TEXT DEFAULT ''")
            }
        }
        */
    }
}

/**
 * Clase auxiliar para datos de ejemplo y semillas
 */
object DatabaseSeeder {
    
    /**
     * Crea usuarios de ejemplo para desarrollo y testing
     */
    suspend fun seedUsers(userDao: UserDao) {
        val sampleUsers = listOf(
            User(
                id = "user_1",
                username = "john_doe",
                email = "john@example.com",
                displayName = "John Doe",
                bio = "Photographer & Travel enthusiast 📸✈️",
                profileImageUrl = "https://picsum.photos/200/200?random=1",
                followersCount = 1250,
                followingCount = 320,
                postsCount = 45,
                isVerified = true
            ),
            User(
                id = "user_2",
                username = "jane_smith",
                email = "jane@example.com",
                displayName = "Jane Smith",
                bio = "Digital Artist | Coffee lover ☕🎨",
                profileImageUrl = "https://picsum.photos/200/200?random=2",
                followersCount = 890,
                followingCount = 150,
                postsCount = 67
            ),
            User(
                id = "user_3",
                username = "mike_wilson",
                email = "mike@example.com",
                displayName = "Mike Wilson",
                bio = "Fitness coach & lifestyle blogger 💪",
                profileImageUrl = "https://picsum.photos/200/200?random=3",
                followersCount = 2100,
                followingCount = 89,
                postsCount = 134,
                isVerified = true
            ),
            User(
                id = "current_user",
                username = "me",
                email = "me@example.com",
                displayName = "Mi Perfil",
                bio = "¡Hola! Soy nuevo en Instagram 👋",
                profileImageUrl = "https://picsum.photos/200/200?random=999",
                followersCount = 5,
                followingCount = 12,
                postsCount = 3
            )
        )
        
        userDao.insertUsers(sampleUsers)
        
        // Crear algunas relaciones de seguimiento
        val follows = listOf(
            UserFollow("current_user", "user_1"),
            UserFollow("current_user", "user_2"),
            UserFollow("current_user", "user_3"),
            UserFollow("user_1", "current_user"),
            UserFollow("user_2", "user_1"),
            UserFollow("user_3", "user_2")
        )
        
        follows.forEach { userDao.followUser(it) }
    }
    
    /**
     * Crea posts de ejemplo para desarrollo
     */
    suspend fun seedPosts(postDao: PostDao) {
        val samplePosts = listOf(
            Post(
                id = "post_1",
                userId = "user_1",
                caption = "Beautiful sunset at the beach 🌅 #photography #sunset #beach",
                imageUrl = "https://picsum.photos/400/400?random=101",
                likesCount = 245,
                commentsCount = 12,
                location = "Malibu Beach, CA"
            ),
            Post(
                id = "post_2",
                userId = "user_2",
                caption = "My latest digital artwork! What do you think? 🎨✨",
                imageUrl = "https://picsum.photos/400/400?random=102",
                likesCount = 189,
                commentsCount = 8
            ),
            Post(
                id = "post_3",
                userId = "user_3",
                caption = "Morning workout complete! 💪 Remember, consistency is key #fitness #motivation",
                imageUrl = "https://picsum.photos/400/400?random=103",
                likesCount = 312,
                commentsCount = 25,
                location = "Gold's Gym"
            ),
            Post(
                id = "post_4",
                userId = "current_user",
                caption = "First post! Excited to be here 🎉",
                imageUrl = "https://picsum.photos/400/400?random=104",
                likesCount = 8,
                commentsCount = 3
            )
        )
        
        postDao.insertPosts(samplePosts)
        
        // Algunos likes de ejemplo
        val likes = listOf(
            PostLike("post_1", "current_user"),
            PostLike("post_2", "current_user"),
            PostLike("post_4", "user_1"),
            PostLike("post_4", "user_2")
        )
        
        likes.forEach { postDao.likePost(it) }
    }
    
    /**
     * Crea stories de ejemplo para desarrollo
     */
    suspend fun seedStories(storyDao: StoryDao) {
        val currentTime = System.currentTimeMillis()
        val sampleStories = listOf(
            Story(
                id = "story_1",
                userId = "user_1",
                imageUrl = "https://picsum.photos/300/500?random=201",
                text = "Good morning! ☀️",
                viewsCount = 89,
                expiresAt = currentTime + (20 * 60 * 60 * 1000) // Expira en 20 horas
            ),
            Story(
                id = "story_2",
                userId = "user_2",
                imageUrl = "https://picsum.photos/300/500?random=202",
                text = "Working on something new...",
                backgroundColor = "#FF6B6B",
                viewsCount = 45,
                expiresAt = currentTime + (18 * 60 * 60 * 1000)
            ),
            Story(
                id = "story_3",
                userId = "user_3",
                imageUrl = "https://picsum.photos/300/500?random=203",
                text = "Leg day! 🔥",
                viewsCount = 167,
                expiresAt = currentTime + (22 * 60 * 60 * 1000)
            )
        )
        
        storyDao.insertStories(sampleStories)
    }
    
    /**
     * Inicializa toda la base de datos con datos de ejemplo
     */
    suspend fun seedDatabase(database: IdatgramDatabase) {
        seedUsers(database.userDao())
        seedPosts(database.postDao())
        seedStories(database.storyDao())
    }
}