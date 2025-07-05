package ru.dgritsenko.app;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistic {

    private Sorter sorter;

    public Statistic(Sorter sorter) {
    }

    public static void printShortStatistic(Sorter sorter) {
        Map<String, Integer> statistic = getShortStatistic(sorter);
        String msg = MessageFormat.format(
                "*** Краткая статистика ***" +
                        "\nКоличество элементов типа Строка: {0}" +
                        "\nКоличество элементов типа Целое число: {1}" +
                        "\nКоличество элементов типа Вещественное число: {2}",
                statistic.get("String"),
                statistic.get("Longs"),
                statistic.get("Strings"));
        System.out.println(msg);
    }

    private static Map<String, Integer> getShortStatistic(Sorter sorter) {
        Map<String, Integer> statistic = new HashMap<>();
        statistic.put("Strings", sorter.getStrings().size());
        statistic.put("Longs", sorter.getLongs().size());
        statistic.put("Doubles", sorter.getDoubles().size());

        return statistic;
    }

    public static void printFullStatistic(Sorter sorter) {
        Map<String, Number> statistic = getFullStatistic(sorter.getStrings(), sorter.getLongs(), sorter.getDoubles());
        String msg = MessageFormat.format(
                "*** Полная статистика ***" +
                        "\nКоличество элементов типа Строка: {0}" +
                        "\nДлина самой короткой строки: {1}" +
                        "\nДлина самой длинной строки: {2}\n" +
                        "\nКоличество элементов типа Целое число: {3}" +
                        "\nЧисло с минимальным значением: {4}" +
                        "\nЧисло с максимальным значением: {5}" +
                        "\nСумма целых чисел: {6}" +
                        "\nСреднее целых чисел: {7}\n" +
                        "\nКоличество элементов типа Вещественное число: {8}" +
                        "\nЧисло с минимальным значением: {9}" +
                        "\nЧисло с максимальным значением: {10}" +
                        "\nСумма вещественных чисел: {11}" +
                        "\nСреднее вещественных чисел: {12}",
                statistic.get("Strings"),
                statistic.get("Strings min"),
                statistic.get("Strings max"),
                statistic.get("Longs"),
                statistic.get("Longs min"),
                statistic.get("Longs max"),
                statistic.get("Longs sum"),
                statistic.get("Longs average"),
                statistic.get("Doubles"),
                statistic.get("Doubles min"),
                statistic.get("Doubles max"),
                statistic.get("Doubles sum"),
                statistic.get("Doubles average"));
        System.out.println(msg);
    }

    private static Map<String, Number> getFullStatistic (List<String> strings, List<Long> longs, List<Double> doubles) {
        Map<String, Number> statistic = new HashMap<>();
        // Получение данных по строкам: min и max длина
        String shortestString = strings.getFirst();
        String longestString = strings.getFirst();
        int minLength = shortestString.length();
        int maxLength = longestString.length();

        for (String string : strings) {
            int length = string.length();
            if (length < minLength) {
                minLength = length;
            } else {
                if (length > maxLength) {
                    maxLength = length;
                }
            }
        }

        statistic.put("Strings", strings.size());
        statistic.put("Strings min", minLength);
        statistic.put("Strings max", maxLength);

        // Получение данных по целым числам: min, max, sum и average
        long sumLong = 0;
        long minLong = longs.getFirst();
        long maxLong = longs.getFirst();

        for (long number : longs) {
            sumLong += number;

            if (number < minLong) {
                minLong = number;
            } else if (number > maxLong) {
                maxLong = number;
            }
        }

        double averageLong = (double) sumLong / longs.size();
        statistic.put("Longs", longs.size());
        statistic.put("Longs min", minLong);
        statistic.put("longs max", maxLong);
        statistic.put("longs sum", sumLong);
        statistic.put("longs average", averageLong);

        // Получение данных по вещественным числам: min, max, sum и average
        double sum = 0;
        double min = doubles.getFirst();
        double max = doubles.getFirst();

        for (double number : doubles) {
            sum += number;

            if (number < min) {
                min = number;
            } else if (number > max) {
                max = number;
            }
        }

        double averageDouble = sum / doubles.size();
        statistic.put("Doubles", doubles.size());
        statistic.put("Doubles min", min);
        statistic.put("Doubles max", max);
        statistic.put("Doubles sum", sum);
        statistic.put("Doubles average", averageDouble);

        return statistic;
    }
}
