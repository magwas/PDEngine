package org.rulez.demokracia.pdengine.contract;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.rulez.demokracia.pdengine.beattable.BeatTableServiceImpl;
import org.rulez.demokracia.pdengine.exception.ReportedException;

//@RunWith(MockitoJUnitRunner.Silent.class)
@RunWith(ContractRunner.class)
public class ContractTest {

  @InjectMocks
  BeatTableServiceImpl beatTableService;

  @Contract("a contract with return definition")
  public void contract_one(
      final TestData testData,
      final ContractInfo<BeatTableServiceImpl> contract
  ) {

    contract.returns(
        testData.beatTableData.beatTableComplex
    )
        .initializeBeatTable(testData.castVoteData.castVoteList);
  }

  @Contract("a contract with exception definition")
  public void contract2(
      final TestData testData,
      final ContractInfo<BeatTableServiceImpl> contract
  ) {

    contract.throwing(
        ReportedException.class,
        "Invalid castVotes"
    )
        .initializeBeatTable(null);

  }

}
