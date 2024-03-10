import io.github.memoryhole.GridSquare;
import io.github.memoryhole.LatLong;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GridSquareJavaTest {

    /*
    This is test demoing the API from Java to ensure the correct annotations are used
     */
    @Test
    public void verifyJavaInterface() {
        String gridsquare = GridSquare.fromLatLong(82.89, 117.12);
        assertEquals("OR82nv", gridsquare);
        LatLong latlong = GridSquare.toLatLong("CM87xh");
        assertEquals(latlong.latitude, 37.313);
        assertEquals(latlong.longitude, -122.042);
    }
}
