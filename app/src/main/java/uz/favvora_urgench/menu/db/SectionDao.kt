package uz.favvora_urgench.menu.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.favvora_urgench.menu.model.Section

@Dao
interface SectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSection(section: Section)

    @Query("DELETE FROM section")
    suspend fun deleteSections()

    @Query("SELECT * FROM section WHERE id = :id ORDER BY position")
    suspend fun getSection(id: Int): Section

}