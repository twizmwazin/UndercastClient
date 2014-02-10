package undercast.network.common;

import net.minecraft.client.renderer.texture.TextureUtil;
import undercast.client.UndercastRenderHandler;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageReader {

    public static ArrayList<LoadedImage> images = new ArrayList<LoadedImage>();

    public static class Image{
        public byte[] image;
        public byte id;
        public Image(byte[] b, byte i){
            image = b;
            id = i;
        }
    }

    public static boolean contains(int id){
        for(LoadedImage l : images){
            if(l.id == id){
                return true;
            }
        }
        return false;
    }

    public static LoadedImage get(int id){
        for(LoadedImage l : images){
            if(l.id == id){
                return l;
            }
        }
        return null;
    }

    public static class LoadedImage{
        public BufferedImage image;
        public int textureId;
        public int width;
        public int height;
        public int id;
        public LoadedImage(BufferedImage i, int id){
            this.image = i;
            this.textureId = UndercastRenderHandler.unusedTextures.get(0);
            UndercastRenderHandler.unusedTextures.remove(0);
            this.width = i.getWidth();
            this.height = i.getHeight();
            this.id = id;
        }
    }

}
