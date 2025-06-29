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

        setParams(args);

        List<String> inputtList = new ArrayList<>();

        for (String sourcePath : sourcePaths) {
            String fileFullName = sourcePath.contains(File.separator) ? sourcePath : currentDir + File.separator + sourcePath;
            loadToList(fileFullName, inputtList);
        }

        sortListAndSave(inputtList);
    }

    private static void setParams(String[] args) {
        // Параметры по умолчанию
        sourcePaths = new ArrayList<>();
        resultPath = currentDir;
        filePrefix = "";
        addToFile = false;

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
                addToFile = true;
            } else if (arg.equals("-s")) {
                simpleStatistic = true;
            } else if (arg.equals("-f")) {
                fullStatistic = true;
            } else if (arg.endsWith(".txt")) {
                sourcePaths.add(arg);
            }
        }
    }

    private static void sortListAndSave(List<String> list) {
        // Списки для отсортированных данных
        List <Long> longs = new ArrayList<>();
        List <Double> doubles = new ArrayList<>();
        List <String> strings = new ArrayList<>();

        // Паттерны для проверки данных на тип данных
        Pattern longPattern = Pattern.compile("^-?\\d+$");
        Pattern doublePattern = Pattern.compile("^-?\\d+\\.\\d+([eE][-+]?\\d+)?$|^-?\\d+[eE][-+]?\\d+$");

        for (String line : list) {
            line = line.trim();

            if (longPattern.matcher(line).matches()) {
                longs.add(Long.parseLong(line));
            } else if (doublePattern.matcher(line).matches()) {
                doubles.add(Double.parseDouble(line));
            } else {
                strings.add(line);
            }
        }

        // Сохранение отсортированных данных
        saveList(longs);
        saveList(doubles);
        saveList(strings);
    }

    private static void loadToList(String fullFileName, List<String> receiverList) {
        // На всякий случай
        if (receiverList == null) {
            receiverList = new ArrayList<>();
        }

        // Чтение файла
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
        // Проверка на необходимость сохранения
        if (list == null || list.isEmpty()) {
            return;
        }

        // Определение имени файла исходя из типов значения списка
        String dataType = list.getFirst().getClass().getSimpleName() + "s";
        String fullFileName = resultPath + File.separator + filePrefix + dataType.toLowerCase() + ".txt";

        // Запись файла/в файл
        try {
            FileWriter fileWriter = new FileWriter(fullFileName, addToFile);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            for (Object item : list) {
                writer.write(item.toString());
                writer.newLine();
            }

            writer.close();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}