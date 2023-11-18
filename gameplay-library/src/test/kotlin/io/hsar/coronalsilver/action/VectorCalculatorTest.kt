package io.hsar.coronalsilver.action

import io.hsar.coronalsilver.action.VectorCalculator.toOctant
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.closeTo
import org.junit.jupiter.api.Test

class VectorCalculatorTest {

    lateinit var origin: Vector
    lateinit var target: Vector

    @Test
    fun `relative bearing works when objective bearing is 0`() {
        setOrigin(x = 0.0, y = 1.0)
        setTarget(x = 0.0, y = 0.0, bearing = 0.0)

        val result = VectorCalculator.findRelativeBearing(origin, target)

        assertThat(result, equalTo(0.0))
    }

    @Test
    fun `relative bearing works when target bearing is 0`() {
        setOrigin(x = 0.0, y = 0.0)
        setTarget(x = 0.0, y = 1.0, bearing = 0.0)

        val result = VectorCalculator.findRelativeBearing(origin, target)

        assertThat(result, equalTo(180.0))
    }

    @Test
    fun `relative bearing works in a simple case`() {
        setOrigin(x = 0.0, y = 0.0)
        setTarget(x = 1.0, y = 1.0, bearing = 90.0)

        val result = VectorCalculator.findRelativeBearing(origin, target)

        assertThat(result, equalTo(135.0))
    }

    @Test
    fun `relative bearing works when neither vector is the origin`() {
        setOrigin(x = 1.0, y = 2.0)
        setTarget(x = 3.0, y = 6.0, bearing = 45.0)

        val result = VectorCalculator.findRelativeBearing(origin, target)

        assertThat(result, closeTo(161.565, 0.1))
    }

    @Test
    fun `octants work`() {
        assertThat(0.0.toOctant(), equalTo(Octant.FRONT))
    }

    fun setOrigin(x: Double, y: Double) {
        origin = Vector(x, y, 0.0, 0.0)
    }

    fun setTarget(x: Double, y: Double, bearing: Double) {
        target = Vector(x, y, 0.0, bearing)
    }
}