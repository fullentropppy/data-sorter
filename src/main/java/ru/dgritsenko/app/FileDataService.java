package ru.dgritsenko.app;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class FileDataService {
    private final String currentDir;
    private String resultPath;
    private String filePrefix;
    private boolean append;

    // -----------------------------------------------------------------------------------------------------------------
    // Конструкторы
    // -----------------------------------------------------------------------------------------------------------------

    public FileDataService() {
        currentDir = System.getProperty("user.dir");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Сеттеры
    // -----------------------------------------------------------------------------------------------------------------

    public void setSavingParameters(String resultPath, String filePrefix, boolean append) {
        this.resultPath = resultPath;
        this.filePrefix = filePrefix;
        this.append = append;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Прочие методы
    // -----------------------------------------------------------------------------------------------------------------

    public void loadToList(String fullFileName, List<String> receiverList) {
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
        } catch (Exception ex) {
            String errMsg = MessageFormat.format(
                    "Не удалось загрузить файл ''{0}'': {1}",
                    fullFileName, ex.getMessage()
            );
            System.out.println(errMsg);
        }
    }

    public void saveList(List<?> list) {
        // Проверка на необходимость сохранения
        if (list == null || list.isEmpty()) {
            return;
        }

        // Определение имени файла исходя из типов значения списка
        String dataTypeName = list.getFirst().getClass().getSimpleName() + "s";
        String fullFileName = resultPath + File.separator + filePrefix + dataTypeName.toLowerCase() + ".txt";

        // Запись файла/в файл
        try {
            FileWriter fileWriter = new FileWriter(fullFileName, append);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            for (Object item : list) {
                writer.write(item.toString());
                writer.newLine();
            }

            writer.close();
        } catch (Exception exception) {
            String errMsg = MessageFormat.format(
                    "Не удалось сохранить файл ''{0}'': {1}",
                    fullFileName, exception.getMessage()
            );
            System.out.println(errMsg);
        }
    }
}