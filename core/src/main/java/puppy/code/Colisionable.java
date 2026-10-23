package puppy.code;
import com.badlogic.gdx.math.Rectangle;

/** Contrato común para las entidades que participan en colisiones. */

public interface Colisionable {

    Rectangle getArea();

    boolean estaDestruido();

    default boolean colisionaCon(Colisionable otro) {
        if (otro == null || estaDestruido() || otro.estaDestruido()) {
            return false;
        }

        return this.getArea().overlaps(otro.getArea());
    }
}
