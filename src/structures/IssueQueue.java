package structures;
import java.util.LinkedList;
import java.util.Queue;

public class IssueQueue {
    private LinkedList<String> pendingIssues = new LinkedList<>();
    
    public void addRequest(String bookIsbn) {
        // Custom logic to add a request, potentially with priority or checks
        // For now, simple add to the end
        pendingIssues.addLast(bookIsbn);
    }
    
    public String processNext() {
        // Custom logic to process the next request, e.g., FIFO
        return pendingIssues.isEmpty() ? null : pendingIssues.removeFirst();
    }
    
    public boolean isEmpty() { 
        return pendingIssues.isEmpty(); 
    }
    

    public String getNext() {
        return pendingIssues.isEmpty() ? null : pendingIssues.getFirst();
    }
}

