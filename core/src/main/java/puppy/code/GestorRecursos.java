package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class GestorRecursos {

    private static GestorRecursos instancia;

    private Texture texturaNave;
    private Texture texturaBala;
    private Texture texturaNaveCaza;
    private Texture texturaNaveArtillera;

    private Sound sonidoExplosion;
    private Sound sonidoHerido;
    private Sound sonidoDisparo;

    private Music musicaJuego;

    private GestorRecursos() {

        texturaNave =
                new Texture(Gdx.files.internal("MainShip3.png"));

        texturaBala =
                new Texture(Gdx.files.internal("Rocket2.png"));

        texturaNaveCaza =
                cargarTexturaSegura("nave_caza.png", texturaNave);

        texturaNaveArtillera =
                cargarTexturaSegura("nave_artillera.png", texturaNave);

        sonidoExplosion =
                Gdx.audio.newSound(
                        Gdx.files.internal("explosion.ogg"));

        sonidoHerido =
                Gdx.audio.newSound(
                        Gdx.files.internal("hurt.ogg"));

        sonidoDisparo =
                Gdx.audio.newSound(
                        Gdx.files.internal("pop-sound.mp3"));

        musicaJuego =
                Gdx.audio.newMusic(
                        Gdx.files.internal("piano-loops.wav"));

        musicaJuego.setLooping(true);
        musicaJuego.setVolume(0.5f);
    }

    private Texture cargarTexturaSegura(
            String archivo,
            Texture respaldo) {

        if (Gdx.files.internal(archivo).exists()) {
            return new Texture(
                    Gdx.files.internal(archivo));
        }

        return respaldo;
    }

    public static GestorRecursos getInstancia() {

        if (instancia == null) {
            instancia = new GestorRecursos();
        }

        return instancia;
    }

    public Texture getTexturaNave() {
        return texturaNave;
    }

    public Texture getTexturaBala() {
        return texturaBala;
    }

    public Texture getTexturaNaveCaza() {
        return texturaNaveCaza;
    }

    public Texture getTexturaNaveArtillera() {
        return texturaNaveArtillera;
    }

    public Sound getSonidoExplosion() {
        return sonidoExplosion;
    }

    public Sound getSonidoHerido() {
        return sonidoHerido;
    }

    public Sound getSonidoDisparo() {
        return sonidoDisparo;
    }

    public Music getMusicaJuego() {
        return musicaJuego;
    }

    public static void liberar() {

        if (instancia != null) {

            if (instancia.texturaNaveCaza
                    != instancia.texturaNave) {
                instancia.texturaNaveCaza.dispose();
            }

            if (instancia.texturaNaveArtillera
                    != instancia.texturaNave) {
                instancia.texturaNaveArtillera.dispose();
            }

            instancia.texturaNave.dispose();
            instancia.texturaBala.dispose();

            instancia.sonidoExplosion.dispose();
            instancia.sonidoHerido.dispose();
            instancia.sonidoDisparo.dispose();

            instancia.musicaJuego.dispose();

            instancia = null;
        }
    }
}