package spill.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public interface StorageInteface {
    /**
	 * Read the value of a property from a stream
	 * @param is The input stream to read from.
     * @param property The property to return.
	 * @return The value of the property.
	 */
	String readData(InputStream is, String property);
	/**
	 * Read the value of a property in a specific save.
	 * @param saveId The id of the save
     * @param property The property to return.
	 * @return The value of the property.
	 * @throws IOException if the saveId is invalid
	 */
	String readData(int saveId, String property) throws IOException;
	
	/**
	 * Write the value to the property in the stream. Creates new property if not in file
     * @param os The stream to write to
	 * @param property The property to write to
     * @param value The value to give the property
	 * 
	 */
	void writeData(OutputStream os, String property, String value);
	/**
	 * Write the value to the property in the file with the given id. Creates new property if not in file. Creates new file if none with the given id.
	 * @param saveId The id of the file to write to
     * @param property The property to write to
     * @param value The value to give the property
	 * @throws IOException If a file at the proper location can't be written to
	 */
	void writeData(int saveId, String property, String value) throws IOException;

}
