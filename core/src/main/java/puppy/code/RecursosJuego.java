package puppy.code;

import java.util.List;
import java.util.Objects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import puppy.code.input.ControlesNave;

/** Propietario único de recursos gráficos y de audio durante la vida de la aplicación. */
public final class RecursosJuego implements Disposable, ServiciosPartida {
    private static RecursosJuego instancia;

    private final Texture texturaNave;
    private final Texture texturaBala;
    private final Texture texturaCaza;
    private final Texture texturaArtillera;
    private final Sound sonidoHerida;
    private final Sound sonidoDisparo;
    private final Sound sonidoExplosion;
    private final Music musica;
    private boolean liberado;

    private RecursosJuego() {
        Texture nave = null;
        Texture bala = null;
        Texture caza = null;
        Texture artillera = null;
        Sound herida = null;
        Sound disparo = null;
        Sound explosion = null;
        Music musicaJuego = null;
        try {
            nave = new Texture(Gdx.files.internal("MainShip3.png"));
            bala = new Texture(Gdx.files.internal("Rocket2.png"));
            caza = cargarTexturaSegura("nave_caza.png", nave);
            artillera = cargarTexturaSegura("nave_artillera.png", nave);
            herida = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
            disparo = Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"));
            explosion = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
            musicaJuego = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));
            musicaJuego.setLooping(true);
            musicaJuego.setVolume(0.5f);
        } catch (RuntimeException error) {
            if (musicaJuego != null) musicaJuego.dispose();
            if (explosion != null) explosion.dispose();
            if (disparo != null) disparo.dispose();
            if (herida != null) herida.dispose();
            if (artillera != null && artillera != nave) artillera.dispose();
            if (caza != null && caza != nave && caza != artillera) caza.dispose();
            if (bala != null) bala.dispose();
            if (nave != null) nave.dispose();
            throw error;
        }
        texturaNave = nave;
        texturaBala = bala;
        texturaCaza = caza;
        texturaArtillera = artillera;
        sonidoHerida = herida;
        sonidoDisparo = disparo;
        sonidoExplosion = explosion;
        musica = musicaJuego;
    }

    public static synchronized RecursosJuego getInstancia() {
        if (instancia == null) instancia = new RecursosJuego();
        return instancia;
    }

    public static synchronized void liberarInstancia() {
        if (instancia == null) return;
        instancia.dispose();
        instancia = null;
    }

    private Texture cargarTexturaSegura(String archivo, Texture respaldo) {
        return Gdx.files.internal(archivo).exists()
            ? new Texture(Gdx.files.internal(archivo)) : respaldo;
    }

    @Override
    public Nave crearNave(int x, int y, int vidas, ControlesNave controles) {
        comprobarDisponible();
        return new Nave(x, y, texturaNave, this, vidas, controles);
    }

    @Override
    public NaveEnemiga crearNaveEnemiga(boolean artillera, int x, int y,
            int velocidadX, int velocidadY) {
        comprobarDisponible();
        if (artillera) {
            return new NaveArtillera(x, y, velocidadX, velocidadY,
                texturaArtillera, this);
        }
        return new NaveCaza(x, y, velocidadX, velocidadY, texturaCaza, this);
    }

    @Override
    public List<Bullet> crearDisparos(float origenX, float origenY,
            float objetivoX, float objetivoY, float rapidez,
            EstrategiaDisparo estrategia, boolean origenJugador) {
        comprobarDisponible();
        return Objects.requireNonNull(estrategia, "estrategia").crearDisparos(origenX, origenY, objetivoX, objetivoY,
            rapidez, texturaBala, origenJugador);
    }

    @Override public void reproducirDisparo() { comprobarDisponible(); sonidoDisparo.play(); }
    @Override public void reproducirHerida() { comprobarDisponible(); sonidoHerida.play(); }
    public void reproducirExplosion() { comprobarDisponible(); sonidoExplosion.play(0.5f); }

    public void reproducirMusica() {
        comprobarDisponible();
        if (!musica.isPlaying()) musica.play();
    }

    public void detenerMusica() { comprobarDisponible(); musica.stop(); }

    private void comprobarDisponible() {
        if (liberado) throw new IllegalStateException("Los recursos del juego ya fueron liberados");
    }

    @Override
    public void dispose() {
        if (liberado) return;
        liberado = true;
        musica.dispose();
        sonidoExplosion.dispose();
        sonidoDisparo.dispose();
        sonidoHerida.dispose();
        if (texturaArtillera != texturaNave && texturaArtillera != texturaCaza) {
            texturaArtillera.dispose();
        }
        if (texturaCaza != texturaNave) texturaCaza.dispose();
        texturaBala.dispose();
        texturaNave.dispose();
    }
}
