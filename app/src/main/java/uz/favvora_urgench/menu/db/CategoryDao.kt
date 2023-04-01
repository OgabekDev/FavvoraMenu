package uz.favvora_urgench.menu.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.favvora_urgench.menu.model.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCategory(category: Category)

    @Query("SELECT * FROM category WHERE parent is null ORDER BY position ASC")
    suspend fun getParentCategories(): List<Category>

    @Query("SELECT * FROM category WHERE parent = :id ORDER BY position ASC")
    suspend fun getSubCategories(id: Int): List<Category>

    @Query("DELETE FROM category")
    suspend fun deleteCategories()


}