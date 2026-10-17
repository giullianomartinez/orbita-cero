package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class Ball2 {
    private float x;
    private float y;
    private int xSpeed;
    private int ySpeed;
    private Sprite spr;

    public Ball2(int x, int y, int size, int xSpeed, int ySpeed, Texture tx) {
    	spr = new Sprite(tx);
        spr.setSize(size * 2, size * 2);
        this.x = MathUtils.clamp(x, 0, AreaJuego.ANCHO - spr.getWidth());
        this.y = MathUtils.clamp(y, 0, AreaJuego.ALTO - spr.getHeight());
        spr.setPosition(this.x, this.y);
        this.setXSpeed(xSpeed);
        this.setySpeed(ySpeed);
    }
    public void update(float delta) {
        float paso = Math.min(delta, 0.05f) * 60;
        x += getXSpeed() * paso;
        y += getySpeed() * paso;

        if (x < 0 || x + spr.getWidth() > AreaJuego.ANCHO) {
            x = MathUtils.clamp(x, 0, AreaJuego.ANCHO - spr.getWidth());
            setXSpeed(-getXSpeed());
        }
        if (y < 0 || y + spr.getHeight() > AreaJuego.ALTO) {
            y = MathUtils.clamp(y, 0, AreaJuego.ALTO - spr.getHeight());
            setySpeed(-getySpeed());
        }
        spr.setPosition(x, y);
    }
    
    public Rectangle getArea() {
    	return spr.getBoundingRectangle();
    }
    public void draw(SpriteBatch batch) {
    	spr.draw(batch);
    }
    
    public void checkCollision(Ball2 b2) {
        Rectangle a = spr.getBoundingRectangle();
        Rectangle b = b2.spr.getBoundingRectangle();
        Vector2 desplazamiento = ColisionAsteroides.desplazamiento(a, b);
        if (desplazamiento.isZero()) return;
        Vector2[] separaciones = ColisionAsteroides.separaciones(
            a, b, AreaJuego.ANCHO, AreaJuego.ALTO
        );

        if (desplazamiento.x != 0) {
            x += separaciones[0].x;
            b2.x += separaciones[1].x;
            float direccion = Math.signum(desplazamiento.x);
            int[] velocidades = ColisionAsteroides.velocidadesTrasChoque(
                xSpeed, b2.xSpeed, direccion
            );
            xSpeed = velocidades[0];
            b2.xSpeed = velocidades[1];
        } else {
            y += separaciones[0].y;
            b2.y += separaciones[1].y;
            float direccion = Math.signum(desplazamiento.y);
            int[] velocidades = ColisionAsteroides.velocidadesTrasChoque(
                ySpeed, b2.ySpeed, direccion
            );
            ySpeed = velocidades[0];
            b2.ySpeed = velocidades[1];
        }
        x = MathUtils.clamp(x, 0, AreaJuego.ANCHO - spr.getWidth());
        y = MathUtils.clamp(y, 0, AreaJuego.ALTO - spr.getHeight());
        b2.x = MathUtils.clamp(b2.x, 0, AreaJuego.ANCHO - b2.spr.getWidth());
        b2.y = MathUtils.clamp(b2.y, 0, AreaJuego.ALTO - b2.spr.getHeight());
        spr.setPosition(x, y);
        b2.spr.setPosition(b2.x, b2.y);
    }
	public int getXSpeed() {
		return xSpeed;
	}
	public void setXSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}
	public int getySpeed() {
		return ySpeed;
	}
	public void setySpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}
	
    
}
