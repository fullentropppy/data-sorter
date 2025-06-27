package ru.dgritsenko.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Application {
    private static List<String> sourcePaths;
    private static String resultPath;
    private static String filePrefix;
    /**
     * <ul>
     *  <li>{@code type1} - режим 1</li>
     *  <li>{@code type2} - режим 2</li>
     * </ul>
     */
    private static String fileWritingMode;
    private static boolean simpleStatistic;
    private static boolean fullStatistic;

    /**
     * Возможные параметры запуска:
     * <ul>
     *  <li> {@code -o} - путь для результатов {@code (-o /some/path)}</li>
     *  <li> {@code -p} - префикс для выходных файлов {@code (-p result_)}</li>
     *  <li> {@code -a} - режим добавления в существующие файлы {@code (-a type1)}</li>
     *  <li> {@code -s} - краткая статистика</li>
     *  <li> {@code -f} - полная статистика</li>
     * </ul>
     */
    public static void main(String[] args) {
        sourcePaths = new ArrayList<>();
        resultPath = "";
        filePrefix = "";
        fileWritingMode = "";

        List<String> params = new ArrayList<>(Arrays.asList(args));
        parseArgs(params);

        System.out.println("=== Пути к файлам:");
        for (String path : sourcePaths) {
            System.out.println(path);
        }

        System.out.println("=== Путь к результатам:");
        System.out.println(resultPath);

        System.out.println("=== Префикс файлов:");
        System.out.println(filePrefix);

        System.out.println("=== Тип записи:");
        System.out.println(fileWritingMode);

        System.out.println("=== Простая статистика:");
        System.out.println(simpleStatistic);

        System.out.println("=== Полная статистика:");
        System.out.println(fullStatistic);

        List<String> file1 = new ArrayList<>();
        file1.add("Нормальная форма числа с плавающей запятой");
        file1.add("15.28535047E-25");
        file1.add("Long");
        file1.add("1234567890123456789");

        sortInputFile(file1);
    }

    private static void parseArgs(List<String> args) {
        List<String> possibleParams = new ArrayList<>();
        possibleParams.add("-o");
        possibleParams.add("-p");
        possibleParams.add("-a");
        possibleParams.add("-s");
        possibleParams.add("-f");

//        args.add("-s");
//        args.add("-a");
//        args.add("-p");
//        args.add("sample-");
//        args.add("in1.txt");
//        args.add("in2.txt");

        for (int i = 0; i < args.size(); i++) {
            String arg = args.get(i);
            String nextArg = i == args.size() - 1 ? "" : args.get(i + 1);
            boolean isNextArgParam = possibleParams.contains(nextArg);

            if (arg.equals("-o") && !isNextArgParam) {
                resultPath = nextArg;
                i++;
            } else if (arg.equals("-p") && !isNextArgParam) {
                filePrefix = nextArg;
                i++;
            } else if (arg.equals("-a") && !isNextArgParam) {
                fileWritingMode = nextArg;
                i++;
            } else if (arg.equals("-s")) {
                simpleStatistic = true;
            } else if (arg.equals("-f")) {
                fullStatistic = true;
            } else if (!isNextArgParam && arg.endsWith(".txt")) {
                sourcePaths.add(arg);
            }
        }
    }

    private static void sortInputFile(List<String> inputFile) {
        List<String> lines = new ArrayList<>(inputFile);

        Pattern longPattern = Pattern.compile("^-?\\d+$");
        Pattern doublePattern = Pattern.compile("^-?\\d+\\.\\d+([eE][-+]?\\d+)?$|^-?\\d+[eE][-+]?\\d+$");

        List <Long> integers = new ArrayList<>();
        List <Double> floats = new ArrayList<>();
        List <String> strings = new ArrayList<>();

        for (String line : lines) {
            line = line.trim();

            if (longPattern.matcher(line).matches()) {
                integers.add(Long.parseLong(line));
            } else if (doublePattern.matcher(line).matches()) {
                floats.add(Double.parseDouble(line));
            } else {
                strings.add(line);
            }
        }

        for (long currentInteger : integers) {
            System.out.println("Целые числа:" + currentInteger);
        }

        for (Double currentFloat : floats) {
            System.out.println("Вещественные числа: " + currentFloat);
        }

        System.out.println("Строки: ");
        for (String currentString : strings) {
            System.out.print(currentString + ", ");
        }
    }
}