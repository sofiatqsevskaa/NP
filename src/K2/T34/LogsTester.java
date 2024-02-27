package K2.T34;

import java.util.*;
import java.util.stream.IntStream;

class Log {
    String type;
    String message;
    int timestamp;
    String microservice;
    public Log(String type, String message, int timestamp) {
        this.type = type;
        this.message = message;
        this.timestamp = timestamp;
        microservice=null;
    }
    void setMicroservice(String microservice){
        this.microservice=microservice;
    }
    int getSeverity(){
        if(type.equals("INFO")) return 0;
        else if(type.equals("WARN")){
            if(message.contains("might cause error") )return 2;
            else return 1;
        }
        else{
            int severity=3;
            if(message.contains("fatal")) severity+=2;
            if(message.contains("exception")) severity+=3;
            return severity;
        }
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}

class Microservice {
    String name;
    List<Log> logs;

    public Microservice(String name) {
        this.name = name;
        logs=new ArrayList<>();
    }
    void addLog(Log log){
        logs.add(log);
    }
    int getNumberOfLogs(){
        return logs.size();
    }
    int getSeverity(int i){
        return logs.get(i).getSeverity();
    }

    public List<Log> getLogs() {
        return logs;
    }

    @Override
    public String toString() {
        return "Microservice{" +
                "name='" + name + '\'' +
                ", logs=" + logs +
                '}';
    }
}

class Service {
    String name;
    Map<String, Microservice> microservices;

    public Service(String name) {
        this.name=name;
        this.microservices = new HashMap<>();
    }
    public void addMicroservice(String microserviceName){
        microservices.putIfAbsent(microserviceName,new Microservice(microserviceName));
    }

    public Map<String, Microservice> getMicroservices() {
        return microservices;
    }
    int getNumberOfMicroservices(){
        return microservices.size();
    }
    int getNumberOfLogs(){
        if (microservices.isEmpty()) return 0;
        return getAllLogs().size();
    }
    double getAverageNumberOfLogs(){
        if (microservices.isEmpty()) return 0;
        if (getSeverityOfLogs()==0) return 0;
        return (double)getNumberOfLogs()/microservices.size();
    }
    int getSeverityOfLogs() {
        return getAllLogs().stream().mapToInt(Log::getSeverity).sum();
    }
    double getAverageSeverityOfLogs(){
        if (microservices.isEmpty()) return 0;
        return (double) getSeverityOfLogs() /getAllLogs().size();
    }

    @Override
    public String toString() {
        return String.format("Service name: %s " +
                        "Count of microservices: %d " +
                        "Total logs in service: %d " +
                        "Average severity for all logs: %.2f " +
                        "Average number of logs per microservice: %.2f",
                name,
                getNumberOfMicroservices()
                ,getNumberOfLogs()
                ,getAverageSeverityOfLogs()
                ,getAverageNumberOfLogs());
    }
    List<Log> getAllLogs(){
        List<Log> logs=new ArrayList<>();
        microservices.values().forEach(m->{
            logs.addAll(m.getLogs());
        });
        return logs;
    }
}

class LogCollector {
    Map<String,Service> services;
    public LogCollector() {
        this.services = new HashMap<>();
    }

    void addLog (String string){
        String[] t=string.split("\\s+");
        String serviceName=t[0];
        String microserviceName=t[1];
        String type=t[2];
        StringBuilder sb=new StringBuilder();
        for (int i = 3; i < t.length-1; i++) {
            sb.append(t[i]).append(" ");
        }
        String message=sb.toString();
        int timestamp=Integer.parseInt(t[t.length-1]);
        services.putIfAbsent(serviceName,new Service(serviceName));
        services.get(serviceName).addMicroservice(microserviceName);
        Log log=new Log(type,message,timestamp);
        log.setMicroservice(microserviceName);
        services.get(serviceName).getMicroservices().get(microserviceName).addLog(log);
    }
    void printServicesBySeverity(){
        services.values().stream()
                .sorted(Comparator.comparing(Service::getAverageSeverityOfLogs).reversed())
                .forEach(System.out::println);
    }
    Map<Integer, Integer> getSeverityDistribution (String service, String microservice){
        Map<Integer,Integer> map=new TreeMap<>();
        Service s=services.get(service);
        if(s.getMicroservices().containsKey(microservice)){
            Microservice m=s.getMicroservices().get(microservice);
            IntStream.range(0,m.getNumberOfLogs()).forEach(i->{
                map.putIfAbsent(m.getSeverity(i),0);
                map.compute(m.getSeverity(i),(k,v) -> v+=1);
            });
        }
        else{
            IntStream.range(0,s.getAllLogs().size()).forEach(i->{
                map.putIfAbsent(s.getAllLogs().get(i).getSeverity(),0);
                map.compute(s.getAllLogs().get(i).getSeverity(),(k,v) -> v+=1);
            });
        }
        return map;
    }
    void displayLogs(String service, String microservice, String order) {
        if (services.containsKey(service) && services.get(service).getMicroservices().containsKey(microservice)) {
            Microservice m = services.get(service).getMicroservices().get(microservice);

            Comparator<Log> c;
            if (order.contains("NEW")) c = Comparator.comparing(Log::getTimestamp).reversed();
            else if (order.contains("OLD")) c = Comparator.comparing(Log::getTimestamp);
            else if (order.contains("MOST")) c = Comparator.comparing(Log::getSeverity).thenComparing(Log::getTimestamp).reversed();
            else c = Comparator.comparing(Log::getSeverity);
            System.out.println("displayLogs "+service+" "+microservice+" "+order);
            m.getLogs().stream().sorted(c).forEach(log -> {
                System.out.printf("%s|%s [%s] %s%d T:%d%n",
                        service,
                        microservice,
                        log.type,
                        log.message,
                        log.timestamp,
                        log.timestamp);
            });
        } else {
            Service m = services.get(service);

            Comparator<Log> c;
            if (order.contains("NEW")) c = Comparator.comparing(Log::getTimestamp).reversed();
            else if (order.contains("OLD")) c = Comparator.comparing(Log::getTimestamp);
            else if (order.contains("MOST")) c = Comparator.comparing(Log::getSeverity).thenComparing(Log::getTimestamp).reversed();
            else c = Comparator.comparing(Log::getSeverity);
            System.out.println("displayLogs "+service+" "+order);
            m.getAllLogs().stream().sorted(c).forEach(log -> {
                System.out.printf("%s|%s [%s] %s%d T:%d%n",
                        service,
                        log.microservice,
                        log.type,
                        log.message,
                        log.timestamp,
                        log.timestamp);
            });
        }
    }
}


public class LogsTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LogCollector collector = new LogCollector();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("addLog")) {
                collector.addLog(line.replace("addLog ", ""));
            } else if (line.startsWith("printServicesBySeverity")) {
                collector.printServicesBySeverity();
            } else if (line.startsWith("getSeverityDistribution")) {
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                if (parts.length == 3) {
                    microservice = parts[2];
                }
                collector.getSeverityDistribution(service, microservice).forEach((k,v)-> System.out.printf("%d -> %d%n", k,v));
            } else if (line.startsWith("displayLogs")){
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                String order = null;
                if (parts.length == 4) {
                    microservice = parts[2];
                    order = parts[3];
                } else {
                    order = parts[2];
                }
                collector.displayLogs(service, microservice, order);
            }
        }
    }
}