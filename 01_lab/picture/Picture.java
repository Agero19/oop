/**
 * This class represents a simple picture. You can draw the picture using
 * the draw method. But wait, there's more: being an electronic picture, it
 * can be changed. You can set it to black-and-white display and back to
 * colors (only after it's been drawn, of course).
 *
 * This class was written as an early example for teaching Java with BlueJ.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 2006.03.30
 */
public class Picture {
    private Square wall;
    private Square window;
    private Triangle roof;
    private Circle sun;

    /**
     * Constructor for objects of class Picture.
     */
    public Picture() {
        // nothing to do... instance variables are automatically set to null
    }

    /**
     * Draw this picture.
     */
    public void draw() {
        wall = new Square();
        wall.moveVertical(80);
        wall.changeSize(100);
        wall.makeVisible();
        
        window = new Square();
        window.changeColor("black");
        window.moveHorizontal(20);
        window.moveVertical(100);
        window.makeVisible();

        roof = new Triangle();  
        roof.changeSize(50, 140);
        roof.moveHorizontal(60);
        roof.moveVertical(70);
        roof.makeVisible();

        sun = new Circle();
        sun.changeColor("yellow");
        sun.moveHorizontal(180);
        sun.moveVertical(+20);
        sun.changeSize(60);
        sun.makeVisible();
    }

    /**
     * Change this picture to black/white display.
     */
    public void setBlackAndWhite() {
        if (wall != null) {  // only if it's painted already...
            wall.changeColor("black");
            window.changeColor("white");
            roof.changeColor("black");
            sun.changeColor("black");
        }
    }

    /**
     * Change this picture to use color display.
     */
    public void setColor() {
        if (wall != null) {  // only if it's painted already...
            wall.changeColor("red");
            window.changeColor("black");
            roof.changeColor("green");
            sun.changeColor("yellow");
        }
    }
    
    public void sunset() {
        draw();
        setBlackAndWhite();
        sun.makeInvisible();
        
        Canvas canvas = Canvas.getCanvas();
        canvas.wait(1000);
        
        sun.moveHorizontal(180);
        sun.moveVertical(-10);
        sun.makeVisible();
        setColor();
        
        for (int i = 0; i < 100; i++) {
            if (sun.getX() >= 150) {
                sun.slowMoveHorizontal(-4);
                sun.slowMoveVertical(-2);
                canvas.wait(50);
            } else {
                sun.slowMoveHorizontal(-4);
                sun.slowMoveVertical(+2);
                canvas.wait(50);    
            }
        }
        
        sun.makeInvisible();
        setBlackAndWhite();
    }
}
