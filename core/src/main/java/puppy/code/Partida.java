package puppy.code;

import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import puppy.code.input.ControlesNave;

/** Mantiene las entidades y resuelve las reglas de una ronda. */
public final class Partida implements ReceptorDisparos {
    private final ArrayList<Asteroide> asteroides = new ArrayList<>();
    private final ArrayList<Bullet> balas = new ArrayList<>();
    private final Nave nave;
    private final RecursosJuego recursos;
    private int puntaje;

    public Partida(EstadoPartida estado, RecursosJuego recursos, ControlesNave controles) {
        this.recursos = recursos;
        this.puntaje = estado.puntaje();
        nave = recursos.crearNave(400 - 22, 30, estado.vidas(), controles);
        Random azar = new Random();
        for (int i = 0; i < estado.asteroides(); i++) {
            asteroides.add(recursos.crearAsteroide(azar.nextInt(AreaJuego.ANCHO),
                50 + azar.nextInt(AreaJuego.ALTO - 50), 20 + azar.nextInt(10),
                estado.velocidadX() + azar.nextInt(4),
                estado.velocidadY() + azar.nextInt(4)));
        }
    }

    @Override
    public void agregarBala(Bullet bala) { balas.add(bala); }

    public void actualizar(float delta) {
        nave.update(delta, this);
        for (int i = 0; i < balas.size(); i++) {
            Bullet bala = balas.get(i);
            bala.update(delta);
            for (int j = 0; j < asteroides.size(); j++) {
                if (bala.checkCollision(asteroides.get(j))) {
                    bala.destruir();
                    recursos.reproducirExplosion();
                    asteroides.remove(j--);
                    puntaje += 10;
                }
            }
            if (bala.isDestroyed()) balas.remove(i--);
        }
        for (Asteroide asteroide : asteroides) asteroide.update(delta);
        for (int i = 0; i < asteroides.size(); i++) {
            for (int j = i + 1; j < asteroides.size(); j++) {
                asteroides.get(i).checkCollision(asteroides.get(j));
            }
        }
        for (int i = 0; i < asteroides.size(); i++) {
            if (nave.checkCollision(asteroides.get(i))) asteroides.remove(i--);
        }
    }

    public void dibujar(SpriteBatch batch) {
        for (Bullet bala : balas) bala.draw(batch);
        nave.draw(batch);
        for (Asteroide asteroide : asteroides) asteroide.draw(batch);
    }

    public int vidas() { return nave.getVidas(); }
    public int puntaje() { return puntaje; }
    public boolean terminada() { return nave.estaDestruido(); }
    public boolean rondaCompletada() { return asteroides.isEmpty() && vidas() > 0; }
}
