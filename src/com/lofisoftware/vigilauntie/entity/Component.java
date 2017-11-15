package com.lofisoftware.vigilauntie.entity;

public interface Component {

    public static final String MESSAGE_TOKEN = ":::::";

    public static enum MESSAGE{
        CURRENT_POSITION,
        CURRENT_DIRECTION,
        SET_POSITION,
        INIT_START_POSITION,
        MOVEMENT_DIRECTION,
        CURRENT_STATE,
        COLLISION_WITH_MAP,
        COLLISION_WITH_ENTITY,
        CHECK_COLLISION,
        ENTITY_MOVED,
        LOAD_ANIMATIONS,
        INIT_DIRECTION,
        INIT_STATE,
        INIT_SELECT_ENTITY,
        ENTITY_SELECTED,
        ENTITY_DESELECTED
    }

    void receiveMessage(String message);
}