package edu.skku.cs.pa1

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import edu.skku.cs.pa1.adapter.WordleAdapter
import edu.skku.cs.pa1.databinding.ActivityMainBinding
import edu.skku.cs.pa1.model.WordleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val wordleAdapter = WordleAdapter()
    private val graysAdapter = WordleAdapter()
    private val yellowsAdapter = WordleAdapter()
    private val greensAdapter = WordleAdapter()

    private val controller = WordleController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progress.visibility = View.VISIBLE
        binding.progress.show()
        loadWords()
    }

    private fun loadWords() {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = runCatching {
                applicationContext.assets
                    .open("words.txt")
                    .bufferedReader().useLines { lines -> controller.words.addAll(lines) }
            }

            lifecycleScope.launch(Dispatchers.Main) {
                if (result.isSuccess) {
                    binding.progress.visibility = View.GONE
                    initWorlde()
                    showToast(getString(R.string.ready))
                } else {
                    binding.progress.visibility = View.GONE
                    showToast(getString(R.string.load_error))
                }
            }
        }
    }

    private fun initWorlde() {
        controller.start()

        initRecycler(wordleAdapter, binding.wordsRecycler)
        initRecycler(graysAdapter, binding.graysRecycler)
        initRecycler(yellowsAdapter, binding.yellowsRecycler)
        initRecycler(greensAdapter, binding.greensRecycler)

        binding.guessInput.isEnabled = true
        binding.submitBtn.isEnabled = true

        binding.submitBtn.setOnClickListener {
            if (controller.hasWon) {
                restart()
            } else {
                handleGuess(binding.guessInput.text.toString().lowercase())
                binding.guessInput.text?.clear()
            }
        }
    }

    private fun handleGuess(guess: String) {
        when (controller.check(guess)) {
            WordleResult.WON -> {
                updateUiState()
                showToast(getString(R.string.you_won))

                binding.guessInput.isEnabled = false
                binding.submitBtn.setText(R.string.restart)
            }
            WordleResult.CONTINUE -> {
                updateUiState()
            }
            WordleResult.INCORRECT_WORD -> {
                showToast(getString(R.string.word_not_in_list_error, guess))
            }
        }
    }

    private fun restart() {
        controller.restart()

        binding.guessInput.isEnabled = true
        binding.submitBtn.setText(R.string.submit)
        updateUiState()
    }

    private fun updateUiState() {
        wordleAdapter.updateList(controller.guesses)
        graysAdapter.updateList(controller.grays)
        yellowsAdapter.updateList(controller.yellows)
        greensAdapter.updateList(controller.greens)
    }

    private fun initRecycler(adapter: WordleAdapter, recycler: RecyclerView) {
        recycler.adapter = adapter
        recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}