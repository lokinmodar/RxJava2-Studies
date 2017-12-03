import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;



/* Working with the codes from the book Learning RxJava by Thomas Nield
    Adding my comments where i see fit for learning purposes
 */
public class Launcher {
    public static void main(String[] args) {
        Observable<String> myStrings =
                Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");
        //pushes 5 string objects
        //Observable can push data from virtually any source
        myStrings.subscribe(System.out::println); //here we could also use myString.subscribe(s -> System.out.println(s));
        //to print the length of the strings in the Observable, the observer is:
        myStrings.map(String::length).subscribe(System.out::println); //we can also use myStrings.map(s -> s.length()).subscribe(System.out::println);

        //
        Observable<Long> secondIntervals =
                Observable.interval(1, TimeUnit.SECONDS);//pushes consecutive Long at specified time interval. It is not only data but also an event!
        secondIntervals.subscribe(System.out::println);
        sleep(5000); //holds Main Thread for 5s so observable has a chance to fire.
    }
    public static void sleep(long millis){
        try {
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
