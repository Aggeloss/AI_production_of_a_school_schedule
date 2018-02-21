package core;

import java.util.ArrayList;
import java.util.Random;

import datastructure.Day;
import datastructure.HourlyDuties;
import datastructure.Lesson;
import datastructure.Schedule;
import datastructure.Teacher;
import debug.Debug;

public class Chromosome implements Comparable<Chromosome> {
	
	private Schedule schedule;
	
	private int fitness;

	public Chromosome() {
		this.schedule = Schedule.createRandomSchedule();
		this.calculateFitness();
	}

    //Constructs a copy of a chromosome
	public Chromosome(Schedule schedule) {
		this.schedule = new Schedule(schedule);
		this.calculateFitness();
	}
	
	public Schedule getSchedule() {
		return this.schedule;
	}
	
	public int getFitness() {
		return this.fitness;
	}
	
	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

	//Our threats will keep up with our constraints
	public void calculateFitness() {

		int score = ScheduleScore.getScore(this.schedule, "total"); // TODO
		this.fitness = score; // TODO DEFINITELY CHANGE THIS 
	}

    //Mutate by randomly changing the position of a queen
	public void mutate() {
		//TODO mutate method
		Random r1 = new Random();

		int day1, hour1, day2, hour2;
		day1 = day2 = hour1 = hour2 = -1;

		day1 = r1.nextInt(Schedule.amountOfDays);
		day2 = r1.nextInt(Schedule.amountOfDays);
		
		hour1 = r1.nextInt(Schedule.amountOfHoursPerDay);
		do {
			hour2 = r1.nextInt(Schedule.amountOfHoursPerDay);
		} while (hour1==hour2);
		
		Day[] dailySchedule = this.schedule.getSchedule();
		
		int dept = r1.nextInt(Schedule.departments.size());
		HourlyDuties hd = dailySchedule[day1].getHourlySchedule()[hour1][dept];
		if (hd!=null && hd.hasAssignment()) {
			Lesson lesson = hd.getCourse();
			Teacher currentTeacher = hd.getTeacher();
			Teacher newTeacher = currentTeacher;
			int counter = 0;
			do {
				newTeacher = Schedule.getRandomTeacher(lesson,null,0,0,0);
				counter++;
			} while (counter<10 && newTeacher == currentTeacher);
			int currentScoreOnePlace = ScheduleScore.getScoreBasedOnTeacherShouldBeInOnePlace(schedule);
			int currentScoreMaxHours = ScheduleScore.getScoreBasedOnMaxWeeklyHoursOfTeacher(schedule);
			hd.setTeacher(newTeacher);
			int changedScoreOnePlace = ScheduleScore.getScoreBasedOnTeacherShouldBeInOnePlace(schedule);
			int changedScoreMaxHours = ScheduleScore.getScoreBasedOnMaxWeeklyHoursOfTeacher(schedule);
			if (changedScoreOnePlace > currentScoreOnePlace && changedScoreMaxHours >= currentScoreMaxHours) {
				hd.setTeacher(currentTeacher);
				//Debug.println("Changed teacher! ;)" );
			}
		}
		
		mutateSpecificallyTeachersInMultiplePlacesAtSameTime();
		
		Schedule.swap(dailySchedule[day1].getHourlySchedule(),dailySchedule[day2].getHourlySchedule(),hour1,hour2);
				
		this.calculateFitness();
		
	}
	
	private void mutateSpecificallyTeachersInMultiplePlacesAtSameTime() {
		int score = ScheduleScore.getScoreBasedOnTeacherShouldBeInOnePlace(schedule);
		if (score==0)
			return;
		//Debug.println("Inside mutate Specifically teahcers");
		for(int day=0; day<schedule.getAmountOfDays(); day++) {
			for(int hour=0; hour<schedule.getDay(day).getHours(); hour++) {
				int[] teachersAmountOfPlacesThatTheyWorkOnThisHour = new int[schedule.getAmountOfTeachers()];
				HourlyDuties[] dutiesThisHour = schedule.getDay(day).getHourlySchedule()[hour];
				for(int teacherNumber=0; teacherNumber<schedule.getAmountOfTeachers(); teacherNumber++) {
					for(HourlyDuties duty : dutiesThisHour) {
						Teacher selectedTeacher = schedule.getTeachers().get(teacherNumber);
						if (duty != null && duty.getCourse() != null && duty.getTeacher() != null && duty.getTeacher().equals(selectedTeacher)) {
							teachersAmountOfPlacesThatTheyWorkOnThisHour[teacherNumber]++;
							if(teachersAmountOfPlacesThatTheyWorkOnThisHour[teacherNumber]>1) {
								ArrayList<Teacher> teachersTeachingClass = schedule.getTeachers(duty.getCourse());
								ArrayList<Teacher> teachersNotExceedingWeeklyHours = new ArrayList<>();
								for (Teacher checkTeacher : teachersTeachingClass) {
									if (checkTeacher.equals(selectedTeacher))
										continue;
									if (checkTeacher.getMaximumHoursWeek() < schedule.getTotalAmountOfWorkingHours(checkTeacher)) {
										teachersNotExceedingWeeklyHours.add(checkTeacher);
									}
								}
								if (teachersNotExceedingWeeklyHours.size()>0) {
									Random r = new Random();
									Teacher pickRandomTeacher = null;
									do{
										pickRandomTeacher = teachersNotExceedingWeeklyHours.get(r.nextInt(teachersNotExceedingWeeklyHours.size()));
										//Debug.println("Picked " + pickRandomTeacher);
										//Debug.println("Instead of " + selectedTeacher);
									} while (pickRandomTeacher.equals(selectedTeacher) && teachersNotExceedingWeeklyHours.size()>1);
									
									duty.setTeacher(pickRandomTeacher);
									int scoreAfterChange = ScheduleScore.getScoreBasedOnTeacherShouldBeInOnePlace(schedule);
									if ( scoreAfterChange > score ) {
										duty.setTeacher(selectedTeacher);
										//Debug.println("Reverting teacher change");
									}
								}
								else {
									Random r = new Random();
									Teacher pickRandomTeacher = null;
									do{
										pickRandomTeacher = teachersTeachingClass.get(r.nextInt(teachersTeachingClass.size()));
										//Debug.println("Picked " + pickRandomTeacher);
										//Debug.println("Instead of " + selectedTeacher);
									} while (pickRandomTeacher.equals(selectedTeacher) && teachersTeachingClass.size()>1);
									
									duty.setTeacher(pickRandomTeacher);
									int scoreAfterChange = ScheduleScore.getScoreBasedOnTeacherShouldBeInOnePlace(schedule);
									if ( scoreAfterChange > score ) {
										duty.setTeacher(selectedTeacher);
										//Debug.println("Reverting teacher change");
									}
								}
							}
						}
					}
				}
			}
		}
		
	}

	//Our program will be printed in Excel. (hopefully)
	public void print() {
		this.schedule.print();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Chromosome) {
			Chromosome objc = (Chromosome) obj;
			return objc.schedule.equals(schedule);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		// TODO check hashCode method
		int hashcode = super.hashCode();
		
		for(int i=0; i<this.schedule.getAmountOfDays(); i++) {
				
			hashcode += this.schedule.getSchedule().hashCode();
				
		}
		
		return hashcode; 
	}
	
	@Override
    //The compareTo function has been overriden so sorting can be done according to fitness scores
	public int compareTo(Chromosome x)
	{
		return this.fitness - x.fitness;
	}
}