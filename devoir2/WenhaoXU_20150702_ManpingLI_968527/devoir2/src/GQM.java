import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GQM {
    static private File jls(String path) {
        List<List<String>> csvOutList = new ArrayList<>();
        getCsvData(path, csvOutList);
        File file = new File("output.csv");
        clearCSV(file);
        file = writeCSV(file, csvOutList);
        return file;
    }

    static private void getCsvData(String projectPath, List<List<String>> csvOutList) {
        File root = new File(projectPath);
        File[] files = root.listFiles();
        if (files == null) return;

        try {
            for (File file : files) {
                if (!file.isFile()) {
                    String newPath = projectPath + "/" + file.getName();
                    getCsvData(newPath, csvOutList);
                } else {
                    String fileName = file.getName();
                    if (fileName.endsWith(".java")) {
                        int indexDot = fileName.lastIndexOf(".");
                        String className = fileName.substring(0, indexDot);
                        String packageName = getPackageName(file);
                        String filePath = file.getPath();
                        List<String> csvData = new ArrayList<>();
                        csvData.add(filePath);
                        csvData.add(packageName);
                        csvData.add(className);
                        csvOutList.addAll(Arrays.asList(csvData));
                    }
                }
            }

        } catch (IllegalArgumentException iae) {
            System.out.println("File Not Found");
        }

    }
    static private String getPackageName(File file) {
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.compareTo("") == 0)
                    continue;
                Matcher m = Pattern.compile("package(.*);").matcher(line);
                if (m.matches()) {
                    int indexStart = line.indexOf("package") + 7;
                    String packageName = line.substring(indexStart, line.length() - 1).trim();
                    scan.close();
                    return packageName;
                }
            }
            scan.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "unnamed package";
    }
    static private File writeCSV(File file, List<List<String>> data) {
        try {
            for (List<String> csvOut : data) {
                String csvFormat = String.join(",", csvOut);
//                System.out.println(csvFormat);
                FileWriter writer = new FileWriter(file, true);
                BufferedWriter out = new BufferedWriter(writer);
                out.write(csvFormat);
                out.newLine();
                out.close();
            }
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        static private File clearCSV(File file) {
            try {
                FileWriter writer = new FileWriter(file);
                BufferedWriter out = new BufferedWriter(writer);
                out.write("");
                out.newLine();
                out.close();
                return file;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    static private List<List<String>> readCSV(File file) {
        try {
            Scanner scan = new Scanner(file);
            List<List<String>> jlsOutputs = new ArrayList<>();
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.compareTo("") == 0)
                    continue;
                List<String> jlslineList = Arrays.asList(line.split(","));
                jlsOutputs.add(jlslineList);
            }
            scan.close();
            return jlsOutputs;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static private List<String> getColDataFromCSVTable(List<List<String>> data, int colIndex) {
        List<String> colData = new ArrayList<>();
        for (List<String> csvRow : data) {
            colData.add(csvRow.get(colIndex));
        }
        return colData;
    }

    static private int nvloc(File file) {
        try {
            Scanner scan = new Scanner(file);
            int NonEmptyLineNumber = 0;
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.compareTo("") == 0)
                    continue;
                NonEmptyLineNumber++;
            }
            return NonEmptyLineNumber;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args){
            String resourceName = "config.json";
            InputStream inputStream = GQM.class.getResourceAsStream(resourceName);
            if (inputStream == null) {
                throw new NullPointerException("Cannot find resource file " + resourceName);
            }
            JSONTokener tokener = new JSONTokener(inputStream);
            JSONObject jsonObj = new JSONObject(tokener);
            String projectPath = jsonObj.getString("PROJECT_PATH");
            jls(projectPath);
        }




}
