package puppy.code;

import com.badlogic.gdx.math.Vector2;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MovimientoNaveTest {
    @Test
    public void mantieneVelocidadAlMoverEnDiagonal() {
        Vector2 posicion = MovimientoNave.posicionSiguiente(200, 200, 1, 1, 1, 45, 45);

        assertEquals(MovimientoNave.VELOCIDAD, posicion.dst(200, 200), 0.01f);
    }

    @Test
    public void limitaLaNaveDentroDeTodoElEspacioLogico() {
        Vector2 posicion = MovimientoNave.posicionSiguiente(1190, 790, 1, 1, 1, 45, 45);

        assertEquals(AreaJuego.ANCHO - 45, posicion.x, 0.001f);
        assertEquals(AreaJuego.ALTO - 45, posicion.y, 0.001f);
    }

    @Test
    public void seDetieneAlSoltarLaTeclaSinDeslizamiento() {
        Vector2 despuesDePulsar = MovimientoNave.posicionSiguiente(
            200, 200, 1, 0, 0.1f, 45, 45
        );
        Vector2 despuesDeSoltar = MovimientoNave.posicionSiguiente(
            despuesDePulsar.x, despuesDePulsar.y, 0, 0, 0.1f, 45, 45
        );

        assertEquals(despuesDePulsar.x, despuesDeSoltar.x, 0.001f);
        assertEquals(despuesDePulsar.y, despuesDeSoltar.y, 0.001f);
    }
}
