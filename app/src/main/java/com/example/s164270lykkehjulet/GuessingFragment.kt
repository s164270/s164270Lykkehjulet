package com.example.s164270lykkehjulet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.s164270lykkehjulet.databinding.FragmentGuessingBinding
import java.lang.StringBuilder
import kotlin.text.sumOf as sumOf1

/**
 * A simple [Fragment] subclass.
 * Use the [GuessingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GuessingFragment : Fragment() {

    private var _binding: FragmentGuessingBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    /* Game variables */
    private var score = 0
    private var lives = 5
    private var selectedLetter: Char = ' '
    private var secretWord: String = ""
    private var shownWord: String = ""
    private var gameStarted: Boolean = false
    private val guessedLetters: MutableList<Char> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGuessingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 9)

        val lambdaLetterClick: (l: String) -> Unit = { letterClick(it) }
        recyclerView.adapter = LetterAdapter(lambdaLetterClick)

        binding.categoryAnimals.setOnClickListener { startNewGame(Category.ANIMALS) }
        binding.categoryFood.setOnClickListener { startNewGame(Category.FOOD) }
        binding.categoryMetals.setOnClickListener { startNewGame(Category.METALS) }
        binding.guessButton.setOnClickListener { guessSelectedLetter() }

        resetGame()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun letterClick(l: String) {
        selectedLetter = l[0]

        binding.score.text = score.toString()
        binding.letterSelected.text = selectedLetter.toString()
    }

    fun guessSelectedLetter() {
        if(!gameStarted) return

        if(guessedLetters.contains(selectedLetter))
        {
            Toast.makeText(context, R.string.alreadyGuessedThatLetter, Toast.LENGTH_SHORT).show()
            return
        }

        guessedLetters.add(selectedLetter)
        val occurrences = secretWord.count { c -> c == selectedLetter }
        if(occurrences > 0) {
            score += 100 * occurrences

            //Revealed the guessed letter
            val tempStr = StringBuilder()
            for (c in secretWord) {
                if (guessedLetters.contains(c) || c.isWhitespace()) tempStr.append(c) else tempStr.append("_")
            }
            shownWord = tempStr.toString()
        }
        else
        {
            lives -= 1
        }

        //
        binding.score.text = score.toString()
        binding.livesLeft.text = lives.toString()
        binding.secretWord.text = shownWord.toString()
        binding.lettersGuessed.text = guessedLetters.toString().substring(1, guessedLetters.toString().length - 1)

        // Check for game over
        if(lives < 1) {
            val action = GuessingFragmentDirections.actionGuessingFragmentToGameLostFragment()
            requireView().findNavController().navigate(action)
        }
        else if(shownWord.contentEquals(secretWord))
        {
            val action = GuessingFragmentDirections.actionGuessingFragmentToGameWonFragment()
            requireView().findNavController().navigate(action)
        }

    }


    fun startNewGame(category: Category) {

        binding.selectedCategory.text = when(category) {
            Category.ANIMALS -> binding.categoryAnimals.text.toString()
            Category.FOOD -> binding.categoryFood.text.toString()
            Category.METALS -> binding.categoryMetals.text.toString()
            else -> "" }

        gameStarted = true

        secretWord = when(category) {
            Category.ANIMALS -> requireContext().resources.getStringArray(R.array.animals).random()
            Category.FOOD -> requireContext().resources.getStringArray(R.array.food).random()
            Category.METALS -> requireContext().resources.getStringArray(R.array.metals).random()
        }.uppercase()


        // Obscure the word/phrase
        val tempStr = StringBuilder()
        for(c in secretWord)
        {
            if(c.isWhitespace()) tempStr.append(" ") else tempStr.append("_")
        }
        shownWord = tempStr.toString()
        binding.secretWord.text = shownWord
    }

    fun resetGame() {
        // Reset game variables
        lives = 5
        score = 0
        selectedLetter = ' '

        // Update layout
        binding.livesLeft.text = lives.toString()
        binding.score.text = score.toString()
        binding.letterSelected.text = selectedLetter.toString()

    }

    enum class Category { ANIMALS, FOOD, METALS}


}