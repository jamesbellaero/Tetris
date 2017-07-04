import java.awt.*;
import java.awt.image.*;
import java.awt.font.*;
import java.awt.geom.*;
public class Score extends DrawableObject
{
	int score;
	int consecutiveTetris;
	int width;
	int height;
	int xPos;
	int yPos;
    public Score(int x, int y, int width, int height)
    {
    	super(x,y,width,height);
    	this.width=width;
    	this.height=height;
    	xPos=x;
    	yPos=y;
    }
    public void addScore(int numCleared)
    {
    	if(numCleared==4)
    	{
    		consecutiveTetris++;
    		score+=Math.pow(1000,consecutiveTetris);
    		TetrisSounds.addSound(4);
    	}
    	else
    	{
    		score+=numCleared*100;
    		
    	}
    	if(numCleared>0&&numCleared<4)
    	{
    		TetrisSounds.addSound(3);
    		consecutiveTetris=0;
    	}
    }
    public BufferedImage draw()
    {
    	BufferedImage img=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    	Graphics2D graphics= img.createGraphics();
    	graphics.setColor(Color.blue);
    	graphics.fillRect(0,0,width,height);
    	graphics.setColor(Color.green);
    	graphics.setFont(new Font("Arial", Font.PLAIN, (int)(height*.5)));
    	String s=""+score;
    	while(s.length()<7)
    	{
    		s+=" ";
    	}
		int x=(int)Math.round((width-graphics.getFont().getStringBounds(s,new FontRenderContext(new AffineTransform(),false,false)).getWidth())/2);
		int y=(int)Math.round((height-graphics.getFont().getStringBounds(s,new FontRenderContext(new AffineTransform(),false,false)).getHeight())/2);
		graphics.drawString(s,x,height-y);
		graphics.drawRect(0,0,width-1,height-1);
    	return img;
    }
    public int getScore()
    {
    	return score;
    }
    public int getConsecutiveTetris()
    {
    	return consecutiveTetris;
    }
}