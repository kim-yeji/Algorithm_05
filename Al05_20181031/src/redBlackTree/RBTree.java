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
		if (root == nil) { //�� ó�� ������ ��(��Ʈ�� ��)
			root = node;
			node.color = BLACK;
			node.parent = nil;
		} else {
			node.color = RED; //ó���� �ƴ� ���� ����� ���Ե�
			while (true) {
				if (node.data < temp.data) { //��� ���� �� data < temp(root) data
					if (temp.left == nil) { //temp���� �����ڽ��� ���ٸ�
						temp.left = node; //���Ե� ��带 �θ� ���ʿ� �ְ�
						node.parent = temp;//���Ե� ����� �θ� temp�� ����
						break;
					} else {
						temp = temp.left;//�θ𿡰� ���� �ڽ��� �ִٸ� �� �ڽ��� �θ�(��Ʈ)�� ����->�ݺ�
					}
				} else if (node.data >= temp.data) { //��� ���� �� data > �θ�data
					if (temp.right == nil) { //temp���� �������ڽ��� ���ٸ�
						temp.right = node; //���Ե� ��带 temp �����ʿ� �ְ�
						node.parent = temp; //���Ե� ����� �θ� temp�� ����
						break;
					} else {
						temp = temp.right; //�θ𿡰� �������ڽ��� �ִٸ� �� �ڽ��� �θ�(��Ʈ)�� ����
					}
				}
			}
			fixTree(node); //���Ե� ��带 ����������
		}
	}

	private void fixTree(Node node) {
		while (node.parent.color == RED) {
			Node uncle = nil;
			if (node.parent == node.parent.parent.left) { //����� �θ� ���θ��� �����ڽ��̸�
				uncle = node.parent.parent.right; //������ ���θ��� �����ʿ� �ִ� ����

				if (uncle != nil && uncle.color == RED) { //������ ���������� �����ϸ�
					node.parent.color = BLACK; //�θ�� ��
					uncle.color = BLACK; //���̵� ��
					node.parent.parent.color = RED; //���θ�� ����
					node = node.parent.parent; //���θ� ���� ����
					continue;
				}
				if (node == node.parent.right) { //��尡 ������ �ڽ��̸�
					// Double rotation needed
					node = node.parent; //�θ� ���� �����ϰ� �������� ȸ��
					rotateLeft(node);
				}
				node.parent.color = BLACK;
				node.parent.parent.color = RED;
				// if the "else if" code hasn't executed, this
				// is a case where we only need a single rotation
				rotateRight(node.parent.parent);
			} else { //����� �θ� ���θ��� �������ڽ��̸� ������ ���θ��� �����ڽ��̴�
				uncle = node.parent.parent.left;
				if (uncle != nil && uncle.color == RED) { //������ �����ϰ� �������̸�
					node.parent.color = BLACK; //�θ�� ������ (recoloring)
					uncle.color = BLACK; //���̵� ������ (recoloring)
					node.parent.parent.color = RED; //���θ�� ������
					node = node.parent.parent; //��带 ���θ�� ����
					continue;
				}
				if (node == node.parent.left) { //��尡 ���� �ڽ��̸�
					// Double rotation needed
					node = node.parent;
					rotateRight(node); //���������� ����
				}
				node.parent.color = BLACK; //����� �θ� ������ ����
				node.parent.parent.color = RED; //���θ� ����� ����
				// "else if" �ڵ尡 ������� ���� ���� �� ���� ȸ���ϸ� �Ǵ� ���
				rotateLeft(node.parent.parent); 
			}
		}
		root.color = BLACK;
	}

	void rotateLeft(Node node) {
		if (node.parent != nil) { //�ֻ��� ��尡 �ƴ϶��
			if (node == node.parent.left) { //��尡 �θ��� ���� �ڽ��̶��
				node.parent.left = node.right; //����� ������ �ڽ��� �ڱ� �ڸ��� ����
			} else {
				node.parent.right = node.right;
			}
			node.right.parent = node.parent; //��� �θ� ��� �ڽ��� �θ��
			node.parent = node.right; //�ڱ�� �ڱ� �ڽ� ��ġ �ٲ�
			if (node.right.left != nil) { //����� ������ �ڽ��� �����ڽ��� �����ϸ�
				node.right.left.parent = node; //���� �θ� �ȴ�
			}
			node.right = node.right.left; //����� �������� �����ڽ��� �������ڽ�����
			node.parent.left = node; //�׸��� ���ʿ� �ڽ��� ��
		} else {//�ֻ��� ����� ���� ��Ʈ�� �߽����� ����
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
