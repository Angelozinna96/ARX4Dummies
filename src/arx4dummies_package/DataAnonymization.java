package arxprova_15_01;


import org.deidentifier.arx.ARXAnonymizer;
import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXPopulationModel.Region;
import org.deidentifier.arx.criteria.*;
import java.io.*;
import java.util.List;

public class DataAnonymization {
	//costant names used into the json file
	private final String subsetCountry = "USA" ;
	private final String subsetSelf = "Self";
	private final String caseKanon = "KAnonymity";
	private final String caseKMap = "KMap";
	private final String caseAvgReidRisk = "AverageReidentificationRisk";
	private final String caseDistDiv = "DistinctLDiversity";
	private final String caseEntrDiv = "EntropyLDiversity";
	private final String caseRecCLDDiv = "RecursiveCLDiversity";
	private final String caseEqDistTClos = "EqualDistanceTCloseness";
	
	//class attributes declaration
	private Data input;
	private ConfigImporter ci = null;
	private List<String> sensitiveAttrs;	
	private ARXConfiguration config = null;
	private ARXResult result = null;
	private ARXAnonymizer anonymizer=null;

	public DataAnonymization(Data inp,List<String> sens) 
	{
		this.input=inp;
		this.sensitiveAttrs=sens;
	}
	private boolean sensitiveAttrIsPresent(String sensAttr) {
		for (String s: sensitiveAttrs)
			if (s.equals(sensAttr))
				return true;	
		return false;
	}
	//General Privacy Models
	private PrivacyCriterion setKanonymity(List<String> params)
	{
		int param;
		//param checks
		try 
		{
			param=Integer.parseInt(params.get(1));
		
		}catch(Exception e)
		{
			System.out.println("setKanonymity: Type mismatch, no integer param detected!");
			return null;
		}
		return new KAnonymity(param);
					
	}
	private PrivacyCriterion setKmap(List<String> params)
	{
		int k;
		double significanceLevel;
		ARXPopulationModel populationmodel;
		//param checks
		if (params.size() < 4)
		{
			System.out.println("setKmap: insert 4 parameters!");
			return null;
		}	
		try 
		{
			k = Integer.parseInt(params.get(1));
			significanceLevel = Double.parseDouble(params.get(2));
				
			if (params.get(3).equals(subsetCountry))
				populationmodel = ARXPopulationModel.create(Region.USA);
			else if (params.get(3).equals(subsetSelf))
				populationmodel = ARXPopulationModel.create(input.getHandle().getNumRows(), 0.01d);
			else 
			{
				System.out.println("setKmap: parameter mismatch, subset does not match USA or Self!");
				return null;
			}
			return new KMap(k,significanceLevel,populationmodel);
		
		}
		catch(Exception e)
		{
			System.out.println("setKmap: Type mismatch on some parameter detected!");
			return null;
		}
	}
		
	private PrivacyCriterion setAvgReidRisk(List<String> params)
	{
		double param;
		//param checks
		try 
		{
			param=Double.parseDouble(params.get(1));
		
		}catch(Exception e)
		{
			System.out.println("setAvgReidRisk: Type mismatch, no double param detected!");
			return null;
		}
		return new AverageReidentificationRisk(param);
	}
	
	//Sensitive Privacy Models
	
	private PrivacyCriterion setDistLDiv(List<String> params)
	{
		int param;
		String sensAttr;
		//param checks
		try 
		{
			sensAttr = params.get(1);
			if (!sensitiveAttrIsPresent(sensAttr))
			{
				System.out.println("setDistLDiv: Sensitive Attribute \""+sensAttr+"\" is not present on the input dataset!");
				return null;
			}
			param = Integer.parseInt(params.get(2));
	
		}catch(Exception e)
		{
			System.out.println("setDistLDiv: Type mismatch, no int param detected!");
			return null;
		}
		return new DistinctLDiversity(sensAttr,param);
	}
	private PrivacyCriterion setEntropyLDiv(List<String> params)
	{
		int param;
		String sensAttr;
		//param checks
		sensAttr = params.get(1);
		if (!sensitiveAttrIsPresent(sensAttr))
		{
			System.out.println("setEntropyLDiv: Sensitive Attribute \""+sensAttr+"\" is not present on the input dataset!");
			return null;
		}
		try 
		{	
			param = Integer.parseInt(params.get(2));
	
		}catch(Exception e)
		{
			System.out.println("setEntropyLDiv: Type mismatch, no int param detected!");
			return null;
		}
		return new EntropyLDiversity(sensAttr,param);
	}
	private PrivacyCriterion setRecursiveCLDiv(List<String> params)
	{
		int param2;
		int param3;
		String sensAttr;
		//param checks
		sensAttr = params.get(1);
		if (!sensitiveAttrIsPresent(sensAttr))
		{
			System.out.println("setRecursiveCLDiv: Sensitive Attribute \""+sensAttr+"\" is not present on the input dataset!");
			return null;
		}
		try 
		{
			param2 = Integer.parseInt(params.get(2));
			param3 = Integer.parseInt(params.get(3));
	
		}catch(Exception e)
		{
			System.out.println("setRecursiveCLDiv: Type mismatch, no int param detected!");
			return null;
		}
		return new RecursiveCLDiversity(sensAttr,param2,param3);
	}
	private PrivacyCriterion setEqualDistanceTCloseness(List<String> params)
	{
		double param;
		String sensAttr;
		//param checks
		sensAttr = params.get(1);
		if (!sensitiveAttrIsPresent(sensAttr))
		{
			System.out.println("setEqualDistanceTCloseness: Sensitive Attribute \""+sensAttr+"\" is not present on the input dataset!");
			return null;
		}
		try 
		{
			param = Double.parseDouble(params.get(2));
	
		}catch(Exception e)
		{
			System.out.println("setEqualDistanceTCloseness: Type mismatch, no int param detected!");
			return null;
		}
		return new EqualDistanceTCloseness(sensAttr,param);
	}
	
	//Method of importing configurations
	
	private void getAnonConfig(String path) throws IOException{
		
		List<List<String>> genConfig;
		List<List<String>> sensConfig;
		float genSuppLim;
		// Create an instance of the anonymizer
        config = ARXConfiguration.create();
        ci = new JsonImporter(path);
        PrivacyCriterion res = null;
        String stringCase="";
        genConfig = ci.getGeneralPrivacyModels();
        genSuppLim = ci.getSuppressionLimit();
        sensConfig = ci.getSensitivePrivacyModels(); 
        //Setting suppression limit
        config.setSuppressionLimit(genSuppLim);
        
        //Setting privacy models for quasi-identifier attributes
        
        for (int i=0;i<genConfig.size();i++)
    	{
        	stringCase = genConfig.get(i).get(0);
        	
        	switch(stringCase)
        	{
        	case caseKanon:
        		res = setKanonymity(genConfig.get(i));break;
        	case caseKMap:
        		res = setKmap(genConfig.get(i));break;
        	case caseAvgReidRisk:
        		res = setAvgReidRisk(genConfig.get(i));break;
        	default:
        		res = null;
        	}
        	
        	if (res == null)
        	{
        		throw new IOException("Error in the anonymizer importing general privacy models process!");          		
        	}
        	config.addPrivacyModel(res);
        	
    	}
        
        //Settings privacy models for sensitive attributes
        for (int i=0;i<sensConfig.size();i++)
    	{
        	stringCase = sensConfig.get(i).get(0);
        	switch(stringCase)
        	{
        	case caseDistDiv:
        		res = setDistLDiv(sensConfig.get(i));break;
        	case caseEntrDiv:
        		res = setEntropyLDiv(sensConfig.get(i));break;
        	case caseRecCLDDiv:
        		res = setRecursiveCLDiv(sensConfig.get(i));break;
        	case caseEqDistTClos:
        		res = setEqualDistanceTCloseness(sensConfig.get(i));break;
        	default:
        		res = null;
        	}
        	
        	if (res == null)
        	{
        		throw new IOException("Error in the anonymizer importing sensitive privacy models process, exiting!");         		
        	}
        	config.addPrivacyModel(res);
    	}

        
	}
	public void importConfig(String configPath) throws IOException
	{ 
		 getAnonConfig(configPath);
	}
	public ARXResult anonymize() throws Exception
	{
		 this.anonymizer = new ARXAnonymizer();
		 this.result = anonymizer.anonymize(this.input, this.config);
		 return result;
	}
	public void saveResult(String path) throws IOException
	{
		result.getOutput(result.getGlobalOptimum(),false).save(path, ';');
	}

}
