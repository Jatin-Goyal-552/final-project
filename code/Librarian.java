
package LMS;

public class Librarian extends Staff {
    
    int officeNo;     
    public static int currentOfficeNumber = 0;
     
    public Librarian(int id,String n, String a, int p, double s,int officeNumber) 
    {
        super(id,n,a,p,s);

        if(officeNumber == -1)
            officeNo = currentOfficeNumber;        
        else
            officeNo = officeNumber;
        
        currentOfficeNumber++;
    }
    
    
    @Override
    public void printInfo()
    {
        super.printInfo();
        System.out.println("Number of Office is:  " + officeNo);
    }   
}
