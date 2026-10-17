"""
City Path Finder
----------------
Features:
- Represents cities as nodes and roads as weighted edges in a graph.
- Dijkstra's Algorithm to find the shortest route and minimum travel distance/cost between any two cities.
- Add cities (nodes), connect roads (weighted edges), view roadmap network, and find shortest routes.
- Includes preloaded demo Indian cities network for quick testing.
"""

import heapq
from typing import Dict, List, Optional, Tuple

class CityGraph:
    def __init__(self):
        # Adjacency list: city_name -> list of (neighbor_city, distance)
        self.adj_list: Dict[str, List[Tuple[str, float]]] = {}

    def add_city(self, city: str) -> bool:
        """Adds a city node to the graph."""
        city = city.strip()
        if not city:
            print("Error: City name cannot be empty.")
            return False
        
        # Check existing (case-insensitive)
        for c in self.adj_list:
            if c.lower() == city.lower():
                print(f"City '{c}' already exists.")
                return False

        self.adj_list[city] = []
        print(f"-> City '{city}' added to network.")
        return True

    def _get_canonical_name(self, city: str) -> Optional[str]:
        """Returns the canonical city name matching case-insensitively."""
        city_lower = city.strip().lower()
        for c in self.adj_list:
            if c.lower() == city_lower:
                return c
        return None

    def add_road(self, city1: str, city2: str, distance: float, bidirectional: bool = True) -> bool:
        """Adds a weighted road (edge) between two cities."""
        c1 = self._get_canonical_name(city1)
        c2 = self._get_canonical_name(city2)

        if not c1 or not c2:
            missing = []
            if not c1: missing.append(city1)
            if not c2: missing.append(city2)
            print(f"Error: City/cities not found: {', '.join(missing)}")
            return False

        if c1 == c2:
            print("Error: Cannot add road between a city and itself.")
            return False

        if distance <= 0:
            print("Error: Distance must be a positive number.")
            return False

        # Check if road already exists, if so update distance
        updated = False
        for i, (neighbor, dist) in enumerate(self.adj_list[c1]):
            if neighbor == c2:
                self.adj_list[c1][i] = (c2, distance)
                updated = True
                break
        if not updated:
            self.adj_list[c1].append((c2, distance))

        if bidirectional:
            updated2 = False
            for i, (neighbor, dist) in enumerate(self.adj_list[c2]):
                if neighbor == c1:
                    self.adj_list[c2][i] = (c1, distance)
                    updated2 = True
                    break
            if not updated2:
                self.adj_list[c2].append((c1, distance))

        print(f"-> Road established: {c1} <---> {c2} ({distance} km)")
        return True

    def dijkstra_shortest_path(self, start_city: str, destination_city: str) -> Optional[Tuple[List[str], float]]:
        """
        Calculates the shortest route and total distance using Dijkstra's algorithm.
        Returns: (path_list, total_distance) or None if no path exists.
        """
        start = self._get_canonical_name(start_city)
        dest = self._get_canonical_name(destination_city)

        if not start or not dest:
            missing = []
            if not start: missing.append(start_city)
            if not dest: missing.append(destination_city)
            print(f"Error: City/cities not found: {', '.join(missing)}")
            return None

        if start == dest:
            return ([start], 0.0)

        # Distances and previous node pointers
        distances: Dict[str, float] = {city: float('inf') for city in self.adj_list}
        previous: Dict[str, Optional[str]] = {city: None for city in self.adj_list}
        distances[start] = 0.0

        # Priority queue stores tuples of (current_distance, city_name)
        pq: List[Tuple[float, str]] = [(0.0, start)]

        while pq:
            curr_dist, curr_city = heapq.heappop(pq)

            if curr_dist > distances[curr_city]:
                continue

            if curr_city == dest:
                break

            for neighbor, weight in self.adj_list[curr_city]:
                alt = curr_dist + weight
                if alt < distances[neighbor]:
                    distances[neighbor] = alt
                    previous[neighbor] = curr_city
                    heapq.heappush(pq, (alt, neighbor))

        # Check if destination reached
        if distances[dest] == float('inf'):
            return None

        # Reconstruct path from start to dest
        path = []
        curr = dest
        while curr:
            path.append(curr)
            curr = previous[curr]
        path.reverse()

        return (path, distances[dest])

    def display_network(self) -> None:
        """Displays all cities and connected road paths."""
        if not self.adj_list:
            print("Graph is currently empty. No cities added yet.")
            return

        print("\n" + "=" * 60)
        print("                   CITY ROAD NETWORK                        ")
        print("=" * 60)
        for city, neighbors in sorted(self.adj_list.items()):
            if not neighbors:
                print(f"• {city} -> [No outgoing roads]")
            else:
                connections = ", ".join([f"{nbr} ({dist} km)" for nbr, dist in neighbors])
                print(f"• {city} -> {connections}")
        print("=" * 60 + "\n")

    def load_demo_network(self) -> None:
        """Loads a default sample road network."""
        cities = ["Delhi", "Jaipur", "Agra", "Kanpur", "Lucknow", "Varanasi", "Mumbai", "Pune"]
        for c in cities:
            if not self._get_canonical_name(c):
                self.adj_list[c] = []

        roads = [
            ("Delhi", "Jaipur", 280),
            ("Delhi", "Agra", 240),
            ("Jaipur", "Agra", 240),
            ("Agra", "Kanpur", 278),
            ("Kanpur", "Lucknow", 90),
            ("Lucknow", "Varanasi", 310),
            ("Kanpur", "Varanasi", 330),
            ("Delhi", "Mumbai", 1400),
            ("Mumbai", "Pune", 150),
            ("Jaipur", "Mumbai", 1150)
        ]
        for c1, c2, d in roads:
            self.add_road(c1, c2, d)
        print("-> Sample demo road network loaded successfully!")


def main():
    graph = CityGraph()
    print("=" * 60)
    print("           CITY PATH FINDER - SHORT ROUTE PLANNER           ")
    print("=" * 60)

    while True:
        print("\nOperations:")
        print("1. Find Shortest Route between Two Cities (Dijkstra)")
        print("2. Add a New City (Node)")
        print("3. Connect Road between Cities (Weighted Edge)")
        print("4. View Entire City Network")
        print("5. Load Demo Map (Preloaded Cities & Routes)")
        print("6. Exit")

        try:
            choice = input("\nEnter choice (1-6): ").strip()
            if choice == "1":
                src = input("Enter Starting City: ").strip()
                dest = input("Enter Destination City: ").strip()
                result = graph.dijkstra_shortest_path(src, dest)
                if result:
                    path, dist = result
                    print("\n" + "-" * 50)
                    print("          SHORTEST ROUTE FOUND          ")
                    print("-" * 50)
                    print(f"Optimal Path: {' -> '.join(path)}")
                    print(f"Total Distance: {dist:.2f} km")
                    print("-" * 50)
                else:
                    print(f"\nNo viable route found connecting '{src}' and '{dest}'.")
            elif choice == "2":
                city_name = input("Enter city name: ").strip()
                graph.add_city(city_name)
            elif choice == "3":
                c1 = input("Enter First City: ").strip()
                c2 = input("Enter Second City: ").strip()
                try:
                    d = float(input("Enter Distance (in km): ").strip())
                    graph.add_road(c1, c2, d)
                except ValueError:
                    print("Invalid distance! Please enter a numerical value.")
            elif choice == "4":
                graph.display_network()
            elif choice == "5":
                graph.load_demo_network()
                graph.display_network()
            elif choice in ("6", "exit", "quit"):
                print("Exiting City Path Finder. Safe travels!")
                break
            else:
                print("Invalid choice. Please select an option from 1 to 6.")
        except (KeyboardInterrupt, EOFError):
            print("\nExiting.")
            break

if __name__ == "__main__":
    main()
