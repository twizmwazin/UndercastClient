package undercast.network.common.packet;

import java.util.HashMap;

public class VIPUser {

    public static HashMap<String, Integer> capes = new HashMap<String, Integer>();
    static{
        capes.put("DEVELOPER_CAPE",0);
        capes.put("DONATOR_CAPE",1);
        capes.put("DONATOR_PLUS_CAPE",2);
        capes.put("VIP_CAPE",3);
        capes.put("USER_CAPE",4);
    }
    private String username;
    private boolean hasCape;
    private int cape;

    public VIPUser(String username, boolean hasCape, int cape) {
        this.username = username;
        this.hasCape = hasCape;
        this.cape = cape;
    }

    public String toString() {
        return this.username + ";" + this.hasCape + ";" + this.cape;
    }

    public static VIPUser fromString(String str) {
        String[] infos = str.split(";");
        String username, hasCape, cape;
        if (infos.length != 3) {
            return null;
        } else {
            username = infos[0];
            hasCape = infos[1];
            cape = infos[2];
            if (!hasCape.equals("true") && !hasCape.equals("false")) {
                return null;
            }
            int capeType;
            try {
                capeType = Integer.parseInt(cape);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return new VIPUser(username, Boolean.parseBoolean(hasCape), capeType);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean hasCape() {
        return hasCape;
    }

    public void setHasCape(boolean hasCape) {
        this.hasCape = hasCape;
    }

    public int getCape() {
        return cape;
    }

    public void setCape(int cape) {
        this.cape = cape;
    }
}
