/*
 * ArraySumTest.java
 * JUnit based test
 *
 * Created on December 9, 2006, 5:48 PM
 */

package tamp.ch14.steal;

import junit.framework.*;

import java.util.concurrent.ExecutionException;

/**
 *
 * @author mph
 */
public class ArraySumTest extends TestCase {
  
  public ArraySumTest(String testName) {
    super(testName);
  }
  
  public void testRun() throws InterruptedException, ExecutionException {
    int[] a = {1, 2, 3};
    int[] b = {1, 2, 3, 4};
    int sum = ArraySum.sum(a);
    assertEquals(6, sum);
    sum = ArraySum.sum(b);
    assertEquals(10, sum);
    
  }
  
}
