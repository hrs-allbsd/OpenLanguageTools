

package org.jvnet.olt.editor.util;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/** This task invokes code in swing event thread
 *  Overload the execute method to run code in swing event thread.
 *
 *  The task can either by blocking on nonblocking.
 */
abstract public class Task {
    private boolean synchronous = false;
    private static final Logger logger = Logger.getLogger(Task.class.getName());
    /** Creates a new non-blocking instance of Task.
     *  call start method to start the task
     */
    public Task() {
    }
    
    /** Creates a new blocking or non-blocking instance of Task.
     *  call start method to start the task
     */
    public Task(boolean synchronous){
        this.synchronous = synchronous;
    }
    
    /** The execution method.
     *  implement this method to execute task code.
     */
    abstract protected void execute();
    
    /** Start the task
     * if the task is synchronous the invokeAndWait will be used, otherwise
     * invokeLater from SwingUtilities
     * If an exception occurs during invokeAndWait method handleException will
     * be called with the thrown exception as an argument will be called
     */
    public void start() {
        
        try{
            if(synchronous){
                SwingUtilities.invokeAndWait(new Runnable(){
                    public void run(){
                        execute();
                    }
                });
                
            } else{
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        execute();
                    }
                });
            }
        } catch (InvocationTargetException ite){
            handleException(ite);
        } catch (InterruptedException ie){
            handleException(ie);
        } catch (Throwable t){
            handleException(t);
        }
    }
    
    protected void handleException(Throwable e){
       logger.warning("Exception caught:"+e); 
    }
}
