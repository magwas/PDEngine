package org.rulez.demokracia.pdengine.vote;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.annotations.TestedBehaviour;
import org.rulez.demokracia.pdengine.annotations.TestedFeature;
import org.rulez.demokracia.pdengine.annotations.TestedOperation;
import org.rulez.demokracia.pdengine.dataobjects.VoteAdminInfo;
import org.rulez.demokracia.pdengine.dataobjects.VoteData;
import org.rulez.demokracia.pdengine.dataobjects.VoteParameters;
import org.rulez.demokracia.pdengine.testhelpers.ThrowableTester;

@TestedFeature("Manage votes")
@TestedOperation("set vote parameters")
@RunWith(MockitoJUnitRunner.Silent.class)
public class VoteSetParametersTest extends ThrowableTester
    implements VoteRepositoryContract, AdminKeyCheckerServiceContract {

  private static final String VALIDATES_INPUTS = "validates inputs";

  private static final String SETS_THE_PARAMETERS_OF_THE_VOTE =
      "sets the parameters of the vote";

  private static final String VOTE_INVARIANTS = "vote invariants";

  @InjectMocks
  private VoteServiceImpl voteService;

  @Mock
  private VoteRepository voteRepository;

  @Mock
  private AdminKeyCheckerService adminKeyCheckerService;

  private String originVoteId;
  private String originAdminKey;
  private List<String> originNeededAssurances;
  private List<String> originCountedAssurances;
  private Boolean originIsPrivate;
  private Boolean originCanUpdate;
  private long originCreationTime;
  private VoteParameters voteParameters;

  private VoteData voteData;

  @Before
  public void setUp() {
    voteData = new VoteData();
    contract(voteRepository, voteData);
    contract(adminKeyCheckerService, voteData);
    saveOriginalValues();
    setVoteParameters();

    voteService
        .setVoteParameters(
            voteData.adminInfoWithNoNeededAssurances,
            voteParameters
        );
  }

  private void setVoteParameters() {
    voteParameters = new VoteParameters();
    voteParameters.setMinEndorsements(0);
    voteParameters.setAddinable(true);
    voteParameters.setEndorsable(true);
    voteParameters.setVotable(true);
    voteParameters.setViewable(true);
  }

  private void saveOriginalValues() {
    originVoteId = voteData.voteWithNoNeededAssurances.getId();
    originAdminKey = voteData.voteWithNoNeededAssurances.getAdminKey();
    originNeededAssurances = new ArrayList<>(
        voteData.voteWithNoNeededAssurances.getNeededAssurances()
    );
    originCountedAssurances = new ArrayList<>(
        voteData.voteWithNoNeededAssurances.getCountedAssurances()
    );
    originIsPrivate = voteData.voteWithNoNeededAssurances.isPrivate();
    originCreationTime = voteData.voteWithNoNeededAssurances.getCreationTime();
    originCanUpdate =
        voteData.voteWithNoNeededAssurances.getParameters().isUpdatable();
  }

  @TestedBehaviour(VALIDATES_INPUTS)
  @Test
  public void invalid_voteId_is_rejected() {
    assertThrows(() -> {
      voteService.setVoteParameters(
          voteData.adminInfoWithInvalidVoteId,
          voteParameters
      );
    }).assertMessageIs(VoteService.ILLEGAL_VOTE_ID);
  }

  @TestedBehaviour(VALIDATES_INPUTS)
  @Test
  public void invalid_adminKey_is_rejected() {
    assertThrows(
        () -> voteService
            .setVoteParameters(
                voteData.adminInfoWithInvalidAdminKey, voteParameters
            )
    )
        .assertMessageIs(ILLEGAL_KEY);
  }

  @TestedBehaviour(VALIDATES_INPUTS)
  @Test
  public void invalid_minEndorsements_is_rejected() {
    final int invalidMinEndorsements = -2;
    voteParameters.setMinEndorsements(invalidMinEndorsements);
    assertThrows(
        () -> voteService
            .setVoteParameters(
                new VoteAdminInfo(
                    voteData.voteWithNoNeededAssurances.getId(),
                    voteData.voteWithNoNeededAssurances.getAdminKey()
                ), voteParameters
            )
    )
        .assertMessageIs("Illegal minEndorsements");
  }

  @TestedBehaviour(SETS_THE_PARAMETERS_OF_THE_VOTE)
  @Test
  public void
      setVoteParameters_sets_the_minEndorsement_parameter_of_the_vote() {
    assertEquals(
        voteParameters.getMinEndorsements(),
        voteData.voteWithNoNeededAssurances.getParameters().getMinEndorsements()
    );
  }

  @TestedBehaviour(SETS_THE_PARAMETERS_OF_THE_VOTE)
  @Test
  public void setVoteParameters_sets_the_canAddIn_parameter_of_the_vote() {
    assertEquals(
        true, voteData.voteWithNoNeededAssurances.getParameters().isAddinable()
    );
  }

  @TestedBehaviour(SETS_THE_PARAMETERS_OF_THE_VOTE)
  @Test
  public void setVoteParameters_sets_the_canEndorse_parameter_of_the_vote() {
    assertEquals(
        true, voteData.voteWithNoNeededAssurances.getParameters().isEndorsable()
    );
  }

  @TestedBehaviour(SETS_THE_PARAMETERS_OF_THE_VOTE)
  @Test
  public void setVoteParameters_sets_the_canVote_parameter_of_the_vote() {
    assertEquals(
        true, voteData.voteWithNoNeededAssurances.getParameters().isVotable()
    );
  }

  @TestedBehaviour(SETS_THE_PARAMETERS_OF_THE_VOTE)
  @Test
  public void setVoteParameters_sets_the_canView_parameter_of_the_vote() {
    assertEquals(
        true, voteData.voteWithNoNeededAssurances.getParameters().isViewable()
    );
  }

  @TestedBehaviour(VOTE_INVARIANTS)
  @Test
  public void setVoteParameters_does_not_overwrite_vote_id_value() {
    assertEquals(originVoteId, voteData.voteWithNoNeededAssurances.getId());
  }

  @TestedBehaviour(VOTE_INVARIANTS)
  @Test
  public void setVoteParameters_does_not_overwrite_admin_key_value() {
    assertEquals(
        originAdminKey, voteData.voteWithNoNeededAssurances.getAdminKey()
    );
  }

  @TestedBehaviour(VOTE_INVARIANTS)
  @Test
  public void setVoteParameters_does_not_overwrite_neededAssurances_value() {
    assertEquals(
        originNeededAssurances,
        voteData.voteWithNoNeededAssurances.getNeededAssurances()
    );
  }

  @TestedBehaviour(VOTE_INVARIANTS)
  @Test
  public void setVoteParameters_does_not_overwrite_countedAssurances_value() {
    assertEquals(
        originCountedAssurances,
        voteData.voteWithNoNeededAssurances.getCountedAssurances()
    );
  }

  @TestedBehaviour(VOTE_INVARIANTS)
  @Test
  public void setVoteParameters_does_not_overwrite_isPrivate_value() {
    assertEquals(
        originIsPrivate, voteData.voteWithNoNeededAssurances.isPrivate()
    );
  }

  @TestedBehaviour(VOTE_INVARIANTS)
  @Test
  public void setVoteParameters_does_not_overwrite_creationTime_value() {
    assertEquals(
        originCreationTime,
        voteData.voteWithNoNeededAssurances.getCreationTime()
    );
  }

  @TestedBehaviour("updatable is a vote invariant")
  @Test
  public void setVoteParameters_does_not_overwrite_canUpdate_value() {
    assertEquals(
        originCanUpdate,
        voteData.voteWithNoNeededAssurances.getParameters().isUpdatable()
    );
  }
}
