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
        double minValue = findMinValue(x0, x);
        for(int i = 0; i < n; i++)
        {
            ArrayList<Double> insertionArray = new ArrayList<>();

            double xVal = x0 + ((x - x0) * randomGenerator.nextDouble());
            double yVal = minValue +(maxValue - minValue) * randomGenerator.nextDouble();

            if ((yVal <= function(xVal) && function(xVal) > 0 && yVal > 0) || (yVal >= function(xVal)  && function(xVal) < 0 && yVal < 0) && (xVal <= x && x >= x0))
            {hitCounter++;}

            insertionArray.add(xVal);
            insertionArray.add(yVal);
            MTCNodes.add(insertionArray);

        }

    }

    private double findMinValue(double x0, double x){
        if(functionType == 3 || functionType == 4){
            return -1;
        }
        if(functionType == 1){return 0;}
        return function(x0);

    }

    public double findAbsoluteMaxValue(double x0, double x){
        if(functionType == 3 || functionType == 4){
            return 1;
        }

        if(functionType == 1){
            double intervalBoarder1 = function(x0);
            double intervalBorder2 = function(1);

            if(intervalBoarder1 > intervalBorder2)
            {
                return intervalBoarder1;
            }
            return intervalBorder2;

        }

        if (x > x0)
        {
            if(function(x) < 0)
            {
                return Math.abs(function(x));
            }
            return function(x);}
        if(function(x) < 0){
        return Math.abs(function(x0));}
        return function(x0);
    }


    public double calculateIntegral(double x0, double x){
        ArrayList<Double> zeroPoints = zeroPoints(x0,x);
        generateMTCNodes(x0, x);
        double minValue = findMinValue(x0, x);
        if(minValue < 0)
        {
            minValue *= -1;
        }

        double K = (double)hitCounter / (double)n;
        double AREA = (minValue +  maxValue) * (x - x0);
        area = AREA * K;
        return area;
    }


    public int getHitCounter(){return hitCounter;}


}
