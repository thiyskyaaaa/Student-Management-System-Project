import java.util.Arrays;

class Student {
    private String id;
    private String name;
    private Module[] modules;

    //assembler to initialize the student with an ID and name
    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.modules = new Module[3];
        // Initialize the modules array with a stable size of 3
    }

    //method to set the mark for a specific module
    public void setModuleMark(int moduleIndex, double mark) {
        modules[moduleIndex] = new Module(mark);
    }

    //method for the student ID
    public String getId() {

        return id;
    }

    //method for the student name
    public String getName() {

        return name;
    }

    //method for the array of modules
    public Module[] getModules() {
        return modules;
    }

    //method for calculate the total marks of the student
    public double getTotal() {
        return Arrays.stream(modules).mapToDouble(Module::getMark).sum();
    }

    //method to calculate the average marks of the student
    public double getAverage() {

        return getTotal() / 3;
    }

    //method to determine the overall grade based on the average marks
    public String getOverallGrade() {

        double avg = getAverage();
        if (avg >= 80) return "Distinction";
        else if (avg >= 70) return "Merit";
        else if (avg >= 40) return "Pass";
        else return "Fail";
    }
}

