package engine;

public class ray {
    public vector origin;
    public double angle;
    public vector direction;

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

    public void reverse(){
        direction.reverse();
        angle+=Math.PI;
    }
}
