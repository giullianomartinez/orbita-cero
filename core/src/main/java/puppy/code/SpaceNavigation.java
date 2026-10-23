package puppy.code;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** Punto de entrada y propietario de los servicios compartidos de la aplicación. */
public final class SpaceNavigation extends Game {
    private SpriteBatch batch;
    private BitmapFont font;
    private RecursosJuego recursos;
    private int highScore;
    private int ultimoPuntaje;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2f);
        recursos = RecursosJuego.getInstancia();
        setScreen(new PantallaMenu(this));
    }

    @Override public void render() { super.render(); }

    @Override
    public void dispose() {
        if (getScreen() != null) getScreen().dispose();
        RecursosJuego.liberarInstancia();
        if (font != null) font.dispose();
        if (batch != null) batch.dispose();
    }

    public RecursosJuego getRecursos() { return recursos; }
    public SpriteBatch getBatch() { return batch; }
    public BitmapFont getFont() { return font; }
    public int getHighScore() { return highScore; }
    public int getUltimoPuntaje() { return ultimoPuntaje; }

    public void registrarPuntaje(int puntaje) {
        ultimoPuntaje = Math.max(0, puntaje);
        highScore = Math.max(highScore, ultimoPuntaje);
    }
}
