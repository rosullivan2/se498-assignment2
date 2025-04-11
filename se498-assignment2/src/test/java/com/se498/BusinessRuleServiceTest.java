package com.se498;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BusinessRuleServiceTest {

    // // Test Lifecycle demonstration
    // private static BusinessRuleService globalService;
    // private BusinessRuleService testService;

    @Mock
    private BusinessRule mockRule;

    // @Spy
    // private AgeRestrictionBusinessRule spyRule;

    //Implement Fixture Pattern
    static class TestRuleBuilder {
        private boolean returnValue = false;
        private boolean throwsException = false;

        public TestRuleBuilder withReturnValue(boolean returnValue) {
            this.returnValue = returnValue;
            return this;
        }

        public TestRuleBuilder thatThrowsException(boolean throwsException) {
            this.throwsException = throwsException;
            return this;
        }

        public BusinessRule build() {
            return new BusinessRule() {
                @Override
                public boolean apply(Object objectToCheck) throws Exception {
                    if (throwsException) {
                        throw new Exception("Simulated exception from test rule");
                    }
                    return returnValue;
                }
            };
        }
    }

    //Implement Container Pattern
    static class RuleTestContainer implements AutoCloseable {
        private final BusinessRule rule;
        private final BusinessRuleService service;
        private final Object testInput;

        public RuleTestContainer(BusinessRule rule, Object testInput) {
            this.rule = rule;
            this.service = new BusinessRuleService(); // or pass it in if needed
            this.testInput = testInput;
        }

        public boolean runTest() {
            return service.applyBusinessRule(rule, testInput);
        }

        @Override
        public void close() {
            // Cleanup would go here in a real container
        }
    }

    //Implement Lifecycle Methods
    @BeforeAll
    static void initAll() {
        System.out.println("[BeforeAll] — Runs once before all tests.");
    }

    @BeforeEach
    void init() {
        System.out.println("[BeforeEach] — Runs before each individual test.");
    }

    @AfterEach
    void tearDown() {
        System.out.println("[AfterEach] — Runs after each individual test.");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("[AfterAll] — Runs once after all tests.");
    }


    //Implement Basic Assertions
    @Test
    @DisplayName("Basic test for applyBusinessRule")
    void testApplyBusinessRule() {
        BusinessRuleService service = new BusinessRuleService();
        Participant participant = new Participant("02/20/2000", "US"); 

        boolean result = service.applyBusinessRule(new AgeRestrictionBusinessRule(), participant);
        assertTrue(result);
    }

    //Implement Exception Testing
    @Test
    @DisplayName("Test exception handling in applyBusinessRule")
    void testApplyBusinessRuleException() {
        BusinessRuleService service = new BusinessRuleService();

        BusinessRule throwingRule = new BusinessRule() {
            @Override
            public boolean apply(Object objectToCheck) throws Exception {
                throw new RuntimeException("Simulated failure.");
            }
        };

        Participant participant = new Participant("01/23/2004", "EU");

        boolean result = service.applyBusinessRule(throwingRule, participant);

        assertFalse(result);

    }

    // Implement Conditional Test
    // @Test
    // @EnabledOnOs(OS.OTHER)
    // @DisplayName("Test that only runs on Windows")
    // void testOperatingSystemOnly() {
    // }

    //Implement Assumption Test
    @Test
    @DisplayName("Test with assumptions")
    void testWithAssumption() {
        Participant p = new Participant("08/01/2000","US");

        assumeTrue("US".equals(p.getLocale()));

        BusinessRule rule = new AgeRestrictionBusinessRule();
        BusinessRuleService service = new BusinessRuleService();

        boolean result = service.applyBusinessRule(rule, p);

        assertTrue(result);

    }

    //Implement Nested Test
    @Nested
    @DisplayName("Tests for AgeRestrictionBusinessRule")
    class AgeRestrictionTests {

        @Test
        @DisplayName("Test valid age")
        void testValidAge() {
            BusinessRuleService service = new BusinessRuleService();
            Participant participant = new Participant("03/20/2002", "US");

            boolean result = service.applyBusinessRule(new AgeRestrictionBusinessRule(), participant);
            
            assertTrue(result);
        }

        @Test
        @DisplayName("Test invalid age")
        void testInvalidAge() {
            BusinessRuleService service = new BusinessRuleService();
            Participant participant = new Participant("20/03/2015", "EU");

            boolean result = service.applyBusinessRule(new AgeRestrictionBusinessRule(), participant);
            
            assertFalse(result);
        }
    }

    //Implement Dynamic Test
    @TestFactory
    @DisplayName("Dynamic tests for age restriction")
    Stream<DynamicTest> dynamicAgeTests() {
        BusinessRule ageRule = new AgeRestrictionBusinessRule();
        BusinessRuleService service = new BusinessRuleService();
    
        // Each entry: {age, expectedResult}
        List<int[]> testCases = Arrays.asList(
            new int[]{10, 0},   // too young
            new int[]{17, 0},
            new int[]{18, 1},   // exactly 18
            new int[]{25, 1}    // adult
        );
    
        return testCases.stream().map(tc -> {
            int age = tc[0];
            boolean expected = tc[1] == 1;
    
            // Generate DOB from age
            LocalDate dob = LocalDate.now().minusYears(age);
            String dobStr = dob.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    
            Participant participant = new Participant(dobStr, "US");
    
            return DynamicTest.dynamicTest("Age " + age + " → expected " + expected, () -> {
                boolean result = service.applyBusinessRule(ageRule, participant);
                assertEquals(expected, result);
            });
        });
    }

    //Implement Parametrized Test
    @ParameterizedTest
    @ValueSource(ints = {18, 20, 25})
    @DisplayName("Test valid ages with parameterized test")
    void testValidAgeParameterized(int age) {
        // Generate DOB from age
        LocalDate dob = LocalDate.now().minusYears(age);
        String dobStr = dob.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        Participant participant = new Participant(dobStr, "US");

        BusinessRule rule = new AgeRestrictionBusinessRule();
        BusinessRuleService service = new BusinessRuleService();

        boolean result = service.applyBusinessRule(rule, participant);

        assertTrue(result, "Expected age " + age + " to pass the rule");
    }

    @ParameterizedTest
    @CsvSource({
        "1,false",   // Underage
        "18,true",   // Exactly 18
        "25,true"    // Adult
    })
    @DisplayName("Test ages with expected results")
    void testAgeWithExpectedResult(int age, boolean expected) {
        LocalDate today = LocalDate.now();
        LocalDate dob = today.minusYears(age);
        String dobStr = dob.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        Participant participant = new Participant(dobStr, "US");

        BusinessRule rule = new AgeRestrictionBusinessRule();
        BusinessRuleService service = new BusinessRuleService();

        boolean result = service.applyBusinessRule(rule, participant);

        assertEquals(expected, result);
    }

    //Implement Mock Test
    @Test
    @DisplayName("Test with mock rule")
    void testWithMockRule() throws Exception {
        // Configure the mock
        when(mockRule.apply(any())).thenReturn(true);

        // Use the mock
        BusinessRuleService service = new BusinessRuleService();
        Participant p = new Participant("01/01/2000", "EU");

        // Verify the mock was called
        boolean result = service.applyBusinessRule(mockRule, p);
        assertTrue(result);
        verify(mockRule).apply(p);
    }

    //Implement Spy Test
    @Test
    @DisplayName("Test with spy rule")
    void testWithSpyRule() throws Exception {
        BusinessRuleService service = new BusinessRuleService();
        Participant participant = new Participant("06/25/2003", "US");

        BusinessRule rule = new AgeRestrictionBusinessRule(); 

        boolean result = service.applyBusinessRule(rule, participant);

        assertTrue(result);
        
}

    //Implement Repeated Test
    @RepeatedTest(5)
    @DisplayName("Repeated test")
    void repeatedTest() {
        BusinessRuleService service = new BusinessRuleService();
        Participant participant = new Participant("10/10/2005", "EU");
        BusinessRule rule = new AgeRestrictionBusinessRule();

        boolean result = service.applyBusinessRule(rule, participant);

        assertTrue(result);

    }

    //Implement Builder Pattern
    @Test
    @DisplayName("Test with builder pattern")
    void testWithBuilder() {
        BusinessRuleService service = new BusinessRuleService();
        Participant participant = new Participant("12/05/2003", "US");

        BusinessRule passingRule = new TestRuleBuilder().withReturnValue(true).build();

        BusinessRule throwingRule = new TestRuleBuilder().thatThrowsException(true).build();

        boolean passResult = service.applyBusinessRule(passingRule, participant);
        boolean exceptionResult = service.applyBusinessRule(throwingRule, participant);

        assertTrue(passResult);
        assertFalse(exceptionResult);

    }

    //Implement Container Test
    @Test
    @DisplayName("Test with container pattern")
    void testWithContainer() {
        BusinessRule rule = new AgeRestrictionBusinessRule();
        Participant participant = new Participant("04/10/2000", "US"); // age >= 18
    
        try (RuleTestContainer container = new RuleTestContainer(rule, participant)) {
            boolean result = container.runTest();
            assertTrue(result);
        }
    }

    //Implement Fixture Pattern
    @Test
    @DisplayName("Test with fixture pattern")
    void testWithFixture() {
        // Create a test fixture
        List<Integer> validAges = Arrays.asList(18, 20, 25);     // should pass
        List<Integer> invalidAges = Arrays.asList(1, 10, 17);    // should fail
    
        BusinessRule ageRule = new AgeRestrictionBusinessRule();
        BusinessRuleService service = new BusinessRuleService();
    
        // Test with the fixture
        for (Integer age : validAges) {
            LocalDate dob = LocalDate.now().minusYears(age);
            String dobStr = dob.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    
            Participant participant = new Participant(dobStr, "US");
            boolean result = service.applyBusinessRule(ageRule, participant);
            assertTrue(result, "Expected age " + age + " to pass the rule");
        }
    
        for (Integer age : invalidAges) {
            LocalDate dob = LocalDate.now().minusYears(age);
            String dobStr = dob.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    
            Participant participant = new Participant(dobStr, "US");
            boolean result = service.applyBusinessRule(ageRule, participant);
            assertFalse(result, "Expected age " + age + " to fail the rule");
        }
    }

    //Implement Exception Testing
    @Test
    @DisplayName("Test AgeRestrictionException creation and properties")
    void testExceptionProperties() {
        AgeRestrictionException example = new AgeRestrictionException("US", "User is under 18.");
        assertEquals("US", example.getLocale());
        assertEquals("User is under 18.", example.getReason());

    }

    //Implement Exception Testing
    @Test
    @DisplayName("Test BusinessRuleService handling AgeRestrictionException")
    void testExceptionHandling() {
        BusinessRuleService service = new BusinessRuleService();
    
        BusinessRule throwingRule = new BusinessRule() {
            @Override
            public boolean apply(Object objectToCheck) throws Exception {
                throw new AgeRestrictionException("US", "User is under 18");
            }
        };
    
        Participant participant = new Participant("01/01/2010", "US");
    
        boolean result = service.applyBusinessRule(throwingRule, participant);
    
        // The service should catch the exception and return false
        assertFalse(result);
    }
}
