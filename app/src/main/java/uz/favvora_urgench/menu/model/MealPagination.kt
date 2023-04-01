package uz.favvora_urgench.menu.model

data class MealPagination (
    var next: String?,
    var previous: String?,
    var count: Int,
    var current_page: Int,
    var results: ArrayList<Meal>
)