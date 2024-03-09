import com.five381.Gridsquare
import com.five381.LatLong
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.test.assertFailsWith

class GridsquareTest {

    @Test
    fun invalidInput() {
        val scenarios = arrayOf("c9", "cm", "cmcm", "cmc9", "CM87xh1", "CM87xha", "CM98b.", "?M23ad", "")
        for (scenario in scenarios) {
            assertFailsWith<IllegalArgumentException>("Invalid input allowed: $scenario") {
                Gridsquare.toLatLong(scenario)
            }
        }
    }
    @Test
    fun toLatLongShort() {
        var latlong = Gridsquare.toLatLong("CM87")
        var expected = LatLong(37.5, -123.0)
        assertEquals(expected, latlong)

        latlong = Gridsquare.toLatLong("RB79")
        expected = LatLong(-70.5, 175.0)
        assertEquals(expected, latlong)
    }

    @Test
    fun toLatLongFull() {
        var latlong = Gridsquare.toLatLong("CM87xh")
        var expected = LatLong(37.313, -122.042)
        assertEquals(expected, latlong)

        latlong = Gridsquare.toLatLong("RB79de")
        expected = LatLong(-70.813, 174.292)
        assertEquals(expected, latlong)
    }

    @Test
    fun fromLatLong() {
        assertEquals("CM87xh", Gridsquare.fromLatLong(37.313, -122.042))
        assertEquals("RB79de", Gridsquare.fromLatLong(-70.813, 174.292))
    }
}