
 class Node {
	
	Node left, right,parent;
	int key;
	c color;
//	o orientation;
	
	Node(int key){
		this.key=key;
	}
	
	void display()
	{
		System.out.println("the Node is "+this.key+" and its color is "+this.color);
		if (this.left!=null)
			System.out.println("the left Child is "+this.left.key+" and its color is "+this.left.color);
		if (this.right!=null)
			System.out.println("the right Child is "+this.right.key+" and its color is "+this.right.color);
		System.out.println("\n\n");
	}

	boolean hasALeftChild() {
		return this.left!=null? true:false;
	}
	
	boolean hasARightChild() {
		return this.right!=null? true:false;
	}

	

	Node getLeft() {
		// TODO Auto-generated method stub
		return this.left!=null? this.left:null;
	}
	Node getRight() {
		return this.right!=null? this.right:null;
	}
	
	 
 
 }
