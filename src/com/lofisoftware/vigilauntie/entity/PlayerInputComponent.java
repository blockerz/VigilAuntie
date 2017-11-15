package com.lofisoftware.vigilauntie.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.lofisoftware.vigilauntie.MainGameScreen;
import com.lofisoftware.vigilauntie.command.Command;
import com.lofisoftware.vigilauntie.command.KickCommand;
import com.lofisoftware.vigilauntie.command.MoveCommand;
import com.lofisoftware.vigilauntie.command.PunchCommand;
import com.lofisoftware.vigilauntie.command.WaitCommand;
import com.lofisoftware.vigilauntie.map.Direction;

import java.util.HashMap;
import java.util.Map;

public class PlayerInputComponent extends InputComponent implements InputProcessor {

    private final static String TAG = PlayerInputComponent.class.getSimpleName();
    private Vector3 _lastMouseCoordinates;
    float frameTime = 0f, lastUpdateTime = 0f;
    private static float UPDATE_DELAY = 0.2f;

    private Command nextCommand;

    public PlayerInputComponent(){
        this._lastMouseCoordinates = new Vector3();
    }

    //protected PositionComponent.Direction currentDirection = null;
    //protected Entity.State currentState = null;
    protected Json json;

    protected enum Keys {
        LEFT, RIGHT, UP, DOWN, WAIT, PAUSE, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, ZERO
    }

    protected enum Mouse {
        SELECT, DOACTION
    }

    protected static Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();
    protected static Map<Mouse, Boolean> mouseButtons = new HashMap<Mouse, Boolean>();

    //initialize the hashmap for inputs
    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.UP, false);
        keys.put(Keys.DOWN, false);
        keys.put(Keys.WAIT, false);
        keys.put(Keys.PAUSE, false);
        keys.put(Keys.ONE, false);
        keys.put(Keys.TWO, false);
        keys.put(Keys.THREE, false);
        keys.put(Keys.FOUR, false);
        keys.put(Keys.FIVE, false);
        keys.put(Keys.SIX, false);
        keys.put(Keys.SEVEN, false);
        keys.put(Keys.EIGHT, false);
        keys.put(Keys.NINE, false);
        keys.put(Keys.ZERO, false);
    };

    static {
        mouseButtons.put(Mouse.SELECT, false);
        mouseButtons.put(Mouse.DOACTION, false);
    };

    @Override
    public Command getNextCommand(Entity entity) {
        if (nextCommand != null) {
            Command command = nextCommand;
            nextCommand = null;
            return command;
        }

        return null;
    }

    @Override
    public void receiveMessage(String message) {
        String[] string = message.split(MESSAGE_TOKEN);

        if( string.length == 0 ) return;

        //Specifically for messages with 1 object payload
        if( string.length == 2 ) {
            if (string[0].equalsIgnoreCase(MESSAGE.MOVEMENT_DIRECTION.toString())) {
                //currentDirection = json.fromJson(Direction.class, string[1]);
                //Gdx.app.debug(TAG, "Message received: " + currentDirection.toString());
            }
        }
    }

//    @Override
//    public void dispose(){
//        Gdx.input.setInputProcessor(null);
//    }

    //@Override
    public void update(Entity entity, float delta){

        frameTime = (frameTime + delta)%1000000;

        //Keyboard input
        if(keys.get(Keys.PAUSE)&& frameTime-lastUpdateTime > UPDATE_DELAY) {
            MainGameScreen.setGameState(MainGameScreen.GameState.PAUSED);
            pauseReleased();
            lastUpdateTime = frameTime;
        }else if( keys.get(Keys.LEFT) && frameTime-lastUpdateTime > UPDATE_DELAY){
            nextCommand = new MoveCommand(entity, Direction.LEFT);
            Gdx.app.debug(TAG, "LEFT message sent.");
            lastUpdateTime = frameTime;
        }else if( keys.get(Keys.RIGHT) && frameTime-lastUpdateTime > UPDATE_DELAY){
            nextCommand = new MoveCommand(entity, Direction.RIGHT);
            Gdx.app.debug(TAG, "RIGHT message sent.");
            lastUpdateTime = frameTime;
        }else if( keys.get(Keys.UP) && frameTime-lastUpdateTime > UPDATE_DELAY){
            nextCommand = new MoveCommand(entity, Direction.UP);
            Gdx.app.debug(TAG, "UP message sent.");
            lastUpdateTime = frameTime;
        }else if(keys.get(Keys.DOWN) && frameTime-lastUpdateTime > UPDATE_DELAY){
            nextCommand = new MoveCommand(entity, Direction.DOWN);
            Gdx.app.debug(TAG, "DOWN message sent.");
            lastUpdateTime = frameTime;
        }else if(keys.get(Keys.ONE) && frameTime-lastUpdateTime > UPDATE_DELAY){
            nextCommand = new PunchCommand(entity, entity.getCurrentDirection());
            Gdx.app.debug(TAG, "PUNCH message sent.");
            lastUpdateTime = frameTime;
        }else if(keys.get(Keys.TWO) && frameTime-lastUpdateTime > UPDATE_DELAY){
            nextCommand = new KickCommand(entity, entity.getCurrentDirection());
            Gdx.app.debug(TAG, "KICK message sent.");
            lastUpdateTime = frameTime;
        }else if(keys.get(Keys.WAIT) && frameTime-lastUpdateTime > UPDATE_DELAY) {
            nextCommand = new WaitCommand(entity);
            Gdx.app.debug(TAG, "WAIT message sent.");
            lastUpdateTime = frameTime;
        }

        //Mouse input
        if( mouseButtons.get(Mouse.SELECT)) {
            //Gdx.app.debug(TAG, "Mouse LEFT click at : (" + _lastMouseCoordinates.x + "," + _lastMouseCoordinates.y + ")" );
            //entity.sendMessage(MESSAGE.INIT_SELECT_ENTITY, json.toJson(_lastMouseCoordinates));
            mouseButtons.put(Mouse.SELECT, false);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if( keycode == Input.Keys.LEFT || keycode == Input.Keys.A){
            this.leftPressed();
        }
        if( keycode == Input.Keys.RIGHT || keycode == Input.Keys.D){
            this.rightPressed();
        }
        if( keycode == Input.Keys.UP || keycode == Input.Keys.W){
            this.upPressed();
        }
        if( keycode == Input.Keys.DOWN || keycode == Input.Keys.S){
            this.downPressed();
        }
        if( keycode == Input.Keys.NUM_1){
            this.keyPressed(Keys.ONE);
        }
        if( keycode == Input.Keys.NUM_2){
            this.keyPressed(Keys.TWO);
        }
        if( keycode == Input.Keys.NUM_3){
            this.keyPressed(Keys.THREE);
        }
        if( keycode == Input.Keys.NUM_4){
            this.keyPressed(Keys.FOUR);
        }
        if( keycode == Input.Keys.NUM_5){
            this.keyPressed(Keys.FIVE);
        }
        if( keycode == Input.Keys.NUM_6){
            this.keyPressed(Keys.SIX);
        }
        if( keycode == Input.Keys.NUM_7){
            this.keyPressed(Keys.SEVEN);
        }
        if( keycode == Input.Keys.NUM_8){
            this.keyPressed(Keys.EIGHT);
        }
        if( keycode == Input.Keys.NUM_9){
            this.keyPressed(Keys.NINE);
        }
        if( keycode == Input.Keys.NUM_0){
            this.keyPressed(Keys.ZERO);
        }
        if( keycode == Input.Keys.Q){
            this.keyPressed(Keys.WAIT);
        }
        if( keycode == Input.Keys.P ){
            this.pausePressed();
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if( keycode == Input.Keys.LEFT || keycode == Input.Keys.A){
            this.leftReleased();
        }
        if( keycode == Input.Keys.RIGHT || keycode == Input.Keys.D){
            this.rightReleased();
        }
        if( keycode == Input.Keys.UP || keycode == Input.Keys.W ){
            this.upReleased();
        }
        if( keycode == Input.Keys.DOWN || keycode == Input.Keys.S){
            this.downReleased();
        }
        if( keycode == Input.Keys.NUM_1){
            this.keyReleased(Keys.ONE);
        }
        if( keycode == Input.Keys.NUM_2){
            this.keyReleased(Keys.TWO);
        }
        if( keycode == Input.Keys.NUM_3){
            this.keyReleased(Keys.THREE);
        }
        if( keycode == Input.Keys.NUM_4){
            this.keyReleased(Keys.FOUR);
        }
        if( keycode == Input.Keys.NUM_5){
            this.keyReleased(Keys.FIVE);
        }
        if( keycode == Input.Keys.NUM_6){
            this.keyReleased(Keys.SIX);
        }
        if( keycode == Input.Keys.NUM_7){
            this.keyReleased(Keys.SEVEN);
        }
        if( keycode == Input.Keys.NUM_8){
            this.keyReleased(Keys.EIGHT);
        }
        if( keycode == Input.Keys.NUM_9){
            this.keyReleased(Keys.NINE);
        }
        if( keycode == Input.Keys.NUM_0){
            this.keyReleased(Keys.ZERO);
        }
        if( keycode == Input.Keys.Q){
            this.keyReleased(Keys.WAIT);
        }
        if( keycode == Input.Keys.P ){
            this.pauseReleased();
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //Gdx.app.debug(TAG, "GameScreen: MOUSE DOWN........: (" + screenX + "," + screenY + ")" );

        if( button == Input.Buttons.LEFT || button == Input.Buttons.RIGHT ){
            this.setClickedMouseCoordinates(screenX, screenY);
        }

        //left is selection, right is context menu
        if( button == Input.Buttons.LEFT){
            this.selectMouseButtonPressed(screenX, screenY);
        }
        if( button == Input.Buttons.RIGHT){
            this.doActionMouseButtonPressed(screenX, screenY);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //left is selection, right is context menu
        if( button == Input.Buttons.LEFT){
            this.selectMouseButtonReleased(screenX, screenY);
        }
        if( button == Input.Buttons.RIGHT){
            this.doActionMouseButtonReleased(screenX, screenY);
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    //Key presses
    public void leftPressed(){
        keys.put(Keys.LEFT, true);
    }

    public void rightPressed(){
        keys.put(Keys.RIGHT, true);
    }

    public void upPressed(){
        keys.put(Keys.UP, true);
    }

    public void downPressed(){
        keys.put(Keys.DOWN, true);
    }

    public void pausePressed() {
        keys.put(Keys.PAUSE, true);
    }

    public void keyPressed(Keys key) { keys.put(key, true); }

    public void setClickedMouseCoordinates(int x,int y){
        _lastMouseCoordinates.set(x, y, 0);
    }

    public void selectMouseButtonPressed(int x, int y){
        mouseButtons.put(Mouse.SELECT, true);
    }

    public void doActionMouseButtonPressed(int x, int y){
        mouseButtons.put(Mouse.DOACTION, true);
    }

    //Releases

    public void leftReleased(){
        keys.put(Keys.LEFT, false);
    }

    public void rightReleased(){
        keys.put(Keys.RIGHT, false);
    }

    public void upReleased(){
        keys.put(Keys.UP, false);
    }

    public void downReleased(){
        keys.put(Keys.DOWN, false);
    }

    public void pauseReleased() { keys.put(Keys.PAUSE, false);}

    public void keyReleased(Keys key) { keys.put(key, false); }

    public void selectMouseButtonReleased(int x, int y){
        mouseButtons.put(Mouse.SELECT, false);
    }

    public void doActionMouseButtonReleased(int x, int y){
        mouseButtons.put(Mouse.DOACTION, false);
    }

//    public static void clear(){
//        keys.put(Keys.LEFT, false);
//        keys.put(Keys.RIGHT, false);
//        keys.put(Keys.UP, false);
//        keys.put(Keys.DOWN, false);
//        keys.put(Keys.WAIT, false);
//    }
}
