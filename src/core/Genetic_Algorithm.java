package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import datastructure.Schedule;
import debug.Debug;

public class Genetic_Algorithm {
	 //ArrayList that contains the current population of chromosomes
	private ArrayList<Chromosome> population;
    /*
     * ArrayList that contains indexes of the chromosomes in the population ArrayList
     * Each chromosome index exists in the ArrayList as many times as its fitness score
     * By creating this ArrayList so, and choosing a random index from it,
     * the greater the fitness score of a chromosome the greater chance it will be chosen.
    */
	private ArrayList<Integer> fitnessBounds;
	
	public Genetic_Algorithm()
	{
		this.population = null;
		this.fitnessBounds = null;
	}

    /* 
     * populationSize: The size of the population in every step
     * mutationPropability: The propability a mutation might occur in a chromosome
     * minimumFitness: The minimum fitness value of the solution we wish to find
     * maximumSteps: The maximum number of steps we will search for a solution
     */
	public Chromosome geneticAlgorithm(int populationSize, double mutationProbability, int minimumFitness, int maximumSteps)
	{
        //We initialize the population
		initializePopulation(populationSize);
		Random r = new Random();
		for(int step=0; step < maximumSteps; step++)
		{
			if (step%25==0)
				Debug.println("Step: " + step + " and counting...");
            //Initialize the new generated population
			ArrayList<Chromosome> newPopulation = new ArrayList<Chromosome>();
			for(int i=0; i < populationSize; i++)
			{
                //We choose two chromosomes from the population
                //Due to how fitnessBounds ArrayList is generated, the propability of
                //selecting a specific chromosome depends on its fitness score
				int parentIndex = this.fitnessBounds.get(r.nextInt(this.fitnessBounds.size()));
				Chromosome parent = this.population.get(parentIndex);

				//Debug.println("Before reproducing " +i+"° child created!");
                //We generate the "child" of the two chromosomes
				Chromosome child = this.reproduce(parent);
				//Debug.println("Reproduced " +i+"° child created!");
                //We might then mutate the child
				if(r.nextDouble() < mutationProbability)
				{
					child.mutate();
				}
				
                //...and finally add it to the new population
				
				newPopulation.add(child);
				/*//TODO maybe we should check child/parent score?    add / after the asterisk to uncomment *
				if (ScheduleScore.getScore(child.getSchedule(), "total") < ScheduleScore.getScore(parent.getSchedule(), "total")) {
					newPopulation.add(child);
					//Debug.println("Child added");
				}
				else {
					newPopulation.add(parent);
					//Debug.println("Parent added instead of child");
				}
				/*
				*/
				
				//TODO added this
				newPopulation.add(parent);
				
				//Debug.println(i+"th child created!");
			}
			this.population = new ArrayList<Chromosome>(newPopulation);

            //We sort the population so the one with the greater fitness is first
			Collections.sort(this.population); // TODO I changed this
			
			//TODO added this
			for(int i=this.population.size()-1; i>=populationSize; i--)
				this.population.remove(i);
			
            //If the chromosome with the best fitness is acceptable we return it
			if(this.population.get(0).getFitness() <= minimumFitness) // TODO I changed this
			{
                System.out.println("Finished after " + step + " steps...");
				return this.population.get(0);
			}
            //We update the fitnessBounds arrayList
			this.updateFitnessBounds();
		}

        System.out.println("Finished after " + maximumSteps + " steps...");
		return this.population.get(0);
	}

    //We initialize the population by creating random chromosomes
	public void initializePopulation(int populationSize)
	{
		this.population = new ArrayList<Chromosome>();
		for(int i=0; i<populationSize; i++)
		{
			this.population.add(new Chromosome());
		}
		this.updateFitnessBounds();
	}

    //Updates the arraylist that contains indexes of the chromosomes in the population ArrayList
	public void updateFitnessBounds()
	{
		this.fitnessBounds = new ArrayList<Integer>();
		for (int i=0; i<this.population.size(); i++)
		{
			for(int j=0; j<this.population.get(i).getFitness(); j++)
			{
                //Each chromosome index exists in the ArrayList as many times as its fitness score
                //By creating this ArrayList so, and choosing a random index from it,
                //the greater the fitness score of a chromosome the greater chance it will be chosen.
				fitnessBounds.add(i);
			}
		}
	}

    //"Reproduces" two chromosomes and generated their "child"
	public Chromosome reproduce(Chromosome x)
	{
		Schedule newSchedule = new Schedule(x.getSchedule());
		newSchedule.swapDays();
		return new Chromosome(newSchedule);
	}
	
}