/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageviewerproject;

import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SlideShow implements Runnable {
    private final long DELAY = 1;
    private int index = 0;
    private ImageView imageView;
    private List<Image> images;
    private Label filename;
    private ExecutorService executor;
    
    public SlideShow(ImageView imageView, List<Image> images, Label filename){
        this.imageView = imageView;
        this.images = images;
        this.filename = filename;
    }
    
    @Override
    public void run(){
        if (!images.isEmpty()){
            try{
                while (true){
                    Platform.runLater(() -> {
                    imageView.setImage(images.get(index)); 
                    filename.setText(imageView.getImage().getUrl());
                    });
                    index = (index + 1) % images.size();
                            
                    TimeUnit.SECONDS.sleep(DELAY);                                        
                }
            }
            catch(InterruptedException ex){
                System.out.println("Slideshow was stopped.");
            }       
        }
    }
    
    public void start()
    {
        executor = Executors.newSingleThreadExecutor();
        executor.submit(this);
    }
    
    public void stop()
    {
        executor.shutdownNow();
    }
    //YoMama
}

