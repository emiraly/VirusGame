public class ScoreTracker {
    private int player1Score = 0;   // Oyuncu 1'in skoru
    private int player2Score = 0;   // Oyuncu 2'nin skoru

    private int P1_FALSE_GRAB_COUNT = 0; // Oyuncu 1'in yanlış yakalama sayısı
    private int P2_FALSE_GRAB_COUNT = 0; // Oyuncu 2'nin yanlış yakalama sayısı
    private final int MAX_FALSE_GRABS;   // Maksimum yanlış yakalama sayısı

    private long P1_PENALTY_END_TIME = 0; // Oyuncu 1'in cezasının biteceği zaman
    private long P2_PENALTY_END_TIME = 0; // Oyuncu 2'nin cezasının biteceği zaman
    private final int PENALTY_DURATION;   // Ceza süresi (milisaniye cinsinden)

    private final int WINNING_SCORE; // Kazanma skoru eşiği

    public ScoreTracker(int maxFalseGrabs, int penaltyDuration, int winningScore) {
        this.MAX_FALSE_GRABS = maxFalseGrabs;
        this.PENALTY_DURATION = penaltyDuration;
        this.WINNING_SCORE = winningScore;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public long getP1PenaltyEndTime() {
        return P1_PENALTY_END_TIME;
    }

    public long getP2PenaltyEndTime() {
        return P2_PENALTY_END_TIME;
    }

    // Puanı günceller
    public void updateScore(int player, boolean isHealthy) {
        if (isHealthy) {
            if (player == 1) player1Score++;
            else player2Score++;
        } else {
            // Çürük peynirde 2 puan düşer
            if (player == 1) player1Score -= 2;
            else player2Score -= 2;
        }
    }

    // Yanlış yakalamayı kaydeder ve ceza uygulayıp uygulamayacağını kontrol eder
    public void recordFalseGrab(int player) {
        long currentTime = System.currentTimeMillis();
        if (player == 1) {
            P1_FALSE_GRAB_COUNT++;
            if (P1_FALSE_GRAB_COUNT >= MAX_FALSE_GRABS) {
                P1_PENALTY_END_TIME = currentTime + PENALTY_DURATION; // Ceza bitiş zamanını ayarlar
                P1_FALSE_GRAB_COUNT = 0; // Yanlış yakalama sayacını sıfırlar
            }
        } else {
            P2_FALSE_GRAB_COUNT++;
            if (P2_FALSE_GRAB_COUNT >= MAX_FALSE_GRABS) {
                P2_PENALTY_END_TIME = currentTime + PENALTY_DURATION; // Ceza bitiş zamanını ayarlar
                P2_FALSE_GRAB_COUNT = 0; // Yanlış yakalama sayacını sıfırlar
            }
        }
    }

    // Oyuncunun ceza durumunda olup olmadığını kontrol eder
    public boolean isPlayerInPenalty(int player, long currentTime) {
        if (player == 1) {
            return currentTime < P1_PENALTY_END_TIME;
        } else {
            return currentTime < P2_PENALTY_END_TIME;
        }
    }

    // Herhangi bir oyuncunun kazanma skoruna ulaşıp ulaşmadığını kontrol eder
    public boolean hasReachedWinningScore() {
        return player1Score >= WINNING_SCORE || player2Score >= WINNING_SCORE;
    }
}