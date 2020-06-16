import java.util.ArrayList;

public class MonteCarloHOM extends MonteCarlo
{
    private int hitCounter = 0;
    private double maxValue = 0;

    public MonteCarloHOM(int n, int functionType)
    {
        super(n, functionType);
        this.n = n;
        this.functionType = functionType;

    }

    protected void generateMTCNodes(double x0, double x){
        maxValue = findAbsoluteMaxValue(x0, x);
        minValue = findMinValue(x0, x);
        for(int i = 0; i < n; i++)
        {
            ArrayList<Double> insertionArray = new ArrayList<>();
            boolean negative = randomGenerator.nextBoolean();
            double xVal = x0 + (x - x0) * randomGenerator.nextDouble();
            double yVal = maxValue * randomGenerator.nextDouble();
            if(negative){yVal *= -1;}

            if (yVal <= function(xVal)){hitCounter++;}

            insertionArray.add(xVal);
            insertionArray.add(yVal);
            MTCNodes.add(insertionArray);

        }

    }

    private double findMinValue(double x0, double x){
        if(functionType == 3 || functionType == 4){
            return -1;
        }
        return function(x0);

    }

    public double findAbsoluteMaxValue(double x0, double x){
        if(functionType == 3 || functionType == 4){
            return 1;
        }
        double borderX0 = Math.abs(function(x0));
        double borderX = Math.abs(function(x));
        if (borderX > borderX0){return function(x);}
        return function(x0);
    }


    public double calculateIntegral(double x0, double x){
        generateMTCNodes(x0, x);
        double K = (double)hitCounter / (double)n;
        double AREA = maxValue * (x - x0);
        area = AREA * K;
        return area;
    }


    public int getHitCounter(){return hitCounter;}


}
