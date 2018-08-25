package s18alg.myapplication.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

@Database(entities = [TargetWebsite::class], version = 1, exportSchema = false)
@TypeConverters(ProfileConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun targetDao(): TargetDAO
}