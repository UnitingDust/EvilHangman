# EvilHangman

Evil Hangman is a spin off of the classic Hangman game where the computer dramatically reduces the chance of the user correctly guessing the word. There is no set "chosen" word when the game starts. Instead the program is going to generate a set of valid words that could possibly work with the user's guess. Each subsequent guess will reduce the set of valid words but the program will do its best to make sure the user doesn't guess the word. 

The algorithm that is implemented in the program seperates the running word list into different "word families" that satisfy the user's guessed letter. The word family that contains the largest amount of words will be the new word list after each letter guess. This process repeats until the list is reduced to a size of one or the user runs out of attempts. 

EXAMPLE:

Word List - hams, heat, runs, char
Letter Guess: h

Word Families
  1. hams, heat (one 'h' at 0 index)
  2. runs (no h)
  3. char (one 'h' at 1 index)
  
First word family has the largest amount of numbers so that will be the new word list after the letter guess. If two or more word   families have the same amount of words, the program will pick a random word family. 
