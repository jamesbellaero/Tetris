import java.awt.*;
import java.awt.image.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class HighScores extends Frame {
    public static final int ExitButton = 1;
    Exit exit;

    public HighScores(int x, int y, int width, int height) {
        super(x, y, width, height);
        try {
            Scanner scanny = new Scanner(new File("highscores.txt"));
            ArrayList<Integer> scores = new ArrayList<>();
            ArrayList<String> names = new ArrayList<>();
            for (int go = 0; scanny.hasNext(); go++) {
                scores.add(scanny.nextInt());
                names.add(scanny.next());
            }
            boolean isSorted=false;
            while(!isSorted){
                isSorted=true;
                for(int i = 0; i<scores.size()-1;i++){
                    if(scores.get(i)<scores.get(i+1)){
                        int tempS=scores.get(i+1);
                        String tempN=names.get(i+1);
                        scores.set(i+1,scores.get(i));
                        names.set(i+1,names.get(i));
                        scores.set(i,tempS);
                        names.set(i,tempN);
                        isSorted=false;
                    }
                }
            }
            for (int go = 0; go < scores.size() && go<10; go++) {
                this.addDrawable(new SingleHighScore(width / 5, 3 * go * height / 34 + height / 34, 3 * width / 5,
                        2 * height / 34, go + 1, names.get(go), scores.get(go)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        exit = new Exit(width / 5, 31 * height / 34, 3 * width / 5, 2 * height / 34);
        this.addDrawable(exit);
    }

    public void update(int targetObj, Object change) {
        if ((targetObj & ExitButton) != 0)
            exit.update(change);
    }

    public Object getInfo(int targetObj) {
        if ((targetObj & ExitButton) != 0)
            return exit.isActive();
        return null;
    }

}

class SingleHighScore extends DrawableObject {
    int score;
    String name;
    int place;

    public SingleHighScore(int x, int y, int width, int height, int place, String name, int score) {
        super(x, y, width, height);
        this.name = name;
        this.score = score;
        this.place = place;
    }

    public BufferedImage draw() {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = img.createGraphics();
        graphics.setColor(Color.green);
        graphics.setFont(new Font("Arial", Font.PLAIN, (int) (height * .75)));
        String s = place + ". " + name + ": " + score;
        int x = (int) Math.round((width - graphics.getFont().getStringBounds(s, new FontRenderContext(new AffineTransform(), false, false)).getWidth()) / 2);
        int y = (int) Math.round((height - graphics.getFont().getStringBounds(s, new FontRenderContext(new AffineTransform(), false, false)).getHeight()) / 2);
        graphics.drawString(s, x, height - y);
        graphics.drawRect(0, 0, width - 1, height - 1);
        return img;
    }
}
//class Exit extends DrawableObject
//{
//	boolean state;
//	public Exit(int x, int y, int width, int height)
//	{
//		super(x,y,width,height);
//		state=false;
//	}
//	public void update(Object obj)
//	{
//		MouseEvent p=(MouseEvent)obj;
//		if(p.getX()>x&&p.getY()>y&&p.getX()<x+width&&p.getY()<y+height)
//				state=true;
//	}
//	public BufferedImage draw()
//	{
//		BufferedImage img=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
//    	Graphics2D graphics= img.createGraphics();
//    	if(state)
//    	{
//    		graphics.setColor(Color.red);
//    		graphics.fillRect(0,0,width,height);
//    	}
//    	graphics.setColor(Color.green);
//    	graphics.setFont(new Font("Arial", Font.PLAIN, (int)(height*.5)));
//    	String s="Menu";
//    	int x=(int)Math.round((width-graphics.getFont().getStringBounds(s,new FontRenderContext(new AffineTransform(),false,false)).getWidth())/2);
//		int y=(int)Math.round((height-graphics.getFont().getStringBounds(s,new FontRenderContext(new AffineTransform(),false,false)).getHeight())/2);
//		graphics.drawString(s,x,height-y);
//		graphics.drawRect(0,0,width-1,height-1);
//    	return img;
//	}
//	public Object isActive()
//	{
//		return state;
//	}
//	
//}