package undercast.client;

import java.util.Timer;
import java.util.TimerTask;

public class MatchTimer {

    Timer timer;

    public MatchTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // this is for all maps without time limit
                // and the else for the ones with time limit
                if (UndercastData.incrementMatchTime) {
                    // seconds
                    UndercastData.matchTimeSec += 1;
                    // minutes
                    if (UndercastData.matchTimeSec == 60) {
                        UndercastData.matchTimeSec = 0;
                        UndercastData.matchTimeMin += 1;
                    }
                    // hours
                    if (UndercastData.matchTimeMin == 60) {
                        UndercastData.matchTimeMin = 0;
                        UndercastData.matchTimeHours += 1;
                    }
                } else {
                    // seconds
                    UndercastData.matchTimeSec -= 1;
                    // minutes
                    if (UndercastData.matchTimeSec == -1) {
                        UndercastData.matchTimeSec = 59;
                        UndercastData.matchTimeMin -= 1;
                    }
                    // hours
                    if (UndercastData.matchTimeMin == -1) {
                        UndercastData.matchTimeMin = 59;
                        UndercastData.matchTimeHours -= 1;
                    }
                    // stop the timer if it reaches 0 or the it's game over
                    if ((UndercastData.matchTimeSec <= 0 && UndercastData.matchTimeMin <= 0 && UndercastData.matchTimeHours <= 0) || UndercastData.isGameOver) {
                        this.cancel();
                    }
                    //stop the timer if hours are negative, it indicates that it wants to decrement from 0:00
                    if (UndercastData.matchTimeHours < 0) {
                        this.cancel();
                        UndercastData.matchTimeHours = 0;
                        UndercastData.matchTimeMin = 0;
                        UndercastData.matchTimeSec = 0;
                        UndercastData.incrementMatchTime = true;

                    }
                }
            }
        }, 1000, 1000);
    }

    public void stop() {
        try {
            timer.cancel();
        } catch (Exception e) {
        }
    }
}
