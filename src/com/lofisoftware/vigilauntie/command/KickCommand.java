package com.lofisoftware.vigilauntie.command;

import com.lofisoftware.vigilauntie.MessageManager;
import com.lofisoftware.vigilauntie.entity.AnimationType;
import com.lofisoftware.vigilauntie.entity.Entity;
import com.lofisoftware.vigilauntie.map.Direction;
import com.lofisoftware.vigilauntie.map.MapManager;

import static com.lofisoftware.vigilauntie.Constants.KICK_DAMAGE;
import static com.lofisoftware.vigilauntie.Constants.KICK_EXPERIENCE;
import static com.lofisoftware.vigilauntie.Constants.KICK_SCORE;
import static com.lofisoftware.vigilauntie.Constants.KICK_SPEED;
import static com.lofisoftware.vigilauntie.Constants.KICK_STUN_AMOUNT;
import static com.lofisoftware.vigilauntie.Constants.TURN_ANIMATION_TIME;


public class KickCommand implements Command {

    Direction direction;
    Entity entity;

    public KickCommand (Entity entity, Direction direction) {
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

                target.stun(KICK_STUN_AMOUNT);
                entity.getStats().addExperience(KICK_EXPERIENCE);
                entity.getStats().addScore(KICK_SCORE);

                if (target.isStunned()) {
                    MessageManager.getMessageManager().sendMessage(entity.getName() + " stunned " + target.getName() + " with a kick.");
                } else {
                    MessageManager.getMessageManager().sendMessage(entity.getName() + " kicked " + target.getName() + ".");
                }

                target.damage(KICK_DAMAGE);
            }

            entity.getStats().addMove();
            entity.playTimedAnimation(AnimationType.KICK, TURN_ANIMATION_TIME);
        }
    }

    @Override
    public int getSpeed() {
        return KICK_SPEED;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }
}