package app;

import exception.*;
import io.ConsolePrinter;
import io.DataReader;
import io.file.FileManager;
import io.file.FileManagerBuilder;
import model.*;
import model.comparator.AlphabeticalTitleComparator;
import model.comparator.DateComparator;

import java.util.*;

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
                case ADD_USER: //dodano
                    addUser();
                    break;
                case PRINT_USERS: //dodano
                    printUsers();
                    break;
                case FIND_BOOK:
                    findBook();
                    break;
                case EXIT:
                    exit();
                    break;
                default:
                    System.out.println("Wrong option. Try again: ");
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
        printer.printBooks(selectAndSort());
    }

    private void addMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine();
            library.addPublication(magazine);
        } catch (InputMismatchException e) {
            printer.printLine("Couldn't create a magazine, incorrect data");
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Capacity limit has reached, you cannot add another magazine");
        }
    }

    private void printMagazines() {
        printer.printMagazines(selectAndSort());
    }

    private void addUser() {
        LibraryUser libraryUser = dataReader.createLibraryUser();
        try {
            library.addUser(libraryUser);
        } catch (UserAlreadyExistsException e) {
            printer.printLine(e.getMessage());
        }
    }

    private void printUsers() {
        printer.printUsers(library.getSortedUsers(
                // sort by last name (alphabetical order)
                Comparator.comparing(User::getLastName, String.CASE_INSENSITIVE_ORDER)
        ));
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
    private void findBook() {
        printer.printLine("Enter the title of the publication:");
        String title = dataReader.getString();
        String notFoundMessage = "There is no such publication";
        library.findPublicationByTitle(title)
                .map(Publication::toString)
                .ifPresentOrElse(System.out::println, () -> System.out.println(notFoundMessage));
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

    private enum Option {
        EXIT(0, "Exit the library"),
        ADD_BOOK(1, "Add the book to the library"),
        ADD_MAGAZINE(2,"Add the magazine to the library"),
        PRINT_BOOKS(3, "Print valid books"),
        PRINT_MAGAZINES(4, "Print valid magazines"),
        DELETE_BOOK(5, "Delete a book"),
        DELETE_MAGAZINE(6, "Delete a magazine"),
        ADD_USER(7, "Add reader"),
        PRINT_USERS(8, "Print readers"),
        FIND_BOOK(9, "Search for a book");

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

    // pick a sorting type and return sorted publications
    private Collection<Publication> selectAndSort() {
        printer.printLine("Select sorting method: ");
        printer.printLine("1 - alphabetical order");
        printer.printLine("2 - sort by date");
        String option = null;
        boolean ischosen = false;
        Collection<Publication> sortedPublication = null;
        do {
            try {
                option = dataReader.getString();
                if (option.equals("1")) {
                    ischosen = true;
                    sortedPublication = library.getSortedPublications(new AlphabeticalTitleComparator());
                    return sortedPublication;
                } else if (option.equals("2")){
                    ischosen = true;
                    sortedPublication = library.getSortedPublications(new DateComparator());
                    return sortedPublication;
                } else {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                printer.printLine("Wrong option. Type \"1\" or \"2\" ");
            }
        } while (!ischosen);
        return sortedPublication;
    }

}
