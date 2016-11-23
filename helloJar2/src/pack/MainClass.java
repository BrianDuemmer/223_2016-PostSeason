package pack;

public class MainClass {

	public static void main(String[] args) {
		for(int i = 0; i < 10; i++)
		{
			System.out.println("Hellooooooooooo World");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
