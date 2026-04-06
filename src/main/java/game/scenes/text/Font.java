package game.scenes.text;

import org.lwjgl.stb.STBTTBakedChar;

record Font(int texture, STBTTBakedChar.Buffer chars,
            float ascent, float descent,
            float lineGap, float lineHeight) {}
