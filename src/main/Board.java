package main;

import main.Point;
import main.Vector2d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

public class Board extends JComponent implements MouseInputListener, ComponentListener {
    private static final long serialVersionUID = 1L;
    private Point[][] points;
    private int size = 10;
    public int editType = 0;
    public double p = 0.3;
    public int maxSpeed = 5;
    private Random generator = new Random();

    public Board(int length, int height) {
        addMouseListener(this);
        addComponentListener(this);
        addMouseMotionListener(this);
        setBackground(Color.WHITE);
        setOpaque(true);
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
            for (int distance = -this.maxSpeed; distance <= this.maxSpeed; distance++) {
                if (!(lane == 0 && distance >= 0)) {
                    int neighborX = Math.floorMod((x + distance),points.length);
                    int neighborY = Math.floorMod((y + lane),points[0].length);
                    if (!(distance < 0 && points[neighborX][neighborY].getSpeed().x < Math.abs(distance))) {
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
        for (int x = 0; x < points.length; ++x) {
            for (int y = 0; y < points[x].length; ++y) {
                // TODO
                // Move cars
                if (points[x][y].isActivated() && !points[x][y].didMove() && points[x][y].getSpeed().x > 0) {
                    int newX = Math.floorMod((x + points[x][y].getSpeed().x),points.length);
                    int newY = Math.floorMod((y + points[x][y].getSpeed().y),points[0].length);
                    points[newX][newY].activate();
                    points[newX][newY].clone(points[x][y]);
                    points[x][y].clear();
                }
            }
        }
        this.repaint();
    }

    public void clear() {
        for (int x = 0; x < points.length; ++x)
            for (int y = 0; y < points[x].length; ++y) {
                points[x][y].clear();
            }
        this.repaint();
    }

    private void initialize(int length, int height) {
        points = new Point[length][height];

        for (int x = 0; x < points.length; ++x) {
            for (int y = 0; y < points[x].length; ++y) {
                points[x][y] = new Point(this.maxSpeed);
            }
        }
    }

    protected void paintComponent(Graphics g) {
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        g.setColor(Color.GRAY);
        drawNetting(g, size);
    }

    private void drawNetting(Graphics g, int gridSpace) {
        Insets insets = getInsets();
        int firstX = insets.left;
        int firstY = insets.top;
        int lastX = this.getWidth() - insets.right;
        int lastY = this.getHeight() - insets.bottom;

        int x = firstX;
        while (x < lastX) {
            g.drawLine(x, firstY, x, lastY);
            x += gridSpace;
        }

        int y = firstY;
        while (y < lastY) {
            g.drawLine(firstX, y, lastX, y);
            y += gridSpace;
        }

        for (x = 0; x < points.length; ++x) {
            for (y = 0; y < points[x].length; ++y) {
                Color color = new Color(255,255,255);
                // TODO
                if (points[x][y].isActivated()) {
                    if (points[x][y].didOvertake && points[x][y].getSpeed().y == -1) {
                        color = new Color(220, 0, 180);
                    }
                    else if (points[x][y].didOvertake && points[x][y].getSpeed().y == 1) {
                        color = new Color(0, 200, 180);
                    }
                    else
                        color = new Color(0,0,0);
                }
                g.setColor(color);
                g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
            }
        }

    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / size;
        int y = e.getY() / size;
        if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
            if (editType == 0) {
                points[x][y].clicked();
            }
            this.repaint();
        }
    }

    public void componentResized(ComponentEvent e) {
        int dlugosc = (this.getWidth() / size) + 1;
        int wysokosc = (this.getHeight() / size) + 1;
        initialize(dlugosc, wysokosc);
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX() / size;
        int y = e.getY() / size;
        if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
            if (editType == 0) {
                points[x][y].clicked();
            }
            this.repaint();
        }
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

}
