import java.awt.*;

public class ScoreboardRenderer {

    public void draw(Graphics g, int player1Score, int player2Score,
                     long p1PenaltyEndTime, long p2PenaltyEndTime,
                     long currentTime, int width, int height) {

        g.setFont(new Font("Arial", Font.BOLD, 20));

        // Oyuncu W kutusu (sol üst)
        int box1X = 20;
        int box1Y = 20;
        int boxWidth = 200;
        int boxHeight = 60;

        g.setColor(Color.WHITE);
        g.fillRect(box1X, box1Y, boxWidth, boxHeight);

        g.setColor(Color.BLACK);
        g.drawRect(box1X, box1Y, boxWidth, boxHeight);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Oyuncu W Skor: " + player1Score, box1X + 10, box1Y + 25);

        if (p1PenaltyEndTime > currentTime) {
            int secondsLeft = (int) ((p1PenaltyEndTime - currentTime) / 1000);
            if (secondsLeft < 0) secondsLeft = 0;
            g.setColor(Color.RED);
            g.drawString("Ceza: " + secondsLeft + " sn", box1X + 10, box1Y + 50);
        }

        // Oyuncu ↑ kutusu (sol alt)
        int box2X = 20;
        int box2Y = height - 80;

        g.setColor(Color.WHITE);
        g.fillRect(box2X, box2Y, boxWidth, boxHeight);

        g.setColor(Color.BLACK);
        g.drawRect(box2X, box2Y, boxWidth, boxHeight);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Oyuncu ↑ Skor: " + player2Score, box2X + 10, box2Y + 25);

        if (p2PenaltyEndTime > currentTime) {
            int secondsLeft = (int) ((p2PenaltyEndTime - currentTime) / 1000);
            if (secondsLeft < 0) secondsLeft = 0;
            g.setColor(Color.RED);
            g.drawString("Ceza: " + secondsLeft + " sn", box2X + 10, box2Y + 50);
        }
    }

    public void drawGameOverScreen(Graphics g, int player1Score, int player2Score, int width, int height) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));

        String result;
        if (player1Score > player2Score) {
            result = "Kazanan: Oyuncu W";
        } else if (player2Score > player1Score) {
            result = "Kazanan: Oyuncu ↑";
        } else {
            result = "Berabere!";
        }

        FontMetrics fm = g.getFontMetrics();
        int resultWidth = fm.stringWidth(result);
        g.drawString(result, (width - resultWidth) / 2, height / 2 - 30);

        // Skor bilgisi parçalar halinde çiziliyor
        g.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics fmScore = g.getFontMetrics();

        String prefix = "Skor: ";
        String middle = " - ";
        String p1ScoreStr = String.valueOf(player1Score);
        String p2ScoreStr = String.valueOf(player2Score);

        int prefixWidth = fmScore.stringWidth(prefix);
        int p1Width = fmScore.stringWidth(p1ScoreStr);
        int middleWidth = fmScore.stringWidth(middle);
        int p2Width = fmScore.stringWidth(p2ScoreStr);

        int totalWidth = prefixWidth + p1Width + middleWidth + p2Width;
        int startX = (width - totalWidth) / 2;
        int y = height / 2 + 20;

        // Beyaz "Skor: "
        g.setColor(Color.WHITE);
        g.drawString(prefix, startX, y);

        // Kırmızı player1Score
        g.setColor(Color.RED);
        g.drawString(p1ScoreStr, startX + prefixWidth, y);

        // Beyaz " - "
        g.setColor(Color.WHITE);
        g.drawString(middle, startX + prefixWidth + p1Width, y);

        // Kırmızı player2Score
        g.setColor(Color.RED);
        g.drawString(p2ScoreStr, startX + prefixWidth + p1Width + middleWidth, y);

        // Butonlar
        g.setFont(new Font("Arial", Font.PLAIN, 28));
        FontMetrics fm2 = g.getFontMetrics();

        int buttonWidth = 200;
        int buttonHeight = 50;

        int playAgainX = (width - buttonWidth) / 2;
        int playAgainY = height / 2 + 80;

        int menuX = (width - buttonWidth) / 2;
        int menuY = height / 2 + 150;

        // Arka plan dikdörtgeni (beyaz yazı için siyah yarı saydam arka plan)
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(playAgainX, playAgainY, buttonWidth, buttonHeight);
        g.fillRect(menuX, menuY, buttonWidth, buttonHeight);

        // Çerçeve
        g.setColor(Color.WHITE);
        g.drawRect(playAgainX, playAgainY, buttonWidth, buttonHeight);
        g.drawRect(menuX, menuY, buttonWidth, buttonHeight);

        // Yazılar
        String playAgainText = "Tekrar Oyna";
        String menuText = "Ana Menü";

        int playAgainTextWidth = fm2.stringWidth(playAgainText);
        int menuTextWidth = fm2.stringWidth(menuText);

        g.setColor(Color.WHITE);
        g.drawString(playAgainText, playAgainX + (buttonWidth - playAgainTextWidth) / 2, playAgainY + 33);
        g.drawString(menuText, menuX + (buttonWidth - menuTextWidth) / 2, menuY + 33);
    }
}
