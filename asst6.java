/**
 * C202 Lab5
 * Lab5.java
 * Purpose: This program explores the algorithms for computing the tour cost of 
 * a traveling salesman problem.
 *  
 * @author Dewey Kincheloe
 * @version 1.0 9/17/2016
 */

import java.util.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.io.File;
import java.io.FileNotFoundException;

public class asst6 <E extends Comparable<E>>
{
    //Attributes
    private int CITI; // Can't create until the number of cities is known
    private int[][] adjacency;
    private int bestcost = Integer.MAX_VALUE;
    private ArrayList<Integer> bestpath; 
    private Stack<Integer> pathStack;
    private int city, startCity, closestCity, currentCity, min = 0;
    //public int [] visitedCities;
    private Boolean minFlag = true;
    
    //Constructors
    public asst6(int number)
    {
	CITI = number;
	adjacency = new int[CITI][CITI];
	bestpath = new ArrayList<>();
        pathStack = new Stack<>();
        //int [] visitedCities = new int[number];
        //System.out.println("visitedCities.length xxxxxxxxxxxxxxxxxx " + visitedCities.length);
        //visitedCities[0] = 0;
        //for(int i = 0; i < number; i++)
        //{
        //    visitedCities[i] = -1;
        //    //System.out.println("visitedCities " + i + " = " + visitedCities[i]);
	//}
    }
    // The populateMatrix constructor populates the fname file.
    public void populateMatrix(String fname)
    {
	File f = new File(fname);
	try
        {
            Scanner input = new Scanner(f);
            int i,j;
            for(i = 0; i < CITI && input.hasNext(); i++)
            {
		for(j = i; j < CITI && input.hasNext(); j++)
                {
                    if(i == j)
                    {
			adjacency[i][j] = 0;
                    }
                    else
                    {
			int value = input.nextInt();
			adjacency[i][j] = value;
			adjacency[j][i] = value;
                    }
		}
            }
            input.close();
        } 
	catch(IOException e)
        {
            System.out.println("File reading failed!");
	}
        
        //for(int i = 0; i < CITI; i++)
        //    {
	//	for(int j = 0; j < CITI; j++)
        //        {System.out.print(adjacency[i][j] + " ");}
        //    System.out.println("xxx");
        //    }
    }
    
    //Methods
    //The cost method receives an integer array and calculates the path cost.
    public int cost(ArrayList<Integer> path)
    {
        int cost = 0;
	for(int i = 0; i < path.size()-1; i++)
        {
            cost += adjacency[path.get(i)][path.get(i+1)];
        }
	if(path.size() == CITI)
        {
            cost += adjacency[path.get(path.size()-1)][0];
	} 
	return cost; 
    }
    // The tspdfs method receives an integer array and an arraylist (cities visited 
    // and to be visited) then recursively searches for a better path with a cheaper cost. 
    public void tspdfs(ArrayList<Integer> partialTour, ArrayList<Integer> remainingCities)
    {
	if(remainingCities.size() == 0)
        {
            int tourCost = cost(partialTour); 
            if(tourCost < bestcost)
            {
                bestcost = tourCost;
                bestpath = new ArrayList<>(partialTour);
            } 
        }
        else
        {
            for(int i = 0; i < remainingCities.size(); i++)
            {
                ArrayList<Integer> temp = new ArrayList<>(partialTour);
                temp.add(remainingCities.get(i));
                int tourCost = cost(temp);
                if(tourCost < bestcost)
                {
                    ArrayList<Integer> temp2 = new ArrayList<>(remainingCities);
                    temp2.remove(i);
                    tspdfs(temp, temp2);
                } 
            }
        }
    }
    
    public boolean notVisited(int [] visitedCities, int checkIfVisited)
    {
        //System.out.println("line 132 visitedCities.length xxxxxxxxxxxxxxxxxxxx ");
        for(int i = 0; i < visitedCities.length; i++)
        {
            //System.out.println("line 138 visitedCities[i] xxxxxxxxxxxxxx " + visitedCities[i]);
            if(visitedCities[i] == checkIfVisited)
            {
                return false;
            }
        }
        return true;
    }
    
    public void addVisited(int[] visitedCities, int closestCity)
    {
        int i = 0;
        while(visitedCities[i] != -1 && i < visitedCities.length)
        {
            i++;
        }
        visitedCities[i] = closestCity;
    }
    
    public void pathSearch(int[] visitedCities, int number)
    {
        //visitedCities[0] = 0;
        //System.out.println("visitedCities[0] = " + visitedCities[0]);
        pathStack.push(startCity);
        closestCity = startCity;
        minFlag = false;
                
        System.out.println("Starting City = 0");
        while(!pathStack.isEmpty())
        {
            currentCity = pathStack.peek();
            min = Integer.MAX_VALUE;
            for(int i = 1; i < number; i++)
            {
                if(adjacency[currentCity][i] != 0 && notVisited(visitedCities, i))
                {
                    if(adjacency[currentCity][i] < min)
                    {
                        min = adjacency[currentCity][i];
                        closestCity = i;
                        minFlag = true;
                    } //end if
                } //end if
            } // end for
            
            if(minFlag)
            {
                addVisited(visitedCities, closestCity);
                pathStack.push(closestCity);
                //System.out.println("The closest city is " + closestCity);
                minFlag = false;
                // Continue??
            } //end if
            
        pathStack.pop();
        } //endwhile
    }
    
    //The main method controls program execution.
    public static void main(String[] args)
    {
	
        Scanner in = new Scanner(System.in);
        System.out.println("Enter number of cities ");
        int number = in.nextInt();
        System.out.println("Enter the file name ");
        String input = in.next();
	asst6 tsp = new asst6(number);
        
        tsp.populateMatrix(input);
        
        int[] visitedCities = new int[number];
        visitedCities[0] = 0;
        for(int i = 0; i < number; i++)
        {
            visitedCities[i] = -1;
	}
        
        tsp.pathSearch(visitedCities, number);
        
        
        
        
        
	ArrayList<Integer> partialT = new ArrayList<>();
	partialT.add(0);
	ArrayList<Integer> remainingT = new ArrayList<>();
	for(int i = 1; i < number; i++)
        {
            remainingT.add(i);
	}
        long sum = 0;
        long start = System.nanoTime();
	tsp.tspdfs(partialT, remainingT);
        long stop = System.nanoTime();
	sum += (stop-start);
        System.out.println(sum);
        tsp.output();
    }
    
        // The output method displays the best path and cost.
        public void output()
        {
	System.out.println();
        System.out.println("Best path and cost");
	for(int i = 0; i < bestpath.size(); i++)
        {
            System.out.println(bestpath.get(i) + " ");
	}
        System.out.println("cost =  " + bestcost);
    }
}