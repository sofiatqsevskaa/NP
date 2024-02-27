package K2.T31;
import java.util.*;
import java.util.stream.Collectors;


class Student {
    String index;
    String name;
    int firstMidtermPoints;
    int secondMidtermPoints;
    int labPoints;

    public Student(String index, String name) {
        this.index = index;
        this.name = name;
        this.firstMidtermPoints=0;
        this.secondMidtermPoints=0;
        this.labPoints=0;
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public int getFirstMidtermPoints() {
        return firstMidtermPoints;
    }

    public int getSecondMidtermPoints() {
        return secondMidtermPoints;
    }

    public int getLabPoints() {
        return labPoints;
    }

    public void setFirstMidtermPoints(int firstMidtermPoints) {
        this.firstMidtermPoints = firstMidtermPoints;
    }

    public void setSecondMidtermPoints(int secondMidtermPoints) {
        this.secondMidtermPoints = secondMidtermPoints;
    }

    public void setLabPoints(int labPoints) {
        this.labPoints = labPoints;
    }
    public double getTotal() {
        return ((double) firstMidtermPoints*0.45)+((double) secondMidtermPoints*0.45)+labPoints;
    }
    public int getGrade() {
        if(getTotal()<50) return 5;
        return (int) ((getTotal()/10) +1);
    }

    @Override
    public String toString() {
        return String.format("ID: %s " +
                "Name: %s " +
                "First midterm: %d " +
                "Second midterm %d " +
                "Labs: %d " +
                "Summary points: %.2f " +
                "Grade: %d",index,name,firstMidtermPoints,secondMidtermPoints,labPoints,getTotal(),getGrade());
    }
}

class AdvancedProgrammingCourse {
    Map<String,Student> students;

    public AdvancedProgrammingCourse() {
        this.students = new HashMap<>();
    }

    public void addStudent (Student s) {
        students.put(s.getIndex(),s);
    }
    public void updateStudent (String idNumber, String activity, int points) {
        Student s=students.get(idNumber);
        if(activity.contains("1")) s.setFirstMidtermPoints(points);
        else if(activity.contains("2")) s.setSecondMidtermPoints(points);
        else if(activity.equals("labs")) s.setLabPoints(points);
    }
    public List<Student> getFirstNStudents (int n) {
        return students.values().stream()
                .sorted(Comparator.comparing(Student::getTotal).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }
    public Map<Integer,Integer> getGradeDistribution() {
        Map<Integer,Integer> grades = new TreeMap<>();
        grades.putIfAbsent(5,0);
        grades.putIfAbsent(6,0);
        grades.putIfAbsent(7,0);
        grades.putIfAbsent(8,0);
        grades.putIfAbsent(9,0);
        grades.putIfAbsent(10,0);
        students.values().stream().forEach(student->{
            grades.compute(student.getGrade(),(k,v)-> v+1);
        });
        return grades;
    }
    public void printStatistics() {
        DoubleSummaryStatistics s = students.values().stream()
                .mapToDouble(Student::getTotal)
                .filter(i->i>50)
                .summaryStatistics();
        System.out.println(String.format("Count: %d Min: %.2f Average: %.2f Max: %.2f",
                s.getCount(),s.getMin(),s.getAverage(),s.getMax()));
    }
}


public class CourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                advancedProgrammingCourse.updateStudent(idNumber, activity, points);
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}
