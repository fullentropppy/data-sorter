package ru.dgritsenko.app;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Application {
    private static String currentDir;

    private static List<String> sourcePaths;
    private static String resultPath;
    private static String filePrefix;
    private static boolean addToFile;
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
     *  <li> {@code .txt} - прочие параметры начинающиеся {@code .txt}
     *  воспринимаются как имена файлов для загрузки данных для сортировки.
     *  <ul>
     *      <li> если указано только имя файла, то будет выполнена попытка загрузить файл
     *      из каталога, где запущено приложение
     *      <li> если указано полное имя файла, будет выполнена попытка загрузить файл
     *      из с использованием полного имени
     *  </ul>
     * </ul>
     */
    public static void main(String[] args) {
        currentDir = System.getProperty("user.dir");

        // Параметры по умолчанию
        sourcePaths = new ArrayList<>();
        resultPath = currentDir;
        filePrefix = "";
        addToFile = false;

        List<String> params = new ArrayList<>(Arrays.asList(args));
        setParams(params);

        List<String> inputtList = new ArrayList<>();

        for (String sourcePath : sourcePaths) {
            String fileFullName = sourcePath.contains(File.separator) ? sourcePath : currentDir + File.separator + sourcePath;
            loadToList(fileFullName, inputtList);
        }

        sortListAndSave(inputtList);
    }

    private static void setParams(List<String> args) {
        List<String> possibleParams = new ArrayList<>();
        possibleParams.add("-o");
        possibleParams.add("-p");
        possibleParams.add("-a");
        possibleParams.add("-s");
        possibleParams.add("-f");

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
            } else if (arg.equals("-a")) {
                addToFile = true;
            } else if (arg.equals("-s")) {
                simpleStatistic = true;
            } else if (arg.equals("-f")) {
                fullStatistic = true;
            } else if (!isNextArgParam && arg.endsWith(".txt")) {
                sourcePaths.add(arg);
            }
        }
    }

    private static void sortListAndSave(List<String> list) {
        List<String> lines = new ArrayList<>(list);

        Pattern longPattern = Pattern.compile("^-?\\d+$");
        Pattern doublePattern = Pattern.compile("^-?\\d+\\.\\d+([eE][-+]?\\d+)?$|^-?\\d+[eE][-+]?\\d+$");

        List <Long> longs = new ArrayList<>();
        List <Double> doubles = new ArrayList<>();
        List <String> strings = new ArrayList<>();

        for (String line : lines) {
            line = line.trim();

            if (longPattern.matcher(line).matches()) {
                longs.add(Long.parseLong(line));
            } else if (doublePattern.matcher(line).matches()) {
                doubles.add(Double.parseDouble(line));
            } else {
                strings.add(line);
            }
        }

        saveList(longs);
        saveList(doubles);
        saveList(strings);
    }

    private static void loadToList(String fullFileName, List<String> receiverList) {
        if (receiverList == null) {
            receiverList = new ArrayList<>();
        }

        try {
            FileReader fileReader = new FileReader(fullFileName);
            BufferedReader reader = new BufferedReader(fileReader);

            String line;
            while ((line = reader.readLine()) != null) {
                receiverList.add(line);
            }

            reader.close();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static void saveList(List<?> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        String dataType = list.getFirst().getClass().getSimpleName() + "s";
        String fullFileName = resultPath + File.separator + filePrefix + dataType.toLowerCase() + ".txt";

        try {
            FileWriter fileWriter = new FileWriter(fullFileName);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            for (Object item : list) {
                writer.write(item.toString());
                writer.newLine();
            }

            writer.close();

            System.out.println("Данные успешно сохранен в файл: " + fullFileName);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}