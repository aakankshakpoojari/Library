package gui;
import service.LibraryService;
import javax.swing.*;
import java.awt.*;

public class IssuePanel extends JPanel {
    private LibraryService service;
    private JTextField isbnField;
    private JTextArea queueDisplay;
    private JLabel statusLabel;
    
    public IssuePanel(LibraryService service) {
        this.service = service;
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel();
        isbnField = new JTextField(12);
        JButton issueBtn = new JButton("➕ Request Issue");
        JButton processBtn = new JButton("✅ Process Next");
        
        issueBtn.addActionListener(e -> {
            String isbn = isbnField.getText().trim();
            if (!isbn.isEmpty()) {
                service.requestIssue(isbn);
                isbnField.setText("");
                updateDisplay();
                statusLabel.setText("Queued: " + isbn);
            }
        });
        
        processBtn.addActionListener(e -> {
            String processed = service.processIssue();
            updateDisplay();
            statusLabel.setText(processed != null ? "Processed: " + processed : "Queue empty");
        });
        
        topPanel.add(new JLabel("ISBN:"));
        topPanel.add(isbnField);
        topPanel.add(issueBtn);
        topPanel.add(processBtn);
        add(topPanel, BorderLayout.NORTH);
        
        
        queueDisplay = new JTextArea();
        queueDisplay.setEditable(false);
        add(new JScrollPane(queueDisplay), BorderLayout.CENTER);
        
    
        statusLabel = new JLabel("Queue ready");
        add(statusLabel, BorderLayout.SOUTH);
        
        updateDisplay();
    }
    
    private void updateDisplay() {
        queueDisplay.setText("Pending Issues (FIFO Queue):\n" + 
            service.getPendingQueueStatus());  
    }
}

