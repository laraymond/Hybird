package util;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class EvenLines<T> extends Spliterators.AbstractSpliterator<T> implements Consumer<T> {

    public static <T> Stream evenLines(Stream<T> source) {
        return StreamSupport.stream(new EvenLines(source.spliterator()), false);
    }

    private static long even(long l) {
        return l == Long.MAX_VALUE ? l : (l + 1) / 2;
    }

    Spliterator<T> originalLines;

    EvenLines(Spliterator<T> source) {
        super(even(source.estimateSize()), source.characteristics());
        originalLines = source;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (originalLines == null || !originalLines.tryAdvance(action))
            return false;
        if (!originalLines.tryAdvance(this)) originalLines = null;
        return true;
    }

    @Override
    public void accept(T t) {
    }
}