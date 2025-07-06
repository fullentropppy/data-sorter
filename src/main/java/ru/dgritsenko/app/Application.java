package ru.dgritsenko.app;

import java.io.*;
import java.util.*;

public class Application {
    private static final String currentDir;

    private static final List<String> sourcePaths;
    private static String resultPath;
    private static String filePrefix;
    private static boolean append;
    private static boolean showShortStatistic;
    private static boolean showFullStatistic;

    static {
        currentDir = System.getProperty("user.dir");

        // Параметры по умолчанию
        sourcePaths = new ArrayList<>();
        resultPath = currentDir;
        filePrefix = "";
        append = false;
    }

    /**
     * Возможные параметры запуска:
     * <ul>
     *  <li> {@code -o} - путь для результатов {@code (-o /some/path)}.
     *  <p>Может быть указан как полный, так и относительный путь
     *  <li> {@code -p} - префикс для выходных файлов {@code (-p result_)}.
     *  <li> {@code -a} - режим добавления в существующие файлы вместо перезаписи
     *  <li> {@code -s} - краткая статистика
     *  <li> {@code -f} - полная статистика
     *  <li> {@code .txt} - прочие параметры, заканчивающиеся {@code .txt}
     *  воспринимаются как имена файлов для загрузки данных с последующей сортировкой.
     *  <p>Может быть указан как полный, так и относительный путь
     * </ul>
     */
    public static void main(String[] args) {
        setParams(args);

        if (sourcePaths.isEmpty()) {
            System.out.println("В параметрах запуска не указан ни один txt-файл-источник данных");
        } else {
            sortData();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // Вспомогательные методы
    //------------------------------------------------------------------------------------------------------------------

    private static void sortData() {
        List<String> inputtList = new ArrayList<>();

        // Загрузка данных из файлов
        FileDataService fileDataService = new FileDataService();
        fileDataService.setSavingParameters(resultPath, filePrefix, append);

        for (String sourcePath : sourcePaths) {
            fileDataService.loadToList(sourcePath, inputtList);
        }

        // Сортировка данных
        Sorter sorter = new Sorter();
        sorter.sortStringsToCollections(inputtList);

        // Вывод статистики по отсортированным данным
        Statistic statistic = new Statistic(sorter);
        if (showFullStatistic) {
            statistic.printFullStatistic();
        } else if (showShortStatistic) {
            statistic.printShortStatistic();
        }

        // Сохранение отсортированных данных
        fileDataService.saveList(sorter.getStrings());
        fileDataService.saveList(sorter.getDoubles());
        fileDataService.saveList(sorter.getLongs());
    }

    private static void setParams(String[] args) {
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
                showShortStatistic = true;
            } else if (arg.equals("-f")) {
                showFullStatistic = true;
            } else if (arg.endsWith(".txt")) {
                sourcePaths.add(arg);
            }
        }
    }
}