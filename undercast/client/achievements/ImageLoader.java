package undercast.client.achievements;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.texturepacks.TexturePackList;
import org.lwjgl.opengl.GL11;

public class ImageLoader extends RenderEngine {

    private ByteBuffer imageData = GLAllocation.createDirectByteBuffer(16777216);
    private BufferedImage prevImage;

    public ImageLoader(TexturePackList par1TexturePackList, GameSettings par2GameSettings) {
        super(par1TexturePackList, par2GameSettings);
    }

    public void setupTexture(BufferedImage par1BufferedImage, int par2, int x, int y) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, par2);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        if (this.prevImage != par1BufferedImage)
        {
            int[] var5 = new int[y * x];
            byte[] var6 = new byte[x * y * 4];

            par1BufferedImage.getRGB(0, 0, x, y, var5, 0, x);

            for (int var7 = 0; var7 < var5.length; ++var7)
            {
                int var8 = var5[var7] >> 24 & 255;
                int var9 = var5[var7] >> 16 & 255;
                int var10 = var5[var7] >> 8 & 255;
                int var11 = var5[var7] & 255;

                var6[var7 * 4 + 0] = (byte) var9;
                var6[var7 * 4 + 1] = (byte) var10;
                var6[var7 * 4 + 2] = (byte) var11;
                var6[var7 * 4 + 3] = (byte) var8;
            }

            this.imageData.clear();
            this.imageData.put(var6);
            this.imageData.position(0).limit(var6.length);
        }
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, x, y, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageData);
        this.prevImage = par1BufferedImage;
    }
}