package us.arkyne.server.message;

public interface SignMessage
{
	public String name();
	
	public String getLine(int index);
	
	public String replace(int index, String search, String replace);
	
	public String[] getLines();
}