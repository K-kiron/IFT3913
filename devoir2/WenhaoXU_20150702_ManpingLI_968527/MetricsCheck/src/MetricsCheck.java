import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MetricsCheck {
    //Convert .csv file and split each line by "," into a list of strings
    //and delete the first line as well as the first column
    static private List<List<String>> convertCSVToArrayList(File file) {
        try {
            Scanner scan = new Scanner(file);
            List<List<String>> csvOutputs = new ArrayList<>();
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.compareTo("") == 0)
                    continue;
                //if there consist of "" in the line, delete them as well as the "," inside the ""
                //for exemple, "1,741" to 1741
                if (line.contains("\"")) {
                    String[] lineArray = line.split(",");
                    for (int i = 0; i < lineArray.length; i++) {
                        if (lineArray[i].contains("\"")) {
                            lineArray[i] = lineArray[i]+lineArray[i+1];
                            lineArray[i] = lineArray[i].replaceAll("\"", "");
                            System.arraycopy(lineArray, i+2, lineArray, i+1, lineArray.length-i-2);
                            lineArray = Arrays.copyOf(lineArray, lineArray.length-1);
                        }
                    }
                    line = String.join(",", lineArray);
                }
                List<String> lineList = Arrays.asList(line.split(","));
//                lineList.remove(0);
                csvOutputs.add(lineList);
            }
            scan.close();
            csvOutputs.remove(0);
            return csvOutputs;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Calculate the average of a list of list of Strings and return a list of Strings
    static private List<List<String>> average(List<List<String>> csvOutputs) {
        int LCOM = 0;
        int WMC = 0;
        int CBO = 0;
        int RFC = 0;
        int NOC = 0;
        int LOC = 0;
        int CLOC = 0;
        int Jm = 0;
        int JTA = 0;

        for (int i = 0; i < csvOutputs.get(0).size(); i++) {
            switch (csvOutputs.get(0).get(i)) {
                case "LCOM" -> LCOM = i;
                case "WMC" -> WMC = i;
                case "CBO" -> CBO = i;
                case "RFC" -> RFC = i;
                case "NOC" -> NOC = i;
                case "LOC" -> LOC = i;
                case "CLOC" -> CLOC = i;
                case "Jm" -> Jm = i;
                case "JTA" -> JTA = i;
            }
        }

        csvOutputs.remove(0);

        List<String> average = new ArrayList<>();
        List<String> count = new ArrayList<>();
        List<String> index = new ArrayList<>();
        List<List<String>> result = new ArrayList<>();

        //To avoid putting "n/a" & "#N/A" into count
        int LCOMSum = 0;
        int LCOMCount = csvOutputs.size();
        int WMCSum = 0;
        int WMCCount = csvOutputs.size();
        int CBOsum = 0;
        int CBOcount = csvOutputs.size();
        int RFCsum = 0;
        int RFCcount = csvOutputs.size();
        int NOCsum = 0;
        int NOCcount = csvOutputs.size();
        int LOCsum = 0;
        int LOCcount = csvOutputs.size();
        double CommentDensitySum = 0;
        int Jmcount = csvOutputs.size();
        int JTAcount = csvOutputs.size();

        for (List<String> csvOutput : csvOutputs) {
            if (csvOutput.get(LCOM).compareTo("n/a") == 0
                    || csvOutput.get(LCOM).compareTo("#N/A") == 0) {
                LCOMCount--;
            } else {
                LCOMSum += Integer.parseInt(csvOutput.get(LCOM));
            }
            if (csvOutput.get(WMC).compareTo("n/a") == 0
                    || csvOutput.get(WMC).compareTo("#N/A") == 0) {
                WMCCount--;
            } else {
                WMCSum += Integer.parseInt(csvOutput.get(WMC));
            }
            if (csvOutput.get(CBO).compareTo("n/a") == 0
                    || csvOutput.get(CBO).compareTo("#N/A") == 0) {
                CBOcount--;
            } else {
                CBOsum += Integer.parseInt(csvOutput.get(CBO));
            }
            if (csvOutput.get(RFC).compareTo("n/a") == 0
                    || csvOutput.get(RFC).compareTo("#N/A") == 0) {
                RFCcount--;
            } else {
                RFCsum += Integer.parseInt(csvOutput.get(RFC));
            }
            if (csvOutput.get(NOC).compareTo("n/a") == 0
                    || csvOutput.get(NOC).compareTo("#N/A") == 0) {
                NOCcount--;
            } else {
                NOCsum += Integer.parseInt(csvOutput.get(NOC));
            }
            if (csvOutput.get(LOC).compareTo("n/a") == 0
                    || csvOutput.get(LOC).compareTo("#N/A") == 0) {
                LOCcount--;
            } else {
                LOCsum += Integer.parseInt(csvOutput.get(LOC));
                CommentDensitySum += Double.parseDouble(csvOutput.get(CLOC)) / Double.parseDouble(csvOutput.get(LOC));
            }
            if (csvOutput.get(Jm).compareTo("n/a") == 0
                    || csvOutput.get(Jm).compareTo("#N/A") == 0) {
                Jmcount--;
            }
            if (csvOutput.get(JTA).compareTo("n/a") == 0
                    || csvOutput.get(JTA).compareTo("#N/A") == 0) {
                JTAcount--;
            }
        }

        average.add(String.valueOf(LCOMSum / LCOMCount));
        average.add(String.valueOf(WMCSum / WMCCount));
        average.add(String.valueOf(CBOsum / CBOcount));
        average.add(String.valueOf(RFCsum / RFCcount));
        average.add(String.valueOf(NOCsum / NOCcount));
        average.add(String.valueOf(LOCsum / LOCcount));
        average.add(String.valueOf(CommentDensitySum / LOCcount));
        average.add("0.9");
        average.add("0");

        count.add(String.valueOf(LCOMCount));
        count.add(String.valueOf(WMCCount));
        count.add(String.valueOf(CBOcount));
        count.add(String.valueOf(RFCcount));
        count.add(String.valueOf(NOCcount));
        count.add(String.valueOf(LOCcount));
        count.add(String.valueOf(LOCcount));
        count.add(String.valueOf(Jmcount));
        count.add(String.valueOf(JTAcount));

        index.add(String.valueOf(LCOM));
        index.add(String.valueOf(WMC));
        index.add(String.valueOf(CBO));
        index.add(String.valueOf(RFC));
        index.add(String.valueOf(NOC));
        index.add(String.valueOf(LOC));
        index.add(String.valueOf(CLOC));
        index.add(String.valueOf(Jm));
        index.add(String.valueOf(JTA));

        result.add(average);
        result.add(count);
        result.add(index);

        return result;
    }

    //Check if the csv classe has a value lower than average(the average might be double),
    // if so, count++, return the percentage
    static private List<Double> judge(List<List<String>> result, List<List<String>> csvOutputs) {
        List<Double> percentage = new ArrayList<>();
        for (int i = 0; i < result.get(0).size(); i++) {
            int count = 0;
            for (List<String> csvOutput : csvOutputs) {
                String value = csvOutput.get(Integer.parseInt(result.get(2).get(i)));
                if (value.compareTo("n/a") == 0 || value.compareTo("#N/A") == 0) {
                    continue;
                }
                //if there consist of percentage, convert it
                //for exemple, "92.31%" -> 0.9231, "90%" -> 0.9
                if (value.contains("%")) {
                    value = value.substring(0, value.length() - 1);
                    value = String.valueOf(Double.parseDouble(value) / 100);
                }
                if (Double.parseDouble(value) <= Double.parseDouble(result.get(0).get(i))) {
                    count++;
                }


            }
            percentage.add((double) count / Integer.parseInt(result.get(1).get(i)));
        }
        return percentage;
    }

    //print the result of Q1
    //calculate the average of the average of WMC,RFC,LOC,CommentDensity.
    //if the result is greater than 90%, print "Pass", else print "Fail"
    static private void resultQ1(List<Double> percentage) {
        double average = 0;
        for (int i = 0; i < percentage.size(); i++) {
            if (i == 1 || i == 3 || i == 5 ) {
                average += percentage.get(i);
            }else if(i == 6){
                average += 1 - percentage.get(i);
            }
        }
        average = average / 4;
        if (average >= 0.8) {
            System.out.println("Le niveau de documentation des classes est approprié à leur complexité.");
        } else {
            System.out.println("Le niveau de documentation des classes n'est pas approprié à leur complexité.");
        }
    }

    //print the result of Q2 "La conception est-elle bien modulaire?"
    //calculate the average of the average of LCOM,WMC,CBO,NOC.
    //if the result is greater than 90%, print "Pass", else print "Fail"
    static private void resultQ2(List<Double> percentage) {
        double average = 0;
        for (int i = 0; i < percentage.size(); i++) {
            if (i == 0 || i == 1 || i == 2 || i == 4) {
                average += percentage.get(i);
            }
        }
        average = average / 4;
        if (average >= 0.8) {
            System.out.println("La conception est bien modulaire.");
        } else {
            System.out.println("La conception n'est pas bien modulaire.");
        }
    }

    //print the result of Q3 "Le code est-il mature?"
    //calculate the average of the average of LCOM,Jm,JTA.
    //if the result is greater than 90%, print "Pass", else print "Fail"
    static private void resultQ3(List<Double> percentage) {
        double average = 0;
        for (int i = 0; i < percentage.size(); i++) {
            if (i == 0 || i == 8) {
                average += percentage.get(i);
            }else if(i == 7){
                average += 1 - percentage.get(i);
            }
        }
        average = average / 3;
        if (average >= 0.8) {
            System.out.println("Le code est mature.");
        } else {
            System.out.println("Le code n'est pas mature.");
        }
    }

    //print the result of Q4 "Le code peut-il être testé bien automatiquement?"
    //calculate the average of the average of CBO,RFC,NOC,Jm.
    //if the result is greater than 90%, print "Pass", else print "Fail"
    static private void resultQ4(List<Double> percentage) {
        double average = 0;
        for (int i = 0; i < percentage.size(); i++) {
            if (i == 2 || i == 3 || i == 4 ) {
                average += percentage.get(i);
            }else if(i == 7){
                average += 1 - percentage.get(i);
            }
        }
        average = average / 4;
        if (average >= 0.8) {
            System.out.println("Le code peut être testé bien automatiquement.");
        } else {
            System.out.println("Le code ne peut pas être testé bien automatiquement.");
        }
    }

    public static void main(String[] args) {
        File file = new File("/Users/kironrothschild/Desktop/A2022/IFT3913/IFT3913_Git/devoir2/" +
                "WenhaoXU_20150702_ManpingLI_968527/MetricsCheck/rawdata.csv");
        List<List<String>> csvOutputs = convertCSVToArrayList(file);
        List<List<String>> result = average(csvOutputs);
        List<Double> percentage = judge(result, csvOutputs);
        resultQ1(percentage);
        resultQ2(percentage);
        resultQ3(percentage);
        resultQ4(percentage);
    }


}
