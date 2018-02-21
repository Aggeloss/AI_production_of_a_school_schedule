package datastructure;

public class Lesson {

	private String nameOfLesson;
	private String ID;
	private int demandedHours;
	private String departmentTeached;
	
	public Lesson(String nameOfLesson, String ID, int demandedHours, String departmentTeached) {
		
		this.ID = ID;
		this.nameOfLesson = nameOfLesson;
		this.demandedHours = demandedHours;
		this.departmentTeached = departmentTeached;
		
	}
	
	public String getDepartmentTeached() {
		return departmentTeached;
	}

	public void setDepartmentTeached(String departmentTeached) {
		this.departmentTeached = departmentTeached;
	}

	public void setNameOfLesson( String name ) {
		nameOfLesson = name;
	}
	
	public String getNameOfLesson() {
		return nameOfLesson;
	}
	
	public void setID( String ID ) {
		this.ID = ID;
	}
	
	public String getID() {
		return ID;
	}
	
	public void setDemandedHours( int dh ) {
		demandedHours = dh;
	}
	
	public int getDemandedHours() {
		return demandedHours;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof Lesson) {
			Lesson lesson = (Lesson) obj;
			return lesson.getID().equals(this.getID()) && lesson.getDemandedHours()== this.getDemandedHours();
		}
		else
			return false;
	}
	
	public String toString() {
		return "Lesson : " + getNameOfLesson() + " " + getID() + " " + getDemandedHours() + " " + getDepartmentTeached() + "\r\n";
	}
	
}
