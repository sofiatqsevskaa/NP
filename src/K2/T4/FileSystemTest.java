package K2.T4;



import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class File implements Comparable<File>{
    String name;
    int size;
    LocalDateTime createdAt;

    public File(String name, int size, LocalDateTime createdAt) {
        this.name = name;
        this.size = size;
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof File)) return false;
        File file = (File) o;
        return Objects.equals(name, file.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size, createdAt);
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public int getYear(){
        return createdAt.getYear();
    }
    public String getMonth(){
        return createdAt.getMonth().toString();
    }
    public int getDay(){
        return createdAt.getDayOfMonth();
    }

    @Override
    public int compareTo(File other) {
        return Comparator.comparing(File::getCreatedAt)
                .thenComparing(File::getName)
                .thenComparingInt(File::getSize)
                //do not forgor :3
                .compare(this, other);
    }

    @Override
    public String toString() {
        return String.format("%-10s %5dB %s",name,size,createdAt.toString());
    }
}

class Folder{
    char name;
    Set<File> files;

    public Folder(char name) {
        this.name = name;
        this.files = new TreeSet<>();
    }

    public char getName() {
        return name;
    }
    public Folder() {
        files=new TreeSet<>();
    }

    public void addFile(File file) {
        files.add(file);
    }

    public int getSize(){
        return files.stream().mapToInt(File::getSize).sum();
    }

    @Override
    public String toString() {
        return files.stream()
                .sorted()
                .collect(Collectors.toList())
                .toString();
    }

    public Set<File> getFiles() {
        return files;
    }
}

class FileSystem {
    Map<Character, Folder> files;
    public FileSystem() {
        this.files = new HashMap<>();
    }

    public void addFile(char folder, String name, int size, LocalDateTime createdAt) {
        files.putIfAbsent(folder, new Folder(folder));
        files.get(folder).addFile(new File(name,size,createdAt));

    }
    public List<File> findAllHiddenFilesWithSizeLessThen(int size){
        //FLATMAP
        return files.values().stream().flatMap(i->i.files.stream())
                .filter(i->i.getName().startsWith("."))
                .filter(i->i.getSize()<size)
                .collect(Collectors.toList());
    }

    public int totalSizeOfFilesFromFolders(List<Character> collect) {
        //!!!
        return collect.stream().filter(character->files.containsKey(character)).mapToInt(i->files.get(i).getSize()).sum();
    }
    public Map<Integer, Set<File>> byYear(){
        Map<Integer,Set<File>> map= new HashMap<>();
        files.values().forEach(i->i.getFiles()
                .forEach(j->{
                    map.putIfAbsent(j.getYear(),new HashSet<>());
                    map.get(j.getYear()).add(j);
                }));
        return map;
    }
    public Map<String, Long> sizeByMonthAndDay() {
        Map<String, Long> map = new TreeMap<>();
        files.values().forEach(folder ->
                folder.getFiles().forEach(file -> {
                    String key = String.format("%s-%d", file.getMonth(), file.getDay());
                    map.putIfAbsent(key, 0L);
                    map.compute(key, (k, v) -> v + file.getSize());
                })
        );
        return map;
    }

}



public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}

// Your code here

