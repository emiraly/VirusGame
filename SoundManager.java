import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundManager {

    private Clip backgroundClip;
    private Clip clickClip;
    private Clip gameOverClip;
    private Clip penaltyClip;   // ceza sesi
    private Clip virusClip;     // virüs alma sesi

    public SoundManager() {
        loadBackgroundMusic("/Sound/music.wav");
        loadClickSound("/Sound/clicksound.wav");
        loadGameOverSound("/Sound/gameover.wav");
        loadPenaltySound("/Sound/ceza.wav");
        loadVirusSound("/Sound/virus.wav");
    }

    private void loadBackgroundMusic(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                System.err.println("Background music not found: " + path);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioIn);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void loadClickSound(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                System.err.println("Click sound not found: " + path);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            clickClip = AudioSystem.getClip();
            clickClip.open(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void loadGameOverSound(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                System.err.println("Game over sound not found: " + path);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            gameOverClip = AudioSystem.getClip();
            gameOverClip.open(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void loadPenaltySound(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                System.err.println("Penalty sound not found: " + path);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            penaltyClip = AudioSystem.getClip();
            penaltyClip.open(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void loadVirusSound(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                System.err.println("Virus sound not found: " + path);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            virusClip = AudioSystem.getClip();
            virusClip.open(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playClickSound() {
        if (clickClip != null) {
            if (clickClip.isRunning()) clickClip.stop();
            clickClip.setFramePosition(0);
            clickClip.start();
        }
    }

    public void playGameOverSound() {
        if (gameOverClip != null) {
            if (gameOverClip.isRunning()) gameOverClip.stop();
            gameOverClip.setFramePosition(0);
            gameOverClip.start();
        }
    }

    public void playPenaltySound() {
        if (penaltyClip != null) {
            if (penaltyClip.isRunning()) penaltyClip.stop();
            penaltyClip.setFramePosition(0);
            penaltyClip.start();
        }
    }

    public void playVirusSound() {
        if (virusClip != null) {
            if (virusClip.isRunning()) virusClip.stop();
            virusClip.setFramePosition(0);
            virusClip.start();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
    }

    public void startBackgroundMusic() {
        if (backgroundClip != null && !backgroundClip.isRunning()) {
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
        }

    }
}
