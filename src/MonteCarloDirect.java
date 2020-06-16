import java.util.ArrayList;
import java.util.Random;

public class MonteCarloDirect extends MonteCarlo
{



    public MonteCarloDirect(int n, int functionType)
    {
        super(n, functionType);
        this.n = n;
        this.functionType = functionType;

    }

    protected void generateMTCNodes(double x0, double x){
        for(int i = 0; i < n; i++)
        {
            boolean negative = randomGenerator.nextBoolean();
            ArrayList<Double> insertionArray = new ArrayList<>();
            double xVal = x0 + (x - x0) * randomGenerator.nextDouble();
            if(negative && -xVal >= x0){xVal *= -1;}
            double yVal = function(xVal);
            insertionArray.add(xVal);
            insertionArray.add(yVal);
            MTCNodes.add(insertionArray);
        }

    }


    public double calculateIntegral(double x0, double x){
        double averageFunctionValue = 0;
        generateMTCNodes(x0, x);

        for(ArrayList<Double> Node: MTCNodes)
        {
                averageFunctionValue += Node.get(1);
        }
        averageFunctionValue = averageFunctionValue / n;
        area = (x - x0) * averageFunctionValue;

        return area;
    }


}
