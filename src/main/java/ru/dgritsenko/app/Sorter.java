package ru.dgritsenko.app;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Sorter {
    List<String> strings;
    List<Long> longs;
    List<Double> doubles;

    // -----------------------------------------------------------------------------------------------------------------
    // Конструкторы
    // -----------------------------------------------------------------------------------------------------------------

    public Sorter() {
        strings = new ArrayList<>();
        longs = new ArrayList<>();
        doubles = new ArrayList<>();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Геттеры
    // -----------------------------------------------------------------------------------------------------------------

    public List<String> getStrings() {
        return strings;
    }

    public List<Long> getLongs() {
        return longs;
    }

    public List<Double> getDoubles() {
        return doubles;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Прочие методы
    // -----------------------------------------------------------------------------------------------------------------

    public void sortStringsToCollections(List<String> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        // Паттерны для проверки данных на тип данных
        Pattern longPattern = Pattern.compile("^-?\\d+$");
        Pattern doublePattern = Pattern.compile("^-?\\d+\\.\\d+([eE][-+]?\\d+)?$|^-?\\d+[eE][-+]?\\d+$");

        for (String line : list) {
            line = line.trim();

            if (longPattern.matcher(line).matches()) {
                longs.add(Long.parseLong(line));
            } else if (doublePattern.matcher(line).matches()) {
                doubles.add(Double.parseDouble(line));
            } else {
                strings.add(line);
            }
        }
    }
}