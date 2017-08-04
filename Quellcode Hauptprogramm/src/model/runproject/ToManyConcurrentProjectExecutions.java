package model.runproject;

public class ToManyConcurrentProjectExecutions extends Exception {

    public ToManyConcurrentProjectExecutions(int maximumConcurrentExecutations) {
        super("Zu viele parallele Ausfuehrungen. Maximum: " + maximumConcurrentExecutations);
    }
    
}
