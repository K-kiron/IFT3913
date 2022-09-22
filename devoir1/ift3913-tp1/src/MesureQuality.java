import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class MesureQuality {
    static private File jls(String path) throws IOException {
        List<List<String>> csvOutList= new ArrayList<>();
        getCsvData(path,csvOutList);
        File file=new File("output.csv");

        for(List<String> csvOut:csvOutList){
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
    }

    static private void getCsvData(String projectPath, List<List<String>> csvOutList) {
        File root = new File(projectPath);
        File[] files = root.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (!file.isFile()) {
                String newPath = projectPath + "/" + file.getName();
                getCsvData(newPath,csvOutList);
            } else {
                String fileName = file.getName();
                if (fileName.endsWith(".java")) {
                    int indexDot = fileName.lastIndexOf(".");
                    String className = fileName.substring(0, indexDot);
                    String packageName = getPackageName(file);
                    String filePath="";
                    if(packageName!="unnamed package"){
                        String fullPath=file.getPath();
                        String pattern=packageName.replace('.','/');
                        int indexStart=fullPath.indexOf(pattern);
                        filePath="./"+fullPath.substring(indexStart);
                    }else{
                        filePath="./"+className+".java";
                    }

                    List<String> csvData=new ArrayList<>();
                    csvData.add(filePath);
                    csvData.add(packageName);
                    csvData.add(className);
                    csvOutList.addAll(Arrays.asList(csvData));
                }
            }
        }
    }

    static private String getPackageName(File file) {
        try {
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()){
                String line = scan.nextLine();
                if(line.compareTo("") == 0)
                    continue;
                Matcher m = Pattern.compile ("package(.*);").matcher(line);
                if(m.matches()){
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

    public static void main(String[] args) throws IOException {
        String resourceName = "config.json";
        InputStream inputStream = MesureQuality.class.getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new NullPointerException("Cannot find resource file " + resourceName);
        }
        JSONTokener tokener = new JSONTokener(inputStream);
        JSONObject jsonObj = new JSONObject(tokener);
        String projectPath = jsonObj.getString("PROJECT_PATH");

        File outCsv=jls(projectPath);

    }

}
