package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/** Proyectil que conserva su origen y expone una colisión común para las naves. */
public final class Bullet implements Colisionable {
    private final float velocidadX;
    private final float velocidadY;
    private final boolean origenJugador;
    private final Sprite sprite;
    private boolean destruida;

    public Bullet(float centroX, float centroY, float velocidadX, float velocidadY,
                  Texture textura) {
        this(centroX, centroY, velocidadX, velocidadY, textura, true);
    }

    public Bullet(float centroX, float centroY, float velocidadX, float velocidadY,
                  Texture textura, boolean origenJugador) {
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        this.origenJugador = origenJugador;
        sprite = new Sprite(textura);
        sprite.setPosition(centroX - sprite.getWidth() / 2f,
            centroY - sprite.getHeight() / 2f);
        sprite.setOriginCenter();
        sprite.setRotation(MathUtils.atan2(velocidadY, velocidadX)
            * MathUtils.radiansToDegrees - 90f);
        if (!origenJugador) sprite.setColor(1f, 0.35f, 0.25f, 1f);
    }

    public void update(float delta) {
        if (destruida || !Float.isFinite(delta) || delta <= 0) return;
        sprite.setPosition(sprite.getX() + velocidadX * delta,
            sprite.getY() + velocidadY * delta);
        if (sprite.getX() < 0 || sprite.getX() + sprite.getWidth() > AreaJuego.ANCHO
                || sprite.getY() < 0
                || sprite.getY() + sprite.getHeight() > AreaJuego.ALTO) {
            destruir();
        }
    }

    public void draw(SpriteBatch batch) { if (!destruida) sprite.draw(batch); }

    public boolean checkCollision(Colisionable objetivo) {
        if (!colisionaCon(objetivo)) return false;
        destruir();
        return true;
    }

    public void destruir() { destruida = true; }
    public boolean isDestroyed() { return destruida; }
    public boolean esOrigenJugador() { return origenJugador; }

    @Override public Rectangle getArea() { return new Rectangle(sprite.getBoundingRectangle()); }
    @Override public boolean estaDestruido() { return destruida; }
}
