package K1.T8;


import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class NonExistingItemException extends Exception{
    public NonExistingItemException(String message) {
        super(message);
    }
}

class Archive implements Comparable<Archive>{
    protected int id;
    protected LocalDate dateArchived;

    public Archive(int id) {
        this.id = id;
        this.dateArchived = LocalDate.MIN;
    }
    public void setDateArchived(LocalDate dateArchived) {
        this.dateArchived = dateArchived;
    }

    @Override
    public int compareTo(Archive o) {
        return o.dateArchived.compareTo(dateArchived);
    }

    public int getId() {
        return id;
    }

    public LocalDate getDateArchived() {
        return dateArchived;
    }
}

class LockedArchive extends Archive{
    private LocalDate dateToOpen;
    public LockedArchive(int id, LocalDate dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    public LocalDate getDateToOpen() {
        return dateToOpen;
    }
}

class SpecialArchive extends Archive{
    private int maxOpen;
    private int currentOpen;
    public int getCurrentOpen(){
        return currentOpen;
    }
    public void setCurrentOpen(int n){
        currentOpen=n;
    }
    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
    }

    public int getMaxOpen() {
        return maxOpen;
    }
}

class ArchiveStore{
    List<Archive> archives;
    List<String> logs;
    public ArchiveStore() {
        archives=new ArrayList<>();
        logs=new ArrayList<>();
    }
    public void archiveItem(Archive item, LocalDate date){
        item.setDateArchived(date);
        archives.add(item);
        logs.add("Item "+item.getId()+" archived at "+date);
    }

    void openItem(int id, LocalDate date) throws NonExistingItemException{
        int ind=-1;
        for (int i = 0; i < archives.size(); i++) {
            if(archives.get(i).getId()==id) ind=i;
        }
        if(ind==-1){
            throw new NonExistingItemException("Item with id "+id+" doesn't exist");
        }
        if(archives.get(ind) instanceof SpecialArchive){
            SpecialArchive temp= (SpecialArchive) archives.get(ind);
            if (temp.getCurrentOpen()>=temp.getMaxOpen()){
                logs.add("Item "+temp.getId()+" cannot be opened more than "+temp.getMaxOpen()+" times");
            }
            else {
                logs.add("Item "+id+" opened at "+date);
                temp.setCurrentOpen(temp.getCurrentOpen()+1);
            }
        }
        else{
            LockedArchive temp=(LockedArchive) archives.get(ind);
            if(date.isBefore(temp.getDateToOpen())){
                logs.add("Item "+temp.getId()+" cannot be opened before "+temp.getDateToOpen());
            }
            else logs.add("Item "+id+" opened at "+date);
        }
    }
    String getLog(){
        StringBuilder sb=new StringBuilder();
        for (String s: logs){
            sb.append(s).append("\n");
        }
        return sb.toString();
    }
}


public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}