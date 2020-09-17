package engine;

public class ray {
    vector origin;
    double angle;
    vector direction;

    public ray(vector origin, double angle){
        this.origin = origin;
        this.angle = angle;
        direction = new vector(Math.cos(angle), Math.sin(angle));
    }

    public vector getorigin(){
        return origin;
    }
    public double getangle(){
        return angle;
    }
    public vector getdirection(){
        return direction;
    }
}
