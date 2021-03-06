package imageviewerproject;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable
{
    private final List<Image> images = new ArrayList<>();
    private int currentImageIndex = 0;
    

    @FXML
    Parent root;

    @FXML
    private Button btnLoad;


    @FXML
    private ImageView imageView;
   
    @FXML
    private Label filename;
    
    private final Scheduler scheduler = new Scheduler();
    
    

    private void handleBtnLoadAction(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images", 
            "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));        
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
                
        if (!files.isEmpty())
        {
            files.forEach((File f) ->
            {
                images.add(new Image(f.toURI().toString()));
                
            });
            displayImage();
        }
        
        // create slideshow instance
        SlideShow slideshow = new SlideShow(imageView, images, filename);
        
        //Add the new slideshow instance to the scheduler
        scheduler.addSlideShow(slideshow);
    }


    private void displayImage()
    {
        if (!images.isEmpty())
        {
            imageView.setImage(images.get(currentImageIndex));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        btnLoad.setOnAction((ActionEvent event) ->
        {
            handleBtnLoadAction(event);
        });

    }


    @FXML
    private void handleBtnStopSlide(ActionEvent event)
    {
        scheduler.removeCurrentSlideshow();
    }

}
