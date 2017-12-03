import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import java.util.Arrays;
import java.util.List;



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
        newReSource.map(String::length).filter(i -> i >= 5)
                .subscribe(i -> System.out.println("Rec: " + i),
                        Throwable::printStackTrace,
                        () -> System.out.println("Done!"));

    }





}
