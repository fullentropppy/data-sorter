package ru.dgritsenko.datasorter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Sorter {
    private final List<String> strings;
    private final List<Long> longs;
    private final List<Double> doubles;

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
        return List.copyOf(strings);
    }

    public List<Long> getLongs() {
        return List.copyOf(longs);
    }

    public List<Double> getDoubles() {
        return List.copyOf(doubles);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Вспомогательные методы
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

            try {
                if (longPattern.matcher(line).matches()) {
                    longs.add(Long.parseLong(line));
                } else if (doublePattern.matcher(line).matches()) {
                    doubles.add(Double.parseDouble(line));
                } else {
                    strings.add(line);
                }
            } catch (NumberFormatException exception) {
                String errMsg = String.format(
                        "Строка ''{0}'' будет проигнорирована, т.к. не удалось применить паттерн: {1}",
                        line, exception.getMessage()
                );
                System.out.println(errMsg);
            }
        }
    }
}