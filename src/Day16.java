import aoc.Day;
import util.Coordinate;
import util.DenseGrid;
import util.Direction;
import util.Pair;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

@SuppressWarnings("unused")
public class Day16 extends Day {

    private final Map<String, Long> cache = new HashMap<>();

    @Override
    protected Long partOne(List<String> input) {
        var map = DenseGrid.fromInput(input).getFirst();
        var start = map.find(c -> c == 'S');
        var end = map.find(c -> c == 'E');

        return (long) dijkstra(map, start, end);
    }

    @Override
    protected Long partTwo(List<String> input) {
        return null;
    }

    int dijkstra(DenseGrid<Character> map, Coordinate start, Coordinate end) {
        var seen = new HashSet<Node>();
        var score = new HashMap<Node, Integer>();
        var parents = new HashMap<Coordinate, Pair<Integer, List<Coordinate>>>();

        var fringe = new PriorityQueue<>(Comparator.comparingInt(State::score));
        fringe.add(new State(new Node(start, Direction.RT), 0));

        while (!fringe.isEmpty()) {
            var current = fringe.poll();
            if (seen.contains(current.node()) || current.score() == -1) {
                continue;
            }
            seen.add(current.node());

            if (!score.containsKey(current.node()) || current.score() < score.get(current.node())) {
                score.put(current.node(), current.score());
            }

            fringe.add(current.advance(map));
            fringe.add(current.turnClockwise());
            fringe.add(current.turnCounterClockwise());
        }

        return score.entrySet().stream()
                .filter(e -> e.getKey().position().equals(end))
                .mapToInt(Map.Entry::getValue)
                .min()
                .orElse(0);
    }

    record State(Node node, int score) {
        State advance(DenseGrid<Character> map) {
            var newNode = node.advance();
            if (!map.contains(newNode.position()) || map.get(newNode.position()) == '#') {
                return new State(newNode, -1);
            }
            return new State(newNode, score + 1);
        }

        State turnClockwise() {
            return new State(node.turnClockwise(), score + 1000);
        }

        State turnCounterClockwise() {
            return new State(node.turnCounterClockwise(), score + 1000);
        }
    }

    record Node(Coordinate position, Direction direction) {
        Node advance() {
            return new Node(position.move(direction), direction);
        }

        Node turnClockwise() {
            return new Node(position, direction.turnClockwise());
        }

        Node turnCounterClockwise() {
            return new Node(position, direction.turnCounterClockwise());
        }
    }

}
