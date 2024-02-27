package K2.T8;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class InvalidIDException extends Exception {
    public InvalidIDException(String message) {
        super("ID "+message+" is not valid");
    }
}

class InvalidDimensionException extends Exception {
    public InvalidDimensionException() {
        super("Dimension 0 is not allowed!");
    }
}

interface Shape {
    public double getArea();
    public double getPerimeter();
    public void scale(double freq);
    String getUser();
}

class Circle implements Shape {
    String user;
    double r;

    public Circle(double r, String id) {
        user=id;
        this.r = r;
    }

    @Override
    public double getArea() {
        return r*r*Math.PI;
    }

    @Override
    public double getPerimeter() {
        return 2*r*Math.PI;
    }

    @Override
    public void scale(double freq) {
        r*=freq;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return String.format("Circle -> Radius: %.2f Area: %.2f Perimeter: %.2f\n",r,getArea(),getPerimeter());
    }
}

class Square implements Shape {
    String user;
    double a;

    public Square(double a,String id) {
        this.a = a;
        user=id;
    }

    @Override
    public double getArea() {
        return a*a;
    }

    @Override
    public double getPerimeter() {
        return 4*a;
    }

    @Override
    public void scale(double freq) {
        a*=freq;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f\n",a,getArea(),getPerimeter());
    }
}

class Rectangle implements Shape {
    String user;
    double a;
    double b;

    public Rectangle(double a, double b,String id) {
        this.a = a;
        this.b = b;
        user=id;
    }

    @Override
    public double getArea() {
        return a*b;
    }

    @Override
    public double getPerimeter() {
        return (2*a)+(2*b);
    }

    @Override
    public void scale(double freq) {
        a*=freq;
        b*=freq;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return String.format("Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f\n",a,b,getArea(),getPerimeter());
    }
}

class User {
    String id;
    List<Shape> shapes;

    public User(String id) {
        this.id = id;
        shapes=new ArrayList<>();
    }

    public void addShape(Shape s) {
        shapes.add(s);
    }
    public void scaleUserShapes(double coef){
        shapes.stream().forEach(shape -> shape.scale(coef));
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("Shapes of user: ").append(id).append("\n");
        shapes.forEach(shape->sb.append(shape.toString()));
        return sb.toString();
    }
    public int getNumShapes(){
        return shapes.size();
    }
    public double getTotalArea(){
        return shapes.stream().mapToDouble(Shape::getArea).sum();
    }
    public void sortShapes(){
        shapes=shapes.stream().sorted(Comparator.comparing(Shape::getPerimeter)).collect(Collectors.toList());
    }
}

class Canvas {
    List<Shape> allShapes;
    Map<String,User> users;

    public Canvas() {
        allShapes=new ArrayList<>();
        users=new HashMap<>();
    }

    public void readShapes(InputStream in) throws InvalidIDException, InvalidDimensionException{
        BufferedReader br=new BufferedReader(new InputStreamReader(in));
        List<String> lines=br.lines().collect(Collectors.toList());
        for(String  line: lines){
            String[] t=line.split("\\s+");
            String id=t[1];
            if (containsSpecialCharacters(id)) {
                try {
                    throw new InvalidIDException(id);
                } catch (InvalidIDException e) {
                    System.out.println(e.getMessage());
                    continue;
                }
            }
            double a= Double.parseDouble(t[2]);
            if(a==0) try {
                throw new InvalidDimensionException();
            } catch (InvalidDimensionException e) {
                System.out.println(e.getMessage());
                break;
            }
            Shape shape;
            if(t[0].equals("1")){
                shape=new Circle(a,id);
                users.putIfAbsent(id,new User(id));
                users.get(id).addShape(shape);
                allShapes.add(shape);
            }
            else if(t[0].equals("2")){
                shape=new Square(a,id);
                users.putIfAbsent(id,new User(id));
                users.get(id).addShape(shape);
                allShapes.add(shape);
            }
            else if(t[0].equals("3")){
                double b=Double.parseDouble(t[3]);
                if(b==0) try {
                    throw new InvalidDimensionException();
                } catch (InvalidDimensionException e) {
                    System.out.println(e.getMessage());
                    break;
                }
                shape=new Rectangle(a,b,id);
                users.putIfAbsent(id,new User(id));
                users.get(id).addShape(shape);
                allShapes.add(shape);
            }
        }
        allShapes.sort(Comparator.comparing(Shape::getArea));
    }
    private boolean containsSpecialCharacters(String string){
        if(string.length()!=6) return true;
        String specialCharacters= ".()*&^%$#@!<>,./?';:|}]{[";
        for(char c: string.toCharArray()){
            if(specialCharacters.indexOf(c)!=-1) return true;
        }
        return false;
    }
    void scaleShapes(String userID, double coef) {
        if (users.containsKey(userID)) {


            allShapes.stream()
                    .filter(shape->shape.getUser().equals(userID))
                    .forEach(s->s.scale(coef));
        }
    }
    void printAllShapes (OutputStream os){
        PrintWriter pw=new PrintWriter(os);
        allShapes.stream().forEach(shape -> pw.append(shape.toString()));
        pw.flush();
    }
    void printByUserId (OutputStream os){
        List<User> sortedUsers= users.values().stream()
                .sorted(Comparator.comparing(User::getNumShapes).reversed().thenComparing(User::getTotalArea))
                .collect(Collectors.toList());
        sortedUsers.forEach(User::sortShapes);
        PrintWriter pw=new PrintWriter(os);
        sortedUsers.forEach(u->pw.append(u.toString()));
        pw.flush();
    }

    public void statistics(PrintStream out) {
        PrintWriter pw=new PrintWriter(out);
        DoubleSummaryStatistics s=allShapes.stream().collect(Collectors.summarizingDouble(Shape::getArea));
        pw.append(String.format("count: %d\n" +
                "sum: %.2f\n" +
                "min: %.2f\n" +
                "average: %.2f\n" +
                "max: %.2f",s.getCount(),s.getSum(),s.getMin(),s.getAverage(),s.getMax()));
        pw.flush();
    }
}

public class CanvasTest {

    public static void main(String[] args) {
        Canvas canvas = new Canvas();

        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
        try {
            canvas.readShapes(System.in);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println("BEFORE SCALING");
        canvas.printAllShapes(System.out);
        canvas.scaleShapes("123456", 1.5);
        System.out.println("AFTER SCALING");
        canvas.printAllShapes(System.out);

        System.out.println("PRINT BY USER ID TESTING");
        canvas.printByUserId(System.out);

        System.out.println("PRINT STATISTICS");
        canvas.statistics(System.out);
    }
}