package uz.favvora_urgench.menu.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "section")
data class Section(
    @PrimaryKey val id: Int,
    val position: Int,
    val main_meal: String?, // Gson For Meal
    val first_meal: String?, // Gson For Meal
    val second_meal: String?, // Gson For Meal
    val third_meal: String?, // Gson For Meal
    val forth_meal: String? // Gson For Meal
)

data class SectionData(
    val id: Int,
    val position: Int,
    val main_meal: Meal?,
    val first_meal: Meal?,
    val second_meal: Meal?,
    val third_meal: Meal?,
    val forth_meal: Meal?
)