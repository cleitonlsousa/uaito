package com.uaito.comparator;

import com.uaito.domain.Tournament;
import com.uaito.dto.Player;
import com.uaito.enuns.CompareOptions;
import com.uaito.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Comparator extends TournamentComparator<Player> {

	private PlayerService playerService;

	public static final CompareOptions[] uniqueCompare = {
			CompareOptions.NAME };
	public static final CompareOptions[] pairingCompare = {
			CompareOptions.SCORE, CompareOptions.MARGIN_OF_VICTORY };
	public static final CompareOptions[] rankingCompare = {
			CompareOptions.SCORE,
			CompareOptions.MARGIN_OF_VICTORY,
			CompareOptions.AVERAGE_STRENGTH_OF_SCHEDULE, CompareOptions.RANDOM };
	public static final CompareOptions[] rankingCompareNoHeadToHead = {
			CompareOptions.SCORE, CompareOptions.MARGIN_OF_VICTORY,
			CompareOptions.AVERAGE_STRENGTH_OF_SCHEDULE, CompareOptions.RANDOM };

	private final Tournament t;
	private final CompareOptions[] sortOrder;

	private Comparator(){
		this.t = null;
		this.sortOrder = null;
		this.playerService = null;
	}

	public Comparator(Tournament t, CompareOptions[] sortOrder, PlayerService playerService) {
		this.t = t;
		this.sortOrder = sortOrder;
		this.playerService = playerService;
	}

	@Override
	public int compare(Player o1, Player o2) {

		int result = 0;

		for (CompareOptions option : sortOrder) {
			if (result == 0) {
				result = compareOption(o1, o2, option);
			}
		}

		return result;
	}

	private int compareOption(Player o1, Player o2, CompareOptions option) {

		int result = 0;

		switch (option) {
		case SCORE:
			result = compareInt(playerService.getScore(o1,t) , playerService.getScore(o2,t));
			break;
		case HEAD_TO_HEAD:
			if (o1.getFirstName().equals(o2.getFirstName())) {
				return 0;
			}
			result = playerService.isHeadToHeadWinner(o1, t) ? 1 : 0;
			if(result == 0){
				result = playerService.isHeadToHeadWinner(o2, t) ? -1 : 0;
			}
			break;
		case STRENGTH_OF_SCHEDULE:
			// Not implemented
			break;
		case AVERAGE_STRENGTH_OF_SCHEDULE:
			result = compareDouble(playerService.getAverageSoS(o1, t), playerService.getAverageSoS(o2, t));
			break;
		case MARGIN_OF_VICTORY:
			result = compareInt(playerService.getMarginOfVictory(t,o1), playerService.getMarginOfVictory(t,o2));
			break;
		case RANDOM:
			String seedValue1 = String.valueOf(Math.random());
			String seedValue2 = String.valueOf(Math.random());

			try {
				Double d1 = Double.valueOf(seedValue1);
				Double d2 = Double.valueOf(seedValue2);

				result = d1.compareTo(d2);
			} catch (NumberFormatException e) {
				result = seedValue1.compareTo(seedValue2);
			}
			break;
		case NAME:
			result = o1.getFirstName().compareTo(o2.getFirstName());
			break;
		}

		return result;
	}


}