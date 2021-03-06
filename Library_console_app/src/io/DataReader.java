package io;

import model.Book;
import model.LibraryUser;
import model.Magazine;
import java.util.Scanner;

public class DataReader {
    private Scanner sc = new Scanner(System.in);
    private ConsolePrinter printer;

    public DataReader(ConsolePrinter printer) {
        this.printer = printer;
    }
    public String getString() {
        return sc.nextLine();
    }

    public void close(){
        sc.close();
    }

    public int getInt() {
        try {
            return sc.nextInt();
        } finally {
            sc.nextLine();
        }
    }

    public Book readAndCreateBook() {

        System.out.println("Title: ");
        String title = sc.nextLine();
        System.out.println("Author: ");
        String  author = sc.nextLine();
        System.out.println("Publisher: ");
        String publisher = sc.nextLine();
        System.out.println("ISBN: ");
        String isbn = sc.nextLine();
        System.out.println("Release date: ");
        int releaseDate = getInt();
        System.out.println("Pages: ");
        int pages = getInt();

        return new Book(title, author, releaseDate, pages, publisher, isbn);
    }

    public Magazine readAndCreateMagazine() {
        System.out.println("Title: ");
        String title = sc.nextLine();
        System.out.println("Publisher: ");
        String publisher = sc.nextLine();
        System.out.println("Language: ");
        String language = sc.nextLine();
        System.out.println("Year: ");
        int year = getInt();
        System.out.println("Month: ");
        int month = getInt();
        System.out.println("Day: ");
        int day = getInt();
        return new Magazine(title, publisher, language, year, month, day);
    }

    public LibraryUser createLibraryUser() {
        printer.printLine("First Name: ");
        String firstName = sc.nextLine();
        printer.printLine("Last Name: ");
        String lastName = sc.nextLine();
        printer.printLine("PESEL: ");
        String pesel = sc.nextLine();
        return new LibraryUser(firstName, lastName, pesel);
    }
}
