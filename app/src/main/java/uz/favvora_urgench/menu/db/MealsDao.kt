package uz.favvora_urgench.menu.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.favvora_urgench.menu.model.Meal

@Dao
interface MealsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMeal(meal: Meal)

    @Query("DELETE FROM meals")
    suspend fun deleteMeals()

    @Query("SELECT * FROM meals WHERE id = :id")
    suspend fun getMeal(id: Int): Meal

    @Query("SELECT * FROM meals")
    suspend fun searchMeals(): List<Meal>

}