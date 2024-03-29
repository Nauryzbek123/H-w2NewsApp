package dev.androidbroadcast.news.database

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteOpenHelper
import dev.androidbroadcast.news.database.dao.ArticleDao
import dev.androidbroadcast.news.database.models.ArticleDBO
import dev.androidbroadcast.news.database.utils.Converters

@Database(entities = [ArticleDBO::class], version = 1)
@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {
  abstract fun articleDao(): ArticleDao

}
fun NewsDatabase(applicationContext: Context): NewsDatabase {
    return Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        NewsDatabase::class.java,
        "news"
    ).build()
}