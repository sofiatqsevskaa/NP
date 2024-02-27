package K2.T9;
import java.util.*;
import java.util.Scanner;


class SeatNotAllowedException extends Exception {
    public SeatNotAllowedException(String message) {
        super(message);
    }
}

class SeatTakenException extends Exception {
    public SeatTakenException(String message) {
        super(message);
    }
}

class Sector{
    String code;
    int numSeats;
    Map<Integer,Integer> seatsTaken;

    public Sector(String code, int numSeats) {
        this.code = code;
        this.numSeats = numSeats;
        seatsTaken=new HashMap<>();
    }

    public String getCode() {
        return code;
    }

    public int getNumSeats() {
        return numSeats;
    }

    public Map<Integer, Integer> getSeatsTaken() {
        return seatsTaken;
    }

    public void add(int seat, int type) {
        seatsTaken.put(seat,type);
    }
    public boolean isTaken(int seat){
        return seatsTaken.containsKey(seat);
    }
    public int availableSeats(){
        return numSeats-seatsTaken.size();
    }

    @Override
    public String toString() {
        return String.format("%s	%d/%d	%.1f%%",code,numSeats-seatsTaken.size(),numSeats,(seatsTaken.size())*100.0/numSeats);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sector)) return false;
        Sector sector = (Sector) o;
        return Objects.equals(code, sector.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, numSeats, seatsTaken);
    }
}

class Stadium {
    Comparator<Sector> comparator= Comparator.comparing(Sector::availableSeats).reversed().thenComparing(Sector::getCode);
    String name;
    Map<String,Sector> sectors;
    public Stadium(String name) {
        this.name=name;
        sectors=new HashMap<>();
    }
    public void createSectors(String[] sectorNames, int[] sectorSizes) {
        for (int i = 0; i < sectorNames.length; i++) {
            sectors.putIfAbsent(sectorNames[i],new Sector(sectorNames[i],sectorSizes[i]));
        }
    }
    public void buyTicket(String sectorName, int seat, int type) throws SeatNotAllowedException, SeatTakenException{
        int temp=-1;
        if(type==2)  temp=1;
        else if(type==1) temp=2;
        if(sectors.get(sectorName).isTaken(seat)) throw new SeatTakenException("SeatTakenException");
        if(sectors.get(sectorName).getSeatsTaken().containsValue(temp)) throw new SeatNotAllowedException("SeatNotAllowedException");
        sectors.get(sectorName).add(seat,type);
    }

    public void showSectors() {
        sectors.values().stream().sorted(comparator).forEach(System.out::println);
    }
}

public class StadiumTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}
