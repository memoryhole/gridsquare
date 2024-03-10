package io.github.memoryhole

import java.lang.Integer.parseInt
import java.math.RoundingMode
import kotlin.math.roundToInt

/*
The GridSquare from the Maidenhead Locator System interleaves latitude and longitude values relative
to the antimeridian (180 degrees from the prime meridian), and the South Pole (90 degrees below the equator).

Example: CM87xh
longitude values = (C, 8, x)
latitude values = (M, 7, h)

The first longitude value represents increments of 20 degrees.
The second longitude value represents increments of 2 degrees.
The third longitude value represents increments of 5 minutes (0.0833333 degrees)

The first latitude value represents increments of 10 degrees.
The second latitude value represents increments of 1 degrees.
The third latitude value represents increments of 2.5 minutes (0.0416667 degrees).

References:
https://www.johndcook.com/blog/2022/05/21/decoding-a-grid-square/#:~:text=The%20two%20digits%20in%20the,multiples%20of%201°%20latitude.

 */
data class LatLong(@JvmField val latitude: Double, @JvmField val longitude: Double)

private val LONG_OFFSET : Double = -180.0
private val LAT_OFFSET: Double = -90.0
private val CHARS = ('A'..'Z').toList()
private val NUMS = (0..9).toList()

class Gridsquare {
    companion object {
        @JvmStatic
        fun fromLatLong(latitude: Double, longitude: Double) : String {

            if (!(-180 < longitude && longitude < 180.0)) {
                throw IllegalArgumentException("longitude must be between -180 and 180 degrees")
            }

            if (!(-90 < latitude && latitude < 90.0)) {
                throw IllegalArgumentException("latitude must be between -90 and 90 degrees")
            }

            var gridsquare = ""

            val longFromAntimeridian = (-LONG_OFFSET) + longitude
            val latFromSouthPole = (-LAT_OFFSET) + latitude

            val firstLongitudeIndex = longFromAntimeridian.roundToInt() / 20
            val firstLatitudeIndex = latFromSouthPole.roundToInt() / 10

            val secondLongitudeIndex = (longFromAntimeridian - (20 * firstLongitudeIndex)).toInt() / 2
            val secondLatitudeIndex = (latFromSouthPole - (10 * firstLatitudeIndex)).toInt()

            val thirdLongitudeIndex = (((longFromAntimeridian - (20 * firstLongitudeIndex) - (2 * secondLongitudeIndex))) / (5 / 60.0)).toInt()
            val thirdLatitudeIndex = ((((latFromSouthPole - (10 * firstLatitudeIndex)) - secondLatitudeIndex)) / (2.5 / 60.0)).toInt()

            gridsquare += CHARS[firstLongitudeIndex]
            gridsquare += CHARS[firstLatitudeIndex]
            gridsquare += NUMS[secondLongitudeIndex]
            gridsquare += NUMS[secondLatitudeIndex]
            gridsquare += CHARS[thirdLongitudeIndex].lowercaseChar()
            gridsquare += CHARS[thirdLatitudeIndex].lowercaseChar()

            return gridsquare
        }
        @JvmStatic
        fun toLatLong(gridsquare: String): LatLong {
            @Suppress("NAME_SHADOWING") val gridsquare = gridsquare.uppercase()

            if (!gridsquare.matches(Regex("""^[A-Z]{2}\d{2}([A-Z]{2})?"""))) {
                throw IllegalArgumentException("The gridsquare must have 4 or 6 characters. 2 letters, 2 numbers, and optionally 2 letters.")
            }

            val fieldLongitudeDegrees = (gridsquare[0].code - 'A'.code) * 20.0
            val fieldLatitudeDegrees = (gridsquare[1].code - 'A'.code) * 10.0
            val squareLongitudeDegrees = (parseInt(gridsquare.slice(2..2))) * 2.0
            val squareLatitudeDegrees = parseInt(gridsquare.slice(3..3))

            var longitude = LONG_OFFSET + fieldLongitudeDegrees + squareLongitudeDegrees
            var latitude = LAT_OFFSET + fieldLatitudeDegrees + squareLatitudeDegrees

            if (gridsquare.length == 4) {
                longitude += 1.0 // add 1 to get the center-point
                latitude += 0.5 // add 0.5 to get the center-point
            } else {
                val subsquareLongitudeDegrees = (gridsquare[4].code - 'A'.code) * (5.0 / 60.0)
                val subsquareLatitudeDegrees = (gridsquare[5].code - 'A'.code) * (2.5 / 60.0)

                longitude += subsquareLongitudeDegrees
                latitude += subsquareLatitudeDegrees

                longitude += 0.5 * (5.0/60)
                latitude += 0.5 * (2.5/60)
            }

            return LatLong(latitude.toBigDecimal().setScale(3, RoundingMode.UP).toDouble(), longitude.toBigDecimal().setScale(3, RoundingMode.UP).toDouble())
        }
    }
}