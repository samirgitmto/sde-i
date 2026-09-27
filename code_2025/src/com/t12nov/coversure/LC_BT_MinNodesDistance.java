package com.t12nov.coversure;

/**
 * Minimum distance between 2 nodes
 * 
 * @since 29-11-2025
 */
public class LC_BT_MinNodesDistance {

	/**
	 * Min Distance = Sum of node1 & node2 distances from root - (2 * LCA)
	 * LCA - least common ancestor of both nodes
	 * Complexity: Time		Space
	 * @param root
	 * @param node1
	 * @param node2
	 * @return
	 */
	static int minDistance(Node root, Node node1, Node node2) {
		
		int minDistance = 0;
		
		int node1Level = getDistanceFromRoot(root, node1, 0);
		int node2Level = getDistanceFromRoot(root, node2, 0);
		
		Node lCANode = getLCANode(root, node1, node2);
		
		int lcaNodeDistanceFromRoot = getDistanceFromRoot(root, lCANode, 0);
		
		minDistance = (node1Level + node2Level) - (2 * lcaNodeDistanceFromRoot);
		return minDistance;
	}
	/**
	 * DFS
	 * Complexity: Time - O(n);		Space - O(h): Balanced tree → O(log N), Skewed tree → O(N)
	 * @param root
	 * @param node1
	 * @param node2
	 * @return LCA Node
	 */
	private static Node getLCANode(Node root, Node node1, Node node2) {
		if (root == null)	return null;
		if (root == node1 || root == node2)	return root;
		
		Node left = getLCANode(root.left, node1, node2);
		Node right = getLCANode(root.right, node1, node2);
		
		if (left != null && right != null)	return root;
				
		return left != null ? left : right;
	}
	/**
	 * DFS
	 * Complexity: Time - O(n);	Space - O(h): Balanced tree → O(log N), Skewed tree → O(N)
	 * @param root
	 * @param node1
	 * @return distance
	 */
	private static int getDistanceFromRoot(Node root, Node node1, int level) {
		if (root == null)	return -1;
		if (root == node1)	return level;
		
		int leftLevel = getDistanceFromRoot(root.left, node1, level + 1);
		
		if (leftLevel != -1)
			return leftLevel;
		else
			return getDistanceFromRoot(root.right, node1, level + 1);
		
	}
	
	public static void main(String[] args) {
		Node root = new Node(3);
		root.left = new Node(2);
		root.left.left = new Node(4);
		root.left.right = new Node(5);
		Node node1 = root.left.right;
		root.right = new Node(1);
		Node node2 = root.right;
		System.out.println(getDistanceFromRoot(root, node1, 0));
		System.out.println(getDistanceFromRoot(root.left, node1, 0));
		System.out.println(getDistanceFromRoot(root.right, node1, 0));
		
		System.out.println(getLCANode(root, root.left.left, root.left.right).val);
		System.out.println(minDistance(root, node1, node2));
	}
	
	static class Node {
		int val;
		Node left, right;
		public Node(int x) {
			this.val = x;
		}
	}
}