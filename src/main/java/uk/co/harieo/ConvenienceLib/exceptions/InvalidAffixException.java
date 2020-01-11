package uk.co.harieo.ConvenienceLib.exceptions;

import uk.co.harieo.ConvenienceLib.scoreboards.tablist.modules.Affix;

public class InvalidAffixException extends IllegalArgumentException {

	public InvalidAffixException(Affix invalidAffix, String error) {
		super(error + " (affix id: '" + invalidAffix.getUniqueId() + "')");
	}

}
