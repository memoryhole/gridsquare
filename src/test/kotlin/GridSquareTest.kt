import io.github.memoryhole.GridSquare
import io.github.memoryhole.LatLong
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.test.assertFailsWith

class GridSquareTest {

    @Test
    fun invalidGridsquareInput() {
        val scenarios = arrayOf("c9", "cm", "cmcm", "cmc9", "CM87xh1", "CM87xha", "CM98b.", "?M23ad", "")
        for (scenario in scenarios) {
            assertFailsWith<IllegalArgumentException>("Invalid input allowed: $scenario") {
                GridSquare.toLatLong(scenario)
            }
        }
    }
    @Test
    fun toLatLongShort() {
        var latlong = GridSquare.toLatLong("CM87")
        var expected = LatLong(37.5, -123.0)
        assertEquals(expected, latlong)

        latlong = GridSquare.toLatLong("RB79")
        expected = LatLong(-70.5, 175.0)
        assertEquals(expected, latlong)
    }

    @Test
    fun toLatLongFull() {
        var latlong = GridSquare.toLatLong("CM87xh")
        var expected = LatLong(37.313, -122.042)
        assertEquals(expected, latlong)

        latlong = GridSquare.toLatLong("RB79de")
        expected = LatLong(-70.813, 174.292)
        assertEquals(expected, latlong)
    }

    @Test
    fun invalidLatLongInput() {
        val scenarios = arrayOf(Pair(190.0, 87.7), Pair(100.0, 98.7), Pair(-190.0, 10.0), Pair(170.987, -108.34))
        for (scenario in scenarios) {
            assertFailsWith<IllegalArgumentException>("Invalid input allowed: $scenario") {
                GridSquare.fromLatLong(scenario.second, scenario.first)
            }
        }
    }
    @Test
    fun fromLatLong() {
        assertEquals("CM87xh", GridSquare.fromLatLong(37.313, -122.042))
        assertEquals("RB79de", GridSquare.fromLatLong(-70.813, 174.292))
    }
}