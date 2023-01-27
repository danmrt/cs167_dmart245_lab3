package edu.ucr.cs.cs167.dmart245;
import java.io.IOException;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import java.util.Arrays;
import java.util.function.Function;
public class App {
    public static void printEvenNumbers(int from, int to) {
        System.out.printf("Printing numbers in the range [%d,%d]\n", from, to);
        for (int i = from; i <= to; i++) {
            if (i % 2 == 0) {
                System.out.println(i);
            }
        }
    }

    public static void printNumbersDivisibleByThree(int from, int to) {
        System.out.printf("Printing numbers divisible by three in the range [%d,%d]\n", from, to);
        for (int i = from; i <= to; i++) {
            if (i % 3 == 0) {
                System.out.println(i);
            }
        }
    }

    public static void printNumbers(int from, int to, Function<Integer, Boolean> filter) {
        System.out.printf("Printing numbers in the range [%d,%d]\n", from, to);
        for (int i = from; i <= to; i++) {
            if (filter.apply(i) == true) {
                System.out.println(i);
            }
        }
    }

    static class IsEven implements Function<Integer, Boolean> {
        @Override
        public Boolean apply(Integer x) {
            return x % 2 == 0;
        }
    }

    static class IsDivisibleByThree implements Function<Integer, Boolean> {
        @Override
        public Boolean apply(Integer x) {
            return x % 3 == 0;
        }
    }

    public static Function<Integer, Boolean> combineWithAnd(Function<Integer, Boolean> ... filters) {
        Function<Integer, Boolean> bruh = new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(Integer x) {
                return Arrays.stream(filters).allMatch(i->i.apply(x));
            }
        };
        return bruh;
    }

    public static Function<Integer, Boolean> combineWithOr(Function<Integer, Boolean> ... filters) {
        Function<Integer, Boolean> xd = new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(Integer x) {
                return Arrays.stream(filters).anyMatch(i->i.apply(x));
            }
        };
        return xd;
    }


    public static void main ( String[] args ) throws Exception {
        if (args.length != 3) {
            System.err.println("Error: At least three parameters expected, from, to, and base.");
            System.exit(-1);
        }

        int from = Integer.parseInt(args[0]);
        int to = Integer.parseInt(args[1]);
        Boolean CheckAnd = false; //this checks whether the third parameter has a , or not
        Boolean CheckOr  = false; //this checks whether the third parameter has a v or not
        String parameter = args[2];

        if (parameter.contains("v")) {
            CheckOr = true;
        }
        else if (parameter.contains(",")) {
            CheckAnd = true;
        }

        String bases[] = parameter.split("(,)|(v)");

        Function<Integer, Boolean>[] filters = new Function[bases.length];
        for (int i = 0; i < filters.length; i++) {
            int v = Integer.valueOf(bases[i]);
            filters[i] = new Function<Integer,Boolean>() {
                @Override
                public Boolean apply(Integer x) {
                    return x % v == 0;
                }
            };
        }

        //Function<Integer, Boolean> divisibleByFive = new Function<Integer, Boolean>() {
        //    @Override
        //    public Boolean apply(Integer x) {
        //        return x % 5 == 0;
        //    }
        //};
        //Function<Integer, Boolean> divisibleByTen = x -> x % 10 == 0;


        Function<Integer,Boolean> filter;

        /*if (base == 2) {
            filter = new IsEven();
        }
        else if (base == 3){
            filter = new IsDivisibleByThree();
        }
        else if (base == 5) {
            filter = divisibleByFive;
        }
        else {
            filter = divisibleByTen;
        }*/
        //filter = divisibleByBase;
        //printNumbers(from,to, filter);

        if (CheckAnd) {
            filter = combineWithAnd(filters);
        }
        else if (CheckOr) {
            filter = combineWithOr(filters);
        }
        else {
            filter = new Function<Integer, Boolean>() {
                    @Override
                    public Boolean apply(Integer x) {
                        return x % Integer.parseInt(args[2]) == 0;
                    }
            };
        }


        printNumbers(from,to,filter);


    }//end of main
}
