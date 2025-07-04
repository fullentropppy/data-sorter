package ru.dgritsenko.app;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

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

        fileDataService.saveList(sorter.getStrings());
        fileDataService.saveList(sorter.getDoubles());
        fileDataService.saveList(sorter.getLongs());

        List<Long> longList = List.of(
                100L,
                -42L,
                -1_000_000L);

        List<String> stringList = List.of(
                "Hello, world!",
                "Java 17",
                "Stream API",
                "Генерация данных",
                "Пример строки"
        );

        List<Double> doubleList = List.of(
                3.14,
                -2.718,
                0.0,
                -1.5,
                1.61803398875,
                -0.001,
                2.99792458e8
        );

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

    public void shortStatistic (Sorter sorter) {
        List<String> strings = sorter.getStrings();
        if (strings == null || strings.isEmpty())
            return;
        int stringsSize = strings.size();

        List<Long> longs = sorter.getLongs();
        if (longs == null || longs.isEmpty())
            return;
        int longsSize = longs.size();

        List<Double> doubles = sorter.getDoubles();
        if (doubles == null || doubles.isEmpty())
            return;
        int doublesSize = doubles.size();
    }

    public void fullStatistic (Sorter sorter) {
        shortStatistic(sorter);

        List<String> strings = sorter.getStrings();
        if (strings == null || strings.isEmpty())
            return;

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

        List<Double> doubles = sorter.getDoubles();
        if (doubles == null || doubles.isEmpty())
            return;

        double sum = 0;
        double min = doubles.getFirst();
        double max = doubles.getFirst();

        for (double number : doubles) {
            sum += number;

            if (number < min) {
                min = number;
            }
            if (number > max) {
                max = number;
            }
        }

        double average = sum / doubles.size();

        List<Long> longs = sorter.getLongs();
        if (longs == null || longs.isEmpty())
            return;

        long sumLong = 0;
        long minLong = longs.getFirst();
        long maxLong = longs.getFirst();

        for (long number : longs) {
            sumLong += number;

            if (number < min) {
                minLong = number;
            }
            if (number > max) {
                maxLong = number;
            }
        }

        long averageLong = sumLong / longs.size();
    }
}