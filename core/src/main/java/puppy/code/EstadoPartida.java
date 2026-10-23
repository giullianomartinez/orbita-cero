package puppy.code;

/** Configuración inmutable de una ronda y reglas para avanzar a la siguiente. */
public final class EstadoPartida {
    private final int ronda;
    private final int vidas;
    private final int puntaje;
    private final int velocidadX;
    private final int velocidadY;
    private final int navesEnemigas;

    private EstadoPartida(int ronda, int vidas, int puntaje, int velocidadX,
                          int velocidadY, int navesEnemigas) {
        if (ronda < 1 || vidas < 0 || puntaje < 0 || velocidadX < 1
                || velocidadY < 1 || navesEnemigas < 1) {
            throw new IllegalArgumentException("Estado de partida inválido");
        }
        this.ronda = ronda;
        this.vidas = vidas;
        this.puntaje = puntaje;
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        this.navesEnemigas = navesEnemigas;
    }

    static EstadoPartida crear(int ronda, int vidas, int puntaje, int velocidadX,
                               int velocidadY, int navesEnemigas) {
        return new EstadoPartida(ronda, vidas, puntaje, velocidadX,
            velocidadY, navesEnemigas);
    }

    public static EstadoPartida nuevaPartida() {
        return new EstadoPartida(1, 3, 0, 1, 1, 10);
    }

    public EstadoPartida siguienteRonda(int vidasRestantes, int puntajeActual) {
        return new EstadoPartida(ronda + 1, vidasRestantes, puntajeActual,
            Math.min(velocidadX + 1, 4), Math.min(velocidadY + 1, 4),
            Math.min(navesEnemigas + 2, 40));
    }

    public int ronda() { return ronda; }
    public int vidas() { return vidas; }
    public int puntaje() { return puntaje; }
    public int velocidadX() { return velocidadX; }
    public int velocidadY() { return velocidadY; }
    public int navesEnemigas() { return navesEnemigas; }
}
