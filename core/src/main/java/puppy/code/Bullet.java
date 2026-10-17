package puppy.code;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;


public class Bullet {

	private float xSpeed;
	private float ySpeed;
	private boolean destroyed = false;
	private Sprite spr;

	    public Bullet(float centroX, float centroY, float xSpeed, float ySpeed, Texture tx) {
	    	spr = new Sprite(tx);
        spr.setPosition(centroX - spr.getWidth() / 2,
            centroY - spr.getHeight() / 2);
	        this.xSpeed = xSpeed;
	        this.ySpeed = ySpeed;
	        spr.setOriginCenter();
	        spr.setRotation(MathUtils.atan2(ySpeed, xSpeed) * MathUtils.radiansToDegrees - 90);
	    }
	    public void update(float delta) {
	        spr.setPosition(spr.getX()+xSpeed*delta, spr.getY()+ySpeed*delta);
	        if (spr.getX() < 0 || spr.getX()+spr.getWidth() > AreaJuego.ANCHO) {
	            destroyed = true;
	        }
	        if (spr.getY() < 0 || spr.getY()+spr.getHeight() > AreaJuego.ALTO) {
	        	destroyed = true;
	        }

	    }

	    public void draw(SpriteBatch batch) {
	    	spr.draw(batch);
	    }

	    public boolean checkCollision(Ball2 b2) {
	        if(spr.getBoundingRectangle().overlaps(b2.getArea())){
	        	// Se destruyen ambos
	            this.destroyed = true;
	            return true;

	        }
	        return false;
	    }

	    public boolean isDestroyed() {return destroyed;}

}
