package com.lofisoftware.vigilauntie.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.lofisoftware.vigilauntie.MessageManager;
import com.lofisoftware.vigilauntie.Utility;
import com.lofisoftware.vigilauntie.command.Command;
import com.lofisoftware.vigilauntie.map.Direction;
import com.lofisoftware.vigilauntie.map.MapManager;
import com.lofisoftware.vigilauntie.profile.ProfileManager;

import java.util.Hashtable;

import squidpony.squidmath.Coord;

import static com.lofisoftware.vigilauntie.Constants.CELL_HEIGHT;
import static com.lofisoftware.vigilauntie.Constants.CELL_WIDTH;
import static com.lofisoftware.vigilauntie.Constants.TURN_ANIMATION_TIME;



public class Entity implements Component {

    private static final String TAG = Entity.class.getSimpleName();

    private Coord currentPosition;
    private Coord newPosition;
    protected AnimatedImage image;
    protected float frameTime = 0f;
    protected CharacterStats stats;

    private State state;
    private Direction currentDirection;
    private Json json;
    private EntityConfig entityConfig;
    private InputComponent inputComponent;

    public static enum State {
        IDLE,
        WALKING,
        START_TURN,
        IMMOBILE;//This should always be last

        static public State getRandomNext() {
            //Ignore IMMOBILE which should be last state
            return State.values()[MathUtils.random(State.values().length - 2)];
        }
    }

    public Entity(Entity entity){

        set(entity);
    }

    private Entity set(Entity entity) {
        inputComponent = entity.inputComponent;
        state = entity.state;

        json = entity.json;
        currentPosition = entity.currentPosition;
        newPosition = entity.newPosition;
        currentDirection = entity.currentDirection;

        entityConfig = new EntityConfig(entity.entityConfig);
        image = entity.image;
        stats = entity.stats;

        return this;
    }

    public Entity(String configFilePath, InputComponent inputComponent){

        json = new Json();
        currentPosition = Coord.get(0,0);
        newPosition = Coord.get(0,0);
        setState(State.IDLE);

        entityConfig = getEntityConfig(configFilePath);
        setState(entityConfig.getState());
        currentDirection = entityConfig.getDirection();

        initAnimations();
        updateAnimations(0.1f);

        this.inputComponent = inputComponent;

        stats = new CharacterStats();
        stats.setMaxHitPoints(entityConfig.getMaxHitPoints());
        stats.setHitPoints(entityConfig.getMaxHitPoints());
        stats.setSpeed(entityConfig.getSpeed());
        stats.setName(entityConfig.getName());

        Gdx.app.log(TAG,"Entity pos: " + currentPosition.toString());
    }

    public CharacterStats getStats() {
        return stats;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    public Coord getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(Coord newPosition) {
        this.newPosition = newPosition;
    }

    public boolean isDead() {
        return stats.getHitPoints() <= 0;
    }

    public void damage(int amount) {

        stats.modifyHitPoints(-amount);



        if (isDead()) {
            //playTimedAnimation(AnimationType.KNOCKDOWN, 5.0f);
            playDeathAnimation();
        }
        else {
            playTimedAnimation(AnimationType.HIT_FACE, TURN_ANIMATION_TIME);
        }

    }

    public boolean isStunned() {
        return (stats.getResiliance() <= 0);
    }

    public void stun(int amount) {

        if (!isDead()) {
            stats.modifyResiliance(-amount);

            if (isStunned()) {
                //playTimedAnimation(AnimationType.HIT_FACE, TURN_ANIMATION_TIME);
            }
        }
    }

    public void resetResiliance() {
        stats.setResiliance(stats.getMaxResiliance());
    }

    public void playTimedAnimation(AnimationType type, float time) {

        image.setAnimation(type);
        image.addAction(Actions.sequence(Actions.delay(time), Actions.run(new Runnable() {
            @Override
            public void run() {
                image.setAnimation(AnimationType.IDLE);
            }
        })));

    }

    public void playDeathAnimation() {

        image.setAnimation(AnimationType.KNOCKOUT);
        MessageManager.getMessageManager().sendMessage(getName() + " was knocked out! ");
        image.addAction(Actions.sequence(Actions.delay(1.0f), Actions.run(new Runnable() {
            @Override
            public void run() {
                //image.setAnimation(animations.get(AnimationType.IDLE));
                removeFromMap();
            }
        })));

    }

    boolean isEntityAdjacent(Entity target) {

        if (currentPosition.y == target.currentPosition.y) {
            if (Math.abs(currentPosition.x - target.currentPosition.x) < 1.5f )
                return true;
        }

        return false;
    }

    void removeFromMap() {
        MapManager.getMapManager().getCurrentMap().removeEntity(this);
    }

    public boolean move(Direction direction) {

        Coord newPostion = currentPosition;

        switch(direction) {

            case UP:
                newPostion = newPostion.translate(0,1);
                break;
            case RIGHT:
                newPostion = newPostion.translate(1,0);
                break;
            case DOWN:
                newPostion = newPostion.translate(0,-1);
                break;
            case LEFT:
                newPostion = newPostion.translate(-1,0);
                break;
        }

        swapDirection(direction);

        if (MapManager.getMapManager().getCurrentMap().isCollision(newPostion)) {
            return false;
        }


        setCurrentPosition(newPostion);

        return true;
    }

    public void swapDirection (Direction direction) {

        if (currentDirection == Direction.RIGHT && direction == Direction.LEFT) {
            currentDirection = Direction.LEFT;
            image.setFlip(true);
        }
        else if (currentDirection == Direction.LEFT && direction == Direction.RIGHT) {
            currentDirection = Direction.RIGHT;
            image.setFlip(false);
        }
    }

    public Coord getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Coord currentPosition) {

//        image.addAction(Actions.run(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }));
        this.currentPosition = currentPosition;
        //image.setZIndex(GROUND_TILES_HEIGHT*2 - getCurrentPosition().getY());
        image.setAnimation(AnimationType.WALK);
        image.addAction(Actions.sequence(Actions.moveTo(getCurrentScreenX(), getCurrentScreenY(), TURN_ANIMATION_TIME), Actions.run(new Runnable() {
            @Override
            public void run() {
                image.setAnimation(AnimationType.IDLE);
            }
        })));
        //Gdx.app.debug(TAG, "z-order: " + image.getZIndex());
    }

    public AnimatedImage getImage() {
        return image;
    }

    public void setImage(AnimatedImage image) {
        this.image = image;
    }

    public EntityConfig getEntityConfig() {
        return entityConfig;
    }

    public void sendMessage(Component.MESSAGE messageType, String ... args){
        String fullMessage = messageType.toString();

        for (String string : args) {
            fullMessage += Component.MESSAGE_TOKEN + string;
        }

        receiveMessage(fullMessage);

    }

    public void registerObserver(ComponentObserver observer){
        inputComponent.addObserver(observer);

    }

    public void unregisterObservers(){
        inputComponent.removeAllObservers();

    }

    public void update(float delta){
        inputComponent.update(this, delta);


        if (isStunned())
            image.setColor(Color.RED);
        else
            image.setColor(Color.WHITE);

        //image.setZIndex(GROUND_TILES_HEIGHT*2 - getCurrentPosition().getY());
        //updateAnimations(delta);

    }

    public Command getNextCommand() {
        return inputComponent.getNextCommand(this);
    }

    public InputProcessor getInputProcessor(){
        if (inputComponent instanceof PlayerInputComponent)
            return (PlayerInputComponent)inputComponent;
        return null;
    }

    public void dispose(){

    }

    public void setEntityConfig(EntityConfig entityConfig){
        this.entityConfig = entityConfig;
    }

    static public EntityConfig getEntityConfig(String configFilePath){
        Json json = new Json();
        return json.fromJson(EntityConfig.class, Gdx.files.internal(configFilePath));
    }

    public static EntityConfig loadEntityConfigByPath(String entityConfigPath){
        EntityConfig entityConfig = Entity.getEntityConfig(entityConfigPath);
        EntityConfig serializedConfig = ProfileManager.getInstance().getProperty(entityConfig.getEntityID(), EntityConfig.class);

        if( serializedConfig == null ){
            return entityConfig;
        }else{
            return serializedConfig;
        }
    }

//    public static Entity initEntity(EntityConfig entityConfig){
//        Json json = new Json();
//        Entity entity = EntityFactory.getEntity(EntityFactory.EntityType.NPC);
//        entity.setEntityConfig(entityConfig);
//
//        //entity.sendMessage(Component.MESSAGE.LOAD_ANIMATIONS, json.toJson(entity.getEntityConfig()));
//        entity.sendMessage(Component.MESSAGE.INIT_START_POSITION, json.toJson(new Vector2(0,0)));
//        entity.sendMessage(Component.MESSAGE.INIT_STATE, json.toJson(entity.getEntityConfig().getState()));
//        entity.sendMessage(Component.MESSAGE.INIT_DIRECTION, json.toJson(entity.getEntityConfig().getDirection()));
//
//        return entity;
//    }

    @Override
    public void receiveMessage(String message) {
        //Gdx.app.debug(TAG, "Got message " + message);
        String[] string = message.split(MESSAGE_TOKEN);

        if( string.length == 0 ) return;

        //Specifically for messages with 1 object payload
        if( string.length == 2 ) {
            if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_POSITION.toString())) {
                currentPosition = json.fromJson(Coord.class, string[1]);
            }  else if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_STATE.toString())) {
                state = json.fromJson(Entity.State.class, string[1]);
            } else if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_DIRECTION.toString())) {
                currentDirection = json.fromJson(Direction.class, string[1]);
            }
        }
    }

    public float getCurrentScreenX() {
        return currentPosition.getX() * CELL_WIDTH/2 + (CELL_WIDTH / 3) + (currentPosition.getY() * (CELL_WIDTH/4));
    }

    public float getCurrentScreenY() {
        return currentPosition.getY() * CELL_HEIGHT/2 + (CELL_HEIGHT / 6);
    }

    public void updateAnimations(float delta){
        frameTime = (frameTime + delta)%5; //Want to avoid overflow

        image.setPosition(getCurrentScreenX(), getCurrentScreenY());
        //image.invalidate();
        //image.setScale(1.25f);
        //image.setDrawable(new TextureRegionDrawable(animation.getKeyFrames()[0]));

        //switch (Entity.AnimationType)
//        switch (_currentDirection) {
//            case DOWN:
//                if (_currentState == Entity.State.MOVING) {
//                    Animation animation = _animations.get(Entity.AnimationType.WALK);
//                    if( animation == null ) return;
//                    _currentFrame = animation.getKeyFrame(_frameTime);
//                } else if(_currentState == Entity.State.IDLE) {
//                    Animation animation = _animations.get(Entity.AnimationType.WALK);
//                    if( animation == null ) return;
//                    _currentFrame = animation.getKeyFrames()[0];
//                } //else if(_currentState == Entity.State.IMMOBILE) {
//                //Animation animation = _animations.get(Entity.AnimationType.WALK);
//                //if( animation == null ) return;
//                // _currentFrame = animation.getKeyFrame(_frameTime);
//                //}
//                break;
//            case LEFT:
//                if (_currentState == Entity.State.MOVING) {
//                    Animation animation = _animations.get(Entity.AnimationType.WALK);
//                    if( animation == null ) return;
//                    _currentFrame = animation.getKeyFrame(_frameTime);
//                } else if(_currentState == Entity.State.IDLE) {
//                    Animation animation = _animations.get(Entity.AnimationType.WALK);
//                    if( animation == null ) return;
//                    _currentFrame = animation.getKeyFrames()[0];
//                }
//                if (!_currentFrame.isFlipX())
//                    _currentFrame.flip(true,false);
//                break;
//            case UP:
//                if (_currentState == Entity.State.MOVING) {
//                    Animation animation = _animations.get(Entity.AnimationType.WALK);
//                    if( animation == null ) return;
//                    _currentFrame = animation.getKeyFrame(_frameTime);
//                } else if(_currentState == Entity.State.IDLE) {
//                    Animation animation = _animations.get(Entity.AnimationType.WALK);
//                    if( animation == null ) return;
//                    _currentFrame = animation.getKeyFrames()[0];
//                }
//                break;
//            case RIGHT:
//                if (_currentState == Entity.State.MOVING) {
//                    Animation animation = _animations.get(Entity.AnimationType.WALK);
//                    if( animation == null ) return;
//                    _currentFrame = animation.getKeyFrame(_frameTime);
//                } else if(_currentState == Entity.State.IDLE) {
//                    Animation animation = _animations.get(Entity.AnimationType.WALK);
//                    if( animation == null ) return;
//                    _currentFrame = animation.getKeyFrames()[0];
//                }
//                if (_currentFrame.isFlipX())
//                    _currentFrame.flip(true,false);
//                break;
//
//            default:
//                break;
//        }


    }

    protected void initAnimations() {

        Array<EntityConfig.AnimationConfig> animationConfigs = entityConfig.getAnimationConfig();

        for( EntityConfig.AnimationConfig animationConfig : animationConfigs ){
            Array<String> textureNames = animationConfig.getTexturePaths();
            Array<GridPoint2> points = animationConfig.getGridPoints();
            AnimationType animationType = animationConfig.getAnimationType();
            float frameDuration = animationConfig.getFrameDuration();
            int frameWidth = animationConfig.getFrameWidth();
            int frameHeight = animationConfig.getFrameHeight();
            Animation.PlayMode mode = animationConfig.getPlaymode();

                Animation animation = null;

            if( textureNames.size == 1) {
                animation = loadAnimation(textureNames.get(0), points, frameDuration, frameWidth, frameHeight, mode);
            }else if( textureNames.size == 2){
                animation = loadAnimation(textureNames.get(0), textureNames.get(1), points, frameDuration, frameWidth, frameHeight, mode);
            }

            //Animation animation = animations.get(AnimationType.IDLE);
            if( animation == null ) return;
            if (image == null) {
                image = new AnimatedImage(AnimationType.IDLE, animation);
                image.setAlign(Align.center);
            }
            else {
                image.addAnimation(animationType, animation);
            }
        }
    }

    //Specific to two frame animations where each frame is stored in a separate texture
    protected Animation loadAnimation(String firstTexture, String secondTexture, Array<GridPoint2> points, float frameDuration, int frameWidth, int frameHeight, Animation.PlayMode mode){
        Utility.loadTextureAsset(firstTexture);
        Texture texture1 = Utility.getTextureAsset(firstTexture);

        Utility.loadTextureAsset(secondTexture);
        Texture texture2 = Utility.getTextureAsset(secondTexture);

        TextureRegion[][] texture1Frames = TextureRegion.split(texture1, frameWidth, frameHeight);
        TextureRegion[][] texture2Frames = TextureRegion.split(texture2, frameWidth, frameHeight);

        Array<TextureRegion> animationKeyFrames = new Array<TextureRegion>(2);

        GridPoint2 point = points.first();

        animationKeyFrames.add(texture1Frames[point.x][point.y]);
        animationKeyFrames.add(texture2Frames[point.x][point.y]);

        return new Animation(frameDuration, animationKeyFrames, mode);
    }

    protected Animation loadAnimation(String textureName, Array<GridPoint2> points, float frameDuration, int frameWidth, int frameHeight, Animation.PlayMode mode){
        Utility.loadTextureAsset(textureName);
        Texture texture = Utility.getTextureAsset(textureName);

        TextureRegion[][] textureFrames = TextureRegion.split(texture, frameWidth, frameHeight);

        Array<TextureRegion> animationKeyFrames = new Array<TextureRegion>(points.size);

        for( GridPoint2 point : points){
            animationKeyFrames.add(textureFrames[point.x][point.y]);
        }

        return new Animation(frameDuration, animationKeyFrames, mode);
    }

    public String getName () {
        return stats.getName();
    }
}