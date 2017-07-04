import java.awt.*;
import java.awt.image.*;
public class DrawableObject
{
	int x;//in relation to its position in the frame
	int y;//in relation to its position in the frame
	int width;
	int height;
    public DrawableObject(int x, int y, int width, int height)
    {
    	this.x=x;
    	this.y=y;
    	this.width=width;
    	this.height=height;
    }
    public int getX()
    {
    	return x;
    }
    public int getY()
    {
    	return y;
    }
    public void update()
    {
    }
    public BufferedImage draw()
    {
    	return null;
    }
}