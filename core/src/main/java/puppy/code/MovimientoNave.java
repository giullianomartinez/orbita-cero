package puppy.code;

import com.badlogic.gdx.math.Vector2;

/** Cálculos puros del desplazamiento de la nave, separados del render. */
public final class MovimientoNave {
    public static final float VELOCIDAD = 245;

    private MovimientoNave() {
    }

    public static Vector2 posicionSiguiente(float x, float y, float horizontal, float vertical,
                                            float delta, float anchoNave, float altoNave) {
        float magnitud = (float) Math.sqrt(horizontal * horizontal + vertical * vertical);
        if (magnitud > 1) {
            horizontal /= magnitud;
            vertical /= magnitud;
        }

        float maxX = AreaJuego.ANCHO - anchoNave;
        float maxY = AreaJuego.ALTO - altoNave;
        return new Vector2(
            limitar(x + horizontal * VELOCIDAD * delta, 0, maxX),
            limitar(y + vertical * VELOCIDAD * delta, 0, maxY)
        );
    }

    private static float limitar(float valor, float minimo, float maximo) {
        return Math.max(minimo, Math.min(maximo, valor));
    }
}
