package uz.favvora_urgench.menu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.LinearLayout
import android.widget.TextView
import uz.favvora_urgench.menu.R
import uz.favvora_urgench.menu.model.Meal
import uz.favvora_urgench.menu.utils.setAsPrice

class SearchAdapter(context: Context, private val meals: ArrayList<Meal>): ArrayAdapter<Meal>(context, 0, meals) {

    var onClick: ((Int?) -> Unit)? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_meal, parent, false)

        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)

        val item = view.findViewById<LinearLayout>(R.id.llItem)

        val meal = getItem(position)

        if (meal != null) {
            tvName.text = meal.name
            tvPrice.text = if (meal.price == 0) "" else meal.price.toString().setAsPrice()

            item.setOnClickListener {
                if (meal.id == 0) {
                    onClick?.invoke(null)
                } else {
                    onClick?.invoke(meal.id)
                }
            }

        }

        return view
    }

    override fun getFilter(): Filter {
        return filter
    }

    private val filter = object: Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            val suggestions = ArrayList<Meal>()

            suggestions.addAll(meals)

            results.values = suggestions
            results.count = suggestions.size

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            clear()
            addAll(results?.values as ArrayList<Meal>)
            notifyDataSetChanged()
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as Meal).name
        }

    }

}