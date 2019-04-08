package org.rulez.demokracia.pdengine;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.rulez.demokracia.pdengine.testhelpers.CreatedDefaultVoteRegistry;

public class AssuranceProviderTest extends CreatedDefaultVoteRegistry{

	@Test
	public void assurances_of_current_user_can_be_obtained() {
		List<String> expected = Arrays.asList("magyar", "német");
		List<String> assurances = voteManager.getAssurances();
		assertEquals(expected,assurances);
	}

}
