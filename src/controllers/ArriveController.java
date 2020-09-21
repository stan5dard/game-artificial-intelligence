package controllers;
import engine.Car;
import engine.Game;
import engine.GameObject;
import engine.vector;

public class ArriveController extends Controller {
GameObject targetdest;

    public ArriveController(GameObject target){
        targetdest = target;
    }

    public void update(Car subject, Game game, double delta_t, double controlVariables[]) {
        controlVariables[VARIABLE_STEERING] = 0;
        controlVariables[VARIABLE_THROTTLE] = 0;
        controlVariables[VARIABLE_BRAKE] = 0;
        double target_radius = 300;
        double slow_radius = 400;

        double steer = 0;
        double throt = 0;
        double brk = 0;
        //double velocity = 0;

        vector d = new vector(targetdest.getX() - subject.getX(), targetdest.getY() - subject.getY());
        vector nd = new vector(d.x / d.length(), d.y / d.length());
        double distance = d.length();

        vector forward = subject.getforward();
        vector right = subject.getright();
        
        double rightdot = nd.x*right.x + nd.y*right.y;
        double forwarddot = nd.x*forward.x + nd.y*forward.y;



        if(distance < target_radius){
            if(forwarddot >= 0){
                throt = 0;    
            }
            else if(forwarddot < 0 && subject.getSpeed() > 0){
                brk = 0.7;
            }
        }
        else if(distance > slow_radius){
            if(forwarddot >= 0){
                throt = 1;    
            }
            else if(forwarddot < 0){
                brk = 0;
            }
        }
        else{
            if(forwarddot >= 0){
                throt =  distance/slow_radius;    
            }
            else if(forwarddot < 0){
                brk = 0;
            }
            //velocity = distance/slow_radius;
        }

        //System.out.println(velocity);


        
        System.out.println(brk);
        steer = rightdot;

        controlVariables[VARIABLE_STEERING] = steer;
        controlVariables[VARIABLE_THROTTLE] = throt;
        controlVariables[VARIABLE_BRAKE] = brk;
    }
}
