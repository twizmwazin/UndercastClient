package undercast.client;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.GameSettings;

import org.lwjgl.opengl.GL11;

public class ImageLoader extends TextureManager {

    private HashMap<BufferedImage,ByteBuffer> imageDatas = new HashMap();

    public ImageLoader(IResourceManager par1ResourceManagers) {
        super(par1ResourceManagers);
    }

    public void setupTexture(BufferedImage par1BufferedImage, int par2, int x, int y) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, par2);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        if (!imageDatas.containsKey(par1BufferedImage))
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

            ByteBuffer bf = GLAllocation.createDirectByteBuffer(4096*4096);
            bf.put(var6);
            bf.position(0).limit(var6.length);
            imageDatas.put(par1BufferedImage, bf);
        }
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, x, y, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.imageDatas.get(par1BufferedImage));
    }
}
