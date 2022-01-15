package arxprova_15_01;
import org.deidentifier.arx.DataType;
import org.deidentifier.arx.AttributeType;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.AttributeType.Hierarchy.DefaultHierarchy;
import org.deidentifier.arx.AttributeType.MicroAggregationFunction;

public class InputStruct {
	private int order;
	private String name;
	private DataType dtype=DataType.STRING;
	private AttributeType ptype=AttributeType.INSENSITIVE_ATTRIBUTE;
	private Hierarchy hierarchy=null;
	private int minHierarchyGen=-1;
	private int maxHierarchyGen=-1;
	public InputStruct(int o, String n, DataType dt,AttributeType pt)
	{
		order=o;
		name=n;
		dtype=dt;
		ptype=pt;
	}
	public InputStruct(int o, String n, DataType dt,AttributeType pt,Hierarchy h,int min,int max)
	{
		order=o;
		name=n;
		dtype=dt;
		ptype=pt;
		hierarchy=h;
		minHierarchyGen=min;
		maxHierarchyGen=max;
		
	}
	

	public void setOrder(int tmp) {this.order=tmp;}
	public void setName(String tmp) {this.name=tmp;}
	public void setHierarchy(DefaultHierarchy h) {this.hierarchy=h;}
	public void setDType(DataType dt) {this.dtype=dt;}
	public void setPType(AttributeType pt) {this.ptype=pt;}
	public void setMinGeneralization(int min) {this.minHierarchyGen=min;}
	public void setMaxGeneralization(int max) {this.maxHierarchyGen=max;}

	public int getOrder() {return this.order;}
	public String getName() {return this.name;}
	public DataType getDType() {return this.dtype;}
	public AttributeType getPType() {return this.ptype;}
	public Hierarchy getHierarchy() {return this.hierarchy;}
	public int getMinGeneralization() {return this.minHierarchyGen;}
	public int getMaxGeneralization() {return this.maxHierarchyGen;}
	

}
