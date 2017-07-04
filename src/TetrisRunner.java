import javax.swing.*;
import java.awt.*;
public class TetrisRunner
{
    public static void main(String args[])
    {
    	while(true)
    	{
	    	Tetris blah=new Tetris();
	    	blah.pack();
	    	blah.setVisible(true);
	    	blah.start();
	    	blah.requestFocus();
			blah.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	long time=System.nanoTime();
	    	long bil=1000000000L;
	    	
	    	blah.startMusic();
	    	
	    	while(true)
	    	{
	    		Graphics2D graphics=blah.getNextGraphics();
	    		if((System.nanoTime()-time)>1000000000/60)
	    		{
	    			blah.update();
	    			blah.writeSounds();
		    		time=System.nanoTime();
	    		}
	    		
	    		blah.render(graphics);
	    		if(blah.gameOver())
	    		{
	    			blah.endGameSequence();
	    		}
	    		if(blah.restart())
	    		{
	    			blah.newGame();
	    		}

	    	}
    	}

    }
}