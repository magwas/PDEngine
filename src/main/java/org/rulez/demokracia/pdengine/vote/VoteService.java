package org.rulez.demokracia.pdengine.vote;

import org.rulez.demokracia.pdengine.dataobjects.VoteAdminInfo;
import org.rulez.demokracia.pdengine.dataobjects.VoteParameters;
import org.springframework.stereotype.Service;

@Service
public interface VoteService {

  String ILLEGAL_VOTE_ID = "illegal voteId";

  VoteAdminInfo createVote(final CreateVoteRequest createVoteRequest);

  Vote getVote(String voteId);

  void modifyVote(VoteAdminInfo adminInfo, String voteName);

  void deleteVote(VoteAdminInfo adminInfo);

  Vote getVoteWithValidatedAdminKey(VoteAdminInfo adminInfo);

  void setVoteParameters(
      final VoteAdminInfo adminInfo, final VoteParameters voteParameters
  );

  Vote saveVote(Vote vote);

  Vote getModifiableVote(VoteAdminInfo adminInfo);

}
