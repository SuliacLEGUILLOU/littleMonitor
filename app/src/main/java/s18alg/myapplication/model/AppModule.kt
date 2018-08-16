package s18alg.myapplication.model

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val context: Context) {
    @Provides fun providesAppContext() = context

    @Provides fun providesAppDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "monitor-db").allowMainThreadQueries().build()

    @Provides fun providesTargetDao(database: AppDatabase) = database.targetDao()
}