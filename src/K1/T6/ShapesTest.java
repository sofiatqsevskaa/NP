package K1.T6;
import java.util.ArrayList;
import java.util.Scanner;

enum Color {
    RED, GREEN, BLUE
}

interface Scalable {
    void scale(float scaleFactor);
}

interface Stackable {
    float weight();
}

class Shape implements Scalable, Stackable {
    String id;
    Color color;

    public Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    @Override
    public void scale(float scaleFactor) {

    }

    @Override
    public float weight() {
        return 0;
    }
}

class Circle extends Shape implements Scalable, Stackable {
    float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        radius *= scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (radius * radius * Math.PI);
    }
    public String toString() {
        String [] colors = {"RED", "GREEN", "BLUE"};
        return String.format("C: %-5s%-10s%10.2f\n", id, colors[color.ordinal()], weight());
    }
}

class Rectangle extends Shape implements Scalable, Stackable {
    float width;
    float height;

    public Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void scale(float scaleFactor) {
        width *= scaleFactor;
        height *= scaleFactor;
    }

    @Override
    public float weight() {
        return width * height;
    }

    public String toString() {
        String [] colors = {"RED", "GREEN", "BLUE"};
        return String.format("R: %-5s%-10s%10.2f\n", id, colors[color.ordinal()], weight());
    }
}
class Canvas {
    ArrayList<Shape> shapes;

    Canvas() {
        shapes = new ArrayList<>();
    }

    void add(String id, Color color, float radius) {
        Circle circle = new Circle(id, color, radius);
        if(shapes.isEmpty()) {
            shapes.add(0, circle);
            return;
        }
        for(int i = 0; i < shapes.size(); i++) {
            if(circle.weight() > shapes.get(i).weight()) {
                shapes.add(i, circle);
                return;
            }
        }
        shapes.add(circle);
    }

    void add(String id, Color color, float width, float height) {
        Rectangle rectangle = new Rectangle(id, color, width, height);
        if(shapes.isEmpty()) {
            shapes.add(0, rectangle);
            return;
        }
        for(int i = 0; i < shapes.size(); i++) {
            if(rectangle.weight() > shapes.get(i).weight()) {
                shapes.add(i, rectangle);
                return;
            }
        }
        shapes.add(rectangle);
    }
    void scale(String id, float scaleFactor) {
        int index = 0;
        for(int i = 0; i < shapes.size(); i++) {
            if(shapes.get(i).getId().equals(id)) {
                shapes.get(i).scale(scaleFactor);
                index = i;
                break;
            }
        }
        int newIndex = 0;
        for(int i = 0; i < shapes.size(); i++) {
            if(shapes.get(i).weight() < shapes.get(index).weight()) {
                break;
            }
            newIndex++;
        }
        if(newIndex == shapes.size()) {
            shapes.add(shapes.get(index));
            shapes.remove(index);
        }
        else if(newIndex <= index){
            shapes.add(newIndex, shapes.get(index));
            shapes.remove(index+1);
        }
        else {
            shapes.add(newIndex, shapes.get(index));
            shapes.remove(index);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Shape shape : shapes) {
            sb.append(shape);
        }
        return sb.toString();
    }
}



public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}