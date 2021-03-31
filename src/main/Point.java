package main;

import java.util.Random;

public class Point {
    private Random random = new Random();
    private final int maxSpeed;
    private int speedX;
    private int speedY;
    private Vector2d speed;
    private boolean activated;
    private boolean moved;
    public boolean didOvertake;
    public int offset;

    public Point(int maxSpeed){
        this.maxSpeed = maxSpeed;
        this.speed = new Vector2d(0,0);
        this.offset = 0;
        this.activated = false;
        this.moved = false;
        this.didOvertake = false;
    }

    public void clone(Point point){
        this.speed.x = point.speed.x;
        this.speed.y = point.speed.y;
        this.offset = point.offset;
        this.moved = true;
        this.didOvertake = point.didOvertake;
    }

    public void clicked() {
        // TODO
        if (!activated) {
            //activate and set speed
            this.activated = true;
//            this.speedX = random.nextInt(maxSpeed + 1);
            this.speed.x = 0;
        }
    }

    public void clear() {
        // TODO
        this.moved = false;
        this.activated = false;
        this.didOvertake = false;
        this.speedX = 0;
        this.speedY = 0;
        this.speed.x = 0;
        this.speed.y = 0;
        this.offset = 0;
    }

    public void changeSpeed(Vector2d changeVector){
        Vector2d newSpeed = this.speed.add(changeVector);
        if (newSpeed.x >= 0 && newSpeed.x <= this.maxSpeed){
            this.speed = newSpeed;
        }
    }

    public void activate(){
        this.activated = true;
    }

    public Vector2d getSpeed(){
        return this.speed;
    }

    public void setSpeed(Vector2d speed){
        this.speed = speed;
    }

    public void setMoved(boolean moved){
        this.moved = moved;
    }

    public boolean didMove(){
        return this.moved;
    }

    public boolean isActivated(){
        return this.activated;
    }
}