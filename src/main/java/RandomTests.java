import io.reactivex.*;

public class RandomTests {
    public static void main(String[] args) {

        int[][] grid = new int[6][10];
        for (int row=0; row < grid.length; row++)
        {
            for (int col=0; col < grid[row].length; col++)
            {
                grid[row][col] = Integer.valueOf(String.valueOf(row)+String.valueOf(col));
                System.out.println("linha " + Integer.toString(row) +
                        " Coluna "+ Integer.toString(col) +
                        " valor " + Integer.toString(grid[row][col]));

            }
        }

        Observable<Integer> value = Observable.defer(() -> Observable.range(1, 100));

        value.subscribe(i -> System.out.println("Valor: " + i));

        Observable<Integer> value2 = Observable.defer(() -> Observable.range(1, 100));

        value2.map(RandomTests::mystery)
                .subscribe(i -> System.out.println("Valor: " + i));




    }

    private static int mystery(int x) {

        return x+2;




    }


}