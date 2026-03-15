package game.audio;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class SoundEffect {
    private final int buffer;
    private final List<Integer> activeSources = new ArrayList<>();

    AudioInputStream ain;
    AudioFormat format;
    int alFormat;

    public SoundEffect(String path) {
        buffer = AL10.alGenBuffers();
        try {
            InputStream in = getClass().getClassLoader().getResourceAsStream(path);
            if (in == null) throw new RuntimeException("Resource not found: " + path);

            ain = AudioSystem.getAudioInputStream(in);
            format = ain.getFormat();

            if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                        format.getSampleRate(),
                        16,
                        format.getChannels(),
                        format.getChannels() * 2,
                        format.getSampleRate(),
                        false);
                ain = AudioSystem.getAudioInputStream(format, ain);
            }

            ByteBuffer data = readAll(ain);
            alFormat = format.getChannels() == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16;

            AL10.alBufferData(buffer, alFormat, data, (int) format.getSampleRate());
        } catch (IOException | UnsupportedAudioFileException ignored) {}
    }

    private ByteBuffer readAll(AudioInputStream ais) throws IOException {
        byte[] bytes = ais.readAllBytes();
        ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        return buffer;
    }

    public void play(float volume) {
        volume = Math.max(0, Math.min(1, volume));
        int source = AL10.alGenSources();
        AL10.alSourcei(source, AL10.AL_BUFFER, buffer);
        AL10.alSourcef(source, AL10.AL_GAIN, volume);
        AL10.alSourcePlay(source);
        activeSources.add(source);
    }

    public void update() {
        Iterator<Integer> it = activeSources.iterator();
        while (it.hasNext()) {
            int source = it.next();
            if (AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED) {
                AL10.alDeleteSources(source);
                it.remove();
            }
        }
    }

    public void cleanup() {
        for (int source : activeSources) AL10.alDeleteSources(source);
        AL10.alDeleteBuffers(buffer);
    }
}
