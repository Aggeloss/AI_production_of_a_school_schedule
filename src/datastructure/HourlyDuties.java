package datastructure;

public class HourlyDuties {
	
	private Lesson course;
	private Teacher teacher;
	private String department;
	
	public HourlyDuties(Lesson course, Teacher teacher, String department) {
		this.course = course;
		this.teacher = teacher;
		this.department = department;
	}
	
	public HourlyDuties(HourlyDuties hourlyDuties) {
		this.course = hourlyDuties.course;
		this.teacher = hourlyDuties.teacher;
		this.department = hourlyDuties.department;
	}

	public Lesson getCourse() {
		return course;
	}
	public void setCourse(Lesson course) {
		this.course = course;
	}
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String toString() {
		if (this.hasAssignment())
			return this.teacher.toString() + this.course.toString() + "Class  : " + getDepartment() + "\r\n";
		else
			return "";
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof HourlyDuties) {
			HourlyDuties hd = (HourlyDuties) obj;
			return hd.getCourse().equals(this.getCourse()) && hd.getDepartment().equals(this.getDepartment()) && hd.getTeacher().equals(this.getTeacher());
		}
		else
			return false;
	}

	public boolean hasAssignment() {
		return teacher!=null && course!=null;
	}
}
