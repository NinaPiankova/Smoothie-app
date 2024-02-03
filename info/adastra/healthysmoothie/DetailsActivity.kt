package info.adastra.healthysmoothie

import android.content.ContentValues
import android.content.res.ColorStateList
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import info.adastra.healthysmoothie.adapters.PagerAdapter
import info.adastra.healthysmoothie.data.DataHolder
import info.adastra.healthysmoothie.db.DatabaseHelper
import info.adastra.healthysmoothie.ui.fragments.recipes.fragments.ProductsFr
import info.adastra.healthysmoothie.ui.fragments.recipes.fragments.InstructionsFr
import info.adastra.healthysmoothie.ui.fragments.recipes.fragments.RewiewFr

class DetailsActivity : AppCompatActivity() {

    private val args: DetailsActivityArgs by navArgs()
    private var mDBHelper: DatabaseHelper? = DataHolder.dbHelper
    private var mDb: SQLiteDatabase? = DataHolder.dbHelper?.readableDatabase

    var iconItem: MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Fragments
        val fragments= ArrayList<Fragment>()
        fragments.add(RewiewFr())
        fragments.add(ProductsFr())
        fragments.add(InstructionsFr())

        val title = ArrayList<String>()

        if(DataHolder.localization.equals("ru")) {
            title.add("Описание")
            title.add("Состав")
            title.add("Инструкция")
        }
        else {
            title.add("Overview")
            title.add("Ingredients")
            title.add("Instructions")
        }

        try{
            var index: Int = getCurrentRecipeIndex()
            val m = DataHolder.listRecipes[index]

            val resultBundle = Bundle()
            resultBundle.putParcelable("recipeBundle", m)

            val adapter = PagerAdapter(
                resultBundle,
                fragments,
                title,
                supportFragmentManager
            )

            val viewPager: ViewPager = findViewById(R.id.viewPager)
            viewPager.adapter = adapter

            val tabLayout: TabLayout = findViewById(R.id.tabLayout)
            tabLayout.setupWithViewPager(viewPager)
        }
        catch (ex:Exception){
            Log.i("exception", "can't upload recipe data")
        }
    }

    private fun getCurrentRecipeIndex(): Int {
        return  DataHolder.index
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        iconItem= menu?.findItem(R.id.save_to_favorites_menu)
        iconItem?.setIconTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)))
        if(DataHolder.favoriteRecipes.contains(DataHolder.listRecipes[DataHolder.index].recipe_id)) {
            iconItem?.setIconTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange)))
        }

        return  true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        else if(item.itemId == R.id.save_to_favorites_menu){
            saveToFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveToFavorites(item: MenuItem) {
        var result = DataHolder.listRecipes[DataHolder.index]
        Log.i("saveToFavorites", "result = " + result.name)

        if(!DataHolder.favoriteRecipes.contains(result.recipe_id)) {
            DataHolder.favoriteRecipes.add(result.recipe_id)
            iconItem?.setIconTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange)))
            addRecipeToDataBase(DataHolder.index)
        }
        else {
            Log.i("favorite", "recipe already added")
            DataHolder.favoriteRecipes.remove(result.recipe_id)
            iconItem?.setIconTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)))
            removeRecipeToDataBase(DataHolder.index)
        }
    }

    private fun removeRecipeToDataBase(index: Int) {

        val dbHelper = DataHolder.dbHelper
        val recipeIdToRemove = DataHolder.listRecipes[index].recipe_id
        dbHelper!!.writableDatabase.delete("favorite", "recipe_id=?", arrayOf(recipeIdToRemove.toString()))
        dbHelper!!.close()
        DataHolder.favoriteRecipes.remove(DataHolder.listRecipes[index].recipe_id)
        GetInfoFromDataBase()
    }

    private fun GetInfoFromDataBase() {
            val query = "SELECT * FROM favorite"

            val mDb = DataHolder.dbHelper?.readableDatabase
            val cursor = mDb!!.rawQuery(query, null)
            if(cursor==null){
                Log.i("db:", "cursor==null()")
            }
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                Log.i("dataBase", "dataBaseValues = " + cursor.getString(1))
                cursor.moveToNext()
            }
            cursor.close()
    }

    private fun addRecipeToDataBase(index: Int) {

        val dbHelper = DataHolder.dbHelper //DatabaseHelper(this.baseContext)
        val db = dbHelper?.getWritableDatabase()
        val contentValues = ContentValues()
        contentValues.put("recipe_id", DataHolder.listRecipes[index].recipe_id)
        db!!.insert("favorite", null, contentValues)
        db.close()
    }
}






