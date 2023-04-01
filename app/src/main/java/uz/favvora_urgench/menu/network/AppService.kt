package uz.favvora_urgench.menu.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uz.favvora_urgench.menu.model.CategoryData
import uz.favvora_urgench.menu.model.Meal
import uz.favvora_urgench.menu.model.MealPagination
import uz.favvora_urgench.menu.model.SectionData

interface AppService {

    @GET("/tablet/menu/categories/")
    suspend fun getCategories(): Response<ArrayList<CategoryData>>

    @GET("/tablet/menu/sections/")
    suspend fun getSections(): Response<ArrayList<SectionData>>

    @GET("/tablet/menu/meals/")
    suspend fun getMeals(@Query("page") page: Int): Response<MealPagination>

}