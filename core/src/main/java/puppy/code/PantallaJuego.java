package puppy.code;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import puppy.code.input.ControlesTecladoNave;


public class PantallaJuego implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;
	private Viewport viewport;
	private SpriteBatch batch;
	private Texture txAsteroide;
	private Sound explosionSound;
	private Music gameMusic;
	private int score;
	private int ronda;
	private int velXAsteroides;
	private int velYAsteroides;
	private int cantAsteroides;

	private Nave4 nave;
	private  ArrayList<Ball2> balls1 = new ArrayList<>();
	private  ArrayList<Bullet> balas = new ArrayList<>();


	public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,
			int velXAsteroides, int velYAsteroides, int cantAsteroides) {
		this.game = game;
		this.ronda = ronda;
		this.score = score;
		this.velXAsteroides = velXAsteroides;
		this.velYAsteroides = velYAsteroides;
		this.cantAsteroides = cantAsteroides;

		batch = game.getBatch();
		camera = new OrthographicCamera();
		viewport = new FitViewport(AreaJuego.ANCHO, AreaJuego.ALTO, camera);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		//inicializar assets; musica de fondo y efectos de sonido
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
		explosionSound.setVolume(1,0.5f);
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav")); //

		gameMusic.setLooping(true);
		gameMusic.setVolume(0.5f);
		gameMusic.play();

	    // cargar imagen de la nave, 64x64
	    nave = new Nave4(400-22,30,new Texture(Gdx.files.internal("MainShip3.png")),
            Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")),
            new Texture(Gdx.files.internal("Rocket2.png")),
            Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3")), vidas,
            new ControlesTecladoNave(viewport));

        //crear asteroides
        txAsteroide = new Texture(Gdx.files.internal("aGreyMedium4.png"));
        Random r = new Random();
	    for (int i = 0; i < cantAsteroides; i++) {
	        Ball2 bb = new Ball2(r.nextInt(AreaJuego.ANCHO),
            50+r.nextInt(AreaJuego.ALTO-50),
            20+r.nextInt(10), velXAsteroides+r.nextInt(4), velYAsteroides+r.nextInt(4),
            txAsteroide);
	  	    balls1.add(bb);
	  	}
	}

	public void dibujaEncabezado() {
		CharSequence str = "Vidas: "+nave.getVidas()+" Ronda: "+ronda;
		game.getFont().getData().setScale(2f);
		game.getFont().draw(batch, str, 10, 30);
		game.getFont().draw(batch, "Score:"+this.score, AreaJuego.ANCHO-150, 30);
		game.getFont().draw(batch, "HighScore:"+game.getHighScore(), AreaJuego.ANCHO/2-100, 30);
	}
	@Override
	public void render(float delta) {
		float paso = Math.min(Math.max(delta, 0), 0.05f);
		viewport.apply();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		nave.update(paso, this);
		  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
          batch.begin();
		  dibujaEncabezado();
		      // colisiones entre balas y asteroides y su destruccion
	    	  for (int i = 0; i < balas.size(); i++) {
		            Bullet b = balas.get(i);
		            b.update(paso);
		            for (int j = 0; j < balls1.size(); j++) {
		              if (b.checkCollision(balls1.get(j))) {
		            	 explosionSound.play();
		            	 balls1.remove(j);
		            	 j--;
		            	 score +=10;
		              }
		  	        }

		         //   b.draw(batch);
		            if (b.isDestroyed()) {
		                balas.remove(b);
		                i--; //para no saltarse 1 tras eliminar del arraylist
		            }
		      }
		      //actualizar movimiento de asteroides dentro del area
	        for (Ball2 ball : balls1) {
	            ball.update(paso);
		      }
		      //colisiones entre asteroides y sus rebotes
		      for (int i=0;i<balls1.size();i++) {
            Ball2 ball1 = balls1.get(i);
		        for (int j=i+1;j<balls1.size();j++) {
		          ball1.checkCollision(balls1.get(j));
		        }
		      }
	      //dibujar balas
	     for (Bullet b : balas) {
	          b.draw(batch);
	      }
	      nave.draw(batch);
	      //dibujar asteroides y manejar colision con nave
	      for (int i = 0; i < balls1.size(); i++) {
	    	    Ball2 b=balls1.get(i);
	    	    b.draw(batch);
		          //perdió vida o game over
	              if (nave.checkCollision(b)) {
		            //asteroide se destruye con el choque
	            	 balls1.remove(i);
	            	 i--;
              }
  	        }

	      batch.end();

	      if (nave.estaDestruido()) {
	          if (score > game.getHighScore()) {
	              game.setHighScore(score);
	          }

	          Screen ss = new PantallaGameOver(game);
	          ss.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	          game.setScreen(ss);
	          dispose();
	          return;
	      }

	      // Si no quedan vidas, permanece aquí hasta que termine
	      // la protección y se pueda mostrar Game Over.
	      if (balls1.isEmpty() && nave.getVidas() > 0) {
	          Screen ss = new PantallaJuego(
	              game,
	              ronda + 1,
	              nave.getVidas(),
	              score,
	              velXAsteroides + 3,
	              velYAsteroides + 3,
	              cantAsteroides + 10
	          );
	          ss.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	          game.setScreen(ss);
	          dispose();
	      }

	}

    public boolean agregarBala(Bullet bb) {
    	return balas.add(bb);
    }

	@Override
	public void show() {
		// TODO Auto-generated method stub
		gameMusic.play();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		this.explosionSound.dispose();
		this.gameMusic.dispose();
		this.txAsteroide.dispose();
		this.nave.dispose();
	}

}
