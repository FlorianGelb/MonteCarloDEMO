import java.util.ArrayList;
import java.util.Random;

public class MonteCarlo
{
    protected ArrayList<ArrayList<Double>> MTCNodes = new ArrayList<>();
    protected Random randomGenerator = new Random();

    protected double area = 0;
    protected int n = 0;
    protected int functionType = 0;

    public MonteCarlo(int n, int functionType)
    {
        this.n = n;
        this.functionType = functionType;
    }

    protected double function(double x)
    {
        switch (functionType){
            case 0:
                return x;
            case 1:
                return Math.pow(x, 2);
            case 2:
                return Math.pow(x, 3);
            case 3:
                return Math.sin(x);
            case 4:
                return Math.cos(x);
            case 5:
                return (1/(Math.pow(Math.E, -x) + 1));
            case 6:
                return Math.pow(Math.E, x);

        }
        return 0;
    }

    public int getN(){return n;}
    public void setN(int n){this.n = n;}

    public int getFunctionType(){return functionType;}
    public void setFunctionType(int f){functionType = f;}

    public ArrayList<ArrayList<Double>> getMTCNodes() {return MTCNodes;}

}
