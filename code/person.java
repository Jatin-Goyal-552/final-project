
package LMS;

public abstract class Person 
{   
    private int id;           
    private String password;  
    private String name;      
    private String address;   
    private int mobileNumber;      
    
    static int currentIdNumber = 0;     

    public Person(int dd, String n, String a, int p)   
    {
        currentIdNumber++;
        
        if(dd==-1)
        {
            id = currentIdNumber;
        }
        else
            id = dd;
        
        password = Integer.toString(id);
        name = n;
        address = a;
        mobileNumber = p;
    }        
    
    
    public void printInfo()
    {
        System.out.println("-----------------------------------------");
        System.out.println("\nThe information are as follows: \n");
        System.out.println("Unique ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("Phone No: " + mobileNumber + "\n");
    }
    
    
    public void setAddress(String a)
    {
        address = a;
    }
    
    public void setMobileNumber(int p)
    {
        mobileNumber = p;
    }
    
    public void setName(String n)
    {
        name = n;
    }
   
    
   
    public String getName()
    {
        return name;
    }
    
    public String getPassword()
    {
        return password;
    }
    
     public String getAddress()
    {
        return address;
    }
     
     public int getPhoneNumber()
    {
        return mobileNumber;
    }
    public int getID()
    {
        return id;
    }
    
    
     public static void setIDCount(int n)
    {
        currentIdNumber=n;
    }
   
} 
