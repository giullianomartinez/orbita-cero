package puppy.code;

import java.util.Objects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

/** Menú inicial del juego. */
public final class PantallaMenu implements Screen {
    private final SpaceNavigation game;
    private final OrthographicCamera camera;

    public PantallaMenu(SpaceNavigation game) {
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
        game.getFont().draw(game.getBatch(), "Orbita Cero", 140, 440);
        game.getFont().draw(game.getBatch(), "WASD: mover   Mouse: apuntar   Clic izquierdo: disparar", 100, 360);
        game.getFont().draw(game.getBatch(), "Destruye las naves enemigas y sobrevive a cada ronda.", 100, 310);
        game.getFont().draw(game.getBatch(), "Haz clic o presiona cualquier tecla para comenzar", 100, 250);
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
