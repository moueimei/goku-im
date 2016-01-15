package com.goku.im.framework.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * 
 * @author zhaodx
 *
 */
public class IniParser 
{
	protected HashMap<String, Properties> sections = new HashMap<String, Properties>();
    private transient String currentSection;
    private transient Properties current;

    public IniParser(String filename) throws IOException 
    {
    	BufferedReader reader = new BufferedReader(new FileReader(filename));
    	read(reader);
    	reader.close();
    }

    protected void read(BufferedReader reader) throws IOException 
    {
    	String line;
    	while ((line = reader.readLine()) != null)
    		parseLine(line);
  
    	if(current != null)
    		sections.put(currentSection, current);
    }

    protected void parseLine(String line) 
    {
    	line = line.trim();
    	if(line.startsWith("#"))
    		return ;
	  
    	if (line.matches("\\[.*\\]"))
    	{
    		if (current != null) 
    		{
    			sections.put(currentSection, current);
    			current = null;
    		}
    		currentSection = line.replaceFirst("\\[(.*)\\]", "$1");           
    		current = new Properties();       
    	} 
    	else if (line.matches(".*=.*")) 
    	{
    		int i = line.indexOf('=');
    		String name = line.substring(0, i).trim();
    		String value = line.substring(i + 1).trim();
    		current.setProperty(name, value);
    	}
    }

    public String get(String section, String name) 
    {
    	Properties p = (Properties)sections.get(section);
        if (p == null)  	  
        	return null;
          
        String value = p.getProperty(name).trim();
        return value;
    }
    
    public boolean getBoolean(String section, String name)
    {
        String value = get(section, name);
        if( value == null)
        	return false;
        
        if( value.equals("0") || value.equals("no") || value.equals("false"))
        	return false;
        
        return true;
    }
    
    public int getInt(String section, String name)
    {
    	String value = get(section, name);
    	if( value == null)
    		return -1;
    	
    	int intValue = Integer.valueOf(value).intValue();
    	return intValue;
    }
    
    public float getFloat(String section, String name)
    {
    	String value = get(section, name);
    	if( value == null)
    		return -1.0f;
    	
    	float floatValue = Float.valueOf(value).floatValue();
    	return floatValue;
    }
    
    public String toString()
    {
    	return sections.toString();
    }
}
