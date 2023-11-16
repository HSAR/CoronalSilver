package io.hsar.coronalsilver.statistics

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class RandomOutcomeTest {

    lateinit var objectUnderTest: RandomOutcome

    @Test
    fun `returns 0 on fail`() {
        setFailedRoll()

        val result = objectUnderTest.roll(50, reroll = Reroll.NONE)

        assertThat(result, equalTo(0))
    }

    @Test
    fun `returns 0 on fail with reroll not hit`() {
        setFailedRoll(7)

        val result = objectUnderTest.roll(50, reroll = Reroll.ON_FAIL_TENS)

        assertThat(result, equalTo(0))
    }

    @Test
    fun `returns 0 on fail rerolling all into fail`() {
        setFailedRoll()

        val result = objectUnderTest.roll(50, reroll = Reroll.ON_FAIL_ALL)

        assertThat(result, equalTo(0))
    }

    @Test
    fun `returns 0 on fail rerolling tens into fail`() {
        setFailedRoll()

        val result = objectUnderTest.roll(50, reroll = Reroll.ON_FAIL_TENS)

        assertThat(result, equalTo(0))
    }

    @Test
    fun `returns correctly on fail rerolling all into success`() {
        setResults(7, 99)

        val result = objectUnderTest.roll(89, reroll = Reroll.ON_FAIL_ALL)

        assertThat(result, equalTo(2))
    }

    @Test
    fun `returns correctly on fail rerolling tens into success`() {
        setResults(40, 80)

        val result = objectUnderTest.roll(50, reroll = Reroll.ON_FAIL_TENS)

        assertThat(result, equalTo(5))
    }

    @Test
    fun `returns correctly on success`() {
        setSuccessfulRoll()

        val result = objectUnderTest.roll(99)

        assertThat(result, equalTo(1))
    }


    private fun setSuccessfulRoll(result: Int = 99) = setFailedRoll(result)

    private fun setFailedRoll(result: Int = 0) {
        setResults(*listOf(result).toIntArray())
    }

    private fun setResults(vararg results: Int) {
        objectUnderTest = RandomOutcome(DeterministicNumberGenerator(results.toList()))
    }

}