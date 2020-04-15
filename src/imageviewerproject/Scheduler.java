/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageviewerproject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author lumby
 */
public class Scheduler implements Runnable
{
    private SlideShow currentSlideShow = null;
    private BlockingQueue<SlideShow> queue = new LinkedBlockingDeque<>();
    private final int TIMESLICE = 4;
    private ExecutorService executor = null;

    @Override
    public void run()
    {
        try{
            while(true){
                if(!queue.isEmpty()){
                    runNextSlideshow();
                }
                TimeUnit.SECONDS.sleep(TIMESLICE);
            }
        }
        catch(InterruptedException ex)
        {
            System.err.println("Slideshow scheduler was stopped.");
        }
    }
    
    public synchronized void runNextSlideshow() throws InterruptedException
    {
        currentSlideShow.stop();
        queue.put(currentSlideShow);
        currentSlideShow = queue.take();
        currentSlideShow.start();
        
    }
    
    public synchronized void addSlideShow(SlideShow s)
    {
        //Start the scheduler, if it is not already running
        if(executor == null || executor.isShutdown())
        {
            executor = Executors.newSingleThreadExecutor();
            executor.submit(this);
            
        }
        
        if(currentSlideShow == null && queue.isEmpty())
        {
            //because there are no other slideshow instances, make the new
            //instance the current slideshow and run it
            currentSlideShow = s;
            currentSlideShow.start();
        }
        else
        {
            /**There are already at least one slideshow instance. put the new
             * instance in the queue to wait for later execution.
             */
            try {
                queue.put(s);
                
            }
            catch(InterruptedException ex)
            {
                System.out.println("slideshow scheduler was stopped");
            }
        }
    }
    
    public synchronized void removeCurrentSlideshow()
    {
        if(currentSlideShow != null)
        {
            currentSlideShow.stop();
            currentSlideShow = null;
        }
        
        if(queue.isEmpty())
        {
            //Stop the scheduler
            executor.shutdownNow();
        }
        else{
            //Run the next slideshow immediately
            try{
                currentSlideShow = queue.take();
                currentSlideShow.start();
            }
            catch(InterruptedException ex){
                System.out.println("Slideshow scheduler was stopped");
            }
        }
    }
    
}
