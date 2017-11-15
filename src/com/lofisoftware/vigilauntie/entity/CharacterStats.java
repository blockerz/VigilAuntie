package com.lofisoftware.vigilauntie.entity;



public class CharacterStats {

    private int hitPoints;
    private int maxHitPoints;
    private int level;
    private int experience;
    private int score;
    private int moves;
    private int speed;
    private int resiliance;
    private int maxResiliance;
    private String name;

    public CharacterStats() {
        hitPoints = 10;
        maxHitPoints = 10;
        level = 1;
        experience = 0;
        score = 0;
        moves = 0;
        speed = 1;
        resiliance = 10;
        maxResiliance = 10;
        name = "";

    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public void addMove() {
        this.moves++;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void modifyHitPoints(int change) {
        this.hitPoints += change;
    }

    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    public void setMaxHitPoints(int maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void addExperience(int experience) {
        this.experience += experience;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getResiliance() {
        return resiliance;
    }

    public void setResiliance(int resiliance) {
        this.resiliance = resiliance;
    }

    public int getMaxResiliance() {
        return maxResiliance;
    }

    public void setMaxResiliance(int maxResiliance) {
        this.maxResiliance = maxResiliance;
    }

    public void modifyResiliance(int change) {
        resiliance += change;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
