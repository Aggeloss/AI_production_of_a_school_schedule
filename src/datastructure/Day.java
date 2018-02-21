package datastructure;

import java.util.ArrayList;

public class Day {

	private int hours;
	private int amountOfTotalClasses;
	private HourlyDuties[][] hourlySchedule;
	
	public Day(int hours, int amountOfTotalClasses) {
		this.hours = hours;
		this.amountOfTotalClasses = amountOfTotalClasses;
		this.hourlySchedule = new HourlyDuties[hours][amountOfTotalClasses];
		for(int hour=0; hour<hours; hour++) {
			for(int dept=0; dept<amountOfTotalClasses; dept++) {
				hourlySchedule[hour][dept] = new HourlyDuties(null, null, null);
			}
		}
	}

	public Day(Day day) {
		this.hours = day.hours;
		this.amountOfTotalClasses = day.amountOfTotalClasses;
		this.hourlySchedule = new HourlyDuties[hours][amountOfTotalClasses];
		for(int hour=0; hour<hours; hour++) {
			for(int dept=0; dept<amountOfTotalClasses; dept++) {
				hourlySchedule[hour][dept] = new HourlyDuties(day.hourlySchedule[hour][dept]);
			}
		}
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getAmountOfTotalClasses() {
		return amountOfTotalClasses;
	}

	public void setAmountOfTotalClasses(int amountOfTotalClasses) {
		this.amountOfTotalClasses = amountOfTotalClasses;
	}

	public HourlyDuties[][] getHourlySchedule() {
		return hourlySchedule;
	}

	public void setHourlySchedule(HourlyDuties[][] hourlySchedule) {
		this.hourlySchedule = hourlySchedule;
	}
	
	//We have to find a way in which we could sort teachers properly.
	public void createDailyProgram(Lesson lessonObj, Teacher teacherObj, String nameOfDepartment) {
		for( int i = 0; i<this.hours; i++ ) {
			int department = nameOfDepartment.equals("A")? 1 : (nameOfDepartment.equals("B")? 2 : 3);
			this.hourlySchedule[i][department] = new HourlyDuties(lessonObj, teacherObj, nameOfDepartment);
			this.setHourlySchedule(this.hourlySchedule);
		}
	}
	
	public void addOnlyOneHour(Lesson lessonObj, Teacher teacherObj, String nameOfDepartment) {
		int department = nameOfDepartment.equals("A")? 1 : (nameOfDepartment.equals("B")? 2 : 3);
		this.hourlySchedule[this.hours][department] = new HourlyDuties(lessonObj, teacherObj, nameOfDepartment);
		this.setHourlySchedule(this.hourlySchedule);
	}
	
	public String toString() {
		String result = "";
		for(int deptNumber=0; deptNumber<amountOfTotalClasses; deptNumber++) {
			for(int hour=0; hour<hours; hour++) {
				if(hourlySchedule[hour][deptNumber]!=null)
					result += String.valueOf(hour+1) + " hour : \r\n" + hourlySchedule[hour][deptNumber] + "\r\n";
			}
			result += "-------------------\r\n";
		}
		return result;
	}
	
	public void print() {
		System.out.println(toString());
		/*
		for( int i = 0; i<this.hourlySchedule.length; i++ ) {
			System.out.print(this.hourlySchedule[i].toString());
		}
		*/
	}

	public ArrayList<int[]> getConsecutiveEmptyTimeslotsBetweenLessonsPerDepartment() {
		int amountOfDepartments = getAmountOfDepartments();
		ArrayList<int[]> result = new ArrayList<>(amountOfDepartments);
		for(int deptNumber=0; deptNumber<amountOfDepartments; deptNumber++) {
			ArrayList<Integer> emptyTimeslots = new ArrayList<>();
			HourlyDuties[] duties = getHourlyDuties(deptNumber); 
			int emptyHours = 0;
			boolean hadClassBeforeEmptyTimeslots = false;
			for (int j=0; j<duties.length; j++) {
				if (duties[j].hasAssignment()) { // There is a lesson at this hour
					if (emptyHours>0) {
						emptyTimeslots.add(emptyHours);
						emptyHours = 0;
					}
					hadClassBeforeEmptyTimeslots = true;
				}
				else {
					if(hadClassBeforeEmptyTimeslots)
						emptyHours++;
				}
			}
			int[] emptyTimeslotsArray = new int[emptyTimeslots.size()];
			for (int i = 0; i < emptyTimeslotsArray.length; i++) {
				emptyTimeslotsArray[i] = emptyTimeslots.get(i);
			}
			result.add(emptyTimeslotsArray);
		}
		return result;
	}

	public HourlyDuties[] getHourlyDuties(int deptNumber) {
		HourlyDuties[] result = new HourlyDuties[getHours()];
		for(int i=0; i<result.length; i++) {
			result[i] = hourlySchedule[i][deptNumber];
		}
		return result;
	}

	public int getAmountOfDepartments() {
		if (hourlySchedule!=null)
			return hourlySchedule[0].length;
		System.out.println("Error getting amount of departments");
		return -1;
	}

	public int getAmountOfWorkingHours(Teacher teacher) {
		int totalWorkingHoursOfTeacher = 0;
		if (hourlySchedule!=null)
			for (int hour = 0; hour < hours; hour++) {
				for (int deptNumber = 0; deptNumber < getAmountOfDepartments(); deptNumber++) {
					Teacher hourlyScheduleTeacher = hourlySchedule[hour][deptNumber].getTeacher();
					if (hourlyScheduleTeacher != null && hourlyScheduleTeacher.equals(teacher))
						totalWorkingHoursOfTeacher++;
				}
			}
		return totalWorkingHoursOfTeacher;
	}

	public int getAmountOfLessonHours(Lesson lesson, int deptNumber) {
		int totalHoursOfLesson = 0;
		if (hourlySchedule!=null)
			for (int hour = 0; hour < hours; hour++) {
				Lesson hourlyScheduleLesson = hourlySchedule[hour][deptNumber].getCourse();
				if (hourlyScheduleLesson != null && hourlyScheduleLesson.equals(lesson))
					totalHoursOfLesson++;
			}
		return totalHoursOfLesson;
	}

	public int getAmountOfLessonHours(int deptNumber) {
		int totalHoursOfLessons = 0;
		if (hourlySchedule!=null)
			for (int hour = 0; hour < hours; hour++) {
				Lesson hourlyScheduleLesson = hourlySchedule[hour][deptNumber].getCourse();
				if (hourlyScheduleLesson != null)
					totalHoursOfLessons++;
			}
		return totalHoursOfLessons;
	}

	public int[] getDailyScheduleOfTeacher(Teacher teacher) {
		int[] teacherHours = new int[hours];
		for(int hour=0; hour<hours; hour++) {
			for(int dept=0; dept<amountOfTotalClasses; dept++) {
				HourlyDuties hd = hourlySchedule[hour][dept];
				if (hd.hasAssignment() && hd.getTeacher().equals(teacher)) {
					teacherHours[hour] += 1;
				}
			}
		}
		
		return teacherHours;
	}

}
