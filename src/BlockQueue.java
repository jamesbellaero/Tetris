import java.awt.*;
import java.awt.image.*;
public class BlockQueue extends DrawableObject
{
	int xPos, yPos, width, height;
	TetrisBlock[] blocks;
	TetrisBlock immediateHeld;
    public BlockQueue(int x, int y, int width, int height)
    {
    	super(x,y,width,height);
    	xPos=x;
    	yPos=y;
    	this.width=width;
    	this.height=height;
    	immediateHeld=null;
    	blocks=new TetrisBlock[4];
    	for(int go=0;go<3;go++)
    		blocks[go]=new TetrisBlock((int)Math.pow(2,(int)(Math.random()*7+4)));
    }
    public void addBlock(int block)
    {
    	
    	if(block==-1&&immediateHeld==null)
    	{
			blocks[0]=blocks[1];
			blocks[1]=blocks[2];
			int newBlock= (int)Math.pow(2,(int)(Math.random()*7+4));
			if(newBlock!=Math.pow(2,4))
				newBlock=(int)Math.pow(2,(int)(Math.random()*7+4));
			blocks[2]=new TetrisBlock(newBlock);
    	}
    	immediateHeld=null;
    	if(block>0)
    	{
    		if(blocks[3]==null)
    		{
				blocks[3]=new TetrisBlock((int)Math.pow(2,block));
    		}
    		else
    		{
    			immediateHeld=blocks[3];
    			blocks[3]=new TetrisBlock((int)Math.pow(2,block));
    		}
    	}
    	
    }
    public TetrisBlock getBlock()
    {
    	if(immediateHeld!=null)
    	{
    		return immediateHeld;
    	}
    	return blocks[0];
    }
    public BufferedImage draw()
    {
    	BufferedImage img=new BufferedImage(width,height*3,BufferedImage.TYPE_INT_ARGB);
    	Graphics2D graphics=img.createGraphics();
    	graphics.setColor(Color.white);
    	graphics.fillRect(0,0,width,height);
    	graphics.setColor(Color.black);
    	graphics.drawRect(0,0,width,height-1);
    	for(int x=0;x<3;x++)
    		graphics.drawImage(blocks[x].draw(width/5,width/5),null,width/10,height/10+x*width/5+x*height/10);
    	graphics.setColor(Color.green);
    	graphics.drawRect(1,height/10+3*width/5+3*height/10,width-2,height/5);
    	if(blocks[3]!=null)
    		graphics.drawImage(blocks[3].draw(width/5,width/5),null,width/10,height/10+3*width/5+3*height/10+height/15);
    	return img;
    }

}