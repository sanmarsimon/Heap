package tp4;

import java.util.List;

public class Point implements Comparable<Point> {
    // TODO vous pouvez modifier ce que vous voulez, tant que vous ne modifiez pas les tests

    private Integer x;
    private Integer y;
    private Integer distance;
    private Integer pos;

    public Point(String xy) {
        String[] xAndY = xy.split(" +");
        this.x = Integer.parseInt(xAndY[0]);
        this.y = Integer.parseInt(xAndY[1]);
    }

    public Point(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public void setDistance(Integer dist) {
        distance = dist;
    }

    public int getDistance(){
        return distance;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }

    public void calculateDistance(Point center){
        distance = Math.abs(center.x - this.x)  + Math.abs(center.y - this.y);
    }

    @Override
    public String toString() {
        return String.format("{X: %d, Y: %d, DIST: %d, POS: %d}", x, y, distance, pos);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) {
            return false;
        }

        Point point = (Point)obj;
        if(point.x.equals(x) && point.y.equals(y)){
            return point.pos.equals(pos);
        }
        return point.x.equals(x) && point.y.equals(y);
    }

    @Override
    public int compareTo(Point point) {
        if((this.distance - point.distance) == 0){
            return (this.pos - point.pos);
        }
        return this.distance - point.distance;
    }
}
