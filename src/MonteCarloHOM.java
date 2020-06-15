import java.util.ArrayList;
import java.util.Random;

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
        maxValue = findMaxValue(x);

        for(int i = 0; i < n; i++)
        {
            ArrayList<Double> insertionArray = new ArrayList<>();

            double xVal = x0 + (x - x0) * randomGenerator.nextDouble();
            double yVal = maxValue * randomGenerator.nextDouble();

            if (yVal <= function(xVal)){hitCounter++;}

            insertionArray.add(xVal);
            insertionArray.add(yVal);
            MTCNodes.add(insertionArray);

        }

    }

    public double findMaxValue(double x){
        if(functionType == 3 || functionType == 4){
            return 1;
        }

        return x;
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
