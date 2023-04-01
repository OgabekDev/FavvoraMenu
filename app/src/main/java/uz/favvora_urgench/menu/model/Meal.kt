package uz.favvora_urgench.menu.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey val id: Int,
    val name: String,
    val small_image: String,
    val bigg_image: String,
    val main_category_name: String,
    val description: String,
    val price: Int
)