import model.FictionBook;
import model.TextBook;
import model.User;
import service.LibraryService;

import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static LibraryService library = LibraryService.getInstance();

    public static void main(String[] args) {
        System.out.println("=== Library Management System ===");
        boolean running = true;
        while (running) {
            printMenu();
            int choice = Integer.parseInt(sc.nextLine().trim());
            switch (choice) {
                case 1 -> addBook();
                case 2 -> removeBook();
                case 3 -> library.listAllBooks();
                case 4 -> searchBook();
                case 5 -> addUser();
                case 6 -> removeUser();
                case 7 -> library.listAllUsers();
                case 8 -> issueBook();
                case 9 -> returnBook();
                case 10 -> viewIssuedBooks();
                case 0 -> {
                    library.shutdown();
                    running = false;
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    static void printMenu() {
        System.out.println("\n--- MENU ---");
        System.out.println("1. Add Book");
        System.out.println("2. Remove Book");
        System.out.println("3. List All Books");
        System.out.println("4. Search Book");
        System.out.println("5. Add User");
        System.out.println("6. Remove User");
        System.out.println("7. List All Users");
        System.out.println("8. Issue Book");
        System.out.println("9. Return Book");
        System.out.println("10. View Issued Books");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }

    static void addBook() {
        System.out.print("ISBN: ");
        String isbn = sc.nextLine().trim();
        System.out.print("Title: ");
        String title = sc.nextLine().trim();
        System.out.print("Author: ");
        String author = sc.nextLine().trim();
        System.out.print("Category (1. Fiction / 2. Textbook): ");
        int cat = Integer.parseInt(sc.nextLine().trim());
        if (cat == 1) {
            library.addBook(new FictionBook(isbn, title, author));
        } else {
            library.addBook(new TextBook(isbn, title, author));
        }
    }

    static void removeBook() {
        System.out.print("Enter ISBN to remove: ");
        library.removeBook(sc.nextLine().trim());
    }

    static void searchBook() {
        System.out.print("Enter title to search: ");
        library.searchBooks(sc.nextLine().trim());
    }

    static void addUser() {
        System.out.print("User ID: ");
        String id = sc.nextLine().trim();
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        library.addUser(new User(id, name));
    }

    static void removeUser() {
        System.out.print("Enter User ID to remove: ");
        library.removeUser(sc.nextLine().trim());
    }

    static void issueBook() {
        System.out.print("User ID: ");
        String userId = sc.nextLine().trim();
        System.out.print("ISBN: ");
        String isbn = sc.nextLine().trim();
        library.issueBook(userId, isbn);
    }

    static void returnBook() {
        System.out.print("User ID: ");
        String userId = sc.nextLine().trim();
        System.out.print("ISBN: ");
        String isbn = sc.nextLine().trim();
        library.returnBook(userId, isbn);
    }

    static void viewIssuedBooks() {
        System.out.print("User ID: ");
        library.listIssuedBooks(sc.nextLine().trim());
    }
}