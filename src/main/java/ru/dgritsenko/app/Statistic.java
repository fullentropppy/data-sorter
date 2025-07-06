package ru.dgritsenko.app;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistic {
    private final Sorter sorter;

    //------------------------------------------------------------------------------------------------------------------
    // Конструктор
    //------------------------------------------------------------------------------------------------------------------

    public Statistic(Sorter sorter) {
        this.sorter = sorter;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Метод для вывода краткой статистики
    //------------------------------------------------------------------------------------------------------------------

    public void printShortStatistic() {
        Map<String, Integer> statistic = getShortStatistic();

        String msg = MessageFormat.format(
                "*** Краткая статистика ***" +
                        "\nКоличество элементов типа Строка: {0}" +
                        "\nКоличество элементов типа Целое число: {1}" +
                        "\nКоличество элементов типа Вещественное число: {2}\n",
                statistic.get("Strings"),
                statistic.get("Longs"),
                statistic.get("Doubles")
        );
        System.out.println(msg);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Метод для вывода полной статистики
    //------------------------------------------------------------------------------------------------------------------

    public void printFullStatistic() {
        Map<String, Number> statistic = getFullStatistic();

        String msg = MessageFormat.format(
                "*** Полная статистика ***" +
                        "\nКоличество элементов типа Строка: {0}" +
                        "\n- размер самой короткой: {1}" +
                        "\n- размер самой длинной: {2}\n" +
                        "\nКоличество элементов типа Целое число: {3}" +
                        "\n- меньшее: {4}" +
                        "\n- большее: {5}" +
                        "\n- сумма: {6}" +
                        "\n- среднее: {7}\n" +
                        "\nКоличество элементов типа Вещественное число: {8}" +
                        "\n- меньшее: {9}" +
                        "\n- большее: {10}" +
                        "\n- сумма: {11}" +
                        "\n- среднее: {12}\n",
                statistic.get("Strings"),
                statistic.get("Strings_min"),
                statistic.get("Strings_max"),
                statistic.get("Longs"),
                statistic.get("Longs_min"),
                statistic.get("Longs_max"),
                statistic.get("Longs_sum"),
                statistic.get("Longs_average"),
                statistic.get("Doubles"),
                statistic.get("Doubles_min"),
                statistic.get("Doubles_max"),
                statistic.get("Doubles_sum"),
                statistic.get("Doubles_average")
        );
        System.out.println(msg);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Вспомогательный метод для получения краткой статистики
    //------------------------------------------------------------------------------------------------------------------

    private Map<String, Integer> getShortStatistic() {
        Map<String, Integer> statistic = new HashMap<>();
        statistic.put("Strings", sorter.getStrings().size());
        statistic.put("Longs", sorter.getLongs().size());
        statistic.put("Doubles", sorter.getDoubles().size());

        return statistic;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Вспомогательный метод для получения полной статистики
    //------------------------------------------------------------------------------------------------------------------

    private Map<String, Number> getFullStatistic() {
        Map<String, Number> statistic = new HashMap<>();

        getStringStatistic(statistic);
        getLongStatistic(statistic);
        getDoubleStatistic(statistic);

        return statistic;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Вспомогательный метод для получения статистики по типу Строка
    //------------------------------------------------------------------------------------------------------------------

    private void getStringStatistic(Map<String, Number> statistic) {
        // Получение данных по строкам: min и max размер
        List<String> strings = sorter.getStrings();

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
        statistic.put("Strings_min", minLength);
        statistic.put("Strings_max", maxLength);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Вспомогательный метод для получения статистики по типу Целое число
    //------------------------------------------------------------------------------------------------------------------

    private void getLongStatistic(Map<String, Number> statistic) {
        // Получение данных по целым числам: min, max, sum и average
        List<Long> longs = sorter.getLongs();

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
        statistic.put("Longs_min", minLong);
        statistic.put("Longs_max", maxLong);
        statistic.put("Longs_sum", sumLong);
        statistic.put("Longs_average", averageLong);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Вспомогательный метод для получения статистики по типу Вещественное число
    //------------------------------------------------------------------------------------------------------------------

    private void getDoubleStatistic(Map<String, Number> statistic) {
        // Получение данных по вещественным числам: min, max, sum и average
        List<Double> doubles = sorter.getDoubles();

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
        statistic.put("Doubles_min", min);
        statistic.put("Doubles_max", max);
        statistic.put("Doubles_sum", sum);
        statistic.put("Doubles_average", averageDouble);
    }
}