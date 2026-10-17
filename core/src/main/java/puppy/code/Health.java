package puppy.code;

public class Health {
    private static final int MAX_LIVES = 3;
    private static final float PROTECTION_TIME = 1.0f;

    private int lives;
    private float protection;

    public Health(int initialLives) {
        if (initialLives < 0 || initialLives > MAX_LIVES) {
            throw new IllegalArgumentException("Vidas iniciales inválidas");
        }

        lives = initialLives;
    }

    public int lives() {
        return lives;
    }

    public boolean isProtected() {
        return protection > 0;
    }

    public boolean isDestroyed() {
        return lives == 0;
    }

    public void update(float dt) {
        protection = Math.max(0, protection - dt);
    }

    public boolean hit() {
        if (isProtected() || isDestroyed()) {
            return false;
        }

        lives--;
        protection = PROTECTION_TIME;
        return true;
    }

    public void restore() {
        lives = MAX_LIVES;
        protection = 0;
    }
}