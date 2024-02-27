package K1.T9;

import java.util.*;

class Triple<T>{
    protected double[] nums;
    public Triple(double num1, double num2, double num3) {
        nums= new double[3];
        nums[0]=num1;
        nums[1]=num2;
        nums[2]=num3;
    }
    public double max(){
        if(nums[0]>=nums[1]&&nums[0]>=nums[2])
        {
            return nums[0];
        }
        else if(nums[1]>=nums[0]&&nums[1]>=nums[2])
        {
            return nums[1];
        }
        else
        {
            return nums[2];
        }
    }
    public double avarage()
    {
        return (nums[0]+nums[1]+nums[2])/3;
    }
    public void sort()
    {
        nums= Arrays.stream(nums).sorted().toArray();
    }

    @Override
    public String toString() {
        return String.format("%2.2f %2.2f %2.2f",nums[0],nums[1],nums[2]);
    }
}

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.avarage());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.avarage());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.avarage());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde
// class Triple


