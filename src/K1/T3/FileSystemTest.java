package K1.T3;

import java.util.*;

class FileNameExistsException extends Exception
{
    public FileNameExistsException(String file, String folder) {
        super(String.format("There is already a file named %s in the folder %s", file, folder));
    }
}

interface IFile extends Comparable<IFile>
{
    public String getFileName();
    public long getFileSize();
    public String getFileInfo(int indent);
    public void sortBySize();
    public long findLargestFile();
    @Override
    default int compareTo(IFile o)
    {
        return Long.compare(getFileSize(),o.getFileSize());
    }
}

class File implements IFile
{
    protected final String name;
    protected final long size;
    public File(String name, long size) {
        this.name = name;
        this.size = size;
    }
    @Override
    public String getFileName() {
        return name;
    }
    @Override
    public long getFileSize() {
        return size;
    }
    public String getFileInfo(int sub) {
        return " ".repeat(Math.max(0, 4 * sub)) + String.format("File name: %10s File size: %10d\n", name, size);
    }
    @Override
    public void sortBySize() {
    }
    @Override
    public long findLargestFile() {
        return size;
    }
    @Override
    public int compareTo(IFile o) {
        return IFile.super.compareTo(o);
    }
}

class Folder implements IFile {
    protected final String name;
    private final List<IFile> files;
    public Folder(String name) {
        this.name = name;
        this.files = new ArrayList<>();
    }
    private boolean check(IFile file) {
        for (IFile f: files)
        {
            if (f.getFileName().equals(file.getFileName())) return true;
        }
        return false;
    }
    public void addFile(IFile file) throws FileNameExistsException {
        if(check(file)) throw new FileNameExistsException(file.getFileName(), name);
        files.add(file);
    }
    @Override
    public String getFileName() {
        return name;
    }
    @Override
    public long getFileSize() {
        return files.stream().mapToLong(i->i.getFileSize()).sum();
    }
    @Override
    public String getFileInfo(int sub) {
        StringBuilder str = new StringBuilder();
        str.append(" ".repeat(Math.max(0, 4 * sub)));
        str.append(String.format("Folder name: %10s Folder size: %10d\n", name, getFileSize()));
        for(IFile f : files) { str.append(f.getFileInfo(sub + 1)); }
        return str.toString();
    }
    @Override
    public void sortBySize()
    {
        files.sort(IFile::compareTo);
        for (IFile f: files)
        {
            f.sortBySize();
        }
    }
    @Override
    public long findLargestFile() {
        return files.stream().mapToLong(IFile::findLargestFile).max().orElse(0);
    }
    @Override
    public int compareTo(IFile o) {
        return IFile.super.compareTo(o);
    }
}

class FileSystem
{
    protected Folder rootDirectory;
    public FileSystem() {
        this.rootDirectory = new Folder("root");
    }
    public void addFile(IFile file) throws FileNameExistsException {
        rootDirectory.addFile(file);
    }
    public long findLargestFile() {
        return rootDirectory.findLargestFile();
    }
    public void sortBySize() {
        rootDirectory.sortBySize();
    }
    @Override
    public String toString() {
        return rootDirectory.getFileInfo(0);
    }
}

public class FileSystemTest {
    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());




    }
}