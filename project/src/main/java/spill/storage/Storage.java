package spill.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Storage implements StorageInteface {

    @Override
    public String readData(InputStream is, String property) {
        // TODO Auto-generated method stub
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
