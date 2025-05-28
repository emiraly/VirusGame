import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VirusGame extends JPanel implements ActionListener, KeyListener,
        CheeseManager.CheeseStateListener, GameTimer.GameTimeListener, MainMenuRenderer.MainMenuListener {

    JFrame frame;
    Timer gameLoopTimer;
    Timer cheeseSpawnTimer;

    GameAssets assets;
    CheeseManager cheeseManager;
    HandMovement handTopMovement;
    HandMovement handMovementBottom;
    ScoreboardRenderer scoreboardRenderer;
    GameTimer gameTimer;
    ScoreTracker scoreTracker;
    SoundManager soundManager;
    MainMenuRenderer mainMenuRenderer;
    DifficultySettings difficultySettings;

    public enum GameState {
        MAIN_MENU,
        COUNTDOWN,  // Yeni durum
        PLAYING,
        GAME_OVER
    }

    private GameState currentGameState;
    private DifficultySettings.Difficulty lastDifficulty;

    boolean gameOver = false;
    int activePlayer = 0;

    final int GRAB_DURATION = 150;
    final int HAND_WIDTH = 200;
    final int HAND_HEIGHT = 150;
    final int HAND_X = 300;
    final int TOP_HAND_INITIAL_Y = 0;
    final int TOP_HAND_GRAB_Y = 200;
    final int BOTTOM_HAND_INITIAL_Y = 420;
    final int BOTTOM_HAND_GRAB_Y = 250;

    final int GAME_DURATION_MS = 30000;
    final int MAX_FALSE_GRABS = 3;
    final int PENALTY_DURATION_MS = 2000;
    final int WINNING_SCORE = 5;

    // Countdown için yeni değişkenler
    private Timer countdownTimer;
    private int countdownValue;

    public VirusGame() {
        frame = new JFrame("Virüs Oyunu");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.addKeyListener(this);
        frame.setResizable(false);
        frame.setVisible(true);

        assets = new GameAssets();
        cheeseManager = new CheeseManager(this);
        handTopMovement = new HandMovement(TOP_HAND_INITIAL_Y, TOP_HAND_GRAB_Y, GRAB_DURATION, HAND_WIDTH, HAND_HEIGHT);
        handMovementBottom = new HandMovement(BOTTOM_HAND_INITIAL_Y, BOTTOM_HAND_GRAB_Y, GRAB_DURATION, HAND_WIDTH, HAND_HEIGHT);
        scoreboardRenderer = new ScoreboardRenderer();
        gameTimer = new GameTimer(GAME_DURATION_MS, this);
        scoreTracker = new ScoreTracker(MAX_FALSE_GRABS, PENALTY_DURATION_MS, WINNING_SCORE);
        soundManager = new SoundManager();
        difficultySettings = new DifficultySettings();
        mainMenuRenderer = new MainMenuRenderer(assets.mainMenuImage, this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (currentGameState == GameState.MAIN_MENU) {
                    mainMenuRenderer.mousePressed(e);
                } else if (currentGameState == GameState.GAME_OVER) {
                    handleGameOverClick(e.getX(), e.getY());
                }
            }
        });

        currentGameState = GameState.MAIN_MENU;
        soundManager.startBackgroundMusic();

        gameLoopTimer = new Timer(16, this);
        gameLoopTimer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (assets.background != null) {
            g.drawImage(assets.background, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(new Color(200, 170, 120));
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        switch (currentGameState) {
            case MAIN_MENU:
                mainMenuRenderer.draw(g, getWidth(), getHeight());
                break;

            case COUNTDOWN:
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 120));
                String text = countdownValue > 0 ? String.valueOf(countdownValue) : "Başla!";
                FontMetrics fm = g.getFontMetrics();
                int strWidth = fm.stringWidth(text);
                int strHeight = fm.getAscent();
                g.drawString(text, (getWidth() - strWidth) / 2, (getHeight() + strHeight) / 2);
                break;

            case PLAYING:
                long currentTime = System.currentTimeMillis();

                handTopMovement.updatePosition();
                handMovementBottom.updatePosition();

                if (handTopMovement.getHandY() == TOP_HAND_INITIAL_Y && activePlayer == 1) activePlayer = 0;
                if (handMovementBottom.getHandY() == BOTTOM_HAND_INITIAL_Y && activePlayer == 2) activePlayer = 0;

                if (assets.handTop != null)
                    g.drawImage(assets.handTop, HAND_X, handTopMovement.getHandY(),
                            handTopMovement.getHandWidth(), handTopMovement.getHandHeight(), null);

                if (assets.handBottom != null)
                    g.drawImage(assets.handBottom, HAND_X, handMovementBottom.getHandY(),
                            handMovementBottom.getHandWidth(), handMovementBottom.getHandHeight(), null);

                if (cheeseManager.isShowCheese()) {
                    int currentCheeseX = (HAND_X + HAND_X + HAND_WIDTH) / 2 - 60;
                    int currentCheeseY = (handTopMovement.getHandY() + handMovementBottom.getHandY()) / 2 + 20;
                    Image cheese = cheeseManager.isHealthy() ? assets.healthyCheese : assets.rottenCheese;
                    if (cheese != null)
                        g.drawImage(cheese, currentCheeseX, currentCheeseY, 120, 120, null);
                    else {
                        g.setColor(cheeseManager.isHealthy() ? Color.YELLOW : Color.GREEN);
                        g.fillOval(currentCheeseX, currentCheeseY, 120, 120);
                    }
                }

                scoreboardRenderer.draw(g, scoreTracker.getPlayer1Score(), scoreTracker.getPlayer2Score(),
                        scoreTracker.getP1PenaltyEndTime(), scoreTracker.getP2PenaltyEndTime(),
                        currentTime, getWidth(), getHeight());

                gameTimer.draw(g, getWidth(), getHeight());
                break;

            case GAME_OVER:
                scoreboardRenderer.drawGameOverScreen(g,
                        scoreTracker.getPlayer1Score(), scoreTracker.getPlayer2Score(), getWidth(), getHeight());
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentGameState == GameState.PLAYING) {
            if (gameTimer.isGameTimeUp() || scoreTracker.hasReachedWinningScore()) {
                endGame();
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (currentGameState != GameState.PLAYING) return;

        int key = e.getKeyCode();
        long currentTime = System.currentTimeMillis();
        boolean p1InPenalty = scoreTracker.isPlayerInPenalty(1, currentTime);
        boolean p2InPenalty = scoreTracker.isPlayerInPenalty(2, currentTime);

        if (key == KeyEvent.VK_W) {
            if (p1InPenalty || activePlayer == 2) return;
            if (activePlayer == 0) activePlayer = 1;
            if (activePlayer != 1) return;

            if (currentTime - handTopMovement.lastGrabTime > GRAB_DURATION * 2) {
                handTopMovement.startGrab();
                soundManager.playClickSound();

                if (cheeseManager.isShowCheese()) {
                    scoreTracker.updateScore(1, cheeseManager.isHealthy());
                    if (!cheeseManager.isHealthy()) soundManager.playVirusSound();
                    cheeseManager.hideCheese();
                } else {
                    scoreTracker.recordFalseGrab(1);
                    soundManager.playPenaltySound();
                }
            }
        } else if (key == KeyEvent.VK_UP) {
            if (p2InPenalty || activePlayer == 1) return;
            if (activePlayer == 0) activePlayer = 2;
            if (activePlayer != 2) return;

            if (currentTime - handMovementBottom.lastGrabTime > GRAB_DURATION * 2) {
                handMovementBottom.startGrab();
                soundManager.playClickSound();

                if (cheeseManager.isShowCheese()) {
                    scoreTracker.updateScore(2, cheeseManager.isHealthy());
                    if (!cheeseManager.isHealthy()) soundManager.playVirusSound();
                    cheeseManager.hideCheese();
                } else {
                    scoreTracker.recordFalseGrab(2);
                    soundManager.playPenaltySound();
                }
            }
        }

        repaint();
    }

    private void startGame(DifficultySettings.Difficulty difficulty) {
        lastDifficulty = difficulty;
        currentGameState = GameState.COUNTDOWN;  // COUNTDOWN durumuna geç
        countdownValue = 3;

        countdownTimer = new Timer(1000, e -> {
            countdownValue--;
            if (countdownValue <= 0) {
                countdownTimer.stop();

                currentGameState = GameState.PLAYING;
                difficultySettings.setDifficulty(difficulty);
                cheeseManager.setCheeseDisplayDuration(difficultySettings.getCheeseDisplayDurationMs());
                gameTimer.startGame();

                if (cheeseSpawnTimer == null) {
                    cheeseSpawnTimer = new Timer(1600, ev -> cheeseManager.spawnCheese());
                }
                cheeseSpawnTimer.start();

                scoreTracker = new ScoreTracker(MAX_FALSE_GRABS, PENALTY_DURATION_MS, WINNING_SCORE);
                soundManager.startBackgroundMusic();
            }
            repaint();
        });
        countdownTimer.start();
    }

    private void endGame() {
        currentGameState = GameState.GAME_OVER;
        if (cheeseSpawnTimer != null) cheeseSpawnTimer.stop();
        cheeseManager.stopHideTimer();
        cheeseManager.hideCheese();
        soundManager.stopBackgroundMusic();
        soundManager.playGameOverSound();
    }

    private void handleGameOverClick(int mouseX, int mouseY) {
        int buttonWidth = 200;
        int buttonHeight = 50;

        int playAgainX = (getWidth() - buttonWidth) / 2;
        int playAgainY = getHeight() / 2 + 80;

        int menuX = (getWidth() - buttonWidth) / 2;
        int menuY = getHeight() / 2 + 150;

        if (mouseX >= playAgainX && mouseX <= playAgainX + buttonWidth &&
                mouseY >= playAgainY && mouseY <= playAgainY + buttonHeight) {
            if (lastDifficulty != null) {
                startGame(lastDifficulty);
            }
        }

        if (mouseX >= menuX && mouseX <= menuX + buttonWidth &&
                mouseY >= menuY && mouseY <= menuY + buttonHeight) {
            currentGameState = GameState.MAIN_MENU;
            repaint();
        }
    }

    @Override public void onCheeseStateChange(boolean showCheese, boolean isHealthy) {}
    @Override public void onGameTimeUp() { endGame(); }
    @Override public void onStartGame(DifficultySettings.Difficulty difficulty) { startGame(difficulty); }
    @Override public void onDifficultySelected(DifficultySettings.Difficulty difficulty) {
        System.out.println("Zorluk seviyesi seçildi: " + difficulty);
    }
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VirusGame::new);
    }
}
