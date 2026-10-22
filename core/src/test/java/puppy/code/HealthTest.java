package puppy.code;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HealthTest {
    @Test
    public void ultimaVidaSePierdeAunqueComienceLaProteccion() {
        Health health = new Health(1);

        assertTrue(health.hit());
        assertTrue(health.isProtected());
        assertTrue(health.isDestroyed());
        assertFalse(health.hit());
    }
}
