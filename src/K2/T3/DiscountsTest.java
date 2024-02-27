package K2.T3;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

class Product{
    int discountPrice;
    int price;

    public Product(int discountPrice, int price) {
        this.discountPrice = discountPrice;
        this.price = price;
    }
    int getDiscountPercent(){
        return getDiscount()*100/price;
    }
    int getDiscount(){
        return price-discountPrice;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public int getPrice() {
        return price;
    }
}

class Store {
    String name;
    List<Product> products;

    public Store(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    double getAverageDiscount(){
        return (double) products.stream().mapToInt(Product::getDiscountPercent).sum() /products.size();
    }

    int getDiscount(){
        return products.stream().mapToInt(Product::getDiscount).sum();
    }

    public String toString() {
        products=products.stream().sorted(Comparator.comparing(Product::getDiscountPercent).thenComparing(Product::getDiscountPrice).reversed()).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append("Average discount: ").append(String.format("%.1f", getAverageDiscount())).append("%").append("\n");
        sb.append("Total discount: ").append(getDiscount());
        products.forEach(product -> {
            sb.append("\n").append(String.format("%2d", product.getDiscountPercent())).append("% ");
            sb.append(product.getDiscountPrice()).append("/").append(product.getPrice());
        });
        return sb.toString();
    }
}

class Discounts{
    List<Store> stores;

    public Discounts() {
        this.stores = new ArrayList<>();
    }

    public int readStores(InputStream in) {
        BufferedReader br=new BufferedReader(new InputStreamReader(in));
        br.lines().forEach(string->{
            String[] t=string.split("\\s+");
            String name=t[0];
            List<Product> products=new ArrayList<>();
            for (int i = 1; i < t.length; i++) {
                products.add(new Product(Integer.parseInt(t[i].split(":")[0]),Integer.parseInt(t[i].split(":")[1])));
            }
            stores.add(new Store(name,products));
        });
        return stores.size();
    }

    public List<Store> byAverageDiscount() {
        stores=stores.stream().sorted(Comparator.comparing(Store::getAverageDiscount).reversed().thenComparing(Store::getName)).collect(Collectors.toList());
        List<Store> first3=new ArrayList<>();
        IntStream.range(0,3).forEach(i->{
            first3.add(stores.get(i));
        });
        return first3;
    }

    public List<Store> byTotalDiscount() {
        stores=stores.stream().sorted(Comparator.comparing(Store::getDiscount).thenComparing(Store::getName)).collect(Collectors.toList());
        List<Store> first3=new ArrayList<>();
        IntStream.range(0,3).forEach(i->{
            first3.add(stores.get(i));
        });
        return first3;
    }
}
