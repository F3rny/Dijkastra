import java.util.*;
import java.lang.Comparable;
class Vertex {
    public String label;

    public Vertex(String vertexLabel) {
        label = vertexLabel;
    }

}
class Edge{
    public Vertex fromVertex;
    public Vertex toVertex;
    public double weight;

    public Edge(Vertex fromVertex, Vertex toVertex, double weight){
        this.fromVertex = fromVertex;
        this.toVertex = toVertex;
        this.weight = weight;
    }
}

class Graphs_Ep{
    // Maps a vertex to an ArrayList of all edges that start from that vertex
    private HashMap<Vertex, ArrayList<Edge>> fromEdges;
    
    // Maps a vertex to an ArrayList of all edges that go to that vertex
    private HashMap<Vertex, ArrayList<Edge>> toEdges;

    public Graphs_Ep() {
        fromEdges = new HashMap<Vertex,ArrayList<Edge>>();
        toEdges = new HashMap<Vertex, ArrayList<Edge>>();

    }

    public Vertex addVertex(String newVertexLabel){
        Vertex newVertex = new Vertex(newVertexLabel);

        fromEdges.put(newVertex, new ArrayList<Edge>());
        toEdges.put(newVertex, new ArrayList<Edge>());

        return newVertex;
    }
    public boolean hasEdge(Vertex fromVertex, Vertex toVertex){
        if(!fromEdges.containsKey(fromVertex)){
            return false;
        }

        ArrayList<Edge> edges = fromEdges.get(fromVertex);
        for(Edge edge : edges){
            if(edge.toVertex == toVertex){
                return true;
            }
            
        }
        return false;
    }
    public Edge addDirectedEdge(Vertex fromVertex, Vertex toVertex){
        // 1.0 as the default edge weight
        return addDirectedEdge(fromVertex, toVertex, 1.0);

    }
    public Edge addDirectedEdge(Vertex fromVertex, Vertex toVertex, double weight) {
        // Don't add the same edge twice
        if (hasEdge(fromVertex, toVertex)) {
           return null;
        }
        
        // The Edge object
        Edge newEdge = new Edge(fromVertex, toVertex, weight);
        
        // Add the edge to the appropriate list in both maps
        fromEdges.get(fromVertex).add(newEdge);
        toEdges.get(toVertex).add(newEdge);
        
        return newEdge;
     }


    public Edge[] addUndirectedEdges(Vertex vertexA, Vertex vertexB){
        // 1.0 as the default edge weight
        return addUndirectedEdges(vertexA, vertexB, 1.0);
    }
    public Edge[] addUndirectedEdges(Vertex vertexA, Vertex vertexB, double weight){
        Edge edge1 = addDirectedEdge(vertexA, vertexB, weight);
        Edge edge2 = addDirectedEdge(vertexA, vertexB, weight);

        Edge[] result = {edge1, edge2};
        return result;
    }
    public Collection<Edge> getEdges(){
        HashSet<Edge> edges = new HashSet<Edge>();
        for(ArrayList<Edge> edgeList : fromEdges.values()){
            edges.addAll(edgeList);
        }

        return edges;       
    }

    public Collection<Edge> getEdgesFrom(Vertex fromVertex){
        return fromEdges.get(fromVertex);
    }
    public Collection<Edge> getEdgesTo(Vertex toVertex){
        return toEdges.get(toVertex);
    }

    public Vertex getVertex(String vertexLabel){
        for(Vertex vertex : getVertices()){
            if(vertex.label.equals(vertexLabel)){
                return vertex;
            }
        }
        return null;
    }

    public Collection<Vertex> getVertices() {
        return fromEdges.keySet();
    }

}

class PathVertexInfo implements Comparable<PathVertexInfo>{
    public Vertex vertex;
    public double distance;
    public Vertex predecessor;

    public PathVertexInfo(Vertex vertex) {
        this.vertex = vertex;
        distance = Double.POSITIVE_INFINITY;
        predecessor = null;

    }
    public int compareTo(PathVertexInfo other){
        if(distance > other.distance){
            return 1;
        }
        else if(distance < other.distance){
            return -1;

        }
        return 0;
    }
}

class DijkstrasMethod {
    public static HashMap<Vertex, PathVertexInfo> dijkstraSP(
        Graphs_Ep graph, Vertex startVertex){
            // Create the HashMap for vertex information
            HashMap<Vertex, PathVertexInfo> info = new HashMap<Vertex, PathVertexInfo>();

            // Put all graph vertices in both the info HashMap and the PriorityQueue 
            // of unvisited vertices
            PriorityQueue<PathVertexInfo> unvisited = new PriorityQueue<PathVertexInfo>();

            for(Vertex vertex : graph.getVertices()) {
                PathVertexInfo vertexInfo = new PathVertexInfo(vertex);
                unvisited.add(vertexInfo);
                info.put(vertex, vertexInfo);
            }
            // startVertex has a distance of 0 from itself
            info.get(startVertex).distance = 0.0;

            // Iterate through all vertices in the priority queue
            while(unvisited.size() > 0){
                 // Get info about the vertex with the shortest distance from startVertex
                PathVertexInfo currentInfo = unvisited.peek();
                unvisited.remove();

                // Check potential path lengths from the current vertex to all neighbors
                for(Edge edge : graph.getEdgesFrom(currentInfo.vertex)) {
                    Vertex adjVertex = edge.toVertex;
                    double altPathDistance = currentInfo.distance + edge.weight;

                    // If a shorter path from startVertex to adjVertex is found,
                    // update adjacentVertex's distance and predecessor
                    PathVertexInfo adjInfo = info.get(adjVertex);
                    if(altPathDistance < adjInfo.distance){

                        unvisited.remove(adjInfo);
                        adjInfo.distance = altPathDistance;
                        adjInfo.predecessor = currentInfo.vertex;
                        unvisited.add(adjInfo);

                    }
                }
            }
            return info;
        }
    public static String getShortestPath(Vertex start, Vertex end, HashMap<Vertex, PathVertexInfo> infoMap ){
        // Start from endVertex and build the path in reverse.
        String path = "";
        Vertex currentVertex = end;
        while(currentVertex != start){
            path = " --> " + currentVertex.label + path;
            currentVertex = infoMap.get(currentVertex).predecessor;

        }
        path = start.label + path;
        return path;
    }

    public static void main(String[] args){
        Graphs_Ep gph = new Graphs_Ep();

        Vertex vertex1 = gph.addVertex("EPCC RIO GRANDE");
        Vertex vertex2 = gph.addVertex("UTEP CS BUILDING");
        Vertex vertex3 = gph.addVertex("EPCC NORTHWEST");
        Vertex vertex4 = gph.addVertex("EPCC TRANSMOUNTAIN");
        Vertex vertex5 = gph.addVertex("EPCC FORTBLISS");
        Vertex vertex6 = gph.addVertex("EPCC VALLE VERDE");
        Vertex vertex7 = gph.addVertex("EPCC MISSION DEL PASO");

        gph.addUndirectedEdges(vertex1, vertex2, 0.54);
        gph.addUndirectedEdges(vertex1, vertex5, 7.29);
        
        gph.addUndirectedEdges(vertex2, vertex3, 10.31);
        gph.addUndirectedEdges(vertex2, vertex5, 7.80);

        gph.addUndirectedEdges(vertex3, vertex5, 16.91);
        gph.addUndirectedEdges(vertex3, vertex4, 8.62);

        gph.addUndirectedEdges(vertex4, vertex5, 5.74);
        gph.addUndirectedEdges(vertex4, vertex7, 17.75);

        gph.addUndirectedEdges(vertex5, vertex6, 4.90);
        gph.addUndirectedEdges(vertex5, vertex7, 13.07);

        gph.addUndirectedEdges(vertex6, vertex7, 8.18);

        HashMap<Vertex, PathVertexInfo> infoMap = dijkstraSP(gph, vertex1);

        Vertex[] vertices = {vertex1, vertex2, vertex3, vertex4, vertex5, vertex6, vertex7};
        
        Scanner scr = new Scanner(System.in);
        
        System.out.println("Welcome to the Drone Path Simulator \n");
        
        while(true){
        System.out.println("Here is the list of the drone destinations...");
        System.out.println("\t 1) EPCC RIO GRANDE");
        System.out.println("\t 2) UTEP CS BUILDING");
        System.out.println("\t 3) EPCC NORTHWEST");
        System.out.println("\t 4) EPCC TRANSMOUNTAIN");
        System.out.println("\t 5) EPCC FORTBLISS");
        System.out.println("\t 6) EPCC VALLE VERDE");
        System.out.println("\t 7) EPCC MISSION DEL PASO");
        System.out.println("Enter -1 to exit the program \n");
        
        System.out.println("please enter your number selection of the starting point:");
        
        int start = scr.nextInt();
        if(start == -1){
            System.out.println("Thank you for using the Drone Path Simulator! ");
            break;
        }
        
        else if(start < 1 || start > 7){
            System.out.println("\nWARNING\n" + "Not a valid option, please try again. \n");
            
            
        }
        else{
            System.out.println("please enter your number selection of the ending point:");
        
            int end = scr.nextInt();
            
            if(end == -1){
                System.out.println("Thank you for using the Drone Path Simulator! ");
                break;

            }
            else if(end < 1 || end > 7){
                System.out.println("\nWARNING\n" + "Not a valid option, please try again.\n");
            
            }
            else{
                System.out.println("\nThe shortest path from " + vertices[start-1].label + " to "
                             + vertices[end-1].label +" will be: " + getShortestPath(vertices[start-1], vertices[end-1], infoMap) + "\n");
        
                PathVertexInfo info = infoMap.get(vertex7);
        
                System.out.println("total distance: " + info.distance + " mi \n");
                
                }
            }
        }
        scr.close();
    }
}
