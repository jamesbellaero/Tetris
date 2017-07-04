import java.awt.*;
import java.awt.image.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
public class GameOver extends Frame 
{
	public static final int Name = 1;
	public static final int NameFinished = 2;
	FinalScore display;
	NameInput nameEntry;
	String finalScore;
    public GameOver(int x, int y, int width, int height, int score) 
    {
    	super(x,y,width,height);
    	display=new FinalScore(width/10, height/10,8*width/10,2*height/10,score);
    	nameEntry= new NameInput(width/10, 5*height/10, 8*width/10, 2*height/10);
    	this.addDrawable(display);
    	this.addDrawable(nameEntry);
    	finalScore="";
    }
    public void update(int targetObj, Object change)
    {
    	if((targetObj&Name)!=0)
    		nameEntry.update(change);
    }
    public Object getInfo(int targetObj)
    {
    	if((targetObj&Name)!=0)
    		return nameEntry.isActive();
    	if((targetObj&NameFinished)!=0)
    		return nameEntry.isFinished();
    	return null;
    }
    public String getFinal()
    {
    	return display.getScore()+" "+nameEntry.getName();
    }
}
class NameInput extends DrawableObject
{
	String name;
	boolean state;
	boolean finished;
	public NameInput(int x, int y, int width, int height)
	{
		super(x,y,width,height);
		name="";
		state=false;
		finished=false;
	}
	public void update(Object obj)
	{
		if(obj instanceof MouseEvent)
		{
			System.out.println("lkdfkl");
			MouseEvent p=(MouseEvent)obj;
			if(p.getX()>x&&p.getY()>y&&p.getX()<x+width&&p.getY()<y+height)
				state=true;
			else
				state=false;
		}
		else if(obj instanceof KeyEvent)
		{
			KeyEvent e=(KeyEvent)obj;
			if(e.getKeyCode()==KeyEvent.VK_ENTER)
			{
				finished=true;
			}
			else if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE)
			{
				name=name.substring(0,name.length()-1);
			}
			else if((e.getKeyCode()<91&&e.getKeyCode()>64)||(e.getKeyCode()<58&&e.getKeyCode()>47))
			{
				name+=""+(char)e.getKeyCode();
			}
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
    	int x=(int)Math.round((width-graphics.getFont().getStringBounds("Enter Name- "+name,new FontRenderContext(new AffineTransform(),false,false)).getWidth())/2);
		int y=(int)Math.round((height-graphics.getFont().getStringBounds("Enter Name- "+name,new FontRenderContext(new AffineTransform(),false,false)).getHeight())/2);
		graphics.drawString("Enter Name- "+name,x,height-y);
		graphics.drawRect(0,0,width-1,height-1);
    	return img;
    }
    public Object isFinished()
    {
    	return finished;
    }
    public Object isActive()
    {
    	return state;
    }
    public String getName()
    {
    	return name;
    }
}
class FinalScore extends DrawableObject
{
	int score;
	public FinalScore(int x, int y, int width, int height, int score)
	{
		super(x,y,width,height);
		this.score=score;
	}
	public BufferedImage draw()
    {
    	BufferedImage img=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
    	Graphics2D graphics= img.createGraphics();
    	graphics.setColor(Color.green);
    	graphics.setFont(new Font("Arial", Font.PLAIN, (int)(height*.5)));
    	int x=(int)Math.round((width-graphics.getFont().getStringBounds("Final Score- "+score,new FontRenderContext(new AffineTransform(),false,false)).getWidth())/2);
		int y=(int)Math.round((height-graphics.getFont().getStringBounds("Final Score- "+score,new FontRenderContext(new AffineTransform(),false,false)).getHeight())/2);
		graphics.drawString("Final Score- "+score,x,height-y);
		graphics.drawRect(0,0,width-1,height-1);
    	return img;
    }
    public int getScore()
    {
    	return score;
    }
}