package puppy.code.input;

/** Estado de los controles leído una sola vez por frame. */
public final class EntradaNave {
    private final float horizontal;
    private final float vertical;
    private final float apuntarX;
    private final float apuntarY;
    private final boolean disparar;

    public EntradaNave(float horizontal, float vertical, float apuntarX,
                       float apuntarY, boolean disparar) {
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.apuntarX = apuntarX;
        this.apuntarY = apuntarY;
        this.disparar = disparar;
    }

    public float horizontal() { return horizontal; }
    public float vertical() { return vertical; }
    public float apuntarX() { return apuntarX; }
    public float apuntarY() { return apuntarY; }
    public boolean disparar() { return disparar; }
}
