import java.util.ArrayList;
import java.util.Random;

public class PerlinNoise{
    static class Perlin1D{
        protected final ArrayList<Double> values = new ArrayList<>();
        protected final Random random = new Random();


        public Perlin1D(Long seed){
            if(seed == null){
                seed = new Random().nextLong();
            }
            random.setSeed(seed);
        }


        public Perlin1D(){
            random.setSeed(new Random().nextLong());
        }


        //Connect a straight line between the points, and get the value at that location
        public double linInterpolation(double x){
            while(x+1 > values.size()){
                values.add(random.nextDouble());
            }
            double low = values.get((int) Math.floor(x));
            double high = values.get((int) Math.ceil(x));
            x = x % 1;
            return low * (1 - x) + high * x;
        }


        //Connect a curved line flattening at the points, and get the value at that location
        public double cosInterpolation(double x){
            while(x+1 > values.size()){
                values.add(random.nextDouble());
            }
            double low = values.get((int) Math.floor(x));
            double high = values.get((int) Math.ceil(x));
            x = x % 1;
            x = (1 - Math.cos(x * Math.PI)) * 0.5;
            return low * (1 - x) + high * x;
        }
    }


    static class Perlin2D{
        protected final ArrayList<Perlin1D> per1D = new ArrayList<>();
        protected final Random random = new Random();


        public Perlin2D(Long seed){
            if(seed == null){
                seed = new Random().nextLong();
            }
            random.setSeed(seed);
        }


        public Perlin2D(){
            random.setSeed(new Random().nextLong());
        }


        //Connect a straight line between the points, and get the value at that location
        public double linInterpolation(double x, double y){
            while(y+1 > per1D.size()){
                per1D.add(new Perlin1D(random.nextLong()));
            }
            double low = per1D.get((int) Math.floor(y)).linInterpolation(x);
            double high = per1D.get((int) Math.ceil(y)).linInterpolation(x);
            y = y % 1;
            return low * (1 - y) + high * y;
        }


        //Connect a curved line  flattening at the points, and get the value at that location
        public double cosInterpolation(double x, double y){
            while(y+1 > per1D.size()){
                per1D.add(new Perlin1D(random.nextLong()));
            }
            double low = per1D.get((int) Math.floor(y)).cosInterpolation(x);
            double high = per1D.get((int) Math.ceil(y)).cosInterpolation(x);
            y = y % 1;
            y = (1 - Math.cos(y * Math.PI)) * 0.5;
            return low * (1 - y) + high * y;
        }
    }


    static class Perlin3D{
        protected final ArrayList<Perlin2D> per2D = new ArrayList<>();
        protected final Random random = new Random();


        public Perlin3D(Long seed){
            if(seed == null){
                seed = new Random().nextLong();
            }
            random.setSeed(seed);
        }


        public Perlin3D(){
            random.setSeed(new Random().nextLong());
        }


        //Connect a straight line between the points, and get the value at that location
        public double linInterpolation(double x, double y, double z){
            while(z+1 > per2D.size()){
                per2D.add(new Perlin2D(random.nextLong()));
            }
            double low = per2D.get((int) Math.floor(z)).linInterpolation(x, y);
            double high = per2D.get((int) Math.ceil(z)).linInterpolation(x, y);
            z = z % 1;
            return low * (1 - z) + high * z;
        }


        //Connect a curved line  flattening at the points, and get the value at that location
        public double cosInterpolation(double x, double y, double z){
            while(z+1 > per2D.size()){
                per2D.add(new Perlin2D(random.nextLong()));
            }
            double low = per2D.get((int) Math.floor(z)).cosInterpolation(x, y);
            double high = per2D.get((int) Math.ceil(z)).cosInterpolation(x, y);
            z = z % 1;
            z = (1 - Math.cos(z * Math.PI)) * 0.5;
            return low * (1 - z) + high * z;
        }
    }


    static class Perlin4D{
        protected final ArrayList<Perlin3D> per3D = new ArrayList<>();
        protected final Random random = new Random();


        public Perlin4D(Long seed){
            if(seed == null){
                seed = new Random().nextLong();
            }
            random.setSeed(seed);
        }


        public Perlin4D(){
            random.setSeed(new Random().nextLong());
        }


        //Connect a straight line between the points, and get the value at that location
        public double linInterpolation(double x, double y, double z, double t){
            while(t+1 > per3D.size()){
                per3D.add(new Perlin3D(random.nextLong()));
            }
            double low = per3D.get((int) Math.floor(t)).linInterpolation(x, y, z);
            double high = per3D.get((int) Math.ceil(t)).linInterpolation(x, y, z);
            t = t % 1;
            return low * (1 - t) + high * t;
        }


        //Connect a curved line  flattening at the points, and get the value at that location
        public double cosInterpolation(double x, double y, double z, double t){
            while(t+1 > per3D.size()){
                per3D.add(new Perlin3D(random.nextLong()));
            }
            double low = per3D.get((int) Math.floor(t)).cosInterpolation(x, y, z);
            double high = per3D.get((int) Math.ceil(t)).cosInterpolation(x, y, z);
            t = t % 1;
            t = (1 - Math.cos(t * Math.PI)) * 0.5;
            return low * (1 - t) + high * t;
        }
    }
}