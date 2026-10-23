package puppy.code;

import puppy.code.input.ControlesNave;

/** Puerto de creación de entidades y servicios que usa la lógica de una ronda. */
public interface ServiciosPartida extends ServiciosNave {
    Nave crearNave(int x, int y, int vidas, ControlesNave controles);
    NaveEnemiga crearNaveEnemiga(boolean artillera, int x, int y,
        int velocidadX, int velocidadY);
    void reproducirExplosion();
}
