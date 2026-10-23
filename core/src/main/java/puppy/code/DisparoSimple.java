package puppy.code;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

//estrategia concreta, disparo normal (gm7)

public class DisparoSimple implements EstrategiaDisparo {
    @Override
    public List<Bullet> crearDisparos(float origenX, float origenY, float objetivoX, float objetivoY, float rapidezBala, Texture txBala, boolean origenJugador) {
        List<Bullet> disparos = new ArrayList<>();
        Vector2 velocidad = DireccionDisparo.velocidad(origenX, origenY, objetivoX, objetivoY, rapidezBala);

        if(velocidad.isZero()){
            return disparos;
        }

        disparos.add(new Bullet(origenX, origenY, velocidad.x, velocidad.y, txBala, origenJugador));

        return disparos;
    }

    @Override
    public String getNombreEstrategia() {
        return "Disparo Simple";
    }

}
