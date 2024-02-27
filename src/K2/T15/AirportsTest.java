package K2.T15;

//package zad15;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

class Flight {
    String from;
    String to;
    LocalTime takeoff;
    LocalTime land;
    String durationString;
    int duration;
    int time;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time=time;
        this.takeoff = LocalTime.of(0, 0, 0);
        this.takeoff=this.takeoff.plusMinutes(time);
        land=takeoff.plusMinutes(duration);
        this.duration = duration;
        int days=(time+duration)/(24*60);
        if (days==0){
            if(duration%60>=10) durationString=String.format("%dh%dm",duration/60,duration%60);
            else durationString=String.format("%dh0%dm",duration/60,duration%60);
        }
        else{
            if(duration%60>=10) durationString=String.format("+%dd %dh%dm",days,duration/60,duration%60);
            else durationString=String.format("+%dd %dh0%dm",days,duration/60,duration%60);
        }
    }

    public int getTime() {
        return time;
    }

    public String getFrom() {
        return from;
    }
    public int getDuration( ){
        return duration;
    }

    public String getDurationString() {
        return durationString;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return String.format("%s-%s %s-%s %s",from,to,takeoff.toString(),land.toString(),durationString);
    }
}

class Airport {
    String name;
    String country;
    String code;
    int passengers;
    List<Flight> outbound;
    List<Flight> inbound;
    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        outbound=new ArrayList<>();
        inbound=new ArrayList<>();
    }

    public void addOutbound(Flight flight){
        outbound.add(flight);
    }
    public void addInbound(Flight flight){
        inbound.add(flight);
    }

    public List<Flight> getOutbound() {
        return outbound;
    }

    public List<Flight> getInbound() {
        return inbound;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCode() {
        return code;
    }

    public int getPassengers() {
        return passengers;
    }
}

class Airports {
    Comparator<Flight> comp=Comparator.comparing(Flight::getTime).thenComparing(Flight::getDuration);
    Comparator<Flight> comp2=Comparator.comparing(Flight::getTo).thenComparing(Flight::getTime);
    Map<String,Airport> airports;

    public Airports() {
        airports=new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers){
        airports.putIfAbsent(code,new Airport(name,country,code,passengers));
    }
    public void addFlights(String from, String to, int time, int duration){
        Flight f=new Flight(from,to,time,duration);
        if(airports.containsKey(from)&& airports.containsKey(to)){
            airports.get(from).addOutbound(f);
            airports.get(to).addInbound(f);
        }
    }
    public void showDirectFlightsFromTo(String from, String to){
        if(airports.containsKey(from)&&airports.get(from).outbound.stream().anyMatch(f->f.getTo().equals(to))){
            airports.get(from).getOutbound().stream().sorted(comp).forEach(flight -> {
                if(flight.getTo().equals(to)) System.out.println(flight);
            });
        }
        else System.out.println("No flights from "+from+" to "+to);
    }
    public void showFlightsFromAirport(String from){
        System.out.println(airports.get(from).getName()+" ("+airports.get(from).getCode()+")");
        System.out.println(airports.get(from).getCountry());
        System.out.println(airports.get(from).getPassengers());
        AtomicInteger counter= new AtomicInteger(1);
        if(airports.containsKey(from)){
            airports.get(from).getOutbound().stream().sorted(comp2).forEach(flight->{
                System.out.println(counter+". "+flight);
                counter.incrementAndGet();
            });
        }
        else System.out.println("No flights from "+from);
    }
    public void showDirectFlightsTo(String to){
        if(airports.containsKey(to)) {
            airports.get(to).getInbound().stream().sorted(comp).forEach(System.out::println);
        }
        else System.out.println("No flights from "+to);
    }
}


public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}