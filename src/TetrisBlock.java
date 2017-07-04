import java.awt.*;
import java.awt.image.*;
public class TetrisBlock
{
	public static final int RotateRight=1;
	public static final int RotateLeft=2;
	public static final int FlipVertical=4;
	public static final int FlipHorizontal=8;
	static final int Block1=(int)Math.pow(2,4);
	static final int Block2=(int)Math.pow(2,5);
	static final int Block3=(int)Math.pow(2,6);
	static final int Block4=(int)Math.pow(2,7);
	static final int Block5=(int)Math.pow(2,8);
	static final int Block6=(int)Math.pow(2,9);
	static final int Block7=(int)Math.pow(2,10);

	Block[][] blocks;
	Color color;
	int xSpot;
	int ySpot;
	int updatesSinceBottom;
	int updatesSinceAction;
	boolean bottom;
	int logB;
    public TetrisBlock(int b)
    {
    	logB=(int)(Math.log(b)/Math.log(2));
    	this.createBlock(b);
    	ySpot=0;
    	for(int r=0;r<blocks.length;r++)
    	{
    		for(int c=0;c<blocks.length;c++)
    		{
    			if(blocks[r][c]!=null)
    			blocks[r][c].setColor(color);
    		}
    	}
    }
    public int getBlockType()
    {
    	return logB;
    }
    public void hitBottom()
    {
    	bottom=true;
    	updatesSinceBottom=0;
    	updatesSinceAction=0;
    }
    public void updateWithoutAction()
    {
    	updatesSinceAction++;
    	updatesSinceBottom++;
    }
    public void updateWithAction()
    {
    	updatesSinceAction=0;
    	updatesSinceBottom++;
    }
    public int getUpdatesWithoutAction()
    {
    	return updatesSinceAction;
    }
    public int getUpdatesSinceBottom()
    {
    	return updatesSinceBottom;
    }
    public void offBottom()
    {
    	bottom=false;
    }
    public boolean getState()
    {
    	return bottom;
    }
    public Block[][] getBlocks()
    {
    	return blocks;
    }
    public void giveX(int x)
    {
    	xSpot=x;
    }
    public int getX()
    {
    	return xSpot;
    }
    public int getY()
    {
    	return ySpot;
    }
    public Color getColor()
    {
    	return color;
    }
    public void setColor(Color clr)
    {
    	color=clr;
    	for(int r=0;r<blocks.length;r++)
    	{
    		for(int c=0;c<blocks.length;c++)
    		{
    			blocks[r][c].setColor(color);
    		}
    	}
    }
    public BufferedImage draw(int blockWidth, int blockHeight)
    {
    	BufferedImage img=new BufferedImage(blockWidth*blocks[0].length,blockHeight*blocks.length,BufferedImage.TYPE_INT_ARGB);
    	Graphics2D graphics=img.createGraphics();
    	for(int r=0;r<blocks.length;r++)
    	{
    		for(int c=0;c<blocks[0].length;c++)
    		{
    			if(blocks[r][c]!=null)
    			{
    				graphics.setColor(blocks[r][c].getColor());
    				graphics.fillRect(c*blockWidth,r*blockHeight,blockWidth,blockHeight);
    			}
    		}
    	}
    	return img;
    }
    public void translate(int xDir, int yDir)
    {
    	xSpot+=xDir;
    	ySpot+=yDir;
    }
    public void transform(int transformation, Block[][] filled)
    {
    	if((RotateRight&transformation)!=0)
    	{
    		boolean nope=false;

    		Block[][] tempTransform = new Block[blocks[0].length][blocks.length];
    		for(int r=0;r<blocks.length;r++)
    		{
    			for(int c=0;c<blocks[0].length;c++)
    			{
    				tempTransform[tempTransform.length-c-1][r]=blocks[r][c];
    			}
    		}
    		while(tempTransform[0].length+xSpot>=filled[0].length)
    			xSpot--;
    		while(tempTransform.length+ySpot>=filled.length)
    			ySpot--;
    		for(int r=0;r<tempTransform.length;r++)//break properly
    		{
    			for(int c=0;c<tempTransform[0].length;c++)
    			{
    				if(tempTransform[r][c]!=null&&filled[ySpot+r][xSpot+c]!=null)
    				{
    					nope=true;
    					break;
    				}
    			}
    		}
    		if(!nope)
    		{
    			TetrisSounds.addSound(1);
    			blocks=tempTransform;
    		}

    	}
    	if((RotateLeft&transformation)!=0)
    	{
    		boolean nope=false;
    		Block[][] tempTransform = new Block[blocks[0].length][blocks.length];
    		for(int r=0;r<blocks.length;r++)
    		{
    			for(int c=0;c<blocks[0].length;c++)
    			{
    				tempTransform[c][tempTransform[0].length-r-1]=blocks[r][c];
    			}
    		}
    		while(tempTransform[0].length+xSpot>=filled[0].length)
    			xSpot--;
    		while(tempTransform.length+ySpot>=filled.length)
    			ySpot--;
    		for(int r=0;r<tempTransform.length;r++)
    		{
    			for(int c=0;c<tempTransform[0].length;c++)
    			{
    				if(tempTransform[r][c]!=null&&filled[ySpot+r][xSpot+c]!=null)
    				{
    					nope=true;
    					break;//break two
    				}
    			}
    		}
    		if(!nope)
    		{
    			blocks=tempTransform;
    			TetrisSounds.addSound(1);
    		}

    	}
    	if((FlipVertical&transformation)!=0)
    	{
    		boolean nope=false;
    		Block[][] tempTransform = new Block[blocks.length][blocks[0].length];
    		for(int r=0;r<blocks.length;r++)
    		{
    			for(int c=0;c<blocks[0].length;r++)
    			{
    				tempTransform[-r][c]=blocks[r][c];
    			}
    		}
    		for(int r=0;r<tempTransform.length;r++)
    		{
    			for(int c=0;c<tempTransform[0].length;c++)
    			{
    				if(tempTransform[r][c]!=null&&filled[ySpot+r][xSpot+c]!=null)
    				{
    					nope=true;
    					break;
    				}
    			}
    		}
    		if(!nope)
    			blocks=tempTransform;
    	}
    	if((FlipHorizontal&transformation)!=0)
    	{
    		boolean nope=false;
    		Block[][] tempTransform = new Block[blocks.length][blocks[0].length];
    		for(int r=0;r<blocks.length;r++)
    		{
    			for(int c=0;c<blocks[0].length;r++)
    			{
    				tempTransform[r][-c]=blocks[r][c];
    			}
    		}
    		for(int r=0;r<tempTransform.length;r++)
    		{
    			for(int c=0;c<tempTransform[0].length;c++)
    			{
    				if(tempTransform[r][c]!=null&&filled[ySpot+r][xSpot+c]!=null)
    				{
    					nope=true;
    					break;
    				}
    			}
    		}
    		if(!nope)
    			blocks=tempTransform;
    	}
    }
    public void createBlock(int b)
    {
    	color=Color.black;
    	if(b==Block1)
    	{
    		color=Color.blue;
    		Block[][] blocks2={{new Block(color),new Block(color),new Block(color),new Block(color)}};
    		blocks=blocks2;
    	}
    	if(b==Block2)
    	{
    		color=Color.magenta;
    		Block[][] blocks2={{new Block(color),null,null},{new Block(color),new Block(color),new Block(color)}};
    		blocks=blocks2;
    	}
    	if(b==Block3)
    	{
    		color=Color.red;
    		Block[][] blocks2={{null,null,new Block(color)},{new Block(color),new Block(color),new Block(color),new Block(color)}};
    		blocks=blocks2;
    	}
    	if(b==Block4)
    	{
    		color=Color.green;
    		Block[][] blocks2={{new Block(color),new Block(color)},{new Block(color),new Block(color)}};
    		blocks=blocks2;
    	}
    	if(b==Block5)
    	{
    		color=Color.yellow;
    		Block[][] blocks2={{null,new Block(color),new Block(color)},{new Block(color),new Block(color),null}};
    		blocks=blocks2;
    	}
    	if(b==Block6)
    	{
    		color=Color.pink;
    		Block[][] blocks2={{null,new Block(color),null},{new Block(color),new Block(color),new Block(color)}};
    		blocks=blocks2;
    	}
    	if(b==Block7)
    	{
    		color=Color.cyan;
    		Block[][] blocks2={{new Block(color),new Block(color),null},{null,new Block(color),new Block(color)}};
    		blocks=blocks2;
    	}
    }
}