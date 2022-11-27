package stepDefinations;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class ObjectProperties{
	
	 static Map<?,?> property;
	 static String page;
	
	public static void initializeObjectProperties(String inputPageObjectFile) {
		try {
			page = inputPageObjectFile;
			String objectfilePath = "src/test/resources/page_objects/" + inputPageObjectFile + ".yml";
			Reader rd = new FileReader(objectfilePath);
			Yaml yml = new Yaml();
			property = (Map<?,?>) yml.load(rd);
			rd.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getElementProperty(String objectString) {
		Map<?,?> map = (Map<?,?>) property.get(page);
		return map.get(objectString).toString();
	}
}