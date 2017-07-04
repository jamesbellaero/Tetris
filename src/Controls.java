import java.awt.*;
import java.awt.image.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
public class Controls extends Frame
{
	public static final int Down = 1;
	public static final int Rotate = 2;
	public static final int Left = 4;
	public static final int Right = 8;
	public static final int Highlighted = 16;
	public static final int ExitButton = 32;
	public static final int Hold = 64;
	DownKey down;
	RotateKey rotate;
	LeftKey left;
	RightKey right;
	HoldKey hold;
	Exit exit;
    public Controls(int x, int y, int width, int height)
    {
    	super(x,y,width,height);
    	try
		{
			Scanner scanny=new Scanner(new File("controls.txt"));
	    	left= new LeftKey(width/5,height/20,width/2, 2*height/20,scanny.nextInt());
	    	right= new RightKey(width/5,4*height/20,width/2, 2*height/20,scanny.nextInt());
	    	down= new DownKey(width/5,7*height/20,width/2, 2*height/20,scanny.nextInt());
	    	rotate= new RotateKey(width/5,10*height/20,width/2, 2*height/20,scanny.nextInt());
	    	hold= new HoldKey(width/5,13*height/20,width/2, 2*height/20,scanny.nextInt());
	    	exit= new Exit(width/5, 16*height/20,width/2,2*height/20);
		}
    	catch(IOException e)
		{
			e.printStackTrace();
		}
    	this.addDrawable(left);
    	this.addDrawable(right);
    	this.addDrawable(down);
    	this.addDrawable(rotate);
    	this.addDrawable(hold);
    	this.addDrawable(exit);
    }
    public static String convertKey(int key)
    {
    	if(key==KeyEvent.VK_UP)
    		return "Up Arrow";
    	else if(key==KeyEvent.VK_DOWN)
    		return "Down Arrow";
    	else if(key==KeyEvent.VK_LEFT)
    		return "Left Arrow";
    	else if(key==KeyEvent.VK_RIGHT)
    		return "Right Arrow";
    	else if(key==KeyEvent.VK_SPACE)
    		return "Space Bar";
    	else
    	return ""+(char)key;
    }
    public void update(int targetObj, Object change)
    {
    	if((targetObj&Left)!=0)
    		left.update(change);
    	if((targetObj&Right)!=0)
    		right.update(change);
    	if((targetObj&Down)!=0)
    		down.update(change);
    	if((targetObj&Rotate)!=0)
    		rotate.update(change);
    	if((targetObj&Hold)!=0)
    		hold.update(change);
    	if((targetObj&ExitButton)!=0)
    		exit.update(change);		
    }
    public Object getInfo(int targetObj)
    {
    	Boolean trueBool=new Boolean(true);
    	if((targetObj&Highlighted)!=0)
    		return trueBool.equals(rotate.isActive())||trueBool.equals(down.isActive())||trueBool.equals(left.isActive())||trueBool.equals(right.isActive())||trueBool.equals(hold.isActive());
    	else if((targetObj&ExitButton)!=0)
    		return exit.isActive();
    	return null;
    }
    public void setState(boolean change)
    {
    	if(!change)
    	{
    		try
    		{
	    		File f;
		    	f=new File("controls.txt");
		    	f.createNewFile();
		    	BufferedWriter send=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
		    	String toSend = ""+left.getKey()+" "+right.getKey()+" "+down.getKey()+" "+rotate.getKey()+" "+hold.getKey();
		    	send.write(toSend,0,toSend.length());
		    	send.close();
    		}
    		catch(IOException e)
    		{
    			e.printStackTrace();
    		}
    	}
    	active=change;
    }
}













class DownKey extends DrawableObject
{
	int xPos;
	int yPos;
	boolean state;
	int key;
	String keyString;
	public DownKey(int x, int y, int width, int height, int key)
	{
		super(x,y,width,height);
    	xPos=x;
    	yPos=y;
    	state=false;
    	this.key=key;
    	keyString="Down: "+Controls.convertKey(key);
	}
	public void update(Object obj)
    {
    	if(obj instanceof MouseEvent)
    	{
	    	MouseEvent p=(MouseEvent)obj;
			if(p.getX()>xPos&&p.getY()>yPos&&p.getX()<xPos+width&&p.getY()<yPos+height)
				state=true;
			else
				state=false;
    	}
    	else if(obj instanceof KeyEvent && this.state)
    	{
    		KeyEvent p=(KeyEvent)obj;
    		key=p.getKeyCode();
    		keyString="Down: "+Controls.convertKey(key);
    		state=false;
    	}
    }
    public BufferedImage draw()
    {
    	BufferedImage img=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    	Graphics2D graphics= img.createGraphics();
    	if(state)
    	{
    		graphics.setColor(Color.red);
    		graphics.fillRect(0,0,width,height);
    	}
    	graphics.setColor(Color.green);
    	graphics.setFont(new Font("Arial", Font.PLAIN, (int)(height*.5)));
    	int x=(int)Math.round((width-graphics.getFont().getStringBounds(keyString,new FontRenderContext(new AffineTransform(),false,false)).getWidth())/2);
		int y=(int)Math.round((height-graphics.getFont().getStringBounds(keyString,new FontRenderContext(new AffineTransform(),false,false)).getHeight())/2);
		graphics.drawString(keyString,x,height-y);
		graphics.drawRect(0,0,width-1,height-1);
    	return img;
    }
    public Object isActive()
    {
    	return state;
    }
    public int getKey()
    {
    	return key;
    }
}
class RotateKey extends DrawableObject
{
	int xPos;
	int yPos;
	boolean state;
	int key;
	String keyString;
	public RotateKey(int x, int y, int width, int height, int key)
	{
		super(x,y,width,height);
    	xPos=x;
    	yPos=y;
    	state=false;
    	this.key=key;
    	keyString="Rotate: "+Controls.convertKey(key);
	}
	public void update(Object obj)
    {
    	if(obj instanceof MouseEvent)
    	{
	    	MouseEvent p=(MouseEvent)obj;
			if(p.getX()>xPos&&p.getY()>yPos&&p.getX()<xPos+width&&p.getY()<yPos+height)
				state=true;
			else
				state=false;
    	}
    	else if(obj instanceof KeyEvent && this.state)
    	{
    		KeyEvent p=(KeyEvent)obj;
    		key=p.getKeyCode();
    		keyString="Rotate: "+Controls.convertKey(key);
    		state=false;
    	}
    }
    public BufferedImage draw()
    {
    	BufferedImage img=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    	Graphics2D graphics= img.createGraphics();
    	if(state)
    	{
    		graphics.setColor(Color.red);
    		graphics.fillRect(0,0,width,height);
    	}
    	graphics.setColor(Color.green);
    	graphics.setFont(new Font("Arial", Font.PLAIN, (int)(height*.5)));
    	int x=(int)Math.round((width-graphics.getFont().getStringBounds(keyString,new FontRenderContext(new AffineTransform(),false,false)).getWidth())/2);
		int y=(int)Math.round((height-graphics.getFont().getStringBounds(keyString,new FontRenderContext(new AffineTransform(),false,false)).getHeight())/2);
		graphics.drawString(keyString,x,height-y);
		graphics.drawRect(0,0,width-1,height-1);
    	return img;
    }
    public Object isActive()
    {
    	return state;
    }
    public int getKey()
    {
    	return key;
    }

}
class LeftKey extends DrawableObject
{
	int xPos;
	int yPos;
	boolean state;
	int key;
	String keyString;
	public LeftKey(int x, int y, int width, int height, int key)
	{
		super(x,y,width,height);
    	xPos=x;
    	yPos=y;
    	state=false;
    	this.key=key;
    	keyString="Left: "+Controls.convertKey(key);
	}
	public void update(Object obj)
    {
    	if(obj instanceof MouseEvent)
    	{
	    	MouseEvent p=(MouseEvent)obj;
			if(p.getX()>xPos&&p.getY()>yPos&&p.getX()<xPos+width&&p.getY()<yPos+height)
				state=true;
			else
				state=false;
    	}
    	else if(obj instanceof KeyEvent && this.state)
    	{
    		KeyEvent p=(KeyEvent)obj;
    		key=p.getKeyCode();
    		keyString="Left: "+Controls.convertKey(key);
    		state=false;
    	}
    }
    public BufferedImage draw()
    {
    	BufferedImage img=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    	Graphics2D graphics= img.createGraphics();
    	if(state)
    	{
    		graphics.setColor(Color.red);
    		graphics.fillRect(0,0,width,height);
    	}
    	graphics.setColor(Color.green);
    	graphics.setFont(new Font("Arial", Font.PLAIN, (int)(height*.5)));
    	int x=(int)Math.round((width-graphics.getFont().getStringBounds(keyString,new FontRenderContext(new AffineTransform(),false,false)).getWidth())/2);
		int y=(int)Math.round((height-graphics.getFont().getStringBounds(keyString,new FontRenderContext(new AffineTransform(),false,false)).getHeight())/2);
		graphics.drawString(keyString,x,height-y);
		graphics.drawRect(0,0,width-1,height-1);
    	return img;
    }
    public Object isActive()
    {
    	return state;
    }
    public int getKey()
    {
    	return key;
    }
}
class RightKey extends DrawableObject
{
	int xPos;
	int yPos;
	boolean state;
	int key;
	String keyString;
	public RightKey(int x, int y, int width, int height, int key)
	{
		super(x,y,width,height);
    	xPos=x;
    	yPos=y;
    	state=false;
    	this.key=key;
    	keyString="Right: "+Controls.convertKey(key);
	}
	public void update(Object obj)
    {
    	if(obj instanceof MouseEvent)
    	{
	    	MouseEvent p=(MouseEvent)obj;
			if(p.getX()>xPos&&p.getY()>yPos&&p.getX()<xPos+width&&p.getY()<yPos+height)
				state=true;
			else
				state=false;
    	}
    	else if(obj instanceof KeyEvent && this.state)
    	{
    		KeyEvent p=(KeyEvent)obj;
    		key=p.getKeyCode();
    		keyString="Right: "+Controls.convertKey(key);
    		state=false;
    	}
    }
    public BufferedImage draw()
    {
    	BufferedImage img=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    	Graphics2D graphics= img.createGraphics();
    	if(state)
    	{
    		graphics.setColor(Color.red);
    		graphics.fillRect(0,0,width,height);
    	}
    	graphics.setColor(Color.green);
    	graphics.setFont(new Font("Arial", Font.PLAIN, (int)(height*.5)));
    	int x=(int)Math.round((width-graphics.getFont().getStringBounds(keyString,new FontRenderContext(new AffineTransform(),false,false)).getWidth())/2);
		int y=(int)Math.round((height-graphics.getFont().getStringBounds(keyString,new FontRenderContext(new AffineTransform(),false,false)).getHeight())/2);
		graphics.drawString(keyString,x,height-y);
		graphics.drawRect(0,0,width-1,height-1);
    	return img;
    }
    public Object isActive()
    {
    	return state;
    }
    public int getKey()
    {
    	return key;
    }
}
class HoldKey extends DrawableObject
{
	int xPos;
	int yPos;
	boolean state;
	int key;
	String keyString;
	public HoldKey(int x, int y, int width, int height, int key)
	{
		super(x,y,width,height);
    	xPos=x;
    	yPos=y;
    	state=false;
    	this.key=key;
    	keyString="Hold: "+Controls.convertKey(key);
	}
	public void update(Object obj)
    {
    	if(obj instanceof MouseEvent)
    	{
	    	MouseEvent p=(MouseEvent)obj;
			if(p.getX()>xPos&&p.getY()>yPos&&p.getX()<xPos+width&&p.getY()<yPos+height)
				state=true;
			else
				state=false;
    	}
    	else if(obj instanceof KeyEvent && this.state)
    	{
    		KeyEvent p=(KeyEvent)obj;
    		key=p.getKeyCode();
    		keyString="Hold: "+Controls.convertKey(key);
    		state=false;
    	}
    }
    public BufferedImage draw()
    {
    	BufferedImage img=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    	Graphics2D graphics= img.createGraphics();
    	if(state)
    	{
    		graphics.setColor(Color.red);
    		graphics.fillRect(0,0,width,height);
    	}
    	graphics.setColor(Color.green);
    	graphics.setFont(new Font("Arial", Font.PLAIN, (int)(height*.5)));
    	int x=(int)Math.round((width-graphics.getFont().getStringBounds(keyString,new FontRenderContext(new AffineTransform(),false,false)).getWidth())/2);
		int y=(int)Math.round((height-graphics.getFont().getStringBounds(keyString,new FontRenderContext(new AffineTransform(),false,false)).getHeight())/2);
		graphics.drawString(keyString,x,height-y);
		graphics.drawRect(0,0,width-1,height-1);
    	return img;
    }
    public Object isActive()
    {
    	return state;
    }
    public int getKey()
    {
    	return key;
    }
}

class Exit extends DrawableObject
{
	boolean state;
	public Exit(int x, int y, int width, int height)
	{
		super(x,y,width,height);
		state=false;
	}
	public void update(Object obj)
	{
		MouseEvent p=(MouseEvent)obj;
		if(p.getX()>x&&p.getY()>y&&p.getX()<x+width&&p.getY()<y+height)
				state=true;
	}
	public BufferedImage draw()
	{
		BufferedImage img=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    	Graphics2D graphics= img.createGraphics();
    	if(state)
    	{
    		graphics.setColor(Color.red);
    		graphics.fillRect(0,0,width,height);
    	}
    	graphics.setColor(Color.green);
    	graphics.setFont(new Font("Arial", Font.PLAIN, (int)(height*.75)));
    	String s="Menu";
    	int x=(int)Math.round((width-graphics.getFont().getStringBounds(s,new FontRenderContext(new AffineTransform(),false,false)).getWidth())/2);
		int y=(int)Math.round((height-graphics.getFont().getStringBounds(s,new FontRenderContext(new AffineTransform(),false,false)).getHeight()));
		graphics.drawString(s,x,height-y);
		graphics.drawRect(0,0,width-1,height-1);
    	return img;
	}
	public Object isActive()
	{
		return state;
	}
	
}
