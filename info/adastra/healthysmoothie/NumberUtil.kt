package info.adastra.healthysmoothie
import android.util.Log

class NumberUtil {
    companion object {
        fun GetNumber(str:String): Int {
            var parsedInt:Int=-1
            try {
                parsedInt = str.toInt()
            } catch (nfe: NumberFormatException) {
                Log.d("exception", "invalid value exception")
            }
            return parsedInt
        }

        fun GetDoubleNumber(str:String): Double {
            var parsedDouble:Double=-1.0
            try {
                parsedDouble = str.toDouble()
            } catch (nfe: NumberFormatException) {
                Log.d("exception", "invalid value exception")
            }
            return parsedDouble
        }
    }
}