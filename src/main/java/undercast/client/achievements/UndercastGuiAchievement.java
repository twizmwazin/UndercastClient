package undercast.client.achievements;

import aurelienribon.tweenengine.Tween;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import undercast.client.achievements.animation.UndercastAchievementAccessor;

import java.util.ArrayList;

public class UndercastGuiAchievement extends GuiScreen {

    private static final ResourceLocation achievementBackground = new ResourceLocation("textures/gui/achievement/achievement_background.png");
    public ArrayList<UndercastAchievement> achievements = new ArrayList();
    ScaledResolution scr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
    private Minecraft client;
    private ArrayList<Integer> usedRank = new ArrayList();

    public UndercastGuiAchievement(Minecraft mc) {
        client = mc;
        Tween.registerAccessor(UndercastAchievement.class, new UndercastAchievementAccessor());
    }

    public void queueTakenAchievement(UndercastAchievement achievement) {
        int rank = getFirstAvailableRank();
        achievement.setRank(rank);
        achievements.add(achievement);
        usedRank.add(rank);
    }

    @Override
    public void updateScreen() {
        for (int i = 0; i < achievements.size(); i++) {
            achievements.get(i).draw();
        }
    }

    public void removeAchievement(UndercastAchievement achievement) {
        usedRank.remove(new Integer(achievement.getRank()));
        achievements.remove(achievement);
    }

    public int getFirstAvailableRank() {
        for (int i = 0; i < 20; i++) {
            if (!usedRank.contains(i)) {
                return i;
            }
        }
        return -1;
    }

}
