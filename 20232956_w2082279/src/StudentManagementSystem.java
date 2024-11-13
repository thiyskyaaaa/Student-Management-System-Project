import java.io.*;
import java.util.Arrays;
import java.util.*;


public class StudentManagementSystem {
    //Maximum number of students that can be stored in the system
    private static final int MAX_STUDENTS = 100;
    //Each row represent a student with ID and Name
    private static final Student[] students = new Student[MAX_STUDENTS];
    //keep track of the number of student currently in the system
    private static int studentCount = 0;
    //Scanner object to read user input from console
    private static Scanner scanner = new Scanner(System.in);

    //Main loop of the program,continues to operate until the user decides to stop it
    public static void main(String[] args) {
        //using this method to load old information in the system
        loadStudentDetails();

        //main program loop
        while (true) {
            try {
                //Display the menu options to the user
                printMenu();
                //Get user's choice
                int choice = getIntInput("Enter your choice: ");

                //process user's choice
                switch (choice) {
                    //check available seats
                    case 1:
                        checkAvailableSeats();
                        break;
                    //register a new student
                    case 2:
                        registerStudent();
                        break;
                    //delete a student by ID
                    case 3:
                        deleteStudent();
                        break;
                    //find a student by ID
                    case 4:
                        findStudent();
                        break;
                    //save student data to file
                    case 5:
                        storeStudentDetails();
                        break;
                    //load student data from file
                    case 6:
                        loadStudentDetails();
                        break;
                    //display sorted list of students
                    case 7:
                        viewSortedStudents();
                        break;
                    //display student results
                    case 8:
                        showResults();
                        break;
                    //exit the program
                    case 9:
                        System.out.println("Exiting...");
                        return;
                    //handle invalid menu options that user inputs
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 to 9.");
                }
            } catch (Exception e) {
                //catch any unexpected errors that can happen during execution
                System.out.println("Error occurred: " + e.getMessage());
            }
        }
    }


    private static void printMenu() {
        //display the menu options to the user for input
        System.out.println("\n1. Check available seats");
        System.out.println("2. Register student");
        System.out.println("3. Delete student");
        System.out.println("4. Find Student");
        System.out.println("5. Store student details into a file");
        System.out.println("6. Load student details from the file");
        System.out.println("7. View sorted list of students");
        System.out.println("8. Show Results");
        System.out.println("9. Exit");
    }


    private static void checkAvailableSeats() {
        try {
            // Calculate the number of available seats in the
            int availableSeats = MAX_STUDENTS - studentCount;

            // Check if the calculation resulted in a valid number
            if (availableSeats < 0) {
                throw new IllegalStateException("Student count exceeds maximum allowed students.");
            }

            // Display the number of available seats
            System.out.println("Available Seats: " + availableSeats);
        } catch (IllegalStateException e) {
            // Handle the case where student count somehow exceeded the maximum
            System.out.println("Error: " + e.getMessage());
            System.out.println("Please contact system administrator.");
        } catch (Exception e) {
            // Handle any unexpected errors
            System.out.println("Error!!!!.");
            System.out.println("Error details: " + e.getMessage());
        }
    }


    private static void registerStudent() {
        // Register new students to the system
        try {
            // Check if there are available seats
            if (studentCount >= MAX_STUDENTS) {
                // If the available seats are exceeded, can't process the registration
                // and print a message to the user
                // At the same time, it'll stop and return to the main menu
                throw new InputMismatchException("No available seats");
            }

            // Get the student ID
            String id = getStringInput("Enter student ID (7 digits starting with w, eg:w2082279): ").toLowerCase();

            // Make sure about the ID format starting from 'w' followed by 7 digits
            if (!id.matches("[Ww]\\d{7}")) {
                // If the ID format is incorrect, handle the error and print a message to the user
                // and return to the main menu
                throw new InputMismatchException("Invalid student ID format. It should be 'w' followed by 7 digits (e.g. w2082279)");
            }

            // Check if the ID already exists in the system
            if (findStudentIndex(id) != -1) {
                // If the ID already exists, show a message to the user
                // and return to the menu again
                throw new InputMismatchException("A student with this ID already exists");
            }

            // Initialize the name variable
            String name = "";

            // Loop until a valid name is entered
            while (true) {
                // Get student name from the user
                name = getStringInput("Enter student name: ");

                // Check if the name has only alphabetical characters
                if (name.matches("[a-zA-Z\\s]+")) {
                    // If valid, break out of the loop
                    break;
                } else {
                    // If invalid, show an error message and prompt again
                    System.out.println("Invalid student name. It should contain only alphabetical characters.");
                }
            }

            // Create a new student object and add to the array
            students[studentCount] = new Student(id, name);
            studentCount++;

            // Inform user of successful registration
            System.out.println("Student registered successfully! If you want to store the registration go to 5");
        } catch (InputMismatchException e) {
            // Handle the error and display any input mismatch errors
            System.out.println("Error: " + e.getMessage());
        }
    }



    private static void deleteStudent() {
        //display the current student list of student
        viewSortedStudents();

        while (true) {
            try {
                // Get the student ID to delete
                String id = getStringInput("Enter student ID to delete (7 digits starting with w, eg:w2082279): ").toLowerCase();

                // Find the student index
                int index = findStudentIndex(id);

                // If the student ID is not found, throw an exception
                if (index == -1) {
                    throw new Exception("Student not found");
                }

                // Shift the students array to remove the student at the found index
                for (int i = index; i < studentCount - 1; i++) {
                    students[i] = students[i + 1];
                }

                // Clear the last element to avoid duplicate references
                students[studentCount - 1] = null;
                // Decrease the student count
                studentCount--;

                // Inform the user of successful delete and how to save
                System.out.println("Student deleted successfully. To save the update, go to option 5");
                break; // Exit the loop after successful deletion
            } catch (Exception e) {
                // Handle any error and display error message
                System.out.println("Error: " + e.getMessage());
                // Prompt the user to try again
                System.out.println("Please try again.");
            }
        }
    }



    private static void findStudent() {
        try {
            //get the ID from user input
            String id = getStringInput("Enter Student ID to find (7 digits starting with W or w, e.g., w2082279): ").toLowerCase();

            //make sure about the student ID format
            if (!id.matches("w\\d{7}")) {
                throw new Exception("Invalid student ID format. It should be 'W' or 'w' followed by 7 digits (e.g., w2082279 or W2082279)");
            }

            //find the index of the student with given ID
            int index = findStudentIndex(id);

            //if student is not found,throw an exception
            if (index == -1) {
                throw new Exception("Student not found");
            }

            //if student is found, print the details
            printStudentDetails(students[index]);
        } catch (Exception e) {
            //Display any error message
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static void storeStudentDetails() {
        try (
                // create a PrintWriter to write to the file "students.txt"
                PrintWriter writer = new PrintWriter(new FileWriter("students.txt"))) {
            //Iterate all registered students
            for (int i = 0; i < studentCount; i++) {
                //get the current student
                Student student = students[i];

                //write the student's ID and name to the file, separated by a comma
                writer.println(student.getId() + "," + student.getName());
            }

            //Inform the user that the operation was successful
            System.out.println("Student details stored successfully!");
        } catch (IOException e) {
            //if an error happened during file writing,catch the IOException
            // and display an error message to the user
            System.out.println("Error storing student details: " + e.getMessage());
        }
    }



    private static void loadStudentDetails() {
        //start with a clean state:
        //set the number of students to zero
        studentCount = 0;

        //automatically close the reader when done
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            //read the file line by line
            //until maximum students reached
            while ((line = reader.readLine()) != null && studentCount < MAX_STUDENTS) {
                //split each line into parts using comma
                String[] parts = line.split(",");

                //check if the line has the ID and name
                if (parts.length != 2) {
                    throw new IOException("Invalid data format in file");
                }

                //create a new student object and add it to the students array
                students[studentCount] = new Student(parts[0], parts[1]);

                //Increment the student count
                studentCount++;
            }

            //Inform the user that proceed was successful
            System.out.println("Student details loaded successfully!");
        } catch (IOException e) {
            //if any error happened while reading the file,catch the IOException
            //and display error message to the user
            System.out.println("Error loading student details: " + e.getMessage());
        }
    }



    private static void viewSortedStudents() {
        // Check if there are no students to display
        if (studentCount == 0) {
            System.out.println("No students to display.");
            return;
        }

        // Create a copy of the students array up to the current student count
        Student[] sortedStudents = Arrays.copyOf(students, studentCount);


        // Bubble sort: Sort students by their names in alphabetical order
        for (int i = 0; i < sortedStudents.length - 1; i++) {
            for (int j = 0; j < sortedStudents.length - i - 1; j++) {

                // Compare names using compareToIgnoreCase for case-insensitive sorting
                if (sortedStudents[j].getName().compareToIgnoreCase(sortedStudents[j + 1].getName()) > 0) {

                    // Swap elements if they are out of the order
                    Student temp = sortedStudents[j];
                    sortedStudents[j] = sortedStudents[j + 1];
                    sortedStudents[j + 1] = temp;
                }
            }
        }

        // Print the sorted list of students
        System.out.println("Sorted list of students (Bubble Sort):");
        for (int i = 0; i < sortedStudents.length; i++) {
            // Print the details of each student in the sorted array
            printStudentDetails(sortedStudents[i]);
        }
    }



    private static void printStudentDetails(Student student) {
        //Print the student's ID and name
        System.out.println("ID: " + student.getId() + ", Name: " + student.getName());
    }



    private static void showResults() {
        //loop will run until the user goes to the main menu
        while (true) {
            //display the menu options
            System.out.println("\na. Add student name");
            System.out.println("b. Module marks 1, 2 and 3");
            System.out.println("c. Generate summary");
            System.out.println("d. Generate complete report");
            System.out.println("e. Return to main menu");

            //enter the choice user wants
            String choice = getStringInput("Enter your choice: ");

            //process the user's choice
            switch (choice.toLowerCase()) {
                case "a":
                    //call the method to add student name
                    addStudentName();
                    break;
                case "b":
                    //call the method to add  module mark
                    addModuleMarks();
                    break;
                case "c":
                    //call the method to add generate summary
                    generateSummary();
                    break;
                case "d":
                    //call the method to generate complete report
                    generateCompleteReport();
                    break;
                case "e":
                    //return to the main menu, exiting the loop
                    return;
                default:
                    //inform the user of an invalid choice
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }



    private static void addStudentName() {
        //get the student ID from user input
        String id = getStringInput("Enter student ID(7 digits starting with w, eg:w2082279):  ").toLowerCase();


        //find the index of the student from given ID
        int index = findStudentIndex(id);

        //check if the give student's ID exits
        if (index == -1) {
            //if the student is not found,print a message to the user
            //and return to the main menu
            System.out.println("Student not found.");
            return;
        }

        //get the new name input for the student
        String name = getStringInput("Enter new name for student: ");

        // Check if the new name contains only alphabetical characters
        if (!name.matches("[a-zA-Z\\s]+")) {
            // If the name is invalid, print an error message and return
            System.out.println("Invalid student name. It should contain only alphabetical characters.");
            return;
        }

        //update the student's name in the array
        students[index] = new Student(id, name);

        //inform the user that the student name was updated successfully
        System.out.println("Student name updated successfully!");
    }



    private static void addModuleMarks() {
        viewSortedStudents();
        //get the student ID from user input
        String id = getStringInput("Enter student ID(7 digits starting with w, eg:w2082279):  ").toLowerCase();

        //find the index of the student with the given ID
        int index = findStudentIndex(id);

        //if the student is not found print a message and return
        if (index == -1) {
            System.out.println("Student not found.");
            return;
        }

        //each of three module
        for (int i = 0; i < 3; i++) {

            //get the mark for the current module from user input
            //getDouble input make sure the input is (module mark) between 0 and 100
            double mark = Double.parseDouble(getDoubleInput("Enter mark for Module " + (i + 1) + ": "));

            //for the found student set the mark for the module
            students[index].setModuleMark(i, mark);
        }

        //inform the user that mark has been successfully updated
        System.out.println("Module marks updated successfully! ");
    }


    private static void generateSummary() {
        //print the summary topic
        System.out.println("System Summary:");

        //print the total number of students registrations
        System.out.println("Total student registrations: " + studentCount);

        //count the number of students passing each module
        int[] passCount = new int[3];

        //a loop through each of the three module
        for (int i = 0; i < studentCount; i++) {
            //get the modules of the current students
            Module[] modules = students[i].getModules();

            //loop through each of the three module
            for (int j = 0; j < 3; j++) {
                //check if the module is null and student has scored 40 or above
                if (modules[j] != null && modules[j].getMark() >= 40) {
                    //increment the pass count of the module
                    passCount[j]++;
                }
            }
        }

        //print the number of students who scored more than 50 marks
        for (int i = 0; i < 3; i++) {
            System.out.println("Students who scored more than 40 marks in Module " + (i + 1) + ": " + passCount[i]);
        }
    }



    private static void generateCompleteReport() {
        // Check if there are any students to display
        if (studentCount == 0) {
            System.out.println("No students to display.");
            return;
        }

        // Create a student array up to the current student count
        Student[] sortedStudents = Arrays.copyOf(students, studentCount);

        // Sort the array of students by their average marks in descending order
        Arrays.sort(sortedStudents, (a, b) -> Double.compare(b.getAverage(), a.getAverage()));

        // Print the header of the Complete Report
        System.out.println("Complete Report (Sorted by Average Marks, Highest to Lowest):");
        System.out.printf("%-10s %-20s %-10s %-10s %-10s %-10s %-10s %-10s\n",
                "ID", "Name", "Module 1", "Module 2", "Module 3", "Total", "Average", "Grade");

        // Print each student in the sorted array
        for (int i = 0; i < studentCount; i++) {
            Student student = sortedStudents[i];
            Module[] modules = student.getModules();

            // Prepare module marks, handling potential null modules
            double mark1 = (modules[0] != null) ? modules[0].getMark() : 0;
            double mark2 = (modules[1] != null) ? modules[1].getMark() : 0;
            double mark3 = (modules[2] != null) ? modules[2].getMark() : 0;

            try {
                // Print student details formatted in columns
                System.out.printf("%-10s %-20s %-10.2f %-10.2f %-10.2f %-10.2f %-10.2f %-10s\n",
                        student.getId(), student.getName(),
                        mark1, mark2, mark3,
                        student.getTotal(), student.getAverage(), student.getOverallGrade());
            } catch (Exception e) {
                // Catch any exceptions that might happen during printing
                System.out.println("Error printing details for student " + student.getId() + ": " + e.getMessage());
            }
        }
    }



    private static int findStudentIndex(String id) {
        //the list of students
        for (int i = 0; i < studentCount; i++) {
            //check the current ID and given ID are matched
            if (students[i].getId().equals(id)) {
                //if the match is found,return the index of the student
                return i;
            }
        }

        //if match is not found,
        // -1 to shows that the student was not found
        return -1;
    }



    private static String getStringInput(String prompt) {
        // Display the prompt and get string input from the user
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        // check if the input is empty
        //if it is , throw an exception
        if (input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be empty.");
        }

        //return the valid input
        return input;
    }



    private static int getIntInput(String prompt) {
        // Display the prompt and get integer input from the user
        System.out.print(prompt);
        try {
            // Attempt to parse the input as an integer
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            // Throw an exception if the input is not a valid integer
            throw new IllegalArgumentException("Invalid input. Please enter a number.");
        }
    }



    private static String getDoubleInput(String prompt) {
        while (true) {
            // Display the prompt and get double input from the user
            System.out.print(prompt);
            try {
                // Attempt to parse the input as a double
                double score = Double.parseDouble(scanner.nextLine().trim());
                // Validate that the score is between 0 and 100
                if (score < 0 || score > 100) {
                    throw new IllegalArgumentException("Score must be between 0 and 100.");
                }
                // Return the score as a string
                return String.valueOf(score);
            } catch (NumberFormatException e) {
                // Display an error message if the input is not a valid number
                System.out.println("Invalid input. Please enter a number.");
            } catch (IllegalArgumentException e) {
                // Display an error message if the score is not within the valid range
                System.out.println(e.getMessage());
            }
        }
    }
}


