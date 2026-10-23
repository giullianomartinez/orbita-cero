package puppy.code;

import java.util.Objects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

/** Muestra el resultado y permite iniciar otra partida. */
public final class PantallaGameOver implements Screen {
    private final SpaceNavigation game;
    private final OrthographicCamera camera;

    public PantallaGameOver(SpaceNavigation game) {
        this.game = Objects.requireNonNull(game, "game");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, AreaJuego.ANCHO, AreaJuego.ALTO);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        game.getFont().draw(game.getBatch(), "Fin de la partida", 120, 450);
        game.getFont().draw(game.getBatch(), "Puntaje: " + game.getUltimoPuntaje(), 120, 380);
        game.getFont().draw(game.getBatch(), "Récord: " + game.getHighScore(), 120, 330);
        game.getFont().draw(game.getBatch(), "Haz clic o presiona cualquier tecla para volver a jugar", 100, 250);
        game.getBatch().end();

        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            Screen siguiente = new PantallaJuegoBuilder(game).build();
            siguiente.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            game.setScreen(siguiente);
            dispose();
        }
    }

    @Override public void show() { }
    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void dispose() { }
}
