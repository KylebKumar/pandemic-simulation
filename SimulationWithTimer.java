/**
 * SimulationWithTimer.java
 *
 * Creates a simulation of a pandemic disease based on a given infect rate, percent of people staying in place,
 * the size of the population and the transfer rate of the virus.
 * When the pandemic has ended, contains information about the time it took to end the virus and the number of people
 * infected at once. These two data points could be used to show the effectiveness of staying at home (although this
 * simulation is rather simplistic)
 *
 * Make sure that the simulation's window is active when the simulation is run or the screen may not be drawn properly.
 *
 * @author Kyle Kumar
 * @since 4-7-2020
 */

import java.awt.*;
import java.util.ArrayList;

public class SimulationWithTimer
{
    private ArrayList<Person> people;
    private int delay = 20;
    /** The maximum number of people infected with the virus at any one time */
    private int maxInfected = 0;
    /**
     * Sets up the simulation
     * @param numberOfPeople the number of people in the population
     * @param numSick the number of sick people initially
     * @param infectRate the rate at which the virus transfers upon contact
     * @param percentStayingInPlace the percent of people staying in place
     * @param tickRate a varriable that controls the speed of the animations
     */
    public SimulationWithTimer(int numberOfPeople, int numSick, double infectRate, double percentStayingInPlace, int tickRate) {
        people = new ArrayList<Person>();
        delay = tickRate;
        for(int i = 0; i < numberOfPeople; i++)
        {
            if(numSick>0)
            {
                people.add(new Person(true, infectRate, Math.random()<percentStayingInPlace));
                numSick--;
            }
            people.add(new Person(false, infectRate, Math.random()<percentStayingInPlace));
        }
    }

    /**
     * Runs the animation and checks for arguments. Will display a help message if one of the arguments can't be read.
     */
    public static void main(String [] args)
    {
        int numberOfPeople = 100, numSick = 3,tickRate = 20;
        double infectRate = .25, percentStayingInPlace= .2;
        // args are: java Simulation numberOfPeople numSick infectRate percentStayingInPlace tickRate
        try
        {
            if(args.length>0) numberOfPeople = Integer.parseInt(args[0]);
            if(args.length>1) numSick= Integer.parseInt(args[1]);
            if(args.length>2) infectRate = Double.parseDouble(args[2]);
            if(args.length>3) percentStayingInPlace = Double.parseDouble(args[3]);
            if(args.length>4) tickRate = Integer.parseInt(args[4]);
        }
        catch(NumberFormatException error)
        {
            System.err.println("\n\nERROR: One of the command-line arguments was invalid.\n");
            System.err.println("The first " + args.length +"/5 argument(s) should be\n(1) the number of people in the simulation(integer)");
            if(args.length>1)System.err.println("(2) the number of infected initially (integer)");
            if(args.length>2) System.err.println("(3) the percent infect rate of the virus(double)");
            if(args.length>3) System.err.println("(4) the percent of people staying in place(double)");
            if(args.length>4) System.err.println("(5) and the delay inbetween frames in ms (integer)");
            System.err.println("\n\n");
            System.exit(1);
        }
        SimulationWithTimer run = new SimulationWithTimer(numberOfPeople,numSick,infectRate,percentStayingInPlace,tickRate);
        run.setUp();
        run.runLoop();
    }

    /**
     * Sets up the emply panel
     */
    public void setUp ( )
    {
        StdDraw.setCanvasSize(800,800);
        StdDraw.setXscale(-10.0, 10.0);
        StdDraw.setYscale(-10.0, 10.0);
        StdDraw.enableDoubleBuffering();
    }

    /**
     * Runs the simulation until the pandemic is over.
     *
     */
    public void runLoop ( )
    {
        long start = System.nanoTime();
        boolean keepGoing = true;
        while(keepGoing)
        {
            StdDraw.clear(StdDraw.LIGHT_GRAY);

            for(Person person : people)
            {
                person.changePosition();
                if(person.isSick())
                    person.incrementDaysSick();

                for(Person otherPerson : people)
                {
                    if(!person.equals(otherPerson)&&person.isTouching(otherPerson))
                    {
                        person.collide(otherPerson);
                    }
                }
                person.draw();
            }

            StdDraw.show();
            StdDraw.pause(delay);
            keepGoing=!isHealthy();
        }
        long stop = System.nanoTime();
        double timeTaken = (stop-start)/1000000000.0;
        StdDraw.setPenColor(Color.BLUE);
        StdDraw.setFont(new Font("serif", Font.BOLD, 40));
        StdDraw.text(0,0,"Time Taken: " + Math.round(timeTaken) +" seconds");
        StdDraw.text(0,1.5," Max Infected At One Time: " + maxInfected + " people");
        StdDraw.show();
        System.out.println("\n\n\n The pandemic ended in " + Math.round(timeTaken) + " seconds");
        System.out.println("The maximum cases at any time was " + maxInfected + " cases\n\n\n");
    }

    /**
     * Checks to see whether or not every person is healthy and changes the count of the maximum number of people
     * infected if the current count is larger than the stored one.
     *
     * @return true if no person has the virus
     */
    public boolean isHealthy()
    {
        int currentSickCount = 0;
        for(Person person: people)
        {
            if(person.isSick())
                currentSickCount++;
        }
        if(currentSickCount>maxInfected)
            maxInfected = currentSickCount;
        return(currentSickCount == 0);
    }
}
