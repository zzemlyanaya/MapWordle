package edu.skku.cs.pa1

import edu.skku.cs.pa1.model.*

class WordleController {

    val words = arrayListOf<String>()
    private var answer = ""
    var hasWon = false

    val guesses = arrayListOf<WordleItem>()
    val grays = arrayListOf<WordleItem>()
    val greens = arrayListOf<WordleItem>()
    val yellows = arrayListOf<WordleItem>()

    fun start() {
        answer = words.random()
    }

    fun check(guess: String): WordleResult {
        if (guess.length != WORD_LENGTH || guess !in words) {
            return WordleResult.INCORRECT_WORD
        }

        val guessItem = WordleItem(
            guess.toCharArray().mapIndexed { index, char ->
                CharUiModel(
                    char = char.toString(),
                    state = checkChar(index, char, guess.count { it == char })
                )
            }
        )

        guesses.add(guessItem)
        saveGuessedChars(guessItem)

        return if (guess == answer) WordleResult.WON.also { hasWon = true } else WordleResult.CONTINUE
    }

    private fun checkChar(index: Int, char: Char, charCount: Int) = when {
        answer.count { it == char } != charCount -> CharState.ABSENT
        char == answer[index] -> CharState.CORRECT
        answer.contains(char) -> CharState.WRONG_POSITION
        else -> CharState.ABSENT
    }

    private fun saveGuessedChars(guess: WordleItem) {
        guess.chars.forEach { charUiModel ->
            val char = charUiModel.char
            val item = WordleItem(chars = listOf(charUiModel))

            when (charUiModel.state) {
                CharState.CORRECT -> {
                    if (checkHas(greens, char)) return

                    greens.add(item)
                    yellows.removeAll { checkHasChar(it.chars, char) }
                }
                CharState.WRONG_POSITION -> {
                    if (checkHas(yellows, char)) return

                    yellows.add(item)
                    grays.removeAll { checkHasChar(it.chars, char) }
                }
                CharState.ABSENT -> {
                    if (checkHas(yellows, char).not() && checkHas(grays, char).not()) grays.add(item)
                }
            }
        }
    }

    fun restart() {
        guesses.clear()
        grays.clear()
        greens.clear()
        yellows.clear()

        answer = words.random()
        hasWon = false
    }

    // because we operate with object, we cannot just use contain
    private fun checkHas(list: List<WordleItem>, char: String) = list.any { it.chars.first().char == char }
    private fun checkHasChar(list: List<CharUiModel>, char: String) = list.any { it.char == char }

    companion object {
        const val WORD_LENGTH = 5
    }
}