package org.ejavdge.domain.problem;

import junit.framework.TestCase;
import org.ejavdge.domain.Fixture;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class ProbExamplesTest extends TestCase {
    private final ProbBrief withoutExamples = new ProbBrief(
        new Text.Of(
            """
            Submit a solution for A-Sum 1
            Задача A
            На стандартном потоке ввода задаются два целых числа, не меньшие
            -32000 и не большие 32000.
            На стандартный поток вывода напечатайте сумму этих чисел.
            
            google
            attachment
            
            Числа задаются по одному в строке. Пробельные символы перед числом и после
            него отсутствуют. Пустые строки в вводе отсутствуют.
            """
        )
    );
    private final ProbBrief withSingleExample = new ProbBrief(
        new Text.Of(
            """
            Submit a solution for A-Sum 1
            Задача A
            На стандартном потоке ввода задаются два целых числа, не меньшие
            -32000 и не большие 32000.
            На стандартный поток вывода напечатайте сумму этих чисел.
            
            google
            attachment
            
            Числа задаются по одному в строке. Пробельные символы перед числом и после
            него отсутствуют. Пустые строки в вводе отсутствуют.
            
            Examples
            Input
            1
            2
            
            Output
            3
            """
        )
    );
    private final ProbBrief withSeveralExamples = new ProbBrief(
        new Text.Of(
            """
            Submit a solution for A-Sum 1
            Задача A
            На стандартном потоке ввода задаются два целых числа, не меньшие
            -32000 и не большие 32000.
            На стандартный поток вывода напечатайте сумму этих чисел.
    
            google
            attachment
    
            Числа задаются по одному в строке. Пробельные символы перед числом и после
            него отсутствуют. Пустые строки в вводе отсутствуют.
    
            Examples
            Input
            1
            2
    
            Output
            3
    
            Input
            4
            5
    
            Output
            9"""
        )
    );

    public void testInputOfSingleExample() {
        assertEquals(
            List.of("1\n2"),
            new ProbExamples(this.withSingleExample)
                .contents()
                .stream()
                .map(Fixture::input)
                .toList()
        );
    }

    public void testExpectationOfSingleExample() {
        assertEquals(
            List.of("3"),
            new ProbExamples(this.withSingleExample)
                .contents()
                .stream()
                .map(Fixture::expected)
                .toList()
        );
    }

    public void testInputOfSeveralExamples() {
        assertEquals(
            List.of("1\n2", "4\n5"),
            new ProbExamples(this.withSeveralExamples)
                .contents()
                .stream()
                .map(Fixture::input)
                .toList()
        );
    }

    public void testExpectationOfSeveralExamples() {
        assertEquals(
            List.of("3", "9"),
            new ProbExamples(this.withSeveralExamples)
                .contents()
                .stream()
                .map(Fixture::expected)
                .toList()
        );
    }

    public void testInputOfBriefWithoutExamples() {
        assertEquals(
            List.of(),
            new ProbExamples(this.withoutExamples)
                .contents()
                .stream()
                .map(Fixture::input)
                .toList()
        );
    }

    public void testExpectationOfBriefWithoutExamples() {
        assertEquals(
            List.of(),
            new ProbExamples(this.withoutExamples)
                .contents()
                .stream()
                .map(Fixture::expected)
                .toList()
        );
    }

    public void testWithoutMaterializationOfFixtures() {
        final var calls = new AtomicInteger(0);
        new ProbExamples(
            new ProbBrief(
                () -> {
                    calls.incrementAndGet();
                    return this.withSeveralExamples.content();
                }
            )
        ).contents();
        assertEquals(1, calls.get());
    }

    public void testEmptyInput() {
        assertEquals(
            List.of(""),
            new ProbExamples(
                new ProbBrief(
                    new Text.Of(
                        """
                        Submit a solution for A-Sum 1
                        Задача A
                        На стандартном потоке ввода задаются два целых числа, не меньшие
                        -32000 и не большие 32000.
                        На стандартный поток вывода напечатайте сумму этих чисел.
                        
                        google
                        attachment
                        
                        Числа задаются по одному в строке. Пробельные символы перед числом и после
                        него отсутствуют. Пустые строки в вводе отсутствуют.
                        
                        Examples
                        Input
                        
                        Output
                        3
                        """
                    )
                )
            ).contents()
                .stream()
                .map(Fixture::input)
                .toList()
        );
    }

    public void testEmptyExpectation() {
        assertEquals(
            List.of(""),
            new ProbExamples(
                new ProbBrief(
                    new Text.Of(
                        """
                        Submit a solution for A-Sum 1
                        Задача A
                        На стандартном потоке ввода задаются два целых числа, не меньшие
                        -32000 и не большие 32000.
                        На стандартный поток вывода напечатайте сумму этих чисел.
                        
                        google
                        attachment
                        
                        Числа задаются по одному в строке. Пробельные символы перед числом и после
                        него отсутствуют. Пустые строки в вводе отсутствуют.
                        
                        Examples
                        Input
                        1
                        2
                        
                        Output
                        
                        """
                    )
                )
            ).contents()
                .stream()
                .map(Fixture::expected)
                .toList()
        );
    }

    public void testWithMaterializationOfEveryFixture() {
        final var calls = new AtomicInteger(0);
        new ProbExamples(
            new ProbBrief(
                () -> {
                    calls.incrementAndGet();
                    return this.withSeveralExamples.content();
                }
            )
        ).contents()
            .stream()
            .map(f -> f.input() + f.expected()).forEach(ignored -> {});
        assertEquals(1, calls.get());
    }

    public void testBrokenBrief() {
        try {
            new ProbExamples(
                new ProbBrief(
                    () -> {
                        throw new InvariantViolation("There is no text.");
                    }
                )
            ).contents();
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }
}
