import java.awt.*;
import java.awt.image.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.event.*;
public class Menu extends DrawableObject
{
	int xPos;
	int yPos;
	boolean state;
    public Menu(int x, int y, int width, int height)
    {
    	super(x,y,width,height);
    	xPos=x;
    	yPos=y;
    	state=false;
    }
    public void update(Object obj)
    {
    	MouseEvent p=(MouseEvent)obj;
		if(p.getX()>xPos&&p.getY()>yPos&&p.getX()<xPos+width&&p.getY()<yPos+height)
			state=true;
    }
    public BufferedImage draw()
    {
    	BufferedImage img=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    	Graphics2D graphics= img.createGraphics();
    	graphics.setColor(Color.green);
    	graphics.setFont(new Font("Arial", Font.PLAIN, (int)(height*.5)));
    	String s="Menu";
    	int x=(int)Math.round((width-graphics.getFont().getStringBounds(s,new FontRenderContext(new AffineTransform(),false,false)).getWidth())/2);
		int y=(int)Math.round((height-graphics.getFont().getStringBounds(s,new FontRenderContext(new AffineTransform(),false,false)).getHeight())/2);
		graphics.drawString(s,x,height-y);
		graphics.drawRect(0,0,width-1,height-1);
    	return img;
    }
    public Object clicked()
    {
    	return state;
    }
}