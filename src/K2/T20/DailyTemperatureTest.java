package K2.T20;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * I partial exam 2016
 */
public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}

// Vashiot kod ovde

class DailyTemperatures {
    Map<Integer,DailyTemperature> dailyTemperatures;

    public DailyTemperatures() {
        this.dailyTemperatures = new HashMap<>();
    }

    void readTemperatures(InputStream inputStream) {
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        List<String> lines=br.lines().collect(Collectors.toList());
        for (String line: lines){
            String[] t= line.split("\\s+");
            int day=Integer.parseInt(t[0]);
            boolean celsius=true;
            List<Double> temperatures=new ArrayList<>();
            for (int i = 1; i < t.length; i++) {
                temperatures.add(Double.parseDouble(t[i].substring(0,t[i].length()-1)));
                if(t[i].charAt(t[i].length() - 1) == 'F') celsius=false;
            }
            DailyTemperature temp= new DailyTemperature(day,temperatures,celsius);
            dailyTemperatures.putIfAbsent(day,temp);
        }
    }
    void writeDailyStats(OutputStream outputStream, char scale) {
        PrintWriter pw=new PrintWriter(outputStream);
        if(Character.toLowerCase(scale)=='c') {
            dailyTemperatures.values()
                    .stream().sorted(Comparator.comparing(DailyTemperature::getDayOfTheYear))
                    .forEach(temp->{
                        pw.append(temp.celsius());
                    });
        }
        else {
            dailyTemperatures.values()
                    .stream().sorted(Comparator.comparing(DailyTemperature::getDayOfTheYear))
                    .forEach(temp->{
                        pw.append(temp.fahrenheit());
                    });
        }
        pw.flush();
    }
}

class DailyTemperature {
    int dayOfTheYear;
    List<Double> temperatures;
    boolean celsius;

    public DailyTemperature(int dayOfTheYear, List<Double> temperatures, boolean celsius) {
        this.dayOfTheYear = dayOfTheYear;
        this.temperatures = temperatures;
        this.celsius=celsius;

    }

    public int getDayOfTheYear() {
        return dayOfTheYear;
    }

    String celsius(){
        if(!celsius){
            temperatures=temperatures.stream().mapToDouble(i->((i-32)*5)/9).boxed().collect(Collectors.toList());
            celsius=true;
        }
        return String.format("%3d: Count: %3d Min: %6.2fC Max: %6.2fC Avg: %6.2fC\n",
                dayOfTheYear,temperatures.size(),
                temperatures.stream().mapToDouble(i->i).min().orElse(0),
                temperatures.stream().mapToDouble(i->i).max().orElse(0),
                temperatures.stream().mapToDouble(i->i).average().orElse(0));
    }
    String fahrenheit(){
        if(celsius){
            temperatures=temperatures.stream().mapToDouble(i->((i*9)/5)+32).boxed().collect(Collectors.toList());
            celsius=false;
        }
        return String.format("%3d: Count: %3d Min: %6.2fF Max: %6.2fF Avg: %6.2fF\n",
                dayOfTheYear,temperatures.size(),
                temperatures.stream().mapToDouble(i->i).min().orElse(0),
                temperatures.stream().mapToDouble(i->i).max().orElse(0),
                temperatures.stream().mapToDouble(i->i).average().orElse(0));
    }
}