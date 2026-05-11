package structures;
import java.util.Stack;

/**
 * Maintains a stack of reversible transactions.
 * Supports undo by popping the last transaction and providing data to reverse it.
 */
public class TransactionStack {
    private Stack<Transaction> history = new Stack<>();
    private static final int MAX_HISTORY = 50; // Limit memory usage
    
    public void logTransaction(Transaction transaction) {
        if (transaction != null) {
            history.push(transaction);
            // Keep only last 50 transactions
            if (history.size() > MAX_HISTORY) {
                history.removeElementAt(0);
            }
        }
    }
    
    public Transaction undo() {
        return history.isEmpty() ? null : history.pop();
    }
    
    public Transaction peek() {
        return history.isEmpty() ? null : history.peek();
    }
    
    public boolean isEmpty() {
        return history.isEmpty();
    }
    
    public int getHistorySize() {
        return history.size();
    }
    
    public Stack<Transaction> getHistory() {
        Stack<Transaction> copy = new Stack<>();
        copy.addAll(history);
        return copy;
    }
}

