package core;

import java.util.ArrayList;

import datastructure.Lesson;
import datastructure.Schedule;
import datastructure.Teacher;

public class MainApplication {
	
	public static void main(String args[]) {

		ArrayList<Lesson> lessons;
		ArrayList<Teacher> teachers;
		
		Input filesInput = null;
		
		System.out.println("Reading input data...");
		switch (args.length) {
		case 1:
			filesInput = new Input(args[0],null,null);
			break;
		case 2:
			filesInput = new Input(args[0],args[1],null);
			break;
		case 3:
			filesInput = new Input(args[0],args[1],args[2]);
			break;
		default:
			filesInput = new Input(null,null,null);
			break;
		}
		
		lessons = filesInput.getLessons();
		teachers = filesInput.getTeachers();
		ArrayList<String> departments = new ArrayList<>();
		String[] depts = {"A","B","C"};
		int classesPerDepartment = 3;
		for (String dept : depts) {
			for (int i=1; i<classesPerDepartment+1; i++)
			departments.add(dept + i);
		}
		
		Schedule.lessons = lessons;
		Schedule.teachers = teachers;
		Schedule.departments = departments;
		
		Genetic_Algorithm ai = new Genetic_Algorithm();
		
		int populationSize = 50;
		int minimumFitness = 150;
		int maximumSteps = 500;
		double mutationProbability = 1;
		
		System.out.println("Initializing...");
		
		Schedule schedule = ai.geneticAlgorithm(populationSize, mutationProbability, minimumFitness, maximumSteps).getSchedule();
		
		//schedule.print();
		
		String outputFilename = "C:/Users/ANGELOS/Desktop/outputSchedule.txt";
		System.out.println("The selected schedule can be found in the file: " + outputFilename);
		schedule.outputToFile(outputFilename);
		
	}
	
}
