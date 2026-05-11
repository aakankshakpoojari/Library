package gui;
import service.LibraryService;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class UserPanel extends JPanel {
    private LibraryService service;
    private JTextField userIdField, userNameField;
    private JTextArea historyArea;
    private HashMap<String, User> users = new HashMap<>();
    
    public UserPanel(LibraryService service) {
        this.service = service;
        setLayout(new BorderLayout());
        
        // Top controls
        JPanel topPanel = new JPanel();
        userIdField = new JTextField(10);
        userNameField = new JTextField(15);
        JButton addUserBtn = new JButton("➕ Add User");
        JButton issueBtn = new JButton("📖 Issue Book");
        JButton undoBtn = new JButton("↩️ Undo Last");
        
        addUserBtn.addActionListener(e -> {
            String id = userIdField.getText().trim();
            String name = userNameField.getText().trim();
            if (!id.isEmpty() && !name.isEmpty()) {
                users.put(id, new User(id, name));
                updateDisplay();
                userIdField.setText(""); userNameField.setText("");
            }
        });
        
        issueBtn.addActionListener(e -> {
            String id = userIdField.getText().trim();
            User user = users.get(id);
            if (user != null) {
                user.addIssue("Book-" + System.currentTimeMillis());
                service.getTransactionStack().logTransaction("User " + id + " issued book");
                updateDisplay();
            }
        });
        
        undoBtn.addActionListener(e -> {
            String id = userIdField.getText().trim();
            User user = users.get(id);
            if (user != null) {
                String undone = user.undoLastIssue();
                historyArea.append("\nUndo: " + (undone != null ? undone : "No history"));
            }
        });
        
        topPanel.add(new JLabel("ID:"));
        topPanel.add(userIdField);
        topPanel.add(new JLabel("Name:"));
        topPanel.add(userNameField);
        topPanel.add(addUserBtn);
        topPanel.add(issueBtn);
        topPanel.add(undoBtn);
        add(topPanel, BorderLayout.NORTH);
        
       
        historyArea = new JTextArea("User History (Stack LIFO):\n");
        historyArea.setEditable(false);
        add(new JScrollPane(historyArea), BorderLayout.CENTER);
        
        updateDisplay();
    }
    
    private void updateDisplay() {
        historyArea.setText("Users (HashMap): " + users.size() + "\n");
        users.values().forEach(user -> 
            historyArea.append(user.getId() + ": " + user.getName() + 
                " | Stack size: " + user.getIssueCount() + "\n"));

    }
}
