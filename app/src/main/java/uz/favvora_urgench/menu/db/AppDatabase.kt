package uz.favvora_urgench.menu.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.favvora_urgench.menu.model.Category
import uz.favvora_urgench.menu.model.Meal
import uz.favvora_urgench.menu.model.Section

@Database(entities = [Category::class, Meal::class, Section::class], version = 4, exportSchema = true)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getCategoryDao(): CategoryDao
    abstract fun getMealsDao(): MealsDao
    abstract fun getSectionDao(): SectionDao

    companion object {

        private var DB_INSTANCE: AppDatabase? = null

        fun getAppDBInstance(context: Context): AppDatabase {
            if (DB_INSTANCE == null) {
                DB_INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return DB_INSTANCE!!
        }
    }

}