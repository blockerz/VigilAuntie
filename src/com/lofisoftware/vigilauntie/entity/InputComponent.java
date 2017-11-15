package com.lofisoftware.vigilauntie.entity;

import com.badlogic.gdx.utils.Json;
import com.lofisoftware.vigilauntie.command.Command;

public abstract class InputComponent extends ComponentSubject implements Component {

    //protected PositionComponent.Direction currentDirection = null;
    //protected Entity.State currentState = null;
    protected Json json;



    InputComponent(){
        json = new Json();
    }

    public abstract void update(Entity entity, float delta);

    public abstract Command getNextCommand(Entity entity);

}

