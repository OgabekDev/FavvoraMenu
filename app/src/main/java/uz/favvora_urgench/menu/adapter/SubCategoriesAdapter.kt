package uz.favvora_urgench.menu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.favvora_urgench.menu.databinding.ItemCategoryBinding
import uz.favvora_urgench.menu.model.CategoryData

class SubCategoriesAdapter(
    private val categories: ArrayList<CategoryData>
): RecyclerView.Adapter<SubCategoriesAdapter.ViewHolder>() {

    var onClick: ((ArrayList<Int>) -> Unit)? = null

    private var checkedPosition = 0

    inner class ViewHolder(private val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryData) {
            binding.apply {
                if (category.isSelected) {
                    vIsSelected.visibility = View.VISIBLE
                } else {
                    vIsSelected.visibility = View.GONE
                }

                tvSingleCategory.text = category.title

                root.setOnClickListener {
                    categories[checkedPosition].isSelected = false
                    notifyItemChanged(checkedPosition)
                    checkedPosition = adapterPosition
                    categories[checkedPosition].isSelected = true
                    notifyItemChanged(checkedPosition)
                    onClick?.invoke(category.sections)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position])
    }

}