package uz.favvora_urgench.menu.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category (
    @PrimaryKey val id: Int,
    val title: String,
    val parent: Int?,
    val position: Int,
    val sections: String // Gson For Sections Id List
)

data class CategoryData (
    @PrimaryKey val id: Int,
    val title: String,
    val parent: Int?,
    val position: Int,
    val sections: ArrayList<Int>,
    var isSelected: Boolean = false
)

data class ParentCategory(
    val id: Int,
    val title: String,
    val position: Int,
    var subCategories: ArrayList<CategoryData>? = null,
    var isSelected: Boolean = false,
    val sections: ArrayList<Int>
)