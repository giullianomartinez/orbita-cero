package puppy.code;
import com.badlogic.gdx.graphics.Texture;

public class NaveCaza extends NaveEnemiga {
    
    public NaveCaza(int x, int y, int xSpeed, int ySpeed, Texture tx){
        super(x, y, 42, 42, xSpeed, ySpeed, 1, 2.1f, 330f, tx, new DisparoSimple());
        cambiarColor(1f, 0.35f, 0.35f);
    }

    @Override
    public int getPuntajeRecompensa(){
        return 15;
    }

    @Override
    public String getNombreTipo() {
        return "Nave Caza";
    }

    @Override
    protected void alRecibirImpacto() {
        // se destruye al primer imapcto
    }
}
