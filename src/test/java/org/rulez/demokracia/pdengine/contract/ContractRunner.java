package org.rulez.demokracia.pdengine.contract;

import static org.mockito.Mockito.mockingDetails;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Stubbing;
import org.rulez.demokracia.pdengine.beattable.BeatTableServiceImpl;

public class ContractRunner extends Runner {

  @SuppressWarnings("rawtypes")
  final List contracts = new ArrayList<>();
  private final Class<? extends Object> testClass;

  @SuppressWarnings("unchecked")
  public ContractRunner(final Class<? extends Object> testClass)
      throws IllegalAccessException,
      IllegalArgumentException, InvocationTargetException,
      InstantiationException, NoSuchMethodException, SecurityException {
    this.testClass = testClass;
    final TestData testData = new TestData();
    final Object instance = testClass.getConstructor().newInstance();
    for (final Method method : testClass.getMethods()) {
      List<Annotation> annotations;
      annotations =
          Arrays.asList(method.getDeclaredAnnotationsByType(Contract.class));
      if (!annotations.isEmpty()) {
        @SuppressWarnings("rawtypes")
        final ContractInfo<BeatTableServiceImpl> contract =
            new ContractInfo(new BeatTableServiceImpl());
        contract.setName(((Contract) annotations.get(0)).value());
        contract.definingFunction = method.getName();
        method.invoke(instance, testData, contract);
        contracts.add(contract);
      }
    }

  }

  @Override
  public Description getDescription() {
    return Description.createSuiteDescription(testClass);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void run(final RunNotifier notifier) {
    for (final ContractInfo<?> contract : (List<ContractInfo<?>>) contracts)
      testContract(contract, notifier);
  }

  private void testContract(
      final ContractInfo<?> contract, final RunNotifier notifier
  ) {
    System.out.println("running " + contract);
    System.out.println("class=" + testClass);
    final Description description =
        Description.createTestDescription(testClass, contract.definingFunction);

    if (contract.returnValue != null)
      testReturningContract(contract, notifier, description);
    else
      testThrowingContract(contract, notifier, description);
  }

  private void testThrowingContract(
      final ContractInfo<?> contract2, final RunNotifier notifier,
      final Description description
  ) {
    final Stubbing throwingStubbing =
        mockingDetails(contract2.stub).getStubbings().iterator().next();
    final Invocation throwingInvocation = throwingStubbing.getInvocation();

    try {
      throwingInvocation.callRealMethod();
      notifier.fireTestAssumptionFailed(
          new Failure(
              description, new AssertionError("We expected an exception here")
          )
      );
    } catch (final Throwable thrown) {
      if (
        thrown.getClass().equals(contract2.exceptionClass) &&
            thrown.getMessage().equals(contract2.exceptionMessage)
      )
        notifier.fireTestFinished(description);
      else
        notifier.fireTestAssumptionFailed(new Failure(description, thrown));
    }
  }

  private void testReturningContract(
      final ContractInfo<?> contract, final RunNotifier notifier,
      final Description description
  ) {
    final Stubbing stubbing =
        mockingDetails(contract.stub).getStubbings().iterator().next();
    final Invocation invocation = stubbing.getInvocation();
    final Object answer;
    try {
      answer = stubbing.answer(invocation);
    } catch (final Throwable thrown) {
      notifier.fireTestFailure(new Failure(description, thrown));
      return;
    }

    if (!contract.returnValue.equals(answer))
      notifier
          .fireTestAssumptionFailed(
              new Failure(
                  description,
                  new AssertionError(
                      "return value differs from the expected answer"
                  )
              )
          );
    try {
      invocation.callRealMethod();
    } catch (final Throwable thrown) {
      notifier.fireTestFailure(new Failure(description, thrown));
      return;
    }
    notifier.fireTestFinished(description);
  }

}
