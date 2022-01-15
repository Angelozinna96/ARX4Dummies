package arxprova_15_01;

public class Test {

	public static void main(String[] args) 
	{
		System.out.println("Test 1:");
		ARX4Dummies test1 = new ARX4Dummies("data/PoliceFatalitiesCleaned.csv","data/PoliceFatalitiesStruct.csv");
		test1.printInputStatistics();
		test1.printInputData(5);
		System.out.println("---------End Test 1---------\n");
		System.out.println("Test 2:");
		ARX4Dummies test2 = new ARX4Dummies("data/PoliceFatalitiesCleaned.csv","data/PoliceFatalitiesStruct.csv","data/processor.json");
		test2.printInputStatistics();
		test2.printInputData(5);
		test2.printResultStatistics();
		test2.printResultData(5);
		System.out.println("---------End Test 2---------\n");
		System.out.println("Test 3:");
		ARX4Dummies test3 = new ARX4Dummies("data/PoliceFatalitiesCleaned.csv","data/PoliceFatalitiesStruct.csv","data/processor.json","data/test3_results.csv");
		test3.printInputStatistics();
		test3.printInputData(5);
		test3.printResultStatistics();
		test3.printResultData(5);
		System.out.println("---------End Test 3---------\n");
		
		System.out.println("Test 4:");
		ARX4Dummies test4 = new ARX4Dummies();
		test4.importInputData("data/PoliceFatalitiesCleaned.csv","data/PoliceFatalitiesStruct.csv");
		test4.printInputStatistics();
		test4.printInputData(5);
		test4.generateAnonymization("data/processor.json");
		test4.printResultStatistics();
		test4.printResultData(5);
		test4.saveResults("data/test4_results.csv");
		System.out.println("---------End Test 4---------\n");
	}

}
