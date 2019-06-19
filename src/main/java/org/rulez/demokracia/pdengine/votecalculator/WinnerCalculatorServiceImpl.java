package org.rulez.demokracia.pdengine.votecalculator;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.rulez.demokracia.pdengine.beattable.BeatTable;
import org.rulez.demokracia.pdengine.beattable.BeatTableIgnoreService;
import org.rulez.demokracia.pdengine.beattable.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WinnerCalculatorServiceImpl implements WinnerCalculatorService {

  @Autowired
  private BeatTableIgnoreService beatTableIgnore;
  private final static Pair nonbeatingPair = new Pair(0, 0);

  @Override
  public List<String> calculateWinners(
      final BeatTable beatTable,
      final Collection<String> ignoredChoices
  ) {
    final BeatTable ignoredBeatTable =
        beatTableIgnore.ignoreChoices(beatTable, ignoredChoices);
    final List<String> result = ignoredBeatTable.getKeyCollection().stream()
        .filter(choice -> isWinner(choice, ignoredBeatTable)).collect(Collectors.toList());
    return result;
  }

  private boolean isWinner(final String choice, final BeatTable beatTable) {
    return beatTable.getKeyCollection().stream()
        .allMatch(
            otherChoice -> nonbeatingPair
                .equals(beatTable.getElement(choice, otherChoice))
        );
  }

}
