package uz.favvora_urgench.menu.viewmodel

import uz.favvora_urgench.menu.db.CategoryDao
import uz.favvora_urgench.menu.db.MealsDao
import uz.favvora_urgench.menu.db.SectionDao
import uz.favvora_urgench.menu.model.Category
import uz.favvora_urgench.menu.model.Meal
import uz.favvora_urgench.menu.model.Section
import uz.favvora_urgench.menu.network.AppService
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val appService: AppService,
    private val categoryDao: CategoryDao,
    private val sectionDao: SectionDao,
    private val mealsDao: MealsDao
) {

    suspend fun getCategoriesFromServer() = appService.getCategories()
    suspend fun getSectionsFromServer() = appService.getSections()
    suspend fun getMealsFromServer(page: Int) = appService.getMeals(page)

    suspend fun deleteCategories() = categoryDao.deleteCategories()
    suspend fun saveCategory(category: Category) = categoryDao.addCategory(category)
    suspend fun getParentCategories() = categoryDao.getParentCategories()
    suspend fun getSubCategories(id: Int) = categoryDao.getSubCategories(id)

    suspend fun deleteSections() = sectionDao.deleteSections()
    suspend fun saveSection(section: Section) = sectionDao.addSection(section)
    suspend fun getSection(id: Int) = sectionDao.getSection(id)

    suspend fun deleteMeals() = mealsDao.deleteMeals()
    suspend fun saveMeal(meal: Meal) = mealsDao.addMeal(meal)
    suspend fun getMeal(id: Int) = mealsDao.getMeal(id)

    suspend fun searchMeal() = mealsDao.searchMeals()

}