package io.file;

import exception.NoSuchFileTypeException;
import io.ConsolePrinter;
import io.DataReader;

public class FileManagerBuilder {
    private ConsolePrinter printer;
    private DataReader reader;

    public FileManagerBuilder(ConsolePrinter printer, DataReader reader) {
        this.printer = printer;
        this.reader = reader;
    }
    // create and return CsvFileManager and SerializableFileManager objects
    public FileManager build() {
        printer.printLine("Select a data format:");
        FileType fileType = getFileType(); // enum
        switch (fileType) {
            case CSV:
                return new CsvFileManager();
            case SERIAL:
                return new SerializableFileManager();
            default:
                throw new NoSuchFileTypeException("Unsupported data type");
        }
    }
    // converts loaded String into type FileType
    private FileType getFileType() {
        boolean typeOk = false;
        FileType result = null;
        do {
            printTypes();
            String type = reader.getString().toUpperCase();
            try {
                result = FileType.valueOf(type);
                typeOk = true;
            } catch (IllegalArgumentException e) {
                printer.printLine("Unsupported data type, select again.");
            }
        } while (!typeOk);
        return result;
    }
    private void printTypes() {
        for (FileType value : FileType.values()) {
            printer.printLine(value.name());
        }
    }
}
