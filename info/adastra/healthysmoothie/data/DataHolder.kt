package info.adastra.healthysmoothie.data

import info.adastra.healthysmoothie.db.DatabaseHelper

class DataHolder {
    companion object {

        lateinit var ingridiensLocalisation: List<String>
        var localization = "en"
        var dbHelper: DatabaseHelper? = null
        var listRecipes = ArrayList<RecipeData>()
        var favoriteRecipes = ArrayList<Int>()
        var vegImage = 0
        var fruitImage= 0
        var allRecipes: ArrayList<RecipeData> = ArrayList<RecipeData>()
        var index = 0
        var ingredients =   mutableMapOf<Int, IngredientData>()
        var ingrediensIcon = mutableMapOf<String, Int>()
        var standartImage = 0
        var searchResult= ArrayList<RecipeData>()
        var listMotivation = ArrayList<MotivationData>()
        var listMotivationImages = ArrayList<Int>()

    }
}