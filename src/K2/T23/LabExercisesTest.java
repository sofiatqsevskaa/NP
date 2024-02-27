package K2.T23;

import java.util.*;
import java.util.stream.Collectors;

class Student {
    private final String index;
    private List<Integer> labPoints;

    public Student(String index, List<Integer> labPoints) {
        this.index = index;
        this.labPoints = labPoints;
    }

    public String getIndex() {
        return index;
    }

    public List<Integer> getLabPoints() {
        return labPoints;
    }
    public double getPoints() {
        return labPoints.stream().mapToDouble(i->(double) i).sum()/10.0;
    }

    @Override
    public String toString() {
        if(passed()) return String.format("%s YES %.2f",index,getPoints());
        else return String.format("%s NO %.2f",index,getPoints());
    }
    public boolean passed() {
        return labPoints.size() >= 8;
    }
}

class LabExercises {
    Comparator<Student> ascendingOrder = Comparator.comparing(Student::getPoints).thenComparing(Student::getIndex);
    Comparator<Student> descendingOrder = Comparator.comparing(Student::getPoints).thenComparing(Student::getIndex).reversed();
    List<Student> students;

    public LabExercises() {
        this.students = new ArrayList<>();
    }

    public void addStudent (Student student){
        students.add(student);
    }
    public void printByAveragePoints (boolean ascending, int n) {
        students.stream()
                .sorted(ascending?ascendingOrder:descendingOrder)
                .limit(n)
                .forEach(System.out::println);
    }
    public List<Student> failedStudents () {
        return students.stream().filter(s->!s.passed())
                .sorted(Comparator.comparing(Student::getIndex).thenComparing(Student::getPoints))
                .collect(Collectors.toList());
    }
    public Map<Integer,Double> getStatisticsByYear(){
        Map<Integer,Double> map=new TreeMap<>();
        students.stream()
                .filter(Student::passed)
                .forEach(s->{
                    int year=20-Integer.parseInt(s.getIndex().substring(0,2));
                    map.putIfAbsent(year,students.stream().filter(st->st.getIndex().startsWith(s.getIndex().substring(0,2))).filter(Student::passed).mapToDouble(Student::getPoints).average().orElse(0));
                });
        return map;
    }
}



public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}

