package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Bullet implements Colisionable {
    private float xSpeed;
    private float ySpeed;
    private boolean destroyed = false;
    private final boolean origenJugador;
    private Sprite spr;

    public Bullet(float centroX, float centroY, float xSpeed, float ySpeed, Texture tx) {
        this(centroX, centroY, xSpeed, ySpeed, tx, true);
    }

    public Bullet(float centroX, float centroY, float xSpeed, float ySpeed, Texture tx, boolean origenJugador) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.origenJugador = origenJugador;
        spr = new Sprite(tx);
        spr.setPosition(centroX - spr.getWidth() / 2f, centroY - spr.getHeight() / 2f);
        spr.setOriginCenter();
        spr.setRotation(MathUtils.atan2(ySpeed, xSpeed) * MathUtils.radiansToDegrees - 90f);
        if (!origenJugador) {
            spr.setColor(1.0f, 0.35f, 0.25f, 1.0f);
        }
    }

    public void update(float delta) {
        spr.setPosition(spr.getX() + xSpeed * delta, spr.getY() + ySpeed * delta);
        if (spr.getX() < 0 || spr.getX() + spr.getWidth() > AreaJuego.ANCHO) {
            destroyed = true;
        }
        if (spr.getY() < 0 || spr.getY() + spr.getHeight() > AreaJuego.ALTO) {
            destroyed = true;
        }
    }

    public void draw(SpriteBatch batch) {
        spr.draw(batch);
    }

    public boolean checkCollision(Colisionable objetivo) {
        if (this.colisionaCon(objetivo)) {
            this.destroyed = true;
            return true;
        }
        return false;
    }

    @Override
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    @Override
    public boolean estaDestruido() {
        return destroyed;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean esOrigenJugador() {
        return origenJugador;
    }
}
