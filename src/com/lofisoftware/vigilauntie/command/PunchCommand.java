package com.lofisoftware.vigilauntie.command;

import com.lofisoftware.vigilauntie.MessageManager;
import com.lofisoftware.vigilauntie.entity.AnimationType;
import com.lofisoftware.vigilauntie.entity.Entity;
import com.lofisoftware.vigilauntie.map.Direction;
import com.lofisoftware.vigilauntie.map.MapManager;

import static com.lofisoftware.vigilauntie.Constants.PUNCH_DAMAGE;
import static com.lofisoftware.vigilauntie.Constants.PUNCH_STUN_AMOUNT;
import static com.lofisoftware.vigilauntie.Constants.PUNCH_EXPERIENCE;
import static com.lofisoftware.vigilauntie.Constants.PUNCH_SCORE;
import static com.lofisoftware.vigilauntie.Constants.PUNCH_SPEED;
import static com.lofisoftware.vigilauntie.Constants.TURN_ANIMATION_TIME;

public class PunchCommand implements Command {

    Direction direction;
    Entity entity;

    public PunchCommand (Entity entity, Direction direction) {
        this.entity = entity;
        this.direction = direction;
    }

    @Override
    public void execute() {

        if (entity != null && direction != null && !entity.isDead()) {
            if (direction != Direction.RIGHT && direction != Direction.LEFT)
                direction = Direction.RIGHT;

            Entity target = MapManager.getMapManager().getCurrentMap().getEntity(
                    entity.getCurrentPosition().getX() + direction.deltaX,
                    entity.getCurrentPosition().getY() + direction.deltaY);

            // if no target in that direction check opposite direction
            if (target == null) {
                direction = direction.opposite();
                target = MapManager.getMapManager().getCurrentMap().getEntity(
                        entity.getCurrentPosition().getX() + direction.deltaX,
                        entity.getCurrentPosition().getY() + direction.deltaY);
            }

            if (target != null) {
                if (entity.getCurrentDirection() != direction) {
                    entity.swapDirection(direction);
                }

                target.stun(PUNCH_STUN_AMOUNT);
                entity.getStats().addExperience(PUNCH_EXPERIENCE);
                entity.getStats().addScore(PUNCH_SCORE);

                if (target.isStunned()) {
                    MessageManager.getMessageManager().sendMessage(entity.getName() + " stunned " + target.getName() + " with a punch.");
                } else {
                    MessageManager.getMessageManager().sendMessage(entity.getName() + " punched " + target.getName() + ".");
                }

                target.damage(PUNCH_DAMAGE);
            }

            entity.getStats().addMove();
            entity.playTimedAnimation(AnimationType.PUNCH, TURN_ANIMATION_TIME);
        }
    }

    @Override
    public int getSpeed() {
        return PUNCH_SPEED;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }
}
