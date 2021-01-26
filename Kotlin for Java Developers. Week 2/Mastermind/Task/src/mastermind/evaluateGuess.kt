package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    var rightPosition = 0
    var wrongPosition = 0
    val listOfSecretCh = mutableListOf(
            secret[0],
            secret[1],
            secret[2],
            secret[3]
    )

    val listOfGuessCh = mutableListOf(
            guess[0],
            guess[1],
            guess[2],
            guess[3]
    )

    for (i in 0..3) {
        if (listOfSecretCh[i] == listOfGuessCh[i]) {
            rightPosition++
            listOfGuessCh[i] = ' '
            listOfSecretCh[i] = ' '
        }
    }

    listOfGuessCh.removeAll { ch -> ch == ' ' }
    listOfSecretCh.removeAll { ch -> ch == ' ' }

    if (listOfGuessCh.isNotEmpty()) {
        for (guessElement in listOfGuessCh) {
            for (i in 0 until listOfSecretCh.size) {
                if (guessElement == listOfSecretCh[i]) {
                    wrongPosition++
                    listOfSecretCh[i] = ' '
                    break
                }
            }
        }
    }

    return Evaluation(rightPosition, wrongPosition)

}
