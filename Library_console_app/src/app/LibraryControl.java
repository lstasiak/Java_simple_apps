package app;

import io.DataReader;
import model.Book;
import model.Library;

public class LibraryControl {
    // controlling variables
    private static final int EXIT = 0;
    private static final int ADD_BOOK = 1;
    private static final int PRINT_BOOKS = 2;

    // IO variable
    private DataReader dataReader = new DataReader();

    // library object
    private Library library = new Library();

    /*
    main control method
    */
    public void controlLoop() {
        int option;

        do {
            printOptions();
            option = dataReader.getInt();
            switch (option) {
                case ADD_BOOK:
                    addBook();
                    break;
                case PRINT_BOOKS:
                    printBooks();
                    break;
                case EXIT:
                    exit();
                    break;
                default:
                    System.out.println("Wrong option. Enter a number again: ");
            }
        } while(option != EXIT);

    }
    private void printOptions() {
        System.out.println("Select option: ");
        System.out.println(EXIT + " - EXIT");
        System.out.println(ADD_BOOK + " - Add new book");
        System.out.println(PRINT_BOOKS + " - Print valid books");
    }

    private void addBook() {
        Book book = dataReader.readAndCreateBook();
        library.addBook(book);
    }

    private void printBooks() {
        library.printBooks();
    }

    private void exit() {
        System.out.println("Bye bye!");

        dataReader.close();
    }

}
