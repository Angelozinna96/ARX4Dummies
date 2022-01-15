package arx4dummies_package;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//Adapter - Target

public class JsonImporter implements ConfigImporter {
	private JSONObject obj;
	private final String JSON_GEN_SETTINGS="GeneralSettings";
	private final String JSON_SENS_SETTINGS="SensitiveAttrSettings";
	private final String JSON_PRIVACY_MODELS="PrivacyModel";
	private final String JSON_SUPPRESSION_LIMIT="setSuppressionLimit";

    private JSONObject extractObjFromJson(JSONObject jo,String str)
    {
	 	 try
	 	 {
	 			return jo.getJSONObject(str);
	 	 }
	 	catch (JSONException e) {
            e.printStackTrace();
        }
	 	return null;
	 	 
    }
    private JSONArray extractArrayFromObjJson(JSONObject jo,String str)
    {
	 	 try
	 	 {
	 			return jo.getJSONArray(str);
	 			
	 	 }
	 	catch (JSONException e) {
            e.printStackTrace();
        }
	 	return null;
	 	 
    }
    private JSONArray extractArrayFromArrayJson(JSONArray jo,int idx)
    {
	 	 try
	 	 { 		 	
	 			return jo.getJSONArray(idx);
	 			
	 	 }
	 	catch (JSONException e) {
            e.printStackTrace();
        }
	 	return null;
	 	 
    }
    private String extractStringFromArrayJson(JSONArray jo,int idx)
    {

	 	 try
	 	 {
	 		 	
	 			return jo.get(idx).toString();
	 			
	 	 }
	 	catch (JSONException e) {
            e.printStackTrace();
        }
	 	return null;
	 	 
    }
    private JSONObject getGeneralSettings() {return extractObjFromJson(obj,JSON_GEN_SETTINGS);}
    
    private JSONObject getSensitiveAttrSettings() {return extractObjFromJson(obj,JSON_SENS_SETTINGS);}
   
    //get the models list given the jsonobj(generalSettings/SensitiveAttributes) and the model name 
    private List<List<String>> getModels(JSONObject gs,String modelsName)
    {
    	try 
    	{
	    	List<List<String>> res = new ArrayList<List<String>>();
	    	
	    	JSONArray arr = extractArrayFromObjJson(gs,modelsName);
	    	for (int i=0;i<arr.length();i++)
	    	{
	    		JSONArray arr2 = extractArrayFromArrayJson(arr,i);
	    		ArrayList<String> el = new ArrayList<>();
	    		for(int j=0;j<arr2.length();j++)
	    		{
	    			
	    			el.add(extractStringFromArrayJson(arr.getJSONArray(i),j));
	    		}
	    		res.add(el);
	    	}
	    	return res;
    	}
    	catch (JSONException e) {
            e.printStackTrace();
         
        }
    	catch (Exception e) {
            e.printStackTrace();

        }
    	return null;
    }
    //public functions
 	public JsonImporter(String path){
 		importFromFile(path);
 	}
	public JsonImporter(){
		obj=null;
	} 
    public void importFromFile(String path){
        //JSON parser object to parse read file
	 	 try
	 	 {
	 		 List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
	 		 for(String l:lines)
	 		 {
	 			 //takes only the first line 
	 			obj = new JSONObject (l);
	 			break;
	 		 }
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	 	 
    }
 
    
    public List<List<String>> getGeneralPrivacyModels(){
    	return getModels(getGeneralSettings(),JSON_PRIVACY_MODELS);
    }
    
    public List<List<String>> getSensitivePrivacyModels(){
    	return getModels(getSensitiveAttrSettings(),JSON_PRIVACY_MODELS);
    }
    public float getSuppressionLimit() {
    	return getGeneralSettings().getFloat(JSON_SUPPRESSION_LIMIT);
    }
}
