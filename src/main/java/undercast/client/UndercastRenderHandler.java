package undercast.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import undercast.network.common.ImageReader;
import undercast.network.common.packet.VIPUser;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UndercastRenderHandler {
    ImageLoader il = new ImageLoader(Minecraft.getMinecraft().getResourceManager());
    BufferedImage donator = null;
    BufferedImage donatorPlus = null;
    BufferedImage dev = null;
    int idDon = TextureUtil.glGenTextures();
    int idDonPlus = TextureUtil.glGenTextures();
    int idDev = TextureUtil.glGenTextures();
    public static ArrayList<Integer> unusedTextures = new ArrayList<Integer>();
    ModelRenderer model; 
    static{
        for(int i = 0; i < 20; i++){
            unusedTextures.add(TextureUtil.glGenTextures());
        }
    }

    public UndercastRenderHandler(){
        MinecraftForge.EVENT_BUS.register(this);
        try {
            ResourceLocation rl = new ResourceLocation("undercast","donator.png");
            IResource iresource = Minecraft.getMinecraft().getResourceManager().getResource(rl);
            InputStream inputstream = iresource.getInputStream();
            donator = ImageIO.read(inputstream);
            il.setupTexture(donator, idDon, 1600, 800);

            rl = new ResourceLocation("undercast","donator+.png");
            iresource = Minecraft.getMinecraft().getResourceManager().getResource(rl);
            inputstream = iresource.getInputStream();
            donatorPlus = ImageIO.read(inputstream);
            il.setupTexture(donatorPlus, idDonPlus, 1600, 800);

            rl = new ResourceLocation("undercast","developer.png");
            iresource = Minecraft.getMinecraft().getResourceManager().getResource(rl);
            inputstream = iresource.getInputStream();
            dev = ImageIO.read(inputstream);
            il.setupTexture(dev, idDev, 1600, 800);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void renderEvent(RenderPlayerEvent.Specials.Pre evt){
        VIPUser player = null;
        for(VIPUser u : UndercastModClass.getInstance().vips){
            if (u.getUsername().equals(evt.entityPlayer.getDisplayName())){
                player = u;
                if(!player.hasCape()){
                    return;
                }
            }
        }
        if (player != null && !evt.entityPlayer.isInvisible() && !evt.entityPlayer.getHideCape())
        {
            if(player.getCape() == VIPUser.capes.get("DONATOR_CAPE") || player.getCape() == VIPUser.capes.get("USER_CAPE")){
                il.setupTexture(donator, idDon, 1600, 800); //BufferedImage, unique id, width,height
            } else if (player.getCape() == VIPUser.capes.get("DONATOR_PLUS_CAPE") || player.getCape() == VIPUser.capes.get("VIP_CAPE")){
                il.setupTexture(donatorPlus, idDonPlus, 1600, 800); //BufferedImage, unique id, width,height
            } else if (player.getCape() == VIPUser.capes.get("DEVELOPER_CAPE")){
                il.setupTexture(dev, idDev, 1600, 800);
            } else if(ImageReader.contains(player.getCape())){
                ImageReader.LoadedImage li = ImageReader.get(player.getCape());
                il.setupTexture(li.image, li.textureId, li.width, li.height);
            }
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            if(model == null) {
                model = new ModelRenderer((ModelBase)ReflectionHelper.getPrivateValue(RenderPlayer.class, evt.renderer, 1), 0, 0);

                float x = -3.0f; // original cape value: -5.0f
                float y = 1.0f; // original cape value: 0.0f
                float z = -0.5f;// original cape value: -1.0f
                int width = 6; // original cape value: 10
                int height = 8; // original cape value: 16
                int depth = 1; // original cape value: 1.0
                float scaleFactor = 1.0f; // original cape value: 1.0

                model.addBox(x, y, z, width, height, depth, scaleFactor);
            }
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            // this argument is a scale as well
            model.render(0.0625F);
            GL11.glPopMatrix();
        }
    }
}
