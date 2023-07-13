package com.fey.signage

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

object GenerateUUID {

    var uuid = MutableLiveData<String>("")

    fun generateSevenDigitAlphanumericUUID(): MutableLiveData<String> {

        val random = Random()
        val alphabet = "abcdefghijklmnopqrstuvwxyz"
        val numbers = "0123456789"
        val uuidBuilder = StringBuilder(7)

        // add at least one letter
        val randomChar = alphabet[random.nextInt(alphabet.length)]
        uuidBuilder.append(randomChar)

        // add at least one number
        val randomNumber = numbers[random.nextInt(numbers.length)]
        uuidBuilder.append(randomNumber)

        repeat(5) {
            val randomCharOrNumber = if (random.nextBoolean()) {
                alphabet[random.nextInt(alphabet.length)]
            } else {
                numbers[random.nextInt(numbers.length)]
            }
            uuidBuilder.append(randomCharOrNumber)
        }

        return MutableLiveData<String>("k305nva")
    }

fun assignmentUUID() {
    uuid = generateSevenDigitAlphanumericUUID()
}

}
