package controllers;
import engine.Car;
import engine.Game;
import engine.GameObject;
import engine.vector;

public class SeekController extends Controller {
GameObject targetdest;

    public SeekController(GameObject target){
        targetdest = target;
    }

    public void update(Car subject, Game game, double delta_t, double controlVariables[]) {
        controlVariables[VARIABLE_STEERING] = 0;
        controlVariables[VARIABLE_THROTTLE] = 0;
        controlVariables[VARIABLE_BRAKE] = 0;
        double steer = 0;
        double throt = 0;
        double brk = 0;

        vector d = new vector(targetdest.getX() - subject.getX(), targetdest.getY() - subject.getY());
        vector nd = new vector(d.x / d.length(), d.y / d.length());

        double angle = subject.getAngle();
        vector forward = new vector(Math.cos(angle), Math.sin(angle));
        vector right = new vector(Math.cos(angle+Math.PI/2), Math.sin(angle+Math.PI/2));
        
        double rightdot = nd.x*right.x + nd.y*right.y;
        double forwarddot = nd.x*forward.x + nd.y*forward.y;

        if(forwarddot >= 0){
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
