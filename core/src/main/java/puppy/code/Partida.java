package puppy.code;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import puppy.code.input.ControlesNave;

/** Coordina entidades, colisiones, puntaje y avance de ronda. */
public final class Partida implements ReceptorDisparos {
    private final List<NaveEnemiga> enemigos = new ArrayList<>();
    private final List<Bullet> balas = new ArrayList<>();
    private final Nave nave;
    private final ServiciosPartida recursos;
    private int puntaje;

    public Partida(EstadoPartida estado, ServiciosPartida recursos,
                   ControlesNave controles, Random azar) {
        Objects.requireNonNull(estado, "estado");
        this.recursos = Objects.requireNonNull(recursos, "recursos");
        Objects.requireNonNull(azar, "azar");
        nave = recursos.crearNave(AreaJuego.ANCHO / 2 - 22, 30,
            estado.vidas(), controles);
        if (estado.ronda() >= 3) {
            nave.setEstrategiaDisparo(new DisparoTriple());
        }
        puntaje = estado.puntaje();
        for (int i = 0; i < estado.navesEnemigas(); i++) {
            int x = azar.nextInt(AreaJuego.ANCHO - 60);
            int y = 160 + azar.nextInt(AreaJuego.ALTO - 220);
            int velocidadX = estado.velocidadX() + azar.nextInt(3);
            int velocidadY = estado.velocidadY() + azar.nextInt(3);
            enemigos.add(recursos.crearNaveEnemiga(i % 3 == 0, x, y,
                velocidadX, velocidadY));
        }
    }

    public Partida(EstadoPartida estado, ServiciosPartida recursos,
                   ControlesNave controles) {
        this(estado, recursos, controles, new Random());
    }

    @Override public void agregarBala(Bullet bala) { balas.add(bala); }

    public void actualizar(float delta) {
        if (!Float.isFinite(delta) || delta <= 0 || nave.estaDestruido()) return;
        nave.update(delta, this);
        for (NaveEnemiga enemigo : enemigos) {
            enemigo.update(delta, nave.getCentroX(), nave.getCentroY(), balas);
        }
        actualizarDisparos(delta);
        resolverColisionesEntreEnemigos();
        for (NaveEnemiga enemigo : enemigos) nave.checkCollision(enemigo);
    }

    private void actualizarDisparos(float delta) {
        for (int i = 0; i < balas.size(); i++) {
            Bullet bala = balas.get(i);
            bala.update(delta);
            if (bala.esOrigenJugador()) {
                for (int j = 0; j < enemigos.size(); j++) {
                    NaveEnemiga enemigo = enemigos.get(j);
                    if (!bala.checkCollision(enemigo)) continue;
                    recursos.reproducirExplosion();
                    if (enemigo.recibirImpacto(1)) {
                        puntaje += enemigo.getPuntajeRecompensa();
                        enemigos.remove(j);
                    }
                    break;
                }
            } else {
                nave.checkCollision(bala);
            }
            if (bala.isDestroyed()) balas.remove(i--);
        }
    }

    private void resolverColisionesEntreEnemigos() {
        for (int i = 0; i < enemigos.size(); i++) {
            for (int j = i + 1; j < enemigos.size(); j++) {
                enemigos.get(i).checkCollision(enemigos.get(j));
            }
        }
    }

    public void dibujar(SpriteBatch batch) {
        for (Bullet bala : balas) bala.draw(batch);
        nave.draw(batch);
        for (NaveEnemiga enemigo : enemigos) enemigo.draw(batch);
    }

    public int vidas() { return nave.getVidas(); }
    public int puntaje() { return puntaje; }
    public int cantidadEnemigos() { return enemigos.size(); }
    public boolean terminada() { return nave.estaDestruido(); }
    public boolean rondaCompletada() { return enemigos.isEmpty() && vidas() > 0; }
}
