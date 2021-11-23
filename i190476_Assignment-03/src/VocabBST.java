public class VocabBST {

    public class Node {
        public String data;
        public Node left;
        public Node right;

        public Node(String data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    public Node root;

    public VocabBST() {
        this.root = null;
    }

    public void insert(String data) {
        this.root = insert(root, data);
    }

    public Node insert(Node root, String data) {
        if (root == null) {
            root = new Node(data);
            return root;
        }
        else if (root.data.compareTo(data) > 0) {
            root.left = insert(root.left, data);
        }
        else {
            root.right = insert(root.right, data);
        }
        return root;
    }

    public boolean search(String data) {
        return search(this.root, data);
    }

    private boolean search(Node root, String data) {
        if (root == null) {
            return false;
        } else if (root.data.compareTo(data) == 0) {
            return true;
        } else if (root.data.compareTo(data) > 0) {
            return search(root.left, data);
        }
        return search(root.right, data);
    }

    public void display() {
        preorder(root);
        System.out.println();
    }

    private void preorder(Node root) {
        if (root == null) {
            return;
        }
        System.out.println(root.data);
        preorder(root.left);
        preorder(root.right);
    }
}