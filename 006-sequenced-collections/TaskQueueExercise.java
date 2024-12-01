import java.util.ArrayList;
import java.util.LinkedList;
import java.util.SequencedCollection;

public class TaskQueueExercise {

    record Task(String id, String description, int priority) {
        @Override
        public String toString() {
            return "[%s] %s (Priority: %d)".formatted(
                    id,
                    description,
                    priority
            );
        }
    }

    static class TaskQueue {
        private final SequencedCollection<Task> tasks;

        public TaskQueue(SequencedCollection<Task> tasks) {
            this.tasks = tasks;
        }

        public void addUrgentTask(Task task) {
            tasks.addFirst(task);
            System.out.println("Added urgent task: " + task);
        }

        public void addRegularTask(Task task) {
            tasks.addLast(task);
            System.out.println("Added regular task: " + task);
        }

        public Task processNextTask() {
            if (tasks.isEmpty())
                return null;
            Task task = tasks.removeFirst();
            System.out.println("Processing next task: " + task);
            return task;
        }

        public Task processLastTask() {
            if (tasks.isEmpty())
                return null;
            Task task = tasks.removeLast();
            System.out.println("Processing last task: " + task);
            return task;
        }

        public SequencedCollection<Task> getReversedView() { return tasks.reversed(); }

        public void modifyAndShowSync() {
            System.out.println("\nDemonstrating view synchronization:");
            var reversed = getReversedView();

            System.out.println("Original order:");
            tasks.forEach(System.out::println);

            System.out.println("Reversed order:");
            reversed.forEach(System.out::println);

            System.out.println("Adding new task...");
            addUrgentTask(new Task("URG3", "Urgent Third Task", 1));

            System.out.println("Reversed order after modification:");
            reversed.forEach(System.out::println);
        }
    }

    public static void main(String[] args) {
        // Test with ArrayList
        System.out.println("Testing with ArrayList:");
        TaskQueue arrayListQueue = new TaskQueue(new ArrayList<>());
        testTaskQueue(arrayListQueue);

        System.out.println("\nTesting with LinkedList:");
        TaskQueue linkedListQueue = new TaskQueue(new LinkedList<>());
        testTaskQueue(linkedListQueue);
    }

    private static void testTaskQueue(TaskQueue queue) {
        // Add tasks from both ends
        queue.addRegularTask(new Task("REG1", "Regular Task 1", 3));
        queue.addUrgentTask(new Task("URG1", "Urgent Task 1", 1));
        queue.addRegularTask(new Task("REG2", "Regular Task 2", 3));
        queue.addUrgentTask(new Task("URG2", "Urgent Task 2", 1));

        // Process some tasks
        queue.processNextTask();  // Should get URG2
        queue.processLastTask();  // Should get REG2

        // Demonstrate view synchronization
        queue.modifyAndShowSync();
    }

}
