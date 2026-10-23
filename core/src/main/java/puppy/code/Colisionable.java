package puppy.code;
import com.badlogic.gdx.math.Rectangle;

//interfez del dominio, (gm4) que va a implementar naveenemiga, nave4 y bullet

public interface Colisionable {
    
    Rectangle getArea();

    boolean estaDestruido();

    default boolean colisionaCon(Colisionable otro){
        if(otro == null || this.estaDestruido() || otro.estaDestruido()){
            return false;
        }

        return this.getArea().overlaps(otro.getArea());
    }
}
