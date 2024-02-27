package K1.T17;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(String message) {
        super(message);
    }
}

class Category implements Comparable<Category>{
    String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Category category) {
        return category.getName().compareTo(getName());
    }
}

abstract class NewsItem{
    String name;
    Date date;
    Category category;
    public NewsItem(String name, Date date, Category category) {
        this.name = name;
        this.date = date;
        this.category = category;
    }
    public String getTeaser(){
        return null;
    }
}

class TextNewsItem extends NewsItem{
    String text;

    public TextNewsItem(String name, Date date, Category category, String text) {
        super(name, date, category);
        this.text=text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getTeaser(){
        StringBuilder sb= new StringBuilder();
        sb.append(name).append("\n");
        long duration = Calendar.getInstance().getTime().getTime() - date.getTime();
        sb.append( TimeUnit.MILLISECONDS.toMinutes(duration)).append("\n");
        sb.append(text, 0, Math.min(text.length(), 80)).append("\n");
        return sb.toString();
    }
}

class MediaNewsItem extends NewsItem{
    String url;
    int num;

    public MediaNewsItem(String name, Date date, Category category, String url, int num) {
        super(name, date, category);
        this.url = url;
        this.num = num;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
    public String getTeaser(){
        StringBuilder sb= new StringBuilder();
        sb.append(name).append("\n");
        long duration = Calendar.getInstance().getTime().getTime() - date.getTime();
        sb.append( TimeUnit.MILLISECONDS.toMinutes(duration)).append("\n");
        sb.append(url).append("\n");
        sb.append(num).append("\n");
        return sb.toString();
    }
}

class FrontPage{
    List<NewsItem> items;
    List<Category> categories;

    public FrontPage(Category []categories) {
        this.categories = Arrays.stream(categories).collect(Collectors.toList());
        this.items=new ArrayList<>();
    }

    public void addNewsItem(NewsItem newsItem){
        items.add(newsItem);
    }

    List<NewsItem> listByCategory(Category category){
        List<NewsItem> tmp=new ArrayList<>();
        for (NewsItem i: items){
            if(i.category.compareTo(category)==0){
                tmp.add(i);
            }
        }
        return tmp;
    }

    List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        int flag=0;
        for(Category cat: categories){
            if (cat.getName().equals(category)) {
                flag = 1;
                break;
            }
        }
        if(flag==0) throw new CategoryNotFoundException("Category "+category+" was not found");
        Category c=new Category(category);
        List<NewsItem> tmp=new ArrayList<>();
        for (NewsItem i: items){
            if(i.category.compareTo(c)==0){
                tmp.add(i);
            }
        }
        return tmp;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        for(NewsItem i:items){
            sb.append(i.getTeaser());
        }
        return sb.toString();
    }
}



public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde