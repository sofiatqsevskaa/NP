package K1.T16;

import java.io.*;
import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;

class AmountNotAllowedException extends Exception{
    public AmountNotAllowedException(String message) {
        super(message);
    }
}

class Item{
    private int price;
    private String taxType;
    public Item(int price, String taxType) {
        this.price = price;
        this.taxType = taxType;
    }
    public int getPrice() {
        return price;
    }
    public double getTax() {
        if(taxType.equals("A")) return 0.18*price;
        else if(taxType.equals("B")) return 0.05*price;
        else return 0;
    }
    public double getDDV(){
        return getTax()*0.15;
    }
}

class Receipt implements Comparable<Receipt>{
    private List<Item> items;
    private long id;
    public Receipt(List<Item> items, long id) {
        this.items = items;
        this.id = id;
    }
    public Receipt(long id) {
        this.id = id;
        this.items=new ArrayList<>();
    }
    public int getTotalPrice(){
        return items.stream().mapToInt(i->i.getPrice()).sum();
    }
    public double getTotalTaxReturn(){
        return items.stream().mapToDouble(i->i.getDDV()).sum();
    }
    public static boolean checkTotal(int total){
        return total>30000;
    }
    public long getId() {
        return id;
    }
    public Receipt (String s) throws AmountNotAllowedException {
        String[] strings=s.split("\\s+");
        this.id=Long.parseLong(strings[0]);
        int sum=0;
        for (int i = 1; i < strings.length; i+=2) {
            sum+=Integer.parseInt(strings[i]);
        }
        if(checkTotal(sum)) {
            throw new AmountNotAllowedException("Receipt with amount "+sum+ " is not allowed to be scanned");
        }
        this.items=new ArrayList<>();
        for (int i = 1; i < strings.length-1; i+=2) {
            this.items.add(new Item(Integer.parseInt(strings[i]),strings[i+1]));
        }
    }

    @Override
    public int compareTo(Receipt o) {
        return Comparator.comparing(Receipt::getTotalTaxReturn).thenComparing(Receipt::getTotalTaxReturn).compare(this,o);
    }
}

class MojDDV{
    private List<Receipt> receipts;
    public MojDDV() {
        this.receipts = new ArrayList<>();
    }
    public void readRecords (InputStream in) {
        BufferedReader br= new BufferedReader(new InputStreamReader (in));
        br.lines().forEach(i->{
            try {
                receipts.add(new Receipt(i));
            }catch (AmountNotAllowedException e){
                System.out.println(e.getMessage());
                return;
            }
        });
    }

    //ID SUM_OF_AMOUNTS TAX_RETURN
    void printTaxReturns (OutputStream os){
        PrintWriter pw=new PrintWriter(os);
        for(Receipt r:receipts) {
            pw.println(String.format("%10s\t %9d\t %9.5f",r.getId(),(int)r.getTotalPrice(),r.getTotalTaxReturn()));
        }
        pw.flush();
    }
    void printStatistics(OutputStream os){
        DoubleSummaryStatistics dss= receipts.stream()
                .mapToDouble(Receipt::getTotalTaxReturn)
                .summaryStatistics();
        PrintWriter pw=new PrintWriter(os);
        pw.println(String.format("min:\t%.3f\nmax:\t%.3f\nsum:\t%.3f\ncount:\t%d\navg:\t%.3f",
                dss.getMin(), dss.getMax(), dss.getSum(), dss.getCount(), dss.getAverage()));
        pw.flush();
    }
}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);
    }
}
