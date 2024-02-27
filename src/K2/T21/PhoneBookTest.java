package K2.T21;


import java.util.Hashtable;
import java.util.Objects;
import java.util.*;
import java.util.stream.Collectors;


class DuplicateNumberException extends Exception{
    public DuplicateNumberException(String message) {
        super(message);
    }
}

class Contact{
    String name;
    String number;
    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;
        Contact contact = (Contact) o;
        return Objects.equals(number, contact.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, name);
    }

    @Override
    public String toString() {
        return String.format("%s %s",name,number);
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }
}

class PhoneBook{
    Set<Contact> contacts;
    private final Comparator<Contact> nameAndNumberComparator=
            Comparator.comparing(Contact::getName).thenComparing(Contact::getNumber);
    public PhoneBook() {
        this.contacts=new HashSet<>();
    }
    void addContact(String name, String num) throws DuplicateNumberException{
        if(contacts.add(new Contact(name,num))){
            return;
        }
        throw new DuplicateNumberException("Duplicate number: "+num);
    }
    public void contactsByNumber(String number){
        if(contacts.stream().anyMatch(i->i.getNumber().contains(number))) {
            contacts.stream().sorted(nameAndNumberComparator).filter(i -> i.getNumber().contains(number)).forEach(System.out::println);
        }
        else System.out.println("NOT FOUND");
    }
    public void contactsByName(String name){
        if(contacts.stream().anyMatch(i->i.getName().equals(name))) {
            contacts.stream().sorted(nameAndNumberComparator).filter(i -> i.getName().equals(name)).forEach(System.out::println);
        }
        else System.out.println("NOT FOUND");
    }
}


public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}


