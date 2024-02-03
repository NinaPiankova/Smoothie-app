package info.adastra.healthysmoothie.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class RecipeData: Parcelable {


    var recipe_id: Int = 0
    var dish_id:Int=0
    var name:String=""
    var likes:Int =0
    var time:Int = 0
    var tag:String = ""
    var description:String=""
    var image_recource =0
    var image_recource_pr =0
    var image_recource_vt =0
    var listIngredients= ArrayList<RecipeIngredientData>()
    var instruction = ""
    var ingredient_tx=""
    var ingredient_tx_ru=""

    var name_ru = ""
    var instruction_ru=""
    var description_ru=""



    var caloriesCount= 0.0
    var totalFat= 0.0
    var dietaryFiber= 0.0
    var dsaturatedFat= 0.0
    var transFat= 0.0
    var cholesterol= 0.0
    var sodium= 0.0
    var pottasium= 0.0
    var totalCarb= 0.0
    var totalSugar= 0.0
    var addedSugar= 0.0
    var protein= 0.0




}