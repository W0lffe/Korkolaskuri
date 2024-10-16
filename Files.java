import java.util.Scanner;
import java.time.LocalDateTime;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.io.File;
import java.awt.Desktop;

class Files{

    private static String path = "saved/";
    private static LocalDateTime currentDate = LocalDateTime.now();


    public static void SaveFile(Scanner myScanner, int time, float percentage, float deposit, float afterIntrest, float earnings, String period){
        int userInput;
        System.out.println("\nDo you wish to save this data?\n1.Yes\n2.No");

        do {


            if(Test.testCase==true){
                userInput = 1;
                break;
            }
            else{
                userInput = Validation.UserInput(myScanner);
            } 
            if (userInput == 1 || userInput == 2) {
                break;
            }
            else{
                System.out.println("Invalid choice!");
            }   
            

        } while (true);
        
        if (userInput == 2) {
            return;
        }

        String dataToSave = toSave(time, period, percentage, deposit, afterIntrest, earnings);

        while (true) {
            String name;
            
            if(Test.testCase==true){
                name = "Testcase";
            }
            else{
                System.out.print("Give a name to file: ");
                name = myScanner.nextLine();
            }

            String newFilePath = path + name + ".txt";
    
            try {
                File newFile = new File(newFilePath);
                if (newFile.createNewFile()) {
                    System.out.println("Created file: " + newFile.getName());
                    FileWriter myWriter = new FileWriter(newFile);
                    myWriter.write(dataToSave);
                    myWriter.close();
                    break;
                }
                else if(!newFile.createNewFile()){
                    System.out.println("File already exists! Data appended! \n");
                    FileWriter myWriter = new FileWriter(newFile, true);
                    myWriter.append("\n\n" + dataToSave);
                    myWriter.close();
                    break;
                }
            } catch (IOException e) {
                System.out.println("Something went wrong!");
            }
        }
       
        OtherFunctions.Delay();
        System.out.println("Data saved!");
    }

    public static void ReadFile(Scanner myScanner){

        File folder = new File(path);
        int a = 1;
        int command;

        if(folder.exists() && folder.isDirectory()){
            File[] folderFiles = folder.listFiles(file -> file.isFile() && file.canRead() && file.canWrite());

            System.out.println("\nFolder has files: ");
           
            for (File file : folderFiles) {
                if (file.canRead() && file.canWrite()) {
                    System.out.println(a + ". " +file.getName());
                    a++;
                }
            }
            
            System.out.println("Which file would you like to read?\n");

            while (true) {
                try {
                    System.out.print("Your choice: ");
                    command = Integer.parseInt(myScanner.nextLine());
                    if (command > 0 && command < a) {
                        break;
                    }
                    else{
                        System.out.println("File not found! Try again");
                    }
                } catch (Exception e) {
                    System.out.println("Error occured!");
                }
            }

            System.out.println("You chose file: " + folderFiles[command-1].getName());
            
            String newPath = path + folderFiles[command-1].getName();
            File file = new File(newPath);

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                OtherFunctions.Delay();
                    try {
                        desktop.open(file);
                        
                    } catch (IOException e) {
                       System.out.println("Unable to open file!");
                       e.printStackTrace();
                    }
                }
            else{
                System.out.println("This system is not capable for desktop operations.");
            }    
        }
        else{
            System.out.println("Folder is not found.");
        }
    }

    private static String toSave(int time, String period, float percentage, float deposit, float afterIntrest, float earnings){
        DecimalFormat df = new DecimalFormat("0.00");
        String dataToSave = "Current date: " + currentDate;
        dataToSave += CalcAndType(); 
        dataToSave += "\n\nInvesting time: " + time + " " + period;
        dataToSave += "\nPercentage:  "+ df.format(percentage) + "%";
        dataToSave += "\nInitial deposit:  " + df.format(deposit) + " euros";
        dataToSave += "\nAfter intrest: "  + df.format(afterIntrest) + " euros";
        dataToSave += "\nEarnings: " + df.format(earnings) + " euros";
        return dataToSave;
    }

    private static String CalcAndType(){
        String dataToSave = "";
        float type = Calculations.VOLATILITY_PERCENTAGE;

        if (DataCollect.VolatilityCalc == true) {
            dataToSave += " (METHOD: VOLATILITY)";
        }
        else{
            dataToSave += " (METHOD: LINEAR)";
        } 
        
        if (type == 0.10f) {
            dataToSave += " (TYPE: Bonds, Low-Risk ETF)";   
        }
        else if(type == 0.18f){
            dataToSave += " (TYPE: Index Funds, Medium-Risk ETF)";
        }
        else if(type == 0.40f){
            dataToSave += " (TYPE: Stocks, Cryptocurrencies, Commodities)";
        }

        return dataToSave;
    }
}