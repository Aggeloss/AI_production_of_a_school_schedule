package datastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import javax.print.attribute.HashAttributeSet;

import core.Output;
import core.ScheduleScore;
import debug.Debug;


public class Schedule {
	
	private Day[] schedule;
	public static int amountOfHoursPerDay, amountOfDays;
	public static ArrayList<Lesson> lessons;
	public static ArrayList<Teacher> teachers;
	public static ArrayList<String> departments;
	
	static { 
		amountOfHoursPerDay = 7;
		amountOfDays = 5;
	}

	public Schedule() {
		this.schedule = new Day[amountOfDays];
		for(int i=0; i<amountOfDays; i++)
			this.schedule[i] = new Day(amountOfHoursPerDay, departments.size());
	}
	
	public Schedule(Schedule sch) {
		this.schedule = new Day[sch.getAmountOfDays()];
		for(int i=0; i<sch.getAmountOfDays(); i++)
			this.schedule[i] = new Day(sch.getDay(i));
	}
	
	public String toString() {
		String result = "";

		for( int i = 0; i<amountOfDays; i++ ) {
			result += "------" + " Day " + (i + 1)  + " ------\r\n";
			result += schedule[i].toString();
		}
		
		return result;
	}
	
	public void print() {
		System.out.print(this.toString());
	}
	
	public int getAmountOfHoursPerDay() {
		return amountOfHoursPerDay;
	}

	public void setAmountOfHoursPerDay(int amountOfHoursPerDay) {
		this.amountOfHoursPerDay = amountOfHoursPerDay;
	}

	public ArrayList<Lesson> getLessons() {
		return lessons;
	}

	public void setLessons(ArrayList<Lesson> lessons) {
		this.lessons = lessons;
	}

	public ArrayList<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(ArrayList<Teacher> teachers) {
		this.teachers = teachers;
	}

	public ArrayList<String> getDepartments() {
		return departments;
	}

	public void setDepartments(ArrayList<String> departments) {
		this.departments = departments;
	}

	public Schedule(Day[] schedule) {
		this.schedule = schedule;
	}

	public Day[] getSchedule() {
		return schedule;
	}

	public void setSchedule(Day[] schedule) {
		this.schedule = schedule;
	}

	public int getAmountOfDays() {
		return schedule.length;
	}

	public Day getDay(int day) {
		if (day>=0 && day<schedule.length)
			return schedule[day];
		else
			return null;
	}

	public int getAmountOfDepartments() {
		return departments.size();
	}

	public ArrayList<int[]> getAmountOfLessonsHoursPerDepartment() {
		ArrayList<int[]> result = new ArrayList<>();
		for(int deptNumber = 0; deptNumber<getAmountOfDepartments(); deptNumber++) {
			int[] hoursPerDay = new int[amountOfDays];
			for(int i=0; i<hoursPerDay.length; i++) {
				hoursPerDay[i] = schedule[i].getAmountOfLessonHours(deptNumber);
			}
			result.add(hoursPerDay);
		}
		return result;
	}

	public ArrayList<Lesson> getLessonsPerDepartment(int deptNumber) {
		ArrayList<Lesson> result = new ArrayList<>();
		for(Lesson lesson : lessons) {
			String departmentName = departments.get(deptNumber);
			if (departmentName.startsWith(lesson.getDepartmentTeached()))
				result.add(lesson);
		}
		return result;
	}

	public int[] getAmountOfLessonHoursPerDayInDepartment(Lesson lesson, int deptNumber) {
		int[] result = new int[schedule.length];
		int i = 0;
		for(Day day : schedule) {
			result[i] = day.getAmountOfLessonHours(lesson, deptNumber); 
			i++;
		}
		return result;
	}

	public int getAmountOfTeachers() {
		return teachers.size();
	}

	public int getTotalAmountOfWorkingHours(Teacher teacher) {
		int totalAmountOfWorkingHours = 0;
		for(Day day : schedule)
			totalAmountOfWorkingHours += day.getAmountOfWorkingHours(teacher);
		return totalAmountOfWorkingHours;
	}

	public void swapDays() {
		int halfWay = (int) Math.floor(this.getAmountOfDays()/2);
		for(int i=0; i < halfWay; i++)
			swap(schedule,schedule,i,halfWay+i);
		if (!(this.getAmountOfDays()%2 ==0)) // If we have e.g. 5 days
			swap(schedule,schedule,this.getAmountOfDays()-1,this.getAmountOfDays()-2);
		
		/*
		// Just keeping it for you man, just for you! :P
        Day[] firstTwoDays = new Day[2];
        Day[] nextTwoDays = new Day[2];
        Day[] finalDay = new Day[1];
        
        for(int i = 0; i < 2; i++) {
            firstTwoDays[i] = schedule[i];
        }
        
        int j = 2;
        
        for(int i = 0; i < 2; i++) {
            nextTwoDays[i] = schedule[j++];
        }
        
        j = 4;
        
        for(int i = 0; i < 1; i++) {
            finalDay[i] = schedule[j++];
        }
        
        for(int i = 0; i < 2; i++) {
            swap(firstTwoDays, nextTwoDays, i, i);
        }
        
        swap(nextTwoDays, finalDay, 1, 0);
        
        Day[] newPanel = new Day[5];
        
        boolean statement = true;
        
        int i = 0;
        
        int j1 = 2;
        
        int j2 = 0;
        
        while(statement) {
            if(i < 2) {
                newPanel[i] = firstTwoDays[i];
            }
            if(i >= 2) {
        		System.out.print("bla : " + j1 + "\n");
                newPanel[j1] = nextTwoDays[j2++];
                if(j1 == 3) {
                	System.out.print("e3upnos!!");
                    statement = false;
                    break;
                }
                j1++;
            }
            i++;
        }
        newPanel[4] = finalDay[0];
        
        schedule = newPanel;
		 */
		
    }
	
	public static final <T> void swap (T[] a, T[] b, int i, int j) {
		T t = a[i];
		a[i] = b[j];
		b[j] = t;
	}

	public static Schedule createRandomSchedule() {
		// TODO createRandomSchedule() Auto-generated method stub
		Schedule schedule = new Schedule();
		HashMap<String, Integer> lessonHoursLeftToDo = new HashMap<>();
		for(String dept : departments) {
			for (Lesson lesson : lessons) {
				if (dept.toLowerCase().startsWith(lesson.getDepartmentTeached().toLowerCase())) {
					lessonHoursLeftToDo.put(dept + lesson.getID(), lesson.getDemandedHours());
				}
			}
		}
		
		
		for(int hour=0; hour<Schedule.amountOfHoursPerDay; hour++) {
			for(int day=0; day<Schedule.amountOfDays; day++) {
				for(int dept=0; dept<departments.size(); dept++) {
					String deptName = departments.get(dept);
					Day dayObject = schedule.getDay(day);
					HourlyDuties[][] hourlyDuties = dayObject.getHourlySchedule();
					HourlyDuties hd = hourlyDuties[hour][dept];
					//TODO silly way, write a better one
					boolean assigned = false;
					while (!assigned) {
						Lesson lesson = getRandomLesson(deptName,lessonHoursLeftToDo);
						if (lesson == null) {
							assigned = true;
							continue;
						}
						String key = deptName+lesson.getID();
						if (lessonHoursLeftToDo.containsKey(key)) {
							int hoursLeft = lessonHoursLeftToDo.get(key);
							if (hoursLeft > 0) {
								//Debug.println("score before: " + ScheduleScore.getScoreBasedOnAllLessonsPerDepartmentTeached(schedule));
								Teacher teacher = getRandomTeacher(lesson, schedule,day,hour,dept);
								hd.setCourse(lesson);
								hd.setDepartment(deptName);
								hd.setTeacher(teacher);
								hoursLeft--;
								lessonHoursLeftToDo.put(key, hoursLeft);
								assigned = true;
								if (hoursLeft==0) {
									lessonHoursLeftToDo.remove(key);
								}
								//Debug.println("score after: " + ScheduleScore.getScoreBasedOnAllLessonsPerDepartmentTeached(schedule));
							}
						}
					}
				}
			}
		}
		//schedule.print();
		Debug.println("Random schedule created. ");
		//Debug.println("Score for all lessons teached: " + ScheduleScore.getScoreBasedOnAllLessonsPerDepartmentTeached(schedule));
		//Debug.println("Score for teachers should be in one place: " + ScheduleScore.getScoreBasedOnTeacherShouldBeInOnePlace(schedule));
		//Debug.println("Score: " + ScheduleScore.getScoreBasedOnSimilarDistributedAmountOfHoursPerLesson(schedule));
		return schedule;
	}

	public static Teacher getRandomTeacher(Lesson lesson, Schedule schedule, int day, int hour, int dept) {
		ArrayList<Teacher> prefTeachers = new ArrayList<>();
		for (Teacher teacher : teachers) {
			if (teacher.getLessonsThatCanTeach().contains(lesson.getID())) {
				prefTeachers.add(teacher);
			}
		}
		
		if (schedule!=null) {
			ArrayList<Teacher> prefNonBusyTeachers = new ArrayList<>(prefTeachers);
			HourlyDuties[][] hourlyDuties = schedule.getDay(day).getHourlySchedule();
			for(int deptNumber=0; deptNumber<Schedule.departments.size(); deptNumber++) {
				HourlyDuties hd = hourlyDuties[hour][deptNumber];
				if (hd != null & hd.hasAssignment()) {
					prefNonBusyTeachers.remove(hd.getTeacher());
				}
			}
			if (!prefNonBusyTeachers.isEmpty())
				prefTeachers = prefNonBusyTeachers;
		}
		
		Random r = new Random();
		return prefTeachers.get(r.nextInt(prefTeachers.size()));
	}

	public static Lesson getRandomLesson(String department, HashMap<String, Integer> lessonHoursLeftToDo) {
		ArrayList<Lesson> prefLessons = new ArrayList<>();
		for (Lesson lesson : lessons) {
			if (department.toLowerCase().startsWith(lesson.getDepartmentTeached().toLowerCase()) && lessonHoursLeftToDo.keySet().contains(department+lesson.getID()))
				prefLessons.add(lesson);
		}
		
		if (prefLessons.isEmpty())
			return null;
		
		Random r = new Random();
		return prefLessons.get(r.nextInt(prefLessons.size()));
	}

	public void outputToFile(String outputFilename) {
		Output out = new Output(outputFilename);
		
		String extraInfo = "Extra information regarding score of the schedule.\r\n";
		extraInfo += "getScoreBasedOnEmptyTimeslotsBetweenLessons: \r\n" + ScheduleScore.getScoreBasedOnEmptyTimeslotsBetweenLessons(this);
		extraInfo += "\r\n";
		extraInfo += "getScoreBasedOnSimilarlyDistributedAmountOfHoursPerDay: \r\n" + ScheduleScore.getScoreBasedOnSimilarlyDistributedAmountOfHoursPerDay(this);
		extraInfo += "\r\n";
		extraInfo += "getScoreBasedOnSimilarDistributedAmountOfHoursPerLesson: \r\n" + ScheduleScore.getScoreBasedOnSimilarDistributedAmountOfHoursPerLesson(this);
		extraInfo += "\r\n";
		extraInfo += "getScoreBasedOnSimilarlyDistributedAmountOfWorkingHoursPerTeacher: \r\n" + ScheduleScore.getScoreBasedOnSimilarlyDistributedAmountOfWorkingHoursPerTeacher(this);
		extraInfo += "\r\n";
		extraInfo += "getScoreBasedOnAllLessonsPerDepartmentTeached: \r\n" + ScheduleScore.getScoreBasedOnAllLessonsPerDepartmentTeached(this);
		extraInfo += "\r\n";
		extraInfo += "getScoreBasedOnTeacherShouldBeInOnePlace: \r\n" + ScheduleScore.getScoreBasedOnTeacherShouldBeInOnePlace(this);
		extraInfo += "\r\n";
		extraInfo += "getScorefromBasedOnMaxConsecutiveTeacherHours: \r\n" + ScheduleScore.getScorefromBasedOnMaxConsecutiveTeacherHours(this);
		extraInfo += "\r\n";
		extraInfo += "getScoreBasedOnMaxWeeklyHoursOfTeacher: \r\n" + ScheduleScore.getScoreBasedOnMaxWeeklyHoursOfTeacher(this);
		extraInfo += "\r\n";
		extraInfo += "Total score: \r\n" + ScheduleScore.getScore(this, "total");
		extraInfo += "\r\n";
		
		out.writeToFile(extraInfo + this.toString());
		
		//int scoreSchedule = ScheduleScore.getScore(this, "total");
		//out.writeToFile("Score: " + scoreSchedule + "\r\n" + this.toString());
	}

	public ArrayList<Teacher> getTeachers(Lesson course) {
		ArrayList<Teacher> prefTeachers = new ArrayList<>();
		for(Teacher teacher : teachers) {
			if (teacher.canTeach(course))
				prefTeachers.add(teacher);
		}
		return prefTeachers;
	}

}