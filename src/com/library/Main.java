package com.library;

import java.util.List;
import java.util.Scanner;

import com.library.exception.BookNotFoundException;
import com.library.exception.BookUnavailableException;
import com.library.model.Book;
import com.library.service.LibraryService;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        LibraryService service = new LibraryService();

        while (true) {

            System.out.println("\n=============================================");
System.out.println("    WELCOME TO LIBRARY MANAGEMENT SYSTEM");
System.out.println("=============================================");
System.out.println("1. Display All Books");
System.out.println("2. Add New Book");
System.out.println("3. Borrow Book");
System.out.println("4. Return Book");
System.out.println("5. Delete Book");
System.out.println("6. Dashboard");
System.out.println("7. View Available Books");
System.out.println("8. Update Book");
System.out.println("9. View Borrowed Books");
System.out.println("10. Search Book");
System.out.println("11. Exit Application");
System.out.println("=============================================");

System.out.print("Enter your choice (1-11): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            try {

                switch (choice) {

                    case 1:
                        List<Book> books = service.getAllBooks();

                        if (books.isEmpty()) {
                            System.out.println("No books found.");
                        } else {
                            for (Book b : books) {
                                System.out.println(b);
                            }
                        }
                        break;

                    case 2:
                        System.out.print("Enter Book Title: ");
                        String title = scanner.nextLine();

                        System.out.print("Enter Author Name: ");
                        String author = scanner.nextLine();

                        service.addNewBook(title, author);
                        break;

                        case 3:
                            System.out.print("Enter Book ID: ");
                            int borrowId = scanner.nextInt();
                            scanner.nextLine();
                        
                            System.out.print("Enter Borrower Name: ");
                            String borrowerName = scanner.nextLine();
                        
                            System.out.print("Enter Student ID: ");
                            String studentId = scanner.nextLine();
                        
                            System.out.print("Enter Phone Number: ");
                            String phone = scanner.nextLine();
                        
                            System.out.print("Enter Borrow Date (yyyy-mm-dd): ");
                            java.sql.Date borrowDate = java.sql.Date.valueOf(scanner.nextLine());
                        
                            System.out.print("Enter Expected Return Date (yyyy-mm-dd): ");
                            java.sql.Date returnDate = java.sql.Date.valueOf(scanner.nextLine());
                        
                            service.borrowBook(borrowId, borrowerName, studentId, phone, borrowDate, returnDate);
                            break;
                    case 4:
                        System.out.print("Enter Book ID: ");
                        int returnId = scanner.nextInt();

                        service.returnBook(returnId);
                        break;

                    case 5:
                        System.out.print("Enter Book ID to Delete: ");
                        int deleteId = scanner.nextInt();

                        service.deleteBook(deleteId);
                        break;
                    case 6:
                            service.showDashboard();
                            break;
                        
                            
                    case 7:

                            List<Book> availableBooks = service.getAvailableBooks();
                        
                            if (availableBooks.isEmpty()) {
                                System.out.println("No books available.");
                            } else {
                                for (Book b : availableBooks) {
                                    System.out.println(b);
                                }
                            }
                        
                            break;
                        
                    case 8:

                            System.out.print("Enter Book ID: ");
                            int updateId = scanner.nextInt();
                            scanner.nextLine();
                        
                            System.out.print("Enter New Title: ");
                            String newTitle = scanner.nextLine();
                        
                            System.out.print("Enter New Author: ");
                            String newAuthor = scanner.nextLine();
                        
                            service.updateBook(updateId, newTitle, newAuthor);
                        
                            break;
                        
                              
                
                    case 9:

                        List<Book> borrowedBooks = service.getBorrowedBooks();
                    
                        if (borrowedBooks.isEmpty()) {
                            System.out.println("No books are currently borrowed.");
                        } else {
                            for (Book b : borrowedBooks) {
                                System.out.println(b);
                            }
                        }
                        break;
                    case 10:

                         System.out.print("Enter Book ID: ");
                         int searchId = scanner.nextInt();

                         Book book = service.searchBookById(searchId);

                         System.out.println(book);

                         break;
                    
                    case 11:
                        System.out.println("Thank you!");
                        scanner.close();
                        System.exit(0);    

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            } catch (BookNotFoundException | BookUnavailableException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}