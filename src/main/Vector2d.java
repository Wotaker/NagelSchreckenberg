package main;

public class Vector2d {
    public int x;
    public int y;

    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Vector2d add(Vector2d vect){
        return new Vector2d(this.x+vect.x, this.y+vect.y);
    }

}
