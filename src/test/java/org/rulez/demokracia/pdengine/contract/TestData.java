package org.rulez.demokracia.pdengine.contract;

import org.rulez.demokracia.pdengine.dataobjects.CastVoteData;
import org.rulez.demokracia.pdengine.dataobjects.VoteData;
import org.rulez.demokracia.pdengine.testhelpers.BeatTableData;

public class TestData {

  VoteData voteData;
  CastVoteData castVoteData;
  BeatTableData beatTableData;

  public TestData() {
    voteData = new VoteData();
    castVoteData = new VoteData().castVoteData;
    beatTableData = new VoteData().beatTableData;
  }

}
