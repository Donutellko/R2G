package ga.patrick.r2g

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.tomakehurst.wiremock.matching.MatchResult
import com.github.tomakehurst.wiremock.matching.MemoizingStringValuePattern
import org.apache.commons.lang3.StringUtils
import org.apache.commons.text.similarity.LevenshteinDistance
import kotlin.math.max

class GraphRequestMatcher(
        @JsonProperty("expectedValue")
        expectedValue: String
) : MemoizingStringValuePattern(expectedValue) {

    override fun calculateMatch(request: String?): MatchResult {
        val actual = request?.removeWhitespaces()

        return if (expected == actual) {
            MatchResult.exactMatch()
        } else {
            MatchResult.partialMatch(normalisedLevenshteinDistance(expected, actual))
        }
    }

    override fun getExpected() = expectedValue?.removeWhitespaces()

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