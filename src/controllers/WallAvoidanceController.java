package controllers;
import engine.Car;
import engine.Game;
import engine.GameObject;
import engine.RotatedRectangle;
import engine.Obstacle;
import engine.ray;
import engine.intersection;

import java.time.Year;

import engine.vector;

public class WallAvoidanceController extends Controller {
    public GameObject targetdest;
    public double unitdist = 70;
    public double new_x;
    public double new_y;
    public Obstacle[] obstacles;

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
        vector origin = new vector(subject.getX(), subject.getY());
        double angle = subject.getAngle();

        ray forwardray = new ray(origin, angle);
        ray leftray = new ray(origin, angle - Math.PI/4);
        ray rightray = new ray(origin, angle + Math.PI/4);

        intersection ints_forward, ints_left, ints_right;
        boolean forward_hit = false, left_hit = false, right_hit = false;
        int i = 0, j = 0, k = 0;

        ints_forward = subject.getCollisionBox().raycast(subject, obstacles[0], forwardray);
        ints_left = subject.getCollisionBox().raycast(subject, obstacles[0], leftray);
        ints_right = subject.getCollisionBox().raycast(subject, obstacles[0], rightray);

        for(i=0; i<obstacles.length; i++){
            ints_forward = subject.getCollisionBox().raycast(subject, obstacles[i], forwardray);
            if(ints_forward.hit){
                //System.out.println(i);
                forward_hit = true;
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
        //System.out.println("forward : " + ints_forward.hit + "  left : " + ints_left.hit + "   right : " + ints_right.hit);
        System.out.println("forward : "+i+"  left : "+j+"   right : "+k);

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
            nd = new vector(d.x / d.length(), d.y / d.length());
        }
        else if(right_hit && !forward_hit && !left_hit){
            double new_x = origin.x() + unitdist * ints_right.normal.x();
            double new_y = origin.y() + unitdist * ints_right.normal.y(); 
            d = new vector(new_x - subject.getX(), new_y - subject.getY());
            nd = new vector(d.x / d.length(), d.y / d.length());
        }
        else{
            d = new vector(targetdest.getX()-subject.getX(), targetdest.getY()-subject.getY());
            nd = new vector(d.x / d.length(), d.y / d.length());
        }
        vector forward = subject.getforward();
        vector right = subject.getright();
        
        double rightdot = nd.x*right.x + nd.y*right.y;
        double forwarddot = nd.x*forward.x + nd.y*forward.y;
        
        if(forwarddot >= 0){
            throt = 0.7;
        }
        else if(forwarddot < 0){
            brk = 1;
        }
        steer = rightdot;
    

        
        /*
        if(subject.getCollisionBox().raycast(target_angle, obstacles, subject, is_forward, unitdist, go_backward)){
            //System.out.println("inside of raycast");
            new_x = subject.getX() + unitdist * Math.cos(subject.getAngle()+target_angle);
            new_y = subject.getY() + unitdist * Math.sin(subject.getAngle()+target_angle);
            System.out.println(target_angle);
            vector d = new vector(new_x - subject.getX(), new_y - subject.getY());
            vector nd = new vector(d.x / d.length(), d.y / d.length());

            vector forward = subject.getforward();
            vector right = subject.getright();
            
            double rightdot = nd.x*right.x + nd.y*right.y;
            double forwarddot = nd.x*forward.x + nd.y*forward.y;
            //System.out.println(go_backward);
            if(go_backward){
                //System.out.println("go_backward");
                brk = 1;
            }
            else{
                if(forwarddot >= 0){
                    throt = 1;
                }
                else if(forwarddot < 0){
                    brk = 1;
                }
                steer = rightdot;
            }
        }
        else{
            System.out.println("inside of seek");
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
        }*/
        controlVariables[VARIABLE_STEERING] = steer;
        controlVariables[VARIABLE_THROTTLE] = throt;
        controlVariables[VARIABLE_BRAKE] = brk;
    }
}
