package puppy.code;

import com.badlogic.gdx.graphics.Texture;

/** Nave resistente: dispara en abanico y se vuelve más agresiva al ser dañada. */
public final class NaveArtillera extends NaveEnemiga {
    public NaveArtillera(int x, int y, int velocidadX, int velocidadY,
                         Texture textura, ServiciosNave servicios) {
        super(x, y, 56, 56, velocidadX, velocidadY, 2, 2.7f, 290f,
            textura, new DisparoTriple(16f), servicios);
        cambiarColor(0.75f, 0.35f, 1f);
    }

    @Override public int getPuntajeRecompensa() { return 35; }
    @Override public String getNombreTipo() { return "Nave Artillera"; }

    @Override
    protected void alRecibirImpacto() {
        if (getPuntosVida() == 1) {
            reducirIntervaloDisparo(0.5f);
            cambiarColor(1f, 0.85f, 0.2f);
        }
    }
}
