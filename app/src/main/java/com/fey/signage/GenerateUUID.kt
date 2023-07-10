package com.fey.signage

import dagger.hilt.android.AndroidEntryPoint
import java.util.*

object GenerateUUID {


    fun generateSevenDigitAlphanumericUUID(): String {

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

        return uuidBuilder.toString()
    }



}
