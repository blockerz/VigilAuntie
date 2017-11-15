package com.lofisoftware.vigilauntie.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.Scaling;

import java.util.Hashtable;


public class AnimatedImage extends Widget {

    private Scaling scaling;
    private int align = Align.center;
    private float imageX, imageY, imageWidth, imageHeight;

    private Hashtable<AnimationType, Animation> animations;
    private Animation animation;
    private Queue<AnimationType> animationQueue;
    private AnimationType defaultAnimationType;
    private TextureRegion region;
    private boolean flip;

    public int state;
    public float stateTime;


    public AnimatedImage(AnimationType type, Animation animation) {
        this(type, animation, Scaling.stretch, Align.center);
    }

    public AnimatedImage(AnimationType type, Animation animation, Scaling scaling, int align) {
        animations = new Hashtable<AnimationType, Animation>();
        animationQueue = new Queue<AnimationType>();
        defaultAnimationType = type;
        setAnimation(defaultAnimationType);
        animationQueue.addFirst(defaultAnimationType);
        animations.put(defaultAnimationType,animation);
        this.animation = animation;
        this.scaling = scaling;
        this.align = align;
        setWidth(getPrefWidth());
        setHeight(getPrefHeight());
        flip = false;
    }

    private void updateAnimations() {

        if (animationQueue.size > 1) {
            if (animation.isAnimationFinished(stateTime)) {
                changeAnimation(animationQueue.removeLast());
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        updateAnimations();

        validate();

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        float x = getX();
        float y = getY();
        float scaleX = getScaleX();
        float scaleY = getScaleY();

        if (animation != null) {
            region = animation.getKeyFrame(stateTime);

//            if (animation.getPlayMode() == Animation.PlayMode.NORMAL) {
//                Gdx.app.debug("AnimatedImage","Frame: " + animation.getKeyFrameIndex(stateTime));
//            }

            if (region.isFlipX()) {
                if (flip)
                    region.flip(false, false);
                else
                    region.flip(true, false);
            }
            else{
                if (flip)
                    region.flip(true, false);
                else
                    region.flip(false, false);
            }
            float rotation = getRotation();
            if (scaleX == 1 && scaleY == 1 && rotation == 0)
                batch.draw(region, x + imageX, y + imageY, imageWidth, imageHeight);
            else {
                batch.draw(region, x + imageX, y + imageY, getOriginX() - imageX, getOriginY() - imageY, imageWidth, imageHeight,
                        scaleX, scaleY, rotation);
            }
        }
    }

    @Override
    public void layout() {
        float regionWidth, regionHeight;
        if (animation != null) {
            regionWidth = animation.getKeyFrame(0).getRegionWidth();
            regionHeight = animation.getKeyFrame(0).getRegionHeight();
        } else
            return;

        float width = getWidth();
        float height = getHeight();

        Vector2 size = scaling.apply(regionWidth, regionHeight, width, height);
        imageWidth = size.x;
        imageHeight = size.y;

        if ((align & Align.left) != 0)
            imageX = 0;
        else if ((align & Align.right) != 0)
            imageX = width-imageWidth;
        else
            imageX = (width/2)-(imageWidth/2);

        if ((align & Align.top) != 0)
            imageY = height-imageHeight;
        else if ((align & Align.bottom) != 0)
            imageY = 0;
        else
            imageY = (height/2)-(imageHeight/2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        if (stateTime > 1000000f)
            stateTime = 0f;
    }

    public void setState(int state) {
        this.state = state;
        stateTime = 0.0f;
    }

    public void setPlayMode (Animation.PlayMode playMode) {
        animation.setPlayMode(playMode);
    }

    public Animation getAnimation() {
        return animation;
    }

    public void addAnimation(AnimationType type, Animation animation) {
        if (animation != null)
            animations.put(type, animation);
    }
    public void setAnimation(AnimationType type) {
        animationQueue.addLast(type);
    }

    public void changeAnimation(AnimationType type) {
        Animation nextAnimation = animations.get(type);

        if (nextAnimation != null) {
            if (this.animation == nextAnimation) return;
            invalidateHierarchy();
        } else {
            if (getPrefWidth() != 0 || getPrefHeight() != 0) invalidateHierarchy();
        }

        this.animation = nextAnimation;
    }

    public void setScaling (Scaling scaling) {
        if (scaling == null) throw new IllegalArgumentException("scaling cannot be null.");
        this.scaling = scaling;
    }

    public void setAlign (int align) {
        this.align = align;
    }

    public float getMinWidth () {
        return 0;
    }

    public float getMinHeight () {
        return 0;
    }

    public float getPrefWidth () {
        if (animation != null) return animation.getKeyFrame(0).getRegionWidth();
        return 0;
    }

    public float getPrefHeight () {
        if (animation != null) return animation.getKeyFrame(0).getRegionHeight();
        return 0;
    }

    public float getImageX () {
        return imageX;
    }

    public float getImageY () {
        return imageY;
    }

    public float getImageWidth () {
        return imageWidth;
    }

    public float getImageHeight () {
        return imageHeight;
    }

    public boolean isFlip() {
        return flip;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }
}