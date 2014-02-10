package undercast.client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import undercast.network.common.ImageReader;
import undercast.network.common.packet.VIPUser;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderPlayerEvent.Specials.Pre;
import net.minecraftforge.common.MinecraftForge;

public class UndercastRenderHandler {
	ImageLoader il = new ImageLoader(Minecraft.getMinecraft().getResourceManager());
	ArrayList<BufferedImage> developer = new ArrayList<BufferedImage>(11);
	BufferedImage donator = null;
	BufferedImage donatorPlus = null;
	ArrayList<Integer> idDev = new ArrayList<Integer>(11);
	int idDon = TextureUtil.glGenTextures();
	int idDonPlus = TextureUtil.glGenTextures();
    public static ArrayList<Integer> unusedTextures = new ArrayList<Integer>();
    static{
        for(int i = 0; i < 20; i++){
            unusedTextures.add(TextureUtil.glGenTextures());
        }
    }

	public UndercastRenderHandler(){
		MinecraftForge.EVENT_BUS.register(this);
		for(int i = 0; i < 11; i++){
			idDev.add(TextureUtil.glGenTextures());
		}
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
			for(int i = 0; i<11;i++){
				rl = new ResourceLocation("undercast","developer" + i + ".png");
	            iresource = Minecraft.getMinecraft().getResourceManager().getResource(rl);
	            inputstream = iresource.getInputStream();
	            developer.add(ImageIO.read(inputstream));
	            il.setupTexture(developer.get(i), idDev.get(i), 1600, 800); //preload all textures at startup to prevent lag IG
			}


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
        	if(player.getCape() == VIPUser.capes.get("DEVELOPER_CAPE")){
        		int c = 0;
        		if((c = UndercastModClass.getInstance().capeCounter) > 10){
        			il.setupTexture(developer.get(0), idDev.get(0), 1600, 800); //BufferedImage, unique id, width,height
        		} else{
        			il.setupTexture(developer.get(c), idDev.get(c), 1600, 800);
        		}
        	}else if(player.getCape() == VIPUser.capes.get("DONATOR_CAPE")){
        		il.setupTexture(donator, idDon, 1600, 800); //BufferedImage, unique id, width,height
        	} else if (player.getCape() == VIPUser.capes.get("DONATOR_PLUS_CAPE")){
        		il.setupTexture(donatorPlus, idDonPlus, 1600, 800); //BufferedImage, unique id, width,height
        	} else if(ImageReader.contains(player.getCape())){
                ImageReader.LoadedImage li = ImageReader.get(player.getCape());
                il.setupTexture(li.image, li.textureId, li.width, li.height);
            }
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double d3 = evt.entityPlayer.field_71091_bM + (evt.entityPlayer.field_71094_bP - evt.entityPlayer.field_71091_bM) * (double)evt.partialRenderTick - (evt.entityPlayer.prevPosX + (evt.entityPlayer.posX - evt.entityPlayer.prevPosX) * (double)evt.partialRenderTick);
            double d4 = evt.entityPlayer.field_71096_bN + (evt.entityPlayer.field_71095_bQ - evt.entityPlayer.field_71096_bN) * (double)evt.partialRenderTick - (evt.entityPlayer.prevPosY + (evt.entityPlayer.posY - evt.entityPlayer.prevPosY) * (double)evt.partialRenderTick);
            double d0 = evt.entityPlayer.field_71097_bO + (evt.entityPlayer.field_71085_bR - evt.entityPlayer.field_71097_bO) * (double)evt.partialRenderTick - (evt.entityPlayer.prevPosZ + (evt.entityPlayer.posZ - evt.entityPlayer.prevPosZ) * (double)evt.partialRenderTick);
            float f5 = evt.entityPlayer.prevRenderYawOffset + (evt.entityPlayer.renderYawOffset - evt.entityPlayer.prevRenderYawOffset) * evt.partialRenderTick;
            double d1 = (double)MathHelper.sin(f5 * (float)Math.PI / 180.0F);
            double d2 = (double)(-MathHelper.cos(f5 * (float)Math.PI / 180.0F));
            float f6 = (float)d4 * 10.0F;

            if (f6 < -6.0F)
            {
                f6 = -6.0F;
            }

            if (f6 > 32.0F)
            {
                f6 = 32.0F;
            }

            float f7 = (float)(d3 * d1 + d0 * d2) * 100.0F;
            float f8 = (float)(d3 * d2 - d0 * d1) * 100.0F;

            if (f7 < 0.0F)
            {
                f7 = 0.0F;
            }

            float f9 = evt.entityPlayer.prevCameraYaw + (evt.entityPlayer.cameraYaw - evt.entityPlayer.prevCameraYaw) * evt.partialRenderTick;
            f6 += MathHelper.sin((evt.entityPlayer.prevDistanceWalkedModified + (evt.entityPlayer.distanceWalkedModified - evt.entityPlayer.prevDistanceWalkedModified) * evt.partialRenderTick) * 6.0F) * 32.0F * f9;

            if (evt.entityPlayer.isSneaking())
            {
                f6 += 25.0F;
            }

            GL11.glRotatef(6.0F + f7 / 2.0F + f6, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f8 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f8 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            ModelBiped modelBipedMain = ReflectionHelper.getPrivateValue(RenderPlayer.class, evt.renderer, 1);
            modelBipedMain.renderCloak(0.0625F);
            GL11.glPopMatrix();
        }
	}
}
