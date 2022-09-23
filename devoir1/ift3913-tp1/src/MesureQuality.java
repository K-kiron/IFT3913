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
                        String filePath = "";
                        if (packageName != "unnamed package") {
                            String fullPath = file.getPath();
                            String pattern = packageName.replace('.', '/');
                            int indexStart = fullPath.indexOf(pattern);
                            filePath = "./" + fullPath.substring(indexStart);
                        } else {
                            filePath = "./" + className + ".java";
                        }
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
                String csvFormat = String.join(", ", csvOut);
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
        List<List<String>> jlsOutlist = readCSV(file);
        List<String> classPaths = getClassPathsFromCSV(jlsOutlist, path);
        List<String> classNames = getClassNamesFromCSV(jlsOutlist);
        List<List<String>> outList = new ArrayList<>();
        for (List<String> csvRow : jlsOutlist) {
            int counter = 0;
            List<String> newRow = new ArrayList<>();
            String classRelativePath = csvRow.get(0);
            String className = csvRow.get(2);
            int startIndex = classRelativePath.indexOf("/");
            int endIndex = path.lastIndexOf("/");
            String fullPath = path.substring(0, endIndex).trim() + classRelativePath.substring(startIndex).trim();
            File classFile = new File(fullPath);
            System.out.println(classFile.getName());
            int i = 0;
            for (String otherClassName : classNames) {
                if (!className.equals(otherClassName)) {
                    if (isMentionned(otherClassName, classFile)) {
                        counter++;
                        i++;
                    } else {
                        File nonMatchedFile = new File(classPaths.get(i));
                        if (isMentionned(className, nonMatchedFile)) {
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

    static private List<String> getClassPathsFromCSV(List<List<String>> data, String path) {
        List<String> fullPaths = new ArrayList<>();
        for (List<String> csvRow : data) {
            String classRelativePath = csvRow.get(0);
            int startIndex = classRelativePath.indexOf("/");
            int endIndex = path.lastIndexOf("/");
            String fullPath = path.substring(0, endIndex).trim() + classRelativePath.substring(startIndex).trim();
            fullPaths.add(fullPath);
        }
        return fullPaths;
    }

    static private List<String> getClassNamesFromCSV(List<List<String>> data) {
        List<String> classNames = new ArrayList<>();
        for (List<String> csvRow : data) {
            String className = csvRow.get(2);
            classNames.add(className);
        }
        return classNames;
    }

    static private boolean isMentionned(String className, File file) {
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (line.compareTo("") == 0)
                    continue;
                String pattern = className;
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(line);
                if (m.find()) {
                    scan.close();
                    return true;
                }
            }
            scan.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    static private void egon(String path, double threshold) {
        File out = jls(path);
        lcsec(path, out);
        List<List<String>> lcsecOutlist = readCSV(out);
        List<List<String>> outList = new ArrayList<>();
        for (List<String> csvRow : lcsecOutlist) {
            String classRelativePath = csvRow.get(0);
            int startIndex = classRelativePath.indexOf("/");
            int endIndex = path.lastIndexOf("/");
            String fullPath = path.substring(0, endIndex).trim() + classRelativePath.substring(startIndex).trim();
            File classFile = new File(fullPath);
            int nvlocInt = nvloc(classFile);
            String nvloc = Integer.toString(nvlocInt);
            List<String> newRow = new ArrayList<>();
            newRow.addAll(csvRow);
            newRow.addAll(Collections.singleton(nvloc));
            outList.add(newRow);
        }
        clearCSV(out);
        writeCSV(out, outList);
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
