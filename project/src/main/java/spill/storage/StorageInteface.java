package spill.storage;

import java.io.IOException;


public interface StorageInteface {
	/**
	 * Read the value of a property in a specific save.
	 * @param saveId The id of the save
     * @param property The property to return.
	 * @return The value of the property.
	 * @throws IOException if the saveId is invalid
	 */
	String readSave(String property) throws IOException;
	/**
	 * Write the value to the property in the file with the given id. Creates new property if not in file. Creates new file if none with the given id.
	 * @param saveId The id of the file to write to
     * @param property The property to write to
     * @param value The value to give the property
	 * @throws IOException If a file at the proper location can't be written to
	 */
	void writeSave(String property, String value) throws IOException;

	void clear() throws IOException;

}
