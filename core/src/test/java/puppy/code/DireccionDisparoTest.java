package puppy.code;

import com.badlogic.gdx.math.Vector2;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DireccionDisparoTest {
    @Test
    public void apuntaEnLasCuatroDireccionesCardinales() {
        assertEquals(360, DireccionDisparo.velocidad(0, 0, 10, 0, 360).x, 0.001f);
        assertEquals(-360, DireccionDisparo.velocidad(0, 0, -10, 0, 360).x, 0.001f);
        assertEquals(360, DireccionDisparo.velocidad(0, 0, 0, 10, 360).y, 0.001f);
        assertEquals(-360, DireccionDisparo.velocidad(0, 0, 0, -10, 360).y, 0.001f);
    }

    @Test
    public void conservaLaRapidezAlApuntarEnDiagonalYEvitaDividirPorCero() {
        Vector2 diagonal = DireccionDisparo.velocidad(0, 0, 10, 10, 360);
        Vector2 sinObjetivo = DireccionDisparo.velocidad(10, 10, 10, 10, 360);

        assertEquals(360, diagonal.len(), 0.001f);
        assertEquals(0, sinObjetivo.len(), 0.001f);
    }

    @Test
    public void rotaLaNaveHaciaElPunteroConSuFrenteOriginalHaciaArriba() {
        assertEquals(0, DireccionDisparo.anguloNave(0, 0, 0, 10), 0.001f);
        assertEquals(-90, DireccionDisparo.anguloNave(0, 0, 10, 0), 0.001f);
        assertEquals(90, DireccionDisparo.anguloNave(0, 0, -10, 0), 0.001f);
        assertEquals(-180, DireccionDisparo.anguloNave(0, 0, 0, -10), 0.001f);
    }
}
