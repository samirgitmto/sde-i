package mt3_thread_coordination;

import java.io.IOException;

/*
to stop the application: typing 'q', setting the thread as a daemon, or forcefully terminating the application.
 This highlights the importance of understanding thread behavior in Java, particularly how non-daemon threads prevent application termination.
 */

public class Quiz1ThreadTermination {

    public static void main(String [] args) {
        Thread thread = new Thread(new WaitingForUserInput());
        thread.setName("InputWaitingThread");
        thread.start();
    }
 
    private static class WaitingForUserInput implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    char input = (char) System.in.read();
                    if(input == 'q') {
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println("An exception was caught " + e);
            };
        }
    }
	
}