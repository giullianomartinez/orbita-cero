package puppy.code;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EstadoPartidaTest {
    @Test
    public void siguienteRondaConservaProgresoYRespetaLimites() {
        EstadoPartida estado = EstadoPartida.nuevaPartida();
        assertEquals(1, estado.ronda());
        assertEquals(3, estado.vidas());
        assertEquals(10, estado.navesEnemigas());

        EstadoPartida siguiente = estado.siguienteRonda(2, 150);
        assertEquals(2, siguiente.ronda());
        assertEquals(2, siguiente.vidas());
        assertEquals(150, siguiente.puntaje());
        assertEquals(12, siguiente.navesEnemigas());

        for (int i = 0; i < 10; i++) siguiente = siguiente.siguienteRonda(2, 150);
        assertEquals(4, siguiente.velocidadX());
        assertEquals(4, siguiente.velocidadY());
        assertEquals(32, siguiente.navesEnemigas());
    }
}
