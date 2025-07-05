package ru.dgritsenko.app;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// todo: добавить разделители между методами (//---...)
// todo: отсортировать методы (https://sky.pro/wiki/java/standartnaya-sortirovka-metodov-v-java-uluchshenie-organizatsii/)
// todo: переписать ключи во всех мапах (выбрать один из стилей для всех: snake_case, camelCase, UPPER_SNAKE_CASE)
public class Statistic {
    private final Sorter sorter;

    public Statistic(Sorter sorter) {
        this.sorter = sorter;
    }

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

    private Map<String, Integer> getShortStatistic() {
        Map<String, Integer> statistic = new HashMap<>();
        statistic.put("Strings", sorter.getStrings().size());
        statistic.put("Longs", sorter.getLongs().size());
        statistic.put("Doubles", sorter.getDoubles().size());

        return statistic;
    }

    // todo: где то может быть выведено null - проверить, исправить
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
                statistic.get("Doubles average")
        );
        System.out.println(msg);
    }

    // todo: разбить метод на несколько, в каждом обрабатывать конкретный тип данных
    private Map<String, Number> getFullStatistic() {
        Map<String, Number> statistic = new HashMap<>();

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
        statistic.put("Strings min", minLength);
        statistic.put("Strings max", maxLength);

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
        statistic.put("Longs min", minLong);
        statistic.put("longs max", maxLong);
        statistic.put("longs sum", sumLong);
        statistic.put("longs average", averageLong);

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
        statistic.put("Doubles min", min);
        statistic.put("Doubles max", max);
        statistic.put("Doubles sum", sum);
        statistic.put("Doubles average", averageDouble);

        return statistic;
    }
}