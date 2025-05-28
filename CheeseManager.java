import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class CheeseManager {

    public interface CheeseStateListener {
        void onCheeseStateChange(boolean showCheese, boolean isHealthy);
    }

    private CheeseStateListener listener;
    private boolean showCheese;
    private boolean isHealthy; // Peynir sağlıklı mı (true) yoksa çürük mü (false)
    private Random random;
    private Timer hideCheeseTimer; // Peyniri gizlemek için zamanlayıcı

    private int cheeseDisplayDurationMs; // Peynirin görünür kalma süresi (dışarıdan ayarlanacak)

    public CheeseManager(CheeseStateListener listener) {
        this.listener = listener;
        this.random = new Random();
        this.showCheese = false; // Başlangıçta peynir görünmez
        this.isHealthy = true; // Varsayılan olarak sağlıklı

        // Zamanlayıcıyı başlatma kısmında süreyi kullanacağız
        hideCheeseTimer = new Timer(cheeseDisplayDurationMs, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideCheese();
            }
        });
        hideCheeseTimer.setRepeats(false); // Sadece bir kez çalışsın
    }

    // Peynirin görünür kalma süresini ayarlamak için yeni metod
    public void setCheeseDisplayDuration(int durationMs) {
        this.cheeseDisplayDurationMs = durationMs;
        // Eğer zamanlayıcı zaten çalışıyorsa ve süresi değişirse, yeniden ayarlamak iyi olur
        if (hideCheeseTimer.isRunning()) {
            hideCheeseTimer.stop();
            hideCheeseTimer.setInitialDelay(this.cheeseDisplayDurationMs);
            hideCheeseTimer.start();
        } else {
            hideCheeseTimer.setInitialDelay(this.cheeseDisplayDurationMs);
        }
    }


    public void spawnCheese() {
        if (!showCheese) { // Sadece peynir görünmüyorken yeni peynir oluştur
            showCheese = true;
            isHealthy = random.nextBoolean(); // Rastgele sağlıklı veya çürük
            listener.onCheeseStateChange(showCheese, isHealthy);

            // Peyniri belirli bir süre sonra gizlemek için zamanlayıcıyı başlat
            hideCheeseTimer.start();
        }
    }

    public void hideCheese() {
        if (showCheese) { // Sadece peynir görünüyorken gizle
            showCheese = false;
            listener.onCheeseStateChange(showCheese, isHealthy);
            hideCheeseTimer.stop(); // Zamanlayıcıyı durdur
        }
    }

    public boolean isShowCheese() {
        return showCheese;
    }

    public boolean isHealthy() {
        return isHealthy;
    }

    public void stopHideTimer() {
        if (hideCheeseTimer != null && hideCheeseTimer.isRunning()) {
            hideCheeseTimer.stop();
        }
    }
}