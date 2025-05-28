import java.awt.*;

public class GameTimer {
    private long gameStartTime;     // Oyunun başladığı zaman
    private final int GAME_DURATION; // Oyunun toplam süresi (milisaniye cinsinden)
    private boolean gameTimeUp = false; // Oyun süresinin dolup dolmadığını tutar

    // Oyun süresi değişimlerini dinlemek için bir arayüz
    public interface GameTimeListener {
        void onGameTimeUp(); // Süre dolduğunda çağrılır
    }

    private GameTimeListener listener; // Dinleyiciyi tutar

    public GameTimer(int durationMillis, GameTimeListener listener) {
        this.GAME_DURATION = durationMillis;
        this.listener = listener;
        startGame(); // Oluşturulduğunda oyunu başlatır
    }

    // Oyun zamanlayıcısını başlatır/sıfırlar
    public void startGame() {
        gameStartTime = System.currentTimeMillis();
        gameTimeUp = false;
    }

    // Kalan süreyi milisaniye cinsinden döndürür
    public long getRemainingTimeMillis() {
        long elapsed = System.currentTimeMillis() - gameStartTime;
        long remaining = GAME_DURATION - elapsed;
        return Math.max(0, remaining); // Negatif süre olmamasını sağlar
    }

    // Oyun süresinin dolup dolmadığını kontrol eder ve dinleyiciyi bilgilendirir
    public boolean isGameTimeUp() {
        if (!gameTimeUp && getRemainingTimeMillis() <= 0) {
            gameTimeUp = true;
            if (listener != null) {
                listener.onGameTimeUp(); // Dinleyiciye süre dolduğunu bildirir
            }
        }
        return gameTimeUp;
    }

    // Kalan süreyi ekrana çizer
    public void draw(Graphics g, int width, int height) {
        long remainingSeconds = getRemainingTimeMillis() / 1000;

        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.setColor(Color.YELLOW);

        String timeText = String.format("%02d:%02d", remainingSeconds / 60, remainingSeconds % 60);

        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(timeText);
        int textHeight = fm.getHeight();

        int margin = 20;
        int scoreBoxHeight = 50;
        int topOfP1Box = margin + scoreBoxHeight;
        int bottomOfP2Box = height - margin - scoreBoxHeight;

        // Zamanlayıcıyı skor kutularının ortasına yerleştirir
        int timerY = topOfP1Box + (bottomOfP2Box - topOfP1Box) / 2 + textHeight / 2 - 10;
        int timerX = margin + (150 - textWidth) / 2; // Skor kutusu genişliği 150 varsayılarak

        // Zamanlayıcının arkasına yarı saydam siyah bir kutu çizer
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(timerX - 10, timerY - fm.getAscent() - 5, textWidth + 20, textHeight + 10);

        g.setColor(Color.WHITE); // Zamanlayıcı metnini beyaz yazar
        g.drawString(timeText, timerX, timerY);
    }
}