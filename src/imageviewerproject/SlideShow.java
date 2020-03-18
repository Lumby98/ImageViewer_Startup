/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageviewerproject;

import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class SlideShow implements Runnable {
    private final long DELAY = 1;
    private int index = 0;
    private ImageView imageView;
    private List<Image> images;
    
    public SlideShow(ImageView imageView, List<Image> images){
        this.imageView = imageView;
        this.images = images;
    }
    
    @Override
    public void run(){
        if (!images.isEmpty()){
            try{
                while (true){
                    imageView.setImage(images.get(index)); 
                    index = (index + 1) % images.size();
                    TimeUnit.SECONDS.sleep(DELAY);                                        
                }
            }
            catch(InterruptedException ex){
                System.out.println("Slideshow was stopped.");
            }       
        }
    }
    
}

