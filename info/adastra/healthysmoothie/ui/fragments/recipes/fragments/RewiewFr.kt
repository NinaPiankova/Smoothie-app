package info.adastra.healthysmoothie.ui.fragments.recipes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import info.adastra.healthysmoothie.data.DataHolder
import info.adastra.healthysmoothie.R
import info.adastra.healthysmoothie.data.RecipeData

class RewiewFr : Fragment() {

    var easyTextView:TextView? = null
    var easyImageView:ImageView? = null
    var lowCaloryTextView:TextView? = null
    var lowCaloryImageView:ImageView? = null
    var fruitsTextView:TextView? = null
    var fruitsImageView:ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_over_view1, container, false)
        val args = arguments
        val recipe: RecipeData? = args?.getParcelable("recipeBundle")
        val resultTitle: TextView? = view?.findViewById(R.id.title_textView)
        val resultImage: ImageView? = view?.findViewById(R.id.main_imageView)
        val drawable = ContextCompat.getDrawable(requireContext(),recipe!!.image_recource_pr )
        val timeText : TextView? = view?.findViewById(R.id.time_textView)
        val description : TextView? = view?.findViewById(R.id.summary_textView)

        easyTextView = view?.findViewById(R.id.easy_textView)
        easyImageView = view?.findViewById(R.id.easy_imageView)
        lowCaloryTextView = view?.findViewById(R.id.lowCalory_textView)
        lowCaloryImageView = view?.findViewById(R.id.lowCalory_imageView)
        fruitsTextView = view?.findViewById(R.id.fruits_textView)
        fruitsImageView = view?.findViewById(R.id.fruits_imageView)

        //nutritionImageView
        //NUTRITION
        val caloriesCount: TextView? = view?.findViewById(R.id.caloriesCount)
        val totalFat: TextView? = view?.findViewById(R.id.totalFatCount)
        val transFat: TextView? = view?.findViewById(R.id.transFatCount)
        val dsaturatedFat: TextView? = view?.findViewById(R.id.saturatedFatCount)
        val cholesterol: TextView? = view?.findViewById(R.id.cholesterolCount)
        val sodium: TextView? = view?.findViewById(R.id.sodiumCount)
        val totalCarb: TextView? = view?.findViewById(R.id.totalCarbCount)
        val dietaryFiber: TextView? = view?.findViewById(R.id.dietaryFiberCount)
        val totalSugar: TextView? = view?.findViewById(R.id.totalSugarCount)
        val addedSugar: TextView? = view?.findViewById(R.id.addedSugarCount)
        val protein: TextView? = view?.findViewById(R.id.proteinCount)
        val vitaminsImage: ImageView? = view?.findViewById(R.id.nutritionImageView)
        val drawableVt = ContextCompat.getDrawable(requireContext(),recipe!!.image_recource_vt )
        var titleTx = recipe?.name
        var descriptionTx = recipe?.description

        if(DataHolder.localization.equals("ru")) {
            titleTx = recipe.name_ru
            descriptionTx = recipe.description_ru
        }
        resultTitle?.text = titleTx
        resultImage?.setImageDrawable(drawable)
        timeText?.text = recipe?.time.toString()
        description?.text = descriptionTx

        caloriesCount!!.text = recipe.caloriesCount.toString()
        totalFat!!.text=recipe.time.toString()
        transFat!!.text = recipe.cholesterol.toString()
        dsaturatedFat!!.text = recipe.transFat.toString()
        cholesterol!!.text = recipe.cholesterol.toString()
        sodium!!.text = recipe.sodium.toString()
        totalCarb!!.text = recipe.totalCarb.toString()
        dietaryFiber!!.text = recipe.dietaryFiber.toString()
        totalSugar!!.text = recipe.totalSugar.toString()
        addedSugar!!.text = recipe.addedSugar.toString()
        protein!!.text = recipe.protein.toString()
        //pottasium!!.text = recipe.pottasium.toString()
        vitaminsImage?.setImageDrawable(drawableVt)

            setValueEasy(recipe.tag)
            setValueLowCaloru(recipe.caloriesCount)
            setValueFruits(recipe.tag)




        return view
    }

    private fun setValueFruits(tag: String) {
        if(tag.contains("Easy")) {
            easyTextView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.textgrey))
            easyImageView?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
        }

    }

    private fun setValueLowCaloru(caloryCount: Double) {
    if(caloryCount < 180) {
        lowCaloryTextView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.textgrey))
        lowCaloryImageView?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
    }
    }

    private fun setValueEasy(tag: String) {
        if(tag.contains("Fruit")) {
            fruitsTextView?.setTextColor(ContextCompat.getColor(requireContext(), R.color.textgrey))
            fruitsImageView?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }
}