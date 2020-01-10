package model;

public class Magazine extends Publication {
    public static final String TYPE = "Magazine";
    private int month;
    private int day;
    private String language;

    public Magazine(String title, String publisher, String language, int year, int month, int day) {
        super(title, publisher, year);
        this.month = month;
        this.day = day;
        this.language = language;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "title: " + getTitle() + "\n" +
                "publisher: " + getPublisher() + "\n" +
                "date: " + day + "." + month + "." + getYear() + "\n" +
                "language: " + language + "\n";
    }

    @Override
    public String toCsv() {
        return (TYPE + ",") +
                getTitle() + "," +
                getPublisher() + "," +
                getYear() + "," +
                month + "," +
                day + "," +
                language + "";
    }

}
