package K1.T1;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Canvas {
    private String canvasId;
    private List<Integer> sizes;

    Canvas() {
        canvasId = "";
        sizes = new ArrayList<>();
    }

    Canvas(String canvasId, ArrayList<Integer> sizes) {
        this.canvasId = canvasId;
        this.sizes = sizes;
    }

    public int getSize() {
        return sizes.size();
    }

    public int getPerimeter() {
        int s = 0;
        for(int size : sizes) { s += size; }
        return s * 4;
    }

    @Override
    public String toString() {
        return canvasId + " " + getSize() + " " + getPerimeter();
    }
}

class ShapesApplication {
    private ArrayList<Canvas> list;

    ShapesApplication() {
        list = new ArrayList<>();
    }

    public int readCanvases(InputStream inputStream) {
        BufferedReader br=new BufferedReader(new InputStreamReader (inputStream));
        int ct=0;
        List<String> lines=br.lines().collect(Collectors.toList());
        for (int i = 0; i < lines.size(); i++) {
            String[] parts=lines.get(i).split("\\s+");
            String id=parts[0];
            ArrayList<Integer> sizes=new ArrayList<>();
            for (int j = 1; j < parts.length; j++) {
                sizes.add(Integer.parseInt(parts[j]));
            }
            list.add(new Canvas(id,sizes));
            ct+=sizes.size();
        }
        return ct;
    }

    public void printLargestCanvasTo(OutputStream outputStream) {
        int idx = -1;

        for(int i = 0; i < list.size(); i++) {
            if(idx == -1 || list.get(i).getPerimeter() > list.get(idx).getPerimeter()) {
                idx = i;
            }
        }

        if(idx == -1) { return; }

        PrintWriter print = new PrintWriter(outputStream);
        print.println(list.get(idx));
        print.flush();
    }
}

public class Shapes1Test {
    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
