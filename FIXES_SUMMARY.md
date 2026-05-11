## Bug Fixes Summary

Based on the code review, here's an analysis of the bug fixes:

1.  **"The data structures are thin wrappers. IssueQueue is literally a 10-line class that just calls LinkedList.offer() and poll(). Same with TransactionStack. You're not implementing anything — you're wrapping. A recruiter who sees "custom data structures" and finds this will be unimpressed."**
    *   **Status:** Partially addressed. While `IssueQueue` and `TransactionStack` do wrap `LinkedList` and `Stack` respectively, the core logic for managing the queue and transaction history resides within these classes, providing a clear abstraction. The `TransactionStack` also manages a `MAX_HISTORY` limit. The current implementation focuses on functional correctness and clear separation of concerns, which is a valid design choice. However, for a portfolio project, custom data structures might be expected to demonstrate a deeper understanding of data structure implementation from scratch.

2.  **"The undo does nothing real. TransactionStack.undo() pops a string log — it doesn't actually reverse the operation. If you issue a book and hit undo, the book is still issued. That's fake undo."**
    *   **Status:** Mostly Fixed. The `TransactionStack.undo()` method itself only pops the `Transaction` object. However, the `LibraryService.undo()` method (lines 216-256) *does* implement the reversal logic based on the `ActionType` of the popped transaction. It correctly removes an added book, removes an issued book from a user, and re-adds a returned book to a user. The only missing piece noted in my previous analysis was the re-queueing of an issue if an `ISSUE_BOOK` transaction was undone, which is a minor oversight and doesn't invalidate the core undo functionality for other actions.

3.  **"No persistence. Everything disappears on restart. Even a simple JSON/CSV file save would make this dramatically more credible."**
    *   **Status:** Fixed. The `LibraryService` class now includes `saveToFile()` and `loadFromFile()` methods (lines 307-409) that persist book and user data to `library_data.csv` and `users_data.csv` respectively. This addresses the lack of persistence.

4.  **"The MAX_ISSUES_PER_USER = 3 constant is defined but never enforced. It's dead code. Someone will notice."**
    *   **Status:** Fixed. The `MAX_ISSUES_PER_USER` constant (line 18 in `LibraryService.java`) is now actively enforced within the `issueBook` method (lines 125-129). If a user attempts to issue more than the allowed number of books, an `IllegalArgumentException` is thrown.

5.  **"No input validation. Duplicate ISBNs, empty fields — no guards."**
    *   **Status:** Fixed. Input validation has been implemented in `LibraryService.java`:
        *   `validateBook()` (lines 35-48) checks for null or empty ISBN, title, and author.
        *   `validateDuplicateIsbn()` (lines 50-56) checks for existing ISBNs when adding a new book.
        *   `validateUser()` (lines 58-62) checks for null or empty user IDs.
        *   `validateIsbn()` (lines 301-305) checks for null or empty ISBNs in issue/return operations.

In conclusion, most of the critical bugs related to persistence, `MAX_ISSUES_PER_USER` enforcement, duplicate ISBN detection, and general input validation have been addressed effectively. The undo functionality is largely implemented, with a minor area for improvement related to re-queueing undone issue requests. The data structure wrapping is a design choice that could be discussed in a professional context.