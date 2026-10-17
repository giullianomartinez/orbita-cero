package puppy.code.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/** Traduce WASD y el mouse a acciones de nave. */
public final class ControlesTecladoNave implements ControlesNave {

    private final Viewport viewport;
    private final Vector2 puntero = new Vector2();

    public ControlesTecladoNave(Viewport viewport) {
        this.viewport = viewport;
    }

    @Override
    public EntradaNave leer() {
        viewport.unproject(puntero.set(Gdx.input.getX(), Gdx.input.getY()));
        boolean dentroDelArea = puntero.x >= 0 && puntero.x <= viewport.getWorldWidth()
            && puntero.y >= 0 && puntero.y <= viewport.getWorldHeight();
        return new EntradaNave(
            eje(Input.Keys.D, Input.Keys.A),
            eje(Input.Keys.W, Input.Keys.S),
            puntero.x, puntero.y,
            dentroDelArea && Gdx.input.isButtonPressed(Input.Buttons.LEFT)
        );
    }

    private float eje(int positivo, int negativo) {
        return (Gdx.input.isKeyPressed(positivo) ? 1 : 0)
            - (Gdx.input.isKeyPressed(negativo) ? 1 : 0);
    }
}
