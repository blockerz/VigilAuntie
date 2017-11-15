package com.lofisoftware.vigilauntie;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lofisoftware.vigilauntie.entity.Entity;
import com.lofisoftware.vigilauntie.map.MapManager;

import static com.lofisoftware.vigilauntie.Constants.CELL_HEIGHT;
import static com.lofisoftware.vigilauntie.Constants.CELL_WIDTH;
import static com.lofisoftware.vigilauntie.Constants.SCREEN_HEIGHT_CELLS;
import static com.lofisoftware.vigilauntie.Constants.SCREEN_WIDTH_CELLS;
import static com.lofisoftware.vigilauntie.Constants.STATUS_HEIGHT_CELLS;
import static com.lofisoftware.vigilauntie.Constants.UI_SCALE_FACTOR;

public class PlayerHUD implements Screen {
    private static final String TAG = PlayerHUD.class.getSimpleName();

    private Stage stage;
    private Viewport viewport;
    private Camera camera;
    private Entity player;

    private StatusUI statusUI;


    public PlayerHUD(OrthographicCamera hudCamera, Entity player, MapManager mapManager) {
        camera = hudCamera;
        viewport = new FitViewport(SCREEN_WIDTH_CELLS * CELL_WIDTH * UI_SCALE_FACTOR, SCREEN_HEIGHT_CELLS * CELL_HEIGHT * UI_SCALE_FACTOR,camera);
        stage = new Stage(viewport);
        //stage.setDebugAll(true);

        statusUI = new StatusUI();
        statusUI.setVisible(true);
        statusUI.setPosition(0, SCREEN_HEIGHT_CELLS * CELL_HEIGHT * UI_SCALE_FACTOR);
        statusUI.setSize(SCREEN_WIDTH_CELLS*CELL_WIDTH * UI_SCALE_FACTOR,STATUS_HEIGHT_CELLS*CELL_HEIGHT * UI_SCALE_FACTOR);
        //statusUI.setKeepWithinStage(false);
        //statusUI.setMovable(false);

        stage.addActor(statusUI);

        statusUI.validate();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        statusUI.update();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }
}