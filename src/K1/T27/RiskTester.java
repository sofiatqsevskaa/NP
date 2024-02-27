package K1.T27;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Risk{
    public Risk() {
    }

    void processAttacksData(InputStream in){
        BufferedReader br=new BufferedReader(new InputStreamReader(in));
        List<String> strings=br.lines().collect(Collectors.toList());
        for (int i = 0; i < strings.size(); i++) {
            String next=strings.get(i);
            String[] playerOne=next.split(";")[0].split("\\s+");
            String[] playerTwo=next.split(";")[1].split("\\s+");
            int[] p1=new int[3];
            int[] p2=new int[3];

            for (int j = 0; j < 3; j++) {
                p1[j]=Integer.parseInt(playerOne[j]);
                p2[j]=Integer.parseInt(playerTwo[j]);
            }

            Arrays.sort(p1);
            Arrays.sort(p2);
            int x=0,y=0;
            for (int j = 0; j < 3; j++) {
                if(p1[j]>p2[j]) x++;
                else y++;
            }
            System.out.println(x+" "+y);
        }
    }
}

public class RiskTester {
    public static void main(String[] args) {
        Risk risk = new Risk();
        risk.processAttacksData(System.in);
    }
}