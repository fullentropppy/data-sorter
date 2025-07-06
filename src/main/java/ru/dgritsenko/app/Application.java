package ru.dgritsenko.app;

import java.io.*;
import java.util.*;

// todo: добавить комментарии в код опционально (в этом классе и/или остальных)
// todo: по окончании разработки и тестирования написать Javadoc для всех методов
public class Application {
    private static final String currentDir;

    private static final List<String> sourcePaths;
    private static String resultPath;
    private static String filePrefix;
    private static boolean append;
    private static boolean showSimpleStatistic;
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
     *  <li> {@code -o} - путь для результатов {@code (-o /some/path)}
     *  <li> {@code -p} - префикс для выходных файлов {@code (-p result_)}
     *  <li> {@code -a} - режим добавления в существующие файлы вместо перезаписи
     *  <li> {@code -s} - краткая статистика
     *  <li> {@code -f} - полная статистика
     *  <li> {@code .txt} - прочие параметры, заканчивающиеся {@code .txt}
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
        setParams(args);
        sortData();
    }

    //------------------------------------------------------------------------------------------------------------------
    // Метод для сортировки данных
    //------------------------------------------------------------------------------------------------------------------

    private static void sortData() {
        List<String> inputtList = new ArrayList<>();

        FileDataService fileDataService = new FileDataService();
        fileDataService.setSavingParameters(resultPath, filePrefix, append);

        for (String sourcePath : sourcePaths) {
            String fileFullName = sourcePath.contains(File.separator) ? sourcePath : currentDir + File.separator + sourcePath;
            fileDataService.loadToList(fileFullName, inputtList);
        }

        Sorter sorter = new Sorter();
        sorter.sortStringsToCollections(inputtList);

        Statistic statistic = new Statistic(sorter);

        if (showSimpleStatistic){
            statistic.printShortStatistic();
        } else if (showFullStatistic) {
            statistic.printFullStatistic();
        }

        fileDataService.saveList(sorter.getStrings());
        fileDataService.saveList(sorter.getDoubles());
        fileDataService.saveList(sorter.getLongs());
    }

    //------------------------------------------------------------------------------------------------------------------
    // Метод для установки параметров
    //------------------------------------------------------------------------------------------------------------------

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
                showSimpleStatistic = true;
            } else if (arg.equals("-f")) {
                showFullStatistic = true;
            } else if (arg.endsWith(".txt")) {
                sourcePaths.add(arg);
            }
        }
    }
}