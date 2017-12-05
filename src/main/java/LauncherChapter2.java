import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;


/* Working with the codes from the book Learning RxJava by Thomas Nield
    Adding my comments where i see fit for learning purposes
 */
public class LauncherChapter2 {
    public static void main(String[] args) {

        //using .create, we can pass the attributes to onNext() and finish with onComplete();
        //useful when pulling non-reactive items into the Observable
        Observable<String> source = Observable.create(emitter ->{
            emitter.onNext("Alpha");
            emitter.onNext("Beta");
            emitter.onNext("Gamma");
            emitter.onNext("Delta");
            emitter.onNext("Epsilon");
            emitter.onComplete();
        });
        source.subscribe(s -> System.out.println("RECEIVED " + s));

        //implementing error handling:

        Observable<String> newSource = Observable.create(emitter ->{
            try {
                emitter.onNext("Alpha");
                emitter.onNext("Beta");
                emitter.onNext("Gamma");
                emitter.onNext("Delta");
                emitter.onNext("Epsilon");
                emitter.onComplete();
            } catch (Throwable e) {
                emitter.onError(e);
            }
        });
        newSource.subscribe(s -> System.out.println("RECEIVED " + s),
                Throwable::printStackTrace); //prints or throws printing of the error
        //Observables can push to other operators serving as next step of the chain
        //using derivatives map() and filter() to manipulate the output data
        Observable<Integer> lengths = newSource.map(String::length);

        Observable<Integer> filtered = lengths.filter(i -> i >= 5);

        filtered.subscribe(s -> System.out.println("RECEIVED " + s));

        //we can avoid using two different variables to handle mapping and filtering
        //map and filter yield new Observables so it is possible to use one variable

        Observable<String> nextSource = Observable.create(emitter ->{
            try {
                emitter.onNext("Alpha");
                emitter.onNext("Beta");
                emitter.onNext("Gamma");
                emitter.onNext("Delta");
                emitter.onNext("Epsilon");
                emitter.onComplete();
            } catch (Throwable e) {
                emitter.onError(e);
            }
        });
        nextSource.map(String::length)
                .filter(i -> i >= 5)
                .subscribe(s -> System.out.println("RECEIVED " + s));


        //Instread of Observable.create(), we can use Observable.just() to pass all items when using Observables
        //we call onCOmplete() when all of them have been pushed

        Observable<String> reSource = Observable.just("Alpha", "Beta", "Gamma",
                "Delta", "Epsilon");

            reSource.map(String::length)
                .filter(i -> i >= 5)
                .subscribe(s -> System.out.println("RECEIVED " + s));

         //Observable.fromIterable() allows us to use values in any object that implements Iterable

        List<String> items = Arrays.asList("Alpha", "Beta", "Gamma",
                "Delta", "Epsilon");

        Observable<String> reNewSource = Observable.fromIterable(items);
        reNewSource.map(String::length)
                .filter(i -> i >= 5)
                .subscribe(s -> System.out.println("RECEIVED " + s));


        //Implementing Observer Interface methods

        //we can do any operation when receiving the items on onNext method
        //Treat the errors on onError()
        //Execute tasks on onComplete()... etc;
        Observable<String> newReSource = Observable.just("Alpha", "Beta", "Gamma",
                "Delta", "Epsilon");
        Observer<Integer> myObserver = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                //do nothing with Disposable for now
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("RECEBIDO: " + integer);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done!");
            }
        };

        newReSource.map(String::length).filter(i -> i >=5)
                .subscribe(myObserver);

        //Implementing methods as lambdas!

        Consumer<Integer> onNext = i -> System.out.println("Recebido: " + i);
        Action onComplete = () -> System.out.println("Done!");
        Consumer<Throwable> onError = Throwable::printStackTrace;

        newReSource.map(String::length).filter(i -> i >= 5)
                .subscribe(onNext, onError, onComplete);

        //another way: putting the lambdas inside the subscribe()
        //much less boilerplate code
        newReSource.map(String::length).filter(i -> i >= 5)
                .subscribe(i -> System.out.println("Rec: " + i),
                        Throwable::printStackTrace,
                        () -> System.out.println("Done!"));

        //you can omit methods in subscribe()
        //here i omit onComplete()

        newReSource.map(String::length).filter(i -> i >= 5)
                .subscribe(i -> System.out.println("Recb: " + i),
                        Throwable::printStackTrace);
        //and here i omit both onError and onComplete:
        //omitting onError should be avoided!
        newReSource.map(String::length).filter(i -> i >= 5)
                .subscribe(i -> System.out.println("Rece: " + i));

        //Multiple observers! (i already used this approach above
        //cold observables allow more than one observer to subscribe
        // in general, cold observables are the ones with sources that emit finite datasets
        //the Observable will emit all items to the first Observer than will emit all items to the next and so on

        newReSource.subscribe(s -> System.out.println("Observer 1 received: " + s));
        newReSource.subscribe(s -> System.out.println("Observer 2 received: " + s));

        //if one observer uses transformation operators, it does not affect the second one
        //first observer
        newReSource.subscribe(s -> System.out.println("Observer 1 Received: " + s));
        //second observer
        newReSource.map(String::length).filter(i -> i >= 5)
                        .subscribe(s -> System.out.println("Observer 2 Received: " +
                                s));

        //ConnectableObservable: makes all emissions to be played all at once to all the Observers
        //works with cold observables as well
        //The concept here is called Multicasting!

        ConnectableObservable<String> conSource =
                Observable.just("Alpha", "Beta", "Gamma",
                        "Delta", "Epsilon").publish();//property needed!!!
        //Setting up obeservers
        conSource.subscribe(s -> System.out.println("Observer 01 received: " + s));
        conSource.map(String::length)
                .subscribe(s -> System.out.println("Observer 02 Received: " +
                        s));
        //Firing the ConnectableObservable
        conSource.connect();

        //Observable.range() emits a consecutive range of integers
        Observable.range(1,30)//(starting value, how many emissions)
                .subscribe(System.out::println);
        //There is also a  rangeLong() operator that workis in a similar way

        //Observable.interval emits according to a set interval

        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(s -> System.out.println(s + " Mississippi"));
                sleep(6000);
        //this runs on the computation Scheduler thread.
        //we need to delay the end of the execution to see the results in main thread
        //it is a cold observable as we can see by adding a second observer to it
        Observable<Long> seconds = Observable.interval(1, TimeUnit.SECONDS);
        seconds.subscribe(s -> System.out.println("Observer 1: " + s));
        sleep(6000);
        seconds.subscribe(s -> System.out.println("Observer 2: " + s));
        sleep(6000);

        //we can use ConnectableObservable to put them working over the same emissions:

        ConnectableObservable<Long> seSeconds =
                Observable.interval(1,TimeUnit.SECONDS).publish();
        //1st observer
        seSeconds.subscribe(a -> System.out.println("Observer 1: " + a));
        seSeconds.connect();

        //sleep 6 seconds
        sleep(6000);

        //2nd observer
        seSeconds.subscribe(b -> System.out.println("Observer 2: " + b));

        //sleep 6 seconds
        sleep(6000);

        //Observable.empty() are the RxJava concept of null.
        //it just goes to onComplete()

        Observable<String> empty = Observable.empty();
        empty.subscribe(System.out::println, Throwable::printStackTrace,
                () -> System.out.println("Done!"));


    }



    public static void sleep(long millis){
        try {
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }




}
