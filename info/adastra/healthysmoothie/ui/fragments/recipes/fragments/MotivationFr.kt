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
import kotlin.random.Random

class JokesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_jokes, container, false)
        val image: ImageView = view.findViewById(R.id.quoteImageView)
        val textQt: TextView = view.findViewById(R.id.quoteTextView)
        val textAuthor: TextView = view.findViewById(R.id.authorTextView)
        val maxValueIm = DataHolder.listMotivationImages.size
        val maxValueQu = DataHolder.listMotivation.size
        var randomImage = getRandomInt(maxValueIm)
        var randomMtQt = getRandomInt(maxValueQu)
        val quoteData = DataHolder.listMotivation[randomMtQt]
        var textQtTx = quoteData.quote
        var textAuthorTx = quoteData.quoteAuthor

        if(DataHolder.localization.equals("ru")) {
            textQtTx = quoteData.quote_ru
            textAuthorTx = quoteData.quoteAuthor_ru
        }
        if(textAuthorTx.equals("unknown") || textAuthorTx.equals("неизвестен")) {
            textAuthorTx=""
        }

        textQt.text = "  " + textQtTx
        textAuthor.text = textAuthorTx
        val drawable = ContextCompat.getDrawable(requireContext(), DataHolder.listMotivationImages[randomImage])
        image.setImageDrawable(drawable)

        return view
    }
}
fun getRandomInt(n: Int): Int {
    return Random.nextInt(n)
}
