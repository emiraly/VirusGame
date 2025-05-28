import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainMenuRenderer {

    public interface MainMenuListener {
        void onStartGame(DifficultySettings.Difficulty difficulty);
        void onDifficultySelected(DifficultySettings.Difficulty difficulty);
    }

    private MainMenuListener listener;
    public Image mainMenuImage;
    private Rectangle startButtonBounds;
    private List<Rectangle> difficultyButtonBounds;
    private DifficultySettings.Difficulty selectedDifficulty;

    // Zorluk seviyelerinin metinleri
    private static final String EASY_TEXT = "Kolay";
    private static final String MEDIUM_TEXT = "Orta";
    private static final String HARD_TEXT = "Zor";

    public MainMenuRenderer(Image image, MainMenuListener listener) {
        this.mainMenuImage = image;
        this.listener = listener;
        this.selectedDifficulty = DifficultySettings.Difficulty.MEDIUM; // Varsayılan olarak orta
        this.difficultyButtonBounds = new ArrayList<>();
    }

    public void draw(Graphics g, int width, int height) {
        // Arka plan görselini çiz
        if (mainMenuImage != null) {
            g.drawImage(mainMenuImage, 0, 0, width, height, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(0, 0, width, height);
        }

        // "Başla" yazısını konumlandırma (Mevcut konumdan biraz sağ alta)
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.setColor(Color.WHITE);
        String startText = "Başla";
        FontMetrics fm = g.getFontMetrics();
        int startTextWidth = fm.stringWidth(startText);
        int startTextHeight = fm.getHeight();

        // **Başla yazısı için kaydırma değerleri**
        int offsetX_start = 10; // Mevcut (width - startTextWidth) / 3 konumundan sağa 100 piksel
        int offsetY_start = 10; // Mevcut height / 2 konumundan aşağıya 100 piksel

        int startButtonX = (width - startTextWidth) / 3 + offsetX_start;
        int startButtonY = height / 2 + offsetY_start;

        g.drawString(startText, startButtonX, startButtonY);
        startButtonBounds = new Rectangle(startButtonX - 10, startButtonY - startTextHeight, startTextWidth + 20, startTextHeight + 10);


        // Zorluk seviyelerini çiz (Mevcut konumdan biraz sol üste ve yan yana)
        g.setFont(new Font("Arial", Font.PLAIN, 35));

        // **Zorluk seçenekleri için kaydırma değerleri**
        int offsetX_difficulty = -80; // Mevcut ortalanmış konumdan sola 150 piksel (negatif değerle sola)
        int offsetY_difficulty = -20; // Mevcut (startButtonY + 100) konumundan yukarıya 150 piksel (negatif değerle yukarıya)


        // Horizontal padding düzeltildi
        int horizontalPadding = -20; // Zorluk seçenekleri arasındaki yatay boşluk (uygun bir değer)

        // Her bir metnin genişliğini önceden hesaplayalım
        int easyTextWidth = fm.stringWidth(EASY_TEXT);
        int mediumTextWidth = fm.stringWidth(MEDIUM_TEXT);
        int hardTextWidth = fm.stringWidth(HARD_TEXT);
        int totalWidth = easyTextWidth + mediumTextWidth + hardTextWidth + (horizontalPadding * 2);

        // Başlangıç X koordinatını hesapla (tüm zorluk seçeneklerini ortalamak için)
        int currentX = (width - totalWidth) / 2 + offsetX_difficulty;

        // Zorluk seçeneklerinin dikey konumu:
        // Mevcut referansınız (startButtonY + 100) idi. Bunu güncellenmiş startButtonY ile kullanıp,
        // üzerine offsetY_difficulty ekleyelim.
        int difficultyY = (height / 2 + 100) + offsetY_difficulty; // height/2 + 100'ü kendime referans aldım


        int textHeight = fm.getHeight(); // Zorluk yazılarının yüksekliği

        difficultyButtonBounds.clear(); // Her çizimde temizle

        // Kolay
        drawDifficultyOption(g, EASY_TEXT, currentX, difficultyY, DifficultySettings.Difficulty.EASY, textHeight);
        currentX += easyTextWidth + horizontalPadding;

        // Orta
        drawDifficultyOption(g, MEDIUM_TEXT, currentX, difficultyY, DifficultySettings.Difficulty.MEDIUM, textHeight);
        currentX += mediumTextWidth + horizontalPadding;

        // Zor
        drawDifficultyOption(g, HARD_TEXT, currentX, difficultyY, DifficultySettings.Difficulty.HARD, textHeight);
    }

    private void drawDifficultyOption(Graphics g, String text, int x, int y, DifficultySettings.Difficulty difficulty, int textHeight) {
        if (selectedDifficulty == difficulty) {
            g.setColor(Color.BLACK);
        } else {
            g.setColor(Color.WHITE);
        }
        g.drawString(text, x, y);

        FontMetrics fm = g.getFontMetrics();
        int currentTextWidth = fm.stringWidth(text);
        // Ascent düzeltmesi ile tıklama alanının doğru olması sağlanır
        difficultyButtonBounds.add(new Rectangle(x, y - textHeight + fm.getAscent(), currentTextWidth, textHeight));
    }


    public void mousePressed(MouseEvent e) {
        if (startButtonBounds != null && startButtonBounds.contains(e.getPoint())) {
            if (listener != null) {
                listener.onStartGame(selectedDifficulty);
            }
        }

        for (int i = 0; i < difficultyButtonBounds.size(); i++) {
            if (difficultyButtonBounds.get(i).contains(e.getPoint())) {
                DifficultySettings.Difficulty clickedDifficulty = null;
                switch (i) {
                    case 0: clickedDifficulty = DifficultySettings.Difficulty.EASY; break;
                    case 1: clickedDifficulty = DifficultySettings.Difficulty.MEDIUM; break;
                    case 2: clickedDifficulty = DifficultySettings.Difficulty.HARD; break;
                }
                if (clickedDifficulty != null) {
                    selectedDifficulty = clickedDifficulty;
                    if (listener != null) {
                        listener.onDifficultySelected(clickedDifficulty);
                    }
                }
            }
        }
    }
}