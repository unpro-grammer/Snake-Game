package snake;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

// the below imports are included because of * in the above import statements

// import java.awt.event.ActionListener;
// import java.awt.event.KeyAdapter;
// import java.awt.event.KeyEvent;
// import java.awt.Graphics;
// import java.awt.event.ActionEvent;

// import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

  static final int SCREEN_WIDTH = 600;
  static final int SCREEN_HEIGHT = 600;
  static final int UNIT_SIZE = 25;
  static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
  static final int DELAY = 75;
  final int x[] = new int[GAME_UNITS];
  final int y[] = new int[GAME_UNITS];
  int bodyParts = 6;
  int applesEaten;
  int appleX; // x pos of apple
  int appleY;
  char direction = 'R'; // direction of snake
  boolean running = false;
  Timer timer;
  Random random;

  GamePanel() {
    random = new Random();
    this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
    this.setBackground(Color.black);
    this.setFocusable(true);
    this.addKeyListener((new MyKeyAdapter()));
    startGame();
  }

  public void startGame() {
    newApple();
    running = true;
    timer = new Timer(DELAY, this);
    timer.start();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  public void draw(Graphics g) {
    for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
      g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); // vertical gridlines
      g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE); // horizontal gridlines
    }

    g.setColor(new Color(0xeb4034)); // hex code of muted red
    g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); // apple

    for (int i = 0; i < bodyParts; i++) {
      if (i == 0) { // head of snake is a different colour
        g.setColor(new Color(0x4c9c4c)); // 0x4c9c4c darker green
        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
      } else {
        g.setColor(new Color(0x81c973)); // 0x81c973 green
        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
      }
    }
  }

  public void newApple() {
    appleX =
        random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE))
            * UNIT_SIZE; // first part is random position (integer). second part is to scale based
    // on unit_size
    appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
  }

  public void move() {
    for (int i = bodyParts; i > 0; i--) {
      x[i] = x[i - 1];
      y[i] = y[i - 1];
    }

    switch (direction) { // for head of snake
      case 'U':
        y[0] = y[0] - UNIT_SIZE;
        break;
      case 'D':
        y[0] = y[0] + UNIT_SIZE;
        break;
      case 'L':
        x[0] = x[0] - UNIT_SIZE;
        break;
      case 'R':
        x[0] = x[0] + UNIT_SIZE;
        break;
    }
  }

  public void checkApple() {}

  public void checkCollisions() {
    // whether snake's head has collided against itself
    for (int i = bodyParts; i > 0; i--) {
      if ((x[0] == x[i]) && (y[0] == y[i])) {
        running = false;
      }
    }

    // whether snake's head has collided against the borders

    if ((x[0] < 0) || (x[0] > SCREEN_WIDTH) || (y[0] < 0) || (y[0] > SCREEN_HEIGHT)) {
      running = false;
    }

    if (!running) {
      timer.stop();
    }
  }

  public void gameOver(Graphics g) {}

  // Fix: Implement the actionPerformed method
  public void actionPerformed(ActionEvent e) {
    if (running) {
      move();
      checkApple();
      checkCollisions();
    }
    repaint();
  }

  public class MyKeyAdapter extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      // arrow keys and WASD keys permitted for movement
      switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_A:
          if (direction != 'R') { // prevent snake from doubling up on itself
            direction = 'L';
          }
          break;
        case KeyEvent.VK_RIGHT:
        case KeyEvent.VK_D:
          if (direction != 'L') {
            direction = 'R';
          }
          break;
        case KeyEvent.VK_UP:
        case KeyEvent.VK_W:
          if (direction != 'D') {
            direction = 'U';
          }
          break;
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_S:
          if (direction != 'U') {
            direction = 'D';
          }
          break;
      }
    }
  }
}
