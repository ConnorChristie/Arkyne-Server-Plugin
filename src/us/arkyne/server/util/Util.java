package us.arkyne.server.util;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPluginLoader;

import sun.reflect.ConstructorAccessor;
import sun.reflect.FieldAccessor;
import sun.reflect.ReflectionFactory;
import us.arkyne.server.ArkyneMain;
import us.arkyne.server.inventory.InventoryPreset;
import us.arkyne.server.message.SignMessagePreset;

public class Util
{
	public static final String PREFIX = ChatColor.GOLD + "[Arkyne] ";
	private static final ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
	
	private static Field loadersF;
	private static SimpleCommandMap scm;
	
	private static Map<String, Command> kc;
	
	public static void noticeableConsoleMessage(String message)
	{
		ArkyneMain.getInstance().getLogger().severe("------------------------------------------------------------");
		ArkyneMain.getInstance().getLogger().severe(message);
		ArkyneMain.getInstance().getLogger().severe("------------------------------------------------------------");
	}
	
	//Load plugin jar and unload it with this
	
	@SuppressWarnings("unchecked")
	public static void unloadPlugin(Plugin plugin) throws Exception
	{
		PluginManager bpm = Bukkit.getServer().getPluginManager();
		
		if (!(bpm instanceof SimplePluginManager))
		{
			throw new Exception("Unknown Bukkit plugin system detected: " + bpm.getClass().getName());
		}
		
		SimplePluginManager spm = (SimplePluginManager) bpm;
		
		if (scm == null)
		{
			Field scmF = SimplePluginManager.class.getDeclaredField("commandMap");
			scmF.setAccessible(true);
			
			scm = ((SimpleCommandMap) scmF.get(spm));
			
			if (!(scm instanceof SimpleCommandMap))
			{
				throw new Exception("Unsupported Bukkit command system detected: " + scm.getClass().getName());
			}
		}
		
		if (kc == null)
		{
			Field kcF = scm.getClass().getDeclaredField("knownCommands");
			kcF.setAccessible(true);
			
			kc = ((Map<String, Command>) kcF.get(scm));
		}
		
		plugin.getClass().getClassLoader().getResources("*");
		
		Field lnF = spm.getClass().getDeclaredField("lookupNames");
		
		lnF.setAccessible(true);
		Map<String, Plugin> ln = (Map<String, Plugin>) lnF.get(spm);
		
		Field plF = spm.getClass().getDeclaredField("plugins");
		
		plF.setAccessible(true);
		List<Plugin> pl = (List<Plugin>) plF.get(spm);
		
		synchronized (scm)
		{
			Iterator<Map.Entry<String, Command>> it = kc.entrySet().iterator();
			
			while (it.hasNext())
			{
				Map.Entry<String, Command> entry = (Map.Entry<String, Command>) it.next();
				
				if ((entry.getValue() instanceof PluginCommand))
				{
					PluginCommand c = (PluginCommand) entry.getValue();
					
					if (c.getPlugin().getName().equalsIgnoreCase(plugin.getName()))
					{
						c.unregister(scm);
						
						it.remove();
					}
				}
			}
		}
		
		spm.disablePlugin(plugin);
		
		synchronized (spm)
		{
			ln.remove(plugin.getName());
			pl.remove(plugin);
		}
		
		JavaPluginLoader jpl = (JavaPluginLoader) plugin.getPluginLoader();
		
		if (loadersF == null)
		{
			loadersF = jpl.getClass().getDeclaredField("loaders");
			loadersF.setAccessible(true);
		}
		
		try
		{
			Map<String, ?> loaderMap = (Map<String, ?>) loadersF.get(jpl);
			
			loaderMap.remove(plugin.getDescription().getName());
		} catch (Exception e)
		{
			throw e;
		}
		
		closeClassLoader(plugin);
		
		System.gc();
		System.gc();
	}
	
	private static void closeClassLoader(Plugin plugin)
	{
		try
		{
			((URLClassLoader) plugin.getClass().getClassLoader()).close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void addInventoryPreset(String enumName, Class<?>[] types, Object[] params)
	{
		addEnum(InventoryPreset.class, enumName, types, params);
	}
	
	public static void addSignMessage(String enumName, Class<?>[] types, Object[] params)
	{
		addEnum(SignMessagePreset.class, enumName, types, params);
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends Enum<?>> void addEnum(Class<T> enumType, String enumName, Class<?>[] types, Object[] params)
	{
		// 0. Sanity checks
		if (!Enum.class.isAssignableFrom(enumType))
		{
			throw new RuntimeException("class " + enumType + " is not an instance of Enum");
		}
		
		// 1. Lookup "$VALUES" holder in enum class and get previous enum
		// instances
		Field valuesField = null;
		Field[] fields = enumType.getDeclaredFields();
		
		for (Field field : fields)
		{
			if (field.getName().contains("$VALUES"))
			{
				valuesField = field;
				
				break;
			}
		}
		
		AccessibleObject.setAccessible(new Field[] { valuesField }, true);
		
		try
		{
			// 2. Copy it
			T[] previousValues = (T[]) valuesField.get(enumType);
			List<T> values = new ArrayList<T>(Arrays.asList(previousValues));
			
			//2.5. Remove enum if already exists
			Iterator<T> valueIterator = values.iterator();
			
			while (valueIterator.hasNext())
			{
				T value = valueIterator.next();
				
				if (value.name().equalsIgnoreCase(enumName))
				{
					valueIterator.remove();
				}
			}
			
			// 3. build new enum
			T newValue = (T) makeEnum(enumType, // The target enum class
					enumName, // THE NEW ENUM INSTANCE TO BE DYNAMICALLY ADDED
					values.size(),
					types, // can be used to pass values to the enum constuctor
					params); // can be used to pass values to the enum constuctor
					
			// 4. add new value
			values.add(newValue);
			
			// 5. Set new values field
			setFailsafeFieldValue(valuesField, null, values.toArray((T[]) Array.newInstance(enumType, 0)));
			
			// 6. Clean enum cache
			cleanEnumCache(enumType);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static Object makeEnum(Class<?> enumClass, String value, int ordinal, Class<?>[] additionalTypes, Object[] additionalValues) throws Exception
	{
		Object[] parms = new Object[additionalValues.length + 2];
		
		parms[0] = value;
		parms[1] = Integer.valueOf(ordinal);
		
		System.arraycopy(additionalValues, 0, parms, 2, additionalValues.length);
		
		return enumClass.cast(getConstructorAccessor(enumClass, additionalTypes).newInstance(parms));
	}
	
	private static ConstructorAccessor getConstructorAccessor(Class<?> enumClass, Class<?>[] additionalParameterTypes) throws NoSuchMethodException
	{
		Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
		
		parameterTypes[0] = String.class;
		parameterTypes[1] = int.class;
		
		System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
		
		return reflectionFactory.newConstructorAccessor(enumClass.getDeclaredConstructor(parameterTypes));
	}
	
	private static void setFailsafeFieldValue(Field field, Object target, Object value) throws NoSuchFieldException, IllegalAccessException
	{
		
		// let's make the field accessible
		field.setAccessible(true);
		
		// next we change the modifier in the Field instance to
		// not be final anymore, thus tricking reflection into
		// letting us modify the static final field
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		int modifiers = modifiersField.getInt(field);
		
		// blank out the final bit in the modifiers int
		modifiers &= ~Modifier.FINAL;
		modifiersField.setInt(field, modifiers);
		
		FieldAccessor fa = reflectionFactory.newFieldAccessor(field, false);
		fa.set(target, value);
	}
	
	private static void cleanEnumCache(Class<?> enumClass) throws NoSuchFieldException, IllegalAccessException
	{
		blankField(enumClass, "enumConstantDirectory"); // Sun (Oracle?!?) JDK 1.5/6
		blankField(enumClass, "enumConstants"); // IBM JDK
	}
	
	private static void blankField(Class<?> enumClass, String fieldName) throws NoSuchFieldException, IllegalAccessException
	{
		for (Field field : Class.class.getDeclaredFields())
		{
			if (field.getName().contains(fieldName))
			{
				AccessibleObject.setAccessible(new Field[] { field }, true);
				setFailsafeFieldValue(field, enumClass, null);
				
				break;
			}
		}
	}
}