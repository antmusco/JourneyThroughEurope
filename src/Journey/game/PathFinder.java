/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Journey.game;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Anthony
 */
public class PathFinder {
    
    /* Hashmap of visited cities. */
    public static HashMap<String, CityNode> visitedCities;
    
    public static ArrayList<CityNode> findShortestPath(CityNode origin, 
      ArrayList<CityNode> destinations) {
        
        if(origin == null || destinations == null || destinations.isEmpty())
            return null;
        
        int size = destinations.size();
        ArrayList<CityNode>[][] paths = new ArrayList[size][size];
        int i = 0;
        for(CityNode start : destinations) {
            int j = 0;
            for(CityNode dest : destinations) {
                if(dest != start)
                    paths[i][j++] = pathHelper(start, dest);
                else
                    paths[i][j++] = null;
            }
            i++;
        }
        
        return djikstrasAlgorithm(paths, destinations);
        
    }
    
    private static ArrayList<CityNode> pathHelper(CityNode origin, CityNode dest) {
        
        /* Declare queue. */
        ArrayList<CityNode> queue = new ArrayList<>();
        origin.prev = null;
        visitedCities = new HashMap<>(180);
        visitedCities.put(origin.getName(), origin);
        queue.add(origin);
        while(!queue.isEmpty()) {
            
            if(queue.get(0) == dest)
                return buildPath(dest);
            
            CityNode cursor = queue.remove(0);
            for(Edge e : cursor.getEdges()) {
                
                CityNode city = e.getNeighbor();
                
                if(city != null && !visitedCities.containsValue(e.getNeighbor())) {              
                    e.getNeighbor().prev = cursor;
                    visitedCities.put(e.getNeighborName(), e.getNeighbor());
                    queue.add(e.getNeighbor());
                }
            }
        }
        
        return null;
        
    }
    
    private static ArrayList<CityNode> buildPath(CityNode dest) {
        
        ArrayList<CityNode> path = new ArrayList<>();
        while(dest != null) {
            
            path.add(dest);
            String destName = (dest.prev == null) ? null : dest.prev.getName();
            dest = dest.prev;
            
        }
        
        return path;
        
    }

    private static ArrayList<CityNode> djikstrasAlgorithm(ArrayList<CityNode>[][] paths, ArrayList<CityNode> dest) {
    
        ArrayList<Integer> selectedCities = new ArrayList<>();
        ArrayList<CityNode> path = new ArrayList<>();
        
        int i = 0, count = 0;
        
        /* For each city in the path matrix, find the shortest path. */
        while(count < paths.length) {
            
            int indexOfShortestRoute = -1;
            for(int j = 1; j < paths[i].length; j++) {
                
                /* Path is null or already included, skip. */
                if(paths[i][j] == null || selectedCities.contains(j))
                    continue;
                
                if(indexOfShortestRoute == -1)
                    indexOfShortestRoute = j;
                else if(paths[i][j].size() < paths[i][indexOfShortestRoute].size())
                    indexOfShortestRoute = j;
                
            }
            if (indexOfShortestRoute < 0) break;
            ArrayList<CityNode> toCity = paths[i][indexOfShortestRoute];
            for(int k = (toCity.size() - 1); k >= 0; k--) {

                if(path.size() == 0 || toCity.get(k) != path.get(path.size() - 1))
                    path.add(toCity.get(k));
                
                
            }
            selectedCities.add(indexOfShortestRoute);
            i = indexOfShortestRoute;
            count++;
            
        }
        ArrayList<CityNode> toCity = paths[i][0];
        for(int k = (toCity.size() - 1); k >= 0; k--) {

            if(path.size() == 0 || toCity.get(k) != path.get(path.size() - 1))
                path.add(toCity.get(k));


        }
        path.remove(0);
        String isDestination;
        for(int t = 0; t < path.size(); t++) {
            isDestination = (dest.contains(path.get(t))) ? " -> CARD" : "";
        }
        return path;
        
    }
}
