package spill.rendering;

import javafx.scene.canvas.GraphicsContext;
import spill.game.Game;

public abstract class Renderer {
    GraphicsContext gc;
    Game game;
    double canvasWidth;
    double canvasHeight;
    

    protected Renderer(GraphicsContext gc, Game gameContext, double canvasWidth, double canvasHeight) {
        this.gc = gc;
        this.game = gameContext;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }
    /**
	 * Render the scene
	 */
    public void render() {}
    /**
	 * Clear the scene
	 */
    public void clear() {}
}
