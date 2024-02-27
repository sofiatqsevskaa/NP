package K2.T37;
import java.util.*;


class Comment {
    String username;
    String id;
    String content;
    String replyToId;
    Map<String,Comment> replies;
    int likes;
    boolean hasReplies=false;
    public Comment(String username, String id, String content, String replyToId) {
        this.username = username;
        this.id = id;
        this.content = content;
        this.replyToId = replyToId;
        replies=new HashMap<>();
        likes=0;
    }

    public String getId() {
        return id;
    }

    public void addReply(Comment comment){
        replies.put(comment.getId(),comment);
        hasReplies=true;
    }
    void likeComment() {
        likes++;
    }
    boolean hasReplies(){
        return hasReplies;
    }
    int getLikes() {
        return likes + recursiveLikesCalculation(new ArrayList<>(replies.values()));
    }
    private int recursiveLikesCalculation(List<Comment> comments) {
        if (comments.isEmpty()) {
            return 0;
        } else {
            return comments.stream()
                    .mapToInt(comment -> comment.likes + recursiveLikesCalculation(new ArrayList<>(comment.replies.values())))
                    .sum();
        }
    }

    public String print(String a){
        StringBuilder sb=new StringBuilder();
        sb.append(a).append("Comment: ").append(content).append("\n");
        sb.append(a).append("Written by: ").append(username).append("\n");
        sb.append(a).append("Likes: ").append(likes).append("\n");
        replies.values().stream().sorted(Comparator.comparing(Comment::getLikes).reversed()).forEach(com->sb.append(com.print(a+"    ")));
        return sb.toString();
    }
}

class Post {
    String username;
    String postContent;
    Map<String,Comment> allComments;
    List<Comment> postComments;

    public Post(String username, String postContent) {
        this.username = username;
        this.postContent = postContent;
        postComments=new ArrayList<>();
        allComments=new HashMap<>();
    }
    void addComment (String username, String commentId, String content, String replyToId){
        Comment c = new Comment(username,commentId,content,replyToId);
        allComments.putIfAbsent(commentId,c);
        if(replyToId!=null) allComments.get(replyToId).addReply(c);
        else postComments.add(c);
    }
    void likeComment (String commentId) {
        allComments.get(commentId).likeComment();
    }
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("Post: ").append(postContent).append("\n");
        sb.append("Written by: ").append(username).append("\n");
        sb.append("Comments: ").append("\n");
        postComments.stream().sorted(Comparator.comparing(Comment::getLikes).reversed()).forEach(com->sb.append(com.print("        ")));
        return sb.toString();
    }
}

public class PostTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String postAuthor = sc.nextLine();
        String postContent = sc.nextLine();

        Post p = new Post(postAuthor, postContent);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            String testCase = parts[0];

            if (testCase.equals("addComment")) {
                String author = parts[1];
                String id = parts[2];
                String content = parts[3];
                String replyToId = null;
                if (parts.length == 5) {
                    replyToId = parts[4];
                }
                p.addComment(author, id, content, replyToId);
            } else if (testCase.equals("likes")) { //likes;1;2;3;4;1;1;1;1;1 example
                for (int i = 1; i < parts.length; i++) {
                    p.likeComment(parts[i]);
                }
            } else {
                System.out.println(p);
            }

        }
    }
}

