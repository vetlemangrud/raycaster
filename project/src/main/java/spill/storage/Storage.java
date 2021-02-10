package spill.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.management.InvalidAttributeValueException;

public class Storage implements StorageInteface {

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

    private Path getSavePath(String name) {
        return getGameUserFolderPath().resolve(name + "." + SAVE_EXTENSION);
    }

    private String validatePropertyName(String property) throws IllegalArgumentException {
        Pattern propertyPattern = Pattern.compile("^(?:[A-Z]|\\_)+$");
        if (!propertyPattern.matcher(property).find()) {
            throw new IllegalArgumentException("Storage property names can only contain capital letters A-Z and underscores (_) Must contain at least one character");
        }
        return property;
    }


    @Override
    public String readData(InputStream is, String property) {
        validatePropertyName(property);
        try (Scanner scanner = new Scanner(is)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(property + PROPERY_VALUE_DIVIDER)) {
                    return line.substring(property.length() + 1);
                }
            }
        }
        return null;
    }

    @Override
    public String readData(int saveId, String property) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void writeData(OutputStream os, String property, String value) {
        // TODO Auto-generated method stub

    }

    @Override
    public void writeData(int saveId, String property, String value) throws IOException {
        // TODO Auto-generated method stub

    }
    
}
