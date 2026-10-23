package puppy.code;

import java.util.List;
import com.badlogic.gdx.graphics.Texture;

public interface EstrategiaDisparo {
    //definimos el contrato para distintos tipos de disparos (gm7)

    List<Bullet> crearDisparos(float origenX, float origenY, float objetivoX, float objetivoY, float rapidezBala, Texture txBala, boolean origenJugador);

    String getNombreEstrategia();
}
