package controllers;
import engine.Car;
import engine.Game;
import engine.GameObject;
import engine.vector;

public class SeekController extends Controller {
    // a controller to make blue car to seek a red car.
GameObject targetdest;

    public SeekController(GameObject target){
        targetdest = target;
    }

    public void update(Car subject, Game game, double delta_t, double controlVariables[]) {
        controlVariables[VARIABLE_STEERING] = 0;
        controlVariables[VARIABLE_THROTTLE] = 0;
        controlVariables[VARIABLE_BRAKE] = 0;
        double steer = 0;       // variables to control the car
        double throt = 0;
        double brk = 0;

        vector d = new vector(targetdest.getX() - subject.getX(), targetdest.getY() - subject.getY());  // get the direction of the target. In this case, the target is red car
        vector nd = new vector(d.x / d.length(), d.y / d.length()); // normalized d

        vector forward = subject.getforward();  // get the forward vector of the subject. This is computed inside engine.car
        vector right = subject.getright();      // get the right vector of the subject. This is computed inside engine.car

        double rightdot = nd.x*right.x + nd.y*right.y;          // A determinant to decide whether the car has to turn right or left.
        double forwarddot = nd.x*forward.x + nd.y*forward.y;    // A computed value of steer.

        if(forwarddot >= 0){    // if this value is positive, then the car has to go front.
            throt = 1;
        }
        else if(forwarddot < 0){
            brk = 1;
        }
        steer = rightdot;

        controlVariables[VARIABLE_STEERING] = steer;
        controlVariables[VARIABLE_THROTTLE] = throt;
        controlVariables[VARIABLE_BRAKE] = brk;
    }
}
