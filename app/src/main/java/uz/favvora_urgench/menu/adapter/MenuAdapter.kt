package uz.favvora_urgench.menu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.favvora_urgench.menu.databinding.ItemSectionBinding
import uz.favvora_urgench.menu.model.SectionData

class MenuAdapter(
    private val sections: ArrayList<SectionData>
): RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    var onClick: ((Int) -> Unit)? = null

    inner class ViewHolder(private val binding: ItemSectionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(section: SectionData) {
            binding.apply {

                ivSecondMeal.visibility = View.VISIBLE
                ivThirdMeal.visibility = View.VISIBLE
                ivForthMeal.visibility = View.VISIBLE

                if (section.main_meal != null && adapterPosition % 2 == 0) {
                    llLeft.visibility = View.VISIBLE
                    llRight.visibility = View.GONE
                    Glide.with(ivLeftFirstMeal).load(section.first_meal?.small_image).centerCrop().into(ivLeftFirstMeal)
                    Glide.with(ivLeftMainMeal).load(section.main_meal.bigg_image).centerCrop().into(ivLeftMainMeal)
                } else if (section.first_meal != null && adapterPosition % 2 == 1) {
                    llLeft.visibility = View.GONE
                    llRight.visibility = View.VISIBLE
                    binding.ivRightMainMeal.visibility = View.VISIBLE
                    Glide.with(ivRightFirstMeal).load(section.first_meal.small_image).centerCrop().into(ivRightFirstMeal)
                    Glide.with(ivRightMainMeal).load(section.main_meal?.bigg_image).centerCrop().into(ivRightMainMeal)
                } else if (section.first_meal == null && adapterPosition % 2 == 1) {
                    llLeft.visibility = View.GONE
                    llRight.visibility = View.VISIBLE
                    binding.ivRightMainMeal.visibility = View.INVISIBLE
                    Glide.with(ivRightFirstMeal).load(section.main_meal?.small_image).centerCrop().into(ivRightFirstMeal)
                }

                if (section.second_meal == null) {
                    ivSecondMeal.visibility = View.GONE
                    ivThirdMeal.visibility = View.GONE
                    ivForthMeal.visibility = View.GONE
                } else if (section.third_meal == null) {
                    ivSecondMeal.visibility = View.VISIBLE
                    ivThirdMeal.visibility = View.INVISIBLE
                    ivForthMeal.visibility = View.INVISIBLE
                } else if (section.forth_meal == null) {
                    ivSecondMeal.visibility = View.VISIBLE
                    ivThirdMeal.visibility = View.VISIBLE
                    ivForthMeal.visibility = View.INVISIBLE
                }

                Glide.with(ivSecondMeal).load(section.second_meal?.small_image).centerCrop().into(ivSecondMeal)
                Glide.with(ivThirdMeal).load(section.third_meal?.small_image).centerCrop().into(ivThirdMeal)
                Glide.with(ivForthMeal).load(section.forth_meal?.small_image).centerCrop().into(ivForthMeal)

                ivLeftMainMeal.setOnClickListener {
                    section.main_meal?.id?.let { it1 -> onClick?.invoke(it1) }
                }

                ivRightMainMeal.setOnClickListener {
                    section.main_meal?.id?.let { it1 -> onClick?.invoke(it1) }
                }

                ivLeftFirstMeal.setOnClickListener {
                    section.first_meal?.id?.let { it1 -> onClick?.invoke(it1) }
                }

                ivRightFirstMeal.setOnClickListener {
                    if (section.first_meal == null) {
                        onClick?.invoke(section.main_meal!!.id)
                    } else {
                        onClick?.invoke(section.first_meal.id)
                    }
                }

                ivSecondMeal.setOnClickListener {
                    section.second_meal?.id?.let { it1 -> onClick?.invoke(it1) }
                }

                ivThirdMeal.setOnClickListener {
                    section.third_meal?.id?.let { it1 -> onClick?.invoke(it1) }
                }

                ivForthMeal.setOnClickListener {
                    section.forth_meal?.id?.let { it1 -> onClick?.invoke(it1) }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = sections.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sections[position])
    }

}