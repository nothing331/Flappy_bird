import java.awt.Color;
import javax.swing.*;        

public class App {
    public static void main(String[] args) {
        int boardWidth = 360;
        int boardHeight = 640;

        JFrame gameFrame = new JFrame();
        gameFrame.setSize(boardWidth, boardHeight);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setResizable(false);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //add stuff
        flappyBird gameScreen = new flappyBird();
        gameFrame.add(gameScreen);
        gameFrame.pack();
        gameFrame.requestFocus();
        gameFrame.getContentPane().setBackground(Color.BLACK);
        gameFrame.setVisible(true);
    }
}