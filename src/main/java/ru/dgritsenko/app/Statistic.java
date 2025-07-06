package ru.dgritsenko.app;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistic {
    private final Sorter sorter;

    //------------------------------------------------------------------------------------------------------------------
    // Конструкторы
    //------------------------------------------------------------------------------------------------------------------

    public Statistic(Sorter sorter) {
        this.sorter = sorter;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Вывод в консоль статистики
    //------------------------------------------------------------------------------------------------------------------

    public void printShortStatistic() {
        Map<String, Integer> statistic = getShortStatistic();

        String msg = MessageFormat.format(
                "\n*** Краткая статистика ***" +
                        "\nКоличество элементов типа Строка: {0}" +
                        "\nКоличество элементов типа Целое число: {1}" +
                        "\nКоличество элементов типа Вещественное число: {2}\n",
                statistic.get("strings_size"),
                statistic.get("longs_size"),
                statistic.get("doubles_size")
        );
        System.out.println(msg);
    }

    public void printFullStatistic() {
        Map<String, Number> statistic = getFullStatistic();

        // Не все double выводятся корректно (например 1.528535047E-25).
        // Для корректного вывода long и double заранее приведены к строке
        String msg = MessageFormat.format(
                "\n*** Полная статистика ***" +
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
                statistic.get("strings_size").toString(),
                statistic.get("strings_min").toString(),
                statistic.get("strings_max").toString(),
                statistic.get("longs_size").toString(),
                statistic.get("longs_min").toString(),
                statistic.get("longs_max").toString(),
                statistic.get("longs_sum").toString(),
                statistic.get("longs_average").toString(),
                statistic.get("doubles_size").toString(),
                statistic.get("doubles_min").toString(),
                statistic.get("doubles_max").toString(),
                statistic.get("doubles_sum").toString(),
                statistic.get("doubles_average").toString()
        );
        System.out.println(msg);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Вспомогательные методы для получения статистики
    //------------------------------------------------------------------------------------------------------------------

    private Map<String, Integer> getShortStatistic() {
        Map<String, Integer> statistic = new HashMap<>();

        statistic.put("strings_size", sorter.getStrings().size());
        statistic.put("longs_size", sorter.getLongs().size());
        statistic.put("doubles_size", sorter.getDoubles().size());

        return statistic;
    }

    private Map<String, Number> getFullStatistic() {
        Map<String, Number> statistic = new HashMap<>();

        calculateStringStatistic(statistic);
        calculateLongStatistic(statistic);
        calculateDoubleStatistic(statistic);

        return statistic;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Вспомогательные методы для получения данных по полной статистике
    //------------------------------------------------------------------------------------------------------------------

    private void calculateStringStatistic(Map<String, Number> statistic) {
        // Получение данных по строкам: min и max размер
        List<String> strings = sorter.getStrings();

        int minLength = Integer.MAX_VALUE;
        int maxLength = Integer.MIN_VALUE;

        for (String string : strings) {
            int length = string.length();
            if (length < minLength) {
                minLength = length;
            }

            if (length > maxLength) {
                maxLength = length;
            }
        }

        int size = strings.size();

        if (size == 0) {
            minLength = 0;
            maxLength = 0;
        }

        statistic.put("strings_size", size);
        statistic.put("strings_min", minLength);
        statistic.put("strings_max", maxLength);
    }

    private void calculateLongStatistic(Map<String, Number> statistic) {
        // Получение данных по целым числам: min, max, sum и average
        List<Long> longs = sorter.getLongs();

        int size = longs.size();
        long sum = 0;
        long min = 0;
        long max = 0;

        if (size > 0) {
            min = longs.getFirst();
            max = min;
        }

        for (long number : longs) {
            sum += number;

            if (number < min) {
                min = number;
            } else if (number > max) {
                max = number;
            }
        }

        double averageLong = size > 0 ? (double) sum / size : 0;

        statistic.put("longs_size", size);
        statistic.put("longs_min", min);
        statistic.put("longs_max", max);
        statistic.put("longs_sum", sum);
        statistic.put("longs_average", averageLong);
    }

    private void calculateDoubleStatistic(Map<String, Number> statistic) {
        // Получение данных по вещественным числам: min, max, sum и average
        List<Double> doubles = sorter.getDoubles();

        int size = doubles.size();
        double sum = 0;
        double min = 0;
        double max = 0;

        if (size > 0) {
            min = doubles.getFirst();
            max = min;
        }

        for (double number : doubles) {
            sum += number;

            if (number < min) {
                min = number;
            } else if (number > max) {
                max = number;
            }
        }

        if (size == 0) {
            min = 0;
            max = 0;
        }

        double averageDouble = size > 0 ? sum / size : 0;

        statistic.put("doubles_size", size);
        statistic.put("doubles_min", min);
        statistic.put("doubles_max", max);
        statistic.put("doubles_sum", sum);
        statistic.put("doubles_average", averageDouble);
    }
}