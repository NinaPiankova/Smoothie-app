package info.adastra.healthysmoothie.adapters


import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import info.adastra.healthysmoothie.data.DataHolder
import info.adastra.healthysmoothie.R
import info.adastra.healthysmoothie.data.RecipeData


class AdapterRecipe(private var recipeList: ArrayList<RecipeData>):
    RecyclerView.Adapter<AdapterRecipe.AppViewHolder>() {

    private lateinit var mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val  itemView = LayoutInflater.from(parent.context).inflate(R.layout.recipes_row_layout, parent, false)

        return  AppViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {

        val  currentItem = recipeList[position]
        val colourGreen = ContextCompat.getColor(holder.itemView.context, R.color.green)
        val colourOrange = ContextCompat.getColor(holder.itemView.context, R.color.orange)
        val imageVegt =  ContextCompat.getDrawable(holder.itemView.context, DataHolder.vegImage)
        val imageFruit = ContextCompat.getDrawable(holder.itemView.context, DataHolder.fruitImage)
        holder.recipeItemId = currentItem.recipe_id-1;

        try{
            var drawable = ContextCompat.getDrawable(holder.itemView.context, currentItem.image_recource)

            if(drawable == null) {
                drawable = ContextCompat.getDrawable(holder.itemView.context,
                    DataHolder.standartImage
                )
            }

            var suff =" min"
            var fruit = "Fr"
            var veg = "Vg"
            holder.titleImage.setImageDrawable(drawable)
            holder.titleText.text = currentItem.name
            holder.description.text = currentItem.description
            if(DataHolder.localization.equals("ru")) {
                holder.titleText.text = currentItem.name_ru
                holder.description.text = currentItem.description_ru
                suff =" мин"
                fruit = "Фр"
                veg = "Овщ"
            }

            if (DataHolder.localization.equals("ru")){

            }
            holder.time.text = currentItem.time.toString() + suff

            if(currentItem.tag.contains("Fruit")) {
                holder.frutsOrVeg.setColorFilter(colourOrange, PorterDuff.Mode.SRC_IN);
                holder.frutsOrVeg.setImageDrawable(imageFruit)
                holder.frutsOrVegText.text = fruit
                holder.frutsOrVegText.setTextColor(colourOrange)
            }
            else {
                holder.frutsOrVeg.setColorFilter(colourGreen, PorterDuff.Mode.SRC_IN);
                holder.frutsOrVeg.setImageDrawable(imageVegt)
                holder.frutsOrVegText.text = veg
                holder.frutsOrVegText.setTextColor(colourGreen)
            }

        } catch (ex: Exception) {
            Log.e("item", currentItem.name)
        }
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    fun updateData(searchResult: ArrayList<RecipeData>) {
        this.recipeList = searchResult
        notifyDataSetChanged()
    }


    class AppViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView) {

        val titleImage: ImageView = itemView.findViewById(R.id.recipe_imageView)
        val titleText: TextView =itemView.findViewById(R.id.title_textView)
        val description: TextView =itemView.findViewById(R.id.description_textView)
        //val likes: TextView =itemView.findViewById(R.id.heart_textView)
        val time: TextView =itemView.findViewById(R.id.clock_textView)
        val frutsOrVeg: ImageView =itemView.findViewById(R.id.leaf_imageView)
        val frutsOrVegText: TextView =itemView.findViewById(R.id.leaf_textView)

        var recipeItemId: Int=0;

        init {
            itemView.setOnClickListener{
                listener.onItemClick(recipeItemId)//adapterPosition)
            }
        }
    }
}