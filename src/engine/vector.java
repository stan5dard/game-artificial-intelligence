package engine;

public class vector {
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
}
