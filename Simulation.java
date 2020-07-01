/**
 * Simulation.java
 *
 * Creates a simulation of a pandemic disease based on a given infection rate, the percent of people staying in place,
 * the size of the population, and the transfer rate of the virus.
 *
 * Make sure that the simulation's window is active when the simulation is run or the screen may not be drawn properly.
 *
 * @author Kyle Kumar
 * @since 4-7-2020
 */


import java.util.ArrayList;

public class Simulation
{
    private ArrayList<Person> people;
    private int delay = 20;

    /**
     * Sets up the simulation
     * @param numberOfPeople the number of people in the population
     * @param numSick the number of sick people initially
     * @param infectRate the rate at which the virus transfers upon contact
     * @param percentStayingInPlace the percent of people staying in place
     * @param tickRate a varriable that controls the speed of the animations
     */
    public Simulation(int numberOfPeople, int numSick, double infectRate, double percentStayingInPlace, int tickRate) {
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
        // command like arguments are: java Simulation numberOfPeople numSick infectRate percentStayingInPlace tickRate
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
        Simulation run = new Simulation(numberOfPeople,numSick,infectRate,percentStayingInPlace,tickRate);
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
     * Runs the simulation(forever)
     */
    public void runLoop ( )
    {
        while(true)
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
        }

    }
}
