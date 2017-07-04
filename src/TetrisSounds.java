import javax.sound.sampled.*;
import javax.sound.sampled.Line.Info;
import java.util.*;
import java.io.*;

public class TetrisSounds {
    static ArrayList<AudioInputStream> sounds;
    static ArrayList<Clip> soundClips;
    static ArrayList<Clip> pendingSounds;
    static ArrayList<AudioInputStream> music;
    static Clip musicClip;
    static int currMusic;

    //1=rotate, 2=block lands, 3=line disappears, 4= tetris disappears
    public static void setupSounds() {

        sounds = new ArrayList<AudioInputStream>();
        soundClips = new ArrayList<Clip>();
        pendingSounds = new ArrayList<Clip>();
        music = new ArrayList<AudioInputStream>();
        try {
            String path = (new File(".").getCanonicalPath());
            for (int x = 1; x <= 5; x++) {
                sounds.add(AudioSystem.getAudioInputStream(new File(path + "/Audio/Sounds/sound-" + x + ".wav")));
            }
            for (int x = 1; x <= 5; x++) {
                soundClips.add((Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, sounds.get(x - 1).getFormat())));
                soundClips.get(x - 1).open(sounds.get(x - 1));
            }
            for (int x = 1; x <= 6; x++) {
                music.add(AudioSystem.getAudioInputStream(new File("Audio/Music/music-" + x + ".wav")));
            }
            musicClip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, music.get(0).getFormat()));
            musicClip.open(music.get(0));
            currMusic = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AudioInputStream getsoundStream(int x) {
        return sounds.get(x - 1);
    }

    public static Clip getsoundClip(int x) {
        return soundClips.get(x - 1);
    }

    public static AudioInputStream getMusicStream() {
        return music.get(currMusic - 1);
    }

    public static Clip getMusicClip() {
        return musicClip;
    }

    public static void playPendingSounds() {
        for (int x = 0; x < pendingSounds.size(); x++) {
            try {
                Clip clippy = pendingSounds.get(x);
                if (clippy.getMicrosecondPosition() == clippy.getMicrosecondLength())
                    clippy.setMicrosecondPosition(0);
                clippy.start();
                pendingSounds.remove(x);
                x--;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void addSound(int x) {
        if (!pendingSounds.contains(soundClips.get(x - 1)))
            pendingSounds.add(soundClips.get(x - 1));
    }

    public static boolean checkMusic() {
        return musicClip.isRunning();
    }

    public static void advanceMusic() {
        try {
            musicClip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, music.get(currMusic).getFormat()));
            musicClip.open(music.get(currMusic));
            musicClip.start();
        } catch (Exception e) {
            System.out.println("Could not advance music");
        } finally {
            musicClip.loop(1);
        }

        currMusic++;
        if (currMusic == 5)
            currMusic = 0;
    }
}