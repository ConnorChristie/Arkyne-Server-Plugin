package us.arkyne.server.game;

public enum GameSubStatus
{
	PREGAME_STANDBY(-1),
	PREGAME_COUNTDOWN(10),
	
	GAME_COUNTDOWN(10),
	GAME_PLAYING(60),
	GAME_END(5);
	
	private int duration;
	
	private GameSubStatus(int duration)
	{
		this.duration = duration;
	}
	
	public int getDuration()
	{
		return duration;
	}
}