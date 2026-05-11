package gui;
import service.LibraryService;
import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
    private LibraryService service = LibraryService.getInstance();
    
    public MainGUI() {
        setTitle("Library Management System - OOP + Data Structures Demo");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  
        
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("📚 Books", new BookPanel(service));     
        tabs.addTab("📋 Issue Queue", new IssuePanel(service));  
        tabs.addTab("👤 Users", new UserPanel(service));    
        
        add(tabs, BorderLayout.CENTER);
        
     
        JLabel status = new JLabel("Ready | Books: " + service.getAllBooks().size());
        add(status, BorderLayout.SOUTH);
    }
    
    public static void main(String[] args) {
      
        SwingUtilities.invokeLater(() -> {
            new MainGUI().setVisible(true);
        });
    }
}

