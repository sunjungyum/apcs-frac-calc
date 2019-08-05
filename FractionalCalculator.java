package fracCalcEC;
/**********************************************************
 * Assignment: FC Extra Credit
 *
 * Author: Sun-Jung Yum
 *
 * Description: This program does all the things that the final Fractional
 * Calculator (Checkpoint 4) does but it also incorporates extra-credit elements.
 * Just like Checkpoint 4, it calculates the answer to math expressions that
 * use addition, subtraction, multiplication, or addition -- with or without
 * fractions. It also offers (these are the extra credit elements) help content
 * when requested with the input "help," and returns an error message if the 
 * input is not a valid expression. 
 *
 * Academic Integrity: I pledge that this program represents my own work. I
 * received help from no one in designing and debugging my program.
 **********************************************************/

import java.util.Scanner;

public class FractionalCalculator
{

	public static void main(String[] args)
	{
		Scanner console = new Scanner(System.in);

		System.out.println("Welcome to the Fraction Calculator!");
		System.out.print("Enter an expression (or \"quit\"): ");
		String entireExpression = console.nextLine();
		while (!entireExpression.equals("quit"))
		{
			while (entireExpression.equals("help"))
			{
				printHelpContent();
				System.out.print("Enter an expression (or \"quit\"): ");
				entireExpression = console.nextLine();
			}
			try
			{
				int positionOfFirstSpace = entireExpression.indexOf(" ");
				String leftOperand = entireExpression.substring(0, positionOfFirstSpace);
				int positionOfSecondSpace = entireExpression.indexOf(" ", positionOfFirstSpace + 1);
				String rightOperand = entireExpression.substring(positionOfSecondSpace + 1, entireExpression.length());
				String operator = entireExpression.substring(positionOfFirstSpace + 1, positionOfSecondSpace);

				leftOperand = convertToFraction(leftOperand);
				rightOperand = convertToFraction(rightOperand);
				String unsimplifiedAnswer = calculate(leftOperand, operator, rightOperand);
				String reducedAnswer = reduce(unsimplifiedAnswer);
				String convertedAnswer = convertToMixed(reducedAnswer);
				System.out.println(convertedAnswer);
				System.out.println();
				System.out.print("Enter an expression (or \"quit\"): ");
				entireExpression = console.nextLine();
			}
			catch (Exception e)
			{
				System.out.println("ERROR: invalid input");
				System.out.println();
				System.out.print("Enter an expression (or \"quit\"): ");
				entireExpression = console.nextLine();
			}
		}
		System.out.print("Goodbye!");
	}

	/*
	 * prints help content for the user
	 */
	public static void printHelpContent()
	{
		System.out.println("Help Content:");
		System.out.println("This program calculates the answer to basic fractional math problems");
		System.out.println("that utilize addition, subtraction, multiplication, and division.");
		System.out.println("It calculates the given math problem and prints the reduced answer");
		System.out.println("in a mixed number or integer form, if appropriate. The program will");
		System.out.println("continue to prompt the user until \"quit\" is entered.");
		System.out.println("The program will only accept certain inputs and will only calculate");
		System.out.println("the answers to equations with one expression per line. Fractions must");
		System.out.println("be in the form \"x/y\" but can be improper. The calculator will also");
		System.out.println("accept mixed numbers in the form \"a_x/y\" where \"a\" is the constant.");
		System.out.println("Integers and negative values are also acceptable inputs. There must be");
		System.out.println("a space between each operand and the operator. Nothing else should be");
		System.out.println("inputed besides those three variables, unless the user is requesting for");
		System.out.println("help or is attempting to quit the program.");
		System.out.println();
	}

	/*
	 * identifies whether or not the inputed expression is a valid expression
	 */
	public static boolean noOperators(String expression)
	{
		return expression.indexOf(" + ") == -1 && expression.indexOf(" - ") == -1 && expression.indexOf(" * ") == -1
				&& expression.indexOf(" / ") == -1;
	}

	/*
	 * identifies whether or not the operand is negative
	 */
	private static boolean isNegative(String operand)
	{
		return operand.indexOf("-") != -1;
	}

	/*
	 * identifies whether or not the operand is a mixed number
	 */
	private static boolean isMixedNumber(String operand)
	{
		return operand.indexOf("_") != -1 && operand.indexOf("/") != -1;
	}

	/*
	 * identifies whether or not the operand is an integer
	 */
	private static boolean isInteger(String operand)
	{
		return operand.indexOf("/") == -1;
	}

	/*
	 * identifies the numerator in an operand
	 */
	private static int findNumerator(String operand)
	{
		int numerator = 0;
		if (operand.indexOf("/") != -1)
		{
			int positionOfSlash = operand.indexOf("/");
			numerator = Integer.parseInt(operand.substring(0, positionOfSlash));
		}
		else
		{
			numerator = Integer.parseInt(operand);
		}
		return numerator;
	}

	/*
	 * identifies the denominator in an operand
	 */
	private static int findDenominator(String operand)
	{
		int denominator = 1;
		if (operand.indexOf("/") != -1)
		{
			int positionOfSlash = operand.indexOf("/");
			denominator = Integer.parseInt(operand.substring(positionOfSlash + 1));
		}
		else
		{
			denominator = 1;
		}
		return denominator;
	}

	/*
	 * finds the greatest common factor for negative and positive numbers
	 */
	private static int gcf(int num1, int num2)
	{
		int greatestSoFar = 0;
		for (int i = 1; i <= Math.abs(num1); i++)
		{
			if (num1 % i == 0 && num2 % i == 0)
			{
				greatestSoFar = i;
			}
		}
		return greatestSoFar;
	}

	/*
	 * converts a mixed number into an improper fraction
	 */
	private static String convertFromMixedNumber(String input)
	{
		int positionOfSlash = input.indexOf("/");
		int positionOfUnderscore = input.indexOf("_");
		int wholeNumber = Integer.parseInt(input.substring(0, positionOfUnderscore));
		int denominator = Integer.parseInt(input.substring(positionOfSlash + 1));
		int numerator = Integer.parseInt(input.substring(positionOfUnderscore + 1, positionOfSlash));
		numerator = (wholeNumber * denominator) + numerator;
		String output = numerator + "/" + denominator;
		return output;
	}

	/*
	 * converts an integer into a fraction
	 */
	private static String convertFromInteger(String input)
	{
		String output = input + "/1";
		return output;
	}

	/*
	 * takes an input that may be a mixed number, integer, or a fraction, and
	 * converts it into a fraction
	 */
	public static String convertToFraction(String input)
	{
		String output;
		if (isNegative(input))
		{
			output = negativeConvertToFraction(input);
		}
		else
		{
			output = input;
			if (isMixedNumber(input))
			{
				output = convertFromMixedNumber(input);
			}
			if (isInteger(input))
			{
				output = convertFromInteger(input);
			}
			return output;
		}
		return output;
	}

	/*
	 * does the same thing as convertToFraction, but works for negative numbers
	 */
	private static String negativeConvertToFraction(String input)
	{
		String inputWithoutNegative = input.substring(1);
		inputWithoutNegative = convertToFraction(inputWithoutNegative);
		String output = "-" + inputWithoutNegative;
		return output;
	}

	/*
	 * calculates a multiplication expression
	 */
	private static String multiply(int leftNumerator, int leftDenominator, int rightNumerator, int rightDenominator)
	{
		int calculatedNumerator = leftNumerator * rightNumerator;
		int calculatedDenominator = leftDenominator * rightDenominator;
		String calculatedFraction = calculatedNumerator + "/" + calculatedDenominator;
		return calculatedFraction;
	}

	/*
	 * calculates a division expression
	 */
	private static String divide(int leftNumerator, int leftDenominator, int rightNumerator, int rightDenominator)
	{
		int calculatedNumerator = leftNumerator * rightDenominator;
		int calculatedDenominator = leftDenominator * rightNumerator;
		String calculatedFraction = calculatedNumerator + "/" + calculatedDenominator;
		return calculatedFraction;
	}

	/*
	 * calculates an addition expression
	 */
	private static String add(int leftNumerator, int rightNumerator, int calculatedDenominator)
	{
		int calculatedNumerator = leftNumerator + rightNumerator;
		String calculatedFraction = calculatedNumerator + "/" + calculatedDenominator;
		return calculatedFraction;
	}

	/*
	 * calculates a subtraction expression
	 */
	private static String subtract(int leftNumerator, int rightNumerator, int calculatedDenominator)
	{
		int calculatedNumerator = leftNumerator - rightNumerator;
		String calculatedFraction = calculatedNumerator + "/" + calculatedDenominator;
		return calculatedFraction;
	}

	/*
	 * calculates the expression, whether it's addition, subtraction,
	 * multiplication, or division and returns the calculated, unsimplified
	 * answer
	 */
	public static String calculate(String left, String operator, String right)
	{
		int leftNumerator = findNumerator(left);
		int leftDenominator = findDenominator(left);
		int rightNumerator = findNumerator(right);
		int rightDenominator = findDenominator(right);
		String calculatedFraction = null;
		if (operator.equals("*"))
		{
			calculatedFraction = multiply(leftNumerator, leftDenominator, rightNumerator, rightDenominator);
		}
		if (operator.equals("/"))
		{
			calculatedFraction = divide(leftNumerator, leftDenominator, rightNumerator, rightDenominator);
		}
		if (operator.equals("+") || operator.equals("-"))
		{
			int calculatedDenominator = leftDenominator * rightDenominator;
			leftNumerator = leftNumerator * rightDenominator;
			rightNumerator = rightNumerator * leftDenominator;
			if (operator.equals("+"))
			{
				calculatedFraction = add(leftNumerator, rightNumerator, calculatedDenominator);
			}
			if (operator.equals("-"))
			{
				calculatedFraction = subtract(leftNumerator, rightNumerator, calculatedDenominator);
			}
		}
		return calculatedFraction;
	}

	/*
	 * reduces fractions but does not convert them to mixed numbers or integers
	 */
	public static String reduce(String fraction)
	{
		String simplifiedFraction = null;
		int numerator = findNumerator(fraction);
		int denominator = findDenominator(fraction);
		if (numerator < 0 || denominator < 0)
		{
			simplifiedFraction = reduceNegative(numerator, denominator);
		}
		else
		{
			if (numerator != 0)
			{
				simplifiedFraction = reducePositive(numerator, denominator);
			}
			if (numerator == 0)
			{
				simplifiedFraction = "0/";
			}
		}
		return simplifiedFraction;
	}

	/*
	 * reduces a fraction with negative signs
	 */
	private static String reduceNegative(int numerator, int denominator)
	{
		String simplifiedFraction = null;
		int numeratorWithoutNegative = Math.abs(numerator);
		int denominatorWithoutNegative = Math.abs(denominator);
		int gcf = gcf(numeratorWithoutNegative, denominatorWithoutNegative);
		numeratorWithoutNegative = numeratorWithoutNegative / gcf;
		denominatorWithoutNegative = denominatorWithoutNegative / gcf;
		if (numerator < 0 && denominator < 0)
		{
			simplifiedFraction = numeratorWithoutNegative + "/" + denominatorWithoutNegative;
		}
		else
		{
			simplifiedFraction = (numeratorWithoutNegative * -1) + "/" + denominatorWithoutNegative;
		}
		return simplifiedFraction;
	}

	/*
	 * reduces a fraction without negative signs
	 */
	private static String reducePositive(int numerator, int denominator)
	{
		int gcf = gcf(numerator, denominator);
		numerator = numerator / gcf;
		denominator = denominator / gcf;
		String simplifiedFraction = numerator + "/" + denominator;
		return simplifiedFraction;
	}

	/*
	 * converts the answer into a mixed fraction if it is improper, an integer
	 * if it is an integer, or zero if it is zero; leaves proper fractions alone
	 */
	public static String convertToMixed(String fraction)
	{
		String convertedFraction = fraction;
		int numerator = findNumerator(fraction);
		if (hasNoDenominator(fraction))
		{
			convertedFraction = "" + numerator;
		}
		else
		{
			int denominator = findDenominator(fraction);
			if (isImproperFraction(numerator, denominator))
			{
				convertedFraction = convertImproperToMixed(numerator, denominator);
			}
			if (denominator == 1)
			{
				convertedFraction = "" + numerator;
			}
		}
		return convertedFraction;
	}

	/*
	 * identifies whether or not a fraction has a denominator
	 */
	private static boolean hasNoDenominator(String fraction)
	{
		return fraction.charAt(fraction.length() - 1) == '/';
	}

	/*
	 * identifies whether or not a fraction is improper
	 */
	private static boolean isImproperFraction(int numerator, int denominator)
	{
		return Math.abs(numerator) >= denominator && denominator != 1;
	}

	/*
	 * converts an improper fraction into a mixed number
	 */
	private static String convertImproperToMixed(int numerator, int denominator)
	{
		int constant = numerator / denominator;
		numerator = Math.abs(numerator % denominator);
		String convertedFraction = constant + "_" + numerator + "/" + denominator;
		return convertedFraction;
	}
}
