package io.file;

import exception.DataExportException;
import exception.DataImportException;
import exception.InvalidDataException;
import model.*;

import java.io.*;
import java.util.Collection;
import java.util.Scanner;

public class CsvFileManager implements FileManager {
    private static final String PUBLICATIONS_FILE_NAME = "Library.csv";
    private static final String USERS_FILE_NAME = "Library_users.csv";

    // EXPORTING
    @Override
    public void exportData(Library library) {
        exportPublications(library);
        exportUsers(library);
    }

    private void exportPublications(Library library) {
        Collection<Publication> publications = library.getPublications().values();
        exportToCsv(publications, PUBLICATIONS_FILE_NAME);
    }

    private void exportUsers(Library library) {
        Collection<LibraryUser> users = library.getUsers().values();
        exportToCsv(users, USERS_FILE_NAME);
    }

    private <T extends CsvConvertible> void exportToCsv(Collection<T> collection, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            for (T element : collection) {
                bufferedWriter.write(element.toCsv());
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            throw new DataExportException("Error writing data to file: " + fileName);
        }
    }
    // IMPORTING
    @Override
    public Library importData() {
        Library library = new Library();
        importPublications(library);
        importUsers(library);
        return library;
    }

    private void importPublications(Library library) {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(PUBLICATIONS_FILE_NAME))) {
            bufferedReader.lines()
                    .map(this::createObjectFromString)
                    .forEach(library::addPublication);
        } catch (FileNotFoundException e) {
            throw new DataImportException("No file: " + PUBLICATIONS_FILE_NAME);
        } catch (IOException e) {
            throw new DataImportException("Error reading file: " + PUBLICATIONS_FILE_NAME);
        }
    }
    // used for importing data
    private Publication createObjectFromString(String csvText) {
        String[] split = csvText.split(","); // default separator
        String type = split[0]; // determine: is pub. a book or magazine
        if(Book.TYPE.equals(type)) {
            return createBook(split);
        } else if(Magazine.TYPE.equals(type)) {
            return createMagazine(split);
        }
        throw new InvalidDataException("Invalid publication type: " + type);
    }

    private void importUsers(Library library) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(USERS_FILE_NAME))) {
            bufferedReader.lines()
                    .map(this::createUserFromString)
                    .forEach(library::addUser);
        } catch (FileNotFoundException e) {
            throw new DataImportException("No file:  " + USERS_FILE_NAME);
        } catch (IOException e) {
            throw new DataImportException("Error reading file: " + USERS_FILE_NAME);
        }

    }

    private LibraryUser createUserFromString(String csvText) {
        String[] split = csvText.split(",");
        String firstName = split[0];
        String lastName = split[1];
        String pesel = split[2];
        return new LibraryUser(firstName, lastName, pesel);
    }

    private Book createBook(String[] data) {
        String title = data[1];
        String publisher = data[2];
        int year = Integer.parseInt(data[3]);
        String author = data[4];
        int pages = Integer.parseInt(data[5]);
        String isbn = data[6];
        return new Book(title, author, year, pages, publisher, isbn);
    }

    private Magazine createMagazine(String[] data) {
        String title = data[1];
        String publisher = data[2];
        int year = Integer.parseInt(data[3]);
        int month = Integer.parseInt(data[4]);
        int day = Integer.parseInt(data[5]);
        String language = data[6];
        return new Magazine(title, publisher, language, year, month, day);
    }
}
