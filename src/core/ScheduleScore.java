package core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.IntStream;

import datastructure.Day;
import datastructure.HourlyDuties;
import datastructure.Lesson;
import datastructure.Schedule;
import datastructure.Teacher;
import debug.Debug;

public class ScheduleScore {
	
	private static int AMOUNT_OF_DAYS_IN_WEEK;
	
	static {
		AMOUNT_OF_DAYS_IN_WEEK = 5;
	}

	public static int getScore(Schedule schedule, String params) {
		int score = 0;
		
		if (params.equals("total")) {
			score += getScoreBasedOnEmptyTimeslotsBetweenLessons(schedule)*30;
			score += getScorefromBasedOnMaxConsecutiveTeacherHours(schedule)*5;
			score += getScoreBasedOnSimilarlyDistributedAmountOfHoursPerDay(schedule)*2;
			score += getScoreBasedOnSimilarDistributedAmountOfHoursPerLesson(schedule);
			score += getScoreBasedOnSimilarlyDistributedAmountOfWorkingHoursPerTeacher(schedule);
			score += getScoreBasedOnAllLessonsPerDepartmentTeached(schedule)*7000;
			score += getScoreBasedOnTeacherShouldBeInOnePlace(schedule)*7000;
			score += getScoreBasedOnMaxWeeklyHoursOfTeacher(schedule)*1000;
		} 
		else if (params.equals("hardRestrictions")) {
			score += getScoreBasedOnAllLessonsPerDepartmentTeached(schedule)*7000;
			score += getScoreBasedOnTeacherShouldBeInOnePlace(schedule)*7000;
			score += getScoreBasedOnMaxWeeklyHoursOfTeacher(schedule)*1000;
		}
		
		return score; // TODO adjust properly if needed
	}
	
	public static int getScoreBasedOnMaxWeeklyHoursOfTeacher(Schedule schedule) {
		int score = 0;
		
		for(Teacher teacher : Schedule.teachers) {
			int totalAmountOfWorkHours = schedule.getTotalAmountOfWorkingHours(teacher);
			if (totalAmountOfWorkHours>teacher.getMaximumHoursWeek()) 
				score += 1;
		}
		
		return score;
	}
	
	public static int getScoreBasedOnEmptyTimeslotsBetweenLessons(Schedule schedule) {
		int score = 0;
		
		for(int i=0; i<schedule.getAmountOfDays(); i++) {
			Day day = schedule.getDay(i);
			ArrayList<int[]> listOfDepartmentsEmptyTimeslots = day.getConsecutiveEmptyTimeslotsBetweenLessonsPerDepartment();
			// ([1,1,2],[2],[3,1])
			for (int deptNumber = 0; deptNumber < listOfDepartmentsEmptyTimeslots.size(); deptNumber++) {
				int[] emptyTimeslots = listOfDepartmentsEmptyTimeslots.get(deptNumber);
				score += getScoreEmptyTimeslotsBetweenLessons(emptyTimeslots);
			}
		}
		
		return score;
	}
	
	private static int getScoreEmptyTimeslotsBetweenLessons(int[] emptyTimeslots) {
		int score = 0;
		for(int hours : emptyTimeslots) {
			score += hours; // TODO consider multiplying it by a number (thus bigger gap between lessons has higher cost)
		}
		return score;
	}
	
	public static int getScoreBasedOnSimilarlyDistributedAmountOfHoursPerDay(Schedule schedule) {
		int score = 0;
		
		for(int i=0; i<schedule.getAmountOfDepartments(); i++) {
			ArrayList<int[]> listOfDepartmentsAmountOfHoursPerDaySchedule = schedule.getAmountOfLessonsHoursPerDepartment();
			for (int deptNumber = 0; deptNumber < listOfDepartmentsAmountOfHoursPerDaySchedule.size(); deptNumber++) {
				int[] amountOfLessonHoursPerDay = listOfDepartmentsAmountOfHoursPerDaySchedule.get(deptNumber);
				int totalAmountOfHoursWeekly = IntStream.of(amountOfLessonHoursPerDay).sum();
				double avgHoursPerDay = totalAmountOfHoursWeekly/amountOfLessonHoursPerDay.length;
				// Actual score adding
				for(int dailyHours : amountOfLessonHoursPerDay) {
					double distance = Math.abs(avgHoursPerDay-dailyHours);
					if (distance > 1)
						score += (int) Math.floor(distance);
				}
			}
			
		}
		
		return score;
	}
	
	public static int getScoreBasedOnSimilarDistributedAmountOfHoursPerLesson(Schedule schedule) {
		int score = 0;
		
		for (int deptNumber = 0; deptNumber < schedule.getAmountOfDepartments(); deptNumber++) {
			ArrayList<Lesson> lessonsInDepartment = schedule.getLessonsPerDepartment(deptNumber);
			for (Lesson lesson : lessonsInDepartment) {
				int[] lessonHoursPerDay = schedule.getAmountOfLessonHoursPerDayInDepartment(lesson, deptNumber);
				int totalAmountOfHoursWeekly = lesson.getDemandedHours();
				double avgHoursPerDay = totalAmountOfHoursWeekly/AMOUNT_OF_DAYS_IN_WEEK;
				// Actual score adding
				for(int dailyHours : lessonHoursPerDay) {
					double distance = Math.abs(avgHoursPerDay-dailyHours);
					if (distance > 1)
						score += (int) Math.floor(distance);
				}
			}
		}
		
		return score;
	}
	
	public static int getScoreBasedOnSimilarlyDistributedAmountOfWorkingHoursPerTeacher(Schedule schedule) {
		int score = 0;
		
		int[] amountOfWorkingHoursPerTeacher = new int[schedule.getAmountOfTeachers()];
		int totalAmountOfHoursOfAllTeachers = 0;
		int i = 0;
		for(Teacher teacher : schedule.getTeachers()) {
			amountOfWorkingHoursPerTeacher[i] = schedule.getTotalAmountOfWorkingHours(teacher);
			totalAmountOfHoursOfAllTeachers += amountOfWorkingHoursPerTeacher[i];
			i++;
		}
		double avgWeeklyHoursPerTeacher = totalAmountOfHoursOfAllTeachers/schedule.getAmountOfTeachers();
		// Actual score adding
		for(int teacherHours : amountOfWorkingHoursPerTeacher) {
			double distance = Math.abs(avgWeeklyHoursPerTeacher-teacherHours);
			if (distance > 1)
				score += (int) Math.floor(distance);
		}
		
		return score;
	}
	
	public static int getScoreBasedOnTeacherShouldBeInOnePlace(Schedule schedule) {
		int score = 0;
		for(int day=0; day<schedule.getAmountOfDays(); day++) {
			for(int hour=0; hour<schedule.getDay(day).getHours(); hour++) {
				int[] teachersAmountOfPlacesThatTheyWorkOnThisHour = new int[schedule.getAmountOfTeachers()];
				HourlyDuties[] dutiesThisHour = schedule.getDay(day).getHourlySchedule()[hour];
				for(HourlyDuties duty : dutiesThisHour) {
					for(int teacherNumber=0; teacherNumber<schedule.getAmountOfTeachers(); teacherNumber++) {
						Teacher selectedTeacher = schedule.getTeachers().get(teacherNumber);
						if (duty != null && duty.getCourse() != null && duty.getTeacher() != null && duty.getTeacher().equals(selectedTeacher)) {
							teachersAmountOfPlacesThatTheyWorkOnThisHour[teacherNumber]++;
						}
					}
				}
				for(int amountOfPlacesAtTheSameTime : teachersAmountOfPlacesThatTheyWorkOnThisHour) {
					if (amountOfPlacesAtTheSameTime>1) {
						score += amountOfPlacesAtTheSameTime-1; // TODO adjust this
					}
				}
			}
		}
		return score;
	}
	
	public static int getScoreBasedOnAllLessonsPerDepartmentTeached(Schedule schedule) {
		int score = 0;
		for(int deptNumber=0; deptNumber<schedule.getAmountOfDepartments(); deptNumber++) {
			for(Lesson lesson : schedule.getLessons()) {
				int amountOfHoursTeachedInDept = 0;
				for(Day day : schedule.getSchedule()) {
					amountOfHoursTeachedInDept += day.getAmountOfLessonHours(lesson, deptNumber);
				}
				int difference = lesson.getDemandedHours() - amountOfHoursTeachedInDept;
				if (difference > 0) {
					score += difference; // TODO adjust this
				}
			}
		}
		return score;		
	}
	
	public static int getScorefromBasedOnMaxConsecutiveTeacherHours( Schedule schedule ) {
		int score = 0;
		int maxConsecutiveHours = 2;
		
		for(Teacher teacher : Schedule.teachers) {
			for(Day day : schedule.getSchedule()) {
				int[] hoursOfDayWorking = day.getDailyScheduleOfTeacher(teacher);
				int consecutiveHoursWorking = 0;
				for(int i=0; i<hoursOfDayWorking.length; i++) {
					if (hoursOfDayWorking[i]>0) {
						consecutiveHoursWorking++;
					}
					//If i-th hour is empty
					else if (hoursOfDayWorking[i]==0 && consecutiveHoursWorking>maxConsecutiveHours){ 
						if(!(i+1>=hoursOfDayWorking.length)) { // If there is a next hour
							if(hoursOfDayWorking[i+1]>0)
								score += 1;
						}
						consecutiveHoursWorking = 0;
					}
				}
			}
		}
		
        return score;
   }

}
