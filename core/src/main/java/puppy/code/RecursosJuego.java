package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

public final class RecursosJuego implements Disposable, ServiciosNave {
    private final Texture nave;
    private final Texture bala;
    private final Texture asteroide;
    private final Sound herida;
    private final Sound disparo;
    private final Sound explosion;
    private final Music musica;

    public RecursosJuego() {
        Texture texturaNave = null;
        Texture texturaBala = null;
        Texture texturaAsteroide = null;
        Sound sonidoHerida = null;
        Sound sonidoDisparo = null;
        Sound sonidoExplosion = null;
        Music musicaJuego = null;
        try {
            texturaNave = new Texture(Gdx.files.internal("MainShip3.png"));
            texturaBala = new Texture(Gdx.files.internal("Rocket2.png"));
            texturaAsteroide = new Texture(Gdx.files.internal("aGreyMedium4.png"));
            sonidoHerida = Gdx.audio.newSound(Gdx.files.internal("hurt.ogg"));
            sonidoDisparo = Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"));
            sonidoExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
            musicaJuego = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav"));
            musicaJuego.setLooping(true);
            musicaJuego.setVolume(0.5f);
        } catch (RuntimeException error) {
            if (musicaJuego != null) musicaJuego.dispose();
            if (sonidoExplosion != null) sonidoExplosion.dispose();
            if (sonidoDisparo != null) sonidoDisparo.dispose();
            if (sonidoHerida != null) sonidoHerida.dispose();
            if (texturaAsteroide != null) texturaAsteroide.dispose();
            if (texturaBala != null) texturaBala.dispose();
            if (texturaNave != null) texturaNave.dispose();
            throw error;
        }
        nave = texturaNave;
        bala = texturaBala;
        asteroide = texturaAsteroide;
        herida = sonidoHerida;
        disparo = sonidoDisparo;
        explosion = sonidoExplosion;
        musica = musicaJuego;
    }

    public Nave crearNave(int x, int y, int vidas, puppy.code.input.ControlesNave controles) {
        return new Nave(x, y, nave, this, vidas, controles);
    }

    public Asteroide crearAsteroide(int x, int y, int tamano, int velocidadX, int velocidadY) {
        return new Asteroide(x, y, tamano, velocidadX, velocidadY, asteroide);
    }

    @Override
    public Bullet crearBala(float x, float y, float velocidadX, float velocidadY) {
        return new Bullet(x, y, velocidadX, velocidadY, bala);
    }

    @Override public void reproducirDisparo() { disparo.play(); }
    @Override public void reproducirHerida() { herida.play(); }

    public void reproducirMusica() {
        if (!musica.isPlaying()) musica.play();
    }

    public void detenerMusica() {
        musica.stop();
    }

    public void reproducirExplosion() {
        explosion.play(0.5f);
    }

    @Override
    public void dispose() {
        musica.dispose();
        explosion.dispose();
        disparo.dispose();
        herida.dispose();
        asteroide.dispose();
        bala.dispose();
        nave.dispose();
    }
}
