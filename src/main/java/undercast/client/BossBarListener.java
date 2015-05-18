package undercast.client;

import net.minecraft.entity.boss.BossStatus;
import net.minecraft.util.StringUtils;

/***
 * This class listens to the Boss health bar.
 */
public class BossBarListener {
    private static String lastContent = "";


    /***
     * This method is called every update game tick.
     */
    public static void update() {
        // get the content
        String healthBarContent = BossStatus.bossName;
        // check if the content has changed
        if (healthBarContent != null && !healthBarContent.equals(lastContent)) {
            onContentChanged(StringUtils.stripControlCodes(healthBarContent), healthBarContent);
            // update the lastContent
            lastContent = healthBarContent;
        }
    }

    /**
     * This is only called when the content changed.
     */
    private static void onContentChanged(String newContent, String newContentWithFormattingCode) {
        // When the server cycles, reset the stats.
        if (newContent.toLowerCase().contains("cycling to") && newContent.contains(" 1 second")) {
            UndercastCustomMethods.resetAllStats();
        }
    }
}
