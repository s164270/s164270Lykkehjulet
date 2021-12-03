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
import kotlin.random.Random
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
    private var gameStage:GameStage = GameStage.SELECTCATEGORY
    private val guessedLetters: MutableList<Char> = mutableListOf()
    private var pointsOnTheWheel = 0

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
        binding.guessAndSpinButton.setOnClickListener { spinOrGuess() }

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

    fun spinOrGuess() {
        when(gameStage) {
            GameStage.SELECTCATEGORY -> return
            GameStage.SPIN -> spinTheWheel()
            GameStage.GUESS -> guessSelectedLetter()
        }
    }

    fun spinTheWheel() {
        val spinNumber = (1 .. 13).random()
        when {
            spinNumber < 11 -> {
                pointsOnTheWheel = 100 * spinNumber
                gameStage = GameStage.GUESS
                binding.onTheWheel.text = pointsOnTheWheel.toString()
                binding.guessAndSpinButton.text = getString(R.string.guess)
            }
            spinNumber == 11 -> {
                lives -= 1
                binding.livesLeft.text = lives.toString()
                binding.onTheWheel.text = getString(R.string.miss_turn)
            }
            spinNumber == 12 -> {
                lives += 1
                binding.livesLeft.text = lives.toString()
                binding.onTheWheel.text = getString(R.string.extra_turn)
            }
            spinNumber == 13 -> {
                score = 0
                binding.score.text = score.toString()
                binding.onTheWheel.text = getString(R.string.bankrupt)
            }
        }

        // Check for game over
        if(lives < 1) {
            val action = GuessingFragmentDirections.actionGuessingFragmentToGameLostFragment()
            requireView().findNavController().navigate(action)
        }
    }

    fun guessSelectedLetter() {
        if(selectedLetter == ' ' || guessedLetters.contains(selectedLetter))
        {
            Toast.makeText(context, R.string.alreadyGuessedThatLetter, Toast.LENGTH_SHORT).show()
            return
        }

        guessedLetters.add(selectedLetter)
        binding.lettersGuessed.text = guessedLetters.toString().substring(1, guessedLetters.toString().length - 1)

        val occurrences = secretWord.count { c -> c == selectedLetter }
        if(occurrences > 0) {
            score += pointsOnTheWheel * occurrences
            binding.score.text = score.toString()

            //Revealed the guessed letter
            val tempStr = StringBuilder()
            for (c in secretWord) {
                if (guessedLetters.contains(c) || c.isWhitespace()) tempStr.append(c) else tempStr.append("_")
            }
            shownWord = tempStr.toString()
            binding.secretWord.text = shownWord
        }
        else
        {
            lives -= 1
            binding.livesLeft.text = lives.toString()
        }

        // Change the stage back to spin
        gameStage = GameStage.SPIN
        // Reset the wheel
        pointsOnTheWheel = 0
        binding.onTheWheel.text = pointsOnTheWheel.toString()

        //
        binding.guessAndSpinButton.text = getString(R.string.spin)

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

        gameStage = GameStage.SPIN
        binding.guessAndSpinButton.text = getString(R.string.spin)
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
    enum class GameStage {SELECTCATEGORY, SPIN, GUESS}

}