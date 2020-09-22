package engine;
import java.awt.Graphics2D;

/**
 *
 * Adapted from the C++ version writteh by Oren Becker:
 * http://www.ragestorm.net/tutorial?id=22
 */
public class RotatedRectangle {
    public static class _Vector2D {
        double x,y;
        
        void add(_Vector2D v) {
            x+=v.x;
            y+=v.y;
        }

        void sub(_Vector2D v) {
            x-=v.x;
            y-=v.y;
        }
        
        void rotate(double ang) {
            double t;
            double cosa = Math.cos(ang);
            double sina = Math.sin(ang);
            t = x; 
            x = t*cosa + y*sina; 
            y = -t*sina + y*cosa;
        }
    }
    
    public _Vector2D C,S;
    public double ang;
    //public double m_tol = 30;
    ray min45;
    ray forward;
    ray plus45;
    
    public RotatedRectangle(double x, double y, double width, double height, double angle) {
        C = new _Vector2D();
        S = new _Vector2D();
        C.x = x;
        C.y = y;
        S.x = width;
        S.y = height;
        ang = angle;
    }

    public static boolean RotRectsCollision(double x1, double y1, double width1, double height1, double angle1,
                                            double x2, double y2, double width2, double height2, double angle2) {
        return RotRectsCollision(new RotatedRectangle(x1,y1,width1,height1,angle1), 
                                 new RotatedRectangle(x2,y2,width2,height2,angle2));
    }
    
    // Rotated Rectangles Collision Detection, Oren Becker, 2001
    public static boolean RotRectsCollision(RotatedRectangle rr1, RotatedRectangle rr2)
    {
     _Vector2D A = new _Vector2D(), B = new _Vector2D(),   // vertices of the rotated rr2
               C,      // center of rr2
               BL, TR; // vertices of rr2 (bottom-left, top-right)

     double ang = rr1.ang - rr2.ang, // orientation of rotated rr1
           cosa = Math.cos(ang),           // precalculated trigonometic -
           sina = Math.sin(ang);           // - values for repeated use

     double t, x, a;      // temporary variables for various uses
     double dx;           // deltaX for linear equations
     double ext1, ext2;   // min/max vertical values

     // move rr2 to make rr1 cannonic
     C = new _Vector2D();
     C.x = rr2.C.x;
     C.y = rr2.C.y;
     C.sub(rr1.C);

     // rotate rr2 clockwise by rr2->ang to make rr2 axis-aligned
     C.rotate(rr2.ang);

     // calculate vertices of (moved and axis-aligned := 'ma') rr2
     BL = new _Vector2D();
     BL.x = C.x;
     BL.y = C.y;
     TR = new _Vector2D();
     TR.x = C.x;
     TR.y = C.y;
     BL.sub(rr2.S);
     TR.add(rr2.S);

     // calculate vertices of (rotated := 'r') rr1
     A.x = -rr1.S.y*sina; B.x = A.x; t = rr1.S.x*cosa; A.x += t; B.x -= t;
     A.y =  rr1.S.y*cosa; B.y = A.y; t = rr1.S.x*sina; A.y += t; B.y -= t;

     t = sina*cosa;

     // verify that A is vertical min/max, B is horizontal min/max
     if (t < 0)
     {
      t = A.x; A.x = B.x; B.x = t;
      t = A.y; A.y = B.y; B.y = t;
     }

     // verify that B is horizontal minimum (leftest-vertex)
     if (sina < 0) { B.x = -B.x; B.y = -B.y; }

     // if rr2(ma) isn't in the horizontal range of
     // colliding with rr1(r), collision is impossible
     if (B.x > TR.x || B.x > -BL.x) return false;

     // if rr1(r) is axis-aligned, vertical min/max are easy to get
     if (t == 0) {ext1 = A.y; ext2 = -ext1; }
     // else, find vertical min/max in the range [BL.x, TR.x]
     else
     {
      x = BL.x-A.x; a = TR.x-A.x;
      ext1 = A.y;
      // if the first vertical min/max isn't in (BL.x, TR.x), then
      // find the vertical min/max on BL.x or on TR.x
      if (a*x > 0)
      {
       dx = A.x;
       if (x < 0) { dx -= B.x; ext1 -= B.y; x = a; }
       else       { dx += B.x; ext1 += B.y; }
       ext1 *= x; ext1 /= dx; ext1 += A.y;
      }

      x = BL.x+A.x; a = TR.x+A.x;
      ext2 = -A.y;
      // if the second vertical min/max isn't in (BL.x, TR.x), then
      // find the local vertical min/max on BL.x or on TR.x
      if (a*x > 0)
      {
       dx = -A.x;
       if (x < 0) { dx -= B.x; ext2 -= B.y; x = a; }
       else       { dx += B.x; ext2 += B.y; }
       ext2 *= x; ext2 /= dx; ext2 -= A.y;
      }
     }

     // check whether rr2(ma) is in the vertical range of colliding with rr1(r)
     // (for the horizontal range of rr2)
     return !((ext1 < BL.y && ext2 < BL.y) ||
          (ext1 > TR.y && ext2 > TR.y));
    }
    
    /*
    public boolean raycast(double target_angle, Obstacle[] obstacles, Car subject, boolean is_forward, double tol, boolean go_backward){
        boolean[] min45ins, forwardins, plus45ins;
        boolean flag1 = false, flag2 = false, flag3 = false;
        min45ins = new boolean[obstacles.length];
        forwardins = new boolean[obstacles.length];
        plus45ins = new boolean[obstacles.length];

        min45 = new ray(new vector(subject.getX(),subject.getY()), subject.getAngle()-Math.PI/4);
        forward = new ray(new vector(subject.getX(),subject.getY()), subject.getAngle());
        plus45 = new ray(new vector(subject.getX(),subject.getY()), subject.getAngle()+Math.PI/4);
        m_tol = tol;

        if(!is_forward){
            min45.reverse();
            min45.angle -= Math.PI/2;
            forward.reverse();
            plus45.reverse();
            plus45.angle += Math.PI/2;
        }
        
        for(int i = 0; i<obstacles.length; i++){
            min45ins[i] = is_intersected(obstacles[i], min45);
            if(min45ins[i]){
                flag1 = true;
            }
            forwardins[i] = is_intersected(obstacles[i], forward);
            if(min45ins[i]){
                flag2 = true;
            }
            plus45ins[i] = is_intersected(obstacles[i], plus45);
            if(min45ins[i]){
                flag3 = true;
            }
        }
        if(flag2){
            if(flag1 && !flag3){
                target_angle = Math.PI/4;
                System.out.print("1");
                return true;
            }
            else if(!flag1 && flag3){
                target_angle = -Math.PI/4;
                System.out.println("2");
                return true;
            }
            else if(flag1 && flag3){
                go_backward = true;
                System.out.println("3");
                return true;
            }
            else if(!flag1 && !flag3){
                target_angle = Math.PI/4;
                System.out.println("4");
                return true;
            }
        }
        else if(flag1){
            if(!flag2 && flag3){
                target_angle = 0;
                System.out.println("5");
                return true;
            }
            else if(!flag2 && !flag3){
                target_angle = Math.PI/4;
                System.out.println("6");
                return true;
            }
        }
        else if(flag3){
            if(!flag1 && !flag2){
                target_angle = -Math.PI/4;
                System.out.println("7");
                return true;
            }
        }
        
        return false;
    }*/

    public intersection raycast(Car subject, Obstacle o, ray r){
        intersection i = new intersection();
        
        double hit_distance = 30;

        boolean lv = false;
        boolean rv = false;
        boolean th = false;
        boolean bh = false;

        vector lt = new vector(o.getX() - o.getWidth()/2, o.getY() + o.getHeight()/2);
        vector rt = new vector(o.getX() + o.getWidth()/2, o.getY( )+ o.getHeight()/2);
        vector lb = new vector(o.getX() - o.getWidth()/2, o.getY() - o.getHeight()/2);
        vector rb = new vector(o.getX() + o.getWidth()/2, o.getY() - o.getHeight()/2);
        
        vector direction = r.getdirection();

        vector left_vert_ins = new vector(lt.x(), direction.y()*(lt.x()-r.origin.x())/direction.x() + r.origin.y());
        vector right_vert_ins = new vector(rt.x(), direction.y()*(rt.x()-r.origin.x())/direction.x() + r.origin.y());
        vector top_horz_ins = new vector(direction.x()*(lt.y()-r.origin.y())/direction.y() + r.origin.x(), lt.y());
        vector bot_horz_ins = new vector(direction.x()*(lb.y()-r.origin.y())/direction.y() + r.origin.x(), lb.y());

        if(left_vert_ins.y() >= lb.y() && left_vert_ins.y() <= lt.y()){
            if(left_vert_ins.distance(subject.getX(), subject.getY()) < hit_distance){
                lv = true;
            }      
        }
        if(right_vert_ins.y() >= rb.y() && right_vert_ins.y() <= rt.y()){
            if(right_vert_ins.distance(subject.getX(), subject.getY()) < hit_distance){
                rv = true;
            }  
        }
        if(top_horz_ins.x() >= lt.x() && top_horz_ins.x() <= rt.x()){
            if(top_horz_ins.distance(subject.getX(), subject.getY()) < hit_distance){
                th = true;
            }  
        }
        if(bot_horz_ins.x() >= lb.x() && bot_horz_ins.x() <= rb.x()){
            if(bot_horz_ins.distance(subject.getX(), subject.getY()) < hit_distance){
                bh = true;
            }  
        }
        //System.out.println(bh);
        if(lv||rv||th||bh){
            i.hit = true;
        }
        //System.out.println(i.hit);
        double dist1 = left_vert_ins.distance(subject.getX(), subject.getY());
        double dist2 = right_vert_ins.distance(subject.getX(), subject.getY());
        double dist3 = top_horz_ins.distance(subject.getX(), subject.getY());
        double dist4 = bot_horz_ins.distance(subject.getX(), subject.getY());

        if(dist1 < dist2 && dist1 < dist3 && dist1 < dist4){
            i.ins = left_vert_ins;
            i.normal = new vector(-1, 0);
            i.distance = dist1;
        }
        else if(dist2 < dist1 && dist2 < dist3 && dist2 < dist4){
            i.ins = right_vert_ins;
            i.normal = new vector(1, 0);
            i.distance = dist2;
        }
        if(dist3 < dist1 && dist3 < dist2 && dist3 < dist4){
            i.ins = top_horz_ins;
            i.normal = new vector(0, 1);
            i.distance = dist3;
        }
        if(dist4 < dist1 && dist4 < dist2 && dist4 < dist3){
            i.ins = bot_horz_ins;
            i.normal = new vector(0, -1);
            i.distance = dist4;
        }

        return i;
        /*
        boolean lt_lb = vertical_edge_intersection(box_lt, box_lb, r);
        boolean lt_rt = horizontal_edge_intersection(box_lt, box_rt, r);
        boolean rt_rb = vertical_edge_intersection(box_rt, box_rb, r);
        boolean lb_rb = horizontal_edge_intersection(box_lb, box_rb, r);
        */
    }

    /*
    public vector vertical_edge_intersection(vector a, vector b, ray r){
        vector direction = r.getdirection();
        vector intersection = new vector(a.x(), direction.y()*(a.x()-r.origin.x())/direction.x() + r.origin.y());
        if(intersection.y() > b.y() && intersection.y() < a.y()){
            if(Math.sqrt((intersection.x()-r.origin.x())*(intersection.x()-r.origin.x())+(intersection.y()-r.origin.y())*(intersection.y()-r.origin.y())) < m_tol){
                return true;
            }
        }
        return false;
    }
    public boolean horizontal_edge_intersection(vector a, vector b, ray r){
        vector direction = r.getdirection();
        vector intersection = new vector(direction.x()*(a.y()-r.origin.y())/direction.y() + r.origin.x(), a.y());
        if(intersection.x() > a.x() && intersection.x() < b.x()){
            if(Math.sqrt((intersection.x()-r.origin.x())*(intersection.x()-r.origin.x())+(intersection.y()-r.origin.y())*(intersection.y()-r.origin.y())) < m_tol){
                return true;
            }
        }
        return false;
    }*/
}
