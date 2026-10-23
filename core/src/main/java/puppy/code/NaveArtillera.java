package puppy.code;
import com.badlogic.gdx.graphics.Texture;

//segunda subclase de naveenemiga(gm4)

public class NaveArtillera extends NaveEnemiga{
    public NaveArtillera(int x, int y, int xSpeed, int ySpeed, Texture tx){
        super(x, y, 56, 56, xSpeed, ySpeed, 2, 2.7f, 290f, tx, new DisparoTriple(16.0f));
        cambiarColor(0.75f, 0.35f, 1.0f);
    }

    @Override
    public int getPuntajeRecompensa(){
        return 35;
    }

    @Override
    public String getNombreTipo() {
        return "Nave Artillera";
    }

    @Override
    protected void alRecibirImpacto() {
        if(getPuntosVida() == 1){
            setIntervaloDisparo(getIntervaloDisparo() * 0.5f);
            cambiarColor(1.0f, 0.85f, 0.2f);
        }
    }
}
