import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.sound.sampled.Line.Info;

public class Tetris extends JFrame implements KeyListener, MouseListener {
    BufferedImage bufferImage;
    Graphics2D buffer;
    int width;
    int height;
    int level;
    Canvas c;
    boolean newBlock;
    BufferStrategy bs;
    RightFrame right;
    Grid grid;
    LeftFrame left;
    MainMenu menu;
    Controls controls;
    HighScores highs;
    GameOver gameOverScreen;
    ArrayList<KeyEvent> keys = new ArrayList<KeyEvent>();
    ArrayList<KeyEvent> seenKeys = new ArrayList<KeyEvent>();
    MouseEvent clicked;
    boolean gameover, scoreEntered;
    int rotateKey;
    int downKey;
    int leftKey;
    int rightKey;
    int holdKey;

    public Tetris() {
        super("Tetris");
        width = 800;
        height = 600;
        bufferImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        buffer = bufferImage.createGraphics();
        buffer.setBackground(Color.lightGray);
        buffer.setColor(Color.lightGray);
        buffer.clearRect(0, 0, width, height);
        c = new Canvas();
        c.setPreferredSize(new Dimension(width, height));
        this.getContentPane().add(c);
        c.addKeyListener(this);
        c.addMouseListener(this);
        menu = new MainMenu(0, 0, width, height);
        clicked = null;
        TetrisSounds.setupSounds();
        try {
            String path = (new File(".").getCanonicalPath());
            Scanner scanny = new Scanner(new File(path + "/controls.txt"));
            leftKey = scanny.nextInt();
            rightKey = scanny.nextInt();
            downKey = scanny.nextInt();
            rotateKey = scanny.nextInt();
            holdKey = scanny.nextInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameover = false;
        scoreEntered = false;
    }

    public void start() {
        c.createBufferStrategy(2);
        bs = c.getBufferStrategy();
    }

    public void newGame() {
        left = new LeftFrame(0, 0, width / 4, height);
        grid = new Grid(width / 4, 0, width / 2, height, 10);
        right = new RightFrame(3 * width / 4, 0, width / 4, height);
        newBlock = true;
        scoreEntered = false;
        gameover = false;
    }

    public void showHighScores() {
        highs = new HighScores(0, 0, width, height);
    }

    public void showControlsMenu() {
        controls = new Controls(0, 0, width, height);
    }


    public void update() {
        if (menu != null && menu.isActive()) {
            if (clicked != null) {
                menu.update(7, clicked);
                clicked = null;
            }
            if (new Boolean(true).equals(menu.getInfo(1))) {
                menu.setState(false);
                this.newGame();
            } else if (new Boolean(true).equals(menu.getInfo(2))) {
                menu.setState(false);
                this.showHighScores();
            } else if (new Boolean(true).equals(menu.getInfo(4))) {
                menu.setState(false);
                this.showControlsMenu();
            }
        } else if (highs != null && highs.isActive()) {
            if (new Boolean(true).equals(highs.getInfo(1))) {
                highs.setState(false);
                menu.setState(true);
            }
            if (clicked != null) {
                highs.update(1, clicked);
                clicked = null;
            }
        } else if (controls != null && controls.isActive()) {
            if (new Boolean(true).equals(controls.getInfo(32))) {
                controls.setState(false);
                menu.setState(true);
                try {
                    Scanner scanny = new Scanner(new File("controls.txt"));
                    leftKey = scanny.nextInt();
                    rightKey = scanny.nextInt();
                    downKey = scanny.nextInt();
                    rotateKey = scanny.nextInt();
                    holdKey = scanny.nextInt();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (new Boolean(true).equals(controls.getInfo(16)) && keys.size() > 0) {
                controls.update(79, keys.get(0));
                keys.clear();
            }
            if (clicked != null) {
                controls.update(111, clicked);
                clicked = null;
            }
        } else if (grid != null && grid.isActive()) {
            int transl = this.createTranslations();
            int transf = this.createTransformations();
            newBlock = !grid.update(transl, transf);

            if (newBlock && !grid.heldBlock()) {

                grid.add((TetrisBlock) right.getInfo(1));
                right.update(1, -1);
                newBlock = false;
            } else if (newBlock && grid.heldBlock()) {
                right.update(1, grid.getHeld());
                grid.add((TetrisBlock) right.getInfo(1));
                right.update(1, -1);
                newBlock = false;
            }

            left.update(1, grid.getWaitingRows());
            if (clicked != null) {
                left.update(2, clicked);
                clicked = null;
            }
            if (new Boolean(true).equals(left.getInfo(2))) {
                grid.setState(false);
                menu.setState(true);
            }
            gameover = grid.gameOver();
        } else if (gameOverScreen != null && gameOverScreen.isActive()) {
            if (!(new Boolean(true).equals(gameOverScreen.getInfo(2)))) {
                if (clicked != null) {
                    gameOverScreen.update(1, clicked);
                    clicked = null;
                }
                if (new Boolean(true).equals(gameOverScreen.getInfo(1)) && keys.size() > 0) {
                    gameOverScreen.update(1, keys.get(0));
                    keys.remove(0);
                }

            } else {
                String toWrite = gameOverScreen.getFinal();
                ArrayList<String> scores = new ArrayList<String>();
                try {
                    Scanner scanny = new Scanner(new File("highscores.txt"));
                    while (scanny.hasNext()) {
                        scores.add(scanny.next() + " " + scanny.next());
                    }
                    scores.add(toWrite);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Collections.sort(scores);
                try {
                    String toSend = "";
                    File f;
                    f = new File("highscores.txt");
                    f.createNewFile();
                    BufferedWriter send = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
                    for (int x = Math.min(scores.size() - 1, 10); x >= 0; x--) {
                        toSend += scores.get(x) + " ";
                    }
                    send.write(toSend, 0, toSend.length());
                    send.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                scoreEntered = true;
                gameOverScreen.setState(false);
            }
        }
    }


    public void render(Graphics2D graphics) {
        if (menu != null && menu.isActive())
            graphics.drawImage(menu.draw(), null, menu.getX(), menu.getY());
        else if (controls != null && controls.isActive())
            graphics.drawImage(controls.draw(), null, controls.getX(), controls.getY());
        else if (highs != null && highs.isActive())
            graphics.drawImage(highs.draw(), null, highs.getX(), highs.getY());
        else if (grid != null && grid.isActive()) {
            graphics.setColor(Color.darkGray);
            graphics.fillRect(0, 0, width, height);
            if (left.isActive())
                graphics.drawImage(left.draw(), null, left.getX(), left.getY());
            if (grid.isActive())
                graphics.drawImage(grid.draw(), null, grid.getX(), grid.getY());
            if (right.isActive())
                graphics.drawImage(right.draw(), null, right.getX(), right.getY());
        } else if (gameOverScreen != null && gameOverScreen.isActive())
            graphics.drawImage(gameOverScreen.draw(), null, gameOverScreen.getX(), gameOverScreen.getY());
        bs.show();
    }


    public void writeSounds() {
        TetrisSounds.playPendingSounds();
        if (!TetrisSounds.checkMusic())
            TetrisSounds.advanceMusic();
    }

    public void startMusic() {
        try {
            TetrisSounds.getMusicClip().start();
        } catch (Exception e) {
            System.out.println("Music failure");
        }
    }

    public void stopMusic() {
        TetrisSounds.getMusicClip().stop();
    }


    public void endGameSequence() {
        int score = (Integer) left.getInfo(1);
        grid.setState(false);
        left.setState(false);
        right.setState(false);
        gameOverScreen = new GameOver(0, 0, width, height, score);
        gameover = false;

    }


    public Graphics2D getNextGraphics() {
        return (Graphics2D) bs.getDrawGraphics();
    }

    public void keyPressed(KeyEvent e) {
        boolean addKey = true;
        for (int x = 0; x < seenKeys.size(); x++) {
            if (e.getKeyCode() == seenKeys.get(x).getKeyCode()) {
                addKey = false;
                break;
            }
        }
        for (int x = 0; x < keys.size(); x++) {
            if (e.getKeyCode() == keys.get(x).getKeyCode()) {
                addKey = false;
                break;
            }
        }
        if (addKey) {
            keys.add(e);
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() != downKey) {
            for (int x = 0; x < seenKeys.size(); x++) {
                if (e.getKeyCode() == seenKeys.get(x).getKeyCode()) {
                    seenKeys.remove(x);
                    break;
                }
            }
        } else {
            for (int x = 0; x < keys.size(); x++) {
                if (e.getKeyCode() == keys.get(x).getKeyCode()) {
                    keys.remove(x);
                    break;
                }
            }
        }

    }

    public void keyTyped(KeyEvent e) {

    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        clicked = e;
    }

    public void mouseReleased(MouseEvent e) {
    }

    public int createTranslations() {
        int send = 0;
        for (int x = 0; x < keys.size(); x++) {
            if (keys.get(x).getKeyCode() == leftKey) {
                send += 4;
                seenKeys.add(keys.get(x));
                keys.remove(x);
                x--;
                continue;
            } else if (keys.get(x).getKeyCode() == rightKey) {
                send += 2;
                seenKeys.add(keys.get(x));
                keys.remove(x);
                x--;
                continue;
            } else if (keys.get(x).getKeyCode() == downKey) {
                send += 8;
                continue;
            } else if (keys.get(x).getKeyCode() == holdKey) {
                send += 16;
                seenKeys.add(keys.get(x));
                keys.remove(x);
                x--;
                continue;
            }

        }
        return send;
    }

    public int createTransformations() {
        int send = 0;
        for (int x = 0; x < keys.size(); x++) {
            if (keys.get(x).getKeyCode() == rotateKey) {
                send += 2;
                seenKeys.add(keys.get(x));
                keys.remove(x);
                x--;
            }

        }
        return send;
    }

    public boolean gameOver() {
        return gameover;
    }

    public boolean restart() {
        return scoreEntered;
    }
}
