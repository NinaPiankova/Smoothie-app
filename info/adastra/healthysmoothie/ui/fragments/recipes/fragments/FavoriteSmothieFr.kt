package info.adastra.healthysmoothie.ui.fragments.recipes.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.adastra.healthysmoothie.data.DataHolder
import info.adastra.healthysmoothie.DetailsActivity
import info.adastra.healthysmoothie.R
import info.adastra.healthysmoothie.data.RecipeData
import info.adastra.healthysmoothie.adapters.AdapterRecipe

class FavoriteSmothieFr : Fragment() {

    private  lateinit var newRecyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_favorite_recepies, container, false)


        newRecyclerView = view.findViewById(R.id.favoriteRecyklerFragmentViewer)
        newRecyclerView.layoutManager = LinearLayoutManager(context)
        newRecyclerView.setHasFixedSize(true)
        var favoriteRecipes = ArrayList<RecipeData>()
        favoriteRecipes = fillFavoriteRecipes()
        var adapter = AdapterRecipe(favoriteRecipes)
        newRecyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : AdapterRecipe.onItemClickListener {
            override fun onItemClick(position: Int) {

                Log.i("listener", "listener = " + DataHolder.listRecipes[position].name)

                try {
                    DataHolder.index = position
                    val intent = Intent(requireContext(), DetailsActivity::class.java)
                    startActivity(intent)
                } catch (ex:Exception) {
                    Log.i("Listener", "can't find recipe")
                }
            }
        })

        return view

        return inflater.inflate(R.layout.fragment_favorite_recepies, container, false)
    }

    private fun fillFavoriteRecipes(): ArrayList<RecipeData> {

        var favoriteRecipes = ArrayList<RecipeData>()
        DataHolder.favoriteRecipes.forEach{
            n-> favoriteRecipes.add(DataHolder.listRecipes[n-1])
        }
        return favoriteRecipes
    }
}