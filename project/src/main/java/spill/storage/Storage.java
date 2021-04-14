package spill.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Storage implements StorageInteface {
    // Mye av koden her er hentet og modifisert fra eksempeloppgaven (TodoList)
    // Endringene kommer av at jeg har en annen måte å lagre data på. Dataene her er på formen "PROPERTY=value", der både property og value er vilkårlige strenger (Feks JSON)
    // Lagringssystemet blir da mer åpent og man er ikke låst til et strengt bruksområde
    // Har også laget et cachingsystem så man slipper å lese fila hver gang

    private static final String SAVE_EXTENSION = "gamesave";
    private static final String PROPERY_VALUE_DIVIDER = "=";

    private int saveId; //Id of the save to store to and read from

    private HashMap<String,String> cache; //The cache is a mirror of the file

    public Storage(int saveId){
        this.saveId = saveId;
        cache = loadCache();
    }

    // Gets path to saves
    private static Path getGameUserFolderPath() {
        return Path.of(System.getProperty("user.home"), "tdt4100", "game");
    }

    //Returns false if no folder
    private static boolean ensureGameUserFolder() {
        try {
            Files.createDirectories(getGameUserFolderPath());
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }

    //Gets path to the save with the given id
    private static Path getSavePath(int id) {
        return getGameUserFolderPath().resolve("Save" + id + "." + SAVE_EXTENSION);
    }

    //Scans savefile and adds properties and values to the cache
    private HashMap<String,String> loadCache(){
        HashMap<String,String> cache = new HashMap<>();
        try (InputStream is = new FileInputStream(getSavePath(saveId).toFile())) {
            try (Scanner scanner = new Scanner(is)) {
                while (scanner.hasNextLine()) {
                   
                    String line = scanner.nextLine();
                    Pattern dataPattern = Pattern.compile("^(?<property>(?:[A-Z_])+)=(?<value>.+)$");
                    Matcher dataMatch = dataPattern.matcher(line);
                    if (dataMatch.find()) {
                        cache.put(dataMatch.group("property"), dataMatch.group("value"));
                    }
                    
                }
            }
        } catch (FileNotFoundException e) {
            
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return cache;
    }

    //Overrwrites the savefile with cache contents
    private void saveCache() throws IOException{
        var savePath = getSavePath(saveId);
        ensureGameUserFolder();
        try (OutputStream os = new FileOutputStream(savePath.toFile())) {
            try (PrintWriter writer = new PrintWriter(os)) {
                for (Map.Entry<String,String> property : cache.entrySet()) {
                    writer.println(property.getKey() + PROPERY_VALUE_DIVIDER + property.getValue());
                }
            }
        }
    }

    private String validatePropertyName(String property) throws IllegalArgumentException {
        Pattern propertyPattern = Pattern.compile("^(?:[A-Z_])+\\z");
        if (!propertyPattern.matcher(property).find()) {
            throw new IllegalArgumentException("Storage property names can only contain capital letters A-Z and underscores (_) Must contain at least one character");
        }
        return property;
    }

    private String validateValue(String value) throws IllegalArgumentException {
        Pattern valuePattern = Pattern.compile("(^$|\\n)");
        if (valuePattern.matcher(value).find()) {
            throw new IllegalArgumentException("Values cannot contain newline characters");
        }
        return value;
    }

    //Get the ids of all saves
    public static int[] getAllUsedIds(){
        File saveFolder = getGameUserFolderPath().toFile();
        File[] listOfSaves = saveFolder.listFiles();

        //Make Arraylist of all ids
        ArrayList<Integer> ids = new ArrayList<Integer>();
        Pattern filePattern = Pattern.compile("^Save(?<id>\\d+)\\."+SAVE_EXTENSION+"$");
        if (listOfSaves != null) {
            for (File save : listOfSaves) {
                Matcher fileMatch = filePattern.matcher(save.getName());
                if(fileMatch.find()){
                    ids.add(Integer.parseInt(fileMatch.group("id")));
                }
            }
        }
        

        //Turn ArrayList to Array when we know the size
        int[] idArray = new int[ids.size()];
        for (int i = 0; i < idArray.length; i++) {
            idArray[i] = ids.get(i);
        }

        return idArray;
    }

    @Override
    public String readSave(String property) {
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

    @Override
    public void clear() throws IOException{
        cache.clear();
        saveCache();
    }
}
