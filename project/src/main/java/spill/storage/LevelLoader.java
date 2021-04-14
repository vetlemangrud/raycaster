package spill.storage;

import spill.game.Level;

public interface LevelLoader {
    /**
	 * Load a level.
     * @param number The level number
	 * @return The level
	 */
    Level load(int number);
}
