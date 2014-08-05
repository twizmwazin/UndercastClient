package undercast.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
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
    ArrayList<BufferedImage> developer = new ArrayList<BufferedImage>(11);
    BufferedImage donator = null;
    BufferedImage donatorPlus = null;
    BufferedImage vip = null;
    BufferedImage user = null;
    ArrayList<Integer> idDev = new ArrayList<Integer>(11);
    int idDon = TextureUtil.glGenTextures();
    int idDonPlus = TextureUtil.glGenTextures();
    int idVip = TextureUtil.glGenTextures();
    int idUser = TextureUtil.glGenTextures();
    public static ArrayList<Integer> unusedTextures = new ArrayList<Integer>();
    ModelRenderer model; 
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

            rl = new ResourceLocation("undercast","vip.png");
            iresource = Minecraft.getMinecraft().getResourceManager().getResource(rl);
            inputstream = iresource.getInputStream();
            vip = ImageIO.read(inputstream);
            il.setupTexture(vip, idVip, 1600, 800);

            rl = new ResourceLocation("undercast","user.png");
            iresource = Minecraft.getMinecraft().getResourceManager().getResource(rl);
            inputstream = iresource.getInputStream();
            user = ImageIO.read(inputstream);
            il.setupTexture(user, idUser, 1600, 800);
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
            } else if (player.getCape() == VIPUser.capes.get("VIP_CAPE")){
                il.setupTexture(vip, idVip, 1600, 800);
            } else if (player.getCape() == VIPUser.capes.get("USER_CAPE")){
                il.setupTexture(user, idUser, 1600, 800);
            } else if(ImageReader.contains(player.getCape())){
                ImageReader.LoadedImage li = ImageReader.get(player.getCape());
                il.setupTexture(li.image, li.textureId, li.width, li.height);
            }
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            if(model == null) {
                model = new ModelRenderer((ModelBase)ReflectionHelper.getPrivateValue(RenderPlayer.class, evt.renderer, 1), 0, 0);

                float x = -3.0f; // original cape value: -5.0f
                float y = 4.0f; // original cape value: 0.0f
                float z = -7.5f;// original cape value: -1.0f, but this makes that the cape is rendered behind the player and not in front
                int width = 6; // original cape value: 10
                int height = 8; // original cape value: 16
                int depth = 1; // original cape value: 1.0
                float scaleFactor = 1.0f; // original cape value: 1.0

                model.addBox(x, y, z, width, height, depth, scaleFactor);
            }
            // this argument is a scale as well
            model.render(0.0625F);
            GL11.glPopMatrix();
        }
    }
}
