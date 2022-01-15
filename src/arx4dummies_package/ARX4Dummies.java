package arxprova_15_01;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.Data;
import java.io.IOException;
/**
 * @author Angelo Zinna
 */
public class ARX4Dummies
{
	private InputHandler inputData = null;
	private Data inputFormattedData = null;
	private DataAnonymization processor = null;
	private ARXResult result = null;
	private ResultStats resultStats = null;
	private OutputStats inputStats = null;
	public ARX4Dummies() {}
	public ARX4Dummies(String inputDataPath,String inputSettingsPath) 
	{
		setInputHandler(inputDataPath,inputSettingsPath);
		setOutputStats();
	}
	public ARX4Dummies(String inputDataPath,String inputSettingsPath, String anonymizatorSettingsPath) 
	{		
		setInputHandler(inputDataPath,inputSettingsPath);
		setDataAnonymization(anonymizatorSettingsPath);
		setOutputStats();
		setResultStats();
			
	}
	public ARX4Dummies(String inputDataPath,String inputSettingsPath, String anonymizatorSettingsPath,String resultPath) 
	{		
		setInputHandler(inputDataPath,inputSettingsPath);
		setDataAnonymization(anonymizatorSettingsPath);
		setOutputStats();
		setResultStats();
		saveResults(resultPath);
	
	}
	
	private void setInputHandler(String inputDataPath,String inputSettingsPath)
	{
		try 
		{
			inputData = InputHandler.getInstance();
			inputData.initData(inputDataPath,inputSettingsPath);
			this.inputFormattedData = inputData.getFormattedData();
			
		}catch(Exception e)
		{
			System.out.println("[X] Error on importing the input settings!");
			e.printStackTrace();
		}
		
	}
	
	private void setDataAnonymization(String anonymizatorSettingsPath)
	{
		try 
		{
			this.processor = new DataAnonymization(this.inputFormattedData,this.inputData.getSensitiveAttributesNames());
			this.processor.importConfig(anonymizatorSettingsPath);
			this.result = this.processor.anonymize();			
			
		}catch(IOException e)
		{
			System.out.println("[X] Error on importing the anonymization settings!");
			e.printStackTrace();
		}
		catch(Exception e)
		{
			System.out.println("[X] Error on the anonymization process!");
			e.printStackTrace();
		}
		this.resultStats = null;
		this.inputStats = null;
	}
	
	private void setResultStats() 
	{
		try 
		{
			resultStats = new ResultStats(inputFormattedData.getHandle(),result);
		}catch(Exception e) 
		{
			System.out.println("[X] Error on generating result statistics!");
			e.printStackTrace();
		}
	}
	
	private void setOutputStats() 
	{
		try 
		{
			inputStats = new OutputStats(inputFormattedData.getHandle());
		}catch(Exception e) 
		{
			System.out.println("[X] Error on generating input statistics!");
			e.printStackTrace();
		}
	}
	public void saveResults(String resultPath) {
		if (this.processor == null || this.result == null ) 
		{
			System.out.println("[X] Error: Result data firstnot generated yet!");
			return;
		}
		try
		{
			processor.saveResult(resultPath);
			
		} catch(IOException e) 
		{
			System.out.println("[X] Error on writing the results!");
			e.printStackTrace();
		}
	}
	public void importInputData(String inputDataPath,String inputSettingsPath) {
		//Deletion of each processed value to prevent data inconsistency
		this.inputData = null;
		this.inputFormattedData = null;
		this.processor = null;
		this.result = null;
		this.resultStats = null;
		this.inputStats = null;
		setInputHandler(inputDataPath,inputSettingsPath);
		setOutputStats();
	}
	
	public void generateAnonymization(String anonymizatorSettingsPath) {
		if (this.inputData == null || this.inputFormattedData == null) 
		{
			System.out.println("[X] Error: Input data and input settings not imported!");
			return;
		}
		//Deletion of each processed value to prevent data inconsistency
		this.processor = null;
		this.result = null;
		this.resultStats = null;
		this.inputStats = null;
		
		setDataAnonymization(anonymizatorSettingsPath);
		setResultStats();
	}
	public void printResultStatistics() 
	{
		if (this.resultStats == null) 
		{
			System.out.println("[X] Error: Input data/input settings/anonymization settings not imported!");
			return;
		}
    	this.resultStats.printResultStatistics();
	}
	public void printResultData(int numRows) 
	{
		if (this.resultStats == null) 
		{
			System.out.println("[X] Error: Input data/input settings/anonymization settings not imported!");
			return;
		}
    	this.resultStats.printRawData(numRows);
	}
	public void printInputData(int numRows) {
		if (this.inputStats == null) 
		{
			System.out.println("[X] Error: Input data/input settings not imported!");
			return;
		}
		this.inputStats.printRawData(numRows);
	}
	public void printInputStatistics() 
	{
		if (this.inputStats == null) 
		{
			System.out.println("[X] Error: Input data/input settings not imported!");
			return;
		}
		this.inputStats.printInputStaticts();
	}
}