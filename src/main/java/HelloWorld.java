import io.reactivex.*;


import java.util.ArrayList;
import java.util.List;


public class HelloWorld {
    public static void main(String[] args) {
        Flowable.just("Hello world").subscribe(System.out::println);


        List<String> names = new ArrayList<>();
        names.add("Dante");
        names.add("Delano");
        names.add("Derek");
        names.add("Dimitri");

        Observable<String> stringObservable = Observable.fromIterable(names);

        stringObservable.subscribe(System.out::println);

    }




}