
public class MainG {

	public static void main(String args[]) {

		RBt structure = new RBt();

		int keys[] = { 1000, 1500, 2000, 2500, 3000, 3500, 4000, 4500, 5000, 5500, 6000, 6500, 7000, 7500, 8000, 8500,
				9000, 9500 };
		for (int a : keys)
			structure.insert(new Node(a));

		structure.root.display();

		structure.inorder();
		System.out.println();

		structure.find(3500).display();

		// structure.find(4000).display();;

		// for (int b: keys )
		// {
		// structure.find(b).display();
		// System.out.println("****************************************");
		// }
		// structure.find(9000).display();
		// structure.delete(9000);
		// structure.find(9500).display();

		structure.delete(4500);
		structure.delete(3500);
		structure.delete(8000);
		structure.delete(3000);
		structure.root.display();
		structure.find(1500).display();
		structure.find(2500).display();

	}
}
