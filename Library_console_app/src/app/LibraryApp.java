package app;

public class LibraryApp {
    final static String APP_NAME = "Library v1.1";

    public static void main(String[] args) {
        System.out.println(APP_NAME);
        LibraryControl libControl = new LibraryControl();
        libControl.controlLoop();
    }
}
