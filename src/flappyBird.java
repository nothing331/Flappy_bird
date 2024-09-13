import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class flappyBird extends JPanel implements ActionListener, KeyListener{
    int boardWidth = 360;
    int boardHeight = 640;

    //bird
    int birdX = boardWidth*2/5;
    int birdY = boardHeight*3/5;
    int birdW = 36;
    int birdH = 26;

    
    //bird class
    class Bird{
        int x =  birdX;
        int y =  birdY;
        int width = birdW;
        int height = birdH;
        Image img;
        Bird(Image img){
            this.img = img;
        }
    }
      
    //pipe
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeW = 64;
    int pipeH = 512;

    //pipe class
    class Pipe{
        int x =  pipeX;
        int y =  pipeY;
        int width = pipeW;
        int height = pipeH;
        Image img;
        boolean passed = false;
        Pipe(Image img){
            this.img = img;
        }
    }

    Image bgImage;
    Image birdImage;
    Image topPipeImg;
    Image bottomPipeImg;

    Bird bird;
    Timer gameLoop;
    Pipe pipe;
    Timer pipeTime;
    boolean game_over = false;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    double score =0;


    //movement 
    int gravity = 1;
    int veloX = -4;
    int veloY = 0;

    flappyBird(){
        bgImage = new ImageIcon(getClass().getResource("/flappybirdbg.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("/flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("/toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("/bottompipe.png")).getImage();
        setPreferredSize(new Dimension(boardWidth,boardHeight));

        setFocusable(true); // Make the panel focusable to receive key events
        addKeyListener(this); // Register this panel as a key listener


        bird = new Bird(birdImage);
        pipes = new ArrayList<>();

        //pipe time
        pipeTime = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                // System.out.println(score);
                placePipe();
            }
        });

        pipeTime.start();

        //timer
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
        
    }

    public void placePipe(){
        int randomPipeY = (int) (pipeY - pipeH/4 - Math.random()*(pipeH/2));
        int openingSpace = boardHeight/4;

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y  + pipeH + openingSpace;
        pipes.add(bottomPipe);
    }
    @Override
    protected  void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        g.drawImage(bgImage, 0, 0, boardWidth, boardHeight, null);
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe_new = pipes.get(i);
            g.drawImage(pipe_new.img, pipe_new.x, pipe_new.y, pipe_new.width, pipe_new.height, null);
        }
        //score
        g.setColor(Color.white);

        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (game_over) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        }
        else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void  move(){
        veloY += gravity;
        bird.y += veloY;
        bird.y = Math.max(bird.y, 0);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe_m = pipes.get(i);
            pipe_m.x += veloX;

            if (!pipe_m.passed && bird.x > pipe_m.x + pipe_m.width) {
                score += 0.5; 
                pipe_m.passed = true;
            }
            if (collision(bird,pipe_m)){
                game_over = true;
            }
        }
        if (bird.y > boardHeight) {
            game_over = true;
        }        
    }

    boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
               a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
               a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
               a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // System.out.print("1");
        move();
        repaint();
        if (game_over) {
            pipeTime.stop();
            gameLoop.stop();
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // System.out.println("JUMP!");
            veloY = -9;
        }
        if (game_over) {
            //restart game by resetting conditions
            bird.y = birdY;
            veloY = 0;
            pipes.clear();
            game_over = false;
            score = 0;
            gameLoop.start();
            pipeTime.start();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    

    @Override
    public void keyReleased(KeyEvent e) {}
}
