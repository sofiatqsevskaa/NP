package K2.T38;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class NumberException extends Exception{
    public NumberException() {
        super("A quiz must have same number of correct and selected answers");
    }
}

class Student {
    String id;
    List<String> correctAnswers;
    List<String> studentsAnswers;

    public Student(String id, List<String> correctAnswers, List<String> studentsAnswers) throws NumberException{
        if(correctAnswers.size()!=studentsAnswers.size()) throw new NumberException();
        this.id = id;
        this.correctAnswers = correctAnswers;
        this.studentsAnswers = studentsAnswers;
    }

    public double getPoints() {
        return IntStream.range(0,studentsAnswers.size()).mapToDouble(index->{
            if(studentsAnswers.get(index).equals(correctAnswers.get(index))) return 1.0;
            else return -0.25;
        }).sum();
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", correctAnswers=" + correctAnswers +
                ", studentsAnswers=" + studentsAnswers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

class QuizProcessor {
    static Map<String, Double> processAnswers(InputStream is) {
        List<Student> students= new ArrayList<>();
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        List<String> lines=br.lines().collect(Collectors.toList());
        lines.stream().forEach(line->{
            String id=line.split(";")[0];
            List<String> correctAnswers= Arrays.stream(line.split(";")[1].split(",")).map(String::trim).collect(Collectors.toList());
            List<String> studentsAnswers= Arrays.stream(line.split(";")[2].split(",")).map(String::trim).collect(Collectors.toList());
            try{
                students.add(new Student(id,correctAnswers,studentsAnswers));
            } catch (NumberException e) {
                System.out.println(e.getMessage());
            }
        });
        Map<String, Double> map=new TreeMap<>();
        students.stream().forEach(student->{
            map.put(student.getId(),student.getPoints());
        });
        return map;
    }
}

public class QuizProcessorTest {
    public static void main(String[] args) {
        QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
    }
}