package scheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/*
 * Objective: Create a weekly scheduling application.
 * 
 * You may create any additional enums, classes, methods or variables needed
 * to accomplish the requirements below:
 * 
 * - You should use an array filled with enums for the days of the week and each
 *   enum should contain a LinkedList of events that includes a time and what is 
 *   happening at the event.
 * 
 * - The user should be able to interact with your application through the
 *   console and have the option to add events, view events or remove events by
 *   day.
 *   
 * - Each day's events should be sorted by chronological order.
 *  
 * - If the user tries to add an event on the same day and time as another event
 *   throw a SchedulingConflictException(created by you) that tells the user
 *   they tried to double book a time slot.
 *   
 * - Make sure any enums or classes you create have properly encapsulated member
 *   variables.
 */

public class Scheduler {

    public static void main(String[] args) {
        Map<DayOfWeek, LinkedList<Event>> schedule = initializeSchedule();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an action (Type number):");
            System.out.println("1. Add event");
            System.out.println("2. View events");
            System.out.println("3. Remove event");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    addEventInteraction(schedule, scanner);
                    break;
                case 2:
                    viewEventsInteraction(schedule, scanner);
                    break;
                case 3:
                    removeEventInteraction(schedule, scanner);
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    scanner.close();
                    return; 
                default:
                    System.out.println("Invalid choice. Please choose a valid action.");
            }
        }
    }

    // Initialize the schedule for each day
    public static Map<DayOfWeek, LinkedList<Event>> initializeSchedule() {
        Map<DayOfWeek, LinkedList<Event>> schedule = new HashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            schedule.put(day, new LinkedList<Event>());
        }
        return schedule;
    }
    
    // Method to interactively add an event
    public static void addEventInteraction(Map<DayOfWeek, LinkedList<Event>> schedule, Scanner scanner) {
        System.out.println("Enter day (SUNDAY, MONDAY, TUESDAY, etc.): ");
        DayOfWeek day = DayOfWeek.valueOf(scanner.nextLine().toUpperCase());

        System.out.println("Enter event title: ");
        String title = scanner.nextLine();

        System.out.println("Enter event time: ");
        String time = scanner.nextLine();

        try {
            Event event = new Event(title, time);
            addEvent(schedule, day, event);
            System.out.println("Event added successfully.");
        } catch (SchedulingConflictException e) {
            System.out.println("Scheduling conflict: " + e.getMessage());
        }
    }

    // Method to interactively view events for a day
    public static void viewEventsInteraction(Map<DayOfWeek, LinkedList<Event>> schedule, Scanner scanner) {
        System.out.println("Enter day (SUNDAY, MONDAY, TUESDAY, etc.): ");
        DayOfWeek day = DayOfWeek.valueOf(scanner.nextLine().toUpperCase());

        LinkedList<Event> daySchedule = schedule.get(day);
        if (daySchedule != null) {
            System.out.println("Events for " + day + ":");
            printEvents(daySchedule);
        } else {
            System.out.println("No events for " + day + ".");
        }
    }

    // Method to interactively remove an event
    public static void removeEventInteraction(Map<DayOfWeek, LinkedList<Event>> schedule, Scanner scanner) {
        System.out.println("Enter day (SUNDAY, MONDAY, TUESDAY, etc.): ");
        DayOfWeek day = DayOfWeek.valueOf(scanner.nextLine().toUpperCase());

        System.out.println("Enter position of the event to remove (Starting from 0 index): ");
        int position = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        removeEvent(schedule, day, position);
        System.out.println("Event removed.");
    }

    // Method to add an event to a specific day's schedule
    public static void addEvent(Map<DayOfWeek, LinkedList<Event>> schedule,
                                DayOfWeek day, Event event) throws SchedulingConflictException {
        LinkedList<Event> daySchedule = schedule.get(day);
        Node<Event> currentEventNode = daySchedule.getHead();

        while (currentEventNode != null) {
            if (currentEventNode.getValue().getTime().equals(event.getTime())) {
                throw new SchedulingConflictException("Time slot is already booked.");
            }
            currentEventNode = currentEventNode.getNext();
        }

        daySchedule.add(event);
    }

    // Method to print events for a specific day
    public static void printEvents(LinkedList<Event> daySchedule) {
        daySchedule.print();
    }

    // Method to remove an event from a specific day's schedule
    public static void removeEvent(Map<DayOfWeek, LinkedList<Event>> schedule,
                                   DayOfWeek day, int position) {
        LinkedList<Event> daySchedule = schedule.get(day);
        daySchedule.remove(position);
    }
}

class Event {
    private String title;
    private String time;

    public Event(String title, String time) {
        this.title = title;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return title + " at " + time;
    }
}

class SchedulingConflictException extends Exception {
    public SchedulingConflictException(String message) {
        super(message);
    }
}

enum DayOfWeek {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
}
