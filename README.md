# Library Management System

A Java Swing desktop application for managing books, users, and issue tracking — built to demonstrate OOP principles and core data structures.

![Demo](images/demo.png)

## Features

- **Live Search** - The book search functionality now updates results in real-time as you type, eliminating the need to click a search button.
- **Book Management** — Add, search, and categorize books (Fiction / Textbook) with duplicate ISBN detection and input validation
- **Issue Queue** — FIFO-based book request processing with per-user borrow limits (max 3 books)
- **User Management** — Register users, track issued books, and undo recent actions via a transaction history stack
- **Persistence** — Book and user data saved to CSV on every change; state is restored on next launch

## Tech Stack

| Layer | Details |
|---|---|
| Language | Java 17 |
| GUI | Java Swing — `JTable`, `JTabbedPane`, event listeners |
| OOP Concepts | Abstraction, Inheritance, Polymorphism, Singleton |
| Data Structures | `ArrayList`, `HashMap`, `Queue` (LinkedList), `Stack` |
| Persistence | Flat-file CSV (no external dependencies) |

## Project Structure

src/<br>
├── model/       # Abstract Book class; FictionBook, TextBook subclasses<br>
├── structures/  # IssueQueue (FIFO), TransactionStack (undo history)<br>
├── service/     # LibraryService singleton — business logic + CSV I/O<br>
└── gui/         # Swing panels: BookPanel, IssuePanel, UserPanel<br>

## How to Run

**VS Code:** Open project → F5 on `MainGUI.java`

**Terminal:**
```bash
./run.sh        # macOS/Linux
run.bat         # Windows
```

> Requires Java 17+. No external libraries needed.
