package io;

import model.*;

import java.util.Collection;

public class ConsolePrinter {

    public void printBooks(Collection<Publication> publications) {
        long counter = publications.stream()
                .filter(p-> p instanceof Book)
                .map(Publication::toString)
                .peek(this::printLine)
                .count();
        if (counter == 0) {
            printLine("There is no books in the library");
        }
    }
    public void printMagazines(Collection<Publication> publications) {
        long counter = publications.stream()
                .filter( p-> p instanceof Magazine)
                .map(Publication::toString)
                .peek(this::printLine)
                .count();
        if (counter == 0) {
            printLine("There is no magazines in the library");
        }
    }

    public void printUsers(Collection<LibraryUser> users) {
        users.stream()
                .map(User::toString)
                .forEach(this::printLine);
    }

    public void printLine(String text) {
        System.out.println(text);
    }
}
