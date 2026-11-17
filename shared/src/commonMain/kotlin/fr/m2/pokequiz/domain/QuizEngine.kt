package fr.m2.pokequiz.domain

import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO

class QuizEngine(private val pokemons: List<Pokemon>) {
    init {
        require(pokemons.isNotEmpty()) { "Une partie nécessite au moins un Pokémon." }
    }

    private val _results = mutableListOf<QuestionResult>()

    val results: List<QuestionResult>
        get() = _results.toList()

    val total: Int
        get() = pokemons.size

    val currentIndex: Int
        get() = _results.size

    val current: Pokemon?
        get() = if (isFinished) null else pokemons[currentIndex]

    val isFinished: Boolean
        get() = _results.size >= pokemons.size

    val totalPoints: Int
        get() = _results.sumOf { it.points }

    val totalTime: Duration
        get() = _results.fold(ZERO) { acc, result -> acc + result.elapsed }

    fun submit(answer: String, hintUsed: Boolean, elapsed: Duration): QuestionResult {
        val pokemon = current ?: throw IllegalStateException("La partie est déjà terminée.")

        val correct = AnswerChecker.isCorrect(answer, pokemon.nameFr) ||
            AnswerChecker.isCorrect(answer, pokemon.nameEn)
        val points = ScoreCalculator.points(correct, hintUsed, elapsed)
        val result = QuestionResult(
            pokemon = pokemon,
            answer = answer,
            correct = correct,
            hintUsed = hintUsed,
            elapsed = elapsed,
            points = points,
        )

        _results.add(result)
        return result
    }
}
