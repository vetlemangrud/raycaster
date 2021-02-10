package spill.storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Storage implements StorageInteface {
    // Mye av koden her er hentet og modifisert fra eksempeloppgaven (TodoList)
    // Endringene kommer av at jeg har en annen måte å lagre data på. Dataene her er på formen "PROPERTY=value", der både property og value er vilkårlige strenger (Feks JSON)
    // Lagringssystemet blir da mer åpent og man er ikke låst til et strengt bruksområde
    //Har også laget et cachingsystem så man slipper å lese fila hver gang

    private static final String SAVE_EXTENSION = "gamesave";
    private static final String PROPERY_VALUE_DIVIDER = "=";

    private int saveId;

    private HashMap<String,String> cache;

    public Storage(int saveId){
        this.saveId = saveId;
        cache = loadCache();
    }

    private HashMap<String,String> loadCache(){
        HashMap<String,String> cache = new HashMap<>();
        try (InputStream is = new FileInputStream(getSavePath(saveId).toFile())) {
            try (Scanner scanner = new Scanner(is)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    Pattern dataPattern = Pattern.compile("^(?<property>(?:[A-Z_])+)=(?<value>.+)$");
                    Matcher dataMatch = dataPattern.matcher(line);
                    dataMatch.find();
                    cache.put(dataMatch.group("property"), dataMatch.group("value"));
                }
            }
        } catch (FileNotFoundException e) {
            
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return cache;
    }

    private void saveCache() throws IOException{
        var savePath = getSavePath(saveId);
        ensureGameUserFolder();
        try (OutputStream os = new FileOutputStream(savePath.toFile())) {
            try (PrintWriter writer = new PrintWriter(os)) {
                for (String property : cache.keySet()) {
                    writer.println(property + PROPERY_VALUE_DIVIDER + cache.get(property));
                }
            }
        }
    }

    private Path getGameUserFolderPath() {
        return Path.of(System.getProperty("user.home"), "tdt4100", "game");
    }

    private boolean ensureGameUserFolder() {
        try {
            Files.createDirectories(getGameUserFolderPath());
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }

    private Path getSavePath(int id) {
        return getGameUserFolderPath().resolve("Save" + id + "." + SAVE_EXTENSION);
    }

    private String validatePropertyName(String property) throws IllegalArgumentException {
        Pattern propertyPattern = Pattern.compile("^(?:[A-Z]|_)+$");
        if (!propertyPattern.matcher(property).find()) {
            throw new IllegalArgumentException("Storage property names can only contain capital letters A-Z and underscores (_) Must contain at least one character");
        }
        return property;
    }

    private String validateValue(String value) throws IllegalArgumentException {
        Pattern valuePattern = Pattern.compile("\\n");
        if (valuePattern.matcher(value).find()) {
            throw new IllegalArgumentException("Values cannot contain newline characters");
        }
        return value;
    }

    @Override
    public String readSave(String property) throws IOException {
        validatePropertyName(property);
        return cache.get(property);
    }

    @Override
    public void writeSave(String property, String value) throws IOException {
        validatePropertyName(property);
        validateValue(value);
        cache.put(property, value);
        saveCache();
    }
}
