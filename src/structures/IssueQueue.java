package structures;
import java.util.LinkedList;
import java.util.Queue;

public class IssueQueue {
    private Queue<String> pendingIssues = new LinkedList<>();
    
    public void addRequest(String bookIsbn) {
        pendingIssues.offer(bookIsbn);
    }
    
    public String processNext() {
        return pendingIssues.poll();
    }
    
    public boolean isEmpty() { 
        return pendingIssues.isEmpty(); 
    }
    

    public String getNext() {
        return pendingIssues.peek();  
    }
}

