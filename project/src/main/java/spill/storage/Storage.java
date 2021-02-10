package spill.storage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Storage implements StorageInteface {
    // Mye av koden her er hentet og modifisert fra eksempeloppgaven (TodoList)
    // Endringene kommer av at jeg har en annen måte å lagre data på. Dataene her er på formen "PROPERTY=value", der både property og value er vilkårlige strenger (Feks JSON)
    // Lagringssystemet blir da mer åpent og man er ikke låst til et strengt bruksområde

    public final static String SAVE_EXTENSION = "gamesave";
    public final static String PROPERY_VALUE_DIVIDER = "=";

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
        Pattern propertyPattern = Pattern.compile("^(?:[A-Z]|\\_)+$");
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
    public String readSave(InputStream is, String property) {
        validatePropertyName(property);
        try (Scanner scanner = new Scanner(is)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(property + PROPERY_VALUE_DIVIDER)) {
                    return line.substring(property.length() + 1); //The value of the property
                }
            }
        }
        return null;
    }

    @Override
    public String readSave(int saveId, String property) throws IOException {
        var savePath = getSavePath(saveId);
        try (var input = new FileInputStream(savePath.toFile())) {
            return readData(input, property);
        }
    }

    @Override
    public void writeSave(InputStream is, OutputStream os, String property, String value) {
        validatePropertyName(property);
        validateValue(value);
        try (var writer = new PrintWriter(os)) {
            try (Scanner scanner = new Scanner(is)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.startsWith(property + PROPERY_VALUE_DIVIDER)) {
                        line = line.substring(property.length() + PROPERY_VALUE_DIVIDER.length()) + value;
                    }
                    writer.println(line);
                }
            }
        }
    }

    @Override
    public void writeSave(int saveId, String property, String value) throws IOException {
        validatePropertyName(property);
        validateValue(value);
        var savePath = getSavePath(saveId);
        ensureGameUserFolder();
        try (var is = new FileInputStream(savePath.toFile())) {
            try (var os = new FileOutputStream(savePath.toFile())) {
                writeSave(is, os, property, value);
            }
        }

    }
    
}
