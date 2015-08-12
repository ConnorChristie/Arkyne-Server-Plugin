package us.arkyne.server.message;

import us.arkyne.server.util.Util;

public class SignMessageHandler
{
	public static void registerSignMessagePresets(SignMessage[] signs)
	{
		for (SignMessage preset : signs)
		{
			Util.addInventoryPreset(preset.name(), new Class<?>[] { String[].class }, new Object[] { preset.getLines() });
		}
	}
}