package controllers;
import engine.Car;
import engine.Game;
import engine.GameObject;
import engine.Obstacle;
import engine.ray;
import engine.intersection;

import java.time.Year;

import engine.vector;

public class WallAvoidanceController extends Controller {
    // a controller to make a blue car to avoid walls
    public GameObject targetdest;
    public double unitdist = 50;
    public double new_x;    // a x value of temp destination when the blue car meets a wall
    public double new_y;    // a y value of temp destination when the blue car meets a wall
    public Obstacle[] obstacles;    // walls that were given in this example

    public WallAvoidanceController(GameObject target, Obstacle[] obstacles){
        targetdest = target;
        this.obstacles = obstacles;
    }

    public void update(Car subject, Game game, double delta_t, double controlVariables[]) {
        controlVariables[VARIABLE_STEERING] = 0;
        controlVariables[VARIABLE_THROTTLE] = 0;
        controlVariables[VARIABLE_BRAKE] = 0;
        double steer = 0;   // variables to control the blue car
        double throt = 0;
        double brk = 0;
        vector origin = new vector(subject.getX(), subject.getY());     // a place where the red car is.
        double angle = subject.getAngle();          // The angle of the red car

        ray forwardray = new ray(origin, angle);        // To avoid the walls, we will cast three rays at three different directions.
        ray leftray = new ray(origin, angle - Math.PI/4);
        ray rightray = new ray(origin, angle + Math.PI/4);

        intersection ints_forward, ints_left, ints_right;   // a variables to store the information of intersection between the walls and the rays
        boolean forward_hit = false, left_hit = false, right_hit = false;   // a boolean variable to keep whether each three rays met the walls or not
        int i = 0, j = 0, k = 0;

        ints_forward = subject.getCollisionBox().raycast(subject, obstacles[0], forwardray);    // perform a raycast method. store the information of the intersected points
        ints_left = subject.getCollisionBox().raycast(subject, obstacles[0], leftray);
        ints_right = subject.getCollisionBox().raycast(subject, obstacles[0], rightray);

        for(i=0; i<obstacles.length; i++){
            ints_forward = subject.getCollisionBox().raycast(subject, obstacles[i], forwardray);
            if(ints_forward.hit){
                //System.out.println(i);
                forward_hit = true;     // if there is a intersection, stop the loop
                break;
            }
        }
        for(j=0; j<obstacles.length; j++){
            ints_left = subject.getCollisionBox().raycast(subject, obstacles[j], leftray);
            if(ints_left.hit){
                //System.out.println(j);
                left_hit = true;
                break;
            }
        }
        for(k=0; k<obstacles.length; k++){
            ints_right = subject.getCollisionBox().raycast(subject, obstacles[k], rightray);
            if(ints_right.hit){
                //System.out.println(k);
                right_hit = true;
                break;
            }
        }
        //System.out.println("forward : "+i+"  left : "+j+"   right : "+k);


        // in this part, we will deal with the case when the car met the wall.
        // the car first has to go to a safe temporary point.
        // Therefore, now d, the final destination is not the red car, and it is newely computed.
        vector d;
        vector nd;
        if(forward_hit ){
            double new_x = origin.x() + unitdist * ints_forward.normal.x();
            double new_y = origin.y() + unitdist * ints_forward.normal.y();
            
            d = new vector(new_x - subject.getX(), new_y - subject.getY());
            nd = new vector(d.x / d.length(), d.y / d.length());
        }
        else if(left_hit && !forward_hit && !right_hit){
            double new_x = origin.x() + unitdist * ints_left.normal.x();
            double new_y = origin.y() + unitdist * ints_left.normal.y(); 
            d = new vector(new_x - subject.getX(), new_y - subject.getY());
            nd = new vector((Math.random()%4+1)*d.x / d.length(), (Math.random()%4+1)*d.y / d.length()); // random numbers were multiplied to prevent stall
        }
        else if(right_hit && !forward_hit && !left_hit){
            double new_x = origin.x() + unitdist * ints_right.normal.x();
            double new_y = origin.y() + unitdist * ints_right.normal.y(); 
            d = new vector(new_x - subject.getX(), new_y - subject.getY());
            nd = new vector((Math.random()%4+1)*d.x / d.length(), (Math.random()%4+1)*d.y / d.length());
        }
        else{ // here, the car didn't met the wall. in this case, the blue car has to follow the red car.
            d = new vector(targetdest.getX()-subject.getX(), targetdest.getY()-subject.getY());
            nd = new vector(d.x / d.length(), d.y / d.length());
        }

        // After computing the destination, use seek method to follow the target.
        vector forward = subject.getforward();
        vector right = subject.getright();
        
        double rightdot = nd.x*right.x + nd.y*right.y;
        double forwarddot = nd.x*forward.x + nd.y*forward.y;
        
        if(forwarddot >= 0){
            throt = 0.7;
        }
        else if(forwarddot < 0){
            brk = 0.7;
        }
        steer = rightdot;
    
        controlVariables[VARIABLE_STEERING] = steer;
        controlVariables[VARIABLE_THROTTLE] = throt;
        controlVariables[VARIABLE_BRAKE] = brk; 
    }
}
