import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TimeTrackingExercise {

    record TimeEntry(String activity, LocalDateTime start, Duration duration) {

        @Override
        public String toString() {
            return "%s: %s for %s".formatted(
                    start.truncatedTo(ChronoUnit.MINUTES),
                    activity,
                    formatDuration(duration)
            );
        }

        private String formatDuration(Duration duration) {
            if (duration.toHours() > 0) {
                return "%dh %dm".formatted(
                        duration.toHours(),
                        duration.toMinutesPart()
                );
            }
            return "%dm".formatted(duration.toMinutes());
        }
    }

    static class ActivityTracker {
        public Duration roundToQuarterHour(Duration duration) {
            long minutes = duration.toMinutes();
            long quarterHours = Math.round(minutes / 15.0);
            return Duration.ofMinutes(quarterHours * 15);
        }

        public String formatDuration(Duration duration) {
            if (duration.isZero())
                return "0m";
            if (duration.isNegative()) {
                return "-" + formatDuration(duration.abs());
            }

            long hours = duration.toHours();
            int minutes = duration.toMinutesPart();

            if (hours > 0) {
                return minutes > 0 ? "%dh %dm".formatted(hours, minutes) : "%dh".formatted(hours);
            }
            return "%dm".formatted(minutes);
        }

        public Duration calculateTotal(List<TimeEntry> entries) {
            return entries.stream()
                    .map(TimeEntry::duration)
                    .reduce(Duration.ZERO, Duration::plus);
        }

        public List<Duration> findGaps(List<TimeEntry> entries) {
            if (entries.size() < 2)
                return List.of();

            List<Duration> gaps = new ArrayList<>();
            var sortedEntries = entries.stream()
                    .sorted(Comparator.comparing(TimeEntry::start))
                    .toList();

            for (int i = 0; i < sortedEntries.size() - 1; i++) {
                TimeEntry current = sortedEntries.get(i);
                TimeEntry next = sortedEntries.get(i + 1);

                LocalDateTime currentEnd = current.start()
                        .plus(current.duration());
                Duration gap = Duration.between(currentEnd, next.start());

                if (!gap.isZero() && !gap.isNegative()) {
                    gaps.add(gap);
                }
            }

            return gaps;
        }
    }

    public static void main(String[] args) {
        ActivityTracker tracker = new ActivityTracker();

        // Create sample time entries
        List<TimeEntry> entries = List.of(
                new TimeEntry(
                        "Coding",
                        LocalDateTime.now().minusHours(3),
                        Duration.ofMinutes(90)
                ),
                new TimeEntry(
                        "Meeting",
                        LocalDateTime.now().minusHours(1),
                        Duration.ofMinutes(45)
                ),
                new TimeEntry(
                        "Documentation",
                        LocalDateTime.now(),
                        Duration.ofMinutes(30)
                )
        );

        // Demonstrate duration rounding
        System.out.println("Original vs Rounded Durations:");
        for (TimeEntry entry : entries) {
            System.out.printf(
                    "%s -> %s%n",
                    tracker.formatDuration(entry.duration()),
                    tracker.formatDuration(
                            tracker.roundToQuarterHour(entry.duration())
                    )
            );
        }

        // Show gap analysis
        System.out.println("\nGaps between activities:");
        List<Duration> gaps = tracker.findGaps(entries);
        gaps.forEach(gap -> System.out.println("Gap: " + tracker.formatDuration(gap)));

        // Display total time
        Duration total = tracker.calculateTotal(entries);
        System.out.println(
                "\nTotal time: " +
                        tracker.formatDuration(total)
        );
    }
}
