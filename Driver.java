package Project;

import java.util.ArrayList;
import java.util.List;

public class Driver {
	public static void main(String[] args){		
		List<Wordsearch> puzzles = new ArrayList<Wordsearch>();
		puzzles.add(new Wordsearch("favWords.txt", 10, 3, 8));
		
		// Test wordFile constructors
		for(Wordsearch puzzle : puzzles){
			// shows puzzle with hidden locations
			puzzle.showWordSearchPuzzle(true);
			System.out.println();
			
			// shows puzzle with locations shown
			puzzle.showWordSearchPuzzle(false);
			
			// evaluation section
			System.out.print("\n-----------------------------------------------------\nEvaluation\n-----------------------------------------------------\n");
			
			System.out.println("Puzzle Shown and locations hidden Successfully\n");
			System.out.println("Puzzle Shown and locations revealed Successfully\n");
			
			// test if there's a correct amount of words in the puzzle
			if(puzzle.getWordSearchList().size() == puzzle.wordCount){
				System.out.println("The puzzle uses the correct amount of words\n");
			} else {
				System.out.println("The puzzle uses a incorrect amount of words\n");
			}
			
			// test if the words in puzzle are correct
			List<String> test = puzzle.loadWordsFromFile(puzzle.wordFile, puzzle.shortest, puzzle.longest);
			int num = 0;
			for(String word : test){
				for(String testee : puzzle.getWordSearchList()){
					if(word.equalsIgnoreCase(testee)){
						num++;
					}
				}
			}
			if(num == puzzle.wordCount){
				System.out.println("The puzzle uses words from the specified file\n");
			} else {
				System.out.println("ERROR: The puzzle seems to be useing words not in the file specified\n");
			}
			
			// test if the words fit size Specifications
			boolean correct = true;
			for(String word : puzzle.getWordSearchList()){
				if(word.length() < puzzle.shortest || word.length() > puzzle.longest){
					correct = false;
					break;
				}
			}
			if(correct == true){
				System.out.println("All the words fit the size Specifications\n");
			} else {
				System.out.println("Some words dont fit the size Specifications\n");
			}
			
			// tests if the puzzle is playable
			if(puzzlePlayer(puzzle.getPuzzleAsString(true), puzzle.getPuzzleAsString(false), puzzle.getWordSearchList()) == true){
				System.out.println("The puzzle was Successfully Completed\n");
			} else {
				System.out.println("ERROR: The puzzle was Completed Unsuccessfully\n");
			}
			
			System.out.print("-----------------------------------------------------\n\n\n");
		}
	}
	
	public static boolean puzzlePlayer(String vertical, String horizontal, List<String> puzzleWords){
		// Plays the puzzle and returns weither it is possible to complete it
		int count = 0;
				
		for(String word : puzzleWords){
			// reverse word
			String drow = "";
			char temp[] = word.toCharArray();
			for(int i = temp.length-1; i > 0; i--){
				drow = drow + temp[i];
			}
			
			// check if the puzzle contains the word or the reverse of the word in the case of going left and up
			if(horizontal.contains(word) || horizontal.contains(drow) || vertical.contains(word) || vertical.contains(drow)){
				count++;
			}
		}
		
		if(count == puzzleWords.size()){
			return true;
		}
		return false;
	}
}
