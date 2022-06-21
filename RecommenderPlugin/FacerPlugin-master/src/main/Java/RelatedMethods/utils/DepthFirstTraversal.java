package RelatedMethods.utils;

import java.util.ArrayList;
import java.util.List;
 
public class DepthFirstTraversal
{ 
 
	public static class Node
	{
		public int value;
		public boolean isVisited;
		public boolean isSink;
		
		public List<Node> neighbours;
 
		public Node(int data)
		{
			this.value=data;
			this.neighbours=new ArrayList<>();
 
		}
		
		public Node(int value, boolean isSink)
		{
			this.value = value;
			this.isSink = isSink;
			this.isVisited = false;
			
		}
		
		public void addneighbours(Node neighbourNode)
		{
			this.neighbours.add(neighbourNode);
		}
		public List<Node> getNeighbours() {
			return neighbours;
		}
		public void setNeighbours(List<Node> neighbours) {
			this.neighbours = neighbours;
		}
	}
 
	// Recursive DFS
	public  void dfs(Node node)
	{
		System.out.print(node.value + " ");		
        node.isVisited=true;
        
        List<Node> neighbours=node.getNeighbours();
        //if(neighbours.size() == 0)
        	//System.out.println("Root: " + node.data);
		for (int i = 0; i < neighbours.size(); i++) {
			Node n=neighbours.get(i);
			if(n!=null && !n.isVisited)
			{
				dfs(n);
			}
		}
	}
 
	public static void main(String arg[])
	{
 
		Node node40 =new Node(40);
		Node node10 =new Node(10);
		Node node20 =new Node(20);
		Node node30 =new Node(30);
		Node node60 =new Node(60);
		Node node50 =new Node(50);
		Node node70 =new Node(70);
 
		node40.addneighbours(node10);
		node40.addneighbours(node20);
		node10.addneighbours(node30);
		//node20.addneighbours(node10);
		//node20.addneighbours(node30);
		node20.addneighbours(node60);
		node20.addneighbours(node50);
		node30.addneighbours(node70);
		//node60.addneighbours(node70);
		//node50.addneighbours(node70);
 
		DepthFirstTraversal dfsExample = new DepthFirstTraversal();
	
 
 
		System.out.println("The DFS traversal of the graph using recursion ");
		dfsExample.dfs(node40);
	}
}