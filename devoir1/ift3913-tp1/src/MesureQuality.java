import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class MesureQuality {
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
                        String filePath =file.getPath();
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
                System.out.println(csvFormat);
                FileWriter writer = new FileWriter(file, true);
                BufferedWriter out = new BufferedWriter(writer);
                out.write(csvFormat);
                out.newLine();
                out.close();
//            System.out.println(csvOut.stream().map(Object::toString).
//                    collect(Collectors.joining(",")));
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
            System.out.println(NonEmptyLineNumber);
            return NonEmptyLineNumber;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static private void lcsec(String path, File file) {
        try {
            List<List<String>> jlsOutlist = readCSV(file);
            List<String> classPaths = getColDataFromCSVTable(jlsOutlist,0);
            List<String> classNames = getColDataFromCSVTable(jlsOutlist,2);
            List<List<String>> outList = new ArrayList<>();
            for (List<String> csvRow : jlsOutlist) {
                int counter = 0;
                List<String> newRow = new ArrayList<>();
                String classPath = csvRow.get(0);
                String className = csvRow.get(2);
                File classFile = new File(classPath);

                int i = 0;//classPaths index
                for (String otherClassName : classNames) {
                    if (!className.equals(otherClassName)) {
                        if (isMentioned(otherClassName, classFile)) {
                            counter++;//mentioned counter
                            i++;
                        } else {
                            File nonMentionedFile = new File(classPaths.get(i));
                            if (isMentioned(className, nonMentionedFile)) {
                                counter++;
                            }
                            i++;
                        }
                    } else {
                        i++;
                    }
                }
                String CSEC = Integer.toString(counter);
                newRow.addAll(csvRow);
                newRow.addAll(Collections.singleton(CSEC));
                outList.add(newRow);
            }
            clearCSV(file);
            writeCSV(file, outList);
        }catch (Exception ex) {
            ex.printStackTrace();
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
    static private List<String> getColDataFromCSVTable(List<List<String>> data,int colIndex) {
        List<String> colData = new ArrayList<>();
        for (List<String> csvRow : data) {
            colData.add(csvRow.get(colIndex));
        }
        return colData;
    }

//    isMentioned(c1,c2) is true if the name of c2 is mentioned in the code of c1. This can happen for various reasons:
//    c1 declares an object of type c2, has a method with a parameter of type c2, calls a method of an object of type
//    c2, calls a static method of c2, accesses an attribute of c2, etc.
    static private boolean isMentioned(String className, File file) {
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.compareTo("") == 0)
                    continue;
                String pattern = className;

                for (int switchcase=0;switchcase<3;switchcase++) {
                    Pattern r = switch (switchcase) {
                        case 0 -> Pattern.compile(" " + pattern);
                        case 1 -> Pattern.compile(pattern + " ");
                        case 2 -> Pattern.compile(pattern + ".");
                        default -> Pattern.compile(pattern);
                    };
                    //Pattern r = Pattern.compile(pattern);
                    Matcher m = r.matcher(line);
                    if (m.find()) {
                        scan.close();
                        return true;
                    }
                }
            }
            scan.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    static private void egon(String path, double seuil) {
        File out = jls(path);
        lcsec(path, out);
        List<List<String>> lcsecOutlist = readCSV(out);
        List<List<String>> outList = new ArrayList<>();
        for (List<String> csvRow : lcsecOutlist) {
            String classPath = csvRow.get(0);
            File classFile = new File(classPath);
            int nvlocInt = nvloc(classFile);
            String nvloc = Integer.toString(nvlocInt);
            List<String> newRow = new ArrayList<>();
            newRow.addAll(csvRow);
            newRow.addAll(Collections.singleton(nvloc));
            outList.add(newRow);
        }
        clearCSV(out);
        writeCSV(out, outList);
        List<List<String>> egonResult = new ArrayList<>();
        List<String> newline = new ArrayList<>();
        egonResult.add(newline);
        List<String> egonTitle = new ArrayList<>(Collections.singleton(seuil + " seul:"));
        egonResult.add(egonTitle);
        List<String> CSECs=getColDataFromCSVTable(outList,3);
        List<String> NVLOCs=getColDataFromCSVTable(outList,4);
        CSECs.sort(Collections.reverseOrder());
        NVLOCs.sort(Collections.reverseOrder());
        boolean egonExist = false;
        for(List<String> row:outList){
            String csec = row.get(3);
            String nvloc = row.get(4);
            if (CSECs.indexOf(csec)%CSECs.size()<=seuil&&
                NVLOCs.indexOf(nvloc)%NVLOCs.size()<=seuil){
                egonResult.add(row);
                egonExist = true;
            }
        }
        if (!egonExist){
            List<String> none = new ArrayList<>(Collections.singleton("NONE"));
            egonResult.add(none);
        }
        writeCSV(out,egonResult);
    }

    public static void main(String[] args) {
        String resourceName = "config.json";
        InputStream inputStream = MesureQuality.class.getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new NullPointerException("Cannot find resource file " + resourceName);
        }
        JSONTokener tokener = new JSONTokener(inputStream);
        JSONObject jsonObj = new JSONObject(tokener);
        String projectPath = jsonObj.getString("PROJECT_PATH");

        double threshold = 0.1;
        egon(projectPath, threshold);
    }

}
