/////NEED TO ADD LABELS TO KNOW FROM WHICH LOOPS TO BREAK
import java.awt.image.*;
import java.awt.*;
import java.util.*;
public class Grid extends DrawableObject
{
	Block[][] grid;
	double slob;//sideLengthOfBlock
	TetrisBlock currBlock;
	int frequency;
	int updates;
	int waitingRows;
	int blockWidth;
	int blockHeight;
	int pendingTranslations;
	int pendingTransformations;
	boolean active;
	public static final int MoveRight=2;
	public static final int MoveLeft=4;
	public static final int SpeedDown=8;
	public static final int HoldBlock=16;
	boolean holdingBlock;
	boolean lost;
    public Grid(int x, int y, int width, int height, int numBlocksAcross)
    {
    	super(x,y,width,height);
    	slob=(double)width/numBlocksAcross;
    	grid= new Block[(int)Math.round(height/slob)][numBlocksAcross];
    	frequency=15;
    	updates=0;
    	waitingRows=0;
    	blockWidth=width/numBlocksAcross;
    	blockHeight=height/((int)Math.round(height/slob));
    	lost=false;
    	active=true;
    	holdingBlock=false;
    }
    public void add(TetrisBlock tb)//spot is top left corner of block
    {
    	currBlock=tb;
    	currBlock.giveX(grid[0].length/2-tb.getBlocks()[0].length/2);
    }
    public boolean update(int translations, int transformations)
    {
    	if(currBlock!=null)
    	{
    	updates++;
    	pendingTransformations=(pendingTransformations|transformations);
    	pendingTranslations=(pendingTranslations|translations);
		if(pendingTransformations!=0)
		{
			currBlock.transform(pendingTransformations,grid);
			pendingTransformations=0;
		}
    	int c=currBlock.getX();
    	int r=currBlock.getY();
    	Block[][] blocks=currBlock.getBlocks();
    	if((pendingTranslations&MoveRight)!=0)//move right
    	{
    		boolean can=true;
    		Info:
    		for(int row=0;row<blocks.length;row++)
    		{
    			for(int col=0;col<blocks[0].length;col++)
    			{
    				if((c+blocks[0].length>grid[0].length-1)||(grid[row+r][col+c+1]!=null&&blocks[row][col]!=null))
    				{
    					can=false;
    					break Info;
    				}
    			}
    		}
    		if(can)
    		{
    			currBlock.translate(1,0);
    			
    		}
    		pendingTranslations-=MoveRight;
    	}
    	if((translations&MoveLeft)!=0)//move left
    	{

    		boolean can=true;
    		Info:
    		for(int row=0;row<blocks.length;row++)
    		{
    			for(int col=0;col<blocks[0].length;col++)
    			{
    				if(c==0||(grid[row+r][col+c-1]!=null&&blocks[row][col]!=null))
    				{
    					can=false;
    					break Info;
    				}
    			}
    		}
    		if(can)
    		{
    			currBlock.translate(-1,0);
    			
    		}
    		pendingTranslations-=MoveLeft;
    	}
		if((pendingTranslations&HoldBlock)!=0)
		{
			holdingBlock=true;
			pendingTranslations-=HoldBlock;
			return false;
		}
		else
		{
			holdingBlock=false;
		}
		boolean onBottom=false;
		for(int row=0;row<blocks.length;row++)
		{
			if(row+r+1==grid.length)
			{
				onBottom=true;
				break;
			}
			for(int col=0;col<blocks[0].length;col++)
			{

				if(grid[row+r+1][col+c]!=null&&blocks[row][col]!=null)
				{
					onBottom=true;
					break;
				}
			}
		}
		if(onBottom)
		{
			if(translations==0)
			{
				currBlock.updateWithoutAction();
			}
			else
			{
				currBlock.updateWithAction();
			}
			if(currBlock.getUpdatesWithoutAction()>=1.5*frequency||currBlock.getUpdatesSinceBottom()>=2.5*frequency)
			{
				for(int row=0;row<blocks.length;row++)
				{
					for(int col=0;col<blocks[0].length;col++)
					{
						if(grid[row+r][col+c]==null)
							grid[row+r][col+c]=blocks[row][col];
						else if(grid[row+r][col+c]!=null&&blocks[row][col]!=null)
							lost=true;
					}
				}
				TetrisSounds.addSound(2);
				currBlock=null;
				return false;//no block
			}
//				for(int row=0;row<blocks.length;row++)
//				{
//					for(int col=0;col<blocks[0].length;col++)
//					{
//						grid[row+r][col+c]=blocks[row][col];
//					}
//				}
//				currBlock=null;
//				return false;//no block
		}
		if(!onBottom)
		{
			if((((translations&SpeedDown)!=0)&&(updates%(frequency/3)==0))||(((pendingTranslations&SpeedDown)==0)&&(updates%frequency==0)))
				currBlock.translate(0,1);
			if((pendingTranslations&SpeedDown)!=0)
				pendingTranslations-=SpeedDown;
		}
		this.checkRows();
		return true;//has a block
    	}
    	return false;
    }
    public void checkRows()
    {
    	boolean[] rowsToClear=new boolean[grid.length];
    	for(int r=0;r<grid.length;r++)
    	{
    		boolean clear=true;
    		thisRow:
    		for(int c=0;c<grid[0].length;c++)
    		{
    			if(grid[r][c]==null)
    			{
    				clear=false;
    				break thisRow;//current loop only
    			}
    		}
    		if(clear)
    			rowsToClear[r]=true;
    	}
    	int[] rowsDown=new int[grid.length];
    	for(int r=grid.length-1;r>=0;r--)
    	{
    		rowsDown[r]=waitingRows;
    		if(rowsToClear[r])
    		{
    			waitingRows++;
    			for(int c=0;c<grid[0].length;c++)
    			{
    				grid[r][c]=null;
    			}
    		}

    	}
    	for(int r=grid.length-2;r>=0;r--)
    	{
    		if(rowsDown[r]!=0)
    		for(int c=0;c<grid[0].length;c++)
    		{
    			grid[r+rowsDown[r]][c]=grid[r][c];
    			grid[r][c]=null;
    		}
    	}
    }
    public int getWaitingRows()
    {
    	int temp=waitingRows;
    	waitingRows=0;
    	return temp;
    }
    public boolean gameOver()
    {
    	return lost;
    }
    public BufferedImage draw()
    {
    	BufferedImage img=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    	Graphics2D graphics=img.createGraphics();
    	for(int r=0;r<grid.length;r++)
    	{
    		for(int c=0;c<grid[0].length;c++)
    		{
    			if(grid[r][c]==null)
    			{
    				graphics.setColor(Color.black);
					graphics.fillRect(c*blockWidth,r*blockHeight,blockWidth,blockHeight);
    			}
    			else
    			{
    				graphics.setColor(grid[r][c].getColor());
    				graphics.fillRect(c*blockWidth,r*blockHeight,blockWidth,blockHeight);
    			}
    		}
    	}
    	if(currBlock!=null)
    		graphics.drawImage(currBlock.draw(blockWidth,blockHeight),null,currBlock.getX()*blockWidth,currBlock.getY()*blockHeight);
    	graphics.setColor(Color.white);
    	for(int r=0;r<grid.length;r++)
    		graphics.drawLine(0,r*blockHeight,width,r*blockHeight);
    	for(int c=0;c<grid.length;c++)
    		graphics.drawLine(c*blockWidth,0,c*blockWidth,height);
    	return img;
    }
    public boolean heldBlock()
    {
    	return holdingBlock;
    }
    public int getHeld()
    {
    	int helper=currBlock.getBlockType();
    	currBlock=null;
    	return helper;
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