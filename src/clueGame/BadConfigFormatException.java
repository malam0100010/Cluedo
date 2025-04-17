package clueGame;

public class BadConfigFormatException extends Exception 
{
	BadConfigFormatException(String e)
	{
		System.out.println(e);
	}

	public BadConfigFormatException(char e) {
		// TODO Auto-generated constructor stub
		System.out.println(e);
	}

}
