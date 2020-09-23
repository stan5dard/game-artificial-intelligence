package controllers;
import engine.Car;
import engine.Game;
import engine.GameObject;
import engine.vector;

public class ArriveController extends Controller {
    // Implemented arrive controller. Used to make a car to arrive at certain position.

GameObject targetdest;

    public ArriveController(GameObject target){
        targetdest = target; //Set a target. In this case, the target should be gree dot which is a final destination.
    }

    public void update(Car subject, Game game, double delta_t, double controlVariables[]) {
        controlVariables[VARIABLE_STEERING] = 0;
        controlVariables[VARIABLE_THROTTLE] = 0;
        controlVariables[VARIABLE_BRAKE] = 0;
        double target_radius = 300;     // target radius and slow radius. The car runs at maximum speed when it is out of slow_radius range.
        double slow_radius = 400;       // the speed gets to 0 when the car is inside of target_radius range.

        double steer = 0;       // variables to control the car
        double throt = 0;
        double brk = 0;

        vector d = new vector(targetdest.getX() - subject.getX(), targetdest.getY() - subject.getY());  // d is the direction of the final destination
        vector nd = new vector(d.x / d.length(), d.y / d.length());                                     // nd is the normalized vector of d.
        double distance = d.length();      // compute the distance from the car to destination.

        vector forward = subject.getforward();  // get the forward and right vector of the subject car.
        vector right = subject.getright();

        double rightdot = nd.x*right.x + nd.y*right.y;          //compute whether the destination is placed on the right or left
        double forwarddot = nd.x*forward.x + nd.y*forward.y;    // compute whether the destination is placed on the front or back



        if(distance < target_radius){
            if(forwarddot >= 0){
                throt = 0;    // if the car has to go front, and the car is inside of target_radius, then the acceleration should be 0.
            }
            else if(forwarddot < 0 && subject.getSpeed() > 0){
                brk = 0.7;      // due to inertia, the car has to brake to stop at certain point.
            }
        }
        else if(distance > slow_radius){
            if(forwarddot >= 0){
                throt = 1;    // if the car is outside of slow_radius, then the car has to run at maximum speed
            }
            else if(forwarddot < 0){
                brk = 0;
            }
        }
        else{
            if(forwarddot >= 0){
                throt =  distance/slow_radius;    // when the car is between target_radius and slow_radius, then the car has to slow down gradually.
            }
            else if(forwarddot < 0){
                brk = 0;
            }
        }

        //System.out.println(velocity);


        
        //System.out.println(brk);
        steer = rightdot;

        controlVariables[VARIABLE_STEERING] = steer;
        controlVariables[VARIABLE_THROTTLE] = throt;
        controlVariables[VARIABLE_BRAKE] = brk;
    }
}
