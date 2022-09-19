
import java.io.File;


public class MesureQuality {
    static private String jls(String path) {
        if (path == null || path.isBlank()) return "";
        File filePath = new File(path);
        String csvFormat = "";
        String packageName = "";
        String className = "";
        if (!isDirectory(filePath)) {
            String fileName = filePath.getName();
            String parent = filePath.getParent();
            if (parent != null) {
                int index = parent.indexOf("/");
                packageName = parent.substring(index + 1);
            } else {
                packageName = "";//path only contain file name "file.txt"
            }
            int indexDot = fileName.lastIndexOf(".");
            className = fileName.substring(0, indexDot);

            csvFormat = path + "," + packageName + "," + className;
            System.out.println(csvFormat);
            return csvFormat;
        } else {
            //path is a directory
            String parent = filePath.getParent();
            if (parent != null) {
                int index = parent.indexOf("/");
                packageName = path.substring(index + 1);
            } else {
                packageName = path;//path contain only one folder from root
            }
            csvFormat = path + "," + packageName + "," + className;
            System.out.println(csvFormat);
            return csvFormat;
        }
    }

    public static void main(String[] args) {
        System.out.println(jls("./gr/spinellis/ckjm/MethodVisitor.java"));
        jls("./gr/spinellis/ckjm");
        jls("ckjm");
        jls("text.csv");
    }

    //Helper funtion to check a path is a directory or not
    public static boolean isDirectory(File filePath) {
        //check if the file/directory is already there
        if (!filePath.exists()) {
            // check if the file/directory has an extension
            return filePath.getName().lastIndexOf('.') == -1;
        } else {
            // check if it is a file or directory
            return filePath.isDirectory();
        }
    }

}
