# City Path Finder

A mini project to model road networks between cities as a weighted graph and compute the shortest routes using **Dijkstra's Algorithm**.

---

## 📌 Features
- **Graph Representation**:
  - Cities are modeled as **Nodes/Vertices**.
  - Roads between cities are modeled as **Weighted Edges** (distances in km).
- **Shortest Path Calculation (Dijkstra's Algorithm)**:
  - Uses min-priority queues (`heapq` in Python, `PriorityQueue` in Java) for optimal $O((V + E) \log V)$ time complexity.
  - Reconstructs and displays the complete step-by-step route and total distance.
- **Dynamic Operations**:
  - Add cities.
  - Connect roads with custom distances.
  - Display the full road network.
  - Built-in demo map with major Indian cities and routes.
- Implementations available in **Python** and **Java**.

---

## 🚀 How to Run

### 1. Python (`city_path_finder.py`)
```bash
python city_path_finder.py
```

### 2. Java (`CityPathFinder.java`)
```bash
javac CityPathFinder.java
java CityPathFinder
```

---

## 💻 Sample Execution
```text
============================================================
           CITY PATH FINDER - SHORT ROUTE PLANNER           
============================================================

Operations:
1. Find Shortest Route between Two Cities (Dijkstra)
2. Add a New City (Node)
3. Connect Road between Cities (Weighted Edge)
4. View Entire City Network
5. Load Demo Map (Preloaded Cities & Routes)
6. Exit

Enter choice (1-6): 5
-> Sample demo road network loaded successfully!

Enter choice (1-6): 1
Enter Starting City: Delhi
Enter Destination City: Varanasi

--------------------------------------------------
          SHORTEST ROUTE FOUND          
--------------------------------------------------
Optimal Path: Delhi -> Agra -> Kanpur -> Lucknow -> Varanasi
Total Distance: 918.00 km
--------------------------------------------------
```
