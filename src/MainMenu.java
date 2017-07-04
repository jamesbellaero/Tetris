import java.awt.*;
public class MainMenu extends Frame
{
	public static final int Game = 1;
	public static final int High = 2;
	public static final int Controls = 4;
	GameButton gameButton;
	ControlsButton controlsButton;
	HighScoresButton highsButton;
    public MainMenu(int x, int y, int width, int height)
    {
    	super(x,y,width,height);
    	gameButton = new GameButton(width/6, height/10,2*width/3,height/5);
    	highsButton = new HighScoresButton(width/6, 4*height/10, 2*width/3, height/5);
		controlsButton=new ControlsButton(width/6,7*height/10,2*width/3, height/5);
		this.addDrawable(gameButton);
		this.addDrawable(highsButton);
		this.addDrawable(controlsButton);
    }
    public void update(int targetObj, Object change)
    {
    	if((targetObj&Game)!=0)
    		gameButton.update(change);
    	if((targetObj&High)!=0)
    		highsButton.update(change);
    	if((targetObj&Controls)!=0)
    		controlsButton.update(change);
    }
    public Object getInfo(int targetObj)
    {
    	if((targetObj&Game)!=0)
    		return gameButton.isActive();
    	if((targetObj&High)!=0)
    		return highsButton.isActive();
    	if((targetObj&Controls)!=0)
    		return controlsButton.isActive();
    	return null;
    }
}