//ogg files have NO REASON to be this messed up
package game.audio;

import game.exceptions.AssetNotFoundException;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.system.MemoryUtil.NULL;

class Music {
    ByteBuffer oggData;

    private static final int BUFFER_SIZE = 64 * 0x400;
    private static final int BUFFER_COUNT = 2;

    final int source;
    final int[] buffers = new int[BUFFER_COUNT];

    final long decoder;
    final STBVorbisInfo info;
    final int alFormat;

    boolean stopped = false;

    public Music(String path) {
        try {
            oggData = oggLoad(path);

            IntBuffer error = BufferUtils.createIntBuffer(1);
            decoder = STBVorbis.stb_vorbis_open_memory(oggData, error, null);
            if (decoder == NULL) throw new RuntimeException("Failed to open OGG: " + error.get(0));

            info = STBVorbisInfo.malloc();
            STBVorbis.stb_vorbis_get_info(decoder, info);
            alFormat = info.channels() == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16;

            source = AL10.alGenSources();
            for (int i = 0; i < BUFFER_COUNT; i++) buffers[i] = AL10.alGenBuffers();

            for (int buffer : buffers) {
                ByteBuffer data = readChunk();
                if (data.remaining() > 0) {
                    AL10.alBufferData(buffer, alFormat, data, info.sample_rate());
                    AL10.alSourceQueueBuffers(source, buffer);
                }
            }

            AL10.alSourcePlay(source);
        } catch (IOException e) {
            throw new AssetNotFoundException("music", path);
        }
    }

    private ByteBuffer readChunk() {
        int channels = info.channels();
        int samples = BUFFER_SIZE / 2;

        ShortBuffer pcm = BufferUtils.createShortBuffer(samples);
        int samplesRead = STBVorbis.stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm);

        if (samplesRead == 0) {
            STBVorbis.stb_vorbis_seek_start(decoder);
            return BufferUtils.createByteBuffer(0);
        }

        pcm.limit(samplesRead * channels);

        ByteBuffer buffer = BufferUtils.createByteBuffer(pcm.remaining() * 2);
        buffer.asShortBuffer().put(pcm);
        return buffer;
    }

    public void update() {
        int processed = AL10.alGetSourcei(source, AL10.AL_BUFFERS_PROCESSED);

        while (processed-- > 0) {
            int buffer = AL10.alSourceUnqueueBuffers(source);
            ByteBuffer data = readChunk();
            if (data.remaining() == 0) data = readChunk();

            AL10.alBufferData(buffer, alFormat, data, info.sample_rate());
            AL10.alSourceQueueBuffers(source, buffer);
        }

        if (!stopped) {
            int state = AL10.alGetSourcei(source, AL10.AL_SOURCE_STATE);
            if (state != AL10.AL_PLAYING) AL10.alSourcePlay(source);
        }
    }

    public void setVolume(float volume) {
        volume = Math.max(0f, Math.min(1f, volume));
        AL10.alSourcef(source, AL10.AL_GAIN, volume);
    }

    public void stop() {
        stopped = true;
        AL10.alSourceStop(source);
    }

    public boolean isStopped() {
        return stopped;
    }

    public void cleanup() {
        AL10.alSourceStop(source);
        AL10.alDeleteSources(source);
        AL10.alDeleteBuffers(buffers);

        STBVorbis.stb_vorbis_close(decoder);
        info.free();
    }

    private static ByteBuffer oggLoad(String path) throws IOException {
        try (InputStream in = Music.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) throw new IOException("Resource not found: " + path);

            byte[] data = in.readAllBytes();
            ByteBuffer buffer = BufferUtils.createByteBuffer(data.length);
            buffer.put(data);
            buffer.flip();
            return buffer;
        }
    }
}
