package app;

import java.util.ArrayList;
import java.util.Collection;

public class LibraryApp {
    final static String APP_NAME = "Library v2.1";

    public static void main(String[] args) {
        System.out.println(APP_NAME);
        LibraryControl libControl = new LibraryControl();
        libControl.controlLoop();

    }
}
