package K2.T29;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Partial exam II 2016/2017
 */

class Team{
    String name;
    int goals;
    int goalsTaken;
    int wins;
    int losses;
    int ties;
    public Team(String name) {
        this.name = name;
        goals=0;
        goalsTaken=0;
        wins=0;
        losses=0;
        ties=0;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team)) return false;
        Team team = (Team) o;
        return Objects.equals(name, team.name);
    }
    public void update(int homeGoals, int awayGoals) {
        if(homeGoals==awayGoals) ties++;
        else if(homeGoals>awayGoals) wins++;
        else losses++;
        goals+=homeGoals;
        goalsTaken+=awayGoals;
    }
    public int getPoints(){
        return (wins*3)+ties;
    }
    public int getDif(){
        return goals-goalsTaken;
    }
    @Override
    public String toString(){
        return String.format("%-15s%5d%5d%5d%5d%5d",name,wins+losses+ties,wins,ties,losses,getPoints());
    }

    public String getName() {
        return name;
    }
}

class FootballTable{
    Map<String,Team> teams;
    public FootballTable() {
        teams=new HashMap<>();
    }
    private final Comparator<Team> teamComparator=
            Comparator.comparing(Team::getPoints).thenComparing(Team::getDif);
    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        teams.putIfAbsent(homeTeam,new Team(homeTeam));
        teams.get(homeTeam).update(homeGoals,awayGoals);
        teams.putIfAbsent(awayTeam,new Team(awayTeam));
        teams.get(awayTeam).update(awayGoals,homeGoals);
    }

    public void printTable() {
        List<Team> tmp =teams.values().stream().sorted(teamComparator.reversed().thenComparing(Team::getName)).collect(Collectors.toList());
        for (int i = 0; i < tmp.size(); i++) {
            System.out.printf("%2d. %s%n",i+1,tmp.get(i).toString());
        }
    }
}

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}




