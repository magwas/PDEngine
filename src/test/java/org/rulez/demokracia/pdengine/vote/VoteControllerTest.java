package org.rulez.demokracia.pdengine.vote;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rulez.demokracia.pdengine.dataobjects.VoteAdminInfo;
import org.rulez.demokracia.pdengine.dataobjects.VoteData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.Silent.class)
public class VoteControllerTest implements VoteServiceContract {

  @InjectMocks
  private VoteController voteController;

  @Mock
  private VoteService voteService;

  private VoteData voteData;

  @BeforeEach
  public void setUp() {
    voteData = new VoteData();
    contract(voteService, voteData);
  }

  @Test
  public void create_vote_returns_ok() throws Exception {
    final ResponseEntity<VoteAdminInfo> responseEntity =
        voteController.createVote(new CreateVoteRequest());
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
  }

}
