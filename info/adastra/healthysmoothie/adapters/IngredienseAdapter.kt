package info.adastra.healthysmoothie.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import info.adastra.healthysmoothie.data.DataHolder
import info.adastra.healthysmoothie.R

class IngredienseAdapter(private val ingrediences: List<String>)
    : RecyclerView.Adapter<IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.ingrediense_row_layout,
            parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        var item = ingrediences[position]
        var textIngredient = item
        var textIngredient_ru = ""
        var textIngredientCount_ru = ""
        var textIngredientCount = "100 gr"
        var item_ru =ingrediences[position]
        if(DataHolder.localization.equals("ru")) {
            item_ru = DataHolder.ingridiensLocalisation[position]
            textIngredientCount = "100 гр"
            textIngredient_ru = item_ru
            val arr_ru = item_ru.split('|')
            if(arr_ru.size>1) {
                textIngredient_ru = arr_ru[0]
                textIngredientCount_ru = arr_ru[1]
            }
        }

        val arr = item.split('|')
        var drawable = ContextCompat.getDrawable(holder.itemView.context,
            DataHolder.ingrediensIcon["any"]!!)

        if(arr.size == 2) {
            textIngredient = arr[0]
            textIngredientCount = arr[1]
            val modifiedString = arr[0].lowercase().replace(" ", "").replace("\n", "").trim()
            if(DataHolder.ingrediensIcon.containsKey(modifiedString)) {
                drawable = ContextCompat.getDrawable(holder.itemView.context,
                    DataHolder.ingrediensIcon[modifiedString]!!)
            }

            holder.ingImage.setImageDrawable(drawable)
            if(DataHolder.localization.equals("ru")) {
                holder.textViewName.text = textIngredient_ru
                holder.textViewCount.text = textIngredientCount_ru
            }
            else {
                holder.textViewName.text = textIngredient
                holder.textViewCount.text = textIngredientCount
            }
        }
    }

    override fun getItemCount(): Int {
        return ingrediences.size
    }
}

class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textViewName: TextView = itemView.findViewById(R.id.nameIngtTextView)
    val textViewCount:TextView = itemView.findViewById(R.id.ingredCountTextView)
    val ingImage:ImageView = itemView.findViewById(R.id.ingredientImageView)
}