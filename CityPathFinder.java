import java.util.*;

public class CityPathFinder {

    // Edge representation: destination city and road distance
    static class Road {
        String destination;
        double distance;

        Road(String destination, double distance) {
            this.destination = destination;
            this.distance = distance;
        }
    }

    // Node for priority queue in Dijkstra
    static class NodeEntry implements Comparable<NodeEntry> {
        String city;
        double distance;

        NodeEntry(String city, double distance) {
            this.city = city;
            this.distance = distance;
        }

        @Override
        public int compareTo(NodeEntry other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    private final Map<String, List<Road>> adjList = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public boolean addCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            System.out.println("Error: City name cannot be empty.");
            return false;
        }
        city = city.trim();
        if (adjList.containsKey(city)) {
            System.out.println("City '" + city + "' already exists.");
            return false;
        }
        adjList.put(city, new ArrayList<>());
        System.out.println("-> City '" + city + "' added to network.");
        return true;
    }

    public boolean addRoad(String city1, String city2, double distance) {
        if (city1 == null || city2 == null || !adjList.containsKey(city1.trim()) || !adjList.containsKey(city2.trim())) {
            System.out.println("Error: One or both cities do not exist in the network.");
            return false;
        }
        city1 = city1.trim();
        city2 = city2.trim();

        if (city1.equalsIgnoreCase(city2)) {
            System.out.println("Error: Cannot add road between a city and itself.");
            return false;
        }
        if (distance <= 0) {
            System.out.println("Error: Distance must be greater than zero.");
            return false;
        }

        adjList.get(city1).add(new Road(city2, distance));
        adjList.get(city2).add(new Road(city1, distance));
        System.out.println("-> Road established: " + city1 + " <---> " + city2 + " (" + distance + " km)");
        return true;
    }

    public void findShortestPath(String startCity, String destinationCity) {
        if (startCity == null || destinationCity == null ||
            !adjList.containsKey(startCity.trim()) || !adjList.containsKey(destinationCity.trim())) {
            System.out.println("Error: Start or destination city does not exist.");
            return;
        }

        String start = startCity.trim();
        String dest = destinationCity.trim();

        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        for (String city : adjList.keySet()) {
            distances.put(city, Double.POSITIVE_INFINITY);
            previous.put(city, null);
        }

        PriorityQueue<NodeEntry> pq = new PriorityQueue<>();
        distances.put(start, 0.0);
        pq.add(new NodeEntry(start, 0.0));

        while (!pq.isEmpty()) {
            NodeEntry current = pq.poll();
            String u = current.city;

            if (current.distance > distances.get(u)) continue;
            if (u.equalsIgnoreCase(dest)) break;

            for (Road road : adjList.get(u)) {
                double alt = distances.get(u) + road.distance;
                if (alt < distances.get(road.destination)) {
                    distances.put(road.destination, alt);
                    previous.put(road.destination, u);
                    pq.add(new NodeEntry(road.destination, alt));
                }
            }
        }

        if (Double.isInfinite(distances.get(dest))) {
            System.out.println("\nNo viable route found connecting '" + start + "' and '" + dest + "'.");
            return;
        }

        // Reconstruct path
        List<String> path = new ArrayList<>();
        String curr = dest;
        while (curr != null) {
            path.add(curr);
            curr = previous.get(curr);
        }
        Collections.reverse(path);

        System.out.println("\n--------------------------------------------------");
        System.out.println("          SHORTEST ROUTE FOUND                    ");
        System.out.println("--------------------------------------------------");
        System.out.println("Optimal Path: " + String.join(" -> ", path));
        System.out.printf("Total Distance: %.2f km\n", distances.get(dest));
        System.out.println("--------------------------------------------------");
    }

    public void displayNetwork() {
        if (adjList.isEmpty()) {
            System.out.println("Network is empty. No cities added yet.");
            return;
        }
        System.out.println("\n==================================================");
        System.out.println("               CITY ROAD NETWORK                  ");
        System.out.println("==================================================");
        for (Map.Entry<String, List<Road>> entry : adjList.entrySet()) {
            System.out.print("• " + entry.getKey() + " -> ");
            if (entry.getValue().isEmpty()) {
                System.out.println("[No outgoing roads]");
            } else {
                List<String> roads = new ArrayList<>();
                for (Road r : entry.getValue()) {
                    roads.add(r.destination + " (" + r.distance + " km)");
                }
                System.out.println(String.join(", ", roads));
            }
        }
        System.out.println("==================================================\n");
    }

    public void loadDemoMap() {
        String[] cities = {"Delhi", "Jaipur", "Agra", "Kanpur", "Lucknow", "Varanasi", "Mumbai", "Pune"};
        for (String c : cities) {
            if (!adjList.containsKey(c)) {
                adjList.put(c, new ArrayList<>());
            }
        }
        addRoad("Delhi", "Jaipur", 280);
        addRoad("Delhi", "Agra", 240);
        addRoad("Jaipur", "Agra", 240);
        addRoad("Agra", "Kanpur", 278);
        addRoad("Kanpur", "Lucknow", 90);
        addRoad("Lucknow", "Varanasi", 310);
        addRoad("Kanpur", "Varanasi", 330);
        addRoad("Delhi", "Mumbai", 1400);
        addRoad("Mumbai", "Pune", 150);
        addRoad("Jaipur", "Mumbai", 1150);
        System.out.println("-> Demo road network loaded!");
    }

    public static void main(String[] args) {
        CityPathFinder finder = new CityPathFinder();
        Scanner scanner = new Scanner(System.in);

        System.out.println("==================================================");
        System.out.println("     CITY PATH FINDER (Dijkstra's Algorithm)     ");
        System.out.println("==================================================");

        while (true) {
            System.out.println("\nOperations:");
            System.out.println("1. Find Shortest Route between Two Cities");
            System.out.println("2. Add a New City (Node)");
            System.out.println("3. Connect Road between Cities (Edge)");
            System.out.println("4. View Entire City Network");
            System.out.println("5. Load Demo Map");
            System.out.println("6. Exit");
            System.out.print("Enter choice (1-6): ");

            if (!scanner.hasNextLine()) break;
            String choice = scanner.nextLine().trim();

            if (choice.equals("6") || choice.equalsIgnoreCase("exit") || choice.equalsIgnoreCase("quit")) {
                System.out.println("Exiting City Path Finder. Safe travels!");
                break;
            }

            switch (choice) {
                case "1":
                    System.out.print("Enter Starting City: ");
                    String start = scanner.nextLine().trim();
                    System.out.print("Enter Destination City: ");
                    String dest = scanner.nextLine().trim();
                    finder.findShortestPath(start, dest);
                    break;
                case "2":
                    System.out.print("Enter City Name: ");
                    String city = scanner.nextLine().trim();
                    finder.addCity(city);
                    break;
                case "3":
                    System.out.print("Enter First City: ");
                    String c1 = scanner.nextLine().trim();
                    System.out.print("Enter Second City: ");
                    String c2 = scanner.nextLine().trim();
                    System.out.print("Enter Distance (in km): ");
                    try {
                        double d = Double.parseDouble(scanner.nextLine().trim());
                        finder.addRoad(c1, c2, d);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid distance number!");
                    }
                    break;
                case "4":
                    finder.displayNetwork();
                    break;
                case "5":
                    finder.loadDemoMap();
                    finder.displayNetwork();
                    break;
                default:
                    System.out.println("Invalid choice. Please select between 1 and 6.");
                    break;
            }
        }
        scanner.close();
    }
}
