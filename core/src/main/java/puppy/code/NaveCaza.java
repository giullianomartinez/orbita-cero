package puppy.code;

import com.badlogic.gdx.graphics.Texture;

/** Nave ligera: rápida, con un punto de vida y disparo simple. */
public final class NaveCaza extends NaveEnemiga {
    public NaveCaza(int x, int y, int velocidadX, int velocidadY, Texture textura,
                    ServiciosNave servicios) {
        super(x, y, 42, 42, velocidadX, velocidadY, 1, 2.1f, 330f,
            textura, new DisparoSimple(), servicios);
        cambiarColor(1f, 0.35f, 0.35f);
    }

    @Override public int getPuntajeRecompensa() { return 15; }
    @Override public String getNombreTipo() { return "Nave Caza"; }
    @Override protected void alRecibirImpacto() { }
}
