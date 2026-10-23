package puppy.code;

import java.util.List;

/** Servicios externos que las naves necesitan para producir disparos y sonidos. */
public interface ServiciosNave {
    List<Bullet> crearDisparos(float origenX, float origenY, float objetivoX,
        float objetivoY, float rapidez, EstrategiaDisparo estrategia,
        boolean origenJugador);
    void reproducirDisparo();
    void reproducirHerida();
}
