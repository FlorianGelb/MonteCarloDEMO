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

    protected ArrayList<Double> zeroPoints(double x0, double x)
    {
        ArrayList<Double> returnArrayList = new ArrayList<>();
        if (functionType == 0 || functionType == 1 || functionType == 2 || functionType == 3){returnArrayList.add(0.0);}
        if (functionType == 4 || functionType == 5){
            double phaseShiftFactor = 1;
            if(functionType == 5){ phaseShiftFactor = 0.5;}

            int negativeZeroPointFactors = (int)(x0 / (phaseShiftFactor * Math.PI));
            int positiveZeroPontFactors = (int)(x / (phaseShiftFactor * Math.PI));

            for (int i = 0; i >= negativeZeroPointFactors; i--)
            {
                returnArrayList.add(i * (phaseShiftFactor * Math.PI));
            }

            for(int i = 0; i <= positiveZeroPontFactors; i++){
                returnArrayList.add(i * (phaseShiftFactor * Math.PI));
            }


        }

        else{returnArrayList.add(Double.MAX_VALUE);}
        return returnArrayList;
    }

    protected double indefinitIntegralFunction(double x)
    {
        switch (functionType){
            case 0:
                return 0.5 * Math.pow(x, 2);
            case 1:
                return (1/3) * Math.pow(x, 3);
            case 2:
                return (1/4) * Math.pow(x, 4);
            case 3:
                return Math.cos(x);
            case 4:
                return -Math.sin(x);
            case 5:
                return Math.log(Math.pow(Math.E, -x) + 1);
            case 6:
                return Math.pow(Math.E, x);

        }
        return 0;

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

    protected double calculateExactIntegral(int x0, int x)
    {
        return Math.abs(indefinitIntegralFunction(x) - indefinitIntegralFunction(x0));
    }

    public int getN(){return n;}
    public void setN(int n){this.n = n;}

    public int getFunctionType(){return functionType;}
    public void setFunctionType(int f){functionType = f;}

    public ArrayList<ArrayList<Double>> getMTCNodes() {return MTCNodes;}

}
