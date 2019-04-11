package org.rulez.demokracia.pdengine;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rulez.demokracia.pdengine.exception.IsNullException;
import org.rulez.demokracia.pdengine.exception.NotValidCharsException;
import org.rulez.demokracia.pdengine.exception.TooLongException;
import org.rulez.demokracia.pdengine.exception.TooShortException;

public class ValidationUtil {

  public static final int MIN_STRING_LENGTH = 3;
  public static final int MAX_STRING_LENGTH = 255;
  public static final String EMPTY_STRING = "";
  public static final String COUNTED = "counted";
  private static final String NO_SPACES_PATTERN = "(\\d|\\w)+";
  private static String WITH_SPACES_PATTERN = "(\\d| |\\w)+";

  public static void checkVoteName(final String voteName) {
    checkStringWithSpaces(voteName, "vote name");
  }

  public static Set<String>
      checkAssurances(final Set<String> neededAssurances, final String type) {

    final Set<String> uniques = new HashSet<>();

    for (final String assurance : neededAssurances) {
      final Set<String> uniques1 = uniques;
      final String assurance1 = assurance;
      final String normalizedAssurance = normalizeAssurance(type, assurance1);
      uniques1.add(normalizedAssurance);
    }

    return uniques;
  }

  private static String
      normalizeAssurance(final String type, final String assurance) {
    if (COUNTED.equals(type) && EMPTY_STRING.equals(assurance))
      return null;
    else
      checkStringNoSpaces(assurance, type + " assurance name");
    return assurance;
  }

  public static void
      checkStringNoSpaces(final String inputString, final String description) {

    checkStringForPattern(inputString, description, NO_SPACES_PATTERN);
  }

  public static void checkStringWithSpaces(
      final String inputString, final String description
  ) {

    checkStringForPattern(inputString, description, WITH_SPACES_PATTERN);
  }

  public static void checkStringForPattern(
      final String inputString, final String description,
      final String noSpaceString
  ) {
    checkNotNull(inputString, description);
    checkNotTooShort(inputString, description);
    checkNotTooLong(inputString, description);
    checkAllValidChars(inputString, description, noSpaceString);
  }

  public static void checkAllValidChars(
      final String inputString, final String description,
      final String noSpaceString
  ) {
    final Pattern pattern =
        Pattern.compile(noSpaceString, Pattern.UNICODE_CHARACTER_CLASS);
    final Matcher matcher = pattern.matcher(inputString);

    if (!matcher.matches())
      throw new NotValidCharsException(description);
  }

  public static void checkNotTooLong(
      final String inputString, final String description
  ) {
    if (inputString.length() > MAX_STRING_LENGTH)
      throw new TooLongException(description);
  }

  public static void checkNotTooShort(
      final String inputString, final String description
  ) {
    if (inputString.length() < MIN_STRING_LENGTH)
      throw new TooShortException(description);
  }

  public static void checkNotNull(
      final String inputString, final String description
  ) {
    if (null == inputString)
      throw new IsNullException(description);
  }

}
