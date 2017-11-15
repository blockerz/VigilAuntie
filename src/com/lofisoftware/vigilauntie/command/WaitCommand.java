package com.lofisoftware.vigilauntie.command;

import com.lofisoftware.vigilauntie.entity.Entity;

public class WaitCommand implements Command {

    private Entity entity;

    public WaitCommand (Entity entity) {
        this.entity = entity;
    }

    @Override
    public void execute() {
        entity.getStats().addMove();
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }
}
