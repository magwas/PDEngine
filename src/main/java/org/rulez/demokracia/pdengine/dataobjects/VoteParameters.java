package org.rulez.demokracia.pdengine.dataobjects;

import javax.persistence.Entity;

import org.rulez.demokracia.pdengine.persistence.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class VoteParameters extends BaseEntity {

  public VoteParameters(
      final int minEndorsements, final boolean canAddin,
      final boolean canEndorse,
      final boolean canVote, final boolean canView, final boolean canUpdate
  ) {
    super();
    this.minEndorsements = minEndorsements;
    addinable = canAddin;
    endorsable = canEndorse;
    votable = canVote;
    viewable = canView;
    updatable = canUpdate;
  }

  public VoteParameters() {
    super();
  }

  private static final long serialVersionUID = 1L;

  private int minEndorsements;
  private boolean addinable;
  private boolean endorsable;
  private boolean votable;
  private boolean viewable;
  private boolean updatable;
}
