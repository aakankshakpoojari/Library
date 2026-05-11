package structures;
import java.util.Stack;

public class TransactionStack {
    private Stack<String> history = new Stack<>();
    
    public void logTransaction(String action) {
        history.push(action);
    }
    
    public String undo() {
        return history.isEmpty() ? null : history.pop();
    }
}

