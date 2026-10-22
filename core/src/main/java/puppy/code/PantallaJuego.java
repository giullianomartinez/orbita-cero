package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import puppy.code.input.ControlesTecladoNave;

/** Presenta la partida y gestiona los cambios de pantalla. */
public class PantallaJuego implements Screen {
    private final SpaceNavigation game;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final SpriteBatch batch;
    private final EstadoPartida estado;
    private final Partida partida;

    public PantallaJuego(SpaceNavigation game, EstadoPartida estado) {
        this.game = game;
        this.estado = estado;
        batch = game.getBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(AreaJuego.ANCHO, AreaJuego.ALTO, camera);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        partida = new Partida(estado, game.getRecursos(), new ControlesTecladoNave(viewport));
    }

    private void dibujarEncabezado() {
        game.getFont().getData().setScale(2f);
        game.getFont().draw(batch, "Vidas: " + partida.vidas() + " Ronda: " + estado.ronda(), 10, 30);
        game.getFont().draw(batch, "Score:" + partida.puntaje(), AreaJuego.ANCHO - 150, 30);
        game.getFont().draw(batch, "HighScore:" + game.getHighScore(), AreaJuego.ANCHO / 2 - 100, 30);
    }

    @Override
    public void render(float delta) {
        float paso = Math.min(Math.max(delta, 0), 0.05f);
        viewport.apply();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        partida.actualizar(paso);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        dibujarEncabezado();
        partida.dibujar(batch);
        batch.end();

        if (partida.terminada()) {
            game.getRecursos().detenerMusica();
            game.registrarPuntaje(partida.puntaje());
            cambiarPantalla(new PantallaGameOver(game));
        } else if (partida.rondaCompletada()) {
            cambiarPantalla(new PantallaJuego(game,
                estado.siguienteRonda(partida.vidas(), partida.puntaje())));
        }
    }

    private void cambiarPantalla(Screen siguiente) {
        siguiente.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.setScreen(siguiente);
        dispose();
    }

    @Override public void show() { game.getRecursos().reproducirMusica(); }
    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void dispose() { /* Recursos compartidos: propiedad de RecursosJuego. */ }
}
