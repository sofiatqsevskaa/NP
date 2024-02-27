package K1.T14;

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

class Receipt{
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
            pw.println(String.format("%s %d %.2f",r.getId(),(int)r.getTotalPrice(),r.getTotalTaxReturn()));
        }
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

    }
}