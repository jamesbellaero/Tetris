import java.awt.*;
import java.awt.image.*;


public class FrameBorder extends DrawableObject
{
	int x;
	int y;
	int height;
	int width;
	int girth;
    public FrameBorder(int x, int y, int width, int height)
    {
    	super(x,y,width,height);
    	this.x=x;
    	this.y=y;
    	this.height=height;
    	this.width=width;
    	girth=5;
    }
    public BufferedImage draw()
    {
    	BufferedImage img=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    	Graphics2D graphics=img.createGraphics();
    	graphics.setColor(Color.red);
    	for(int go=0;go<girth;go++)
    		graphics.drawRect(x+go,y+go,width-2*go,height-2*go);
    	return img;
    }
}