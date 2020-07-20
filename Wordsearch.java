package Project;

import java.util.ArrayList;
import java.io.*;

public class Wordsearch {
	// Basic wordsearch methods
	private char[][] puzzle;
	
	private ArrayList<String> puzzleWords;
	private ArrayList<String> puzzleWordsLocations;

	private int puzzleHeight, puzzleWidth;
	
	public String wordFile;
	public int wordCount, shortest, longest;
	
	public Wordsearch(ArrayList<String> userSpecifiedWords){
		userSpecifiedWords.replaceAll(String::toUpperCase);
		puzzleWords = userSpecifiedWords;
		generateWordSearchPuzzle();
	}
	
	public Wordsearch(String wordFile, int wordCount, int shortest, int longest){
		this.wordFile = wordFile;
		this.wordCount = wordCount;
		this.shortest = shortest;
		this.longest = longest;
		wordsFromFile(wordFile, wordCount, shortest, longest);
		generateWordSearchPuzzle();
	}
	
	private void generateWordSearchPuzzle(){
		getLength();
		placeWords();
		randomFill();
	}
	
	public ArrayList<String> getWordSearchList(){
		return puzzleWords;
	}
	
	public char[][] getPuzzleAsGrid(){
		return puzzle;
	}
	
	public String getPuzzleAsString(boolean vertical){
		String puzzle = "";
		for(int row = 0; row < puzzleHeight; row++){
			for(int col = 0; col < puzzleWidth; col++){
				if(vertical == true){
					puzzle += this.puzzle[col][row];
				} else {
					puzzle += this.puzzle[row][col];
				}
			}
			puzzle += "\n";
		}
		return puzzle;
	}

	public void showWordSearchPuzzle(boolean hide){
		// title generation
		String puzzleTitle = "", wordTitle = "", top = "", bottom = "";
		for(int i = 0; i < puzzleWidth; i++){
			if(i == 0){
				puzzleTitle += "------";
				wordTitle += "\n------";
				top += "\n    _";
				bottom += "    -";
			} else {
				puzzleTitle += "---";
				wordTitle += "---";
				top += "___";
				bottom += "---";
			}
		}
		puzzleTitle += "\nPUZZLE\n" + puzzleTitle + "\n";
		wordTitle += "\nWORDS" + wordTitle + "\n";

		// title + x axis display
		System.out.println(puzzleTitle);

		System.out.printf("%2s", "");
		for(int i = 0; i < puzzleWidth; i++){
			System.out.printf("%3s", i);
		}

		// Puzzle display + y axis display
		System.out.println(top);
		for(int row = 0; row < puzzleHeight; row++){
			for(int col = 0; col < puzzleWidth; col++){
				if(col == 0){
					if(row < 10){
						System.out.print(row + "  |" + puzzle[row][col]);
					} else if(row < 100){
						System.out.print(row + " |" + puzzle[row][col]);
					} else {
						System.out.print(row + "|" + puzzle[row][col]);
					}
				} else {
					System.out.printf("%3s", puzzle[row][col]);
				}
			}
			System.out.println("|");
		}
		System.out.println(bottom);

		// Words display
		System.out.println(wordTitle);

		// Words list generation
		if(hide == true){
			System.out.println("Num\t" + "Word\n");
		} else {
			System.out.println("Num\t" + "Word\t\t" + "[X][Y][Direction]\n");
		}
		for(int i = 0; i < puzzleWords.size(); i++){
			if(hide == true){
				System.out.print((i+1) + ".\t" + puzzleWords.get(i) + "\n");
			} else {
				System.out.print((i+1) + ".\t" + puzzleWords.get(i));
				String tab = "\t\t";
				if(puzzleWords.get(i).length() > 7){
					tab = "\t";
				}
				System.out.format(tab + puzzleWordsLocations.get(i) + "\n");
			}
		}
	}

	// creaters
	private void wordsFromFile(String fileName, int number, int shortest, int longest){
		// we use this method to set puzzleWords from selected file

		// load words into arrayList
		ArrayList<String> words = loadWordsFromFile(fileName, shortest, longest);
		
		puzzleWords = new ArrayList<String>();

		// pick random words from the list
		for(int i = 0; i < number; i++){
			int r = (int)(Math.random() * words.size());
			if(!puzzleWords.contains(words.get(r))){
				puzzleWords.add(words.get(r));
			} else {
				i--;
			}
			words.remove(r);
		}
	}

	public ArrayList<String> loadWordsFromFile(String fileName, int shortest, int longest){
		// Store whole file in arraylist
		try{
			FileReader aFileReader = new FileReader(fileName);
			BufferedReader aBufferReader = new BufferedReader(aFileReader);

			String lineFromFile;

			ArrayList<String> words = new ArrayList<String>();

			lineFromFile = aBufferReader.readLine();
			while(lineFromFile != null){
				if(lineFromFile.length() >= shortest && lineFromFile.length() <= longest){
					words.add(lineFromFile.toUpperCase());
				}
				lineFromFile = aBufferReader.readLine();
			}
			aBufferReader.close();
			aFileReader.close();
			return words;
		}catch(IOException x){
			return null;
		}
	}

	private boolean getLength(){
		int longest, totalChars;

		// determine the longest word + number of characters
		longest = puzzleWords.get(0).length();
		totalChars = puzzleWords.get(0).length();

		for(String word : puzzleWords){
			totalChars += word.length();
			if(word.length() > longest){
				longest = word.length();
			}
		}

		// set size
		puzzleHeight = (int) (totalChars*1.75/5);
		if(longest > puzzleHeight){
			puzzleHeight = longest + 4;
		}
		puzzleWidth = puzzleHeight;
		if(puzzleHeight <= 26){
			puzzle = new char[puzzleHeight][puzzleWidth];
			return true;
		} else {
			return false;
		}
	}

	private void placeWords(){
		boolean wordNotPlaced;
		String directions = "";
		int row = 0, col = 0;
		
		puzzleWordsLocations = new ArrayList<String>();

		for(String word : puzzleWords){
			wordNotPlaced = true;
			while(wordNotPlaced){
				if(word.length() > 6){
					// very obvious due to length
					directions = "UL";
				} else {
					directions = "UDLR";
				}
				while(directions.length() > 0 && wordNotPlaced){
					// pick a direction
					char chosenDirection = directions.charAt((int)(Math.random() * directions.length()));
					// random location based on direction
					boolean locationChosen = false;
					while(locationChosen == false){
						switch(chosenDirection){
							case 'U':
								row = (int) (Math.random() * (puzzleHeight - word.length())) + word.length();
								col = (int) (Math.random() * puzzleWidth);
							break;
							case 'D':
								row = (int) (Math.random() * (puzzleHeight - word.length()));
								col = (int) (Math.random() * puzzleWidth);
							break;
							case 'L':
								row = (int) (Math.random() * puzzleHeight);
								col = (int) (Math.random() * (puzzleWidth - word.length())) + word.length();
							break;
							case 'R':
								row = (int) (Math.random() * puzzleHeight);
								col = (int) (Math.random() * (puzzleWidth - word.length()));
							break;
						}
						if(puzzle[row][col] == '\0'){
							locationChosen = true;
						}
					}
					if(spaceAvailable(row,col,chosenDirection,word.length())){
						placeWord(row, col, chosenDirection, word);
						wordNotPlaced = false;
						puzzleWordsLocations.add("[" + row + "][" + col + "][" + chosenDirection + "]"); 
					} else {
						directions = directions.replace("" + chosenDirection, "");
					}
				}
			}
		}
	}

	private boolean spaceAvailable(int row, int col, char direction, int length){
		int blanks = 0;
		if(direction == 'U'){
			if(length + 1 <= row){
				for(int i = 0; i < length; i++){
					if(puzzle[row-1][col] == '\0'){
						blanks++;	
					}
				}
			}
		} else if(direction == 'D'){
			if(row + length - 1 < puzzleHeight){
				for(int i = 0; i < length; i++){
					if(puzzle[row+i][col] == '\0'){
						blanks++;
					}
				}
			}
		} else if(direction == 'L'){
			if(length + 1 <= col){
				for(int i = 0; i < length; i++){
					if(puzzle[row][col-i] == '\0'){
						blanks++;
					}
				}
			}
		} else if(direction == 'R'){
			if(col + length - 1 < puzzleWidth){
				for(int i = 0; i < length; i++){
					if(puzzle[row][col+i] == '\0'){
						blanks++;
					}
				}
			}
		}
		return (blanks == length);
	}

	private void placeWord(int row, int col, char direction, String word){
		for(int i = 0; i < word.length(); i++){
			switch(direction){
				case 'U': puzzle[row-i][col] = word.charAt(i);
				break;
				case 'D': puzzle[row+i][col] = word.charAt(i);
				break;
				case 'L': puzzle[row][col-i] = word.charAt(i);
				break;
				case 'R': puzzle[row][col+i] = word.charAt(i);
				break;
			}
		}
	}

	private void randomFill(){
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for(int row = 0; row < puzzleHeight; row++){
			for(int col = 0; col < puzzleWidth; col++){
				if(puzzle[row][col] == '\0'){
					puzzle[row][col] = alphabet.charAt((int)(Math.random() * alphabet.length()));
				}
			}
		}
	}

	// Solving solution
	public boolean solve(){
		String horizontal = getPuzzleAsString(false);
		String vertical = getPuzzleAsString(true);
		
		for(String word : puzzleWords){
			// reverse word
			String drow = "";
			char temp[] = word.toCharArray();
			for(int i = temp.length-1; i > 0; i--){
				drow = drow + temp[i];
			}
			
			// check if the puzzle contains the word or the reverse of the word in the case of going left and up
			if(!horizontal.contains(word) || !horizontal.contains(drow) || !vertical.contains(word) || !vertical.contains(drow)){
				return false;
			}
		}
		return true;
	}
}
