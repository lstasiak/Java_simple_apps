package app;

import exception.NoSuchOptionException;

enum Option {
    EXIT(0, "Exit the library"),
    ADD_BOOK(1, "Add the book to the library"),
    ADD_MAGAZINE(2,"Add the magazine to the library"),
    PRINT_BOOKS(3, "Print valid books"),
    PRINT_MAGAZINES(4, "Print valid magazines");

    private int value;
    private String description;

    public int getValue() {
        return value;
    }
    public String getDescription() {
        return description;
    }

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

