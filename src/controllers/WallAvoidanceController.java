package controllers;
import engine.Car;
import engine.Game;
import engine.GameObject;
import engine.RotatedRectangle;
import engine.Obstacle;

import java.time.Year;

import engine.vector;

public class WallAvoidanceController extends Controller {
    GameObject targetdest;
    double unitdist = 50;
    double new_x;
    double new_y;
    Obstacle[] obstacles;

    public WallAvoidanceController(GameObject target, Obstacle[] obstacles){
        targetdest = target;
        this.obstacles = obstacles;
    }

    public void update(Car subject, Game game, double delta_t, double controlVariables[]) {
        controlVariables[VARIABLE_STEERING] = 0;
        controlVariables[VARIABLE_THROTTLE] = 0;
        controlVariables[VARIABLE_BRAKE] = 0;
        double steer = 0;
        double throt = 0;
        double brk = 0;
        double target_angle = 0;
        
        if(subject.getCollisionBox().raycast(target_angle, obstacles, subject.getAngle(), new vector(subject.getX(), subject.getY()))){
            new_x = subject.getX() + unitdist * Math.cos(subject.getAngle()+target_angle);
            new_y = subject.getY() + unitdist * Math.sin(subject.getAngle()+target_angle);
            vector d = new vector(new_x - subject.getX(), new_y - subject.getY());
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
        }
        else{
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
        }
        controlVariables[VARIABLE_STEERING] = steer;
        controlVariables[VARIABLE_THROTTLE] = throt;
        controlVariables[VARIABLE_BRAKE] = brk;
    }
}
