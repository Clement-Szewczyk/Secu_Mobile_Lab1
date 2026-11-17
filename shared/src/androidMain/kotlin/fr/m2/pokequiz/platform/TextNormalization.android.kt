package fr.m2.pokequiz.platform

import java.text.Normalizer

private val COMBINING_MARKS = Regex("\\p{Mn}+")

actual fun String.removeDiacritics(): String {
    val decomposed = Normalizer.normalize(this, Normalizer.Form.NFD)
    return decomposed.replace(COMBINING_MARKS, "")
}
