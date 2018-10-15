import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;
import java.util.Random;

public class controler implements Initializable {



    @FXML
    private Button btnJoin;

    @FXML
    private Button btnOpen;

    @FXML
    private Button btnSave;

    @FXML
    private TextField txtPath;

    @FXML
    private TextArea txtEnonce;
    String str;
    String matrice [][];
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSave.setOnAction(event -> {
            File file1=null;
            if(!txtEnonce.getText().isEmpty()) {

                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extTXTFilter = new FileChooser.ExtensionFilter("TXT Files", "*.txt");


                fileChooser.getExtensionFilters().add(extTXTFilter);

                file1 = fileChooser.showSaveDialog(btnOpen.getScene().getWindow());
                if (file1 != null) {
                    try {
                        BufferedWriter br = new BufferedWriter(new FileWriter(file1));
                        br.write(txtEnonce.getText());
                        br.close();

                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }

            }
            });
        btnOpen.setOnAction(event -> {
            File file1=null;
            if(txtPath.getText().isEmpty()) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extTXTFilter = new FileChooser.ExtensionFilter("TXT Files", "*.txt");
                fileChooser.setTitle("Open statement");


                fileChooser.getExtensionFilters().add(extTXTFilter);

                file1 = fileChooser.showOpenDialog(btnOpen.getScene().getWindow());

            }else{
                file1  =new File(txtPath.getText());
            }
            if (file1 == null) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                String s = "you have not select any File !!";
                alert.setContentText(s);
                alert.showAndWait();
            } else {
                if(file1.canRead()) {
                    txtPath.setText(file1.getAbsolutePath());
                    str = openFile(file1.getAbsolutePath());

                    txtEnonce.clear();
                    txtEnonce.appendText(str);
                }else{
                    txtPath.setText("");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    String s = "File not found !!";
                    alert.setContentText(s);
                    alert.showAndWait();
                }

            }

        });
        btnJoin.setOnAction(event -> {
          if(!txtEnonce.getText().isEmpty()) {
              str=txtEnonce.getText();
              if(isCorrect())
              join();
              else{
                  Alert alert = new Alert(Alert.AlertType.ERROR);
                  alert.setTitle("Error");
                  String s = "Erreur syntaxe \nEnonce syntax : \n(concepte 1, relation, concepte 2) !!" ;
                  alert.setContentText(s);
                  alert.showAndWait();
              }
          }else{
              Alert alert = new Alert(Alert.AlertType.ERROR);
              alert.setTitle("Error");
              String s = "your statement is null !!" ;
              alert.setContentText(s);
              alert.showAndWait();
          }

        });

    }
    
   /***********************    lire le fichier d'Enoncé      ***********************************/  
    
    
    public  String openFile(String path) {
        File f = new File(path);
        String line;
        String lines = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));


            while ((line = br.readLine()) != null) {
                lines += (line + "\n");
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return lines;
    }
    
    
  /***********************     tester la syntaxe des triplés syntaxe: (concepte 1, relation, concepte 2)       ***********************************/  
    
public boolean isCorrect(){
    String [] lines = str.split("\n");
    for(int i=0;i<lines.length;i++){
       if(!lines[i].replaceAll("\\s+","").matches("[(][a-zA-Z0-9*_']+,[a-zA-Z0-9*_']+,[a-zA-Z0-9*_']+[)]")){
           System.out.println("errrr"+i);
           System.out.println(lines[i]);
                   
         return false;
       }
    }
        return true;
}




/***********************     éliminer la redondance des triplets et      ***********************************/  




    public void join(){
        String [] lines = str.split("\n");
        ArrayList<String> args= new ArrayList<>();
        ArrayList<String> relation= new ArrayList<>();
       //lire tout les fais est supprimer les redondants
        for(int i=0;i<lines.length-1;i++){
            for(int j=i+1;j<lines.length;j++){
                if(lines[i].equals(lines[j])){
                    lines[j]="";
                }
            }
        }

       /* for(int i=0;i<lines.length-1;i++){
            if(!lines[i].equals("")) {
                String[] mots = lines[i].split("[(]");

                String r1=mots[0];
                mots=mots[1].replace(")","").split(",");

                String []s1=mots[0].split(":");
                String []s2=mots[1].split(":");
                for (int j = 0; j < lines.length; j++) {
                    if(i!=j) {
                        if (s1[1].equals("*") && s2[1].equals("*") && lines[j].startsWith(r1) && lines[j].contains("(" + s1[0]) && lines[j].contains("," + s2[0])) {
                            lines[i] = "";
                            j = lines.length;


                        }
                    }

                }
            }
        }*/
        for(int i=0;i<lines.length;i++) {
            if (!lines[i].equals("")) {
                String s = lines[i].replaceFirst("[(]","");
                s= s.replaceFirst("[)]", "");
                String mots []= s.split(",");
                if (relation.indexOf(mots[1]) == -1)
                    relation.add(mots[1]);
                
                
                if (args.indexOf(mots[0]) == -1)
                    args.add(mots[0]);
                if (args.indexOf(mots[2]) == -1)
                    args.add(mots[2]);

            }
        }
        
        
        
        
        System.out.println();
        System.out.println(args);
        System.out.println(relation);
        
          /***********************************   cree fichier .dot puis afficher graphe  ******************/
        try {
            toString(args,lines);
            String path=System.getProperty("user.dir")+"/tmp01.dot";
             // System.out.println(path);
            graph(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        
        
        
        
       /* matrice=new String[args.size()+1][args.size()+1];
        matrice[0][0]="X";

        for(int i1=1;i1<matrice.length;i1++){
            matrice[0][i1]=args.get(i1-1);
            matrice[i1][0]=args.get(i1-1);
        }
        for(int i1=1;i1<matrice.length;i1++) {

                for (int r = 0; r < lines.length; r++) {

                        if(lines[r].contains("("+matrice[i1][0])){
                            for(int i2=1;i2<matrice.length;i2++){
                                if(lines[r].contains(","+matrice[0][i2])){
                                    matrice[i1][i2]= lines[r].split("[(]")[0];
                                }

                            }
                        }



            }
        }
        for(int i1=0;i1<matrice.length;i1++){
            for(int i2=0;i2<matrice.length;i2++){
                System.out.print(matrice[i1][i2]+", ");
            }
            System.out.println();
        }
*/
       
       
     

    }
    
    
    /***********************************   creation d'un  fichier tmp01.dot   ******************/
    
    
    static void toString(ArrayList<String>args,String [] lines) throws FileNotFoundException{
        int j;
        String tab[]=new String[args.size()];

        String label="label=\"Réseau_sémantique";

        label += "\";\n";

        PrintWriter out = new PrintWriter(String.format("tmp%02d.dot",1));
        out.print("digraph G {\n"+" rankdir=LR;\n "+label);


        for(j=0;j<args.size();j++){
            tab[j]=getSaltString();
            out.print(" "+tab[j]+"[label=\""+args.get(j)+"\",shape=\"");
            out.print("box\"");
            out.println("];");
        }
        out.println();

        /*for(j=0;j<lines.length;j++){
            if(!lines[j].equals("")) {
                out.print(" " + j + "[label=\"" + lines[j].split("[(]")[0] + "\"");

                out.println("];");
            }
        }
        out.println();*/

     //   for(int i=0;i<args.size();i++) {
            for (j = 0; j < lines.length; j++) {
                if (!lines[j].equals("")) {
                    String s = lines[j].replaceFirst("[(]","").replaceFirst("[)]","");
                    String mots []= s.split(",");
                    
                        out.println(tab[args.indexOf(mots[0])]+"->"+tab[args.indexOf(mots[2])]+"[label=\""+mots[1]+"\"];");
                    
                }
            }
            //  }
        out.println();



        out.println("}");
        out.close();




    }
    
    
   /***********************************   creation de L'image du graphe tmp01.dot.png   ******************/ 
    
    
    
    public static void graph(String fileDot){
        try {
            File f = new File(fileDot);
            String arg1 =f.getAbsolutePath();
           
            String arg2 = arg1 + ".png";
            String[] c = {"C:/Program Files (x86)/Graphviz2.38/bin/dot.exe", "-Tpng", arg1, "-O"};
            Process p = Runtime.getRuntime().exec(c);
            sleep(5000);

afficher(arg2);

            int err = p.waitFor();
        }
        catch(IOException e1) {
            System.out.println(e1);
        }
        catch(InterruptedException e2) {
            System.out.println(e2);
        }
    }
    
    
    
    
     /***********************************   Affichage  du graphe final  ******************/ 

    public static void  afficher(String path){
        Stage theStage=new Stage();
        theStage.setTitle( "Graphe Concepuels" );

        AnchorPane root = new AnchorPane();
        Scene theScene = new Scene( root );
        theStage.setScene( theScene );
        root.setStyle("-fx-background-color:#fff;");
        Image mg = new Image("file:/"+path);
        ImageView img = new ImageView(mg);
        img.setX(0);
        img.setY(45);
        Button save = new Button("save result");
        save.setStyle(" -fx-background-color: #4CD4B0;" +
                " -fx-background-insets: 0,1,2,0;\n" +
                "    -fx-text-fill: #FFF;\n" +
                "    -fx-font-family: \"Segoe UI Semibold\";\n" +
                "    -fx-font-size: 16px;\n" +
                "    -fx-padding: 10 20 10 20;");
        save.setLayoutX(10);
        save.setLayoutY(0);
        save.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extPNGFilter = new FileChooser.ExtensionFilter("PNG Images", "*.png");


            fileChooser.getExtensionFilters().add(extPNGFilter);
            fileChooser.setTitle("Save Image");

            File file = fileChooser.showSaveDialog(theStage);
            if (file != null) {
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(img.getImage(),
                            null), "png", file);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        root.getChildren().add(img);
        root.getChildren().add(save);






        theStage.show();
    }

    
  /***********************************  générer des Strings aléatoire qui utilisent en création de fichier .dot  ******************/    
    
protected static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }


}
