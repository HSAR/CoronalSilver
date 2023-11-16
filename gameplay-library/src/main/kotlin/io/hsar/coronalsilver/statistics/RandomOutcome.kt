package io.hsar.coronalsilver.statistics

import io.hsar.coronalsilver.statistics.Reroll.NONE
import io.hsar.coronalsilver.statistics.Reroll.ON_FAIL_ALL
import io.hsar.coronalsilver.statistics.Reroll.ON_FAIL_TENS
import kotlin.random.Random

class RandomOutcome(private val generator: NumberGenerator = RandomNumberGenerator) {

    /**
     * @param targetNumber target number, higher is harder
     * Returns the number of degrees of success, which is 1 plus 1 per ten exceeding the target number.
     * 0 degrees of success is a fail.
     */
    fun roll(targetNumber: Int, reroll: Reroll = NONE): Int = generator()
        .let { result ->
            return if (result >= targetNumber) {
                1 + additionalDegreesOfSuccess(targetNumber, result)
            } else {
                if (reroll == ON_FAIL_ALL || reroll == ON_FAIL_TENS && (result % 10 == 0)) {
                    roll(targetNumber, reroll = NONE) // rerolls do not chain
                } else {
                    0 // fail
                }
            }
        }

    private fun additionalDegreesOfSuccess(targetNumber: Int, result: Int) = (result - targetNumber) / 10

    companion object {
        val Default = RandomOutcome()
    }
}

fun roll(targetNumber: Int, reroll: Reroll = NONE) = RandomOutcome.Default.roll(targetNumber, reroll)

interface NumberGenerator {
    operator fun invoke(): Int
}

object RandomNumberGenerator : NumberGenerator {
    override fun invoke() = Random.nextInt(100)
}

/**
 * For test use only.
 */
class DeterministicNumberGenerator(private val results: List<Int>) : NumberGenerator {

    private var counter = 0

    override fun invoke(): Int {
        val index = counter++ % results.size
        return results[index]
    }
}