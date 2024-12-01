void main() {
    System.out.println("Welcome to modern Java!");
    showCurrentTime();
}

void showCurrentTime() {
    System.out.println("Current time: " + java.time.LocalTime.now());
}
