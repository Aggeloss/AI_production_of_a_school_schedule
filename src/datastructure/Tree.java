package datastructure;
class T_Node< T extends Comparable<T> > {
		
	T_Node< T > leftNode;
	T_Node< T > rightNode;
	T data;
	
	public T_Node( T nodeData ) {
		data = nodeData;
		leftNode = rightNode = null;
	}
	
	public void insert( T insertValue ) {
		
		if ( insertValue.compareTo( data ) < 0 ) {
			if( leftNode == null )
				leftNode = new T_Node< T >( insertValue );
			else
				leftNode.insert( insertValue );
		}
		else if ( insertValue.compareTo( data ) < 0 ) {
			if( rightNode == null )
				rightNode = new T_Node< T >( insertValue );
			else
				rightNode.insert( insertValue );
		}
	}
}

public class Tree< T extends Comparable< T >> {
	
	private T_Node< T > root;
	
	public Tree() {
		root = null;
	}
	
	public void insertNode( T insertValue ) {
		if( root == null )
			root = new T_Node< T >( insertValue );
		else
			root.insert( insertValue );
	}
	
	public void preorderTraversal() {
		preorderHelper( root );
	}
	
	private void preorderHelper(T_Node< T > node) {
		if( node == null )
			return;
		System.out.printf("%s ", node.data);
		preorderHelper(node.leftNode);
		preorderHelper(node.rightNode);
	}
}