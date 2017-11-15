package com.lofisoftware.vigilauntie.command;


import com.lofisoftware.vigilauntie.entity.Entity;
import com.lofisoftware.vigilauntie.map.Direction;
import static com.lofisoftware.vigilauntie.Constants.MOVE_SPEED;
public class MoveCommand implements Command {

    Direction direction;
    Entity entity;

    public MoveCommand (Entity entity, Direction direction) {
        this.entity = entity;
        this.direction = direction;
    }

    @Override
    public void execute() {

        if (entity != null && direction != null && !entity.isDead()) {
            entity.move(direction);
            entity.getStats().addMove();
        }
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public int getSpeed() {
        return MOVE_SPEED;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }
}
