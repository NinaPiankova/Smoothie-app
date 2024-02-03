package info.adastra.healthysmoothie.ui.fragments.recipes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.adastra.healthysmoothie.data.DataHolder
import info.adastra.healthysmoothie.adapters.InstructionAdapter
import info.adastra.healthysmoothie.R

class InstructionsFr : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_instructions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.instruction_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        var index = DataHolder.index
        var strInstruction = DataHolder.listRecipes[index].instruction.split('/')
        if(DataHolder.localization.equals("ru")) {
            strInstruction = DataHolder.listRecipes[index].instruction_ru.split('/')
        }
        val adapter = InstructionAdapter(strInstruction)
        recyclerView.adapter = adapter
    }
}