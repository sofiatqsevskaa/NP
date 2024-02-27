package K2.T1;

import java.util.*;


class Participant{
    String city;
    String code;
    String name;
    int age;

    public Participant(String city, String code, String name, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return city.equals(that.city) && code.equals(that.code);
    }
    @Override
    public int hashCode() {
        return Objects.hash(city, code);
    }
    @Override
    public String toString() {
        return String.format("%s %s %d",code,name,age);
    }
}
class Audition{
    Set<Participant> participants;
    public Audition() {
        this.participants = new HashSet<>();
    }
    private final Comparator<Participant> nameAndAgeComparator=
            Comparator.comparing(Participant::getName).thenComparing(Participant::getAge);
    public void addParticpant(String city, String code, String name, int age) {
        participants.add(new Participant(city,code,name,age));
    }

    public void listByCity(String city) {
        participants.stream().sorted(nameAndAgeComparator).filter(i->i.getCity().equals(city)).forEach(System.out::println);
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}