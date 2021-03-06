package test;

import controllers.EmptyController;
import controllers.KeyboardController;
import controllers.WallAvoidanceController;
import engine.Car;
import engine.Game;
import engine.GameObject;
import engine.GameWindow;
import engine.Obstacle;
import java.awt.Color;
import controllers.SeekController;


/**
 *
 * @author santi
 */
public class WallAvoidanceSeekScenario {
    /*
        Goal of this exercise:
        - Write a controller for "car2" that uses the "Seek" steerig behavior to always run
          after car1 (that is controlled using the arrow keys).
        - Include "wall avoidance" in your controller to prevent it from hitting any wall
    */
    
    public static void main(String args[]) throws Exception {
        Game game = new Game(800,600, 25);
        /*
        // set up the outside walls:
        game.add(new Obstacle(0,0,800,25,Color.GRAY));
        game.add(new Obstacle(0,575,800,25,Color.GRAY));
        game.add(new Obstacle(0,0,25,600,Color.GRAY));
        game.add(new Obstacle(775,0,25,600,Color.GRAY));
        // set up some inside walls
        game.add(new Obstacle(100,100,25,25,Color.GRAY));
        game.add(new Obstacle(675,100,25,25,Color.GRAY));
        game.add(new Obstacle(100,475,25,25,Color.GRAY));
        game.add(new Obstacle(657,475,25,25,Color.GRAY));
        game.add(new Obstacle(375,150,50,300,Color.GRAY));
        */
        
        Obstacle[] obstacles = new Obstacle[9];
        obstacles[0] = new Obstacle(0,0,800,25,Color.GRAY);
        obstacles[1] = new Obstacle(0,575,800,25,Color.GRAY);
        obstacles[2] = new Obstacle(0,0,25,600,Color.GRAY);
        obstacles[3] = new Obstacle(775,0,25,600,Color.GRAY);
        obstacles[4] = new Obstacle(100,100,25,25,Color.GRAY);
        obstacles[5] = new Obstacle(675,100,25,25,Color.GRAY);
        obstacles[6] = new Obstacle(100,475,25,25,Color.GRAY);
        obstacles[7] = new Obstacle(657,475,25,25,Color.GRAY);
        obstacles[8] = new Obstacle(375,150,50,300,Color.GRAY);
        for(int i=0;i<obstacles.length;i++){
            game.add(obstacles[i]);
        }


        // set up the cars and markers:
        GameObject car1 = new Car("graphics/redcar.png",200,300,-Math.PI/2, new KeyboardController());
        GameObject car2 = new Car("graphics/bluecar.png",600,300,-Math.PI/2, new WallAvoidanceController(car1, obstacles));

        game.add(car1);
        game.add(car2);
        GameWindow.newWindow(game);
    }
}
