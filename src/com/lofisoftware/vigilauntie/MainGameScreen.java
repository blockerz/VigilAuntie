package com.lofisoftware.vigilauntie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.lofisoftware.vigilauntie.entity.Entity;
import com.lofisoftware.vigilauntie.entity.EntityFactory;
import com.lofisoftware.vigilauntie.map.MapManager;
import com.lofisoftware.vigilauntie.map.MapStage;

import static com.lofisoftware.vigilauntie.Constants.*;

public class MainGameScreen implements Screen {

    private static final String TAG = MainGameScreen.class.getSimpleName();

    public static class VIEWPORT {
        public static float viewportWidth;
        public static float viewportHeight;
        public static float virtualWidth;
        public static float virtualHeight;
        public static float physicalWidth;
        public static float physicalHeight;
        public static float aspectRatio;
    }

    public static enum GameState {
        SAVING,
        LOADING,
        RUNNING,
        PAUSED,
        GAME_OVER
    }



    private static GameState gameState;

    private Json json;
    private VigilAuntie game;
    private InputMultiplexer multiplexer;
    protected OrthographicCamera hudCamera = null;
    private PlayerHUD playerHUD;

    SpriteBatch batch;

    float frameTime, lastUpdateTime = 0f;

    MapStage mapWindow1;
    Entity player;

    //private Coord cursor, player;

    public MainGameScreen(VigilAuntie game) {
        this.game = game;
        json = new Json();

        setGameState(GameState.RUNNING);

        setupViewport(SCREEN_WIDTH_CELLS*CELL_WIDTH,SCREEN_HEIGHT_CELLS*CELL_HEIGHT);

        //CP437FntGenerator.writeFile(Gdx.files.internal("rex/cp437_20x20.fnt").file(), CP437FntGenerator.generatecp437fnt("cp437_20x20", 20, 20));

        batch = new SpriteBatch();

        player = EntityFactory.getInstance().getEntity(EntityFactory.EntityType.PLAYER);
        MapManager.getMapManager().setPlayer(player);

        mapWindow1 = new MapStage(SCREEN_WIDTH_CELLS,10,0,BUTTON_HEIGHT_CELLS, batch);
        //mapWindow2 = new MapStage(30,20,30,25, MapManager.getMapManager().player2, batch);

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, VIEWPORT.virtualWidth, VIEWPORT.virtualHeight);

        playerHUD = new PlayerHUD(hudCamera, player, MapManager.getMapManager());

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(mapWindow1.getStage());
        multiplexer.addProcessor(player.getInputProcessor());
        Gdx.input.setInputProcessor(multiplexer);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        frameTime = (frameTime + delta)%1000000;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        MapManager.getMapManager().update(delta);

        mapWindow1.render(delta);
        //mapWindow2.render(delta);
        playerHUD.render(delta);

        /*
        if (Gdx.input.isKeyPressed(Input.Keys.I) && frameTime-lastUpdateTime > 0.2f) {
            MapManager.getMapManager().player1 = MapManager.getMapManager().player1.translate(0,-1);
            Gdx.app.log(TAG, "player1 pos: " + MapManager.getMapManager().player1.toString());
            lastUpdateTime = frameTime;
            MapManager.getMapManager().updateMap();
            //camera.position.add(0, CELL_HEIGHT, 0);
            //Gdx.app.log(TAG, "Camera pos: " + camera.position.toString());
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.J) && frameTime-lastUpdateTime > 0.2f) {
            MapManager.getMapManager().player1 = MapManager.getMapManager().player1.translate(-1,0);
            Gdx.app.log(TAG, "player1 pos: " + MapManager.getMapManager().player1.toString());
            lastUpdateTime = frameTime;
            MapManager.getMapManager().updateMap();
            //camera.position.add(-CELL_WIDTH,0,0);
            //Gdx.app.log(TAG, "Camera pos: " + camera.position.toString());
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.K) && frameTime-lastUpdateTime > 0.2f) {
            MapManager.getMapManager().player1 = MapManager.getMapManager().player1.translate(0,1);
            Gdx.app.log(TAG, "player1 pos: " + MapManager.getMapManager().player1.toString());
            lastUpdateTime = frameTime;
            MapManager.getMapManager().updateMap();
            //camera.position.add(0,-CELL_HEIGHT,0);
            //Gdx.app.log(TAG, "Camera pos: " + camera.position.toString());
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.L) && frameTime-lastUpdateTime > 0.2f) {
            MapManager.getMapManager().player1 = MapManager.getMapManager().player1.translate(1,0);
            Gdx.app.log(TAG, "player1 pos: " + MapManager.getMapManager().player1.toString());
            lastUpdateTime = frameTime;
            MapManager.getMapManager().updateMap();
            //camera.position.add(CELL_WIDTH,0,0);
            //Gdx.app.log(TAG, "Camera pos: " + camera.position.toString());
        }
        */

    }

    @Override
    public void resize(int width, int height) {
        setupViewport(SCREEN_WIDTH_CELLS*CELL_HEIGHT,SCREEN_HEIGHT_CELLS*CELL_HEIGHT);
        //camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
        //stage.getViewport().update(width, height);
        mapWindow1.resize(width,height);
        //mapWindow2.resize(width,height);
        playerHUD.resize(width,height);
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

    /*
    public void putMap()
    {

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Map.Cell cell = currentMap.getCell(i,j);
                if (cell != null) {
                    display.put(i, j, cell.c, currentMap.getPalette(), cell.foregroundColor, cell.backgroundColor, -255);
                }
            }
        }
        for (Coord pt : toCursor)
        {
            // use a brighter light to trace the path to the cursor, from 170 max lightness to 0 min.
            display.highlight(pt.x, pt.y, 100);
        }
        //places the player as an '@' at his position in orange (6 is an index into SColor.LIMITED_PALETTE).
        //display.put(player.x, player.y, '@', 6);
    }*/

    public static void setGameState(GameState gameState){
        switch(gameState){
            case RUNNING:
                gameState = GameState.RUNNING;
                break;
            case LOADING:
                //ProfileManager.getInstance().loadProfile();
                gameState = GameState.RUNNING;
                break;
            case SAVING:
                //ProfileManager.getInstance().saveProfile();
                gameState = GameState.PAUSED;
                break;
            case PAUSED:
                if( gameState == GameState.PAUSED ){
                    gameState = GameState.RUNNING;
                }else if( gameState == GameState.RUNNING ){
                    gameState = GameState.PAUSED;
                }
                break;
            case GAME_OVER:
                gameState = GameState.GAME_OVER;
                break;
            default:
                gameState = GameState.RUNNING;
                break;
        }

    }

    private void setupViewport(int width, int height){
        //Make the viewport a percentage of the total display area
        VIEWPORT.virtualWidth = width;
        VIEWPORT.virtualHeight = height;

        //Current viewport dimensions
        VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
        VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;

        //pixel dimensions of display
        VIEWPORT.physicalWidth = Gdx.graphics.getWidth();
        VIEWPORT.physicalHeight = Gdx.graphics.getHeight();

        //aspect ratio for current viewport
        VIEWPORT.aspectRatio = (VIEWPORT.virtualWidth / VIEWPORT.virtualHeight);

        //update viewport if there could be skewing
        if( VIEWPORT.physicalWidth / VIEWPORT.physicalHeight >= VIEWPORT.aspectRatio){
            //Letterbox left and right
            VIEWPORT.viewportWidth = VIEWPORT.viewportHeight * (VIEWPORT.physicalWidth/VIEWPORT.physicalHeight);
            VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
        }else{
            //letterbox above and below
            VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
            VIEWPORT.viewportHeight = VIEWPORT.viewportWidth * (VIEWPORT.physicalHeight/VIEWPORT.physicalWidth);
        }

        Gdx.app.debug(TAG, "WorldRenderer: virtual: (" + VIEWPORT.virtualWidth + "," + VIEWPORT.virtualHeight + ")" );
        Gdx.app.debug(TAG, "WorldRenderer: viewport: (" + VIEWPORT.viewportWidth + "," + VIEWPORT.viewportHeight + ")" );
        Gdx.app.debug(TAG, "WorldRenderer: physical: (" + VIEWPORT.physicalWidth + "," + VIEWPORT.physicalHeight + ")" );
    }

}
