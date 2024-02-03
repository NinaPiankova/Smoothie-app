package info.adastra.healthysmoothie.ui.fragments.recipes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.adastra.healthysmoothie.data.DataHolder
import info.adastra.healthysmoothie.adapters.IngredienseAdapter
import info.adastra.healthysmoothie.R

class ProductsFr : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ingredience, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.ingredients_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        //listIngredients
        var index = DataHolder.index


        var strIngrediense = DataHolder.listRecipes[index].ingredient_tx.split('*')
        if(DataHolder.localization.equals("ru")) {
            DataHolder.ingridiensLocalisation = DataHolder.listRecipes[index].ingredient_tx_ru.split('*')
        }

        val adapter = IngredienseAdapter(strIngrediense)
        recyclerView.adapter = adapter
    }
}