
enum c {
	RED, BLACK,
};

class RBt {
	Node root;

	RBt() {
	}

	void insert(Node k) {
		Node temp = null, parent = null;
		if (root == null) {
			root = k;
			root.color = c.BLACK;
		} else if (root != null) {
			temp = root;
			while (temp != null) {
				parent = temp;

				if (k.key > temp.key)
					temp = temp.right;
				else if (k.key < temp.key)
					temp = temp.left;
			}
			k.color = c.RED;
			k.parent = parent;

			if (k.key > parent.key) {
				parent.right = k;
			} else if (k.key < parent.key) {
				parent.left = k;
			}
			insertionBalanceHandler(k);
		}
	}

	private void insertionBalanceHandler(Node leaf) {
		Node uncle, parent, grandParent;
		parent = leaf.parent;
		grandParent = leaf.parent.parent;
		uncle = getUncle(leaf);
		while (leaf.color == c.RED && parent.color == c.RED) {
			if (grandParent == null)
				parent.color = c.BLACK;
			else if (uncle == null || uncle.color == c.BLACK) {
				if (leaf.key > parent.key && parent.key > parent.parent.key) // right-right case
				{
					L(parent);
					grandParent.color = parent.color; // should be red
					parent.color = c.BLACK;
				} else if (leaf.key > parent.key && parent.key < parent.parent.key) {
					LR(leaf);
					grandParent.color = parent.color; // should be red
					leaf.color = c.BLACK;
				} else if (leaf.key < parent.key && parent.key < parent.parent.key) {
					R(parent);
					grandParent.color = parent.color; // should be red
					parent.color = c.BLACK;
				} else if (leaf.key < parent.key && parent.key > parent.parent.key) {
					RL(leaf);
					grandParent.color = parent.color; // should be red
					leaf.color = c.BLACK;
				}

			} else if (uncle.color == c.RED) {
				uncle.color = parent.color = parent.parent.color; // should be black
				if (grandParent != root)
					grandParent.color = c.RED;
			}
			leaf = grandParent;
			parent = grandParent.parent;
			if (parent != null)
				grandParent = parent.parent;
			uncle = getUncle(leaf);
		}
	}

	private void RL(Node x) {
		R(x);
		L(x);
	}

	private void R(Node x) {
		Node px = x.parent, gx = x.parent.parent;
		px.left = x.right;
		if (px.left != null)
			px.left.parent = px;
		x.right = px;
		x.parent = px.parent;
		px.parent = x;
		if (px == root)
			root = x;
		if (gx != null)
			if (x.key > gx.key)
				gx.right = x;
			else
				gx.left = x;
	}

	private void L(Node x) { // x is the node; px is the parent of x;

		Node px = x.parent, gx = x.parent.parent; // setting up the grandparent
		px.right = x.left; // leftchild of parent is gonna be grandparent's rightchild
		if (px.right != null)
			px.right.parent = px; // setting the parent of the left child to the parent;
		x.left = px;
		x.parent = px.parent;
		px.parent = x;
		if (px == root)
			root = x;
		if (gx != null)
			if (x.key > gx.key)
				gx.right = x;
			else
				gx.left = x;
	}

	private void LR(Node x) {
		L(x);
		R(x);
	}

	Node find(int key) {
		Node node = root;
		while (key != node.key) {
			if (key > node.key)
				node = node.right;
			else if (key < node.key)
				node = node.left;
		}
		return node;
	}

	void delete(int key) {
		Node node = find(key);
		if (node.key == root.key && !node.hasALeftChild() && !node.hasARightChild())
			root = null;
		else if (node.right == null && node.left == null)
			firstCaseDeletion(node);
		else if (node.right != null && node.left == null || node.left != null && node.right == null)
			secondCaseDeletion(node);
		else if (node.right != null && node.left != null)
			thirdCaseDeletion(node);
	}

	private void firstCaseDeletion(Node nodeX) {
		rbDeletionColorHandler(nodeX);
		if (nodeX.key > nodeX.parent.key)
			nodeX.parent.right = null;
		else if (nodeX.key < nodeX.parent.key)
			nodeX.parent.left = null;
	}

	private void secondCaseDeletion(Node node) {
		rbDeletionColorHandler(node);
		Node parent = node.parent;
		if (node.right == null && node.left != null) {
			if (node.key > parent.key)
				parent.right = node.left;

			// the equal is for the 3rd case deletion to work if the leaf node had a child.
			// and was the immediate left child instead of a
			// right leaf of this left child.
			else if (node.key <= parent.key)
				parent.left = node.left;
			// either case I will assign the grandparent as the parent of the deleted node's
			// child.
			node.left.parent = node.parent;
		} else if (node.left == null && node.right != null) {
			if (node.key > parent.key)
				parent.right = node.right;
			else if (node.key < parent.key)
				parent.left = node.right;

			node.right.parent = node.parent;
		}
	}

	private void thirdCaseDeletion(Node nodeX) {
		Node parent = nodeX.parent, leafNode = nodeX.left, leafCopy = null;

		while (leafNode.right != null)
			leafNode = leafNode.right;

		leafCopy = new Node(leafNode.key);

		if (nodeX == root)
			root = leafCopy;
		else if (nodeX != root) {
			if (nodeX.key > parent.key)
				parent.right = leafCopy;
			else if (nodeX.key < parent.key)
				parent.left = leafCopy;
		}
		leafCopy.color = nodeX.color;
		leafCopy.parent = parent;

		leafCopy.right = nodeX.right;
		nodeX.right.parent = leafCopy;

		leafCopy.left = nodeX.left;
		nodeX.left.parent = leafCopy;

		if (hasOneChild(leafNode))
			secondCaseDeletion(leafNode);
		else
			firstCaseDeletion(leafNode);

	}

	void rbDeletionColorHandler(Node node) {
		if (node.color == c.RED || hasARedChild(node))
			firstCaseRBDeletionColorHandler(node);
		else if (node.color == c.BLACK)
		{
			secondCaseRBDeletionColorHandler(node);
		}
	}

	private void firstCaseRBDeletionColorHandler(Node nodeX) {
		if (nodeX.color == c.RED)
			; // do nothing proceed to the to the standard binary tree delete.

		else if (hasARedChild(nodeX)) // specific for second case standard BST deletion.
			if (nodeX.left != null)
				nodeX.left.color = c.BLACK;
			else // if (nodeX.right!=null)
				nodeX.right.color = c.BLACK;
	}

	private void secondCaseRBDeletionColorHandler(Node x) {
		Node sibling = getSibling(x);
		if (sibling.color == c.BLACK && hasARedChild(sibling)) // case 2 a.
			case2a(sibling);
		else if (sibling.color == c.BLACK && !hasARedChild(sibling))
			case2b(sibling);
		else if (sibling.color == c.RED)
			case2c(sibling);
	}

	private void case2a(Node sibling) {
		Node parent = sibling.parent;

		sibling.parent.color = c.BLACK; // in all cases I will make the parent black

		if (sibling.key < parent.key && sibling.hasALeftChild() && sibling.left.color == c.RED) // left case
		{
			sibling.left.color = c.BLACK;
			R(sibling);
		} else if (sibling.key < parent.key && sibling.hasARightChild() // LR case
				&& !sibling.hasALeftChild() && sibling.right.color == c.RED) {
			sibling.right.color = c.BLACK;
			LR(sibling.right);
		} else if (sibling.key > parent.key && sibling.hasARightChild() && sibling.right.color == c.RED) // R case
		{
			sibling.right.color = c.BLACK;
			L(sibling);
		} else if (sibling.key > parent.key && sibling.hasALeftChild() // RL case
				&& !sibling.hasARightChild() && sibling.left.color == c.RED) {
			sibling.left.color = c.BLACK;
			RL(sibling.left);
		}

	}

	private void case2b(Node sibling) {
		Node parent = sibling.parent;

		while (parent.color != c.RED) {
			sibling.color = c.RED;
			sibling = getSibling(parent);
			parent = parent.parent;

			if (parent == root) {
				break;
			} else if (sibling.color == c.RED) {
				case2c(sibling);
				return;
			} else if (sibling.color == c.BLACK && hasARedChild(sibling)) // case 2 a.
			{
				case2a(sibling);
				return;
			}

		}
		sibling.color = c.RED;
		parent.color = c.BLACK;

		/*
		 * there should not be two consecutive reds and the insertion and deletion
		 * should be handling all of this.
		 */
	}

	private void case2c(Node sibling) {
		Node parent = sibling.parent;
		if (sibling.key < parent.key) {
			R(sibling);
			sibling.color = parent.color;
			if (parent.hasALeftChild())
				parent.left.color = c.RED;

		} else if (sibling.key > parent.key) {
			L(sibling);
			sibling.color = parent.color; // should be black
			if (parent.hasARightChild())
				parent.right.color = c.RED;
		}
	}

	private Node getUncle(Node f) {
		Node p = f.parent;
		if (p == null)
			return null;
		else if (p.parent == null)
			return null;

		return p.key > p.parent.key ? p.parent.getLeft() : p.parent.getRight();
	}

	private Node getSibling(Node f) {
		return f.key > f.parent.key ? f.parent.getLeft() : f.parent.getRight();
	}

	private boolean hasARedChild(Node f) {
		if ((f.right != null && f.right.color == c.RED) || (f.left != null && f.left.color == c.RED))
			return true;
		return false;
	}

	private boolean hasOneChild(Node node) {
		if (node.left != null && node.right == null || node.right != null && node.left == null)
			return true;
		else
			return false;
	}

	public void preorder() {
		preorder(root);
	}

	private void preorder(Node r) {
		if (r != null) {
			System.out.print(r.key + " ");
			preorder(r.left);
			preorder(r.right);
		}
	}

	public void inorder() {
		inorder(root);
	}

	private void inorder(Node r) {
		if (r != null) {
			inorder(r.left);
			System.out.print(r.key + " ");
			inorder(r.right);
		}
	}

}
