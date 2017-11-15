package com.lofisoftware.vigilauntie.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.lofisoftware.vigilauntie.command.Command;
import com.lofisoftware.vigilauntie.command.MoveCommand;
import com.lofisoftware.vigilauntie.command.PunchCommand;
import com.lofisoftware.vigilauntie.command.WaitCommand;
import com.lofisoftware.vigilauntie.map.MapManager;

import com.lofisoftware.vigilauntie.map.Direction;
import squidpony.squidmath.Coord;

public class NPCInputComponent extends InputComponent {
    private static final String TAG = NPCInputComponent.class.getSimpleName();

    Command nextCommand;

    NPCInputComponent(){

    }

    @Override
    public Command getNextCommand(Entity entity) {

        if (nextCommand == null) {

            Entity player = MapManager.getMapManager().getPlayer();
            double playerDistance = player.getCurrentPosition().distance(entity.getCurrentPosition());
            boolean playerAdjacent = (MapManager.getMapManager().getCurrentMap().getDistanceToPath(entity.getCurrentPosition().getX(), entity.getCurrentPosition().getY()) == 0);
            //Gdx.app.debug(TAG, "Distance to Player: " + MapManager.getMapManager().getCurrentMap().getDistanceToPath(entity.getCurrentPosition().getX(), entity.getCurrentPosition().getY()));

            if (playerAdjacent) {
                Direction direction = Direction.toGoTo(entity.getCurrentPosition(),player.getCurrentPosition());
                nextCommand = new PunchCommand(entity,direction);
                //Gdx.app.debug(TAG,"I am going to punch you!");
            }
            else if (playerDistance < 10) {
                nextCommand = getMoveToEntityCommand2(entity, player);
                //if (nextCommand != null)
                    //Gdx.app.debug(TAG,"I am going to move: " + ((MoveCommand)nextCommand).getDirection());
            }

            // if we can't decide then wait
            if (nextCommand == null) {
                nextCommand = new WaitCommand(entity);
                //Gdx.app.debug(TAG,"I am going to wait here.");
            }

        }

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
        if( string.length == 1 ) {
            if (string[0].equalsIgnoreCase(MESSAGE.COLLISION_WITH_MAP.toString())) {
                //currentDirection = PositionComponent.Direction.getRandomNext();
            }else if (string[0].equalsIgnoreCase(MESSAGE.COLLISION_WITH_ENTITY.toString())) {
                //currentState = Entity.State.IDLE;
                //currentDirection = currentDirection.getOpposite();
            }
        }

        if( string.length == 2 ) {
            if (string[0].equalsIgnoreCase(MESSAGE.INIT_STATE.toString())) {
                //currentState = json.fromJson(Entity.State.class, string[1]);
            }else if (string[0].equalsIgnoreCase(MESSAGE.INIT_DIRECTION.toString())) {
                //currentDirection = json.fromJson(PositionComponent.Direction.class, string[1]);
            }
        }

    }

//    @Override
//    public void dispose(){
//
//    }

    @Override
    public void update(Entity entity, float delta){



//        //If IMMOBILE, don't update anything
//        if( currentState == Entity.State.IMMOBILE ) {
//            entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.IMMOBILE));
//            return;
//        }
//
//
//        //Change direction after so many seconds
//        //if( frameTime > MathUtils.random(1, 5) ){
//        //    currentState = Entity.State.getRandomNext();
//        //    currentDirection = PositionComponent.Direction.getRandomNext();
//        //    frameTime = 0.0f;
//        //}
//
//        if( currentState == Entity.State.IDLE ){
//            //entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.IDLE));
//            return;
//        }
//
//        switch(currentDirection) {
//            case LEFT:
//                entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.WALKING));
//                entity.sendMessage(MESSAGE.MOVEMENT_DIRECTION, json.toJson(PositionComponent.Direction.LEFT));
//                break;
//            case RIGHT:
//                entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.WALKING));
//                entity.sendMessage(MESSAGE.MOVEMENT_DIRECTION, json.toJson(PositionComponent.Direction.RIGHT));
//                break;
//            case UP:
//                entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.WALKING));
//                entity.sendMessage(MESSAGE.MOVEMENT_DIRECTION, json.toJson(PositionComponent.Direction.UP));
//                break;
//            case DOWN:
//                entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.WALKING));
//                entity.sendMessage(MESSAGE.MOVEMENT_DIRECTION, json.toJson(PositionComponent.Direction.DOWN));
//                break;
//        }
    }

    private Command getMoveToEntityCommand2(Entity self, Entity target) {
        Command command = null;
        Coord selfPosition = self.getCurrentPosition();

        Array<Coord> points = MapManager.getMapManager().getCurrentMap().getNextPathPoints(selfPosition.getX(),selfPosition.getY());

        if (points != null && points.size > 0) {
            Coord point = points.pop();

                return new MoveCommand(self, Direction.toGoTo(selfPosition,point));
        }

        return null;
    }
    private Command getMoveToEntityCommand(Entity self, Entity target) {

        Command command = null;
        Coord selfPosition = self.getCurrentPosition();

        Coord targetPositionLeft = target.getCurrentPosition();
        Coord targetPositionRight = targetPositionLeft;

        targetPositionLeft = targetPositionLeft.translate(-1,0);
        targetPositionRight = targetPositionRight.translate(1,0);

        // Optimally - head toward the closest open spot to the right or left of the target
        if (targetPositionLeft.distance(selfPosition) < targetPositionRight.distance(selfPosition)) {

            Direction direction = getDirection(selfPosition, targetPositionLeft);

            if (direction != null) {
                return new MoveCommand(self, direction);
            }

        }
        else {

            Direction direction = getDirection(selfPosition, targetPositionRight);

            if (direction != null) {
                return new MoveCommand(self, direction);
            }

        }

        // Now try the left even if its further
        if (targetPositionLeft.distance(selfPosition) >= targetPositionRight.distance(selfPosition)) {
            Direction direction = getDirection(selfPosition, targetPositionLeft);

            if (direction != null) {
                return new MoveCommand(self, direction);
            }
        }

        return null;

    }

    private Direction getDirection(Coord source, Coord target) {

        int deltaX =0, deltaY = 0;

        deltaX = target.x - source.x;
        deltaY = target.y - source.y;

        Direction direction = Direction.getCardinalDirection(deltaX,deltaY);

        if (!MapManager.getMapManager().getCurrentMap().isCollision(Coord.get(source.x+direction.deltaX,source.y+direction.deltaY))) {
            return direction;
        }

        return null;
    }
}


