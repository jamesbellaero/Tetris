
public class RightFrame extends Frame
{
	BlockQueue blocks;
	public final static int blocksChange=1;
    public RightFrame(int x, int y, int width, int height)
    {
    	super(x,y,width,height);
    	blocks=new BlockQueue(width/6,height/6,2*width/3,2*height/3);
    	this.addDrawable(blocks);
    }
    public void update(int targetObj, Object change)
    {
		if((blocksChange&targetObj)!=0)
			blocks.addBlock((Integer)change);

    }
    public Object getInfo(int targetObj)
    {
    	if((blocksChange&targetObj)!=0)
			return blocks.getBlock();
		else
			return null;

    }

}