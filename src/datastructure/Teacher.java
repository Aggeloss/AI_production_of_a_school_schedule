package datastructure;

import java.util.ArrayList;

public class Teacher {
	
	private String name;
	private String ID;
	private ArrayList<String> lessonsThatCanTeach;
	private int maximumHoursWeek;
	private int [] maximumHoursDay;
	
	public Teacher(String name, String ID, ArrayList<String> lessons, int maxHoursWeek) {
		
		this.name = name;
		this.ID    = ID;
		this.lessonsThatCanTeach = lessons;
		this.maximumHoursWeek = maxHoursWeek;
		
		this.maximumHoursDay = new int[5];
		for(int i=0; i<maximumHoursDay.length; i++)
			maximumHoursDay[i] = maximumHoursWeek;
	}
	

	public Teacher(String name, String ID, ArrayList<String> lessons, int maxHoursWeek, int[] maxHoursDay) {
		
		this.name = name;
		this.ID    = ID;
		this.lessonsThatCanTeach = lessons;
		this.maximumHoursWeek = maxHoursWeek;
		this.maximumHoursDay = maxHoursDay;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getLessonsThatCanTeach() {
		return lessonsThatCanTeach;
	}

	public void setLessonsThatCanTeach(ArrayList<String> lessonsThatCanTeach) {
		this.lessonsThatCanTeach = lessonsThatCanTeach;
	}

	public int getMaximumHoursWeek() {
		return maximumHoursWeek;
	}

	public void setMaximumHoursWeek(int maximumHoursWeek) {
		this.maximumHoursWeek = maximumHoursWeek;
	}

	public int[] getMaximumHoursDay() {
		return maximumHoursDay;
	}

	public void setMaximumHoursDay(int[] maximumHoursDay) {
		this.maximumHoursDay = maximumHoursDay;
	}
	
	public void setID( String ID ) {
		this.ID = ID;
	}
	
	public String getID() {
		return ID;
	}
	
	public String toString() {
		return "Teacher : " + getName() + "\r\n";
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof Teacher) {
			Teacher teacher = (Teacher) obj;
			return teacher.getID().equals(this.getID());
		}
		else
			return false;
	}


	public boolean canTeach(Lesson course) {
		return this.getLessonsThatCanTeach().contains(course.getID());
	}
	
} 
