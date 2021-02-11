package spill.storage;

import java.io.IOException;


public interface StorageInteface {
	/**
	 * Read the value of the property.
     * @param property The property to return.
	 * @return The value of the property.
	 * @throws IOException if the saveId is invalid
	 */
	String readSave(String property) throws IOException;
	/**
	 * Write the value to the property in the file. Creates new property if not in file.
     * @param property The property to write to
     * @param value The value to give the property
	 * @throws IOException If a file at the proper location can't be written to
	 */
	void writeSave(String property, String value) throws IOException;

	/**
	 * Deletes every property and value
	 * @throws IOException If a file at the proper location is not found
	 */
	void clear() throws IOException;

}
