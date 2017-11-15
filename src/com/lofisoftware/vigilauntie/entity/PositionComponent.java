package com.lofisoftware.vigilauntie.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Json;
import com.lofisoftware.vigilauntie.map.MapManager;

import com.lofisoftware.vigilauntie.map.Direction;
import squidpony.squidmath.Coord;

public class PositionComponent implements Component{

    private static final String TAG = PositionComponent.class.getSimpleName();


//    public static enum Direction {
//        UP,
//        RIGHT,
//        DOWN,
//        LEFT;
//
//        static public Direction getRandomNext() {
//            return Direction.values()[MathUtils.random(Direction.values().length - 1)];
//        }
//
//        public Direction getOpposite() {
//            if( this == LEFT){
//                return RIGHT;
//            }else if( this == RIGHT){
//                return LEFT;
//            }else if( this == UP){
//                return DOWN;
//            }else{
//                return UP;
//            }
//        }
//
//        public Coord getNewPosition(Coord pos) {
//            if( this == LEFT){
//                return Coord.get(pos.getX() -1, pos.getY());
//            }else if( this == RIGHT){
//                return Coord.get(pos.getX() +1, pos.getY());
//            }else if( this == UP){
//                return Coord.get(pos.getX(), pos.getY() -1);
//            }else if( this == DOWN){
//                return Coord.get(pos.getX(), pos.getY() +1);
//            }else {
//                return Coord.get(pos.getX(), pos.getY());
//            }
//        }
//    }

    private Coord position;
    private Coord newPosition;
    private Json json;

    public PositionComponent() {
        json = new Json();
        position = Coord.get(-1,-1);
    }

    public Coord getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(Coord newPosition) {
        this.newPosition = newPosition;
    }

    public Coord getPosition() {
        return position;
    }

    public void setPosition(Coord position) {
        this.position = position;
    }

    @Override
    public void receiveMessage(String message) {
        String[] string = message.split(MESSAGE_TOKEN);

        if( string.length == 0 ) return;

        //Specifically for messages with 1 object payload
        if( string.length == 2 ) {
            if (string[0].equalsIgnoreCase(MESSAGE.MOVEMENT_DIRECTION.toString())) {
                Direction currentDirection = json.fromJson(Direction.class, string[1]);
                //newPosition = currentDirection.getNewPosition(position);
                position = newPosition;
                MapManager.getMapManager().updateMap();
                Gdx.app.debug(TAG, string[0] + " Message received: " + position.toString());
            }

            if (string[0].equalsIgnoreCase(MESSAGE.SET_POSITION.toString())) {
                Coord pos = json.fromJson(Coord.class, string[1]);
                newPosition = pos;
                position = pos;
                MapManager.getMapManager().updateMap();
                Gdx.app.debug(TAG, string[0] + " Message received: " + position.toString());
            }

            if (string[0].equalsIgnoreCase(MESSAGE.ENTITY_MOVED.toString())) {
                Coord pos = json.fromJson(Coord.class, string[1]);
                position = pos;
                MapManager.getMapManager().updateMap();
                Gdx.app.debug(TAG, string[0] + " Message received: " + position.toString());
            }
        }
    }

//    @Override
//    public void update(Entity entity, float delta){
//
//        if( position != newPosition) {
//            entity.sendMessage(MESSAGE.CHECK_COLLISION, json.toJson(newPosition));
//        }
//    }
//
//    @Override
//    public void dispose() {
//
//    }
}
