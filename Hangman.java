import java.io.File;

import java.io.FileNotFoundException;
import java.util.*;

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
						
		while (scan.hasNext())
		{
			String current = scan.next();	
			
			list.add(current);
			nums.add(new Integer(current.length()));
		}
		
		scan = new Scanner(System.in);	
				
		int wordlength = getValidLength(nums, scan);		
		ArrayList<String> temp = new ArrayList<String>();
		
		for (int i = 0; i < list.size(); i++)
		{			
			if (list.get(i).length() == wordlength)
			{
				temp.add(list.get(i));
			}			
		}
		
		list = temp;
					
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
			
			while (guess.contains(new Character(letter)))
			{				
				System.out.print("You already used this letter already. Enter another letter: ");
				letter = scan.next().charAt(0);				
			}
			
			guess.add(new Character(letter));
			
			char[] copy = predict.toCharArray();		
			list = processList(list, letter, predict.toCharArray());
			
			int track = 0;
			
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
			
			predict = new String(copy);
			
			if (!predict.contains("-"))
			{
				System.out.println("You guessed the word! Sugoi! It was " +list.get(0));
				System.exit(1);
			}	
			
			System.out.println("------------------------------");
		}
		
		int result = (int)(Math.random() * list.size());
		System.out.println("You Lost :) The word was " +list.get(result));
				
	}
	
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
		
	public static int getValidCount(HashSet<Integer> nums, Scanner scan)
	{			
		System.out.print("Enter a word length: ");
		int wordlength = scan.nextInt();
		
		while (!nums.contains(new Integer(wordlength)))
		{
			if (wordlength <= 0)
			{
				System.out.print("Seriously? Ain't no negative numbers. Enter another: ");
				wordlength = scan.nextInt();
			}
			
			else
			{
				System.out.print("No words are that long. Enter another: ");
				wordlength = scan.nextInt();
			}
		}
				
		return wordlength;
		
	}
	
	public static ArrayList<String> processList(ArrayList<String>list, char letter, char[] predict)
	{					
		HashMap<String, ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
		
		char[] copy = new char[predict.length];
		
		for (int i = 0; i < copy.length; i++)
		{
			copy[i] = predict[i];
		}
					
		for (int i = 0; i < list.size(); i++)
		{			
			for (int j = 0; j < list.get(i).length(); j++)
			{
				if (list.get(i).charAt(j) == letter)
					predict[j] = letter;
			}
					
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
					
			for (int j = 0; j < copy.length; j++)
			{
				predict[j] = copy[j];
			}			
		}
		
		int max = Integer.MIN_VALUE;
		String toUse = "";
		
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
