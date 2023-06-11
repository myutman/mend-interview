package com.mend.interview;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import com.mend.interview.Solution;

import java.util.ArrayList;
import java.util.function.Function;

public class SolutionTest {
    @Test
    public void testSolution() {
        ArrayList<Solution.Edge> edges = new ArrayList<>();
        edges.add(new Solution.Edge(1, 3, "red"));
        Solution.TaskInput input = new Solution.TaskInput(3, 1, edges);
        JSONArray answer = new JSONArray(Solution.solve(input));
        Assertions.assertEquals(answer.toString(), "[0,-1,1]");
    }

    @Test
    public void testSolutionTwoRedEdges() {
        ArrayList<Solution.Edge> edges = new ArrayList<>();
        edges.add(new Solution.Edge(1, 2, "blue"));
        edges.add(new Solution.Edge(1, 3, "red"));
        edges.add(new Solution.Edge(2, 4, "red"));
        edges.add(new Solution.Edge(3, 4, "blue"));
        edges.add(new Solution.Edge(4, 5, "red"));
        Solution.TaskInput input = new Solution.TaskInput(5, 1, edges);
        JSONArray answer = new JSONArray(Solution.solve(input));
        Assertions.assertEquals(answer.toString(), "[0,1,1,2,-1]");
    }

    @Test
    public void testSolutionOneRedEdge() {
        ArrayList<Solution.Edge> edges = new ArrayList<>();
        edges.add(new Solution.Edge(1, 2, "blue"));
        edges.add(new Solution.Edge(1, 3, "red"));
        edges.add(new Solution.Edge(2, 4, "red"));
        edges.add(new Solution.Edge(3, 4, "blue"));
        edges.add(new Solution.Edge(4, 5, "blue"));
        Solution.TaskInput input = new Solution.TaskInput(5, 1, edges);
        JSONArray answer = new JSONArray(Solution.solve(input));
        Assertions.assertEquals(answer.toString(), "[0,1,1,2,3]");
    }

    @Test
    public void testSolutionMoreEdges() {
        ArrayList<Solution.Edge> edges = new ArrayList<>();
        edges.add(new Solution.Edge(1, 2, "blue"));
        edges.add(new Solution.Edge(1, 3, "red"));
        edges.add(new Solution.Edge(2, 4, "red"));
        edges.add(new Solution.Edge(3, 4, "blue"));
        edges.add(new Solution.Edge(4, 5, "red"));
        edges.add(new Solution.Edge(2, 6, "blue"));
        edges.add(new Solution.Edge(6, 7, "blue"));
        edges.add(new Solution.Edge(7, 8, "blue"));
        edges.add(new Solution.Edge(8, 5, "blue"));
        edges.add(new Solution.Edge(3, 5, "red"));

        Solution.TaskInput input = new Solution.TaskInput(8, 1, edges);
        JSONArray answer = new JSONArray(Solution.solve(input));
        Assertions.assertEquals(answer.toString(), "[0,1,1,2,5,2,3,4]");
    }
}
