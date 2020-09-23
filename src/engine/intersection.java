package engine;

public class intersection {
    // a class to store the intersection between the rays and the walls
    public vector ins;  // the computed intersection point
    public vector normal; // the normal of the wall at the intersection point
    public boolean hit; // the hit information whether the ray met the wall or not
    public double distance; // the distance between the intersection point and the car

    public intersection(){}
}