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
    private GestorRecursos recursos;
    private int score;
    private int ronda;
    private int velXEnemigos;
    private int velYEnemigos;
    private int cantidadEnemigos;
    private Nave4 nave;
    private ArrayList<NaveEnemiga> enemigos = new ArrayList<>();
    private ArrayList<Bullet> balas = new ArrayList<>();

    public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score, int velXEnemigos, int velYEnemigos, int cantidadEnemigos) {
    
        this.game = game;
        this.ronda = ronda;
        this.score = score;
        
   		this.velXEnemigos = velXEnemigos;
		this.velYEnemigos = velYEnemigos;
		this.cantidadEnemigos = cantidadEnemigos;
		
		recursos = GestorRecursos.getInstancia();
		
        batch = game.getBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(AreaJuego.ANCHO, AreaJuego.ALTO, camera);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        
        

        nave = new Nave4(400 - 22,30,recursos.getTexturaNave(),recursos.getSonidoHerido(),recursos.getTexturaBala(),recursos.getSonidoDisparo(),vidas,new ControlesTecladoNave(viewport));
        if (ronda >= 3) {
            nave.setEstrategiaDisparo(new DisparoTriple());
        }
        
        Random r = new Random();
        for (int i = 0; i < cantidadEnemigos; i++) {
            int posX = r.nextInt(AreaJuego.ANCHO - 60);
            int posY = 160 + r.nextInt(AreaJuego.ALTO - 220);
            int velX = velXEnemigos + r.nextInt(3);
            int velY = velYEnemigos + r.nextInt(3);
            if (i % 3 == 0) {
               enemigos.add(new NaveArtillera(posX,posY,velX,velY,recursos.getTexturaNaveArtillera()));
      
            } else {
            	enemigos.add(new NaveCaza(posX,posY,velX,velY,recursos.getTexturaNaveCaza()));
       
            }
        }
    }

    

    public void dibujaEncabezado() {
        CharSequence str = "Vidas: " + nave.getVidas() + "  Ronda: " + ronda;
        game.getFont().getData().setScale(2f);
        game.getFont().draw(batch, str, 10, 30);
        game.getFont().draw(batch, "Score: " + this.score, AreaJuego.ANCHO - 180, 30);
        game.getFont().draw(batch, "HighScore: " + game.getHighScore(), AreaJuego.ANCHO / 2f - 60, 30);
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
        for (int i = 0; i < balas.size(); i++) {
            Bullet b = balas.get(i);
            b.update(paso);
            if (b.esOrigenJugador()) {
                for (int j = 0; j < enemigos.size(); j++) {
                    NaveEnemiga enemigo = enemigos.get(j);
                    if (b.checkCollision(enemigo)) {
                        recursos.getSonidoExplosion().play();
                        if (enemigo.recibirImpacto(1)) {
                            score += enemigo.getPuntajeRecompensa();
                            enemigos.remove(j);
                            j--;
                        }
                        break;
                    }
                }
            } else {
                nave.checkCollisionBalaEnemiga(b);
            }
            if (b.isDestroyed()) {
                balas.remove(i);
                i--;
            }
        }
        for (NaveEnemiga enemigo : enemigos) {
            enemigo.update(paso, nave.getCentroX(), nave.getCentroY(), recursos.getTexturaBala(), balas);
        }
        for (int i = 0; i < enemigos.size(); i++) {
            NaveEnemiga e1 = enemigos.get(i);
            for (int j = i + 1; j < enemigos.size(); j++) {
                e1.checkCollision(enemigos.get(j));
            }
        }
        for (Bullet b : balas) {
            b.draw(batch);
        }
        nave.draw(batch);
        for (int i = 0; i < enemigos.size(); i++) {
            NaveEnemiga enemigo = enemigos.get(i);
            enemigo.draw(batch);
            if (nave.checkCollision(enemigo)) {
                enemigos.remove(i);
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
        if (enemigos.isEmpty() && nave.getVidas() > 0) {
            Screen ss =
		    new PantallaJuegoBuilder(game)
		        .ronda(ronda + 1)
		        .vidas(nave.getVidas())
		        .score(score)
		        .velocidadEnemigos(
		            velXEnemigos + 1,
		            velYEnemigos + 1
		        )
		        .cantidadEnemigos(
		            cantidadEnemigos + 2
		        )
		        .build();
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
        recursos.getMusicaJuego().play();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
