package K2.T25;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(message);
    }
}


class Product {
    String category;
    String id;
    String name;
    LocalDateTime createdAt;
    double price;
    int numSales;
    public Product(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
        numSales=0;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void incrementSales(int n){
        numSales+=n;
    }

    public int getNumSales() {
        return numSales;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", quantitySold=" + numSales +
                '}';
    }
}


class OnlineShop {
    Comparator<Product> newestFirst = Comparator.comparing(Product::getCreatedAt).reversed();
    Comparator<Product> oldestFirst = Comparator.comparing(Product::getCreatedAt);
    Comparator<Product> highestPriceFirst = Comparator.comparing(Product::getPrice).reversed();
    Comparator<Product> lowestPriceFirst = Comparator.comparing(Product::getPrice);
    Comparator<Product> mostSoldFirst = Comparator.comparing(Product::getNumSales).reversed();
    Comparator<Product> leastSoldFirst = Comparator.comparing(Product::getNumSales);
    Map<String,Product> products;

    OnlineShop() {
        products=new HashMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price){
        products.put(id,new Product(category,id,name,createdAt,price));
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException{
        if(!products.containsKey(id))throw new ProductNotFoundException("Product with id "+id+" does not exist in the online shop!");
        products.get(id).incrementSales(quantity);
        return products.get(id).getPrice()*quantity;
    }


    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<List<Product>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        List<Product> prod=new ArrayList<>();
        if(comparatorType.equals(COMPARATOR_TYPE.NEWEST_FIRST)){
            prod=products.values().stream().sorted(newestFirst).collect(Collectors.toList());
        }
        else if(comparatorType.equals(COMPARATOR_TYPE.OLDEST_FIRST)){
            prod=products.values().stream().sorted(oldestFirst).collect(Collectors.toList());
        }
        else if(comparatorType.equals(COMPARATOR_TYPE.LOWEST_PRICE_FIRST)){
            prod=products.values().stream().sorted(lowestPriceFirst).collect(Collectors.toList());
        }
        else if(comparatorType.equals(COMPARATOR_TYPE.MOST_SOLD_FIRST)){
            prod=products.values().stream().sorted(mostSoldFirst).collect(Collectors.toList());
        }
        else if(comparatorType.equals(COMPARATOR_TYPE.LEAST_SOLD_FIRST)){
            prod=products.values().stream().sorted(leastSoldFirst).collect(Collectors.toList());
        }
        else prod=products.values().stream().sorted(highestPriceFirst).collect(Collectors.toList());

        if(category!=null) {
            int index=0;
            prod=prod.stream().filter(p->p.getCategory().equals(category)).collect(Collectors.toList());
            for (Product p : prod) {
                if(result.get(index).size()==pageSize){
                    result.add(new ArrayList<>());
                    index++;
                }
                if(p.getCategory().equals(category)) result.get(index).add(p);
            }
        }

        else{
            int index=0;
            for (Product p : prod) {
                if(result.get(index).size()==pageSize){
                    result.add(new ArrayList<>());
                    index++;
                }
                result.get(index).add(p);
            }
        }
        return result;
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category=null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}


