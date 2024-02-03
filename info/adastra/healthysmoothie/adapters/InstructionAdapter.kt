package info.adastra.healthysmoothie.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import info.adastra.healthysmoothie.R

class InstructionAdapter(private val instruction: List<String>)
    : RecyclerView.Adapter<InstructionViewHolder>() {

    //var number:Int = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.instruction_row_layout1,
            parent, false)
        return InstructionViewHolder(view)
    }

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {
        val item = instruction[position]
        var title = "title"
        var instruction = "instruction"

        var srtArr = item.split(':')
        if(srtArr.size == 2) {
            var title = srtArr[0]
            var instruction = srtArr[1]
            holder.textViewTitle.text = title
            holder.textViewInstruction.text = instruction
        }
        else {
            var instruction = item
        }
    }

    override fun getItemCount(): Int {
        return instruction.size
    }
}
class InstructionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textViewTitle: TextView = itemView.findViewById(R.id.insTitletextView)
    //val textViewInstructionStep: TextView = itemView.findViewById(R.id.insSteptextView)
    val textViewInstruction: TextView = itemView.findViewById(R.id.instrTextView)


}