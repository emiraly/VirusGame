import java.awt.*; // java.awt.Image sınıfı için gerekli

public class HandMovement {
    private int handY;          // Elin mevcut Y koordinatı (ekranda nerede olduğu)
    private int initialY;       // Elin başlangıç (dinlenme) Y koordinatı
    private int grabY;          // Elin "yakalama" anındaki Y koordinatı
    public long lastGrabTime = 0; // Son yakalama girişiminin zamanı (milisaniye cinsinden).
    // Bu alan 'public' çünkü VirusGame sınıfı bu zamana doğrudan erişmek istiyor.
    private int grabDuration;   // Yakalama animasyonunun süresi (elin 'grabY' konumunda ne kadar kalacağı)
    private int handWidth;      // Elin görsel genişliği
    private int handHeight;     // Elin görsel yüksekliği

    /**
     * HandMovement sınıfının yapıcısı. Bir elin hareket özelliklerini başlatır.
     *
     * @param initialY     Elin başlangıç (dinlenme) Y koordinatı.
     * @param grabY        Elin yakalama anındaki Y koordinatı.
     * @param grabDuration Yakalama animasyonunun süresi (milisaniye).
     * @param handWidth    Elin görsel genişliği.
     * @param handHeight   Elin görsel yüksekliği.
     */
    public HandMovement(int initialY, int grabY, int grabDuration, int handWidth, int handHeight) {
        this.initialY = initialY;
        this.handY = initialY; // Başlangıçta el dinlenme konumunda başlar
        this.grabY = grabY;
        this.grabDuration = grabDuration;
        this.handWidth = handWidth;
        this.handHeight = handHeight;
    }

    /**
     * Yakalama animasyonunu başlatır. Elin yakalama pozisyonuna geçmesini tetikler.
     * Bu metod çağrıldığında, 'lastGrabTime' güncellenir.
     */
    public void startGrab() {
        lastGrabTime = System.currentTimeMillis(); // Mevcut zamanı yakalama zamanı olarak kaydeder
    }

    /**
     * Elin pozisyonunu günceller. Eğer yakalama animasyonu süresi hala devam ediyorsa,
     * eli 'grabY' konumunda tutar; aksi takdirde 'initialY' konumuna geri döndürür.
     */
    public void updatePosition() {
        long currentTime = System.currentTimeMillis();
        // Eğer yakalama süresi içindeysen (ve yakalama daha önce başladıysa), eli "yakalama" konumuna getir
        if (currentTime - lastGrabTime < grabDuration && lastGrabTime != 0) {
            handY = grabY;
        } else {
            // Aksi takdirde, eli dinlenme konumuna geri getir
            handY = initialY;
        }
    }

    // Aşağıdaki metotlar, elin mevcut durum bilgilerini döndürmek için 'getter' metotlarıdır.

    /**
     * Elin mevcut Y koordinatını döndürür.
     * @return Elin Y koordinatı.
     */
    public int getHandY() {
        return handY;
    }

    /**
     * Elin görsel genişliğini döndürür.
     * @return Elin genişliği.
     */
    public int getHandWidth() {
        return handWidth;
    }

    /**
     * Elin görsel yüksekliğini döndürür.
     * @return Elin yüksekliği.
     */
    public int getHandHeight() {
        return handHeight;
    }
}