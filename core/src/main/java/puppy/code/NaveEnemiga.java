package puppy.code;

import java.util.List;
import java.util.Objects;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/** Base con el ciclo compartido, colisiones y estado de las naves enemigas. */
public abstract class NaveEnemiga implements Colisionable {
    private final Sprite sprite;
    private final float rapidezProyectil;
    private final ServiciosNave servicios;
    private float x;
    private float y;
    private int velocidadX;
    private int velocidadY;
    private int puntosVida;
    private float intervaloDisparo;
    private float tiempoDisparo;
    private final EstrategiaDisparo estrategiaDisparo;

    protected NaveEnemiga(int x, int y, int ancho, int alto, int velocidadX,
            int velocidadY, int puntosVida, float intervaloDisparo,
            float rapidezProyectil, Texture textura,
            EstrategiaDisparo estrategiaDisparo, ServiciosNave servicios) {
        if (ancho <= 0 || alto <= 0 || puntosVida <= 0 || intervaloDisparo <= 0
                || rapidezProyectil <= 0) {
            throw new IllegalArgumentException("Configuración de nave enemiga inválida");
        }
        this.puntosVida = puntosVida;
        this.intervaloDisparo = intervaloDisparo;
        this.tiempoDisparo = intervaloDisparo * 0.6f;
        this.rapidezProyectil = rapidezProyectil;
        this.estrategiaDisparo = Objects.requireNonNull(estrategiaDisparo, "estrategiaDisparo");
        this.servicios = Objects.requireNonNull(servicios, "servicios");
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        sprite = new Sprite(Objects.requireNonNull(textura, "textura"));
        sprite.setSize(ancho, alto);
        sprite.setOriginCenter();
        this.x = MathUtils.clamp(x, 0, AreaJuego.ANCHO - sprite.getWidth());
        this.y = MathUtils.clamp(y, 0, AreaJuego.ALTO - sprite.getHeight());
        sincronizarRepresentacion();
    }

    /** Template Method: fija el orden de movimiento, límites, orientación y ataque. */
    public final void update(float delta, float objetivoX, float objetivoY,
                             List<Bullet> balasEnJuego) {
        if (estaDestruido() || !Float.isFinite(delta) || delta <= 0) return;
        float paso = Math.min(delta, 0.05f) * 60f;
        mover(paso);
        resolverLimites();
        sincronizarRepresentacion();
        sprite.setRotation(DireccionDisparo.anguloNave(
            getCentroX(), getCentroY(), objetivoX, objetivoY));
        atacar(delta, objetivoX, objetivoY, balasEnJuego);
    }

    protected void mover(float paso) {
        x += velocidadX * paso;
        y += velocidadY * paso;
    }

    protected void resolverLimites() {
        if (x < 0 || x + sprite.getWidth() > AreaJuego.ANCHO) {
            x = MathUtils.clamp(x, 0, AreaJuego.ANCHO - sprite.getWidth());
            velocidadX = -velocidadX;
        }
        if (y < 0 || y + sprite.getHeight() > AreaJuego.ALTO) {
            y = MathUtils.clamp(y, 0, AreaJuego.ALTO - sprite.getHeight());
            velocidadY = -velocidadY;
        }
    }

    protected void sincronizarRepresentacion() { sprite.setPosition(x, y); }

    protected void atacar(float delta, float objetivoX, float objetivoY,
                          List<Bullet> balasEnJuego) {
        tiempoDisparo = Math.max(0f, tiempoDisparo - delta);
        if (tiempoDisparo > 0) return;
        balasEnJuego.addAll(servicios.crearDisparos(getCentroX(), getCentroY(),
            objetivoX, objetivoY, rapidezProyectil, estrategiaDisparo, false));
        tiempoDisparo = intervaloDisparo;
    }

    /** Template Method de daño: la base valida y actualiza vida; cada tipo reacciona. */
    public final boolean recibirImpacto(int dano) {
        if (estaDestruido() || dano <= 0) return false;
        puntosVida = Math.max(0, puntosVida - dano);
        alRecibirImpacto();
        return estaDestruido();
    }

    protected abstract void alRecibirImpacto();

    public final void checkCollision(NaveEnemiga otra) {
        if (otra == null || otra == this || !colisionaCon(otra)) return;
        Rectangle a = getArea();
        Rectangle b = otra.getArea();
        float solapamientoX = Math.min(a.x + a.width, b.x + b.width)
            - Math.max(a.x, b.x);
        float solapamientoY = Math.min(a.y + a.height, b.y + b.height)
            - Math.max(a.y, b.y);

        if (solapamientoX <= solapamientoY) {
            float direccion = centroX(a) < centroX(b) ? -1f : 1f;
            float moverA = Math.min(solapamientoX / 2f, espacioDisponibleX(direccion));
            float moverB = Math.min(solapamientoX - moverA, otra.espacioDisponibleX(-direccion));
            x += direccion * moverA;
            otra.x -= direccion * moverB;
            if ((velocidadX - otra.velocidadX) * direccion < 0) intercambiarVelocidadX(otra);
        } else {
            float direccion = centroY(a) < centroY(b) ? -1f : 1f;
            float moverA = Math.min(solapamientoY / 2f, espacioDisponibleY(direccion));
            float moverB = Math.min(solapamientoY - moverA, otra.espacioDisponibleY(-direccion));
            y += direccion * moverA;
            otra.y -= direccion * moverB;
            if ((velocidadY - otra.velocidadY) * direccion < 0) intercambiarVelocidadY(otra);
        }
        limitarPosicion();
        otra.limitarPosicion();
        sincronizarRepresentacion();
        otra.sincronizarRepresentacion();
    }

    public final void resolverColisionConJugador(Rectangle areaJugador) {
        if (areaJugador == null) return;
        Rectangle areaEnemiga = getArea();
        if (!areaEnemiga.overlaps(areaJugador)) return;
        float solapamientoX = Math.min(areaEnemiga.x + areaEnemiga.width,
            areaJugador.x + areaJugador.width)
            - Math.max(areaEnemiga.x, areaJugador.x);
        float solapamientoY = Math.min(areaEnemiga.y + areaEnemiga.height,
            areaJugador.y + areaJugador.height)
            - Math.max(areaEnemiga.y, areaJugador.y);
        if (solapamientoX <= solapamientoY) {
            float direccion = centroX(areaEnemiga) < centroX(areaJugador) ? -1f : 1f;
            x += direccion * (solapamientoX + 0.5f);
            if (velocidadX * direccion < 0) velocidadX = -velocidadX;
        } else {
            float direccion = centroY(areaEnemiga) < centroY(areaJugador) ? -1f : 1f;
            y += direccion * (solapamientoY + 0.5f);
            if (velocidadY * direccion < 0) velocidadY = -velocidadY;
        }
        limitarPosicion();
        sincronizarRepresentacion();
    }

    private float espacioDisponibleX(float direccion) {
        return direccion < 0 ? x : AreaJuego.ANCHO - x - sprite.getWidth();
    }

    private float espacioDisponibleY(float direccion) {
        return direccion < 0 ? y : AreaJuego.ALTO - y - sprite.getHeight();
    }

    private void limitarPosicion() {
        x = MathUtils.clamp(x, 0, AreaJuego.ANCHO - sprite.getWidth());
        y = MathUtils.clamp(y, 0, AreaJuego.ALTO - sprite.getHeight());
    }

    private void intercambiarVelocidadX(NaveEnemiga otra) {
        int temporal = velocidadX;
        velocidadX = otra.velocidadX;
        otra.velocidadX = temporal;
    }

    private void intercambiarVelocidadY(NaveEnemiga otra) {
        int temporal = velocidadY;
        velocidadY = otra.velocidadY;
        otra.velocidadY = temporal;
    }

    private float centroX(Rectangle area) { return area.x + area.width / 2f; }
    private float centroY(Rectangle area) { return area.y + area.height / 2f; }

    public final void rebotarPorImpacto() {
        velocidadX = -velocidadX;
        velocidadY = -velocidadY;
    }

    public final void reducirIntervaloDisparo(float factor) {
        if (factor <= 0 || factor >= 1) throw new IllegalArgumentException("Factor inválido");
        intervaloDisparo = Math.max(0.2f, intervaloDisparo * factor);
    }

    public void draw(SpriteBatch batch) { if (!estaDestruido()) sprite.draw(batch); }
    protected final void cambiarColor(float r, float g, float b) { sprite.setColor(r, g, b, 1f); }
    @Override public Rectangle getArea() { return new Rectangle(sprite.getBoundingRectangle()); }
    @Override public boolean estaDestruido() { return puntosVida <= 0; }
    public final float getCentroX() { return x + sprite.getWidth() / 2f; }
    public final float getCentroY() { return y + sprite.getHeight() / 2f; }
    public final int getPuntosVida() { return puntosVida; }
    public final float getIntervaloDisparo() { return intervaloDisparo; }
    public abstract int getPuntajeRecompensa();
    public abstract String getNombreTipo();
}
