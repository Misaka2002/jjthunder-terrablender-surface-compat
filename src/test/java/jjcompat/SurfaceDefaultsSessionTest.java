package jjcompat;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SurfaceDefaultsSessionTest {
    @Test void missingMarkerAndWrongHeightAreRejected() {
        assertFalse(SurfaceDefaultsSession.matchesOverworld(false, -64, 2096));
        assertFalse(SurfaceDefaultsSession.matchesOverworld(true, -64, 384));
        assertFalse(SurfaceDefaultsSession.matchesOverworld(true, 0, 2096));
        assertTrue(SurfaceDefaultsSession.matchesOverworld(true, -64, 2096));
        assertFalse(SurfaceDefaultsSession.matchesNether(0, 256));
        assertFalse(SurfaceDefaultsSession.matchesNether(-64, 2032));
        assertTrue(SurfaceDefaultsSession.matchesNether(0, 2032));
    }

    @Test void stopRestoresOnlyChangedCategoriesAndIsIdempotent() {
        var values = new HashMap<>(Map.of("overworld", "original", "nether", "nether-original", "end", "end-original"));
        var session = new SurfaceDefaultsSession<String, String>(values::get, values::put);
        session.replace("overworld", "pack");
        session.restore();
        assertEquals(Map.of("overworld", "original", "nether", "nether-original", "end", "end-original"), values);
        values.put("overworld", "later-mod-value");
        session.restore();
        assertEquals("later-mod-value", values.get("overworld"));
    }

    @Test void capturesNullRatherThanTreatingItAsInactive() {
        var values = new HashMap<String, String>();
        values.put("overworld", null);
        var session = new SurfaceDefaultsSession<String, String>(values::get, values::put);
        session.replace("overworld", "pack");
        session.replace("overworld", "second-replacement");
        session.restore();
        assertTrue(values.containsKey("overworld"));
        assertNull(values.get("overworld"));
    }

    @Test void consecutiveWorldsRecoverEvenIfPreviousStopWasMissed() {
        var values = new HashMap<>(Map.of("overworld", "original", "nether", "nether-original"));
        var session = new SurfaceDefaultsSession<String, String>(values::get, values::put);
        session.replace("overworld", "world-one");
        session.replace("nether", "nether-one");
        session.restore(); // start of world two, even without a previous stop event
        assertEquals("nether-original", values.get("nether"));
        session.replace("overworld", "world-two");
        session.restore();
        assertEquals("original", values.get("overworld"));
        session.restore(); // entering an unmarked world makes no replacements
        assertEquals("original", values.get("overworld"));
    }
}
