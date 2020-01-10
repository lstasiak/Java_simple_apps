package app;

import exception.DataExportException;
import exception.DataImportException;
import exception.InvalidDataException;
import exception.NoSuchOptionException;
import io.ConsolePrinter;
import io.DataReader;
import io.file.FileManager;
import io.file.FileManagerBuilder;
import model.Book;
import model.Library;
import model.Magazine;
import model.Publication;
import model.comparator.AlphabeticalTitleComparator;
import model.comparator.DateComparator;

import java.util.Arrays;
import java.util.InputMismatchException;

public class LibraryControl {

    private ConsolePrinter printer = new ConsolePrinter();
    private DataReader dataReader = new DataReader(printer);
    private FileManager fileManager;

    private Library library;

    LibraryControl() {
        fileManager = new FileManagerBuilder(printer, dataReader).build();
        try {
            library = fileManager.importData();
            printer.printLine("Data from the file has been imported");
        } catch (DataImportException | InvalidDataException e) {
            printer.printLine(e.getMessage());
            printer.printLine("A new base has been initiated.");
            library = new Library();
        }
    }

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
                case DELETE_BOOK:
                    deleteBook();
                    break;
                case DELETE_MAGAZINE:
                    deleteMagazine();
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
                printer.printLine(e.getMessage() + ", try again: ");
            } catch (InputMismatchException ignored) {
                printer.printLine("The value is not a number, try again: ");
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
            library.addPublication(book);
        } catch (InputMismatchException e) {
            printer.printLine("Couldn't create a book, incorrect data");
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Capacity limit reached, you cannot add another book");
        }
    }

    private void printBooks() {
        Publication[] publications = getSortedPublications();
        printer.printBooks(publications);
    }

    private void addMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine();
            library.addPublication(magazine);
        } catch (InputMismatchException e) {
            printer.printLine("Couldn't create a magazine, incorrect data");
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Capacity limit reached, you cannot add another magazine");
        }
    }

    private void printMagazines() {
        Publication[] publications = getSortedPublications();
        printer.printMagazines(publications);
    }
    private void deleteMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine();
            if (library.removePublication(magazine))
                printer.printLine("The magazine has been removed.");
            else
                printer.printLine("There is no such magazine.");
        } catch (InputMismatchException e) {
            printer.printLine("Failed to create a magazine, invalid data");
        }
    }

    private void deleteBook() {
        try {
            Book book = dataReader.readAndCreateBook();
            if (library.removePublication(book))
                printer.printLine("The book has been removed.");
            else
                printer.printLine("There is no such book.");
        } catch (InputMismatchException e) {
            printer.printLine("Failed to create a book, invalid data");
        }
    }
    private void exit() {
        try {
            fileManager.exportData(library);
            printer.printLine("Export data to a file successfully completed");
        } catch (DataExportException e) {
            printer.printLine(e.getMessage());
        }
        printer.printLine("Bye Bye!");
        dataReader.close();
    }

    private Publication[] getSortedPublications() {
        Publication[] publications = library.getPublications();

        printer.printLine("Select the sorting method:");
        printer.printLine("alphabetical - " + 1);
        printer.printLine("by date - " + 2);
        int optionS = dataReader.getInt();

        boolean chosen = false;
        do {
            if (optionS == 1){
                Arrays.sort(publications, new AlphabeticalTitleComparator());
                chosen = true;
            } else if (optionS == 2) {
                Arrays.sort(publications, new DateComparator());
                chosen = true;
            } else
                System.out.println("Wrong option. Enter a number: 1 or 2");

        } while(!chosen);

        return publications;
    }

    private enum Option {
        EXIT(0, "Exit the library"),
        ADD_BOOK(1, "Add the book to the library"),
        ADD_MAGAZINE(2,"Add the magazine to the library"),
        PRINT_BOOKS(3, "Print valid books"),
        PRINT_MAGAZINES(4, "Print valid magazines"),
        DELETE_BOOK(5, "Delete a book"),
        DELETE_MAGAZINE(6, "Delete a magazine");

        private int value;
        private String description;

        Option(int value, String description) {
            this.value = value;
            this.description = description;
        }

        @Override
        public String toString() {
            return value + " - " + description;
        }

        static Option createFromInt(int option) throws NoSuchOptionException {
            try {
                return Option.values()[option];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchOptionException("There is no option with id " + option);
            }
        }
    }

}
