void main(String[] args) {
    if (args.length == 0) {
        System.out.println("Please provide your name as an argument");
        return;
    }

    String name = args[0];
    greet(name);
    showInfo();
}

void greet(String name) {
    System.out.println("Hello, " + name + "!");
}

void showInfo() {
    System.out.println("This is a modern Java program with:");
    System.out.println("- Unnamed class");
    System.out.println("- Instance main");
    System.out.println("- Command-line arguments");
}
