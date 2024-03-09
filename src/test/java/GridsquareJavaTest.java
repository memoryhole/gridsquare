import com.five381.Gridsquare;
import com.five381.LatLong;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GridsquareJavaTest {

    /*
    This is test demoing the API from Java to ensure the correct annotations are used
     */
    @Test
    public void verifyJavaInterface() {
        String gridsquare = Gridsquare.fromLatLong(82.89, 117.12);
        assertEquals("OR82nv", gridsquare);
        LatLong latlong = Gridsquare.toLatLong("CM87xh");
        assertEquals(latlong.latitude, 37.313);
        assertEquals(latlong.longitude, -122.042);
    }
}
