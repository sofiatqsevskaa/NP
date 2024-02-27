package K1.T11;

//package generic11;
import java.util.Scanner;


class ZeroDenominatorException extends Exception{
    public ZeroDenominatorException(String message) {
        super(message);
    }
}

class GenericFraction<T extends Number,U extends Number>{
    T numerator;
    U denominator;

    public double getNumerator() {
        return numerator.doubleValue();
    }

    public double getDenominator() {
        return denominator.doubleValue();
    }

    GenericFraction (T numerator, U denominator) throws ZeroDenominatorException {
        if(denominator.doubleValue()==0) throw new ZeroDenominatorException("Denominator cannot be zero");
        this.numerator=numerator;
        this.denominator=denominator;
    }
    GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException {
        double n = (gf.getNumerator() * this.getDenominator()) + (this.getNumerator() * gf.getDenominator());
        double d=  this.getDenominator() * gf.getDenominator();
        return new GenericFraction<>(n,d);
    }

    double toDouble() {
        return getNumerator() / getDenominator();
    }

    @Override
    public String toString() {
        double n=getNumerator();
        double d=getDenominator();
        for (int i = (int)getNumerator()/2+1; i >0; i--) {
            if(n%i==0&&d%i==0){
                n/=i;
                d/=i;
            }
        }
        return String.format("%.2f / %.2f",n,d);
    }
}

public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}
