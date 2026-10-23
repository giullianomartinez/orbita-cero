package puppy.code;

import java.util.Objects;

/** Construye una pantalla con los datos inmutables de una ronda. */
public final class PantallaJuegoBuilder {
    private final SpaceNavigation game;
    private int ronda = 1;
    private int vidas = 3;
    private int puntaje;
    private int velocidadX = 1;
    private int velocidadY = 1;
    private int navesEnemigas = 10;

    public PantallaJuegoBuilder(SpaceNavigation game) {
        this.game = Objects.requireNonNull(game, "game");
    }

    public PantallaJuegoBuilder(SpaceNavigation game, EstadoPartida estado) {
        this.game = Objects.requireNonNull(game, "game");
        Objects.requireNonNull(estado, "estado");
        ronda = estado.ronda();
        vidas = estado.vidas();
        puntaje = estado.puntaje();
        velocidadX = estado.velocidadX();
        velocidadY = estado.velocidadY();
        navesEnemigas = estado.navesEnemigas();
    }

    public PantallaJuegoBuilder ronda(int ronda) { this.ronda = ronda; return this; }
    public PantallaJuegoBuilder vidas(int vidas) { this.vidas = vidas; return this; }
    public PantallaJuegoBuilder puntaje(int puntaje) { this.puntaje = puntaje; return this; }

    public PantallaJuegoBuilder velocidadNaves(int velocidadX, int velocidadY) {
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        return this;
    }

    public PantallaJuegoBuilder cantidadNaves(int cantidad) {
        navesEnemigas = cantidad;
        return this;
    }

    public PantallaJuego build() {
        EstadoPartida estado = EstadoPartida.crear(ronda, vidas, puntaje,
            velocidadX, velocidadY, navesEnemigas);
        return new PantallaJuego(game, estado);
    }
}
