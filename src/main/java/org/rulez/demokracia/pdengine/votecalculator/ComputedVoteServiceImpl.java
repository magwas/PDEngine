package org.rulez.demokracia.pdengine.votecalculator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.rulez.demokracia.pdengine.beattable.BeatTable;
import org.rulez.demokracia.pdengine.beattable.BeatTableService;
import org.rulez.demokracia.pdengine.beattable.BeatTableTransitiveClosureService;
import org.rulez.demokracia.pdengine.tally.Tally;
import org.rulez.demokracia.pdengine.tally.TallyService;
import org.rulez.demokracia.pdengine.vote.Vote;
import org.rulez.demokracia.pdengine.votecast.CastVote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComputedVoteServiceImpl implements ComputedVoteService {

  @Autowired
  private VoteResultComposer voteResultComposer;

  @Autowired
  private BeatTableService beatTableService;

  @Autowired
  private BeatTableTransitiveClosureService beatTableTransitiveClosureService;

  @Autowired
  private TallyService tallyService;

  @Override
  public ComputedVote computeVote(final Vote vote) {
    final ComputedVote result = new ComputedVote(vote);

    result.setBeatTable(
        beatTableService.initializeBeatTable(vote.getVotesCast())
    );
    final BeatTable beatPathTable =
        beatTableService.normalize(result.getBeatTable());
    final BeatTable closedTable = beatTableTransitiveClosureService
        .computeTransitiveClosure(beatPathTable);
    result
        .setBeatPathTable(
            closedTable
        );
    result.setVoteResults(
        voteResultComposer.composeResult(result.getBeatPathTable())
    );

    result.setTallying(computeTallying(vote));

    return result;
  }

  private Map<String, Tally> computeTallying(final Vote vote) {
    final List<CastVote> votesCast = vote.getVotesCast();
    return vote.getCountedAssurances().stream().map(
        a -> tallyService.calculateTally(a, votesCast)
    ).collect(Collectors.toMap(Tally::getAssurance, t -> t));
  }
}
