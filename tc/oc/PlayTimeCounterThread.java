package tc.oc;

public class PlayTimeCounterThread extends Thread {
    public PlayTimeCounterThread() {
        try {
            // only start the Thread if the playing time is used
            if(AresConfig.showPlayingTime) {
                this.start();
            }
        } catch (Exception e) {
            System.out.println("[ProjectAres]: Playing Time Counter crashed");
            System.out.println("[ProjectAres]: ERROR: " + e.toString());
        }
    }
    
    public void run() {
        while (true) {
            try {
                Thread.sleep(60000);
                if(!AresData.isPA) {
                    return;
                }
                AresData.playTimeMin += 1;
                if(AresData.playTimeMin == 60) {
                    AresData.playTimeHours += 1;
                    AresData.playTimeMin = 0;
                }
            } catch (Exception ignored) {
            }
        }
    }
}
