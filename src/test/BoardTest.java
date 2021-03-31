package test;

import java.util.Random;
import main.Point;
import main.Vector2d;

public class BoardTest {
    private Point[][] points;
    private int length;
    private int height;
    private double carsPassed;     // carsPassed
    private double velocitySum;
    public double p;
    private int numberOfCars;
    public int maxSpeed;
    private Random generator = new Random();
    public boolean countFlag;

    public BoardTest(double p, int maxSpeed, int length, int height) {
        this.maxSpeed = maxSpeed;
        this.p = p;
        this.length = length;
        this.height = height;
        this.carsPassed = 0;
        this.velocitySum = 0;
        this.countFlag = false;

    }

    private int neighborDistance(int x, int y){
        for (int i = 1; i <= points[x][y].getSpeed().x; i++){
            if (points[(i+x)%points.length][y].isActivated()) {
                return i;
            }
        }
        return -1;
    }

    private boolean canOvertake(int x, int y, int direction){
        //left -> "-" right -> "+"
        for (int lane = 0; Math.abs(lane) <= 2; lane += direction){
            for (int distance = -this.maxSpeed; distance <= points[x][y].getSpeed().x; distance++) {
                if (!(lane == 0 && distance >= 0)) {
                    int neighborX = Math.floorMod((x + distance),points.length);
                    int neighborY = Math.floorMod((y + lane),points[0].length);
                    if (distance > 0 && Math.abs(lane) == 2 ){
                        //if car on lane 2 is overtaking on the lane that our car also wants to overtake
                        if(points[neighborX][neighborY].isActivated() && points[neighborX][neighborY].getSpeed().y == -direction)
                            return false;
                    }
                    else if (!(distance < 0 && points[neighborX][neighborY].getSpeed().x < Math.abs(distance))) {
                        if (points[neighborX][neighborY].isActivated()) return false;
                    }
                }
            }
        }
        return true;
    }

    public void iteration(){
        for (int x = 0; x < points.length; ++x) {
            for (int y = 0; y < points[x].length; ++y) {
                // TODO
                // Accelerate and set moved flag to false
                if(points[x][y].isActivated()) {
                    points[x][y].setMoved(false);
                    points[x][y].setSpeed(new Vector2d(points[x][y].getSpeed().x, 0));
                    points[x][y].didOvertake = false;
                    points[x][y].changeSpeed(new Vector2d(1,0));
                }
            }
        }
        for (int x = 0; x < points.length; ++x) {
            for (int y = 0; y < points[x].length; ++y) {
                // TODO
                // Decelerate or overtake
                if(points[x][y].isActivated()) {
                    int distance = neighborDistance(x, y);
                    int firstDirection = -1;
                    if (points[x][y].offset < 0) {
                        //wants to overtake from right
                        firstDirection = 1;
                    }
                    if (distance > 0 || points[x][y].offset != 0) {
                        int estimatedSpeed = distance - 1;
                        if (canOvertake(x, y, firstDirection)) {
                            points[x][y].changeSpeed(new Vector2d(0,firstDirection));
                            points[x][y].didOvertake = true;
                            points[x][y].offset += firstDirection;
                        } else if (distance > 0 && canOvertake(x, y, -firstDirection)) {
                            points[x][y].changeSpeed(new Vector2d(0,-firstDirection));
                            points[x][y].didOvertake = true;
                            points[x][y].offset -= firstDirection;
                        } else if (distance > 0) {
                            points[x][y].setSpeed(new Vector2d(estimatedSpeed, 0));
                        }
                    }
                }
            }
        }
        for (int x = 0; x < points.length; ++x) {
            for (int y = 0; y < points[x].length; ++y) {
                // TODO
                // Random decelerate
                if (points[x][y].isActivated() && generator.nextInt(1000) < p*1000-1)
                    points[x][y].changeSpeed(new Vector2d(-1, 0));
            }
        }
        double iterSpeedSum = 0;
        for (int x = 0; x < points.length; ++x) {
            for (int y = 0; y < points[x].length; ++y) {
                // TODO
                // Move cars
                if (points[x][y].isActivated() && !points[x][y].didMove() && points[x][y].getSpeed().x > 0) {
//                    if(countFlag) {
//                        carsPassed += 1;
//                    }
                    iterSpeedSum += points[x][y].getSpeed().x;
                    if(countFlag && (x + points[x][y].getSpeed().x) >= points.length) {
                        carsPassed += 1;
                    }
                    int newX = Math.floorMod((x + points[x][y].getSpeed().x), points.length);
                    int newY = Math.floorMod((y + points[x][y].getSpeed().y), points[0].length);
                    points[newX][newY].activate();
                    points[newX][newY].clone(points[x][y]);
                    points[x][y].clear();
                }
            }
        }
        if(countFlag) {
            this.velocitySum += iterSpeedSum / this.numberOfCars;
        }
    }

    public void clear() {
        for (int x = 0; x < points.length; ++x)
            for (int y = 0; y < points[x].length; ++y) {
                points[x][y].clear();
            }
    }

    public void initialize() {
        points = new Point[this.length][this.height];

        for (int x = 0; x < points.length; ++x) {
            for (int y = 0; y < points[x].length; ++y) {
                points[x][y] = new Point(this.maxSpeed);
            }
        }
    }

    public void placeCars(int N){
        this.numberOfCars = N;
        if (N >= (double)0.8*this.length*this.height){
            //if N is above 80% of board area, then don't pick position randomly
            for (int x = 0; x < points.length && N > 0; ++x) {
                for (int y = 0; y < points[x].length && N > 0; ++y, N-=1) {
                    points[x][y].clicked();
                }
            }
        }
        else{
            for (int n = 1; n <= N; n+=1){
                int x = generator.nextInt(this.length);
                int y = generator.nextInt(this.height);
                while (points[x][y].isActivated()){
                    x = generator.nextInt(this.length);
                    y = generator.nextInt(this.height);
                }
                points[x][y].clicked();
            }
        }
    }

    public double getVelocitySum(){
        return this.velocitySum;
    }

    public double getCarsPassed(){
        return this.carsPassed;
    }

}
