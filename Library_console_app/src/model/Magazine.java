package model;

import java.time.MonthDay;

public class Magazine extends Publication {
    public static final String TYPE = "Magazine";
    private MonthDay monthDay;
    private String language;

    public Magazine(String title, String publisher, String language, int year, int month, int day) {
        super(title, publisher, year);
        this.monthDay = MonthDay.of(month, day);
        this.language = language;
    }

    public MonthDay getMonthDay() {
        return monthDay;
    }

    public void setMonthDay(MonthDay monthDay) {
        this.monthDay = monthDay;
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
                "date: " + monthDay.getDayOfMonth() + "-" + monthDay.getMonthValue() + "-" + getYear() + "\n" +
                "language: " + language + "\n";
    }

    @Override
    public String toCsv() {
        return (TYPE + ",") +
                getTitle() + "," +
                getPublisher() + "," +
                getYear() + "," +
                monthDay.getMonthValue() + "," +
                monthDay.getDayOfMonth() + "," +
                language + "";
    }

}
