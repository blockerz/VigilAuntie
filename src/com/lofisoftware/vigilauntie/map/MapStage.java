package com.lofisoftware.vigilauntie.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lofisoftware.vigilauntie.entity.AnimatedImage;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static com.lofisoftware.vigilauntie.Constants.CELL_HEIGHT;
import static com.lofisoftware.vigilauntie.Constants.CELL_WIDTH;
import static com.lofisoftware.vigilauntie.Constants.SCREEN_HEIGHT_CELLS;
import static com.lofisoftware.vigilauntie.Constants.SCREEN_WIDTH_CELLS;

public class MapStage {

    private static final String TAG = MapStage.class.getSimpleName();

    private Map map;
    private OrthographicCamera camera = new OrthographicCamera();
    private OrthogonalTiledMapRenderer mapRenderer;
    private Stage stage;
    private int mapHeight;

    float frameTime, lastUpdateTime = 0f;

    public MapStage (int width, int height, int x, int y, SpriteBatch batch) {

        camera = new OrthographicCamera();
        camera.setToOrtho(false, width * CELL_WIDTH, height * CELL_HEIGHT);
        stage = new Stage(new FitViewport(SCREEN_WIDTH_CELLS * CELL_WIDTH, SCREEN_HEIGHT_CELLS * CELL_HEIGHT, camera), batch);

        mapHeight = y * (Gdx.graphics.getHeight()/SCREEN_HEIGHT_CELLS);

        //camera.position.set(x * CELL_WIDTH, y * CELL_HEIGHT,0);
        Gdx.app.log(TAG,"Camera pos: " + camera.position.toString());

        //display = new MapDisplay(width, height);



        //display.setDebug(true);
        map = MapManager.getMapManager().getCurrentMap();
        map.setSize(width * CELL_WIDTH, height * CELL_HEIGHT);
        map.setPosition(x * CELL_WIDTH, y * CELL_HEIGHT);
        map.setEntity(1, 1, MapManager.getMapManager().getPlayer());
        //map.setCamera(camera);

        //stage.setDebugAll(true);
        //stage.addActor(display);
        //stage.addActor(MapManager.getMapManager().getPlayer().getImage());
        stage.addActor(map);
        mapRenderer = new OrthogonalTiledMapRenderer(map.getTiledMap());
    }

    public Stage getStage() {
        return stage;
    }

    public void sortActors () {
        Collections.sort(Arrays.asList(stage.getActors().toArray()), new ActorComparator());
    }

    public void render(float delta) {
        frameTime = (frameTime + delta)%1000000;

        //MapManager.getMapManager().getPlayer().updateAnimations(delta);


        //MapManager.getMapManager().getPlayer().update(stage.getBatch(), delta);
        //display.update();
        //stage.getViewport().apply();

        //sortActors();

        Gdx.gl.glViewport(0,mapHeight, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.act();
        stage.draw();

        if (Gdx.input.isKeyPressed(Input.Keys.I) && frameTime-lastUpdateTime > 0.2f) {
//            MapManager.getMapManager().player1 = MapManager.getMapManager().player1.translate(0,-1);
//            Gdx.app.log(TAG, "player1 pos: " + MapManager.getMapManager().player1.toString());
            lastUpdateTime = frameTime;
//            MapManager.getMapManager().updateMap();
            camera.position.add(0, CELL_HEIGHT, 0);
            Gdx.app.log(TAG, "Camera pos: " + camera.position.toString());
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.J) && frameTime-lastUpdateTime > 0.2f) {
//            MapManager.getMapManager().player1 = MapManager.getMapManager().player1.translate(-1,0);
//            Gdx.app.log(TAG, "player1 pos: " + MapManager.getMapManager().player1.toString());
            lastUpdateTime = frameTime;
//            MapManager.getMapManager().updateMap();
            camera.position.add(-CELL_WIDTH, 0, 0);
            Gdx.app.log(TAG, "Camera pos: " + camera.position.toString());
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.K) && frameTime-lastUpdateTime > 0.2f) {
//            MapManager.getMapManager().player1 = MapManager.getMapManager().player1.translate(0,1);
//            Gdx.app.log(TAG, "player1 pos: " + MapManager.getMapManager().player1.toString());
            lastUpdateTime = frameTime;
//            MapManager.getMapManager().updateMap();
            camera.position.add(0, -CELL_HEIGHT, 0);
            Gdx.app.log(TAG, "Camera pos: " + camera.position.toString());
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.L) && frameTime-lastUpdateTime > 0.2f) {
//            MapManager.getMapManager().player1 = MapManager.getMapManager().player1.translate(1,0);
//            Gdx.app.log(TAG, "player1 pos: " + MapManager.getMapManager().player1.toString());
            lastUpdateTime = frameTime;
//            MapManager.getMapManager().updateMap();
            camera.position.add(CELL_WIDTH, 0, 0);
            Gdx.app.log(TAG, "Camera pos: " + camera.position.toString());
        }/*
        else if (Gdx.input.isKeyPressed(Input.Keys.W) && frameTime-lastUpdateTime > 0.2f) {

            if (!MapManager.getMapManager().getPlayer().move(Direction.UP))
                Gdx.app.debug(TAG, "Collision: " + Direction.UP.toString());

            lastUpdateTime = frameTime;
            Gdx.app.log(TAG, "Player pos: " + MapManager.getMapManager().getPlayer().getCurrentPosition().toString());
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.A) && frameTime-lastUpdateTime > 0.2f) {

            if (!MapManager.getMapManager().getPlayer().move(Direction.LEFT))
                Gdx.app.debug(TAG, "Collision: " + Direction.LEFT.toString());

            lastUpdateTime = frameTime;
            Gdx.app.log(TAG, "Player pos: " + MapManager.getMapManager().getPlayer().getCurrentPosition().toString());
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S) && frameTime-lastUpdateTime > 0.2f) {

            if (!MapManager.getMapManager().getPlayer().move(Direction.DOWN))
                Gdx.app.debug(TAG, "Collision: " + Direction.DOWN.toString());

            lastUpdateTime = frameTime;
            Gdx.app.log(TAG, "Player pos: " + MapManager.getMapManager().getPlayer().getCurrentPosition().toString());
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D) && frameTime-lastUpdateTime > 0.2f) {

            if (!MapManager.getMapManager().getPlayer().move(Direction.RIGHT))
                Gdx.app.debug(TAG, "Collision: " + Direction.RIGHT.toString());

            lastUpdateTime = frameTime;
            Gdx.app.log(TAG, "Player pos: " + MapManager.getMapManager().getPlayer().getCurrentPosition().toString());
        }
        */

    }

    public void resize(int width, int height) {
        stage.getViewport().update(width * CELL_WIDTH, height * CELL_HEIGHT);
    }

    public Camera getCamera() {
        return stage.getCamera();
    }

    public class ActorComparator implements Comparator<Actor> {
        @Override
        public int compare(Actor arg0, Actor arg1) {
            if (arg0.getY() < arg1.getY()) {
                return -1;
            } else if (arg0.getX() == arg1.getX()) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}
