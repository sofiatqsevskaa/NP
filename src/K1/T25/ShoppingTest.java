package K1.T25;



import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class InvalidOperationException extends Exception{
    public InvalidOperationException(String message) {
        super(message);
    }
}

class Item implements Comparable<Item>{
    protected int id;
    protected String name;
    protected String type;
    protected double price;
    protected double quantity;
    public Item(int id, String name, String type, double price, double quantity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
    }
    public Double getTotalPrice() {
        if(type.equals("WS")) {
            return quantity * price;
        }
        return (quantity / 1000.0) * price;
    }
    @Override
    public int compareTo(Item o) {
        return o.getTotalPrice().compareTo(getTotalPrice());
    }
    public double getSalePrice(){
        return (double) getTotalPrice()*0.1;
    }
    public int getId() {
        return id;
    }
    public String toString() {
        return String.format("%d - %.2f", id, getTotalPrice());
    }
}

class ShoppingCart{
    protected List<Item> items;
    public ShoppingCart(List<Item> items) {
        this.items = items;
    }
    public ShoppingCart() {
        this.items=new ArrayList<>();
    }
    public void addItem(String itemData) throws InvalidOperationException {
        String[] parts= itemData.split(";");
        int id=Integer.parseInt(parts[1]);
        String name=parts[2];
        double price=Double.parseDouble(parts[3]);
        double quantity=Double.parseDouble(parts[4]);
        String type=parts[0];
        if(quantity == 0) {
            throw new InvalidOperationException(String.format("The quantity of the product with id %d can not be 0.",id));
        }
        items.add(new Item(id,name,type,price,quantity));
    }
    public void printShoppingCart(OutputStream os) {
        sortShoppingCart();
        PrintWriter pw= new PrintWriter(os);
        for (Item i:items) pw.println(i.toString());
        pw.flush();
    }
    private void sortShoppingCart() {
        items=items.stream().sorted(Item::compareTo).collect(Collectors.toList());
    }
    void blackFridayOffer(List<Integer> discountItems, OutputStream os) throws InvalidOperationException {
        if(discountItems.isEmpty()) {
            throw new InvalidOperationException("There are no products with discount.");
        }
        PrintWriter pw = new PrintWriter(os);
        List<Item> saleItems=items.stream().filter(i->discountItems.contains(i.getId())).collect(Collectors.toList());
        for (Item i:saleItems) {
            pw.println(String.format("%s - %.2f", i.getId(), i.getSalePrice()));
        }
        pw.flush();
    }
}

public class ShoppingTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        int items = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < items; i++) {
            try {
                cart.addItem(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Integer> discountItems = new ArrayList<>();
        int discountItemsCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < discountItemsCount; i++) {
            discountItems.add(Integer.parseInt(sc.nextLine()));
        }

        int testCase = Integer.parseInt(sc.nextLine());
        if (testCase == 1) {
            cart.printShoppingCart(System.out);
        } else if (testCase == 2) {
            try {
                cart.blackFridayOffer(discountItems, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}