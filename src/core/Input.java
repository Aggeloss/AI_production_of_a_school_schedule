package core;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import datastructure.Lesson;
import datastructure.Teacher;
import debug.Debug;

public class Input {

	public static String LESSONS_INPUT_FILE_PATH, TEACHERS_INPUT_FILE_PATH;

	static {
		LESSONS_INPUT_FILE_PATH = "resources/lessons.txt";
		TEACHERS_INPUT_FILE_PATH = "resources/teachers.txt";
	}

	public final ArrayList<Lesson> lessons;
	public final ArrayList<Teacher> teachers;
	
	public Input(String lessonsFilePathname, String teachersFilePathname, String delimiter) {
		if (lessonsFilePathname == null)
			lessonsFilePathname = LESSONS_INPUT_FILE_PATH;
		if (teachersFilePathname == null)
			teachersFilePathname = TEACHERS_INPUT_FILE_PATH;
		if (delimiter == null)
			delimiter = ",";
		lessons = parseSplittedLinesIntoLessonsObjects(readAllLinesAndSplit(lessonsFilePathname, delimiter));
		teachers = parseSplittedLinesIntoTeachersObjects(readAllLinesAndSplit(teachersFilePathname, delimiter));
	}

	public ArrayList<Lesson> getLessons() {
		return lessons;
	}
	
	public ArrayList<Teacher> getTeachers() {
		return teachers;
	}
	
	public static ArrayList<String[]> readAllLinesAndSplit(String filename, String delimiter) {
		
		// Declare and initialize variable that will store the result information
		ArrayList<String[]> readLines = new ArrayList<>();
		
		// Declare and open input file
		FileReader file = null;
		Scanner input = null;
		String line;
		try {
			file = new FileReader(filename);
			input = new Scanner(new BufferedReader(file));
		} catch (FileNotFoundException e) {
			Debug.printException(e);
			return null;
		}
		
		// Read all data from the file and store them into the readLines
		try {
			while (input.hasNextLine()) {
				line = input.nextLine().trim();
				if (!line.equals("")) {
					readLines.add(line.split(delimiter));
				}
			}
		}
		catch (Exception e) {
			Debug.printException(e);
			return null;
		}
		finally {
			// Close file
			try {
				input.close();
				file.close();
			} catch (IOException e) {
				Debug.printException(e);
				return null;
			}
		}
		
		
		return readLines;
	}

	public static ArrayList<Lesson> parseSplittedLinesIntoLessonsObjects(ArrayList<String []> splittedLines) {
		ArrayList<Lesson> lessons = new ArrayList<>();
		for (String[] line : splittedLines) {
			//Skip comments
			if(line.length>0 && line[0].startsWith("//")) {
				continue;
			}
			// Validation of line input
			if (line.length != 4) {
				Debug.println("Invalid amount of arguments while parsing a lesson: " + Debug.joinStringArray(line) );
			}
			else {
				String lessonID, lessonTitle, lessonClass;
				int lessonWeeklyHours = -1;
				lessonID = line[0].trim();
				lessonTitle = line[1].trim();
				lessonClass = line[3].trim();
				try {
					lessonWeeklyHours = Integer.parseInt(line[2].trim());
				} catch (NumberFormatException ex) {
					Debug.printException(ex);
				}
				Lesson lesson = new Lesson(lessonTitle, lessonID, lessonWeeklyHours, lessonClass); // TODO change this depending on the class
				lessons.add(lesson);
			}
		}
		return lessons;
	}

	public static ArrayList<Teacher> parseSplittedLinesIntoTeachersObjects(ArrayList<String []> splittedLines) {
		ArrayList<Teacher> teachers = new ArrayList<>();
		for (String[] line : splittedLines) {
			//Skip comments
			if(line.length>0 && line[0].startsWith("//")) {
				continue;
			}
			// Validation of line input
			if (line.length < 4) {
				Debug.println("Invalid amount of arguments while parsing a teacher: " + Debug.joinStringArray(line) );
			}
			else {
				String teacherID, teacherName; 
				int teacherWeeklyHours = -1;
				ArrayList<String> lessonsQualifiedTeaching = new ArrayList<>();
				teacherID = line[0].trim();
				teacherName = line[1].trim();
				try {
					teacherWeeklyHours = Integer.parseInt(line[2].trim());
				} catch (NumberFormatException ex) {
					Debug.printException(ex);
				}
				for (int i = 3; i < line.length; i++) {
					lessonsQualifiedTeaching.add( line[i].trim() );
				}
				Teacher teacher = new Teacher(teacherName, teacherID, lessonsQualifiedTeaching, teacherWeeklyHours); // TODO change this depending on the class
				teachers.add(teacher);
			}
		}
		return teachers;
	}
	
}
