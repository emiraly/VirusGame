import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class GameAssets {

    public Image background;
    public Image handTop;
    public Image handBottom;
    public Image healthyCheese;
    public Image rottenCheese;
    public Image mainMenuImage;

    public GameAssets() {
        background = loadImage("images/background.png");
        handTop = loadImage("images/mousereversebutton.png");
        handBottom = loadImage("images/mousebutton.png");
        healthyCheese = loadImage("images/antivirus.png");
        rottenCheese = loadImage("images/virus.png");
        mainMenuImage = loadImage("images/mainMenuImage.png");
    }

    private Image loadImage(String fileName) {
        URL url = getClass().getClassLoader().getResource(fileName);
        if (url == null) {
            System.err.println("Hata: Resim bulunamadı: " + fileName);
            System.err.println("Lütfen 'resources/images/' klasöründe olduğundan emin olun.");
            return null;
        }
        return new ImageIcon(url).getImage();
    }
}
