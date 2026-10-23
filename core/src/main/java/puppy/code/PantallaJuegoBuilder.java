package puppy.code;

public class PantallaJuegoBuilder {

    private SpaceNavigation game;

    private int ronda = 1;
    private int vidas = 3;
    private int score = 0;

    private int velXEnemigos = 1;
    private int velYEnemigos = 1;

    private int cantidadEnemigos = 10;

    public PantallaJuegoBuilder(SpaceNavigation game) {
        this.game = game;
    }

    public PantallaJuegoBuilder ronda(int ronda) {
        this.ronda = ronda;
        return this;
    }

    public PantallaJuegoBuilder vidas(int vidas) {
        this.vidas = vidas;
        return this;
    }

    public PantallaJuegoBuilder score(int score) {
        this.score = score;
        return this;
    }

    public PantallaJuegoBuilder velocidadEnemigos(
            int velocidadX,
            int velocidadY) {

        this.velXEnemigos = velocidadX;
        this.velYEnemigos = velocidadY;

        return this;
    }

    public PantallaJuegoBuilder cantidadEnemigos(
            int cantidadEnemigos) {

        this.cantidadEnemigos = cantidadEnemigos;
        return this;
    }

    public PantallaJuego build() {

        return new PantallaJuego(
                game,
                ronda,
                vidas,
                score,
                velXEnemigos,
                velYEnemigos,
                cantidadEnemigos
        );
    }
}