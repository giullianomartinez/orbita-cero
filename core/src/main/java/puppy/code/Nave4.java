package puppy.code;

import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import puppy.code.input.ControlesNave;
import puppy.code.input.EntradaNave;

public class Nave4 implements Colisionable {
    private final Health health;
    private final ControlesNave controles;
    private static final float INTERVALO_DISPARO = 0.22f;
    private static final float RAPIDEZ_BALA = 640f;
    private float tiempoDisparo;
    private Sprite spr;
    private Sound sonidoHerido;
    private Sound soundBala;
    private Texture txBala;
    private EstrategiaDisparo estrategiaDisparo;

    public Nave4(int x, int y, Texture tx, Sound soundChoque, Texture txBala, Sound soundBala, int vidasIniciales, ControlesNave controles) {
        health = new Health(vidasIniciales);
        this.controles = controles;
        this.estrategiaDisparo = new DisparoSimple();
        sonidoHerido = soundChoque;
        this.soundBala = soundBala;
        this.txBala = txBala;
        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setBounds(x, y, 45, 45);
        spr.setOriginCenter();
    }

    public void update(float delta, PantallaJuego juego) {
        EntradaNave entrada = controles.leer();
        tiempoDisparo = Math.max(0, tiempoDisparo - delta);
        Vector2 posicion = MovimientoNave.posicionSiguiente(spr.getX(), spr.getY(), entrada.horizontal(), entrada.vertical(), delta, spr.getWidth(), spr.getHeight());
        spr.setPosition(posicion.x, posicion.y);
        health.update(delta);
        float origenX = getCentroX();
        float origenY = getCentroY();
        if (entrada.apuntarX() != origenX || entrada.apuntarY() != origenY) {
            spr.setRotation(DireccionDisparo.anguloNave(origenX, origenY, entrada.apuntarX(), entrada.apuntarY()));
        }
        if (entrada.disparar() && tiempoDisparo == 0) {
            List<Bullet> disparos = estrategiaDisparo.crearDisparos(origenX, origenY, entrada.apuntarX(), entrada.apuntarY(), RAPIDEZ_BALA, txBala, true);
            if (disparos.isEmpty()) {
                return;
            }
            for (Bullet bala : disparos) {
                juego.agregarBala(bala);
            }
            soundBala.play();
            tiempoDisparo = INTERVALO_DISPARO;
        }
    }

    public void draw(SpriteBatch batch) {
        spr.setAlpha(health.isProtected() ? 0.5f : 1f);
        spr.draw(batch);
        spr.setAlpha(1f);
    }

    public void dispose() {
        sonidoHerido.dispose();
        soundBala.dispose();
        txBala.dispose();
        spr.getTexture().dispose();
    }

    public boolean checkCollision(NaveEnemiga enemigo) {
        if (!this.colisionaCon(enemigo) || !health.hit()) {
            return false;
        }
        enemigo.setXSpeed(-enemigo.getXSpeed());
        enemigo.setySpeed(-enemigo.getySpeed());
        sonidoHerido.play();
        return true;
    }

    public boolean checkCollisionBalaEnemiga(Bullet bala) {
        if (bala == null || bala.esOrigenJugador() || !this.colisionaCon(bala)) {
            return false;
        }
        bala.setDestroyed(true);
        if (!health.hit()) {
            return false;
        }
        sonidoHerido.play();
        return true;
    }

    public boolean checkCollision(Ball2 b) {
        if (!b.getArea().overlaps(spr.getBoundingRectangle()) || !health.hit()) {
            return false;
        }
        b.setXSpeed(-b.getXSpeed());
        b.setySpeed(-b.getySpeed());
        sonidoHerido.play();
        return true;
    }

    @Override
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    @Override
    public boolean estaDestruido() {
        return health.isDestroyed() && !health.isProtected();
    }

    public boolean estaHerido() {
        return health.isProtected();
    }

    public int getVidas() {
        return health.lives();
    }

    public int getX() {
        return (int) spr.getX();
    }

    public int getY() {
        return (int) spr.getY();
    }

    public float getCentroX() {
        return spr.getX() + spr.getWidth() / 2f;
    }

    public float getCentroY() {
        return spr.getY() + spr.getHeight() / 2f;
    }

    public EstrategiaDisparo getEstrategiaDisparo() {
        return estrategiaDisparo;
    }

    public void setEstrategiaDisparo(EstrategiaDisparo estrategiaDisparo) {
        this.estrategiaDisparo = estrategiaDisparo;
    }
}