import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * 
 * @author Angel Chen
 * @version 11/30/17
 */
public class Hangman {

	public static void main(String[] args) throws FileNotFoundException, InterruptedException
	{
		File dictionary = new File("dictionary.txt");
		//File dictionary = new File("C:\\Users\\212674282\\Desktop\\dictionary.txt");
		Scanner scan = new Scanner(dictionary);
		
		System.out.println("------------------------------");
		System.out.println("Welcome to Evil Hangman!!!");
		System.out.println("Let's see if you can even guess the word");
		System.out.println("------------------------------");
		Thread.sleep(1000);
		
		ArrayList<String>list = new ArrayList<String>();
		LinkedHashSet<Character> guess = new LinkedHashSet<Character>();
		HashSet<Integer> nums = new HashSet<Integer>();
		
		//Add all of the dictionary words into a list
		while (scan.hasNext())
		{
			String current = scan.next();	
			
			list.add(current);
			nums.add(new Integer(current.length()));
		}
		
		scan = new Scanner(System.in);	
				
		int wordlength = getValidLength(nums, scan);		
		ArrayList<String> temp = new ArrayList<String>();
		
		//Add words of specified length into a temporary list
		for (int i = 0; i < list.size(); i++)
		{			
			if (list.get(i).length() == wordlength)
			{
				temp.add(list.get(i));
			}			
		}
		
		//Set current list as the temporary list
		list = temp;
					
		//This string shows the progress on which letters the user guessed correctly for the word
		String predict = String.format("%" + wordlength + "s", "").replace(" ", "-");	
		
		System.out.print("Enter number of attempts: ");
		int count = scan.nextInt();
		
		if (count >= 20)
		{
			System.out.println("Wow you must be really desperate to beat me... Ok.");
			Thread.sleep(2000);
		}
		
		for (int i = count; i > 0; i--)
		{				
			printStatus(i, guess, predict);
			
			System.out.print("Enter guess: ");
			char letter = getValidGuess(scan);
			
			//Make sure input isn't a duplicate
			while (guess.contains(new Character(letter)))
			{				
				System.out.print("You already used this letter already. Enter another letter: ");
				letter = scan.next().charAt(0);				
			}
			
			guess.add(new Character(letter));
			
			char[] copy = predict.toCharArray();		
			list = processList(list, letter, predict.toCharArray());
			
			int track = 0;
			
			//Count how many times did the user match the "chosen word"
			for (int j = 0; j < list.get(0).length(); j++)
			{
				if (list.get(0).charAt(j) == letter)
				{
					copy[j] = letter;
					track++;
					
				}
					
			}
			
			if (track == 0)
				System.out.println("Unlucky! No matched letters!");
			
			else
				System.out.println("You matched " + track + " letters!");
			
			//Update the predict string with the newly matched letters, if any
			predict = new String(copy);
			
			//Did the user correctly guessed the whole word?
			if (!predict.contains("-"))
			{
				System.out.println("You guessed the word! Sugoi! It was " +list.get(0));
				System.exit(1);
			}	
			
			System.out.println("------------------------------");
		}
		
		//Choose a random word from the current list as the "chosen" word
		int result = (int)(Math.random() * list.size());
		System.out.println("You Lost :) The word was " +list.get(result));
				
	}
	
	/**
	 * Print out statements telling the user how many tries the user has left, 
	 * what has been correctly predicted so far, and past guesses
	 * @param num, Number of tries left
	 * @param guess, List of previous entered guesses
	 * @param predict, Correctly predicted string so far 
	 */
	public static void printStatus(int num, LinkedHashSet<Character> guess, String predict)
	{
		if (num == 1)
			System.out.println("Last chance before you lose. Heh heh!");
		
		else
			System.out.println("You currently have " + num + " tries left");
		
		System.out.print("Used Letters: ");
		Iterator<Character> itr = guess.iterator();
		
		while (itr.hasNext())
		{
			System.out.print(itr.next().charValue() + " ");
		}
		
		System.out.println("");
		
		System.out.println("Word: " +predict);
	
	}
	
	/**
	 * This method will ask for a guess from the user and ensure that it valid
	 * @param scan, Scanner object
	 * @return a valid char
	 */
	public static char getValidGuess(Scanner scan)
	{
		System.out.print("Enter guess: ");
		char letter = scan.next().charAt(0);
		
		while (!Character.isAlphabetic(letter))
		{
			System.out.println("You didn't enter a letter. Try again : ");
			letter = scan.next().charAt(0);
		}
		
		return letter;
	}
		
	/**
	 * This method will ask for a desired word length from the user and ensure that 
	 * there is at least one word in the dictionary that has the specified length 
	 * @param nums, Set of word lengths from all of  words in the dictionary 
	 * @param scan, Scan object
	 * @return a valid word length
	 */
	public static int getValidLength(HashSet<Integer> nums, Scanner scan)
	{			
		System.out.print("Enter a word length: ");
		int wordlength = scan.nextInt();
		
		while (!nums.contains(new Integer(wordlength)))
		{
			if (wordlength < 0)
			{
				System.out.print("Seriously? Ain't no negative numbers. Enter another: ");
				wordlength = scan.nextInt();
			}
			
			else
			{
				System.out.print("No words of the specified length. Enter another: ");
				wordlength = scan.nextInt();
			}
		}
				
		return wordlength;
		
	}
	
	/**
	 * This method is going to break down the current list into different lists of "word families"
	 * that fulfill the criteria of the user's guess letter. The word family with the 
	 * largest amount of words will be the new current list 
	 * @param list, Running list of valid words left 
	 * @param letter, User input guess letter
	 * @param predict, Represents what has been correctly guessed so far 
	 * @return a list of words that pass the criteria 
	 */
	public static ArrayList<String> processList(ArrayList<String>list, char letter, char[] predict)
	{			
		
		//Map each Word Family to a list of words 
		HashMap<String, ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
		
		//Make a copy of the predict array
		char[] copy = new char[predict.length];
		
		for (int i = 0; i < copy.length; i++)
		{
			copy[i] = predict[i];
		}
					
		for (int i = 0; i < list.size(); i++)
		{
			//Classify which "word family" the word its in
			for (int j = 0; j < list.get(i).length(); j++)
			{
				if (list.get(i).charAt(j) == letter)
					predict[j] = letter;
			}
			
			//Create a new word family if it doesn't exist in the map. Otherwise insert word into existing word family
			if (map.containsKey(new String(predict)))
			{
				map.get(new String(predict)).add(list.get(i));
			}
				
			
			else
			{
				ArrayList<String> toInsert = new ArrayList<String>();
				toInsert.add(list.get(i));
				map.put(new String(predict), toInsert);
			}
					
			//Reset the array to the original 
			for (int j = 0; j < copy.length; j++)
			{
				predict[j] = copy[j];
			}			
		}
		
		int max = Integer.MIN_VALUE;
		String toUse = "";
		
		//Find the word family that has the most words
		for (String key : map.keySet())
		{			
			int temp = map.get(key).size();
					
			if (temp > max)
			{
				max = temp;
				toUse = key;
			}		
		}
								
		return map.get(toUse);
	}
}
