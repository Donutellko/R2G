package ga.patrick.r2g

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.tomakehurst.wiremock.matching.MatchResult
import com.github.tomakehurst.wiremock.matching.MemoizingStringValuePattern
import org.apache.commons.text.similarity.LevenshteinDistance
import kotlin.math.max

class GraphRequestMatcher(
        @JsonProperty("expectedValue")
        expected: String
) : MemoizingStringValuePattern(expected) {

    override fun calculateMatch(actual: String?): MatchResult {
        val actualPrepared = actual?.removeWhitespaces()
        val expectedPrepared = expected?.removeWhitespaces()

        return if (expectedPrepared == actualPrepared) {
            MatchResult.exactMatch()
        } else {
            MatchResult.partialMatch(normalisedLevenshteinDistance(expectedPrepared, actualPrepared))
        }
    }

    private fun normalisedLevenshteinDistance(one: String?, two: String?): Double {
        if (one == null || two == null) {
            return 1.0
        }
        val maxDistance = max(one.length, two.length)

        val actualDistance =LevenshteinDistance(0).apply(one, two).toDouble()
        return actualDistance / maxDistance
    }

    private fun String.removeWhitespaces() =
            replace(Regex("([\\s])|(\\\\n)"), "")
}