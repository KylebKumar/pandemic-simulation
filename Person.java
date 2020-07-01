/**
 * Person.java
 *
 * Represents a single person in a pandemic simulation.
 *
 * @author Kyle Kumar
 * @since 4-7-2020
 */



public class Person
{
    private double x, y, vx, vy, radius, infectRate;
    private int viralState;     //  0 = uninfected, 1 = infected, 2 = recovered.
    private int daysSick;
    private boolean shelterInPlace;

    /**
     * Sets up the person object with a random position and velocity
     *
     * @param sick true if the person starts off sick
     * @param infectRate the decimal rate of transfer of the virus
     * @param stayInPlace the decimal rate of people staying in place
     */
    public Person ( boolean sick, double infectRate, boolean stayInPlace)
    {
        radius = 0.2;
        x = Math.random() * (20.0 - 2 * radius) - (10.0 - radius);
        y = Math.random() * (20.0 - 2 * radius) - (10.0 - radius);
        vx = Math.random() * radius - radius / 2;
        vy = Math.random() * radius - radius / 2;
        viralState = 0;
        if(sick)
            viralState = 1;
        daysSick = 0;
        shelterInPlace = stayInPlace;
        if(shelterInPlace)
        {
            vx=0;
            vy=0;
        }
        this.infectRate = infectRate;
    }

    /**
     * Moves the person object based on its velocity and makes sure the person does not leave the screen
     */
    public void changePosition ( )
    {
        if (Math.abs(x + vx) > 10.0 - radius)
        {
            vx = -vx;
        }
        if (Math.abs(y + vy) > 10.0 - radius)
        {
            vy = -vy;
        }

        x = x + vx;
        y = y + vy;
    }

    /**
     * Draws the person object with the correct color of its viral state.
     */
    public void draw ( )
    {
        StdDraw.setPenColor(StdDraw.BLACK);
        if(viralState == 1)
        {
            StdDraw.setPenColor(StdDraw.RED);
        }
        else if(viralState == 2)
        {
            StdDraw.setPenColor(StdDraw.GREEN);
        }
        StdDraw.filledCircle(x, y, radius);
    }

    /**
     * Increments the number of days this person object has been sick, and makes it better after 600 days.
     */
    public void incrementDaysSick() {
        daysSick++;
        if(daysSick>=600)
            viralState = 2;
    }

    /**
     * Returns true if the person is sick
     *
     * @return true if the person is sick
     */
    public boolean isSick() { return viralState==1;}

    /**
     * Checks to see if the person is colliding with another
     * @param otherPerson the other person being checked for collision with
     * @return
     */
    public boolean isTouching(Person otherPerson)
    {
        if(Math.sqrt((x-otherPerson.x)*(x-otherPerson.x)+(y-otherPerson.y)*(y-otherPerson.y))>radius+otherPerson.radius)
            return false;
        return true;
    }

    /**
     * Performs the math required to adjust the velocity and positions of this player object and the one it collides with
     *
     * @param other the other person that this player is colliding with
     */
    public void collide(Person other)
    {
        if(shelterInPlace&&other.shelterInPlace)
        {
            double dx = other.x - x;
            double dy = other.y - y;
            double dist = Math.sqrt(dx * dx + dy * dy);

            double cos = dx / dist;
            double sin = dy / dist;

            double midx = (x + other.x) / 2;
            double midy = (y + other.y) / 2;

            x = midx - radius * cos;
            y = midy - radius * sin;
            other.x = midx + other.radius * cos;
            other.y = midy + other.radius * sin;
        }
        if(shelterInPlace)
        {
            other.vx *=-1;
            other.vy *=-1;
            other.changePosition();
        }
        else if(other.shelterInPlace)
        {
            vy*=-1;
            vx*=-1;
            changePosition();
        }
        else
        {
            double dx = other.x - x;
            double dy = other.y - y;
            double dist = Math.sqrt(dx * dx + dy * dy);

            double cos = dx / dist;
            double sin = dy / dist;

            double midx = (x + other.x) / 2;
            double midy = (y + other.y) / 2;

            x = midx - radius * cos;
            y = midy - radius * sin;
            other.x = midx + other.radius * cos;
            other.y = midy + other.radius * sin;

            double velvector = (vx - other.vx) * cos + (vy - other.vy) * sin;

            vx -= velvector * cos;
            vy -= velvector * sin;
            other.vx += velvector * cos;
            other.vy += velvector * sin;
        }
        if (viralState == 1 && other.viralState == 0 && Math.random() < infectRate)
            other.viralState = 1;
        if (other.viralState == 1 && viralState == 0 && Math.random() < infectRate)
            viralState = 1;

    }
}