/*
 * Name: Amiru Randil
 * UoW ID: W1761765
 * "I confirm that I understand what plagiarism / collusion / contract cheating is and have read and understood the
 * section on Assessment Offences in the Essential Information for Students. The work that I have submitted is entirely
 * my own. Any work from other authors is duly referenced and acknowledged."
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph {

        public static int[][] graph; // adjacency matrix
        public static int numberOfNodes;
        public Graph(String File) throws FileNotFoundException {
                Scanner scanner = new Scanner(new File(File));
                numberOfNodes = Integer.parseInt(scanner.next()); // Number of nodes in the graph
                graph = new int[numberOfNodes][numberOfNodes];  //creating graph that is each to the number of nodes
                ArrayList<String[]> allLineArr = new ArrayList<>();

                while (scanner.hasNext()) {
                        String[] lineArr = new String[3];
                        lineArr[0] = scanner.next();
                        lineArr[1] = scanner.next();
                        lineArr[2] = scanner.next();
                        allLineArr.add(lineArr);
                }

                for (int i = 0; i < allLineArr.size(); i++){    //accessing each row
                        String tempNode = allLineArr.get(i)[0];
                        for (int n = 0; n < allLineArr.size(); n++) {   //accessing each column
                                if (tempNode.equals(allLineArr.get(n)[0])) {
                                        int num1 = Integer.parseInt(allLineArr.get(n)[1]);
                                        graph[Integer.parseInt(tempNode)][num1] = Integer.parseInt(allLineArr.get(n)[2]);       //setting capacities for edges
                                }
                        }
                }
        }

        /* Returns true if there is a path from source 's' to
        sink 't' in residual graph. Also fills parent[] to
        store the path */
        boolean breadthFirstSearch(int[][] residualGraph, int s, int t, int[] parent)
        {
                boolean[] visited = new boolean[numberOfNodes]; // creating the visited array with the size of the number of nodes
                for (int i = 0; i < numberOfNodes; ++i) {
                        visited[i] = false;     //setting all the elements to false
                }
                // Create a queue, enqueue source node and mark
                // source node as visited
                LinkedList<Integer> queue = new LinkedList<Integer>();  //creating the queue linkedList
                queue.add(s);   //adding the s which is the sink node to the queue
                visited[s] = true;      //setting the node status of visited to true
                parent[s] = -1;

                // Standard BFS Loop
                while (queue.size() != 0) {
                        int node1 = queue.poll();
                        for (int node2 = 0; node2 < numberOfNodes; node2++) {
                                if (!visited[node2] && residualGraph[node1][node2] > 0) {
                                        // If we find a connection to the sink node, then there is no point in conducting
                                        // the Breadth first Search anymore We just have to set its parent and can return true
                                        if (node2 == t) {
                                                parent[node2] = node1;
                                                return true;
                                        }
                                        queue.add(node2);
                                        parent[node2] = node1;
                                        visited[node2] = true;
                                }
                        }
                }

                return false;   // if sink no reached in Breadth first Search starting from source,
        }


        int fordFulkersonAlgorithm(int[][] graph, int s, int t)         // To calculate the tne maximum flow from s to t in the given graph
        {
                int node1, node2;

                int[][] residualGraph = new int[numberOfNodes][numberOfNodes];  // Create a residual graph and fill the residual graph with the capacities in the original graph as residual capacities in residual graph
                // Residual graph where residualGraph[node1][node2] indicates residual capacity of edge from i to j
                // (if there is an edge. If residualGraph[node1][node2] is 0, then there is not)
                for (node1 = 0; node1 < numberOfNodes; node1++){
                        for (node2 = 0; node2 < numberOfNodes; node2++){
                                residualGraph[node1][node2] = graph[node1][node2];
                        }
                }

                int[] parent = new int[numberOfNodes];  // This array is filled by Breadth first Search and to store path

                int maximumFlow = 0; // Max flow is set to 0

                // Augment the flow while there is path from source to sink
                System.out.println("---------PATHS----------");
                while (breadthFirstSearch(residualGraph, s, t, parent)) {
                        // Find minimum residual capacity of the edges along the path filled by Breadth first Search
                        int pathFlow = Integer.MAX_VALUE;
                        ArrayList<Integer> path = new ArrayList<>();    //arraylist to store the path
                        for (node2 = t; node2 != s; node2 = parent[node2]) {
                                node1 = parent[node2];
                                pathFlow = Math.min(pathFlow, residualGraph[node1][node2]);
                                path.add(node1);
                        }
                        path.add(numberOfNodes -1);
                        Collections.sort(path);
                        for (Integer integer : path) { //displaying the path
                                System.out.print(integer + " ");
                        }
                        System.out.println();


                        for (node2 = t; node2 != s; node2 = parent[node2]) {    // update residual capacities of the edges and reverse edges along the path
                                node1 = parent[node2];
                                residualGraph[node1][node2] -= pathFlow;
                        }

                        maximumFlow += pathFlow;        // Add path flow to overall flow
                }
                System.out.println("------------------------");

                return maximumFlow;     // Return the overall flow
        }

        public static void main(String[] args) throws FileNotFoundException {
                long start = System.currentTimeMillis(); //start counting the time
                Graph graphObj = new Graph("testing.txt");
                System.out.println("The maximum possible flow is "
                        + graphObj.fordFulkersonAlgorithm(graph, 0, numberOfNodes -1));
                long stop = System.currentTimeMillis(); //stop counting hte time

                long timeTaken = stop - start;  //calculate the time taken

                System.out.println("Execution time in milliseconds: " + timeTaken);     //displaying the time taken
        }

}
