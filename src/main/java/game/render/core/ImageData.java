package game.render.core;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.memAlloc;

final class ImageData implements AutoCloseable {
    public final int width, height;
    public ByteBuffer image;
    private ByteBuffer imageBytes;

    public ImageData(String path) {
        try (MemoryStack stack = stackPush()) {
            try (InputStream in = getClass().getClassLoader().getResourceAsStream(path)) {
                if (in == null) throw new RuntimeException("Resource not found: " + path);
                imageBytes = ioResourceToByteBuffer(in);
            }

            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            image = STBImage.stbi_load_from_memory(imageBytes, w, h, channels, 4);
            if (image == null) throw new RuntimeException("Failed to load image: " + path);

            width = w.get();
            height = h.get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ByteBuffer ioResourceToByteBuffer(InputStream source) throws IOException {
        ByteBuffer buffer = memAlloc(0x2000);
        try (var rbc = Channels.newChannel(source)) {
            while (true) {
                int bytes = rbc.read(buffer);
                if (bytes == -1) break;
                if (buffer.remaining() == 0) {
                    ByteBuffer newBuffer = memAlloc(buffer.capacity() * 2);
                    buffer.flip();
                    newBuffer.put(buffer);
                    buffer = newBuffer;
                }
            }
        }
        buffer.flip();
        return buffer;
    }

    @Override
    public void close() {
        STBImage.stbi_image_free(image);
        org.lwjgl.system.MemoryUtil.memFree(imageBytes);
        image = null;
        imageBytes = null;
    }
}
