package undercast.client.gui;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class BeastnodeButton extends GuiButton {
	private static final ResourceLocation BACKGROUND = new ResourceLocation("undercast", "beastnode.png");

	public BeastnodeButton(int par1, int par2, int par3) {
		super(par1, par2, par3, 51, 45, "");
	}

	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		if (this.visible) {
			boolean hovered = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			par1Minecraft.getTextureManager().bindTexture(BACKGROUND);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, hovered ? 0 : 45, 0, 45, 51);

			if (hovered)
				drawHoveringText(Arrays.asList(EnumChatFormatting.AQUA+"Undercast Client "+EnumChatFormatting.WHITE+"is sponsored by "+EnumChatFormatting.BLUE+EnumChatFormatting.BOLD+"Beastnode"), par2, par3, Minecraft.getMinecraft().fontRenderer);
		} 
	}

	/**
	 * Helper method for drawing a tooltip on the screen. Copied from GuiScreen
	 * @param text
	 * @param p_146283_2_ The x coordinate
	 * @param p_146283_3_ The y coordinate
	 * @param font The font to draw, <code>Minecraft.getMinecraft().fontRenderer</code> for example
	 */
	protected void drawHoveringText(List<String> text, int p_146283_2_, int p_146283_3_, FontRenderer font) {
		if (!text.isEmpty()) {
			GL11.glDisable(32826);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(2896);
			GL11.glDisable(2929);
			int k = 0;
			Iterator<String> iterator = text.iterator();

			while (iterator.hasNext()) {
				String s = (String)iterator.next();
				int l = font.getStringWidth(s);
				if (l > k) {
					k = l;
				}
			}

			int j2 = p_146283_2_ + 12;
			int k2 = p_146283_3_ - 12;
			int i1 = 8;

			if (text.size() > 1) {
				i1 += 2 + (text.size() - 1) * 10;
			}

			j2 -= 28 + k;

			if (k2 + i1 + 6 > Minecraft.getMinecraft().displayHeight) {
				k2 = Minecraft.getMinecraft().displayHeight - i1 - 6;
			}

			this.zLevel = 300.0F;
			int j1 = -267386864;
			drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
			drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
			drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
			drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
			drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
			int k1 = 1347420415;
			int l1 = (k1 & 0xFEFEFE) >> 1 | k1 & 0xFF000000;
			drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
			drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
			drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
			drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

			for (int i2 = 0; i2 < text.size(); i2++) {
				String s1 = (String)text.get(i2);
				font.drawStringWithShadow(s1, j2, k2, -1);

				if (i2 == 0) {
					k2 += 2;
				}

				k2 += 10;
			}

			this.zLevel = 0.0F;
			GL11.glEnable(2896);
			GL11.glEnable(2929);
			RenderHelper.enableStandardItemLighting();
			GL11.glEnable(32826);
		}
	}
}
