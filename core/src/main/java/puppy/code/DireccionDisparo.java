package puppy.code;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

/** Calcula la velocidad de un proyectil hacia una coordenada objetivo. */
public final class DireccionDisparo {
    private DireccionDisparo() {
    }

    public static Vector2 velocidad(float origenX, float origenY,
                                    float objetivoX, float objetivoY, float rapidez) {
        Vector2 direccion = new Vector2(objetivoX - origenX, objetivoY - origenY);
        if (direccion.isZero()) return direccion;
        return direccion.nor().scl(rapidez);
    }

    /** El PNG de la nave mira hacia arriba cuando la rotación es cero. */
    public static float anguloNave(float origenX, float origenY,
                                  float objetivoX, float objetivoY) {
        return MathUtils.atan2(objetivoY - origenY, objetivoX - origenX)
            * MathUtils.radiansToDegrees - 90;
    }
}
