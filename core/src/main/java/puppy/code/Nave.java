package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import puppy.code.input.ControlesNave;
import puppy.code.input.EntradaNave;

public class Nave {

    private final Health health;
    private final ControlesNave controles;
    private static final float INTERVALO_DISPARO = 0.22f;
    private float tiempoDisparo;
    private Sprite spr;
    private final ServiciosNave servicios;

    Nave(int x, int y, Texture tx, ServiciosNave servicios, int vidasIniciales,
                 ControlesNave controles) {
        health = new Health(vidasIniciales);
        this.controles = controles;

        this.servicios = servicios;

        spr = new Sprite(tx);
        spr.setPosition(x, y);
        spr.setBounds(x, y, 45, 45);
        spr.setOriginCenter();
    }

    public void update(float delta, ReceptorDisparos receptor) {
        EntradaNave entrada = controles.leer();
        tiempoDisparo = Math.max(0, tiempoDisparo - delta);
        Vector2 posicion = MovimientoNave.posicionSiguiente(
            spr.getX(), spr.getY(), entrada.horizontal(), entrada.vertical(),
            delta, spr.getWidth(), spr.getHeight()
        );
        spr.setPosition(posicion.x, posicion.y);
        health.update(delta);

        float origenX = spr.getX() + spr.getWidth() / 2;
        float origenY = spr.getY() + spr.getHeight() / 2;
        if (entrada.apuntarX() != origenX || entrada.apuntarY() != origenY) {
            spr.setRotation(DireccionDisparo.anguloNave(
                origenX, origenY, entrada.apuntarX(), entrada.apuntarY()
            ));
        }

        if (entrada.disparar() && tiempoDisparo == 0) {
            Vector2 velocidad = DireccionDisparo.velocidad(
                origenX, origenY, entrada.apuntarX(), entrada.apuntarY(), 640
            );
            if (velocidad.isZero()) return;

            Bullet bala = servicios.crearBala(
                origenX,
                origenY,
                velocidad.x,
                velocidad.y
            );
            receptor.agregarBala(bala);
            servicios.reproducirDisparo();
            tiempoDisparo = INTERVALO_DISPARO;
        }
    }

    public void draw(SpriteBatch batch) {
        spr.setAlpha(health.isProtected() ? 0.5f : 1f);
        spr.draw(batch);
        spr.setAlpha(1f);
    }

    public boolean checkCollision(Asteroide b) {
        boolean hayColision =
            b.getArea().overlaps(spr.getBoundingRectangle());

        if (!hayColision || !health.hit()) {
            return false;
        }

        b.rebotarPorImpacto();

        servicios.reproducirHerida();
        return true;
    }

    public boolean estaDestruido() {
        return health.isDestroyed();
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
}
