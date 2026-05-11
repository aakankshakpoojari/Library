package service;
import model.*;
public class TestService {
    public static void main(String[] args) {
        LibraryService service = LibraryService.getInstance();
        
     
        service.addBook(new FictionBook("F1", "Harry Potter", "Rowling"));
        service.addBook(new TextBook("T1", "Java OOP", "Author"));
        service.requestIssue("F1");
        
        System.out.println("Fiction books: " + service.getFictionBooks().size());
        System.out.println("Processed issue: " + service.processIssue());
    }
}

