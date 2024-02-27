package K2.T35;

import java.io.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.*;

class Item{
    String name;
    int price;

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}

class Student{
    Comparator<Item> priceComparator = Comparator.comparing(Item::getPrice).reversed();
    String id;
    List<Item> items;
    void addItem(Item item) {
        items.add(item);
    }
    int getTotal(){
        return items.stream().mapToInt(Item::getPrice).sum();
    }
    int getFee() {
        if(Math.round(getTotal()*0.0114)<3) return 3;
        else if(Math.round(getTotal()*0.0114)>300) return 300;
        else return (int) Math.round(getTotal()*0.0114);
    }
    public Student(String id) {
        this.id = id;
        items=new ArrayList<>();
    }
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(String.format("Student: %s Net: %d Fee: %d Total: %d",id,getTotal(),getFee(),getFee()+getTotal()));
        sb.append("\n").append("Items:").append("\n");
        items.sort(priceComparator);
        for (int i = 0; i < items.size(); i++) {
            sb.append(String.format("%d. %s %d%n",i+1,items.get(i).getName(),items.get(i).getPrice()));
        }
        return sb.toString();
    }
}

class OnlinePayments{
    Map<String,Student> students;
    public OnlinePayments() {
        students=new HashMap<>();
    }
    public void readItems(InputStream in) {
        BufferedReader br=new BufferedReader(new InputStreamReader(in));
        List<String> lines=br.lines().collect(Collectors.toList());
        lines.stream().forEach(line->{
            String[] t=line.split(";");
            students.putIfAbsent(t[0],new Student(t[0]));
            students.get(t[0]).addItem(new Item(t[1],Integer.parseInt(t[2])));
        });
    }

    public void printStudentReport(String id, PrintStream out) {
        PrintWriter pw = new PrintWriter(out);
        Student student = students.get(id);
        if (student != null) {
            pw.append(student.toString());
        } else {
            pw.append("Student " + id + " not found!").append("\n");
        }
        pw.flush();
    }
}


public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
    }
}