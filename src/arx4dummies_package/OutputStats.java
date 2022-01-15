package arxprova_15_01;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Iterator;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.risk.RiskEstimateBuilder;
import org.deidentifier.arx.risk.RiskModelHistogram;
import org.deidentifier.arx.risk.RiskModelPopulationUniqueness;
import org.deidentifier.arx.risk.RiskModelSampleRisks;
import org.deidentifier.arx.risk.RiskModelSampleUniqueness;
import org.deidentifier.arx.risk.RiskModelPopulationUniqueness.PopulationUniquenessModel;

public class OutputStats {
	
	private DataHandle data = null;	
	protected DecimalFormat df = new DecimalFormat();
	
	//Equivalence classes
	public double EQAvg;
	public int EQNumClasses;
	public int EQRecords;
	public int suppRecords;
	//Risk estimates: Sample-based measures
	public double RAvg;
	public double RLowest;
	public double RLowestTupleAff;
	public double RHighest;
	public double RHighestTupleAff;
	public double RSampleUniq;
	public double RPopUniqueZayatz;
	//Mixed risks
	public double ProsecutorRisk;
	public double JournalistRisk;
	public double MarketerRisk;


private OutputStats() {}

public OutputStats(DataHandle data){	
	this.data = data;
	setFractionLimit(2);
	generateGeneralStatistics(); 
}
private void setFractionLimit(int lim) {
	df.setMinimumFractionDigits(lim);
	df.setMaximumFractionDigits(lim);
}

private void generateGeneralStatistics() {
	
	ARXPopulationModel populationmodel = ARXPopulationModel.create(this.data.getNumRows(), 0.01d);
    //ARXPopulationModel populationmodel = ARXPopulationModel.create(Region.USA);
    RiskEstimateBuilder builder = this.data.getRiskEstimator(populationmodel);
    RiskModelHistogram classes = builder.getEquivalenceClassModel();
    RiskModelSampleRisks sampleReidentifiationRisk = builder.getSampleBasedReidentificationRisk();
    RiskModelSampleUniqueness sampleUniqueness = builder.getSampleBasedUniquenessRisk();
    RiskModelPopulationUniqueness populationUniqueness = builder.getPopulationBasedUniquenessRisk();
  //ARXPopulationModel populationmodel = ARXPopulationModel.create(data.getHandle().getNumRows(), 0.01d);
    //riskmodel = builder.getAttributeRisks();
    
    EQAvg = classes.getAvgClassSize();
	EQNumClasses = (int) classes.getNumClasses();
	EQRecords = (int) classes.getNumRecords();
	//Risk estimates: Sample-based measures
	RAvg = sampleReidentifiationRisk.getAverageRisk();
	RLowest = sampleReidentifiationRisk.getLowestRisk();
	RLowestTupleAff = sampleReidentifiationRisk.getFractionOfRecordsAffectedByLowestRisk();
	RHighest = sampleReidentifiationRisk.getHighestRisk();
	RHighestTupleAff = sampleReidentifiationRisk.getFractionOfRecordsAffectedByHighestRisk();
	RSampleUniq = sampleUniqueness.getFractionOfUniqueRecords();
	RPopUniqueZayatz = populationUniqueness.getFractionOfUniqueTuples(PopulationUniquenessModel.ZAYATZ);
	//Mixed risks
	ProsecutorRisk = this.data.getRiskEstimator(populationmodel).getSampleBasedReidentificationRisk().getEstimatedProsecutorRisk();
	JournalistRisk = this.data.getRiskEstimator(populationmodel).getSampleBasedReidentificationRisk().getEstimatedJournalistRisk();
	MarketerRisk = this.data.getRiskEstimator(populationmodel).getSampleBasedReidentificationRisk().getEstimatedMarketerRisk();
}
protected DataHandle getDataHandle() {return this.data;}

public void printInputStaticts() 
{
	String
	 str= " + Input Statistics\n";
	str+="   * Equivalence classes:\n";
	str+="     - Average size: "+df.format(EQAvg)+"\n";
	str+="     - Num classes : "+EQNumClasses+"\n";
	str+="     - Records : "+EQRecords+"\n\n";
	str+="   * Risk estimates:\n";
	str+="     - Sample-based measures:\n";
	str+="       + Average risk     : "+df.format(RAvg)+"\n";
	str+="       + Lowest risk      : "+df.format(RLowest)+"\n";
	str+="       + Tuples affected  : "+df.format(RLowestTupleAff)+"\n";
	str+="       + Highest risk     : "+df.format(RHighest)+"\n";
	str+="       + Tuples affected  : "+df.format(RHighestTupleAff)+"\n";
	str+="       + Sample uniqueness: "+df.format(RSampleUniq)+"\n\n";
	str+="     - Population-based measures:\n";
	str+="       + Population unqueness (Zayatz): "+df.format(RPopUniqueZayatz)+"\n\n";
	str+="	   - Mixed risks:\n";
	str+="       + Prosecutor re-identification risk: "+df.format(ProsecutorRisk)+"\n";
	str+="       + Journalist re-identification risk: "+df.format(JournalistRisk)+"\n";
	str+="       + Marketer re-identification risk  : "+df.format(MarketerRisk)+"\n\n";
	System.out.println(str);
}
//function to get a sample of raw results. If numValue == -1 it returns all results  
	public void printRawData(int numValue) 
	{
		
		int c = numValue;
	    final Iterator<String[]> iterator = this.data.iterator();
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
