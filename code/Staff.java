
package LMS;

public class Staff extends Person 
{
    private double salary;
    
    public Staff(int id, String n, String a,int p, double s)
    {
        super(id,n,a,p);
        salary = s;
    }
    
    @Override
    public void printInfo()
    {
        super.printInfo();
        System.out.println("The Salary is : " + salary + "\n");         
    }
    
    public double getSalary()
    {
        return salary;
    }
}