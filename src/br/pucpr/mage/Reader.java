package br.pucpr.mage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;


public class Reader {
    private String workingDirectory;
    private String targetPath;

    public Reader(){
        this.workingDirectory = Paths.get("").toAbsolutePath().toString();
    }

    public static InputStream findInputStream(String name){
        try{
            System.out.println(name);
            var resource = Reader.class.getResourceAsStream( "/br/pucpr/resource/" + name);
            if (resource != null){
                return resource;
            }
            return new FileInputStream(name);
        } catch (Exception e){
            throw new RuntimeException("Cannot load: " + name, e);
        }
    }

    /**
     * Le o conteúdo de um arquivo e carrega em uma String
     * @param in Um InputStream apontando para o arquivo
     * @return Um texto com o conteúdo do arquivo
     */
    public static String readInputStream(InputStream in){
        try(var br = new BufferedReader(new InputStreamReader(in))) {
            var sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null){
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Cannot load file!", e);
        }
    }

    public static BufferedImage getImageFromInputStream(InputStream in){
        try {
            return ImageIO.read(in);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load image!", e);
        }
    }



    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }


    public String getWorkingDirectory(){
        return workingDirectory;
    }
}
