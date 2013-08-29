package undercast.client.achievements2;

public class UndercastAchievement {

	public String killerName;
	public String line1;
	public String line2;
	public int achievementTime = 0;
	public float xOffset = 1;
	public UndercastAchievement(String name, String l1, String l2){
		killerName = name;
		line1 = l1;
		line2 = l2;
	}
}
