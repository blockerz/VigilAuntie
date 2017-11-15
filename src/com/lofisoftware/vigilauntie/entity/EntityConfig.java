package com.lofisoftware.vigilauntie.entity;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.lofisoftware.vigilauntie.map.Direction;

public class EntityConfig {
    private Array<AnimationConfig> animationConfig;
    //private Array<InventoryItem.ItemTypeID> inventory;
    private Entity.State state = Entity.State.IDLE;
    private Direction direction = Direction.RIGHT;
    private String entityID;
    private String conversationConfigPath;
    private String questConfigPath;
    private String currentQuestID;
    private String itemTypeID;
    private int entityWidth = 16;
    private int entityHeight = 32;
    private int maxHitPoints = 100;
    private int speed = 1;
    private String name;

    private ObjectMap<String, String> entityProperties;

    public static enum EntityProperties{
        ENTITY_HEALTH_POINTS,
        ENTITY_ATTACK_POINTS,
        ENTITY_DEFENSE_POINTS,
        ENTITY_HIT_DAMAGE_TOTAL,
        ENTITY_XP_REWARD,
        ENTITY_GP_REWARD,
        NONE
    }

    EntityConfig(){
        animationConfig = new Array<AnimationConfig>();
        //inventory = new Array<InventoryItem.ItemTypeID>();
        entityProperties = new ObjectMap<String, String>();
    }

    EntityConfig(EntityConfig config){
        state = config.getState();
        //direction = config.getDirection();
        entityID = config.getEntityID();
        conversationConfigPath = config.getConversationConfigPath();
        questConfigPath = config.getQuestConfigPath();
        currentQuestID = config.getCurrentQuestID();
        itemTypeID = config.getItemTypeID();
        name = config.getName();

        animationConfig = new Array<AnimationConfig>();
        animationConfig.addAll(config.getAnimationConfig());

        //inventory = new Array<InventoryItem.ItemTypeID>();
        //inventory.addAll(config.getInventory());

        entityProperties = new ObjectMap<String, String>();
        entityProperties.putAll(config.entityProperties);
    }

    public ObjectMap<String, String> getEntityProperties() {
        return entityProperties;
    }

    public void setEntityProperties(ObjectMap<String, String> entityProperties) {
        this.entityProperties = entityProperties;
    }

    public void setPropertyValue(String key, String value){
        entityProperties.put(key, value);
    }

    public String getPropertyValue(String key){
        Object propertyVal = entityProperties.get(key);
        if( propertyVal == null ) return new String();
        return propertyVal.toString();
    }

    public String getCurrentQuestID() {
        return currentQuestID;
    }

    public void setCurrentQuestID(String currentQuestID) {
        this.currentQuestID = currentQuestID;
    }

    public String getItemTypeID() {
        return itemTypeID;
    }

    public void setItemTypeID(String itemTypeID) {
        this.itemTypeID = itemTypeID;
    }

    public String getQuestConfigPath() {
        return questConfigPath;
    }

    public void setQuestConfigPath(String questConfigPath) {
        this.questConfigPath = questConfigPath;
    }

    public String getConversationConfigPath() {
        return conversationConfigPath;
    }

    public void setConversationConfigPath(String conversationConfigPath) {
        this.conversationConfigPath = conversationConfigPath;
    }

    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    public int getEntityHeight() {
        return entityHeight;
    }

    public void setEntityHeight(int entityHeight) {
        this.entityHeight = entityHeight;
    }

    public int getEntityWidth() {
        return entityWidth;
    }

    public void setEntityWidth(int entityWidth) {
        this.entityWidth = entityWidth;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Entity.State getState() {
        return state;
    }

    public void setState(Entity.State state) {
        this.state = state;
    }

    public Array<AnimationConfig> getAnimationConfig() {
        return animationConfig;
    }

    public void addAnimationConfig(AnimationConfig animationConfig) {
        this.animationConfig.add(animationConfig);
    }

    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    public void setMaxHitPoints(int maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //public Array<InventoryItem.ItemTypeID> getInventory() {
    //    return inventory;
    //}

    //public void setInventory(Array<InventoryItem.ItemTypeID> inventory) {
    //    this.inventory = inventory;
    //}

    static public class AnimationConfig{
        private float frameDuration = 1.0f;
        private int frameWidth = 32;
        private int frameHeight = 32;
        private AnimationType animationType;
        private Animation.PlayMode playmode;
        private Array<String> texturePaths;
        private Array<GridPoint2> gridPoints;

        public AnimationConfig(){
            animationType = AnimationType.IDLE;
            texturePaths = new Array<String>();
            gridPoints = new Array<GridPoint2>();
            playmode = Animation.PlayMode.LOOP;
        }


        public float getFrameDuration() {
            return frameDuration;
        }

        public void setFrameDuration(float frameDuration) {
            this.frameDuration = frameDuration;
        }

        public int getFrameHeight() {
            return frameHeight;
        }

        public void setFrameHeight(int frameHeight) {
            this.frameHeight = frameHeight;
        }

        public int getFrameWidth() {
            return frameWidth;
        }

        public void setFrameWidth(int frameWidth) {
            this.frameWidth = frameWidth;
        }

        public Animation.PlayMode getPlaymode() {
            return playmode;
        }

        public void setPlaymode(Animation.PlayMode playmode) {
            this.playmode = playmode;
        }

        public Array<String> getTexturePaths() {
            return texturePaths;
        }

        public void setTexturePaths(Array<String> texturePaths) {
            this.texturePaths = texturePaths;
        }

        public Array<GridPoint2> getGridPoints() {
            return gridPoints;
        }

        public void setGridPoints(Array<GridPoint2> gridPoints) {
            this.gridPoints = gridPoints;
        }

        public AnimationType getAnimationType() {
            return animationType;
        }

        public void setAnimationType(AnimationType animationType) {
            this.animationType = animationType;
        }
    }

}

