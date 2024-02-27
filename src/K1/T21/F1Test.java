package K1.T21;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class Lap implements Comparable<Lap>{
    String lap;
    Lap(String lap)
    {
        this.lap = lap;
    }

    public String getLap() {
        return lap;
    }

    public int time(){
        String [] times = lap.split(":");
        return Integer.parseInt(times[2]) + Integer.parseInt(times[1])*1000 + Integer.parseInt(times[0])*60*1000;
    }

    @Override
    public int compareTo(Lap o) {
        return Integer.compare(this.time(), o.time());
    }
}

class Competitor implements Comparable<Competitor>{
    String name;
    List<Lap> laps;

    public String getName() {
        return name;
    }

//    public List<Lap> getLaps() {
//        return laps;
//    }

    Competitor(String line)
    {
        String [] parts = line.split("\\s+");
        laps = new ArrayList<>();
        name = parts[0];
        laps.add(new Lap(parts[1]));
        laps.add(new Lap(parts[2]));
        laps.add(new Lap(parts[3]));
    }

    public Lap getFastestLap() {
        return laps.stream().min(Comparator.naturalOrder()).orElse(new Lap("0:0:0"));
    }


//    public void sort(){
//        this.laps = laps.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
//    }

    @Override
    public int compareTo(Competitor o) {
//        this.sort();
//        o.sort();
        return Integer.compare(this.getFastestLap().time(), o.getFastestLap().time());
    }
}

class F1Race {
    // vashiot kod ovde
    List<Competitor> competitors;
    F1Race(){
        this.competitors = new ArrayList<>();
    }
    public void readResults(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        this.competitors = br.lines().map(Competitor::new).collect(Collectors.toList());

    }

    public void printSorted(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);
        List<Competitor> sorted = competitors.stream().sorted().collect(Collectors.toList());
        for (int i = 0; i < competitors.size(); i++) {
            pw.println(String.format("%d. %-10s%10s",i+1, sorted.get(i).getName(), sorted.get(i).getFastestLap().getLap()));
        }
        pw.flush();
    }

}
