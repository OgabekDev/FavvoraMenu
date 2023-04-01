package uz.favvora_urgench.menu.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.favvora_urgench.menu.model.*
import uz.favvora_urgench.menu.utils.UiStateList
import uz.favvora_urgench.menu.utils.UiStateObject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    private val _categoriesFromServer = MutableStateFlow<UiStateObject<Unit>>(UiStateObject.EMPTY)
    val categoriesFromServer = _categoriesFromServer

    fun getCategoriesFromServer() = viewModelScope.launch {
        _categoriesFromServer.value = UiStateObject.LOADING
        try {
            val response = repository.getCategoriesFromServer()
            if (response.isSuccessful) {
                saveCategories(response.body()!!)
            } else {
                _categoriesFromServer.value = UiStateObject.ERROR(response.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _categoriesFromServer.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    private val _sectionsFromServer = MutableStateFlow<UiStateObject<Unit>>(UiStateObject.EMPTY)
    val sectionsFromServer = _sectionsFromServer

    fun getSectionsFromServer(context: Context) = viewModelScope.launch {
        _sectionsFromServer.value = UiStateObject.LOADING
        try {
            val response = repository.getSectionsFromServer()
            if (response.isSuccessful) {
                saveSections(context, response.body()!!)
            } else {
                _sectionsFromServer.value = UiStateObject.ERROR(response.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _sectionsFromServer.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    private val _mealsFromServer = MutableStateFlow<UiStateObject<Unit>>(UiStateObject.EMPTY)
    val getMealsFromServer = _mealsFromServer

    private var page: Int = 1
    fun getMealsFromServer(context: Context) = viewModelScope.launch {
        _mealsFromServer.value = UiStateObject.LOADING
        try {
            while (true) {
                val response = repository.getMealsFromServer(page)
                if (response.isSuccessful) {
                    saveMeals(context, response.body()!!.results, page == 1)
                    if (response.body()!!.next == null) break else page++
                } else {
                    _mealsFromServer.value = UiStateObject.ERROR(response.message())
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _mealsFromServer.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }

    private fun saveCategories(categories: ArrayList<CategoryData>) = viewModelScope.launch {
        repository.deleteCategories()
        try {
            for (i in categories) {
                val category = Category(i.id, i.title, i.parent, i.position, Gson().toJson(i.sections))
                repository.saveCategory(category)
            }
            _categoriesFromServer.value = UiStateObject.SUCCESS(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            _categoriesFromServer.value = UiStateObject.ERROR(e.localizedMessage ?: "Category > Writing to Database")
        }
    }

    private fun saveSections(context: Context, sections: ArrayList<SectionData>) = viewModelScope.launch {
        repository.deleteSections()
        try {
            for (i in sections) {
                Glide.with(context).load(i.main_meal?.bigg_image).preload()
                Glide.with(context).load(i.main_meal?.small_image).preload()
                Glide.with(context).load(i.first_meal?.bigg_image).preload()
                Glide.with(context).load(i.first_meal?.small_image).preload()
                Glide.with(context).load(i.second_meal?.bigg_image).preload()
                Glide.with(context).load(i.second_meal?.small_image).preload()
                Glide.with(context).load(i.third_meal?.bigg_image).preload()
                Glide.with(context).load(i.third_meal?.small_image).preload()
                Glide.with(context).load(i.forth_meal?.bigg_image).preload()
                Glide.with(context).load(i.forth_meal?.small_image).preload()
                val section = Section(i.id, i.position, Gson().toJson(i.main_meal), Gson().toJson(i.first_meal), Gson().toJson(i.second_meal), Gson().toJson(i.third_meal), Gson().toJson(i.forth_meal))
                repository.saveSection(section)
            }
            _sectionsFromServer.value = UiStateObject.SUCCESS(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            _sectionsFromServer.value = UiStateObject.ERROR(e.localizedMessage ?: "Sections > Writing to Database")
        }
    }

    private fun saveMeals(context: Context, meals: ArrayList<Meal>, clear: Boolean) = viewModelScope.launch {
        if (clear) repository.deleteMeals()
        try {
            for (i in meals) {
                Glide.with(context).load(i.bigg_image).preload()
                Glide.with(context).load(i.small_image).preload()
                repository.saveMeal(i)
            }
            _mealsFromServer.value = UiStateObject.SUCCESS(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            _mealsFromServer.value = UiStateObject.ERROR(e.localizedMessage ?: "Meal > Writing to Database")
        }
    }

    private val _categories = MutableStateFlow<UiStateList<ParentCategory>>(UiStateList.EMPTY)
    val categories = _categories

    fun getCategories() = viewModelScope.launch {
        _categories.value = UiStateList.LOADING
        try {
            val parent = ArrayList<ParentCategory>()
            val parentCategories = repository.getParentCategories()
            for (i in parentCategories) {
                val subCategories = repository.getSubCategories(i.id)
                val subCategory: ArrayList<CategoryData> = ArrayList<CategoryData>().apply {
                    for (j in subCategories) {
                        val sections: ArrayList<Int> = Gson().fromJson(j.sections, ArrayList::class.java) as ArrayList<Int>
                        add(CategoryData(j.id, j.title, j.parent, j.position, sections))
                    }
                }
                val section: ArrayList<Int> = Gson().fromJson(i.sections, ArrayList::class.java) as ArrayList<Int>
                val parentCategory = ParentCategory(i.id, i.title, i.position, subCategory, false, section)
                parent.add(parentCategory)
            }
            _categories.value = UiStateList.SUCCESS(parent)
        } catch (e: Exception) {
            e.printStackTrace()
            _categories.value = UiStateList.ERROR(e.localizedMessage ?: "Category > Getting from Database")
        }
    }

    private val _sections = MutableStateFlow<UiStateList<SectionData>>(UiStateList.EMPTY)
    val sections = _sections

    fun getSections(sectionIDs: ArrayList<Int>) = viewModelScope.launch {
        _sections.value = UiStateList.LOADING
        try {
            val sectionsData = ArrayList<SectionData>()
            for (i: Int in sectionIDs) {
                val response = repository.getSection(i)

                var mainMeal: Meal? = null
                var firstMeal: Meal? = null

                var secondMeal: Meal?
                var thirdMeal: Meal? = null
                var forthMeal: Meal? = null

                if (response.first_meal == null && response.main_meal != null) {
                    mainMeal = Gson().fromJson(response.main_meal, Meal::class.java)
                } else if (response.first_meal != null && response.main_meal != null)  {
                    firstMeal = Gson().fromJson(response.first_meal, Meal::class.java)
                    mainMeal = Gson().fromJson(response.main_meal, Meal::class.java)
                } else {
                    firstMeal = Gson().fromJson(response.first_meal, Meal::class.java)
                }

                if (response.second_meal == null) {
                    continue
                } else if (response.third_meal == null) {
                    secondMeal = Gson().fromJson(response.second_meal, Meal::class.java)
                } else if (response.forth_meal == null) {
                    secondMeal = Gson().fromJson(response.second_meal, Meal::class.java)
                    thirdMeal = Gson().fromJson(response.third_meal, Meal::class.java)
                } else {
                    secondMeal = Gson().fromJson(response.second_meal, Meal::class.java)
                    thirdMeal = Gson().fromJson(response.third_meal, Meal::class.java)
                    forthMeal = Gson().fromJson(response.forth_meal, Meal::class.java)
                }

                val section = SectionData(response.id, response.position, mainMeal, firstMeal, secondMeal, thirdMeal, forthMeal)
                sectionsData.add(section)
            }
            _sections.value = UiStateList.SUCCESS(sectionsData)
        } catch (e: Exception) {
            e.printStackTrace()
            _sections.value = UiStateList.ERROR(e.localizedMessage ?: "SECTION > Getting from Database")
        }
    }

    private val _meal = MutableStateFlow<UiStateObject<Meal>>(UiStateObject.EMPTY)
    val meal = _meal

    fun getMeal(id: Int) = viewModelScope.launch {
        _meal.value = UiStateObject.LOADING
        try {
            val response = repository.getMeal(id)
            _meal.value = UiStateObject.SUCCESS(response)
        } catch (e: Exception) {
            e.printStackTrace()
            _meal.value = UiStateObject.ERROR(e.localizedMessage ?: "SECTION > Getting from Database")
        }
    }

    private val _search = MutableStateFlow<UiStateList<Meal>>(UiStateList.EMPTY)
    val search = _search

    fun getMeals() = viewModelScope.launch {
        _search.value = UiStateList.LOADING
        try {
            val response = repository.searchMeal()
            _search.value = UiStateList.SUCCESS(response)
        } catch (e: Exception) {
            _search.value = UiStateList.ERROR(e.localizedMessage ?: "")
        }
    }

}