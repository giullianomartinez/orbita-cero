package puppy.code;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/** Calcula cuánto separar dos rectángulos solapados para que no queden trabados. */
public final class ColisionAsteroides {
    private ColisionAsteroides() {
    }

    public static Vector2 desplazamiento(Rectangle a, Rectangle b) {
        if (!a.overlaps(b)) return new Vector2();

        float solapamientoX = Math.min(a.x + a.width, b.x + b.width)
            - Math.max(a.x, b.x);
        float solapamientoY = Math.min(a.y + a.height, b.y + b.height)
            - Math.max(a.y, b.y);
        float centroAX = a.x + a.width / 2;
        float centroAY = a.y + a.height / 2;
        float centroBX = b.x + b.width / 2;
        float centroBY = b.y + b.height / 2;

        if (solapamientoX < solapamientoY) {
            float direccion = centroAX < centroBX ? -1 : 1;
            return new Vector2(direccion * (solapamientoX + 1) / 2, 0);
        }
        float direccion = centroAY < centroBY ? -1 : 1;
        return new Vector2(0, direccion * (solapamientoY + 1) / 2);
    }

    /** Calcula el movimiento de cada rectángulo y conserva ambos dentro del área. */
    public static Vector2[] separaciones(Rectangle a, Rectangle b,
                                         float anchoArea, float altoArea) {
        Vector2 mitad = desplazamiento(a, b);
        if (mitad.isZero()) return new Vector2[] { new Vector2(), new Vector2() };

        boolean ejeX = mitad.x != 0;
        float total = Math.abs(ejeX ? mitad.x : mitad.y) * 2;
        float direccionA = Math.signum(ejeX ? mitad.x : mitad.y);
        float direccionB = -direccionA;
        float inicioA = ejeX ? a.x : a.y;
        float inicioB = ejeX ? b.x : b.y;
        float tamA = ejeX ? a.width : a.height;
        float tamB = ejeX ? b.width : b.height;
        float limite = ejeX ? anchoArea : altoArea;

        float disponibleA = direccionA < 0 ? inicioA : limite - inicioA - tamA;
        float disponibleB = direccionB < 0 ? inicioB : limite - inicioB - tamB;
        disponibleA = Math.max(0, disponibleA);
        disponibleB = Math.max(0, disponibleB);

        float moverA = Math.min(total / 2, disponibleA);
        float moverB = Math.min(total - moverA, disponibleB);
        float restante = total - moverA - moverB;
        float adicionalA = Math.min(restante, disponibleA - moverA);
        moverA += adicionalA;
        restante -= adicionalA;
        moverB += Math.min(restante, disponibleB - moverB);

        Vector2 ajusteA = ejeX ? new Vector2(direccionA * moverA, 0)
                               : new Vector2(0, direccionA * moverA);
        Vector2 ajusteB = ejeX ? new Vector2(direccionB * moverB, 0)
                               : new Vector2(0, direccionB * moverB);
        return new Vector2[] { ajusteA, ajusteB };
    }

    /** Devuelve el intercambio de velocidades solo si se aproximan en el eje del choque. */
    public static int[] velocidadesTrasChoque(int velocidadA, int velocidadB,
                                              float direccionSeparacion) {
        if ((velocidadA - velocidadB) * direccionSeparacion < 0) {
            return new int[] { velocidadB, velocidadA };
        }
        return new int[] { velocidadA, velocidadB };
    }
}
