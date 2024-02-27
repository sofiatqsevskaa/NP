package K2.T6;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


abstract class Employee{
    String id;
    String level;

    public Employee(String id, String level) {
        this.id = id;
        this.level = level;
    }

    abstract double getPayment();

    public String getLevel() {
        return level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, level);
    }
}

class HourlyEmployee extends Employee{
    double hours;
    double rate;
    public HourlyEmployee(String id, String level, double hours, double rate) {
        super(id, level);
        this.hours = hours;
        this.rate=rate;
    }
    @Override
    double getPayment(){
        if(hours>40.0){
            return (40*rate)+((hours-40.0)*rate*1.5);
        }
        else return hours*rate;
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Regular hours: %.2f Overtime hours: %.2f",id,level,getPayment(),(hours>40)?40.00:hours,(hours<40)?0.00:hours-40.0);
    }
}

class FreelanceEmployee extends Employee{
    List<Integer> tickets;
    double rate;

    public FreelanceEmployee(String id, String level, List<Integer> tickets, double rate) {
        super(id, level);
        this.tickets = tickets;
        this.rate=rate;
    }

    @Override
    double getPayment() {
        return tickets.stream().mapToInt(i->i).sum()*rate;
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f Tickets count: %d Tickets points: %d",id,level,getPayment(),tickets.size(),tickets.stream().mapToInt(i->i).sum());
    }
}

class PayrollSystem {
    Map<String,Double> hourlyRateByLevel;
    Map<String,Double> ticketRateByLevel;
    List<Employee> employees;

    Comparator<Employee> paymentComparator= Comparator.comparing(Employee::getPayment).thenComparing(Employee::getLevel);

    PayrollSystem(Map<String,Double> hourlyRateByLevel, Map<String,Double> ticketRateByLevel){
        this.hourlyRateByLevel=hourlyRateByLevel;
        this.ticketRateByLevel=ticketRateByLevel;
        employees=new ArrayList<>();
    }

    public void readEmployees(InputStream in) {
        BufferedReader br=new BufferedReader(new InputStreamReader(in));
        List<String> lines=br.lines().collect(Collectors.toList());
        lines.stream().forEach(line->{
            String[] tmp=line.split(";");
            if(tmp[0].equals("H")){
                double rate=hourlyRateByLevel.get(tmp[2]);
                employees.add(new HourlyEmployee(tmp[1],tmp[2],Double.parseDouble(tmp[3]),rate));
            }
            else{
                List<Integer> tickets=new ArrayList<>();
                double rate=ticketRateByLevel.get(tmp[2]);
                for (int i = 3; i < tmp.length; i++) {
                    tickets.add(Integer.parseInt(tmp[i]));
                }
                employees.add(new FreelanceEmployee(tmp[1],tmp[2],tickets,rate));
            }
        });
    }

    public Map<String, Set<Employee>> printEmployeesByLevels(PrintStream out, Set<String> levels) {
        Map<String, Set<Employee>> map= new TreeMap<>();
        employees.stream().sorted(paymentComparator).forEach(employee -> {
            if(levels.contains(employee.level)){
                map.putIfAbsent(employee.getLevel(),new TreeSet<>(paymentComparator.reversed()));
                map.get(employee.getLevel()).add(employee);
            }
        });
        PrintWriter pw=new PrintWriter(out);
        map.forEach((level,employeeSet)->{
            pw.println("LEVEL: " + level);
            pw.println("Employees: ");
            employeeSet.forEach(employee -> pw.println(employee.toString()));
            pw.println("------------");
        });
        //pw.flush();
        return map;
    }
}

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i=5;i<=10;i++) {
            levels.add("level"+i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: "+ level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });


    }
}