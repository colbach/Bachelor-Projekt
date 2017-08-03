package commandline.functions.tests;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.structures.ReferentedValue;
import utils.structures.Single;

public class BunchOfTests {
    
    private static void recursion(long actualDepths, long maxDepths, ReferentedValue<Long> depths) {
        depths.set(actualDepths);
        if (actualDepths < maxDepths) {
            recursion(actualDepths + 1, maxDepths, depths);
        }
    }
    
    private static String[] WORDS = new String[]{"tempor", "invidunt", "ut", "labore", "et", "dolore", "magna", "aliquyam", "erat"};
    
    private static String randomPhrase(int words) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words; i++) {
            sb.append(WORDS[(int) (Math.random() * WORDS.length)]);
        }
        return sb.toString();
    }


    public static HashMap<String, Test> get() {
        HashMap<String, Test> tests = new HashMap<>();
        tests.put("print", (Test) () -> {
            System.out.println("Hallo");
            System.out.println("Welt");
            System.out.println("Testausgabe (Normal)");
            System.err.println("Testausgabe (Fehler)");
            System.out.println("Ende");
        });
        tests.put("out", (Test) () -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(i + " Normalle Ausgabe");
            }
        });
        tests.put("errout", (Test) () -> {
            for (int i = 0; i < 10; i++) {
                if(i%2==0) {
                    System.out.println(i + " Normalle Ausgabe");
                } else {
                    System.err.println(i + " Fehler Ausgabe");
                }
            }
        });
        tests.put("erroutrandom", (Test) () -> {
            for (int i = 0; i < 10; i++) {
                if(Math.random() < 0.5) {
                    System.out.println(i + " Normalle Ausgabe");
                } else {
                    System.err.println(i + " Fehler Ausgabe");
                }
            }
        });
        tests.put("lotsofoutsfast100", (Test) () -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(randomPhrase((int) (Math.random() * 10)));
                try {
                    Thread.sleep((int) (Math.random() * 100));
                } catch (InterruptedException ex) {
                    Logger.getLogger(TestCommandLineFunction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        tests.put("lotsofoutsfast1000", (Test) () -> {
            for (int i = 0; i < 1000; i++) {
                System.out.println(randomPhrase((int) (Math.random() * 10)));
                try {
                    Thread.sleep((int) (Math.random() * 100));
                } catch (InterruptedException ex) {
                    Logger.getLogger(TestCommandLineFunction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        tests.put("lotsofoutsslow100", (Test) () -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(randomPhrase((int) (Math.random() * 10)));
                try {
                    Thread.sleep((int) (Math.random() * 1000));
                } catch (InterruptedException ex) {
                    Logger.getLogger(TestCommandLineFunction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        tests.put("lotsofoutsslow1000", (Test) () -> {
            for (int i = 0; i < 1000; i++) {
                System.out.println(randomPhrase((int) (Math.random() * 10)));
                try {
                    Thread.sleep((int) (Math.random() * 1000));
                } catch (InterruptedException ex) {
                    Logger.getLogger(TestCommandLineFunction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        tests.put("throwexception", (Test) () -> {
            throw new RuntimeException("Random Exception");
        });
        tests.put("counttomaxinteger", (Test) () -> {
            System.out.println("start");
            for (int i = 0; i < Integer.MAX_VALUE - 1; i++) {
            }
            System.out.println("finished");
        });
        tests.put("counttomaxlong", (Test) () -> {
            System.out.println("start");
            for (long i = 0; i < Long.MAX_VALUE - 1; i++) {
            }
            System.out.println("finished");
        });
        tests.put("sleep1min", (Test) () -> {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TestCommandLineFunction.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        tests.put("sleep1s", (Test) () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TestCommandLineFunction.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        tests.put("sleep5s", (Test) () -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TestCommandLineFunction.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        tests.put("recursion", (Test) () -> {
            ReferentedValue<Long> depths = new ReferentedValue<>(0L);
            try {
                recursion(0, Long.MAX_VALUE, depths);
            } catch (StackOverflowError e) {
            }
            System.out.println("Erreichte Tiefe: " + depths);
        });
        return tests;
    }
    
    
}
