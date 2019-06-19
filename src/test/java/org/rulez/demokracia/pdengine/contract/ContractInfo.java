package org.rulez.demokracia.pdengine.contract;

import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
class ContractInfo<ServiceClass> {

  ServiceClass service;
  Object stub;
  Object returnValue;
  public Class<? extends RuntimeException> exceptionClass;
  public String exceptionMessage;
  @Setter
  private String name;
  public String definingFunction;

  ContractInfo(final ServiceClass service) {
    this.service = service;
  }

  @SuppressWarnings("unchecked")
  public ServiceClass returns(final Object returnValue) {
    this.returnValue = returnValue;
    stub = doReturn(returnValue).when(mock(service.getClass()));
    return (ServiceClass) stub;
  }

  @SuppressWarnings("unchecked")
  public ServiceClass throwing(
      final Class<? extends RuntimeException> exceptionClass,
      final String exceptionMessage
  ) {
    this.exceptionClass = exceptionClass;
    this.exceptionMessage = exceptionMessage;
    try {
      stub =
          doThrow(exceptionClass.getConstructor(String.class)
              .newInstance(exceptionMessage)
          )
              .when(mock(service.getClass()));
    } catch (
        InstantiationException | IllegalAccessException |
        IllegalArgumentException | InvocationTargetException |
        NoSuchMethodException | SecurityException e
    ) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return (ServiceClass) stub;
  }

}
