package puppy.code;

/** Operaciones que la nave necesita del propietario de los recursos. */
public interface ServiciosNave {
    Bullet crearBala(float x, float y, float velocidadX, float velocidadY);
    void reproducirDisparo();
    void reproducirHerida();
}
