import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        GameProgress gameProgress1 = new GameProgress(100, 2, 1, 15);
        GameProgress gameProgress2 = new GameProgress(96, 3, 2, 34);
        GameProgress gameProgress3 = new GameProgress(95, 4, 3, 55);

        String homeDir = "D://Games/savegames";
        // Сериализуем объекты GameProgress в папку savegames
        saveGame(homeDir + "/save1.dat", gameProgress1);
        saveGame(homeDir + "/save2.dat", gameProgress2);
        saveGame(homeDir + "/save3.dat", gameProgress3);
        // Созданные файлы из папки savegames запаковываем в архив zip
        List<String> filePaths = new ArrayList<>();
        filePaths.add(homeDir + "/save1.dat");
        filePaths.add(homeDir + "/save2.dat");
        filePaths.add(homeDir + "/save3.dat");
        zipFiles(homeDir + "/zip.zip", filePaths);
        // Удаляем файлы сохранений, лежащие вне архива
        for (String fileForDel : filePaths) {
            if (new File(fileForDel).delete()) {
                System.out.println(fileForDel + " : file deleted successfully");
            } else {
                System.out.println(fileForDel + " : file not deleted");
            }
        }
    }

    public static void saveGame(String filePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String zipPath, List<String> filePaths) {

        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath))) {

            for (String filePath : filePaths) {
                try (FileInputStream fis = new FileInputStream(filePath)) {
                    ZipEntry entry = new ZipEntry(new File(filePath).getName()); // в архиве путь целиком получается, некрасиво, но как иначе непонятно, ведь в метод путь целиком передаем
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}