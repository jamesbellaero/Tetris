import java.awt.*;
import java.util.*;
import java.awt.image.*;
public class Frame extends DrawableObject
{
	int x;
	int y;
	int height;
	int width;
	boolean active;
	FrameBorder border;
	Color bgColor;
	ArrayList<DrawableObject> drawables;
    public Frame(int x, int y, int width, int height)
    {
    	super(x,y,height,width);
    	this.x=x;
    	this.y=y;
    	this.height=height;
    	this.width=width;
    	border=new FrameBorder(0,0,width,height);
    	active=true;
    	drawables = new ArrayList<DrawableObject>();
    	bgColor=Color.darkGray;
    }
    public void addDrawable(DrawableObject obj)
    {
    	drawables.add(obj);
    }
    public void removeDrawable(DrawableObject obj)
    {
    	drawables.remove(obj);
    }
    public BufferedImage draw()
    {
    	BufferedImage img=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    	Graphics2D graphics=img.createGraphics();
    	graphics.setColor(bgColor);
    	graphics.fillRect(0,0,width,height);
    	graphics.setColor(Color.lightGray);
    	for(int go=0;go<drawables.size();go++)
    		graphics.drawImage(drawables.get(go).draw(),null,drawables.get(go).getX(),drawables.get(go).getY());
    	graphics.drawImage(border.draw(),null,0,0);
    	return img;
    }
    public void update(int targetObj, Object change)
    {
    }
    public Object getInfo(int targetObj)
    {
    	return null;
    }
    public void setState(boolean state)
    {
    	active=state;
    }
    public boolean isActive()
    {
    	return active;
    }
}