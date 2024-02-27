package K2.T27;

import java.util.*;
import java.util.stream.Collectors;

class Name{
    String name;
    int num;
    public Name(String name) {
        this.name = name;
        this.num=1;
    }
    public String getName() {
        return name;
    }
    public int getNum() {
        return num;
    }
    public void setNum(int n){
        num=n;
    }
    public int getUniqueChars(){
        String tmp=name.toLowerCase();
        Set<Character> chars=new HashSet<>();
        for (int i = 0; i < tmp.length(); i++) {
            chars.add(tmp.charAt(i));
        }
        return chars.size();
    }

    @Override
    public String toString() {
        return name;
    }
}

class Names{
    List<Name> names;
    private final Comparator<Name> nameComparator=
            Comparator.comparing(Name::getName).thenComparing(Name::getNum);

    public Names() {
        this.names = new ArrayList<>();
    }

    public void addName(String name) {
        for (Name n : names) {
            if (n.getName().equals(name)) {
                n.setNum(n.getNum() + 1);
                return;
            }
        }
        names.add(new Name(name));
    }

    public void printN(int n) {
        names.stream().sorted(nameComparator).filter(i->i.getNum()>=n).forEach(j-> System.out.printf("%s (%d) %d%n",j.getName(),j.getNum(),j.getUniqueChars()));
    }
    public String findName(int len, int index) {
        List<Name> tmp=names.stream().filter(i->i.getName().length()<len).sorted(nameComparator).collect(Collectors.toList());
        if(index==tmp.size()) return tmp.get(0).getName();
        else if(index<tmp.size()) return tmp.get(index).getName();
        else return tmp.get(index%tmp.size()).getName();
    }
}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}
