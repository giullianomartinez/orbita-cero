package puppy.code;

/** Datos y reglas para iniciar una partida y avanzar de ronda. */
public final class EstadoPartida {
    private final int ronda, vidas, puntaje, velocidadX, velocidadY, asteroides;

    private EstadoPartida(int ronda, int vidas, int puntaje, int velocidadX,
                          int velocidadY, int asteroides) {
        this.ronda = ronda;
        this.vidas = vidas;
        this.puntaje = puntaje;
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        this.asteroides = asteroides;
    }

    public static EstadoPartida nuevaPartida() {
        return new EstadoPartida(1, 3, 0, 1, 1, 10);
    }

    public EstadoPartida siguienteRonda(int vidasRestantes, int puntajeActual) {
        return new EstadoPartida(ronda + 1, vidasRestantes, puntajeActual,
            Math.min(velocidadX + 1, 4), Math.min(velocidadY + 1, 4),
            Math.min(asteroides + 10, 20));
    }

    public int ronda() { return ronda; }
    public int vidas() { return vidas; }
    public int puntaje() { return puntaje; }
    public int velocidadX() { return velocidadX; }
    public int velocidadY() { return velocidadY; }
    public int asteroides() { return asteroides; }
}
