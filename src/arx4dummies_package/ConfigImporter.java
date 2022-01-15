package arxprova_15_01;

import java.util.List;

//Adapter
public interface ConfigImporter {
	public void importFromFile(String s);
	public List<List<String>> getGeneralPrivacyModels();
	public List<List<String>> getSensitivePrivacyModels();
	public float getSuppressionLimit();
}
