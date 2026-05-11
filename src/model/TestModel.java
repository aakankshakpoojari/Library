package model;
import java.util.ArrayList;

public class TestModel {
    public static void main(String[] args) {
        ArrayList<Book> books = new ArrayList<>();
        books.add(new FictionBook("F001", "Harry Potter", "JK Rowling"));
        books.add(new TextBook("T001", "Java OOP", "Your Prof"));
        
        for(Book b : books) {  
            System.out.println(b); 
        }
    }
}
