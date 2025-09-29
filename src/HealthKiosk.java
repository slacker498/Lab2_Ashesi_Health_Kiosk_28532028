/*
 * Name of project: Ashesi Health Kiosk
 * Name of author: Jachin Hugh Dzidumor Kpogli
 * Date created: 26th September, 2025
 * Purpose: This program is designed for the Ashesi University's Health Center to simulate
 *          "self-service kiosk". A student (user) enters some details and based on that, a 
 *          short intake slip is printed providing the following information:
 *          a) Which desk to visit
 *          b) A quick metric (e.g., BMI)
 *          c) A basic ID sanity check
 *          d) A short “secure” identifier 
 */

import java.util.Scanner;

public class HealthKiosk {
    public static void main(String[] args) throws Exception {
        
        // Task 0 — Project setup
        Scanner sc = new Scanner(System.in); // Create Scanner object for user input
        System.out.println("Welcome!\n");


        // Task 1 — Service Router (focus: switch)
        System.out.println("Enter service code (P/L/T/C): ");
        char serviceCode = Character.toUpperCase((sc.next()).charAt(0)); // Accept input for service code

        System.out.println( "\nGo to: " + 
            switch (serviceCode) {
                case 'P'-> "Pharmacy Desk";
                case 'L'-> "Lab Desk";
                case 'T'-> "Triage Desk";
                case 'C'-> "Counselling Desk";
                default -> "Invalid service code";
            }
        );


        // Task 2 — Mini Health Metric (focus: Math functions)
        // Code executes if Triage has been selected
        byte healthMetric = 0; // To be used if triage is chosen (to allow for local variable to be used globally)
        double metricData = 0; // To be used if triage and bmi are chosen (to allow for local variable to be used globally)
 
        if (serviceCode=='T') {
            System.out.println("Enter the health metric (1 for BMI, 2 for Dosage round-up, 3 for simple trig helper): ");
            healthMetric = sc.nextByte();     

            switch (healthMetric) {
                case 1: // BMI quick calc
                    // Accept input for weight and height
                    System.out.println("Enter weight in kg: ");
                    double weight = sc.nextDouble();
                    System.out.println("Enter height in metres: ");
                    double height = sc.nextDouble();

                    // Compute BMI and display BMI category
                    double bmi = Math.round((weight/Math.pow(height, 2)) * 10) / 10.0;
                    if (bmi < 18.5) System.out.println("Category: Underweight");
                    else if (bmi>=18.5 && bmi<=24.9) System.out.println("Category: Normal");
                    else if (bmi>=25.0 && bmi<=29.9) System.out.println("Category: Overweight");
                    else if (bmi >= 30) System.out.println("Category: Obese");

                    metricData = bmi; // Store metric value if used

                    break;
                case 2: // Dosage round-up
                    // Accept input for required dosage
                    System.out.println("Enter the required dosage (mg): ");
                    double requiredDosage = sc.nextDouble();

                    // Compute and display number of tablets
                    final int tabletsDispensedByPharmacy = 250;
                    int tablets = (int) (Math.ceil(requiredDosage / tabletsDispensedByPharmacy));
                    System.out.println("Number of tablets: " + tablets);
                    
                    metricData = tablets; // Store metric value if used

                    break;
                case 3: // Simple trig helper
                    // Accept user input for angle in degrees
                    System.out.println("Enter an angle in degrees: ");
                    double angleInDegrees = sc.nextDouble();

                    // Compute and display computed sin and cos of angle, where angle is in radians
                    double angleInRadians = Math.toRadians(angleInDegrees);
                    double sineOfAngle = Math.round(Math.sin(angleInRadians) * 1000) / 1000.0;
                    double cosOfAngle = Math.round(Math.cos(angleInRadians) * 1000) / 1000.0;

                    System.out.printf("sin(%0.3f) = %0.3f and cos(%0.3f) = %0.3f, where angles are in degrees.\n", angleInDegrees, sineOfAngle, angleInDegrees, cosOfAngle);
                    
                    metricData = sineOfAngle; // Store metric value if used
                    
                    break;
                default:
                    System.out.println("Invalid input!");
                    break;
            }
        }

        // Task 3 — ID Sanity Check (focus: characters & strings)
        // Generate student ID
        char randomUpperCharacter = (char) (65 + (Math.random() * (26)));
        byte randomNum1 = (byte) (3 + (int) (Math.random()*7));
        byte randomNum2 = (byte) (3 + (int) (Math.random()*7));
        byte randomNum3 = (byte) (3 + (int) (Math.random()*7));
        byte randomNum4 = (byte) (3 + (int) (Math.random()*7));
        String shortCode = "" + randomUpperCharacter + randomNum1 + randomNum2 + randomNum3 + randomNum4;
        
        // Check if generated ID is valid
        boolean isValidShortCode = (shortCode.length() == 5) && (Character.isLetter(shortCode.charAt(0))) && (Character.isDigit(shortCode.charAt(1))) && (Character.isDigit(shortCode.charAt(2))) && (Character.isDigit(shortCode.charAt(3))) && (Character.isDigit(shortCode.charAt(4)));

        if (isValidShortCode) System.out.println("ID OK");
        else if (shortCode.length() != 5) System.out.println("Invalid length");
        else if (!Character.isLetter(shortCode.charAt(0))) System.out.println("Invalid: first char must be a letter");
        else System.out.println("Invalid: last 4 must be digits");


        // Task 4 — “Secure” Display Code (focus: char arithmetic & strings)
        System.out.println("\nEnter your first name: "); // Accept user input for student first name
        String studentFirstName = sc.next();

        char base = (studentFirstName.toUpperCase()).charAt(0);
        char shiftedLetter= (char) ('A' + (base - 'A' + 2) % 26);
        
        // Generate secure code
        String secureCode = shiftedLetter + shortCode.substring(3) + "-" + (Math.round(metricData));
        System.out.println("Display Code: " + secureCode);        


        // Task 5 — Service Summary (light switch + strings)
        // Print final short intake slip
        System.out.println( "\nSummary: " + 
            switch (serviceCode) {
                case 'P'-> "PHARMACY | ID=" + shortCode + " | Code=" + secureCode;
                case 'T'-> {if (healthMetric == 1) yield "TRIAGE | ID=" + shortCode + " | BMI=" + metricData + " | Code=" + secureCode;
                           else if (healthMetric == 1) yield "TRIAGE | ID=" + shortCode + " | Tablets=" + metricData + " | Code=" + secureCode;
                           else yield "TRIAGE | ID=" + shortCode + " | sin(angle)=" + metricData + " | Code=" + secureCode;}
                case 'L'-> "LAB | ID=" + shortCode + " | Code=" + secureCode;
                case 'C'-> "COUNSELLING | ID=" + shortCode + " | Code=" + secureCode;
                default -> "Invalid service code"; 
            }
        );

        sc.close(); // Close Scanner object

    }
}
