package com.lofisoftware.vigilauntie.command;


import com.lofisoftware.vigilauntie.entity.Entity;

public interface Command {

    void execute();

    int getSpeed();

    Entity getEntity();

}
