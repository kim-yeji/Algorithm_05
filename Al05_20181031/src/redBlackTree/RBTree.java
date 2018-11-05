package redBlackTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RBTree {

	private final int RED = 0;
	private final int BLACK = 1;

	private class Node {

		int data = -1, color = BLACK;
		Node left = nil, right = nil, parent = nil;

		Node(int key) {
			this.data = key;
		}
	}

	private final Node nil = new Node(-1);
	private Node root = nil;

	public Node getRoot() {
		return this.root;
	}

	public void printTree(Node node, int depth) {
		if (node == null || node.data ==-1)
			return;
		for (int i = 0; i < depth; i++) {
			System.out.print("\t");
		}
		if (node.color == RED) {
			System.out.println(node.data + "R");
		} else {
			System.out.println(node.data + "B");
		}
		printTree(node.left, depth + 1);
		printTree(node.right, depth + 1);
	}


	private void insert(Node node) {
		Node temp = root; 
		if (root == nil) { //맨 처음 생성할 때(루트는 블랙)
			root = node;
			node.color = BLACK;
			node.parent = nil;
		} else {
			node.color = RED; //처음이 아닌 노드는 레드로 삽입됨
			while (true) {
				if (node.data < temp.data) { //방금 삽입 된 data < temp(root) data
					if (temp.left == nil) { //temp에게 왼쪽자식이 없다면
						temp.left = node; //삽입된 노드를 부모 왼쪽에 넣고
						node.parent = temp;//삽입된 노드의 부모를 temp로 설정
						break;
					} else {
						temp = temp.left;//부모에게 왼쪽 자식이 있다면 그 자식을 부모(루트)로 설정->반복
					}
				} else if (node.data >= temp.data) { //방금 삽입 된 data > 부모data
					if (temp.right == nil) { //temp에게 오른쪽자식이 없다면
						temp.right = node; //삽입된 노드를 temp 오른쪽에 넣고
						node.parent = temp; //삽입된 노드의 부모를 temp로 설정
						break;
					} else {
						temp = temp.right; //부모에게 오른쪽자식이 있다면 그 자식을 부모(루트)로 설정
					}
				}
			}
			fixTree(node); //삽입된 노드를 정리해주자
		}
	}

	private void fixTree(Node node) {
		while (node.parent.color == RED) {
			Node uncle = nil;
			if (node.parent == node.parent.parent.left) { //노드의 부모가 조부모의 왼쪽자식이면
				uncle = node.parent.parent.right; //삼촌은 조부모의 오른쪽에 있는 노드다

				if (uncle != nil && uncle.color == RED) { //삼촌이 빨간색으로 존재하면
					node.parent.color = BLACK; //부모는 블랙
					uncle.color = BLACK; //삼촌도 블랙
					node.parent.parent.color = RED; //조부모는 빨강
					node = node.parent.parent; //조부모를 노드로 설정
					continue;
				}
				if (node == node.parent.right) { //노드가 오른쪽 자식이면
					// Double rotation needed
					node = node.parent; //부모를 노드로 설정하고 왼쪽으로 회전
					rotateLeft(node);
				}
				node.parent.color = BLACK;
				node.parent.parent.color = RED;
				// if the "else if" code hasn't executed, this
				// is a case where we only need a single rotation
				rotateRight(node.parent.parent);
			} else { //노드의 부모가 조부모의 오른쪽자식이면 삼촌은 조부모의 왼쪽자식이다
				uncle = node.parent.parent.left;
				if (uncle != nil && uncle.color == RED) { //삼촌이 존재하고 빨간색이면
					node.parent.color = BLACK; //부모는 검정색 (recoloring)
					uncle.color = BLACK; //삼촌도 검정색 (recoloring)
					node.parent.parent.color = RED; //조부모는 빨간색
					node = node.parent.parent; //노드를 조부모로 설정
					continue;
				}
				if (node == node.parent.left) { //노드가 왼쪽 자식이면
					// Double rotation needed
					node = node.parent;
					rotateRight(node); //오른쪽으로 돌림
				}
				node.parent.color = BLACK; //노드의 부모를 블랙으로 설정
				node.parent.parent.color = RED; //조부모를 레드로 설정
				// "else if" 코드가 실행되지 않은 경우는 한 번만 회전하면 되는 경우
				rotateLeft(node.parent.parent); 
			}
		}
		root.color = BLACK;
	}

	void rotateLeft(Node node) {
		if (node.parent != nil) { //최상위 노드가 아니라면
			if (node == node.parent.left) { //노드가 부모의 왼쪽 자식이라면
				node.parent.left = node.right; //노드의 오른쪽 자식을 자기 자리로 설정
			} else {
				node.parent.right = node.right;
			}
			node.right.parent = node.parent; //노드 부모를 노드 자식의 부모로
			node.parent = node.right; //자기랑 자기 자식 위치 바꿈
			if (node.right.left != nil) { //노드의 오른쪽 자식의 왼쪽자식이 존재하면
				node.right.left.parent = node; //걔의 부모가 된다
			}
			node.right = node.right.left; //노드의 오른쪽의 왼쪽자식을 오른쪽자식으로
			node.parent.left = node; //그리고 왼쪽에 자신이 들어감
		} else {//최상위 노드일 때는 루트를 중심으로 설정
			Node right = root.right;
			root.right = right.left;
			right.left.parent = root;
			root.parent = right;
			right.left = root;
			right.parent = nil;
			root = right;
		}
	}

	void rotateRight(Node node) {
		if (node.parent != nil) {
			if (node == node.parent.left) {
				node.parent.left = node.left;
			} else {
				node.parent.right = node.left;
			}

			node.left.parent = node.parent;
			node.parent = node.left;
			if (node.left.right != nil) {
				node.left.right.parent = node;
			}
			node.left = node.left.right;
			node.parent.right = node;
		} else {// Need to rotate root
			Node left = root.left;
			root.left = root.left.right;
			left.right.parent = root;
			root.parent = left;
			left.right = root;
			left.parent = nil;
			root = left;
		}
	}

	public void consoleUI() throws IOException {
		Node node;

		BufferedReader br = new BufferedReader(new FileReader("C:/Users/Administrator/Desktop/[Al]03/Data1.txt"));

		for (int i = 0; i < 24; i++) {
			String line = br.readLine();
			if (line == null) {
				break;
			} else {
				node = new Node(Integer.parseInt(line));
				insert(node);
			}
		}
	}

	public static void main(String[] args) throws IOException {

		RBTree rbt = new RBTree();
		rbt.consoleUI();
		rbt.printTree(rbt.getRoot(), 0);
	}
}
