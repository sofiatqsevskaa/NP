package K1.T5;

import java.util.Collection;
import java.util.Scanner;

class MinMax<T extends Comparable<T>>{
    private int minc=1;
    private int maxc=1;
    protected T min;
    protected T max;
    private int counter;
    public MinMax()
    {
        min=null;
        max=null;
        counter=0;
    }
    void update(T element) {
        if(counter==0) {
            min=element;
            max=element;
        }
        else {
            if(min.compareTo(element)==0)
            {
                minc++;
            }
            if(max.compareTo(element)==0)
            {
                maxc++;
            }
            if(min.compareTo(element)<0) {
                min=element;
                minc=1;
            }
            if(max.compareTo(element)>0) {
                max=element;
                maxc=1;
            }
        }
        counter++;
    }
    T max() {
        return max;
    }

    T min() {
        return min;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d\n", max(), min(), counter-maxc-minc);
    }

}

public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}