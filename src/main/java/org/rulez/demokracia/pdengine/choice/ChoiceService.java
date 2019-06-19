package org.rulez.demokracia.pdengine.choice;

import org.rulez.demokracia.pdengine.dataobjects.VoteAdminInfo;

public interface ChoiceService extends CanModifyVote {

  Choice addChoice(VoteAdminInfo adminInfo, String choiceName, String user);

  Choice getChoice(String voteId, String choiceId);

  void endorseChoice(
      VoteAdminInfo adminInfo, String choiceId, String givenUserName
  );

  void deleteChoice(VoteAdminInfo adminInfo, String choiceId);

  void modifyChoice(
      VoteAdminInfo adminInfo, String choiceId, String choiceName
  );

}
