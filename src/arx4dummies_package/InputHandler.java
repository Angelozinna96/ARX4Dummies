package arx4dummies_package;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.deidentifier.arx.AttributeType;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataSource;
import org.deidentifier.arx.DataType;
import org.deidentifier.arx.AttributeType.Hierarchy;


public class InputHandler{
	
	public static String DATE_FORMAT="MM.dd.yyyy";
	
	private static InputHandler singleton = new InputHandler();
	private DataSource source;
	private Data data;
	private ArrayList<InputStruct> contents;
	//private functions
	private InputHandler(){
		this.contents = new ArrayList<>();
		this.source = null;
	}
	
	private int importCSVData(String strPath){	
		
		File f = new File(strPath);
		if(!f.exists())
		{
			System.out.println("Can not find file: "+strPath+" !");
			return -1;
		}
		this.source = DataSource.createCSVSource(strPath, StandardCharsets.UTF_8, ';',true);
		return 0;
		}
	
	private int importCSVStruct(String strPath){
		String name,strType,strPriv,strMode, attr[];
    	DataType type;
    	AttributeType atype;
    	Hierarchy hierarchy=null;
    	int minGen,maxGen;
    	
    	Path pathToFile = Paths.get(strPath);
    	int i=0;
    	File f = new File(strPath);
		if(!f.exists())
		{
			System.out.println("Can not find file: "+strPath+" !");
			return -1;
		}
    	try (BufferedReader br = Files.newBufferedReader(pathToFile,StandardCharsets.US_ASCII)) 
    	{
    		// read the first line from the text file 
    		String line = br.readLine();
    		// select the right datatype
    		while (line != null) 
    		{
    			
    			attr = line.split(";");
    			name = attr[0];
    			strType = attr[1];
    			strPriv = attr[2];
    			strMode = attr[3];
    			try {
    			minGen = Integer.parseInt(attr[4]);
    			maxGen = Integer.parseInt(attr[5]);
    			}catch (NumberFormatException ex){
    				System.out.println(line);
    	            ex.printStackTrace();
    	            i++;
        			hierarchy=null;
        			line = br.readLine();
    	            continue;
    	        }
    			switch(strType)
    			{			
    				case "INTEGER":
    					type=DataType.INTEGER; break;
    				  			
    				case "DATA": 
    					type=DataType.DATE; break;
    					
    				case "STRING": 					
    				default:
    					type=DataType.STRING;
    			}
    			switch(strPriv)
    			{			
    				case "ID":
    					atype=AttributeType.IDENTIFYING_ATTRIBUTE;break;  				  			
    				case "SENS": 
    					atype=AttributeType.SENSITIVE_ATTRIBUTE; break;					
    				case "QUID": 
    					atype=AttributeType.QUASI_IDENTIFYING_ATTRIBUTE; break;
    				case "UNID": 
    				default:
    					atype=AttributeType.INSENSITIVE_ATTRIBUTE;
    			}
    			
    			switch(strMode)
    			{			
    				case "NULL":
    					break;
    				default:
    					hierarchy = Hierarchy.create(strMode, Charset.defaultCharset(), ';');
    			}
    			this.contents.add(new InputStruct(i, name, type, atype,hierarchy,minGen,maxGen));
    			this.source.addColumn(i, name, type, true);
    			i++;
    			hierarchy=null;
    			line = br.readLine();
    		}
    		
        }
    	catch (IOException ioe) 
    	{
    		ioe.printStackTrace();
			return -2;
        }
    	catch (Exception ioe) 
    	{
    		ioe.printStackTrace();
			return -3;
        }
    	return 0;
	}
	
	private void createData() throws Exception {
		this.data = Data.create(source);
	}
	
	private void addGenericDef(String name, AttributeType at) {
		this.data.getDefinition().setAttributeType(name,at);
		}
	
	private void addDateDef(String name, AttributeType at,String format) {
		this.data.getDefinition().setAttributeType(name,at);
		this.data.getDefinition().setDataType(name, DataType.createDate(format));
		}
	
	private void createDataDef(){
		for(InputStruct el: contents) 
		{
			if (el.getDType() == DataType.STRING)
				addDateDef(el.getName(), el.getPType(),DATE_FORMAT);
			else
				addGenericDef(el.getName(), el.getPType());
		}
	}
	
	private void setHierarchy(String name,Hierarchy h){
		this.data.getDefinition().setHierarchy(name, h);
	}
	private void setMinGeneralization(String name,int min){
		this.data.getDefinition().setMinimumGeneralization(name, min);
	}
	private void setMaxGeneralization(String name,int max){
		this.data.getDefinition().setMaximumGeneralization(name, max);
	}
	
	private void setHierarchies(){
		for (InputStruct el: contents)
		{
			if (el.getHierarchy() != null)
			{
				setHierarchy(el.getName(),el.getHierarchy());
				if (el.getMinGeneralization() !=-1) 
					setMinGeneralization(el.getName(),el.getMinGeneralization());
				if (el.getMaxGeneralization() !=-1) 
					setMaxGeneralization(el.getName(),el.getMaxGeneralization());
			}
		}
	}

		
		
	//public functions
	
	public static InputHandler getInstance() {return singleton;}
	public DataSource getSource() {return source;}
	public ArrayList<InputStruct> getAttributes() {return contents;}
	public Data getFormattedData() {return data;}	
	
	public List<InputStruct> getSensitiveAttributes(){
		ArrayList<InputStruct> tmp=new ArrayList<> ();
		for (InputStruct el: contents)
		{
			if (el.getPType() == AttributeType.SENSITIVE_ATTRIBUTE)
			{
				tmp.add(el);
			}
		}
		return tmp;
	}
	public List<String> getSensitiveAttributesNames(){
		ArrayList<String> tmp=new ArrayList<> ();
		for (InputStruct el: contents)
		{
			if (el.getPType() == AttributeType.SENSITIVE_ATTRIBUTE)
			{
				tmp.add(el.getName());
			}
		}
		return tmp;
		
	}
	public List<InputStruct> getQuasiIdentifiedAttributes(){
		ArrayList<InputStruct> tmp=new ArrayList<> ();
		for (InputStruct el: contents)
		{
			if (el.getPType() == AttributeType.QUASI_IDENTIFYING_ATTRIBUTE)
			{
				tmp.add(el);
			}
		}
		return tmp;
		
	}
	public List<String> getQuasiIdentifiedAttributesNames(){
		ArrayList<String> tmp=new ArrayList<> ();
		for (InputStruct el: contents)
		{
			if (el.getPType() == AttributeType.QUASI_IDENTIFYING_ATTRIBUTE)
			{
				tmp.add(el.getName());
			}
		}
		return tmp;
		
	}
	public InputStruct getAttributeByName(String name){
		
		for (InputStruct el: contents)
		{
			if (name.equals(el.getName()))
			{
				return el;
			}
		}
		return null;
		
	}
	public int initData(String dataPath, String structPath) throws Exception {

		int error;	
		error=importCSVData(dataPath);
		if (error < 0)
		{
			System.out.println("Error found on inporting CSV input file, exiting!");
			return -1;
		}
		error=importCSVStruct(structPath);
		if (error < 0)
		{
			System.out.println("Error found on inporting CSV struct file, exiting!");
			return -2;
		}
		createData();
		createDataDef();
		setHierarchies();
		return 0;
		
	}
	

}
