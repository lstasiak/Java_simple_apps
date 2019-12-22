package app;

import exception.NoSuchOptionException;
import io.ConsolePrinter;
import io.DataReader;
import model.Book;
import model.Library;
import model.Magazine;
import model.Publication;

import java.util.InputMismatchException;

public class LibraryControl {

    private ConsolePrinter printer = new ConsolePrinter();
    private DataReader dataReader = new DataReader(printer);
    private Library library = new Library();

    public void controlLoop() {
        Option option;

        do {
            printOptions();
            option = getOption();
            switch (option) {
                case ADD_BOOK:
                    addBook();
                    break;
                case ADD_MAGAZINE:
                    addMagazine();
                    break;
                case PRINT_BOOKS:
                    printBooks();
                    break;
                case PRINT_MAGAZINES:
                    printMagazines();
                    break;
                case EXIT:
                    exit();
                    break;
                default:
                    System.out.println("Wrong option. Enter the number again: ");
            }
        } while(option != Option.EXIT);

    }
    private Option getOption() {
        boolean optionOk = false;
        Option option = null;
        while(!optionOk) {
            try {
                option = Option.createFromInt(dataReader.getInt());
                optionOk = true;
            } catch (NoSuchOptionException e) {
                printer.printLine(e.getMessage() + ", enter again: ");
            } catch (InputMismatchException ignored) {
                printer.printLine("The value is not a number, enter again: ");
            }
        }
        return option;
    }

    private void printOptions() {
        System.out.println("Select option: ");
        for (Option option: Option.values()) {
            printer.printLine(option.toString());
        }
    }

    private void addBook() {
        try {
            Book book = dataReader.readAndCreateBook();
            library.addBook(book);
        } catch (InputMismatchException e) {
            printer.printLine("Couldn't create a book, incorrect data");
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Capacity limit reached, you cannot add another book");
        }
    }

    private void printBooks() {
        Publication[] publications = library.getPublications();
        printer.printBooks(publications);
    }


    private void addMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine();
            library.addMagazine(magazine);
        } catch (InputMismatchException e) {
            printer.printLine("Couldn't create a magazine, incorrect data");
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Capacity limit reached, you cannot add another magazine");
        }
    }

    private void printMagazines() {
        Publication[] publications = library.getPublications();
        printer.printMagazines(publications);
    }

    private void exit() {
        printer.printLine("Bye Bye!");
        dataReader.close();
    }

}
