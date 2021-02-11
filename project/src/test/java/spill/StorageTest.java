package spill;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spill.storage.Storage;

public class StorageTest {

    private Storage storage;

    @BeforeEach
    public void setUp() {
        storage = new Storage(-1);
        try {
            storage.clear();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        
    }

    private void testInvalidPropertyName(String propertyName){
        assertThrows(IllegalArgumentException.class, () -> {
            storage.readSave(propertyName);
        });
    }

    private void testInvalidValueName(String valueName){
        assertThrows(IllegalArgumentException.class, () -> {
            storage.writeSave("TEST", valueName);
        });
    }

    @Test
    void testValidProperties(){
        testInvalidPropertyName("abc");
        testInvalidPropertyName("");
        testInvalidPropertyName("A BC");
        testInvalidPropertyName("AB3");
        testInvalidPropertyName("ABC=");
        testInvalidPropertyName("ABC=CBA");
        testInvalidPropertyName("ABC\n");
        testInvalidPropertyName("-ABC");
        testInvalidPropertyName(" ABC");
        assertDoesNotThrow(() -> {
            storage.readSave("ABC");
            storage.readSave("A_BC");
        });
    }

    @Test
    void testValidNames(){
        testInvalidValueName("");
        testInvalidValueName("\n");
        testInvalidValueName("\n\n\n");
    }

    @Test
    void testSaveRead(){
        assertDoesNotThrow(() -> {
            storage.writeSave("ABC", "CBA");
        });
        assertSame("CBA", storage.readSave("ABC"));
    }

    @Test
    void testSeparateSaves(){
        testSaveRead();
        Storage other = new Storage(-2);
        assertDoesNotThrow(() -> {
            other.writeSave("ABC", "OTHER");
        });
        assertSame("CBA", storage.readSave("ABC"));
        assertSame("OTHER", other.readSave("ABC"));
    }

    @Test
    void testClear(){
        assertDoesNotThrow(() -> {
            storage.writeSave("ABC", "CBA");
        });
        assertDoesNotThrow(() -> {
            storage.clear();
        });
        assertSame(null, storage.readSave("ABC"));

    }

}
