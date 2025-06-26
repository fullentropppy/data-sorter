package ru.dgritsenko.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
}