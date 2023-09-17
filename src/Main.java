import java.util.Scanner;

enum Order {
    ASCENDING,
    DESCENDING
}

public class Main {

    public static void main(String[] args) {

        System.out.println("Game");
        System.out.println();

        System.out.println("What do you want the size of the tower to be?");
        System.out.print("Input the size: ");

        Scanner scanner = new Scanner(System.in);

        int size = scanner.nextInt();

        if (size < 3) {
            System.out.println();
            System.out.println("Value must be at least 3.");
            System.exit(1);
        }

        System.out.println();

        System.out.println("Do you want to sort the tower ascending (A) or descending (D)?");
        System.out.print("Input an option: ");

        String option = scanner.next().toLowerCase();

        if (!option.equals("a") && !option.equals("d")) {
            System.out.println();
            System.out.println("Invalid option.");
            System.exit(1);
        }

        System.out.println();

        Order order = option.equals("a") ? Order.ASCENDING : Order.DESCENDING;

        Game game = new Game(size, order);
        game.loop();

    }

}

class Game {

    private final Stack stack1;
    private final Stack stack2;
    private final Stack stack3;

    private int moves;

    private final Order order;

    public Game(int size, Order order) {
        this.stack1 = new Stack(size);
        this.stack2 = new Stack(size);
        this.stack3 = new Stack(size);

        for (int i = size; i > 0; i--) {
            int value = (int) (Math.random() * 100 + 1);
            stack1.push(value);
        }

        this.order = order;
    }

    public void loop() {

        printAll();

        System.out.println();

        if (stack1.isFull() && stack1.isOrdered(order)) {
            System.out.println("You won in " + this.moves + " moves!");
            System.exit(0);
        }

        if (stack2.isFull() && stack2.isOrdered(order)) {
            System.out.println("You won in " + this.moves + " moves!");
            System.exit(0);
        }

        if (stack3.isFull() && stack3.isOrdered(order)) {
            System.out.println("You won in " + this.moves + " moves!");
            System.exit(0);
        }

        System.out.println("0. Exit");
        System.out.println("1. Make a move");
        System.out.println("2. Auto solve");

        System.out.println();
        System.out.print("Input an option: ");

        Scanner scanner = new Scanner(System.in);

        int option = scanner.nextInt();

        System.out.println();

        if (option == 0) {

            System.out.println("Bye!");
            System.exit(0);

        } else if (option == 1) {

            System.out.print("Input the source stack: ");
            String source = scanner.next().toLowerCase();

            System.out.print("Input the destination stack: ");
            String destination = scanner.next().toLowerCase();

            System.out.println();

            Stack sourceStack = source.equals("a") ? stack1 : source.equals("b") ? stack2 : stack3;
            Stack destinationStack = destination.equals("a") ? stack1 : destination.equals("b") ? stack2 : stack3;

            move(sourceStack, destinationStack);

        } else if (option == 2) {

            autoSolve(stack1, stack2, stack3);

        } else {
            System.out.println("Invalid option.");
        }

        System.out.println();
        loop();

    }

    public void autoSolve(Stack source, Stack auxiliary, Stack destination) {

        while (!source.isEmpty()) {
            int current = source.pop();

            if (this.order == Order.ASCENDING) {
                while (!destination.isEmpty() && destination.peek().getData() > current) {
                    move(destination, auxiliary);
                }
            } else {
                while (!destination.isEmpty() && destination.peek().getData() < current) {
                    move(destination, auxiliary);
                }
            }

            destination.push(current);

            while (!auxiliary.isEmpty()) {
                move(auxiliary, destination);
            }
        }

        while (!destination.isEmpty()) {
            move(destination, source);
        }

    }

    public void move(Stack from, Stack to) {

        int value = from.pop();

        if (value == -1) {
            System.out.println("Invalid move.");
            return;
        }

        to.push(value);

        this.moves++;

        String fromLabel = from == stack1 ? "A" : from == stack2 ? "B" : "C";
        String toLabel = to == stack1 ? "A" : to == stack2 ? "B" : "C";

        System.out.println("Move " + this.moves + ": " + value + " from " + fromLabel + " to " + toLabel);

    }

    public void printAll() {

        System.out.print("A: ");
        stack1.print();

        System.out.print("B: ");
        stack2.print();

        System.out.print("C: ");
        stack3.print();

    }

}

class Stack {

    private Node top;
    private int size;
    private final int maxSize;

    public Stack(int maxSize) {
        this.top = null;
        this.maxSize = maxSize;
    }

    public Node peek() {
        return this.top;
    }

    public void push(int value) {

        if (this.isFull()) { return; }

        Node newNode = new Node(value);

        newNode.setNext(this.top);
        this.top = newNode;

        this.size++;

    }

    public int pop() {

        if (this.isEmpty()) { return -1; }

        int topValue = this.top.getData();
        this.top = this.top.getNext();

        this.size--;

        return topValue;

    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean isFull() {
        return this.size == this.maxSize;
    }

    public void print() {

        Node current = this.top;

        while (current != null) {
            System.out.print(current.getData() + " ");
            current = current.getNext();
        }

        System.out.println();

    }

    public boolean isOrdered(Order order) {

        Node current = this.top;

        boolean ordered = true;

        while (current != null) {

            if (current.getNext() != null) {

                if (order == Order.ASCENDING) {

                    if (current.getData() > current.getNext().getData()) {
                        ordered = false;
                        break;
                    }

                } else {

                    if (current.getData() < current.getNext().getData()) {
                        ordered = false;
                        break;
                    }

                }

            }

            current = current.getNext();

        }

        return ordered;

    }

}

class Node {

    private int data;
    private Node next;

    public Node(int data) {
        this.data = data;
        this.next = null;
    }

    public int getData() {
        return this.data;
    }

    public Node getNext() {
        return this.next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

}