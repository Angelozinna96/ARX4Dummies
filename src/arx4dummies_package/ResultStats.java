package arxprova_15_01;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.ARXLattice.ARXNode;

public class ResultStats extends OutputStats{
	private OutputStats inp = null;
	private ARXResult result = null;

	public double TotalTime;
	private StringBuffer[] identifiers;
	private StringBuffer[] generalizations;
	private List<String> qis;
	public ResultStats(DataHandle input,ARXResult result) {
		super(result.getOutput(result.getGlobalOptimum(),false)); 
		inp = new OutputStats(input);
		this.result = result;
		generateResults();
	}
	
	private  void generateResults() {
		if (this.getDataHandle() == null) {
			System.out.println("[X]  Method not allowed!");
			return;
		}
		
	    this.TotalTime = this.result.getTime() / 1000d;
	    ARXNode optimum = this.result.getGlobalOptimum(); 
	    //generateGeneralStatistics(optResult);
	    this.suppRecords = this.getDataHandle().getStatistics().getEquivalenceClassStatistics().getNumberOfSuppressedRecords();
	    // Extract 
	    // da controllare in un vecchio commit se è inp o res!!!!!
	    this.qis = new ArrayList<String>(this.getDataHandle().getDefinition().getQuasiIdentifyingAttributes());
	    if (optimum == null) {
	        System.out.println("[X]  No solution found!");
	        return;
	    }
	    // Initialize
	    this.identifiers = new StringBuffer[this.qis.size()];
	    this.generalizations = new StringBuffer[this.qis.size()];
	    int lengthI = 0;
	    int lengthG = 0;
	    for (int i = 0; i < this.qis.size(); i++) {
	        this.identifiers[i] = new StringBuffer();
	        this.generalizations[i] = new StringBuffer();
	        this.identifiers[i].append(this.qis.get(i));
	        this.generalizations[i].append(optimum.getGeneralization(this.qis.get(i)));
	        if (this.getDataHandle().getDefinition().isHierarchyAvailable(this.qis.get(i)))
	            this.generalizations[i].append("/").append(this.getDataHandle().getDefinition().getHierarchy(this.qis.get(i))[0].length - 1);
	        lengthI = Math.max(lengthI, this.identifiers[i].length());
	        lengthG = Math.max(lengthG, this.generalizations[i].length());
	    }
	    // Padding
	    for (int i = 0; i < this.qis.size(); i++) {
	        while (this.identifiers[i].length() < lengthI) {
	            this.identifiers[i].append(" ");
	        }
	        while (this.generalizations[i].length() < lengthG) {
	            this.generalizations[i].insert(0, " ");
	        }
	    }
	}
	public void printResultStatistics() 
	{	
		String 
		str= " + Result Statistics\n";
		str+="   * Time needed: " + df.format(TotalTime) + " seconds \n";
		str+="   * Equivalence classes:\n";
		str+="     - Average size: "+df.format(EQAvg)+" (Previous: "+df.format(inp.EQAvg)+")\n";
		str+="     - Num classes : "+EQNumClasses+" (Previous: "+inp.EQNumClasses+")\n";
		str+="     - Records : "+EQRecords+" (Previous: "+inp.EQRecords+")\n";
		str+="     - Suppressed Records: "+suppRecords+" ("+df.format(((suppRecords*100)/inp.EQRecords))+" %)\n\n";
		str+="   * Risk estimates:\n";
		str+="     - Sample-based measures:\n";
		str+="       + Average risk     : "+df.format(RAvg)+" (Previous: "+df.format(inp.RAvg)+")\n";
		str+="       + Lowest risk      : "+df.format(RLowest)+" (Previous: "+df.format(inp.RLowest)+")\n";
		str+="       + Tuples affected  : "+df.format(RLowestTupleAff)+" (Previous: "+df.format(inp.RLowestTupleAff)+")\n";
		str+="       + Highest risk     : "+df.format(RHighest)+" (Previous: "+df.format(inp.RHighest)+")\n";
		str+="       + Tuples affected  : "+df.format(RHighestTupleAff)+" (Previous: "+df.format(inp.RHighestTupleAff)+")\n";
		str+="       + Sample uniqueness: "+df.format(RSampleUniq)+ " (Previous: "+df.format(inp.RSampleUniq)+")\n\n";
		str+="     - Population-based measures:\n";
		str+="       + Population unqiueness (Zayatz): "+df.format(RPopUniqueZayatz)+" (Previous: "+df.format(inp.RPopUniqueZayatz)+")\n\n";
		str+="	   - Mixed risks:\n";
		str+="       + Prosecutor re-identification risk: "+df.format(ProsecutorRisk)+" (Previous: "+df.format(inp.ProsecutorRisk)+")\n";
		str+="       + Journalist re-identification risk: "+df.format(JournalistRisk)+" (Previous: "+df.format(inp.JournalistRisk)+")\n";
		str+="       + Marketer re-identification risk  : "+df.format(MarketerRisk)+" (Previous: "+df.format(inp.MarketerRisk)+")\n\n";
		str+="   * Optimal generalization:\n";
		
	    for (int i = 0; i < qis.size(); i++) {
	        str+=("       + " + identifiers[i] + ": " + generalizations[i]+"\n");
	    }
		System.out.println(str);
	}

	public void printRawData(int numValue) 
	{	
		int c = numValue;
	    final Iterator<String[]> iterator = this.result.getOutput(false).iterator();
	    if (numValue<0)
	        while (iterator.hasNext()) 
	        {
	            System.out.print("   ");
	            System.out.println(Arrays.toString(iterator.next()));
	        }
	    else
		    while (iterator.hasNext() && c >= 0) 
		    {
		    	c--;
		        System.out.print("   ");
		        System.out.println(Arrays.toString(iterator.next()));
		    }
	}
}
