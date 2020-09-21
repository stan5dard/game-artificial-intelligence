package controllers;
import engine.Car;
import engine.Game;
import engine.GameObject;
import engine.RotatedRectangle;
import engine.Obstacle;
import engine.ray;

import java.time.Year;

import engine.vector;

public class WallAvoidanceController extends Controller {
    public GameObject targetdest;
    public double unitdist = 50;
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
        ray rayp45;
        ray raym45;
        ray ray0;
        boolean m45ins = false;
        boolean p45ins = false;
        boolean ins0 = false;

        
        double angle = subject.getAngle();
        vector forward = subject.getforward();
        vector right = subject.getright();
        vector subject_position = new vector(subject.getX(), subject.getY());

        rayp45 = new ray(subject_position, angle + Math.PI/4);
        raym45 = new ray(subject_position, angle - Math.PI/4);
        ray0 = new ray(subject_position, angle);

        for(int i = 0; i<obstacles.length; i++){
            m45ins = subject.getCollisionBox().is_intersected(obstacles[i], raym45);
        }
        for(int i = 0; i<obstacles.length; i++){
            ins0 = subject.getCollisionBox().is_intersected(obstacles[i], ray0);
        }
        for(int i = 0; i<obstacles.length; i++){
            p45ins = subject.getCollisionBox().is_intersected(obstacles[i], rayp45);
        }
        System.out.println("45 : "+ m45ins+"    0 : "+ins0+"    p45ins : "+p45ins);

        vector d;
        vector nd;
        
        if(m45ins && !ins0 && !p45ins){
            new_x = subject.getX() + unitdist * Math.cos(subject.getAngle() + Math.PI/4);
            new_y = subject.getY() + unitdist * Math.sin(subject.getAngle() + Math.PI/4);
            d = new vector(new_x, new_y);
        }

        else if(!m45ins && !ins0 && p45ins){
            new_x = subject.getX() + unitdist * Math.cos(subject.getAngle() - Math.PI/4);
            new_y = subject.getY() + unitdist * Math.sin(subject.getAngle() - Math.PI/4);
            d = new vector(new_x, new_y);
        }

        else{
            d = new vector(targetdest.getX() - subject.getX(), targetdest.getY() - subject.getY());
        }
        nd = new vector(d.x / d.length(), d.y / d.length());

        double rightdot = nd.x*right.x + nd.y*right.y;
        double forwarddot = nd.x*forward.x + nd.y*forward.y;

        if(forwarddot >= 0){
            throt = 1;
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
