class Module {
    private double mark;
    private String grade;


    //constructor to initialize the module with a mark
    public Module(double mark) {
        this.mark = mark;
        calculateGrade();
        //calculate the grade based on the mark
    }

    //private method to calculate the grade based on the mark
    private void calculateGrade() {
        if (mark >= 80) grade = "Distinction";
        else if (mark >= 70) grade = "Merit";
        else if (mark >= 40) grade = "Pass";
        else grade = "Fail";
    }

    // method for the mark
    public double getMark() {
        return mark;
    }

    // methode for the grade
    public String getGrade() {
        return grade;
    }
}