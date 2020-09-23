package engine;

public class ray {
// a ray class that is used to implement avoiddance controller.

    public vector origin; // the origin of the ray
    public double angle;    // the angle of the ray
    public vector direction;    // the direction of the ray

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
