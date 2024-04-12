package model;

public class Moderator {
    private final int employeeID;
    private final String firstName;
    private final String lastName;
    private final double salary;
    private final String team;
    private final String supervisor;

    public Moderator(int ID, String fn, String ln, double salary, String team, String supervisor) {
        this.employeeID = ID;
        this.firstName = fn;
        this.lastName = ln;
        this.salary = salary;
        this.team = team;
        this.supervisor = supervisor;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getSalary() {
        return salary;
    }

    public String getTeam() {
        return team;
    }

    public String getSupervisor() {
        return supervisor;
    }
}
