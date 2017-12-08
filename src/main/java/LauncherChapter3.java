import io.reactivex.Observable;

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






    }
}
