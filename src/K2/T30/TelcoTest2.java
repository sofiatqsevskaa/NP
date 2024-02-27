package K2.T30;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class DurationConverter {
    public static String convert(long duration) {
        long minutes = duration / 60;
        duration %= 60;
        return String.format("%02d:%02d", minutes, duration);
    }
}

class Call {
    String id;
    String dialer;
    String receiver;
    long ringTimestamp;
    long answerTimestamp;
    long endTimestamp;
    long hold;
    long durationOnHold;
    boolean notResumed;
    public Call(String id, String dialer, String receiver, long ringTimestamp) {
        this.id = id;
        this.dialer = dialer;
        this.receiver = receiver;
        this.ringTimestamp = ringTimestamp;
        answerTimestamp=0;
        endTimestamp=0;
        durationOnHold=0;
        hold=0;
        notResumed=false;
    }
    void update(long timestamp, String action) {
        if(action.equals("ANSWER")) answerTimestamp=timestamp;
        else if(action.equals("END")) endTimestamp=timestamp;
        else if(action.equals("HOLD")) {
            hold=timestamp;
            notResumed=true;
        }
        else if(action.equals("RESUME")) {
            durationOnHold+=(timestamp-hold);
            notResumed=false;
        }
    }
    long getDuration() {
        if(answerTimestamp==0) return 0;
        return (notResumed)? hold-answerTimestamp-durationOnHold : endTimestamp-answerTimestamp-durationOnHold;
    }
    String getDurationString() {
        return DurationConverter.convert(getDuration());
    }

    public String getId() {
        return id;
    }

    public String getDialer() {
        return dialer;
    }

    public String getReceiver() {
        return receiver;
    }

    public long getRingTimestamp() {
        return ringTimestamp;
    }

    public long getAnswerTimestamp() {
        return answerTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }
}

class TelcoApp {
    Map<String, Call> calls;
    public TelcoApp() {
        this.calls = new HashMap<>();
    }
    void addCall (String uuid, String dialer, String receiver, long timestamp) {
        calls.put(uuid, new Call(uuid,dialer,receiver,timestamp));
    }
    void updateCall (String uuid, long timestamp, String action) {
        calls.get(uuid).update(timestamp,action);
    }
    void printChronologicalReport(String phoneNumber) {
        calls.values().stream().filter(call->call.getDialer().equals(phoneNumber)||call.getReceiver().equals(phoneNumber))
                .sorted(Comparator.comparing(Call::getRingTimestamp))
                .forEach(call->{
                    String RD= (call.getDialer().equals(phoneNumber))? "D" : "R";
                    String otherCaller = (call.getDialer().equals(phoneNumber))? call.getReceiver() : call.getDialer();
                    if(call.getAnswerTimestamp()==0) {
                        System.out.println(String.format("%s %s %d MISSED CALL 00:00",
                                RD,otherCaller,
                                (int)call.getEndTimestamp()));
                    }
                    else {
                        System.out.println(String.format("%s %s %d %d %s",
                                RD,otherCaller,
                                (int)call.getAnswerTimestamp(),
                                (int)call.getEndTimestamp(),
                                call.getDurationString()));
                    }
                });
    }
    void printReportByDuration(String phoneNumber) {
        calls.values().stream().filter(call->call.getDialer().equals(phoneNumber)||call.getReceiver().equals(phoneNumber))
                .sorted(Comparator.comparing(Call::getDuration).reversed())
                .forEach(call->{
                    String RD= (call.getDialer().equals(phoneNumber))? "D" : "R";
                    String otherCaller = (call.getDialer().equals(phoneNumber))? call.getReceiver() : call.getDialer();
                    if(call.getAnswerTimestamp()==0) {
                        System.out.println(String.format("%s %s %d MISSED CALL 00:00",
                                RD,otherCaller,
                                (int)call.getEndTimestamp()));
                    }
                    else {
                        System.out.println(String.format("%s %s %d %d %s",RD,otherCaller,(int)call.getAnswerTimestamp(),(int)call.getEndTimestamp(),call.getDurationString()));
                    }
                });
    }
    void printCallsDuration(){
        Map<String,Long> totalDurations= new TreeMap<>();
        calls.forEach((key, value) -> {
            String c = value.getDialer() + " <-> " + value.getReceiver();
            totalDurations.putIfAbsent(c, 0L);
            totalDurations.compute(c, (k, v) -> v + value.getDuration());
        });
        totalDurations.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry->{
                    System.out.println(entry.getKey()+" : "+DurationConverter.convert(entry.getValue()));
                });
    }
}

public class TelcoTest2 {
    public static void main(String[] args) {
        TelcoApp app = new TelcoApp();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            String command = parts[0];

            if (command.equals("addCall")) {
                String uuid = parts[1];
                String dialer = parts[2];
                String receiver = parts[3];
                long timestamp = Long.parseLong(parts[4]);
                app.addCall(uuid, dialer, receiver, timestamp);
            } else if (command.equals("updateCall")) {
                String uuid = parts[1];
                long timestamp = Long.parseLong(parts[2]);
                String action = parts[3];
                app.updateCall(uuid, timestamp, action);
            } else if (command.equals("printChronologicalReport")) {
                String phoneNumber = parts[1];
                app.printChronologicalReport(phoneNumber);
            } else if (command.equals("printReportByDuration")) {
                String phoneNumber = parts[1];
                app.printReportByDuration(phoneNumber);
            } else {
                app.printCallsDuration();
            }
        }

    }
}
