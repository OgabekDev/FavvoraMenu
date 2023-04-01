package uz.favvora_urgench.menu.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import uz.favvora_urgench.menu.R
import uz.favvora_urgench.menu.adapter.CategoriesAdapter
import uz.favvora_urgench.menu.adapter.MenuAdapter
import uz.favvora_urgench.menu.adapter.SearchAdapter
import uz.favvora_urgench.menu.adapter.SubCategoriesAdapter
import uz.favvora_urgench.menu.databinding.ActivityMainBinding
import uz.favvora_urgench.menu.databinding.DialogDetailsBinding
import uz.favvora_urgench.menu.model.Meal
import uz.favvora_urgench.menu.model.ParentCategory
import uz.favvora_urgench.menu.model.SectionData
import uz.favvora_urgench.menu.utils.*
import uz.favvora_urgench.menu.viewmodel.MainViewModel

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private var categoriesDownloaded: Boolean = false
    private var sectionsDownloaded: Boolean = false
    private var mealsDownloaded: Boolean = false
    private var search: Boolean = true

    private lateinit var meals: ArrayList<Meal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }

    private fun initViews() {

        mealsObserver()

        viewModel.getCategories()
        setUpDatabaseObservers()

        binding.ivSync.setOnClickListener {
            if (isOnline(this)) {
                showWarningDialog(getString(R.string.str_title), getString(R.string.str_message), "скачать", {
                    viewModel.getCategoriesFromServer()
                    viewModel.getSectionsFromServer(this)
                    viewModel.getMealsFromServer(this)
                    val adapter = CategoriesAdapter(arrayListOf())
                    binding.rvCategories.adapter = adapter
                    val subAdapter = SubCategoriesAdapter(arrayListOf())
                    binding.rvSubCategories.adapter = subAdapter
                    binding.rvSubCategories.visibility = View.GONE
                    setUpServerObservers()
                }, "отменить", {})
            } else {
                toast(binding.root, getString(R.string.str_no_connection))
            }
        }

        viewModel.getMeals()



    }

    private fun setUpDatabaseObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.categories.collect {
                when (it) {
                    is UiStateList.LOADING -> {
                        showLoading()
                    }
                    is UiStateList.SUCCESS -> {
                        dismissLoading()
                        setUpCategories(it.data)
                    }
                    is UiStateList.ERROR -> {
                        toast(binding.root, "ERROR FROM DATABASE: ${it.message}")
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.sections.collect {
                when (it) {
                    is UiStateList.LOADING -> {
                        showLoading()
                    }
                    is UiStateList.SUCCESS -> {
                        dismissLoading()
                        setUpSections(it.data)
                    }
                    is UiStateList.ERROR -> {
                        dismissLoading()
                        toast(binding.root, "ERROR FROM DATABASE: ${it.message}")
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.search.collect {
                when (it) {
                    is UiStateList.LOADING -> {
                        showLoading()
                    }
                    is UiStateList.SUCCESS -> {
                        dismissLoading()
                        meals = it.data as ArrayList

                        binding.etSearch.addTextChangedListener(object: TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {

                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                                val search = ArrayList<Meal>()
                                search.clear()
                                for (i in meals) {
                                    if (i.name.lowercase().contains(s.toString().lowercase())) {
                                        search.add(i)
                                    }
                                }
                                if (search.isEmpty()) {
                                    search.add(Meal(0, "Hech narsa topilmadi", "", "", "", "", 0))
                                }
                                search(search)
                            }

                            override fun afterTextChanged(s: Editable?) {
                            }

                        })

                    }
                    is UiStateList.ERROR -> {
                        dismissLoading()
                        toast(binding.root, "ERROR FROM DATABASE: ${it.message}")
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun search(meals: ArrayList<Meal>) {
        this.search = false
        val adapter = SearchAdapter(this, meals)
        adapter.onClick = { id ->
            if (id != null) {
                binding.etSearch.setText("")
                viewModel.getMeal(id)
            }
        }
        binding.etSearch.setAdapter(adapter)
    }

    private fun mealsObserver() {
        lifecycleScope.launchWhenCreated {
            viewModel.meal.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        dismissLoading()
                        showDetails(it.data)
                    }
                    is UiStateObject.ERROR -> {
                        dismissLoading()
                        toast(binding.root, "ERROR FROM DATABASE ${it.message}")
                    }
                    else -> Unit
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDetails(data: Meal) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        val dialogBinding = DialogDetailsBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.apply {
            setImageHeight(ivImage)
            Glide.with(ivImage).load(data.bigg_image).into(ivImage)
            tvName.text = data.name
            tvParentCategoryName.text = data.main_category_name
            tvDescription.text = getString(R.string.source) + data.description
            tvPrice.text = data.price.toString().setAsPrice() + getString(R.string.UZS)
        }

        dialog.show()

    }

    private fun setUpSections(data: List<SectionData>) {
        val adapter = MenuAdapter(data as ArrayList<SectionData>)

        adapter.onClick = {
            viewModel.getMeal(it)
        }

        binding.rvMeals.adapter = adapter
    }

    private fun setUpCategories(data: List<ParentCategory>) {
        val adapter = CategoriesAdapter(data as ArrayList<ParentCategory>)

        adapter.onClick = { it ->
            if (it.isNullOrEmpty()) {
                binding.rvSubCategories.visibility = View.GONE
            } else {
                binding.rvSubCategories.visibility = View.VISIBLE
                for (i in it) {
                    i.isSelected = false
                }

                it[0].isSelected = true

                viewModel.getSections(it[0].sections)

                val subAdapter = SubCategoriesAdapter(it)

                subAdapter.onClick = { sections ->
                    viewModel.getSections(sections)
                }

                binding.rvSubCategories.adapter = subAdapter
            }
        }

        adapter.onCategoryClick = {
            binding.rvSubCategories.visibility = View.GONE
            viewModel.getSections(it)
        }

        binding.rvCategories.adapter = adapter

        for (i in data) {
            if (i.id == 1) {
                viewModel.getSections(i.sections)
                break
            }
        }

    }

    private fun setUpServerObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.categoriesFromServer.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        categoriesDownloaded = true
                        isAllDone()
                    }
                    is UiStateObject.ERROR -> {
                        toast(binding.root, "ERROR FROM SERVER: ${it.message}")
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.sectionsFromServer.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        sectionsDownloaded = true
                        isAllDone()
                    }
                    is UiStateObject.ERROR -> {
                        toast(binding.root, "ERROR FROM SERVER: ${it.message}")
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.getMealsFromServer.collect {
                when (it) {
                    is UiStateObject.LOADING -> {
                        showLoading()
                    }
                    is UiStateObject.SUCCESS -> {
                        mealsDownloaded = true
                        isAllDone()
                    }
                    is UiStateObject.ERROR -> {
                        toast(binding.root, "ERROR FROM SERVER: ${it.message}")
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun isAllDone() {
        if (categoriesDownloaded && sectionsDownloaded && mealsDownloaded) {
            viewModel.getCategories()
            setUpDatabaseObservers()
        }
    }

    private fun showLoading() {
        binding.llLoading.visibility = View.VISIBLE
        binding.laLoading.playAnimation()
    }

    private fun dismissLoading() {
        binding.llLoading.visibility = View.GONE
    }


}