package ru.dgritsenko.app;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

public class Application {
    private static String currentDir;

    private static List<String> sourcePaths;
    private static String resultPath;
    private static String filePrefix;
    private static boolean append;
    private static boolean simpleStatistic;
    private static boolean fullStatistic;

    /**
     * Возможные параметры запуска:
     * <ul>
     *  <li> {@code -o} - путь для результатов {@code (-o /some/path)}
     *  <li> {@code -p} - префикс для выходных файлов {@code (-p result_)}
     *  <li> {@code -a} - режим добавления в существующие файлы вместо перезаписи
     *  <li> {@code -s} - краткая статистика
     *  <li> {@code -f} - полная статистика
     *  <li> {@code .txt} - прочие параметры, начинающиеся {@code .txt}
     *  воспринимаются как имена файлов для загрузки данных с последующей сортировкой
     *  <ul>
     *      <li> если указано только имя файла, то будет выполнена попытка загрузить файл
     *      из каталога, где запущено приложение
     *      <li> если указано полное имя файла, будет выполнена попытка загрузить файл
     *      с использованием полного имени
     *  </ul>
     * </ul>
     */
    public static void main(String[] args) {
        currentDir = System.getProperty("user.dir");

        // todo вынести весь следующий код метода в отдельный метод
        setParams(args);

        List<String> inputtList = new ArrayList<>();

        FileDataService fileDataService = new FileDataService();
        fileDataService.setSavingParameters(resultPath, filePrefix, append);

        for (String sourcePath : sourcePaths) {
            String fileFullName = sourcePath.contains(File.separator) ? sourcePath : currentDir + File.separator + sourcePath;
            fileDataService.loadToList(fileFullName, inputtList);
        }

        Sorter sorter = new Sorter();
        sorter.sortStringsToCollections(inputtList);

        printShortStatistic(sorter);
        fullStatistic(sorter);

        fileDataService.saveList(sorter.getStrings());
        fileDataService.saveList(sorter.getDoubles());
        fileDataService.saveList(sorter.getLongs());
    }

    private static void setParams(String[] args) {
        // Параметры по умолчанию
        // todo подумать над загрузкой файлов с пробелами в пути
        sourcePaths = new ArrayList<>();
        resultPath = currentDir;
        filePrefix = "";
        append = false;

        // Возможные параметры
        List<String> possibleParams = new ArrayList<>();
        possibleParams.add("-o");
        possibleParams.add("-p");
        possibleParams.add("-a");
        possibleParams.add("-s");
        possibleParams.add("-f");

        // Преобразование в ArrayList для удобства
        List<String> params = new ArrayList<>(Arrays.asList(args));

        // Парсинг параметров из строки запуска
        for (int i = 0; i < params.size(); i++) {
            String arg = params.get(i);
            String nextArg = i == params.size() - 1 ? "" : params.get(i + 1);
            boolean isNextArgParam = possibleParams.contains(nextArg);

            if (arg.equals("-o") && !isNextArgParam) {
                resultPath = nextArg;
                i++;
            } else if (arg.equals("-p") && !isNextArgParam) {
                filePrefix = nextArg;
                i++;
            } else if (arg.equals("-a")) {
                append = true;
            } else if (arg.equals("-s")) {
                simpleStatistic = true;
            } else if (arg.equals("-f")) {
                fullStatistic = true;
            } else if (arg.endsWith(".txt")) {
                sourcePaths.add(arg);
            }
        }
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

    public static void fullStatistic (Sorter sorter) {
        // Получение данных по строкам: мин и макс длина
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

        // Получение данных по вещественным числам: мин, макс, сумма и сред
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

        double average = sum / doubles.size();

        // Получение данных по целым числам: мин, макс, сумма и сред
        List<Long> longs = sorter.getLongs();

        long sumLong = 0;
        long minLong = longs.getFirst();
        long maxLong = longs.getFirst();

        for (long number : longs) {
            sumLong += number;

            if (number < min) {
                minLong = number;
            } else if (number > max) {
                maxLong = number;
            }
        }

        double averageLong = (double) sumLong / longs.size();
    }
}