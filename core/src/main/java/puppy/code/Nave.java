package puppy.code;

import java.util.List;
import java.util.Objects;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import puppy.code.input.ControlesNave;
import puppy.code.input.EntradaNave;

/** Nave controlada por el jugador. Conserva el estado de vida y delega servicios externos. */
public final class Nave implements Colisionable {
    private static final float INTERVALO_DISPARO = 0.22f;
    private static final float RAPIDEZ_BALA = 640f;

    private final Health health;
    private final ControlesNave controles;
    private final ServiciosNave servicios;
    private final Sprite sprite;
    private float tiempoDisparo;
    private EstrategiaDisparo estrategiaDisparo = new DisparoSimple();

    Nave(int x, int y, Texture textura, ServiciosNave servicios, int vidasIniciales,
         ControlesNave controles) {
        this.health = new Health(vidasIniciales);
        this.controles = Objects.requireNonNull(controles, "controles");
        this.servicios = Objects.requireNonNull(servicios, "servicios");
        this.sprite = new Sprite(Objects.requireNonNull(textura, "textura"));
        this.sprite.setBounds(x, y, 45, 45);
        this.sprite.setOriginCenter();
    }

    public void update(float delta, ReceptorDisparos receptor) {
        if (!Float.isFinite(delta) || delta <= 0 || health.isDestroyed()) return;
        EntradaNave entrada = controles.leer();
        tiempoDisparo = Math.max(0, tiempoDisparo - delta);
        Vector2 posicion = MovimientoNave.posicionSiguiente(
            sprite.getX(), sprite.getY(), entrada.horizontal(), entrada.vertical(),
            delta, sprite.getWidth(), sprite.getHeight());
        sprite.setPosition(posicion.x, posicion.y);
        health.update(delta);

        float origenX = getCentroX();
        float origenY = getCentroY();
        if (entrada.apuntarX() != origenX || entrada.apuntarY() != origenY) {
            sprite.setRotation(DireccionDisparo.anguloNave(
                origenX, origenY, entrada.apuntarX(), entrada.apuntarY()));
        }

        if (entrada.disparar() && tiempoDisparo == 0) {
            List<Bullet> disparos = servicios.crearDisparos(
                origenX, origenY, entrada.apuntarX(), entrada.apuntarY(),
                RAPIDEZ_BALA, estrategiaDisparo, true);
            if (disparos.isEmpty()) return;
            for (Bullet bala : disparos) receptor.agregarBala(bala);
            servicios.reproducirDisparo();
            tiempoDisparo = INTERVALO_DISPARO;
        }
    }

    public void draw(SpriteBatch batch) {
        sprite.setAlpha(health.isProtected() ? 0.5f : 1f);
        sprite.draw(batch);
        sprite.setAlpha(1f);
    }

    public boolean checkCollision(NaveEnemiga enemiga) {
        if (!colisionaCon(enemiga)) return false;
        enemiga.resolverColisionConJugador(getArea());
        if (!health.hit()) return false;
        servicios.reproducirHerida();
        return true;
    }

    public boolean checkCollision(Bullet bala) {
        if (bala == null || bala.esOrigenJugador() || !bala.checkCollision(this)) return false;
        if (!health.hit()) return true;
        servicios.reproducirHerida();
        return true;
    }

    public void setEstrategiaDisparo(EstrategiaDisparo estrategia) {
        this.estrategiaDisparo = Objects.requireNonNull(estrategia, "estrategia");
    }

    public boolean estaDestruido() { return health.isDestroyed(); }
    public boolean estaHerido() { return health.isProtected(); }
    public int getVidas() { return health.lives(); }
    public int getX() { return (int) sprite.getX(); }
    public int getY() { return (int) sprite.getY(); }
    public float getCentroX() { return sprite.getX() + sprite.getWidth() / 2f; }
    public float getCentroY() { return sprite.getY() + sprite.getHeight() / 2f; }

    @Override public Rectangle getArea() { return new Rectangle(sprite.getBoundingRectangle()); }
}
