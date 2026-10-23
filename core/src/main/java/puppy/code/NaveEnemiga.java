package puppy.code;

import java.util.List;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class NaveEnemiga implements Colisionable {
    private float x;
    private float y;
    private int xSpeed;
    private int ySpeed;
    private int puntosVida;
    private float intervaloDisparo;
    private float tiempoDisparo;
    private float rapidezProyectil;
    private Sprite spr;
    private EstrategiaDisparo estrategiaDisparo;

    protected NaveEnemiga(int x, int y, int ancho, int alto, int xSpeed, int ySpeed, int puntosVida, float intervaloDisparo, float rapidezProyectil, Texture tx, EstrategiaDisparo estrategiaDisparo) {
        this.puntosVida = puntosVida;
        this.intervaloDisparo = intervaloDisparo;
        this.tiempoDisparo = intervaloDisparo * 0.6f;
        this.rapidezProyectil = rapidezProyectil;
        this.estrategiaDisparo = estrategiaDisparo;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.spr = new Sprite(tx);
        this.spr.setSize(ancho, alto);
        this.spr.setOriginCenter();
        this.x = MathUtils.clamp(x, 0, AreaJuego.ANCHO - spr.getWidth());
        this.y = MathUtils.clamp(y, 0, AreaJuego.ALTO - spr.getHeight());
        this.spr.setPosition(this.x, this.y);
    }

    public void update(float delta, float objetivoX, float objetivoY, Texture txBala, List<Bullet> balasEnJuego) {
        if (estaDestruido()) {
            return;
        }
        float paso = Math.min(delta, 0.05f) * 60f;
        x += xSpeed * paso;
        y += ySpeed * paso;
        if (x < 0 || x + spr.getWidth() > AreaJuego.ANCHO) {
            x = MathUtils.clamp(x, 0, AreaJuego.ANCHO - spr.getWidth());
            xSpeed = -xSpeed;
        }
        if (y < 0 || y + spr.getHeight() > AreaJuego.ALTO) {
            y = MathUtils.clamp(y, 0, AreaJuego.ALTO - spr.getHeight());
            ySpeed = -ySpeed;
        }
        spr.setPosition(x, y);
        spr.setRotation(DireccionDisparo.anguloNave(getCentroX(), getCentroY(), objetivoX, objetivoY));
        tiempoDisparo = Math.max(0f, tiempoDisparo - delta);
        if (tiempoDisparo == 0f) {
            balasEnJuego.addAll(dispararHacia(objetivoX, objetivoY, txBala));
            tiempoDisparo = intervaloDisparo;
        }
    }

    public List<Bullet> dispararHacia(float objetivoX, float objetivoY, Texture txBala) {
        return estrategiaDisparo.crearDisparos(getCentroX(), getCentroY(), objetivoX, objetivoY, rapidezProyectil, txBala, false);
    }

    public boolean recibirImpacto(int dano) {
        if (estaDestruido() || dano <= 0) {
            return false;
        }
        puntosVida = Math.max(0, puntosVida - dano);
        alRecibirImpacto();
        return estaDestruido();
    }

    public void checkCollision(NaveEnemiga otra) {
        Rectangle a = this.getArea();
        Rectangle b = otra.getArea();
        Vector2 desplazamiento = ColisionAsteroides.desplazamiento(a, b);
        if (desplazamiento.isZero()) {
            return;
        }
        Vector2[] separaciones = ColisionAsteroides.separaciones(a, b, AreaJuego.ANCHO, AreaJuego.ALTO);
        if (desplazamiento.x != 0) {
            x += separaciones[0].x;
            otra.x += separaciones[1].x;
            float direccion = Math.signum(desplazamiento.x);
            int[] velocidades = ColisionAsteroides.velocidadesTrasChoque(xSpeed, otra.xSpeed, direccion);
            xSpeed = velocidades[0];
            otra.xSpeed = velocidades[1];
        } else {
            y += separaciones[0].y;
            otra.y += separaciones[1].y;
            float direccion = Math.signum(desplazamiento.y);
            int[] velocidades = ColisionAsteroides.velocidadesTrasChoque(ySpeed, otra.ySpeed, direccion);
            ySpeed = velocidades[0];
            otra.ySpeed = velocidades[1];
        }
        x = MathUtils.clamp(x, 0, AreaJuego.ANCHO - spr.getWidth());
        y = MathUtils.clamp(y, 0, AreaJuego.ALTO - spr.getHeight());
        otra.x = MathUtils.clamp(otra.x, 0, AreaJuego.ANCHO - otra.spr.getWidth());
        otra.y = MathUtils.clamp(otra.y, 0, AreaJuego.ALTO - otra.spr.getHeight());
        spr.setPosition(x, y);
        otra.spr.setPosition(otra.x, otra.y);
    }

    public void draw(SpriteBatch batch) {
        if (!estaDestruido()) {
            spr.draw(batch);
        }
    }

    public void cambiarColor(float r, float g, float b) {
        spr.setColor(r, g, b, 1.0f);
    }

    public void setColor(float r, float g, float b) {
        spr.setColor(r, g, b, 1.0f);
    }

    @Override
    public Rectangle getArea() {
        return spr.getBoundingRectangle();
    }

    @Override
    public boolean estaDestruido() {
        return puntosVida <= 0;
    }

    public float getCentroX() {
        return x + spr.getWidth() / 2f;
    }

    public float getCentroY() {
        return y + spr.getHeight() / 2f;
    }

    public abstract int getPuntajeRecompensa();
    public abstract String getNombreTipo();
    protected abstract void alRecibirImpacto();

    public int getXSpeed() {
        return xSpeed;
    }

    public void setXSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }

    public int getPuntosVida() {
        return puntosVida;
    }

    public float getIntervaloDisparo() {
        return intervaloDisparo;
    }

    public void setIntervaloDisparo(float intervaloDisparo) {
        this.intervaloDisparo = Math.max(0.2f, intervaloDisparo);
    }

    public EstrategiaDisparo getEstrategiaDisparo() {
        return estrategiaDisparo;
    }

    public void setEstrategiaDisparo(EstrategiaDisparo estrategiaDisparo) {
        this.estrategiaDisparo = estrategiaDisparo;
    }
}