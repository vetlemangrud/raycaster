package spill.rendering;

import javafx.scene.canvas.GraphicsContext;

public class BirdseyeRenderer{
    GraphicsContext gc;
    public BirdseyeRenderer(GraphicsContext gc) {
        this.gc = gc;
    }

    public void render(){
        gc.fillRect(100, 100, 100, 200);
    }

}
