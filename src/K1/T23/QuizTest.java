package K1.T23;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

class InvalidOperationException extends Exception{
    public InvalidOperationException(String message) {
        super(message);
    }
}

abstract class Question implements Comparable<Question>{
    String text;

    int points;

    public Question(String text, int points) {
        this.text = text;
        this.points = points;
    }

    @Override
    public int compareTo(Question question) {
        return Integer.compare(getPoints(),question.getPoints());
        //return Comparator.comparing(Question::getPoints).thenComparing(Question::getPoints).compare(this,question);
    }
    public int getPoints(){
        return points;
    }

    abstract public boolean check(String answer);
    abstract public String toString();
    abstract public String getAnswer();
}

class YesNoQuestion extends Question{
    boolean answer;

    public YesNoQuestion(String text, int points, boolean answer) {
        super(text, points);
        this.answer = answer;
    }

    public boolean check(String answer){
        String tf="false";
        if(this.answer) tf="true";
        return answer.equals(tf);
    }

    @Override
    public String toString() {
        return String.format("True/False Question: %s Points: %d Answer: %b",text,points,answer);
    }

    @Override
    public String getAnswer() {
        return answer? "true" : "false";
    }
}

class MultipleChoiceQuestion extends Question{
    String answer;

    public MultipleChoiceQuestion(String text, int points, String answer) {
        super(text, points);
        this.answer = answer;
    }

    @Override
    public boolean check(String answer) {
        return (answer.equals(this.answer));
    }

    @Override
    public String toString() {
        return String.format("Multiple Choice Question: %s Points %d Answer: %s",text,points,answer);
    }

    @Override
    public String getAnswer() {
        return answer;
    }
}

class Quiz{
    List<Question> questions;

    public Quiz() {
        this.questions = new ArrayList<>();
    }

    void addQuestion(String questionData) throws InvalidOperationException{
        String[] strings=questionData.split(";");
        String text=strings[1];
        int points= Integer.parseInt(strings[2]);
        String answer=strings[3];
        if(strings[0].equals("TF")){
            if(!answer.equals("true")&&!answer.equals("false")){
                throw new InvalidOperationException(answer+ " is not allowed option for this question");
            }
            boolean a= answer.equals("true");
            questions.add(new YesNoQuestion(text,points,a));
        }
        else if(strings[0].equals("MC")){
            if(!answer.equals("A")&&!answer.equals("B")&&!answer.equals("C")&&!answer.equals("D")&&!answer.equals("E")){
                throw new InvalidOperationException(answer+ " is not allowed option for this question");
            }
            questions.add(new MultipleChoiceQuestion(text,points,answer));
        }
    }
    void printQuiz(OutputStream os){
        List<Question> newList=questions.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        PrintWriter pw=new PrintWriter(os);
        for(Question q:newList){
            pw.println(q.toString());
        }
        pw.flush();
    }
    void answerQuiz(List<String> answers, OutputStream os) throws InvalidOperationException {
        if(answers.size()>questions.size()) throw new InvalidOperationException("Answers and questions must be of same length!");
        PrintWriter pw=new PrintWriter(os);
        double totalPoints=0.0;
        for (int i = 0; i < answers.size(); i++) {
            if(questions.get(i) instanceof YesNoQuestion){
                double points=0.0;
                if(questions.get(i).getAnswer().equals(answers.get(i))){
                    points=(double) questions.get(i).getPoints();
                }
                pw.append(String.format("%d. %.2f",i+1,points)).append("\n");
                totalPoints+=points;
            }
            else if(questions.get(i) instanceof MultipleChoiceQuestion){
                double points=0.0;
                if(questions.get(i).getAnswer().equals(answers.get(i))){
                    points= (double)questions.get(i).getPoints();
                }
                else points-=questions.get(i).getPoints()*0.2;
                pw.append(String.format("%d. %.2f",i+1,points)).append("\n");
                totalPoints+=points;
            }
        }
        pw.append(String.format("Total points: %.2f",totalPoints));
        pw.flush();
    }
}

public class QuizTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i=0;i<questions;i++) {
            try {
                quiz.addQuestion(sc.nextLine());
            }catch (InvalidOperationException e){
                System.out.println(e.getMessage());
            }
        }

        List<String> answers = new ArrayList<>();

        int answersCount =  Integer.parseInt(sc.nextLine());

        for (int i=0;i<answersCount;i++) {
            answers.add(sc.nextLine());
        }

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase==1) {
            quiz.printQuiz(System.out);
        } else if (testCase==2) {
            try{
                quiz.answerQuiz(answers, System.out);
            }catch (InvalidOperationException e){
                System.out.println(e.getMessage());
            }

        } else {
            System.out.println("Invalid test case");
        }
    }
}

