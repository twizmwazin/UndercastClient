package tc.oc.update;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import tc.oc.AresData;

import net.minecraft.src.mod_Ares;

public class Ares_Updater {

    public Ares_Updater(){
        String readline="";
        String readline2="Could not get update information.";
        try {
            //download link
            URL data = new URL("http://goo.gl/zG8cE");
            final BufferedReader in = new BufferedReader(new InputStreamReader(data.openStream()));
            readline = in.readLine();     
            readline2 = in.readLine();
        } catch (Exception e) {
            AresData.setUpdate(false);
            AresData.setUpdateLink("Could not get update information.");
        }
        if(!mod_Ares.MOD_VERSION.equalsIgnoreCase(readline)){
            AresData.setUpdate(false);
            AresData.setUpdateLink(readline2);
        }
    }
}
