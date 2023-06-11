package com.mend.interview;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Solution {
    enum Color {
        RED,
        BLUE
    }

    static class Edge {
        final int v1;
        final int v2;
        final Color color;

        Edge(int v1, int v2, String color) {
            this.v1 = v1;
            this.v2 = v2;
            if ("red".equals(color)) {
                this.color = Color.RED;
            } else {
                this.color = Color.BLUE;
            }
        }
    }

    static class TaskInput {
        final int nNodes;
        final int start;
        final ArrayList<Edge> edges;

        TaskInput(int nNodes, int start, ArrayList<Edge> edges) {
            this.nNodes = nNodes;
            this.start = start;
            this.edges = edges;
        }
    }

    static class Node {
        int originalNumber;
        int layer;

        Node(int originalNumber, int layer) {
            this.originalNumber = originalNumber;
            this.layer = layer;
        }

        @Override
        public int hashCode() {
            return this.originalNumber * 2 + this.layer;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (getClass() != obj.getClass()) return false;

            Node other = (Node) obj;

            return this.originalNumber == other.originalNumber && this.layer == other.layer;
        }
    }

    static ArrayList<Integer> solve(TaskInput taskInput) {
        int nNodes = taskInput.nNodes;
        int start = taskInput.start;
        HashMap<Node, Integer> distances = new HashMap<>();
        HashMap<Node, ArrayList<Node>> edges = new HashMap<>();

        for (Edge edge: taskInput.edges) {
            Node v10 = new Node(edge.v1, 0);
            Node v11 = new Node(edge.v1, 1);
            Node v20 = new Node(edge.v2, 0);
            Node v21 = new Node(edge.v2, 1);

            for (Node v: new Node[]{v10, v11, v20, v21}) {
                if (!edges.containsKey(v)) {
                    edges.put(v, new ArrayList<>());
                }
            }

            if (edge.color == Color.RED) {
                edges.get(v10).add(v21);
                edges.get(v20).add(v11);
            } else {
                edges.get(v10).add(v20);
                edges.get(v20).add(v10);
                edges.get(v11).add(v21);
                edges.get(v21).add(v11);
            }
        }

        ArrayDeque<Node> queue = new ArrayDeque<>();

        Node startNode = new Node(start, 0);
        queue.addLast(startNode);
        distances.put(startNode, 0);

        while (!queue.isEmpty()) {
            Node v = queue.pollFirst();
            for (Node v1: edges.get(v)) {
                if (!distances.containsKey(v1)) {
                    distances.put(v1, distances.get(v) + 1);
                    queue.addLast(v1);
                }
            }
        }

        ArrayList<Integer> answer = new ArrayList<>();
        for (int index = 1; index <= nNodes; index++) {
            Node v0 = new Node(index, 0);
            Node v1 = new Node(index, 1);
            int distance = Math.min(distances.getOrDefault(v0, 2 * nNodes + 1), distances.getOrDefault(v1, 2 * nNodes + 1));
            if (distance < 2 * nNodes) {
                answer.add(distance);
            } else {
                answer.add(-1);
            }
        }

        return answer;
    }
}
