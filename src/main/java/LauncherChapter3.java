import io.reactivex.Observable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class LauncherChapter3 {
    public static void main (String[] args){

        //RxJava2 Operators!
        //Basic Operators!

        //1 - Suppressing operators - suppress emissions that fail to meet a specified criterion


        //filter() - accepts Predicate <T> for a given Observable<T>
        //you can provide lambda expressions and those emissions that are not according to it, do not go forward in the stream
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .filter(s -> s.length() != 5)//filters words that  have 5 letters out
                .subscribe(s -> System.out.println("RECEIVED: " + s));

        //take() - has two options: take(x) emissions and call onComplete() and dispose of all subscriptions
        // or take emissions during a set interval of time.
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .take(3)
                .subscribe(s -> System.out.println("RECEIVED: " + s));

        //or

        Observable.interval(300, TimeUnit.MILLISECONDS)
                .take(2, TimeUnit.SECONDS)
                .subscribe(i -> System.out.println("RECEIVED: " + i));
        sleep(5000);//make thread sleep so we can give time for the stream to complete

        //takeLast() operator queues all emissions until onComplete()
        // and determine the last ones and emits them.

        //skip() - the opposite of the take() operator
        // Ignores the specified number of emissions.
        Observable.range(1,100)
                .skip(90)// skips the 1st 90 emissions
                .subscribe(i -> System.out.println("RECEIVED: " + i));
        //like the take() operator, it has an overload allowing for input of a time duration
        //and there is also skipLast() ans it also delays the emissions until onComplete.
        // so it can know which emissions to skip.


        // takeWhile() and SkipWhile() - take or skip emissions according to a condition[
        // they execute their operation while the condition specified is true
        Observable.range(1,100)
                .takeWhile(i -> i < 5)// if any value is >=5, the operation completes.
                .subscribe(i -> System.out.println("RECEIVED: " + i));

        Observable.range(1,100)
                .skipWhile(i -> i <= 95)
                .subscribe(i -> System.out.println("RECEIVED: " + i));

        // takeUntil and skipUntil are similar to the ones above
        // but they accept an Observable as a parameter
        // they will keep performing their operations until that other Observable pushes an emission

        //distinct() - emits each unique emission and suppresses following duplicates
        // equality is based on hashCode() or equals() implementation of emitted objects
        // keep in mind that if the emission set is big, it will cost a lot of memory


        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .map(String::length)
                .distinct()
                .subscribe(i -> System.out.println("RECEIVED: " + i));

        // It is possible to add a lambda argument to map emissions to a key used for equality evaluation
        // this allows emissions to go forward while using the key for the distinct() logic
        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .distinct(String::length)
                .subscribe(i -> System.out.println("RECEIVED: " + i));

        // distinctUntilChanged() - ignores consecutive duplicates

        Observable.just(1, 1, 1, 2, 2, 3, 3, 2, 1, 1)
                .distinctUntilChanged()
                .subscribe(i -> System.out.println("RECEIVED: " + i));

        // it is also possible to use a lambda as argument
        Observable.just("Alpha", "Beta", "Zeta", "Eta", "Gamma",
                "Delta")
                .distinctUntilChanged(String::length)
                .subscribe(i -> System.out.println("RECEIVED: " + i));


        // elementAt() - geets emission by its index (Long type) starting at 0
        // when the item is emitted, it calls onComplete() and dispose of the subscription

        Observable.just("Alpha", "Beta", "Zeta", "Eta", "Gamma", "Delta")
                .elementAt(3)
                .subscribe(i -> System.out.println("RECEIVED: " + i));
        //it returns Maybe<T> instead of Observable<T>
        // other flavors: elementAtOrError(), singleElement(), firstElement() and lastElement()

        //2 - Transforming operators - transform emissions!

        //map() - transforms an emission <T> into an R emission using Function<T,R> lambda
        // already used in other examples in chapter 2 when getting String lengths
        // only allows onte-to-one conversion for each emission!

        // this example gets the date and changes format to LocalDate
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy");
                Observable.just("1/3/2016", "5/9/2016", "10/12/2016")
                        .map(s -> LocalDate.parse(s, dtf))
                        .subscribe(i -> System.out.println("RECEIVED: " + i));


    }


    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
