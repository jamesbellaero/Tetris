
public class LeftFrame extends Frame
{
	Score scoreBoard;
	Menu menu;
	public static final int scoreChange=1;
	public static final int menuChange=2;
    public LeftFrame(int x, int y, int width, int height)
    {
    	super(x,y,width,height);
		scoreBoard=new Score(width/8,height/4,3*width/4,height/6);
		menu=new Menu(width/8,3*height/4,3*width/4,height/6);
		this.addDrawable(scoreBoard);
		this.addDrawable(menu);
    }
    public void update(int targetObj, Object change)
    {
		if((scoreChange&targetObj)!=0)
			scoreBoard.addScore((Integer)change);
		else if((menuChange&targetObj)!=0)
			menu.update(change);
    }
    public Object getInfo(int targetObj)
    {
    	if((scoreChange&targetObj)!=0)
    		return scoreBoard.getScore();
    	else if((menuChange&targetObj)!=0)
    		return menu.clicked();
    	else
    		return null;
    }
}