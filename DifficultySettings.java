public class DifficultySettings {

    // Zorluk seviyelerini tanımlayan enum
    public enum Difficulty {
        EASY,   // Kolay
        MEDIUM, // Orta
        HARD    // Zor
    }

    private Difficulty currentDifficulty;

    public DifficultySettings() {
        // Varsayılan olarak orta zorlukta başlasın
        this.currentDifficulty = Difficulty.MEDIUM;
    }

    public Difficulty getCurrentDifficulty() {
        return currentDifficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.currentDifficulty = difficulty;
    }

    /**
     * Mevcut zorluk seviyesine göre peynirin ekranda kalma süresini (milisaniye cinsinden) döndürür.
     * @return Peynirin ekranda kalma süresi (milisaniye).
     */
    public int getCheeseDisplayDurationMs() {
        switch (currentDifficulty) {
            case EASY:
                return 1500; // Kolay: 3 saniye
            case MEDIUM:
                return 700; // Orta: 2 saniye
            case HARD:
                return 300; // Zor: 1 saniye
            default:
                return 2000; // Varsayılan: Orta
        }
    }
}