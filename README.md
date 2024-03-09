# gridsquare

This is a small library to convert between the Amateur Radio [Maidenhead Locator System](https://en.wikipedia.org/wiki/Maidenhead_Locator_System) and latitude/longitude coordinates.

## Example Usage
```java
import com.five381.Gridsquare;
import com.five381.LatLong;

public class Example {
    public static void main(String [] args) {

        LatLong latlong = Gridsquare.toLatLong("CM87xh");
        System.out.printf("Coordinates: (%f, %f)\n", latlong.latitude, latlong.longitude);

        String gridsquare = Gridsquare.fromLatLong(37.313, -122.042);
        System.out.printf("Gridsquare: %s\n", gridsquare);
    }
}
```
