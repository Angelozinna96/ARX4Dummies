package arx4dummies_package;

import java.util.List;

//Adapter
public interface ConfigImporter {
	public int importFromFile(String s);
	public List<List<String>> getGeneralPrivacyModels();
	public List<List<String>> getSensitivePrivacyModels();
	public float getSuppressionLimit();
}
