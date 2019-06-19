package org.rulez.demokracia.pdengine.choice;

import javax.persistence.Entity;

import org.rulez.demokracia.pdengine.persistence.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
public class RankedChoice extends BaseEntity {

  private static final long serialVersionUID = 1L;
  private String choiceId;
  private int rank;

  public RankedChoice(final String choiceId, final int rank) {
    super();
    this.choiceId = choiceId;
    this.rank = rank;
  }
}
