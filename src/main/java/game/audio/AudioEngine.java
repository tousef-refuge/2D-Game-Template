package game.audio;

import game.data.PlayerData;
import game.exceptions.AssetNotFoundException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.openal.ALC10.*;

public class AudioEngine {
    private static long device;
    private static long context;

    private static Music currentMusic;
    private static final Map<String, SoundEffect> SoundEffectCache = new HashMap<>();

    @SuppressWarnings("ConstantConditions")
    public static void init() {
        device = alcOpenDevice((String) null);
        context = alcCreateContext(device, (int[]) null);
        alcMakeContextCurrent(context);

        AL.createCapabilities(ALC.createCapabilities(device));

        ClassLoader loader = AudioEngine.class.getClassLoader();
        try (InputStream in = loader.getResourceAsStream("sfx.manifest")) {
            if (in == null) throw new AssetNotFoundException("sfx.manifest not found");

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            for (String line : reader.lines().toList()) {
                String path = "audio/sfx/" + line.trim();
                SoundEffectCache.put(path, new SoundEffect(path));
            }
        } catch (IOException ignored) {}
    }

    public static void playSoundEffect(String soundEffect) {
        // preferably wav
        String path = "audio/sfx/" + soundEffect;
        SoundEffect currentSoundEffect = SoundEffectCache.get(path);
        if (currentSoundEffect == null) throw new AssetNotFoundException("sound", path);

        currentSoundEffect.play(PlayerData.getFloat("soundVolume"));
    }

    public static void playMusic(String music) {
        // preferably ogg
        String path = "audio/music/" + music;
        if (currentMusic != null) currentMusic.cleanup();
        currentMusic = new Music(path);
        setMusicVolume(PlayerData.getFloat("musicVolume"));
    }

    public static void setMusicVolume(float volume) {
        currentMusic.setVolume(volume);
    }

    public static void update() {
        if (currentMusic != null) currentMusic.update();
        SoundEffectCache.values().forEach(SoundEffect::update);
    }

    public static void cleanup() {
        if (currentMusic != null) currentMusic.cleanup();
        SoundEffectCache.values().forEach(SoundEffect::cleanup);

        alcDestroyContext(context);
        alcCloseDevice(device);
        currentMusic = null;
    }

    public static void stopMusic() {
        if (currentMusic != null) currentMusic.stop();
    }
}
