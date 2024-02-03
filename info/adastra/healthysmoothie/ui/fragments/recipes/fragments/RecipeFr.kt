package info.adastra.healthysmoothie.ui.fragments.recipes.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.adastra.healthysmoothie.adapters.AdapterRecipe
import info.adastra.healthysmoothie.data.DataHolder
import info.adastra.healthysmoothie.DetailsActivity
import info.adastra.healthysmoothie.R
import info.adastra.healthysmoothie.data.RecipeData

class RecipeFr : Fragment(), SearchView.OnQueryTextListener {

    private  lateinit var newRecyclerView: RecyclerView
    private var searchQuerry = ""
    private  var searchResult = ArrayList<RecipeData>()
    private var adapter: AdapterRecipe? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_recepies, container, false)


        newRecyclerView = view.findViewById(R.id.mrecyklerFragmentViewer)
        newRecyclerView.layoutManager = LinearLayoutManager(context)
        newRecyclerView.setHasFixedSize(true)
        adapter = AdapterRecipe(DataHolder.listRecipes)
        newRecyclerView.adapter = adapter
        adapter!!.setOnItemClickListener(object : AdapterRecipe.onItemClickListener {
            override fun onItemClick(position: Int) {
                try {
                    DataHolder.index = position
                    val intent = Intent(requireContext(), DetailsActivity::class.java)
                    startActivity(intent)
                } catch (ex:Exception) {
                    Log.i("Listener", "can't find recipe")
                }
            }
        })

        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipe_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchRecipe(searchQuerry)
        return true
    }

    private fun searchRecipe(searchQuerry: String) {
        searchResult.clear()
        if(!searchQuerry.equals("")) {
            var searcWord = searchQuerry.toLowerCase().trim()
            val words = searcWord.split("[,\\s]+".toRegex())

            if(words.size == 1) {
                searchResult = searchWord(searcWord, DataHolder.listRecipes)
            } else if (words.size > 1) {
                var startArr = searchWord(words[0], DataHolder.listRecipes)
                for (i in 1 until words.size) {
                    startArr =  searchWord(words[i], startArr)
                }
                searchResult = startArr
            }
        }
        else {
            Log.i("search", "Can't find")
        }
        DataHolder.searchResult = searchResult
        updateAdapterData(searchResult)
    }

    private fun searchWord(searcWord: String, listRecipes: ArrayList<RecipeData>): ArrayList<RecipeData> {
        var result = ArrayList<RecipeData>()
        for( i in 0 until listRecipes.size) {
            var info = listRecipes[i].name.toLowerCase() + " " + listRecipes[i].ingredient_tx
            if(info.contains(searcWord)) {
                result.add(listRecipes[i])
            }
        }
        return result
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchQuerry = newText!!
        return true
    }
    private fun updateAdapterData(newRecipesList: List<RecipeData>) {
        adapter!!.updateData(searchResult)
        }
    }

