package uz.favvora_urgench.menu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.favvora_urgench.menu.R
import uz.favvora_urgench.menu.databinding.ItemCategoryBinding
import uz.favvora_urgench.menu.model.CategoryData
import uz.favvora_urgench.menu.model.ParentCategory

class CategoriesAdapter(
    private val categories: ArrayList<ParentCategory>
): RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    var onClick: ((ArrayList<CategoryData>?) -> Unit)? = null
    var onCategoryClick: ((ArrayList<Int>) -> Unit)? = null

    private var checkedPosition = 0

    inner class ViewHolder(private val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(category: ParentCategory) {
            binding.apply {
                if (category.isSelected) {
                    vIsSelected.visibility = View.VISIBLE
                    ivMultiCategory.setImageResource(R.drawable.ic_up)
                } else {
                    vIsSelected.visibility = View.GONE
                    ivMultiCategory.setImageResource(R.drawable.ic_down)
                }

                tvSingleCategory.text = category.title

                if (category.subCategories.isNullOrEmpty()) {
                    ivMultiCategory.visibility = View.GONE
                } else {
                    ivMultiCategory.visibility = View.VISIBLE
                }

                root.setOnClickListener {
                    if (category.subCategories!!.isEmpty()) {
                        categories[checkedPosition].isSelected = false
                        notifyItemChanged(checkedPosition)
                        checkedPosition = adapterPosition
                        categories[checkedPosition].isSelected = true
                        notifyItemChanged(checkedPosition)
                        onCategoryClick?.invoke(category.sections)
                    } else {
                        if (category.isSelected) {
                            categories[adapterPosition].isSelected = false
                            notifyItemChanged(checkedPosition)
                            onClick?.invoke(null)
                        } else {
                            categories[checkedPosition].isSelected = false
                            notifyItemChanged(checkedPosition)
                            checkedPosition = adapterPosition
                            categories[checkedPosition].isSelected = true
                            notifyItemChanged(checkedPosition)
                            onClick?.invoke(category.subCategories)
                        }
                    }
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