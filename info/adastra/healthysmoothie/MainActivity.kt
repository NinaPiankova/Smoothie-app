package info.adastra.healthysmoothie

import android.content.res.Configuration
import android.content.res.Resources
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import info.adastra.healthysmoothie.data.DataHolder
import info.adastra.healthysmoothie.data.IngredientData
import info.adastra.healthysmoothie.data.MotivationData
import info.adastra.healthysmoothie.data.RecipeData
import info.adastra.healthysmoothie.data.RecipeIngredientData
import info.adastra.healthysmoothie.db.DatabaseHelper
import java.io.IOException
import java.math.BigDecimal


class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    var listRecipes = ArrayList<RecipeData>()
    var dicRecipeIngredients = mutableMapOf<Int, ArrayList<RecipeIngredientData>>()
    var dicIngredientsData =   mutableMapOf<Int, ArrayList<String>>()
    var dicIngredientsDataClass =   mutableMapOf<Int, IngredientData>()
    var mDBHelper: DatabaseHelper? = null
    var mDb: SQLiteDatabase? = null
    var listMotivation = ArrayList<MotivationData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Localization
        val resources: Resources = resources
        val configuration: Configuration = resources.configuration
        val currentLocale = configuration.locale
        val languageCode = currentLocale.language

        if(languageCode.equals("ru")) {
            DataHolder.localization = "ru"
        }
        else {
            DataHolder.localization = "en"
        }

        //DATA
        mDBHelper = DatabaseHelper(this)
        DataHolder.dbHelper = mDBHelper

        try {
            mDBHelper?.updateDataBase()
        } catch (mIOException: IOException) {
            throw Error("UnableToUpdateDatabase")
        }

        mDb = try {
            mDBHelper?.getWritableDatabase()
        } catch (mSQLException: Exception) {
            throw mSQLException
        }

        listRecipes = GetRecipeData()
        DataHolder.listRecipes = listRecipes
        GetDishIngrediens()
        AddIngredientsIntoRecipeData()
        FillNutritionData()

        //NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.recepiesFragment,
            R.id.favoriteRecepiesFragment,
            R.id.jokesFragment))

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val vegImg = getResources().getIdentifier("ic_vegvgline_24","drawable", getPackageName())
        val fruitImg = getResources().getIdentifier("ic_vegfrline_24","drawable", getPackageName())

        DataHolder.vegImage= vegImg
        DataHolder.fruitImage = fruitImg
        DataHolder.allRecipes = listRecipes
        DataHolder.ingredients = dicIngredientsDataClass
        DataHolder.standartImage = getResources().getIdentifier("smpr10","drawable", getPackageName())
        DataHolder.favoriteRecipes = fillFavoriteRecipe()
        DataHolder.listMotivation = fillMotivationData()
        FillRecipeNutritionData()
        InitIngredienceIcon()

    }

    private fun fillMotivationData(): ArrayList<MotivationData> {

            val query = "SELECT * FROM quote"
            val cursor = mDb!!.rawQuery(query, null)
            if(cursor==null){
                Log.i("db:", "cursor==null()")
            }
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                var motivationData = MotivationData()
                motivationData.id_qu = NumberUtil.GetNumber(cursor.getString(0))
                motivationData.quote = cursor.getString(1)
                motivationData.quoteAuthor = cursor.getString(2)
                motivationData.quote_ru = cursor.getString(3)
                motivationData.quoteAuthor_ru = cursor.getString(4)
                var image_name = cursor.getString(5)
                listMotivation.add(motivationData)
                if (!image_name.equals("insp")) {
                    var imageRes = getResources().getIdentifier(image_name,"drawable", getPackageName())
                    DataHolder.listMotivationImages.add(imageRes)
                }
                cursor.moveToNext()
            }
            cursor.close()

        return listMotivation
    }

    private fun fillFavoriteRecipe(): ArrayList<Int> {

            var favorite = arrayListOf<Int>()
            val cursor = mDb!!.rawQuery("SELECT * FROM favorite", null)
            if(cursor==null){
                Log.i("db:", "cursor==null()")
            }
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                var recipe_id = NumberUtil.GetNumber(cursor.getString(1))
                favorite.add(recipe_id)
                cursor.moveToNext()
            }
            cursor.close()
        return favorite
    }

    private fun FillRecipeNutritionData() {

        for (i in 0..listRecipes.size-1) {
            var key = listRecipes[i].dish_id
            var nutrients = dicRecipeIngredients[key]
            var caloriesCount= 0.0
            var totalFat= 0.0
            var dietaryFiber= 0.0
            var saturatedFat= 0.0
            var transFat= 0.0
            var cholesterol= 0.0
            var sodium= 0.0
            var totalCarb= 0.0
            var totalSugar= 0.0
            var protein= 0.0

            for (j in 0 until nutrients!!.size) {
                var nutrientId = nutrients[j].id_ingredient
                var nutrientCount =  nutrients[j].count_ingredient

                caloriesCount = caloriesCount + dicIngredientsDataClass[nutrientId]!!.calories * nutrientCount*0.025
                totalFat = totalFat +dicIngredientsDataClass[nutrientId]!!.fat * nutrientCount*0.025
                saturatedFat=saturatedFat+dicIngredientsDataClass[nutrientId]!!.fat_saturated * nutrientCount*0.025
                transFat=transFat+dicIngredientsDataClass[nutrientId]!!.fat_trans * nutrientCount*0.025
                cholesterol=cholesterol + dicIngredientsDataClass[nutrientId]!!.cholesterol * nutrientCount*0.025
                sodium = sodium + dicIngredientsDataClass[nutrientId]!!.sodium * nutrientCount*0.025
                totalCarb = totalCarb + dicIngredientsDataClass[nutrientId]!!.carbogidrate * nutrientCount*0.025
                dietaryFiber=dietaryFiber+dicIngredientsDataClass[nutrientId]!!.fiber_dietary * nutrientCount*0.025
                totalSugar = totalSugar + dicIngredientsDataClass[nutrientId]!!.sugars*nutrientCount*0.025
                protein = protein + dicIngredientsDataClass[nutrientId]!!.protein*nutrientCount*0.025
            }
            listRecipes[i].caloriesCount = BigDecimal(caloriesCount).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()//caloriesCount
            listRecipes[i].totalFat = BigDecimal(totalFat).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()//totalFat
            listRecipes[i].dietaryFiber = BigDecimal(dietaryFiber).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()//dietaryFiber
            listRecipes[i].dsaturatedFat = BigDecimal(saturatedFat).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()//dsaturatedFat
            listRecipes[i].transFat = BigDecimal(transFat).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()//transFat
            listRecipes[i].cholesterol = BigDecimal(cholesterol).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()//cholesterol
            listRecipes[i].sodium = BigDecimal(sodium).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()//sodium
            listRecipes[i].totalCarb = BigDecimal(totalCarb).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()//sodium
            listRecipes[i].totalSugar = BigDecimal(totalSugar).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()//totalSugar
            listRecipes[i].protein = BigDecimal(protein).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()//protein
        }
    }

    private fun FillNutritionData() {

        val query = "SELECT * FROM ingredients WHERE id IN (1265,1074,739,1817,1812,1796,1790,1252,2194,667," +
                "1827,1839,1880,2428,2201,752,1814,1835,1831,1834,1221,2132,1825,2353,1859,1811,1774,2792,759," +
                "2171,801,1801,844,2389,1185,2664,1889,746,1029,1886,1845,1872,681,753,2312,2650,1771,2169,2332," +
                "2665,1877,1778,2597,2810,1550,2817,1987,2017,2603,2361,2308,2786,2322,767,2824,764,2196)"

        val cursor = mDb!!.rawQuery(query, null)
        if(cursor==null){
            Log.i("db:", "cursor==null()")
        }
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            var ingredientData = IngredientData()
            ingredientData.id = NumberUtil.GetNumber(cursor.getString(0))
            ingredientData.name_key = cursor.getString(1)
            ingredientData.protein = NumberUtil.GetDoubleNumber(cursor.getString(2))
            ingredientData.fat = NumberUtil.GetDoubleNumber(cursor.getString(3))
            ingredientData.carbogidrate = NumberUtil.GetDoubleNumber(cursor.getString(4))
            ingredientData.calories = NumberUtil.GetDoubleNumber(cursor.getString(5))
            ingredientData.weight_base = NumberUtil.GetDoubleNumber(cursor.getString(6))
            ingredientData.weight_measureKey = NumberUtil.GetNumber(cursor.getString(7))
            ingredientData.fat_saturated = NumberUtil.GetDoubleNumber(cursor.getString(8))
            ingredientData.fat_trans =  NumberUtil.GetDoubleNumber(cursor.getString(9))
            ingredientData.cholesterol =  NumberUtil.GetDoubleNumber(cursor.getString(10))
            ingredientData.sodium =  NumberUtil.GetDoubleNumber(cursor.getString(11))
            ingredientData.fiber_dietary =  NumberUtil.GetDoubleNumber(cursor.getString(12))
            ingredientData.sugars=  NumberUtil.GetDoubleNumber(cursor.getString(13))
            ingredientData.icon = cursor.getString(16)

            var key = ingredientData.id
            dicIngredientsDataClass[key] = ingredientData

            cursor.moveToNext()
        }
        cursor.close()
    }

    private fun AddIngredientsIntoRecipeData() {
        for (i in 0..listRecipes.size - 1){
            var key = listRecipes[i].dish_id
           listRecipes[i].listIngredients = dicRecipeIngredients[key]!!
        }
    }

    private fun GetDishIngrediens() {
        var str = ""
        val cursor = mDb!!.rawQuery("SELECT * FROM dish_elements WHERE type_consist=1", null)
        if(cursor==null){
            Log.i("db:", "cursor==null()")
        }
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            var recipeIngredientData = RecipeIngredientData()
            var dishKey = NumberUtil.GetNumber(cursor.getString(1))
            var name = cursor.getString(2)
            var nutrientId = NumberUtil.GetNumber(name)
            var count = cursor.getString(3)

            recipeIngredientData.id_ingredient = NumberUtil.GetNumber(name)
            recipeIngredientData.count_ingredient = NumberUtil.GetDoubleNumber(count)
            str = name+"|"+count
            cursor.moveToNext()

            dicIngredientsData[nutrientId] = arrayListOf()

            if(!dicRecipeIngredients.containsKey(dishKey)) {
                dicRecipeIngredients[dishKey] = arrayListOf()
                dicRecipeIngredients[dishKey]?.add(recipeIngredientData)
            }
            else {
                dicRecipeIngredients[dishKey]?.add(recipeIngredientData)
            }
        }
        cursor.close()
    }

    private fun GetRecipeData(): ArrayList<RecipeData> {
        val cursor = mDb!!.rawQuery("SELECT * FROM recipes", null)
        if(cursor==null){
            Log.i("db:", "cursor==null()")
        }
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val recipe = RecipeData()
            recipe.recipe_id = NumberUtil.GetNumber(cursor.getString(0))
            recipe.dish_id =  NumberUtil.GetNumber(cursor.getString(1))
            recipe.name = cursor.getString(2)
            recipe.likes = NumberUtil.GetNumber(cursor.getString(3))
            recipe.time = NumberUtil.GetNumber(cursor.getString(4))
            recipe.tag = cursor.getString(5)
            recipe.description = cursor.getString(6)
            var imageName = cursor.getString(7)
            recipe.ingredient_tx=cursor.getString(8)
            recipe.ingredient_tx_ru=cursor.getString(9)
            var imageNamePr = cursor.getString(10)
            recipe.instruction = cursor.getString(11)
            recipe.instruction_ru = cursor.getString(12)
            recipe.description_ru = cursor.getString(13)
            recipe.name_ru = cursor.getString(14)
            var image_vt = cursor.getString(15)

            recipe.image_recource = getResources().getIdentifier(imageName,"drawable", getPackageName())
            recipe.image_recource_pr = getResources().getIdentifier(imageNamePr,"drawable", getPackageName())
            recipe.image_recource_vt =  getResources().getIdentifier(image_vt,"drawable", getPackageName())
            listRecipes.add(recipe)
            cursor.moveToNext()
        }
        cursor.close()
        return listRecipes
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun InitIngredienceIcon() {
        DataHolder.ingrediensIcon["blueberry"] = getResources().getIdentifier("fruits18","drawable", getPackageName())
        DataHolder.ingrediensIcon["oatmealflakes"] = getResources().getIdentifier("grains7","drawable", getPackageName())
        DataHolder.ingrediensIcon["yoghurt"] = getResources().getIdentifier("dairy60","drawable", getPackageName())
        DataHolder.ingrediensIcon["yogurt"] = getResources().getIdentifier("dairy60","drawable", getPackageName())
        DataHolder.ingrediensIcon["ice"] = getResources().getIdentifier("ice1","drawable", getPackageName())
        DataHolder.ingrediensIcon["ginger"] = getResources().getIdentifier("vegetables152","drawable", getPackageName())
        DataHolder.ingrediensIcon["honeyorsugar"] = getResources().getIdentifier("sweets112","drawable", getPackageName())
        DataHolder.ingrediensIcon["strawberry"] = getResources().getIdentifier("fruits108","drawable", getPackageName())
        DataHolder.ingrediensIcon["banana"] = getResources().getIdentifier("fruits13","drawable", getPackageName())
        DataHolder.ingrediensIcon["almondnuts"] = getResources().getIdentifier("nuts6","drawable", getPackageName())
        DataHolder.ingrediensIcon["raspberries"] = getResources().getIdentifier("fruits103","drawable", getPackageName())
        DataHolder.ingrediensIcon["bilberry"] = getResources().getIdentifier("fruits18","drawable", getPackageName())
        DataHolder.ingrediensIcon["milk"] = getResources().getIdentifier("dairy35","drawable", getPackageName())
        DataHolder.ingrediensIcon["cherry"] = getResources().getIdentifier("fruits27","drawable", getPackageName())
        DataHolder.ingrediensIcon["dates"] = getResources().getIdentifier("fruits97","drawable", getPackageName())
        DataHolder.ingrediensIcon["walnuts"] = getResources().getIdentifier("nuts35","drawable", getPackageName())
        DataHolder.ingrediensIcon["orange"] = getResources().getIdentifier("fruits76","drawable", getPackageName())
        DataHolder.ingrediensIcon["orangejuice"] = getResources().getIdentifier("beverages63","drawable", getPackageName())
        DataHolder.ingrediensIcon["lemonjuice"] = getResources().getIdentifier("beverages27","drawable", getPackageName())
        DataHolder.ingrediensIcon["vanillin"] = getResources().getIdentifier("spices42","drawable", getPackageName())
        DataHolder.ingrediensIcon["redcurrant"] = getResources().getIdentifier("fruits35","drawable", getPackageName())
        DataHolder.ingrediensIcon["cinnamon"] = getResources().getIdentifier("spices13","drawable", getPackageName())
        DataHolder.ingrediensIcon["blackcurrant"] = getResources().getIdentifier("fruits34","drawable", getPackageName())
        DataHolder.ingrediensIcon["cranberries"] = getResources().getIdentifier("fruits31","drawable", getPackageName())
        DataHolder.ingrediensIcon["cream"] = getResources().getIdentifier("dairy11","drawable", getPackageName())
        DataHolder.ingrediensIcon["lingonberry"] = getResources().getIdentifier("fruits31","drawable", getPackageName())
        DataHolder.ingrediensIcon["cherryjuice"] = getResources().getIdentifier("beverages90","drawable", getPackageName())
        DataHolder.ingrediensIcon["redapple"] = getResources().getIdentifier("fruits2","drawable", getPackageName())
        DataHolder.ingrediensIcon["peanutbutter"] = getResources().getIdentifier("nuts47","drawable", getPackageName())
        DataHolder.ingrediensIcon["kiwi"] = getResources().getIdentifier("fruits56","drawable", getPackageName())
        DataHolder.ingrediensIcon["avocado"] = getResources().getIdentifier("fruits12","drawable", getPackageName())
        DataHolder.ingrediensIcon["yogurtormilk"] = getResources().getIdentifier("dairy35","drawable", getPackageName())
        DataHolder.ingrediensIcon["pineapple"] = getResources().getIdentifier("fruits89","drawable", getPackageName())
        DataHolder.ingrediensIcon["pumpkinpuree"] = getResources().getIdentifier("vegetables58","drawable", getPackageName())
        DataHolder.ingrediensIcon["coconutmilk"] = getResources().getIdentifier("beverages35","drawable", getPackageName())
        DataHolder.ingrediensIcon["pineapplejuice"] = getResources().getIdentifier("beverages100","drawable", getPackageName())
        DataHolder.ingrediensIcon["watermelonpulp"] = getResources().getIdentifier("fruits112","drawable", getPackageName())
        DataHolder.ingrediensIcon["instantcoffee"] = getResources().getIdentifier("beverages36","drawable", getPackageName())
        DataHolder.ingrediensIcon["chickenegg"] = getResources().getIdentifier("dairy44","drawable", getPackageName())
        DataHolder.ingrediensIcon["cocoa"] = getResources().getIdentifier("spices16","drawable", getPackageName())
        DataHolder.ingrediensIcon["spinach"] = getResources().getIdentifier("vegetables163","drawable", getPackageName())
        DataHolder.ingrediensIcon["water"] = getResources().getIdentifier("beverages23","drawable", getPackageName())
        DataHolder.ingrediensIcon["pear"] = getResources().getIdentifier("fruits84","drawable", getPackageName())
        DataHolder.ingrediensIcon["applejuice"] = getResources().getIdentifier("beverages59","drawable", getPackageName())
        DataHolder.ingrediensIcon["oatmeal"] = getResources().getIdentifier("grains7","drawable", getPackageName())
        DataHolder.ingrediensIcon["peach"] = getResources().getIdentifier("fruits81","drawable", getPackageName())
        DataHolder.ingrediensIcon["apple"] = getResources().getIdentifier("fruits2","drawable", getPackageName())
        DataHolder.ingrediensIcon["grapefruit"] = getResources().getIdentifier("fruits99","drawable", getPackageName())
        DataHolder.ingrediensIcon["mintleaves"] = getResources().getIdentifier("vegetables121","drawable", getPackageName())
        DataHolder.ingrediensIcon["mint"] = getResources().getIdentifier("vegetables121","drawable", getPackageName())
        DataHolder.ingrediensIcon["mango"] = getResources().getIdentifier("fruits68","drawable", getPackageName())
        DataHolder.ingrediensIcon["ricemilk"] = getResources().getIdentifier("dairy35","drawable", getPackageName())
        DataHolder.ingrediensIcon["celerystalk"] = getResources().getIdentifier("vegetables149","drawable", getPackageName())
        DataHolder.ingrediensIcon["celery"] = getResources().getIdentifier("vegetables149","drawable", getPackageName())
        DataHolder.ingrediensIcon["apricot"] = getResources().getIdentifier("fruits8","drawable", getPackageName())
        DataHolder.ingrediensIcon["cucumber"] = getResources().getIdentifier("vegetables16","drawable", getPackageName())
        DataHolder.ingrediensIcon["plum"] = getResources().getIdentifier("fruits93","drawable", getPackageName())
        DataHolder.ingrediensIcon["nectarine"] = getResources().getIdentifier("fruits81","drawable", getPackageName())
        DataHolder.ingrediensIcon["carrots"] = getResources().getIdentifier("vegetables1","drawable", getPackageName())
        DataHolder.ingrediensIcon["tomatoes"] = getResources().getIdentifier("vegetables8","drawable", getPackageName())
        DataHolder.ingrediensIcon["olive oil"] = getResources().getIdentifier("fats30","drawable", getPackageName())
        DataHolder.ingrediensIcon["fresh greens"] = getResources().getIdentifier("vegetables149","drawable", getPackageName())
        DataHolder.ingrediensIcon["greens"] = getResources().getIdentifier("vegetables149","drawable", getPackageName())
        DataHolder.ingrediensIcon["parsley"] = getResources().getIdentifier("vegetables149","drawable", getPackageName())
        DataHolder.ingrediensIcon["basil"] = getResources().getIdentifier("vegetables121","drawable", getPackageName())
        DataHolder.ingrediensIcon["beet"] = getResources().getIdentifier("vegetables83","drawable", getPackageName())
        DataHolder.ingrediensIcon["soymilk"] = getResources().getIdentifier("dairy35","drawable", getPackageName())
        DataHolder.ingrediensIcon["sproutedwheat"] = getResources().getIdentifier("grains7","drawable", getPackageName())
        DataHolder.ingrediensIcon["garlic"] = getResources().getIdentifier("vegetables101","drawable", getPackageName())
        DataHolder.ingrediensIcon["tabascosauce"] = getResources().getIdentifier("vegetables14","drawable", getPackageName())
        DataHolder.ingrediensIcon["salt"] = getResources().getIdentifier("spices8","drawable", getPackageName())
        DataHolder.ingrediensIcon["zucchini"] = getResources().getIdentifier("vegetables52","drawable", getPackageName())
        DataHolder.ingrediensIcon["lemon"] = getResources().getIdentifier("fruits58","drawable", getPackageName())
        DataHolder.ingrediensIcon["bellpepper"] = getResources().getIdentifier("vegetables89","drawable", getPackageName())
        DataHolder.ingrediensIcon["pomegranatejuice"] = getResources().getIdentifier("beverages104","drawable", getPackageName())
        DataHolder.ingrediensIcon["any"] = getResources().getIdentifier("any","drawable", getPackageName())
    }

    override fun onDestroy() {
        mDBHelper?.close()
        super.onDestroy()
    }

}