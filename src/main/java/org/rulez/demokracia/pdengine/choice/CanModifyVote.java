package org.rulez.demokracia.pdengine.choice;

import org.rulez.demokracia.pdengine.vote.Vote;

public interface CanModifyVote {

  default void checkIfVoteIsAddinable(
      final Vote vote, final RuntimeException exception
  ) {
    if (!vote.getParameters().isAddinable())
      throw exception;
  }

  default void checkIfUserIsTheSame(
      final Choice votesChoice, final String userName,
      final RuntimeException exception
  ) {
    if (!votesChoice.getUserName().equals(userName))
      throw exception;
  }
}
