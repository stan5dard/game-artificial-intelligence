package engine;

public class vector {
    // a 2dimension vector class that is made for convenience.
    public double x, y, length;

    public vector(){
        x = 0;
        y = 0;
    }

    public vector(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double x(){
        return x;
    }

    public double y(){
        return y;
    }

    public double length(){
        this.length = Math.sqrt(x*x + y*y);
        return length;
    }

    public void normalize(){
        x = x/Math.sqrt(x*x + y*y);
        y = y/Math.sqrt(x*x + y*y);
    }

    public void reverse(){
        x = -x;
        y = -y;
    }
    public double distance(double x, double y){
        double dist = Math.sqrt((this.x-x)*(this.x-x) + (this.y-y)*(this.y-y));
        return dist;
    }

    public double dot(double x, double y){
        double d = this.x * x + this.y * y;
        return d;
    }

    public vector mult(double m){
        double a = this.x * m;
        double b = this.y * m;
        return new vector(a, b);
    }
}
