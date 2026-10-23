package puppy.code;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ColisionAsteroidesTest {
    @Test
    public void separaLosAsteroidesEnElEjeDeMenorSolapamiento() {
        Rectangle primero = new Rectangle(100, 100, 50, 50);
        Rectangle segundo = new Rectangle(140, 105, 50, 50);

        Vector2 desplazamiento = ColisionAsteroides.desplazamiento(primero, segundo);
        primero.x += desplazamiento.x;
        primero.y += desplazamiento.y;
        segundo.x -= desplazamiento.x;
        segundo.y -= desplazamiento.y;

        assertFalse(primero.overlaps(segundo));
        assertTrue(desplazamiento.x < 0);
    }

    @Test
    public void noMueveAsteroidesQueNoEstanEnContacto() {
        Vector2 desplazamiento = ColisionAsteroides.desplazamiento(
            new Rectangle(0, 0, 20, 20), new Rectangle(30, 0, 20, 20)
        );

        assertTrue(desplazamiento.isZero());
    }

    @Test
    public void separaAsteroidesSolapadosJuntoAlBordeSinSacarlosDelArea() {
        Rectangle primero = new Rectangle(0, 100, 50, 50);
        Rectangle segundo = new Rectangle(40, 100, 50, 50);

        Vector2[] ajustes = ColisionAsteroides.separaciones(primero, segundo, 300, 300);
        primero.x += ajustes[0].x;
        segundo.x += ajustes[1].x;

        assertFalse(primero.overlaps(segundo));
        assertTrue(primero.x >= 0);
        assertTrue(segundo.x + segundo.width <= 300);
    }

    @Test
    public void reparteLaSeparacionSiUnAsteroideEstaCercaDelBorde() {
        Rectangle primero = new Rectangle(245, 100, 50, 50);
        Rectangle segundo = new Rectangle(205, 100, 50, 50);

        Vector2[] ajustes = ColisionAsteroides.separaciones(primero, segundo, 300, 300);
        primero.x += ajustes[0].x;
        segundo.x += ajustes[1].x;

        assertFalse(primero.overlaps(segundo));
        assertTrue(primero.x + primero.width <= 300);
        assertTrue(segundo.x >= 0);
    }

    @Test
    public void intercambiaVelocidadesCuandoLosAsteroidesSeAcercan() {
        int[] velocidades = ColisionAsteroides.velocidadesTrasChoque(10, -10, -1);

        assertTrue(velocidades[0] == -10);
        assertTrue(velocidades[1] == 10);
    }

    @Test
    public void conservaVelocidadesCuandoLosAsteroidesSeAlejan() {
        int[] velocidades = ColisionAsteroides.velocidadesTrasChoque(-10, 10, -1);

        assertTrue(velocidades[0] == -10);
        assertTrue(velocidades[1] == 10);
    }
}
