package structures;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Maintains a stack of reversible transactions.
 * Supports undo by popping the last transaction and providing data to reverse it.
 */
public class TransactionStack {
    private LinkedList<Transaction> history = new LinkedList<>();
    private static final int MAX_HISTORY = 50; // Limit memory usage
    
    public void logTransaction(Transaction transaction) {
        if (transaction != null) {
            history.addLast(transaction);
            // Keep only last 50 transactions
            if (history.size() > MAX_HISTORY) {
                history.removeFirst();
            }
        }
    }
    
    public Transaction undo() {
        return history.isEmpty() ? null : history.removeLast();
    }
    
    public Transaction peek() {
        return history.isEmpty() ? null : history.getLast();
    }
    
    public boolean isEmpty() {
        return history.isEmpty();
    }
    
    public int getHistorySize() {
        return history.size();
    }
    
    public LinkedList<Transaction> getHistory() {
        // Return a new LinkedList to prevent external modification
        return new LinkedList<>(history);
    }
}

